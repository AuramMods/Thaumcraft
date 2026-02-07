package thaumcraft.common.lib.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.internal.CommonInternals;
import thaumcraft.common.lib.utils.Utils;

public class ThaumcraftCraftingManager {
   static final int ASPECTCAP = 500;

   public static ShapedRecipes createFakeRecipe(ItemStack par1ItemStack, Object... par2ArrayOfObj) {
      String var3 = "";
      int var4 = 0;
      int var5 = 0;
      int var6 = 0;
      int var9;
      if (par2ArrayOfObj[var4] instanceof String[]) {
         String[] var7 = (String[])((String[])((String[])par2ArrayOfObj[var4++]));
         String[] var8 = var7;
         var9 = var7.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            String var11 = var8[var10];
            ++var6;
            var5 = var11.length();
            var3 = var3 + var11;
         }
      } else {
         while(par2ArrayOfObj[var4] instanceof String) {
            String var13 = (String)par2ArrayOfObj[var4++];
            ++var6;
            var5 = var13.length();
            var3 = var3 + var13;
         }
      }

      HashMap var14;
      for(var14 = new HashMap(); var4 < par2ArrayOfObj.length; var4 += 2) {
         Character var16 = (Character)par2ArrayOfObj[var4];
         ItemStack var17 = null;
         if (par2ArrayOfObj[var4 + 1] instanceof Item) {
            var17 = new ItemStack((Item)par2ArrayOfObj[var4 + 1]);
         } else if (par2ArrayOfObj[var4 + 1] instanceof Block) {
            var17 = new ItemStack((Block)par2ArrayOfObj[var4 + 1]);
         } else if (par2ArrayOfObj[var4 + 1] instanceof ItemStack) {
            var17 = (ItemStack)par2ArrayOfObj[var4 + 1];
         }

         var14.put(var16, var17);
      }

      ItemStack[] var15 = new ItemStack[var5 * var6];

      for(var9 = 0; var9 < var5 * var6; ++var9) {
         char var18 = var3.charAt(var9);
         if (var14.containsKey(var18)) {
            var15[var9] = ((ItemStack)var14.get(var18)).func_77946_l();
         } else {
            var15[var9] = null;
         }
      }

