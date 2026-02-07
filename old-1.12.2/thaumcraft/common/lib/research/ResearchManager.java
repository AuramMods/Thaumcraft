package thaumcraft.common.lib.research;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.internal.CommonInternals;
import thaumcraft.api.research.ResearchAddendum;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchEntry;
import thaumcraft.api.research.ResearchStage;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketKnowledgeGain;

public class ResearchManager {
   public static ConcurrentHashMap<String, Boolean> syncList = new ConcurrentHashMap();
   public static boolean noFlags = false;
   public static LinkedHashSet<Integer> craftingReferences = new LinkedHashSet();

   public static boolean addKnowledge(EntityPlayer player, IPlayerKnowledge.EnumKnowledgeType type, ResearchCategory category, int amount) {
      IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
      if (!type.hasFields()) {
         category = null;
      }

      int kp = knowledge.getKnowledge(type, category);
      knowledge.addKnowledge(type, category, amount);
      int kr = knowledge.getKnowledge(type, category) - kp;
      if (amount > 0) {
         for(int a = 0; a < kr; ++a) {
            PacketHandler.INSTANCE.sendTo(new PacketKnowledgeGain((byte)type.ordinal(), category == null ? null : category.key), (EntityPlayerMP)player);
         }
      }

      syncList.put(player.func_70005_c_(), true);
      return true;
   }

   public static boolean completeResearch(EntityPlayer player, String researchkey, boolean sync) {
      boolean b;
      for(b = false; progressResearch(player, researchkey, sync); b = true) {
      }

      return b;
   }

   public static boolean completeResearch(EntityPlayer player, String researchkey) {
      boolean b;
      for(b = false; progressResearch(player, researchkey, true); b = true) {
      }

      return b;
   }

   public static boolean startResearchWithPopup(EntityPlayer player, String researchkey) {
      boolean b = progressResearch(player, researchkey, true);
      if (b) {
         IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
         knowledge.setResearchFlag(researchkey, IPlayerKnowledge.EnumResearchFlag.POPUP);
         knowledge.setResearchFlag(researchkey, IPlayerKnowledge.EnumResearchFlag.RESEARCH);
      }

      return b;
   }

   public static boolean progressResearch(EntityPlayer player, String researchkey) {
      return progressResearch(player, researchkey, true);
   }

   public static boolean progressResearch(EntityPlayer player, String researchkey, boolean sync) {
      IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
      if (!knowledge.isResearchComplete(researchkey) && doesPlayerHaveRequisites(player, researchkey)) {
         if (!knowledge.isResearchKnown(researchkey)) {
            knowledge.addResearch(researchkey);
         }

         ResearchEntry re = ResearchCategories.getResearch(researchkey);
         int cs;
         int w2;
         int var18;
         if (re != null) {
            boolean popups = true;
            int warp;
            if (re.getStages() != null) {
               addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.EPIPHANY, (ResearchCategory)null, 1);
               cs = knowledge.getResearchStage(researchkey);
               ResearchStage currentStage = null;
               if (cs > 0) {
                  currentStage = re.getStages()[cs - 1];
               }

               if (re.getStages().length == 1 && cs == 0 && re.getStages()[0].getCraft() == null && re.getStages()[0].getObtain() == null && re.getStages()[0].getKnow() == null && re.getStages()[0].getResearch() == null) {
                  ++cs;
               } else if (re.getStages().length > 1 && re.getStages().length <= cs + 1 && cs < re.getStages().length && re.getStages()[cs].getCraft() == null && re.getStages()[cs].getObtain() == null && re.getStages()[cs].getKnow() == null && re.getStages()[cs].getResearch() == null) {
                  ++cs;
               }

               knowledge.setResearchStage(researchkey, Math.min(re.getStages().length + 1, cs + 1));
               popups = cs >= re.getStages().length;
               if (currentStage != null) {
                  warp = currentStage.getWarp();
                  if (warp > 0 && !Config.wuss && !player.field_70170_p.field_72995_K) {
                     addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.EPIPHANY, (ResearchCategory)null, 1);
                     if (warp > 1) {
                        IPlayerWarp pw = ThaumcraftCapabilities.getWarp(player);
                        w2 = warp / 2;
                        if (warp - w2 > 0) {
                           ThaumcraftApi.internalMethods.addWarpToPlayer(player, warp - w2, IPlayerWarp.EnumWarpType.PERMANENT);
                        }

                        if (w2 > 0) {
                           ThaumcraftApi.internalMethods.addWarpToPlayer(player, w2, IPlayerWarp.EnumWarpType.NORMAL);
                        }
                     } else {
                        ThaumcraftApi.internalMethods.addWarpToPlayer(player, warp, IPlayerWarp.EnumWarpType.PERMANENT);
                     }
                  }
               }
            }

            if (sync && popups) {
               knowledge.setResearchFlag(researchkey, IPlayerKnowledge.EnumResearchFlag.POPUP);
               if (!noFlags) {
                  knowledge.setResearchFlag(researchkey, IPlayerKnowledge.EnumResearchFlag.RESEARCH);
               } else {
                  noFlags = false;
               }

               if (re.getRewardItem() != null) {
                  ItemStack[] var16 = re.getRewardItem();
                  var18 = var16.length;

                  for(warp = 0; warp < var18; ++warp) {
                     ItemStack rs = var16[warp];
                     if (!player.field_71071_by.func_70441_a(rs.func_77946_l())) {
                        player.func_70099_a(rs.func_77946_l(), 1.0F);
                     }
                  }
               }

               if (re.getRewardKnow() != null) {
                  ResearchStage.Knowledge[] var17 = re.getRewardKnow();
                  var18 = var17.length;

                  for(warp = 0; warp < var18; ++warp) {
                     ResearchStage.Knowledge rk = var17[warp];
                     addKnowledge(player, rk.type, rk.category, rk.type.getProgression() * rk.amount);
                  }
               }
            }
         }