      return new ShapedRecipes(var5, var6, var15, par1ItemStack);
   }

   public static CrucibleRecipe findMatchingCrucibleRecipe(EntityPlayer player, AspectList aspects, ItemStack lastDrop) {
      int highest = 0;
      CrucibleRecipe out = null;
      Iterator var5 = ThaumcraftApi.getCraftingRecipes().values().iterator();

      while(true) {
         Object re;
         do {
            do {
               if (!var5.hasNext()) {
                  return out;
               }

               re = var5.next();
            } while(re == null);
         } while(!(re instanceof CrucibleRecipe[]));

         CrucibleRecipe[] var7 = (CrucibleRecipe[])((CrucibleRecipe[])re);
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            CrucibleRecipe recipe = var7[var9];
            ItemStack temp = lastDrop.func_77946_l();
            temp.field_77994_a = 1;
            if (player != null && ThaumcraftCapabilities.knowsResearchStrict(player, recipe.research) && recipe.matches(aspects, temp)) {
               int result = recipe.aspects.visSize();
               if (result > highest) {
                  highest = result;
                  out = recipe;
               }
            }
         }
      }
   }

   public static IArcaneRecipe findMatchingArcaneRecipe(InventoryCrafting awb, EntityPlayer player) {
      int var2 = 0;
      ItemStack var3 = null;
      ItemStack var4 = null;

      for(int var5 = 0; var5 < 15; ++var5) {
         ItemStack var6 = awb.func_70301_a(var5);
         if (var6 != null) {
            if (var2 == 0) {
               ;
            }

            if (var2 == 1) {
               ;
            }

            ++var2;
         }
      }

      Iterator var11 = ThaumcraftApi.getCraftingRecipes().values().iterator();

      while(true) {
         Object recipes;
         do {
            do {
               if (!var11.hasNext()) {
                  return null;
               }

               recipes = var11.next();
            } while(recipes == null);
         } while(!(recipes instanceof IArcaneRecipe[]));

         IArcaneRecipe[] var7 = (IArcaneRecipe[])((IArcaneRecipe[])recipes);
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            IArcaneRecipe recipe = var7[var9];
            if (recipe.matches(awb, player.field_70170_p, player)) {
               return recipe;
            }
         }
      }
   }

   public static ItemStack findMatchingArcaneRecipeResult(InventoryCrafting awb, EntityPlayer player) {
      IArcaneRecipe var13 = findMatchingArcaneRecipe(awb, player);
      return var13 == null ? null : var13.func_77572_b(awb);
   }

   public static ItemStack[] findMatchingArcaneRecipeCrystals(InventoryCrafting awb, EntityPlayer player) {
      IArcaneRecipe var13 = findMatchingArcaneRecipe(awb, player);
      return var13 == null ? null : var13.getCrystals();
   }

   public static int findMatchingArcaneRecipeVis(InventoryCrafting awb, EntityPlayer player) {
      IArcaneRecipe var13 = findMatchingArcaneRecipe(awb, player);
      return var13 == null ? 0 : (var13.getVis() > 0 ? var13.getVis() : var13.getVis(awb));
   }

   public static InfusionRecipe findMatchingInfusionRecipe(ArrayList<ItemStack> items, ItemStack input, EntityPlayer player) {
      Iterator var3 = ThaumcraftApi.getCraftingRecipes().values().iterator();

      while(true) {
         Object recipes;
         do {
            do {
               if (!var3.hasNext()) {
                  return null;
               }

               recipes = var3.next();
            } while(recipes == null);
         } while(!(recipes instanceof InfusionRecipe[]));

         InfusionRecipe[] var5 = (InfusionRecipe[])((InfusionRecipe[])recipes);
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            InfusionRecipe recipe = var5[var7];
            if (recipe.matches(items, input, player.field_70170_p, player)) {
               return recipe;
            }
         }
      }
   }

   public static AspectList getObjectTags(ItemStack itemstack) {
      return getObjectTags(itemstack, (ArrayList)null);
   }

   public static AspectList getObjectTags(ItemStack itemstack, ArrayList<String> history) {
      String ss = null;
      ItemStack sc = null;

      try {
         sc = itemstack.func_77946_l();
         sc.field_77994_a = 1;
         ss = sc.serializeNBT().toString();
      } catch (Exception var6) {
         return null;
      }

      if (ss == null) {
         return null;
      } else {
         AspectList tmp = (AspectList)CommonInternals.objectTags.get(ss);
         if (tmp == null) {
            try {
               sc.func_77964_b(32767);
               ss = sc.serializeNBT().toString();
               tmp = (AspectList)CommonInternals.objectTags.get(ss);
               if (tmp == null) {
                  if (itemstack.func_77952_i() == 32767 && tmp == null) {
                     int index = 0;

                     do {
                        sc.func_77964_b(index);
                        ss = sc.serializeNBT().toString();
                        tmp = (AspectList)CommonInternals.objectTags.get(ss);
                        ++index;
                     } while(index < 16 && tmp == null);
                  }

                  if (tmp == null) {
                     tmp = generateTags(itemstack, history);
                  }
               }
            } catch (Exception var7) {
            }
         }

         return capAspects(getBonusTags(itemstack, tmp), 500);
      }
   }

   private static AspectList capAspects(AspectList sourcetags, int amount) {
      if (sourcetags == null) {
         return sourcetags;
      } else {
         AspectList out = new AspectList();
         Aspect[] var3 = sourcetags.getAspects();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Aspect aspect = var3[var5];
            if (aspect != null) {
               out.merge(aspect, Math.min(amount, sourcetags.getAmount(aspect)));
            }
         }

         return out;
      }
   }

   private static AspectList getBonusTags(ItemStack itemstack, AspectList sourcetags) {
      AspectList tmp = new AspectList();
      Item item = itemstack.func_77973_b();
      Aspect[] var4;
      int var5;
      int var6;
      Aspect tag;
      if (item != null && item instanceof IEssentiaContainerItem && !((IEssentiaContainerItem)item).ignoreContainedAspects()) {
         if (sourcetags != null) {
            sourcetags.aspects.clear();
         }

         tmp = ((IEssentiaContainerItem)item).getAspects(itemstack);
         if (tmp != null && tmp.size() > 0) {
            var4 = tmp.copy().getAspects();
            var5 = var4.length;

            for(var6 = 0; var6 < var5; ++var6) {
               tag = var4[var6];
               if (tmp.getAmount(tag) <= 0) {
                  tmp.remove(tag);
               }
            }
         }
      }

      if (tmp == null) {
         tmp = new AspectList();
      }

      if (sourcetags != null) {
         var4 = sourcetags.getAspects();
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            tag = var4[var6];
            if (tag != null) {
               tmp.add(tag, sourcetags.getAmount(tag));
            }
         }
      }

      if (item != null && (tmp != null || item == Items.field_151068_bn)) {
         int var5;
         if (item instanceof ItemArmor) {
            tmp.merge(Aspect.PROTECT, ((ItemArmor)item).field_77879_b * 4);
         } else if (item instanceof ItemSword && ((ItemSword)item).func_150931_i() + 1.0F > 0.0F) {
            tmp.merge(Aspect.AVERSION, (int)(((ItemSword)item).func_150931_i() + 1.0F) * 4);
         } else if (item instanceof ItemBow) {
            tmp.merge(Aspect.AVERSION, 10).merge(Aspect.FLIGHT, 5);
         } else if (item instanceof ItemTool) {
            String mat = ((ItemTool)item).func_77861_e();
            ToolMaterial[] var14 = ToolMaterial.values();
            var6 = var14.length;

            for(var5 = 0; var5 < var6; ++var5) {
               ToolMaterial tm = var14[var5];
               if (tm.toString().equals(mat)) {
                  tmp.merge(Aspect.TOOL, (tm.func_77996_d() + 1) * 4);
               }
            }
         } else if (item instanceof ItemShears || item instanceof ItemHoe) {
            if (item.func_77612_l() <= ToolMaterial.WOOD.func_77997_a()) {
               tmp.merge(Aspect.TOOL, 4);
            } else if (item.func_77612_l() > ToolMaterial.STONE.func_77997_a() && item.func_77612_l() > ToolMaterial.GOLD.func_77997_a()) {
               if (item.func_77612_l() <= ToolMaterial.IRON.func_77997_a()) {
                  tmp.merge(Aspect.TOOL, 12);
               } else {
                  tmp.merge(Aspect.TOOL, 16);
               }
            } else {
               tmp.merge(Aspect.TOOL, 8);
            }
         }

         String[] dyes = new String[]{"dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite"};
         int[] ores = OreDictionary.getOreIDs(itemstack);
         int var3;
         if (ores != null && ores.length > 0) {
            Arrays.sort(dyes);
            int[] var16 = ores;
            var5 = ores.length;

            for(var3 = 0; var3 < var5; ++var3) {
               int od = var16[var3];
               String s = OreDictionary.getOreName(od);
               if (s != null && Arrays.binarySearch(dyes, s) >= 0) {
                  tmp.merge(Aspect.SENSES, 5);
                  break;
               }
            }
         }

         NBTTagList ench = itemstack.func_77986_q();
         if (item instanceof ItemEnchantedBook) {
            ench = ((ItemEnchantedBook)item).func_92110_g(itemstack);
         }

         if (ench != null) {
            var5 = 0;

            for(var3 = 0; var3 < ench.func_74745_c(); ++var3) {
               short eid = ench.func_150305_b(var3).func_74765_d("id");
               short lvl = (short)(ench.func_150305_b(var3).func_74765_d("lvl") * 3);
               Enchantment e = Enchantment.func_185262_c(eid);
               if (e != null) {
                  if (e == Enchantments.field_185299_g) {
                     tmp.merge(Aspect.WATER, lvl);
                  } else if (e == Enchantments.field_180312_n) {
                     tmp.merge(Aspect.BEAST, lvl / 2).merge(Aspect.AVERSION, lvl / 2);
                  } else if (e == Enchantments.field_185297_d) {
                     tmp.merge(Aspect.PROTECT, lvl / 2).merge(Aspect.ENTROPY, lvl / 2);
                  } else if (e == Enchantments.field_185305_q) {
                     tmp.merge(Aspect.TOOL, lvl);
                  } else if (e == Enchantments.field_180309_e) {
                     tmp.merge(Aspect.FLIGHT, lvl);
                  } else if (e == Enchantments.field_77334_n) {
                     tmp.merge(Aspect.FIRE, lvl / 2).merge(Aspect.AVERSION, lvl / 2);
                  } else if (e == Enchantments.field_77329_d) {
                     tmp.merge(Aspect.PROTECT, lvl / 2).merge(Aspect.FIRE, lvl / 2);
                  } else if (e == Enchantments.field_185311_w) {
                     tmp.merge(Aspect.FIRE, lvl);
                  } else if (e == Enchantments.field_185308_t) {
                     tmp.merge(Aspect.DESIRE, lvl);
                  } else if (e == Enchantments.field_185312_x) {
                     tmp.merge(Aspect.CRAFT, lvl);
                  } else if (e == Enchantments.field_180313_o) {
                     tmp.merge(Aspect.AIR, lvl);
                  } else if (e == Enchantments.field_185304_p) {
                     tmp.merge(Aspect.DESIRE, lvl);
                  } else if (e == Enchantments.field_185309_u) {
                     tmp.merge(Aspect.AVERSION, lvl);
                  } else if (e == Enchantments.field_180308_g) {
                     tmp.merge(Aspect.PROTECT, lvl);
                  } else if (e == Enchantments.field_180310_c) {
                     tmp.merge(Aspect.PROTECT, lvl);
                  } else if (e == Enchantments.field_185310_v) {
                     tmp.merge(Aspect.AIR, lvl);
                  } else if (e == Enchantments.field_185298_f) {
                     tmp.merge(Aspect.AIR, lvl);
                  } else if (e == Enchantments.field_185302_k) {
                     tmp.merge(Aspect.AVERSION, lvl);
                  } else if (e == Enchantments.field_185306_r) {
                     tmp.merge(Aspect.EXCHANGE, lvl);
                  } else if (e == Enchantments.field_92091_k) {
                     tmp.merge(Aspect.AVERSION, lvl);
                  } else if (e == Enchantments.field_185303_l) {
                     tmp.merge(Aspect.UNDEAD, lvl / 2).merge(Aspect.AVERSION, lvl / 2);
                  } else if (e == Enchantments.field_185307_s) {
                     tmp.merge(Aspect.EARTH, lvl);
                  } else if (e == Enchantments.field_185300_i) {
                     tmp.merge(Aspect.WATER, lvl);
                  } else if (e == Enchantments.field_151370_z) {
                     tmp.merge(Aspect.DESIRE, lvl);
                  } else if (e == Enchantments.field_151369_A) {
                     tmp.merge(Aspect.BEAST, lvl);
                  } else if (e == Enchantments.field_185301_j) {
                     tmp.merge(Aspect.COLD, lvl);
                  } else if (e == Enchantments.field_185296_A) {
                     tmp.merge(Aspect.CRAFT, lvl);
                  }

                  if (e.func_77324_c() == Rarity.UNCOMMON) {
                     var5 += 2;
                  }

                  if (e.func_77324_c() == Rarity.RARE) {
                     var5 += 4;
                  }

                  if (e.func_77324_c() == Rarity.VERY_RARE) {
                     var5 += 6;
                  }
               }

               var5 += lvl;
            }

            if (var5 > 0) {
               tmp.merge(Aspect.MAGIC, var5);
            }
         }
      }

      return AspectHelper.cullTags(tmp);
   }

   public static AspectList generateTags(ItemStack is) {
      AspectList temp = generateTags(is, new ArrayList());
      return temp;
   }

   public static AspectList generateTags(ItemStack is, ArrayList<String> history) {
      if (history == null) {
         history = new ArrayList();
      }

      ItemStack stack = is.func_77946_l();
      stack.field_77994_a = 1;

      try {
         if (stack.func_77973_b().func_77645_m() || !stack.func_77973_b().func_77614_k()) {
            stack.func_77964_b(32767);
         }
      } catch (Exception var5) {
      }

      if (ThaumcraftApi.exists(stack)) {
         return getObjectTags(stack, history);
      } else {
         String ss = stack.serializeNBT().toString();
         if (history.contains(ss)) {
            return null;
         } else {
            history.add(ss);
            if (history.size() < 100) {
               if (stack.func_77952_i() == 32767) {
                  stack.func_77964_b(0);
               }

               AspectList ret = generateTagsFromRecipes(stack, history);
               ret = capAspects(ret, 500);
               ThaumcraftApi.registerObjectTag(is, ret);
               return ret;
            } else {
               return null;
            }
         }
      }
   }

   private static AspectList generateTagsFromCrucibleRecipes(ItemStack stack, ArrayList<String> history) {
      CrucibleRecipe cr = ThaumcraftApi.getCrucibleRecipe(stack);
      if (cr != null && !CommonInternals.craftingRecipesCatalogFake.contains(cr.getRecipeName())) {
         AspectList ot = cr.aspects.copy();
         int ss = cr.getRecipeOutput().field_77994_a;
         ItemStack cat = null;
         if (cr.catalyst instanceof ItemStack) {
            cat = (ItemStack)cr.catalyst;
         } else if (cr.catalyst instanceof List && ((List)cr.catalyst).size() > 0) {
            cat = (ItemStack)((List)cr.catalyst).get(0);
         }

         if (cat == null) {
            return null;
         } else {
            AspectList ot2 = getObjectTags(cat, history);
            AspectList out = new AspectList();
            Aspect[] var8;
            int var9;
            int var10;
            Aspect as;
            if (ot2 != null && ot2.size() > 0) {
               var8 = ot2.getAspects();
               var9 = var8.length;

               for(var10 = 0; var10 < var9; ++var10) {
                  as = var8[var10];
                  out.add(as, ot2.getAmount(as));
               }
            }

            var8 = ot.getAspects();
            var9 = var8.length;

            for(var10 = 0; var10 < var9; ++var10) {
               as = var8[var10];
               int amt = (int)(Math.sqrt((double)ot.getAmount(as)) / (double)ss);
               out.add(as, amt);
            }

            var8 = out.getAspects();
            var9 = var8.length;

            for(var10 = 0; var10 < var9; ++var10) {
               as = var8[var10];
               if (out.getAmount(as) <= 0) {
                  out.remove(as);
               }
            }

            return out;
         }
      } else {
         return null;
      }
   }

   private static AspectList generateTagsFromArcaneRecipes(ItemStack stack, ArrayList<String> history) {
      AspectList ret = null;
      int value = 0;
      HashMap<String, Object> recipeList = ThaumcraftApi.getCraftingRecipes();
      Iterator var5 = recipeList.keySet().iterator();

      label185:
      while(true) {
         String q;
         do {
            do {
               if (!var5.hasNext()) {
                  return ret;
               }

               q = (String)var5.next();
            } while(CommonInternals.craftingRecipesCatalogFake.contains(q));
         } while(!(recipeList.get(q) instanceof IArcaneRecipe[]));

         IArcaneRecipe[] var7 = (IArcaneRecipe[])((IArcaneRecipe[])recipeList.get(q));
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            IArcaneRecipe recipe = var7[var9];
            if (recipe.func_77571_b() != null && recipe.func_77571_b().func_77973_b() != null) {
               int idR = recipe.func_77571_b().func_77952_i() == 32767 ? 0 : recipe.func_77571_b().func_77952_i();
               int idS = stack.func_77952_i() < 0 ? 0 : stack.func_77952_i();
               if (recipe.func_77571_b().func_77973_b() == stack.func_77973_b() && idR == idS) {
                  ArrayList<ItemStack> ingredients = new ArrayList();
                  new AspectList();
                  byte cval = 0;

                  try {
                     int i;
                     ItemStack is;
                     if (recipe instanceof ShapedArcaneRecipe) {
                        int width = ((ShapedArcaneRecipe)recipe).width;
                        i = ((ShapedArcaneRecipe)recipe).height;
                        Object[] items = ((ShapedArcaneRecipe)recipe).getInput();

                        for(int i = 0; i < width && i < 3; ++i) {
                           for(int j = 0; j < i && j < 3; ++j) {
                              if (items[i + j * width] != null) {
                                 ItemStack it;
                                 if (items[i + j * width] instanceof List) {
                                    Iterator var34 = ((List)items[i + j * width]).iterator();

                                    while(var34.hasNext()) {
                                       it = (ItemStack)var34.next();
                                       if (Utils.isEETransmutionItem(it.func_77973_b())) {
                                          continue label185;
                                       }

                                       AspectList obj = getObjectTags(it, history);
                                       if (obj != null && obj.size() > 0) {
                                          ItemStack is = it.func_77946_l();
                                          is.field_77994_a = 1;
                                          ingredients.add(is);
                                          break;
                                       }
                                    }
                                 } else {
                                    is = (ItemStack)items[i + j * width];
                                    if (Utils.isEETransmutionItem(is.func_77973_b())) {
                                       continue label185;
                                    }

                                    it = is.func_77946_l();
                                    it.field_77994_a = 1;
                                    ingredients.add(it);
                                 }
                              }
                           }
                        }
                     } else if (recipe instanceof ShapelessArcaneRecipe) {
                        ArrayList items = ((ShapelessArcaneRecipe)recipe).getInput();

                        for(i = 0; i < items.size() && i < 9; ++i) {
                           if (items.get(i) != null) {
                              ItemStack it;
                              if (items.get(i) instanceof List) {
                                 Iterator var29 = ((List)items.get(i)).iterator();

                                 while(var29.hasNext()) {
                                    it = (ItemStack)var29.next();
                                    if (Utils.isEETransmutionItem(it.func_77973_b())) {
                                       continue label185;
                                    }

                                    AspectList obj = getObjectTags(it, history);
                                    if (obj != null && obj.size() > 0) {
                                       is = it.func_77946_l();
                                       is.field_77994_a = 1;
                                       ingredients.add(is);
                                       break;
                                    }
                                 }
                              } else {
                                 ItemStack it = (ItemStack)items.get(i);
                                 if (Utils.isEETransmutionItem(it.func_77973_b())) {
                                    continue label185;
                                 }

                                 it = it.func_77946_l();
                                 it.field_77994_a = 1;
                                 ingredients.add(it);
                              }
                           }
                        }
                     }

                     AspectList ph = getAspectsFromIngredients(ingredients, recipe.func_77571_b(), history);
                     if (recipe.getVis() > 0) {
                        ph.add(Aspect.MAGIC, (int)(Math.sqrt((double)(1 + recipe.getVis() / 2)) / (double)((float)recipe.func_77571_b().field_77994_a)));
                     }

                     Aspect[] var27 = ph.copy().getAspects();
                     i = var27.length;

                     for(int var30 = 0; var30 < i; ++var30) {
                        Aspect as = var27[var30];
                        if (ph.getAmount(as) <= 0) {
                           ph.remove(as);
                        }
                     }

                     if (cval >= value) {
                        ret = ph;
                        value = cval;
                     }
                  } catch (Exception var25) {
                     var25.printStackTrace();
                  }
               }
            }
         }
      }
   }

   private static ItemStack stackFromRecipeObject(Object obj) {
      ItemStack out = null;
      if (obj instanceof ItemStack) {
         return (ItemStack)obj;
      } else {
         if (obj instanceof String) {
            String s = (String)obj;
            int l = Integer.MAX_VALUE;
            Iterator var4 = OreDictionary.getOres(s, false).iterator();

            while(var4.hasNext()) {
               ItemStack stack = (ItemStack)var4.next();
               if (out == null) {
                  out = stack.func_77946_l();
               }

               AspectList al = AspectHelper.getObjectAspects(stack);
               int q = al.visSize();
               if (q > 0 && q < l) {
                  l = q;
                  out = stack.func_77946_l();
               }
            }
         }

         return out;
      }
   }

   private static AspectList generateTagsFromInfusionRecipes(ItemStack stack, ArrayList<String> history) {
      InfusionRecipe cr = ThaumcraftApi.getInfusionRecipe(stack);
      if (cr != null && !CommonInternals.craftingRecipesCatalogFake.contains(cr.getRecipeName())) {
         AspectList ot = cr.getAspects().copy();
         ArrayList<ItemStack> ingredients = new ArrayList();
         ItemStack is = stackFromRecipeObject(cr.getRecipeInput());
         is.field_77994_a = 1;
         ingredients.add(is);
         Object[] var6 = cr.getComponents();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Object cat = var6[var8];
            ItemStack is2 = stackFromRecipeObject(cat);
            is2.field_77994_a = 1;
            ingredients.add(is2);
         }

         AspectList out = new AspectList();
         AspectList ot2 = getAspectsFromIngredients(ingredients, (ItemStack)cr.getRecipeOutput(), history);
         Aspect[] var15 = ot2.getAspects();
         int var16 = var15.length;

         Aspect as;
         int var17;
         for(var17 = 0; var17 < var16; ++var17) {
            as = var15[var17];
            out.add(as, ot2.getAmount(as));
         }

         var15 = ot.getAspects();
         var16 = var15.length;

         for(var17 = 0; var17 < var16; ++var17) {
            as = var15[var17];
            int amt = (int)(Math.sqrt((double)ot.getAmount(as)) / (double)((ItemStack)cr.getRecipeOutput()).field_77994_a);
            out.add(as, amt);
         }

         var15 = out.getAspects();
         var16 = var15.length;

         for(var17 = 0; var17 < var16; ++var17) {
            as = var15[var17];
            if (out.getAmount(as) <= 0) {
               out.remove(as);
            }
         }

         return out;
      } else {
         return null;
      }
   }

   private static AspectList generateTagsFromCraftingRecipes(ItemStack stack, ArrayList<String> history) {
      AspectList ret = null;
      int value = Integer.MAX_VALUE;
      List recipeList = CraftingManager.func_77594_a().func_77592_b();

      label220:
      for(int q = 0; q < recipeList.size(); ++q) {
         if (!CommonInternals.craftingRecipesCatalogFake.contains(recipeList.get(q))) {
            IRecipe recipe = (IRecipe)recipeList.get(q);
            if (recipe != null && recipe.func_77571_b() != null && Item.func_150891_b(recipe.func_77571_b().func_77973_b()) > 0 && recipe.func_77571_b().func_77973_b() != null) {
               int idR = recipe.func_77571_b().func_77952_i() == 32767 ? 0 : recipe.func_77571_b().func_77952_i();
               int idS = stack.func_77952_i() == 32767 ? 0 : stack.func_77952_i();
               if (recipe.func_77571_b().func_77973_b() == stack.func_77973_b() && idR == idS) {
                  ArrayList<ItemStack> ingredients = new ArrayList();
                  new AspectList();
                  boolean var11 = false;

                  try {
                     int width;
                     int i;
                     ItemStack is;
                     int i;
                     if (recipeList.get(q) instanceof ShapedRecipes) {
                        width = ((ShapedRecipes)recipeList.get(q)).field_77576_b;
                        i = ((ShapedRecipes)recipeList.get(q)).field_77577_c;
                        ItemStack[] items = ((ShapedRecipes)recipeList.get(q)).field_77574_d;

                        for(int i = 0; i < width && i < 3; ++i) {
                           for(int j = 0; j < i && j < 3; ++j) {
                              if (items[i + j * width] != null) {
                                 if (Utils.isEETransmutionItem(items[i + j * width].func_77973_b())) {
                                    continue label220;
                                 }

                                 is = items[i + j * width].func_77946_l();
                                 is.field_77994_a = 1;
                                 ingredients.add(is);
                              }
                           }
                        }
                     } else {
                        ItemStack it;
                        if (recipeList.get(q) instanceof ShapelessRecipes) {
                           List<ItemStack> items = ((ShapelessRecipes)recipeList.get(q)).field_77579_b;

                           for(i = 0; i < items.size() && i < 9; ++i) {
                              if (items.get(i) != null) {
                                 if (Utils.isEETransmutionItem(((ItemStack)items.get(i)).func_77973_b())) {
                                    continue label220;
                                 }

                                 it = ((ItemStack)items.get(i)).func_77946_l();
                                 it.field_77994_a = 1;
                                 ingredients.add(it);
                              }
                           }
                        } else {
                           ItemStack it;
                           if (recipeList.get(q) instanceof ShapedOreRecipe) {
                              width = ((ShapedOreRecipe)recipeList.get(q)).func_77570_a();
                              Object[] items = ((ShapedOreRecipe)recipeList.get(q)).getInput();

                              for(i = 0; i < width && i < 9; ++i) {
                                 if (items[i] != null) {
                                    ItemStack it;
                                    if (items[i] instanceof List) {
                                       Iterator var28 = ((List)items[i]).iterator();

                                       while(var28.hasNext()) {
                                          it = (ItemStack)var28.next();
                                          if (Utils.isEETransmutionItem(it.func_77973_b())) {
                                             continue label220;
                                          }

                                          AspectList obj = getObjectTags(it, history);
                                          if (obj != null && obj.size() > 0) {
                                             ItemStack is = it.func_77946_l();
                                             is.field_77994_a = 1;
                                             ingredients.add(is);
                                             break;
                                          }
                                       }
                                    } else {
                                       it = (ItemStack)items[i];
                                       if (Utils.isEETransmutionItem(it.func_77973_b())) {
                                          continue label220;
                                       }

                                       it = it.func_77946_l();
                                       it.field_77994_a = 1;
                                       ingredients.add(it);
                                    }
                                 }
                              }
                           } else if (recipeList.get(q) instanceof ShapelessOreRecipe) {
                              ArrayList items = ((ShapelessOreRecipe)recipeList.get(q)).getInput();

                              for(i = 0; i < items.size() && i < 9; ++i) {
                                 if (items.get(i) != null) {
                                    if (items.get(i) instanceof List) {
                                       Iterator var27 = ((List)items.get(i)).iterator();

                                       while(var27.hasNext()) {
                                          it = (ItemStack)var27.next();
                                          if (Utils.isEETransmutionItem(it.func_77973_b())) {
                                             continue label220;
                                          }

                                          AspectList obj = getObjectTags(it, history);
                                          if (obj != null && obj.size() > 0) {
                                             is = it.func_77946_l();
                                             is.field_77994_a = 1;
                                             ingredients.add(is);
                                             break;
                                          }
                                       }
                                    } else {
                                       it = (ItemStack)items.get(i);
                                       if (Utils.isEETransmutionItem(it.func_77973_b())) {
                                          continue label220;
                                       }

                                       it = it.func_77946_l();
                                       it.field_77994_a = 1;
                                       ingredients.add(it);
                                    }
                                 }
                              }
                           }
                        }
                     }

                     AspectList ph = getAspectsFromIngredients(ingredients, recipe.func_77571_b(), history);
                     Aspect[] var22 = ph.copy().getAspects();
                     i = var22.length;

                     for(i = 0; i < i; ++i) {
                        Aspect as = var22[i];
                        if (ph.getAmount(as) <= 0) {
                           ph.remove(as);
                        }
                     }

                     if (ph.visSize() < value && ph.visSize() > 0) {
                        ret = ph;
                        value = ph.visSize();
                     }
                  } catch (Exception var19) {
                     var19.printStackTrace();
                  }
               }
            }
         }
      }

      return ret;
   }

   private static AspectList getAspectsFromIngredients(ArrayList<ItemStack> ingredients, ItemStack recipeOut, ArrayList<String> history) {
      AspectList out = new AspectList();
      AspectList mid = new AspectList();
      Iterator var5 = ingredients.iterator();

      while(true) {
         AspectList obj;
         int var10;
         label67:
         do {
            ItemStack is;
            do {
               if (!var5.hasNext()) {
                  Aspect[] var13 = mid.getAspects();
                  int var14 = var13.length;

                  int var15;
                  Aspect as;
                  for(var15 = 0; var15 < var14; ++var15) {
                     as = var13[var15];
                     if (as != null) {
                        float v = (float)mid.getAmount(as) * 0.75F / (float)recipeOut.field_77994_a;
                        if (v < 1.0F && (double)v > 0.75D) {
                           v = 1.0F;
                        }

                        out.add(as, (int)v);
                     }
                  }

                  var13 = out.getAspects();
                  var14 = var13.length;

                  for(var15 = 0; var15 < var14; ++var15) {
                     as = var13[var15];
                     if (out.getAmount(as) <= 0) {
                        out.remove(as);
                     }
                  }

                  return out;
               }

               is = (ItemStack)var5.next();
               obj = getObjectTags(is, history);
               if (is.func_77973_b().func_77668_q() == null) {
                  continue label67;
               }
            } while(is.func_77973_b().func_77668_q() == is.func_77973_b());

            AspectList objC = getObjectTags(new ItemStack(is.func_77973_b(), 1, 32767), history);
            Aspect[] var9 = objC.getAspects();
            var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               Aspect as = var9[var11];
               out.reduce(as, objC.getAmount(as));
            }
         } while(obj == null);

         Aspect[] var16 = obj.getAspects();
         int var18 = var16.length;

         for(var10 = 0; var10 < var18; ++var10) {
            Aspect as = var16[var10];
            if (as != null) {
               mid.add(as, obj.getAmount(as));
            }
         }
      }
   }

   private static AspectList generateTagsFromRecipes(ItemStack stack, ArrayList<String> history) {
      AspectList ret = null;
      int value = false;
      ret = generateTagsFromCrucibleRecipes(stack, history);
      if (ret != null) {
         return ret;
      } else {
         ret = generateTagsFromArcaneRecipes(stack, history);
         if (ret != null) {
            return ret;
         } else {
            ret = generateTagsFromInfusionRecipes(stack, history);
            if (ret != null) {
               return ret;
            } else {
               ret = generateTagsFromCraftingRecipes(stack, history);
               return ret;
            }
         }
      }
   }
}