         if (re != null && re.getSiblings() != null) {
            String[] var14 = re.getSiblings();
            cs = var14.length;

            for(var18 = 0; var18 < cs; ++var18) {
               String sibling = var14[var18];
               if (!knowledge.isResearchComplete(sibling) && doesPlayerHaveRequisites(player, sibling)) {
                  completeResearch(player, sibling, sync);
               }
            }
         }

         Iterator var15 = ResearchCategories.researchCategories.keySet().iterator();

         label142:
         while(var15.hasNext()) {
            String rc = (String)var15.next();
            Iterator var20 = ResearchCategories.getResearchCategory(rc).research.values().iterator();

            while(true) {
               while(true) {
                  ResearchEntry ri;
                  do {
                     do {
                        do {
                           if (!var20.hasNext()) {
                              continue label142;
                           }

                           ri = (ResearchEntry)var20.next();
                        } while(ri == null);
                     } while(ri.getAddenda() == null);
                  } while(!knowledge.isResearchComplete(ri.getKey()));

                  ResearchAddendum[] var25 = ri.getAddenda();
                  w2 = var25.length;

                  for(int var11 = 0; var11 < w2; ++var11) {
                     ResearchAddendum addendum = var25[var11];
                     if (addendum.getResearch() != null && Arrays.asList(addendum.getResearch()).contains(researchkey)) {
                        ITextComponent text = new TextComponentTranslation("tc.addaddendum", new Object[]{ri.getLocalizedName()});
                        player.func_145747_a(text);
                        knowledge.setResearchFlag(ri.getKey(), IPlayerKnowledge.EnumResearchFlag.PAGE);
                        break;
                     }
                  }
               }
            }
         }

         if (sync) {
            syncList.put(player.func_70005_c_(), true);
            if (re != null) {
               player.func_71023_q(5);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean doesPlayerHaveRequisites(EntityPlayer player, String key) {
      ResearchEntry ri = ResearchCategories.getResearch(key);
      if (ri == null) {
         return true;
      } else {
         String[] parents = ri.getParentsStripped();
         return parents != null ? ThaumcraftCapabilities.knowsResearchStrict(player, parents) : true;
      }
   }

   public static Aspect getCombinationResult(Aspect aspect1, Aspect aspect2) {
      Collection<Aspect> aspects = Aspect.aspects.values();
      Iterator var3 = aspects.iterator();

      Aspect aspect;
      do {
         do {
            if (!var3.hasNext()) {
               return null;
            }

            aspect = (Aspect)var3.next();
         } while(aspect.getComponents() == null);
      } while((aspect.getComponents()[0] != aspect1 || aspect.getComponents()[1] != aspect2) && (aspect.getComponents()[0] != aspect2 || aspect.getComponents()[1] != aspect1));

      return aspect;
   }

   public static void parseAllResearch() {
      JsonParser parser = new JsonParser();
      Iterator var1 = CommonInternals.jsonLocs.values().iterator();

      while(true) {
         while(var1.hasNext()) {
            ResourceLocation loc = (ResourceLocation)var1.next();
            String s = "/assets/" + loc.func_110624_b() + "/" + loc.func_110623_a();
            if (!s.endsWith(".json")) {
               s = s + ".json";
            }

            InputStream stream = ResearchManager.class.getResourceAsStream(s);
            if (stream != null) {
               try {
                  InputStreamReader reader = new InputStreamReader(stream);
                  JsonObject obj = parser.parse(reader).getAsJsonObject();
                  JsonArray entries = obj.get("entries").getAsJsonArray();
                  int a = 0;
                  Iterator var9 = entries.iterator();

                  while(var9.hasNext()) {
                     JsonElement element = (JsonElement)var9.next();
                     ++a;

                     try {
                        JsonObject entry = element.getAsJsonObject();
                        ResearchEntry researchEntry = parseResearchJson(entry);
                        addResearchToCategory(researchEntry);
                     } catch (Exception var13) {
                        var13.printStackTrace();
                        Thaumcraft.log.warn("Invalid research entry [" + a + "] found in " + loc.toString());
                        --a;
                     }
                  }

                  Thaumcraft.log.info("Loaded " + a + " research entries from " + loc.toString());
               } catch (Exception var14) {
                  Thaumcraft.log.warn("Invalid research file: " + loc.toString());
               }
            } else {
               Thaumcraft.log.warn("Research file not found: " + loc.toString());
            }
         }

         return;
      }
   }

   private static ResearchEntry parseResearchJson(JsonObject obj) throws Exception {
      ResearchEntry entry = new ResearchEntry();
      entry.setKey(obj.getAsJsonPrimitive("key").getAsString());
      if (entry.getKey() == null) {
         throw new Exception("Invalid key in research JSon");
      } else {
         entry.setName(obj.getAsJsonPrimitive("name").getAsString());
         entry.setCategory(obj.getAsJsonPrimitive("category").getAsString());
         if (entry.getCategory() == null) {
            throw new Exception("Invalid category in research JSon");
         } else {
            String[] sl;
            if (obj.has("icons")) {
               sl = arrayJsonToString(obj.get("icons").getAsJsonArray());
               if (sl != null && sl.length > 0) {
                  Object[] ir = new Object[sl.length];

                  for(int a = 0; a < sl.length; ++a) {
                     ItemStack stack = parseJSONtoItemStack(sl[a]);
                     if (stack != null) {
                        ir[a] = stack;
                     } else if (sl[a].startsWith("focus")) {
                        ir[a] = sl[a];
                     } else {
                        ir[a] = new ResourceLocation(sl[a]);
                     }
                  }

                  entry.setIcons(ir);
               }
            }

            if (obj.has("parents")) {
               entry.setParents(arrayJsonToString(obj.get("parents").getAsJsonArray()));
            }

            if (obj.has("siblings")) {
               entry.setSiblings(arrayJsonToString(obj.get("siblings").getAsJsonArray()));
            }

            int var6;
            String s;
            ArrayList stages;
            String[] var19;
            int var20;
            if (obj.has("meta")) {
               sl = arrayJsonToString(obj.get("meta").getAsJsonArray());
               if (sl != null && sl.length > 0) {
                  stages = new ArrayList();
                  var19 = sl;
                  var20 = sl.length;

                  for(var6 = 0; var6 < var20; ++var6) {
                     s = var19[var6];
                     ResearchEntry.EnumResearchMeta en = ResearchEntry.EnumResearchMeta.valueOf(s.toUpperCase());
                     if (en == null) {
                        throw new Exception("Illegal metadata in research JSon");
                     }

                     stages.add(en);
                  }

                  entry.setMeta((ResearchEntry.EnumResearchMeta[])stages.toArray(new ResearchEntry.EnumResearchMeta[stages.size()]));
               }
            }

            if (obj.has("location")) {
               Integer[] location = arrayJsonToInt(obj.get("location").getAsJsonArray());
               if (location != null && location.length == 2) {
                  entry.setDisplayColumn(location[0]);
                  entry.setDisplayRow(location[1]);
               }
            }

            if (obj.has("reward_item")) {
               entry.setRewardItem(parseJsonItemList(entry.getKey(), arrayJsonToString(obj.get("reward_item").getAsJsonArray())));
            }

            if (obj.has("reward_knowledge")) {
               sl = arrayJsonToString(obj.get("reward_knowledge").getAsJsonArray());
               if (sl != null && sl.length > 0) {
                  stages = new ArrayList();
                  var19 = sl;
                  var20 = sl.length;

                  for(var6 = 0; var6 < var20; ++var6) {
                     s = var19[var6];
                     ResearchStage.Knowledge k = ResearchStage.Knowledge.parse(s);
                     if (k != null) {
                        stages.add(k);
                     }
                  }

                  if (stages.size() > 0) {
                     entry.setRewardKnow((ResearchStage.Knowledge[])stages.toArray(new ResearchStage.Knowledge[stages.size()]));
                  }
               }
            }

            JsonArray stagesJson = obj.get("stages").getAsJsonArray();
            stages = new ArrayList();

            ResearchStage stage;
            for(Iterator var21 = stagesJson.iterator(); var21.hasNext(); stages.add(stage)) {
               JsonElement element = (JsonElement)var21.next();
               JsonObject stageObj = element.getAsJsonObject();
               stage = new ResearchStage();
               stage.setText(stageObj.getAsJsonPrimitive("text").getAsString());
               if (stage.getText() == null) {
                  throw new Exception("Illegal stage text in research JSon");
               }

               if (stageObj.has("recipes")) {
                  stage.setRecipes(arrayJsonToString(stageObj.get("recipes").getAsJsonArray()));
               }

               if (stageObj.has("required_item")) {
                  stage.setObtain(parseJsonItemList(entry.getKey(), arrayJsonToString(stageObj.get("required_item").getAsJsonArray())));
               }

               int var12;
               String[] sl;
               if (stageObj.has("required_craft")) {
                  sl = arrayJsonToString(stageObj.get("required_craft").getAsJsonArray());
                  stage.setCraft(parseJsonItemList(entry.getKey(), sl));
                  if (stage.getCraft() != null && stage.getCraft().length > 0) {
                     int[] refs = new int[stage.getCraft().length];
                     int q = 0;
                     ItemStack[] var11 = stage.getCraft();
                     var12 = var11.length;

                     for(int var13 = 0; var13 < var12; ++var13) {
                        ItemStack stack = var11[var13];
                        int code = createItemStackHash(stack);
                        craftingReferences.add(code);
                        refs[q] = code;
                        ++q;
                     }

                     stage.setCraftReference(refs);
                  }
               }

               if (stageObj.has("required_knowledge")) {
                  sl = arrayJsonToString(stageObj.get("required_knowledge").getAsJsonArray());
                  if (sl != null && sl.length > 0) {
                     ArrayList<ResearchStage.Knowledge> kl = new ArrayList();
                     String[] var34 = sl;
                     int var35 = sl.length;

                     for(var12 = 0; var12 < var35; ++var12) {
                        String s = var34[var12];
                        ResearchStage.Knowledge k = ResearchStage.Knowledge.parse(s);
                        if (k != null) {
                           kl.add(k);
                        }
                     }

                     if (kl.size() > 0) {
                        stage.setKnow((ResearchStage.Knowledge[])kl.toArray(new ResearchStage.Knowledge[kl.size()]));
                     }
                  }
               }

               if (stageObj.has("required_research")) {
                  stage.setResearch(arrayJsonToString(stageObj.get("required_research").getAsJsonArray()));
               }

               if (stageObj.has("warp")) {
                  stage.setWarp(stageObj.getAsJsonPrimitive("warp").getAsInt());
               }
            }

            if (stages.size() > 0) {
               entry.setStages((ResearchStage[])stages.toArray(new ResearchStage[stages.size()]));
            }

            if (obj.get("addenda") != null) {
               JsonArray addendaJson = obj.get("addenda").getAsJsonArray();
               ArrayList<ResearchAddendum> addenda = new ArrayList();

               ResearchAddendum addendum;
               for(Iterator var27 = addendaJson.iterator(); var27.hasNext(); addenda.add(addendum)) {
                  JsonElement element = (JsonElement)var27.next();
                  JsonObject addendumObj = element.getAsJsonObject();
                  addendum = new ResearchAddendum();
                  addendum.setText(addendumObj.getAsJsonPrimitive("text").getAsString());
                  if (addendum.getText() == null) {
                     throw new Exception("Illegal addendum text in research JSon");
                  }

                  if (addendumObj.has("recipes")) {
                     addendum.setRecipes(arrayJsonToString(addendumObj.get("recipes").getAsJsonArray()));
                  }

                  if (addendumObj.has("required_research")) {
                     addendum.setResearch(arrayJsonToString(addendumObj.get("required_research").getAsJsonArray()));
                  }
               }

               if (addenda.size() > 0) {
                  entry.setAddenda((ResearchAddendum[])addenda.toArray(new ResearchAddendum[addenda.size()]));
               }
            }

            return entry;
         }
      }
   }

   public static int createItemStackHash(ItemStack stack) {
      if (stack == null) {
         return 0;
      } else {
         stack.field_77994_a = 1;
         return stack.serializeNBT().toString().hashCode();
      }
   }

   private static String[] arrayJsonToString(JsonArray jsonArray) {
      ArrayList<String> out = new ArrayList();
      Iterator var2 = jsonArray.iterator();

      while(var2.hasNext()) {
         JsonElement element = (JsonElement)var2.next();
         out.add(element.getAsString());
      }

      return out.size() == 0 ? null : (String[])out.toArray(new String[out.size()]);
   }

   private static Integer[] arrayJsonToInt(JsonArray jsonArray) {
      ArrayList<Integer> out = new ArrayList();
      Iterator var2 = jsonArray.iterator();

      while(var2.hasNext()) {
         JsonElement element = (JsonElement)var2.next();
         out.add(element.getAsInt());
      }

      return out.size() == 0 ? null : (Integer[])out.toArray(new Integer[out.size()]);
   }

   private static ItemStack[] parseJsonItemList(String key, String[] stacks) {
      if (stacks != null && stacks.length != 0) {
         ItemStack[] work = new ItemStack[stacks.length];
         int idx = 0;
         String[] var4 = stacks;
         int var5 = stacks.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String s = var4[var6];
            s = s.replace("'", "\"");
            ItemStack stack = parseJSONtoItemStack(s);
            if (stack != null) {
               work[idx] = stack;
               ++idx;
            }
         }

         ItemStack[] out = null;
         if (idx > 0) {
            out = (ItemStack[])((ItemStack[])Arrays.copyOf(work, idx));
         }

         return out;
      } else {
         return null;
      }
   }

   public static ItemStack parseJSONtoItemStack(String entry) {
      if (entry == null) {
         return null;
      } else {
         String[] split = entry.split(";");
         String name = split[0];
         int num = -1;
         int dam = -1;
         String nbt = null;

         for(int a = 1; a < split.length; ++a) {
            if (split[a].startsWith("{")) {
               nbt = split[a];
               nbt.replaceAll("'", "\"");
               break;
            }

            boolean var7 = true;

            int q;
            try {
               q = Integer.parseInt(split[a]);
            } catch (NumberFormatException var10) {
               continue;
            }

            if (q >= 0 && num < 0) {
               num = q;
            } else if (q >= 0 && dam < 0) {
               dam = q;
            }
         }

         if (num < 0) {
            num = 1;
         }

         if (dam < 0) {
            dam = 0;
         }

         ItemStack stack = null;

         try {
            Item it = Item.func_111206_d(name);
            if (it != null) {
               stack = new ItemStack(it, num, dam);
               if (nbt != null) {
                  stack.func_77982_d(JsonToNBT.func_180713_a(nbt));
               }
            }
         } catch (Exception var9) {
         }

         return stack;
      }
   }

   private static void addResearchToCategory(ResearchEntry ri) {
      ResearchCategory rl = ResearchCategories.getResearchCategory(ri.getCategory());
      if (rl != null && !rl.research.containsKey(ri.getKey())) {
         Iterator var2 = rl.research.values().iterator();

         while(var2.hasNext()) {
            ResearchEntry rr = (ResearchEntry)var2.next();
            if (rr.getDisplayColumn() == ri.getDisplayColumn() && rr.getDisplayRow() == ri.getDisplayRow()) {
               Thaumcraft.log.warn("Research [" + ri.getKey() + "] not added as it overlaps with existing research [" + rr.getKey() + "] at " + ri.getDisplayColumn() + "," + rr.getDisplayRow());
               return;
            }
         }

         rl.research.put(ri.getKey(), ri);
         if (ri.getDisplayColumn() < rl.minDisplayColumn) {
            rl.minDisplayColumn = ri.getDisplayColumn();
         }

         if (ri.getDisplayRow() < rl.minDisplayRow) {
            rl.minDisplayRow = ri.getDisplayRow();
         }

         if (ri.getDisplayColumn() > rl.maxDisplayColumn) {
            rl.maxDisplayColumn = ri.getDisplayColumn();
         }

         if (ri.getDisplayRow() > rl.maxDisplayRow) {
            rl.maxDisplayRow = ri.getDisplayRow();
         }
      } else {
         Thaumcraft.log.warn("Could not add invalid research entry " + ri.getKey());
      }

   }
}
