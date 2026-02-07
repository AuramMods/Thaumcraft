package thaumcraft.api.research.theorycraft;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchEntry;

public class ResearchTableData {
   public TileEntity table;
   public String player;
   public int inspiration;
   public int inspirationStart;
   public int bonusDraws;
   public int placedCards;
   public int aidsChosen;
   public ArrayList<Long> savedCards = new ArrayList();
   public ArrayList<String> aidCards = new ArrayList();
   public TreeMap<String, Integer> categoryTotals = new TreeMap();
   public ArrayList<String> categoriesBlocked = new ArrayList();
   public ArrayList<ResearchTableData.CardChoice> cardChoices = new ArrayList();
   public ResearchTableData.CardChoice lastDraw;

   public ResearchTableData(TileEntity tileResearchTable) {
      this.table = tileResearchTable;
   }

   public ResearchTableData(EntityPlayer player2, TileEntity tileResearchTable) {
      this.player = player2.func_70005_c_();
      this.table = tileResearchTable;
   }

   public boolean isComplete() {
      return this.inspiration <= 0;
   }

   public boolean hasTotal(String cat) {
      return this.categoryTotals.containsKey(cat);
   }

   public int getTotal(String cat) {
      return this.categoryTotals.containsKey(cat) ? (Integer)this.categoryTotals.get(cat) : 0;
   }

   public void addTotal(String cat, int amt) {
      int current = this.categoryTotals.containsKey(cat) ? (Integer)this.categoryTotals.get(cat) : 0;
      current += amt;
      if (current <= 0) {
         this.categoryTotals.remove(cat);
      } else {
         this.categoryTotals.put(cat, current);
      }

   }

   public void addInspiration(int amt) {
      this.inspiration += amt;
      if (this.inspiration > this.inspirationStart) {
         this.inspirationStart = this.inspiration;
      }

   }

   public NBTTagCompound serialize() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.func_74778_a("player", this.player);
      nbt.func_74768_a("inspiration", this.inspiration);
      nbt.func_74768_a("inspirationStart", this.inspirationStart);
      nbt.func_74768_a("placedCards", this.placedCards);
      nbt.func_74768_a("bonusDraws", this.bonusDraws);
      nbt.func_74768_a("aidsChosen", this.aidsChosen);
      NBTTagList savedTag = new NBTTagList();
      Iterator var3 = this.savedCards.iterator();

      while(var3.hasNext()) {
         Long card = (Long)var3.next();
         NBTTagCompound gt = new NBTTagCompound();
         gt.func_74772_a("card", card);
         savedTag.func_74742_a(gt);
      }

      nbt.func_74782_a("savedCards", savedTag);
      NBTTagList categoriesBlockedTag = new NBTTagList();
      Iterator var11 = this.categoriesBlocked.iterator();

      while(var11.hasNext()) {
         String category = (String)var11.next();
         NBTTagCompound gt = new NBTTagCompound();
         gt.func_74778_a("category", category);
         categoriesBlockedTag.func_74742_a(gt);
      }

      nbt.func_74782_a("categoriesBlocked", categoriesBlockedTag);
      NBTTagList categoryTotalsTag = new NBTTagList();
      Iterator var14 = this.categoryTotals.keySet().iterator();

      while(var14.hasNext()) {
         String category = (String)var14.next();
         NBTTagCompound gt = new NBTTagCompound();
         gt.func_74778_a("category", category);
         gt.func_74768_a("total", (Integer)this.categoryTotals.get(category));
         categoryTotalsTag.func_74742_a(gt);
      }

      nbt.func_74782_a("categoryTotals", categoryTotalsTag);
      NBTTagList aidCardsTag = new NBTTagList();
      Iterator var17 = this.aidCards.iterator();

      while(var17.hasNext()) {
         String mc = (String)var17.next();
         NBTTagCompound gt = new NBTTagCompound();
         gt.func_74778_a("aidCard", mc);
         aidCardsTag.func_74742_a(gt);
      }

      nbt.func_74782_a("aidCards", aidCardsTag);
      NBTTagList cardChoicesTag = new NBTTagList();
      Iterator var20 = this.cardChoices.iterator();

      while(var20.hasNext()) {
         ResearchTableData.CardChoice mc = (ResearchTableData.CardChoice)var20.next();
         NBTTagCompound gt = this.serializeCardChoice(mc);
         cardChoicesTag.func_74742_a(gt);
      }

      nbt.func_74782_a("cardChoices", cardChoicesTag);
      if (this.lastDraw != null) {
         nbt.func_74782_a("lastDraw", this.serializeCardChoice(this.lastDraw));
      }

      return nbt;
   }

   public NBTTagCompound serializeCardChoice(ResearchTableData.CardChoice mc) {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.func_74778_a("cardChoice", mc.key);
      nbt.func_74757_a("aid", mc.fromAid);
      nbt.func_74757_a("select", mc.selected);

      try {
         nbt.func_74782_a("cardNBT", mc.card.serialize());
      } catch (Exception var4) {
      }

      return nbt;
   }

   public void deserialize(NBTTagCompound nbt) {
      if (nbt != null) {
         this.inspiration = nbt.func_74762_e("inspiration");
         this.inspirationStart = nbt.func_74762_e("inspirationStart");
         this.placedCards = nbt.func_74762_e("placedCards");
         this.bonusDraws = nbt.func_74762_e("bonusDraws");
         this.aidsChosen = nbt.func_74762_e("aidsChosen");
         this.player = nbt.func_74779_i("player");
         NBTTagList savedTag = nbt.func_150295_c("savedCards", 10);
         this.savedCards = new ArrayList();

         for(int x = 0; x < savedTag.func_74745_c(); ++x) {
            NBTTagCompound nbtdata = savedTag.func_150305_b(x);
            this.savedCards.add(nbtdata.func_74763_f("card"));
         }

         NBTTagList categoriesBlockedTag = nbt.func_150295_c("categoriesBlocked", 10);
         this.categoriesBlocked = new ArrayList();

         for(int x = 0; x < categoriesBlockedTag.func_74745_c(); ++x) {
            NBTTagCompound nbtdata = categoriesBlockedTag.func_150305_b(x);
            this.categoriesBlocked.add(nbtdata.func_74779_i("category"));
         }

         NBTTagList categoryTotalsTag = nbt.func_150295_c("categoryTotals", 10);
         this.categoryTotals = new TreeMap();

         NBTTagCompound pe;
         for(int x = 0; x < categoryTotalsTag.func_74745_c(); ++x) {
            pe = categoryTotalsTag.func_150305_b(x);
            this.categoryTotals.put(pe.func_74779_i("category"), pe.func_74762_e("total"));
         }

         NBTTagList aidCardsTag = nbt.func_150295_c("aidCards", 10);
         this.aidCards = new ArrayList();

         for(int x = 0; x < aidCardsTag.func_74745_c(); ++x) {
            NBTTagCompound nbtdata = aidCardsTag.func_150305_b(x);
            this.aidCards.add(nbtdata.func_74779_i("aidCard"));
         }

         pe = null;
         if (this.table != null && this.table.func_145831_w() != null && !this.table.func_145831_w().field_72995_K) {
            EntityPlayer var17 = this.table.func_145831_w().func_72924_a(this.player);
         }

         NBTTagList cardChoicesTag = nbt.func_150295_c("cardChoices", 10);
         this.cardChoices = new ArrayList();

         for(int x = 0; x < cardChoicesTag.func_74745_c(); ++x) {
            NBTTagCompound nbtdata = cardChoicesTag.func_150305_b(x);
            ResearchTableData.CardChoice cc = this.deserializeCardChoice(nbtdata);
            if (cc != null) {
               this.cardChoices.add(cc);
            }
         }

         this.lastDraw = this.deserializeCardChoice(nbt.func_74775_l("lastDraw"));
      }
   }

   public ResearchTableData.CardChoice deserializeCardChoice(NBTTagCompound nbt) {
      if (nbt == null) {
         return null;
      } else {
         String key = nbt.func_74779_i("cardChoice");
         TheorycraftCard tc = this.generateCardWithNBT(nbt.func_74779_i("cardChoice"), nbt.func_74775_l("cardNBT"));
         return tc == null ? null : new ResearchTableData.CardChoice(key, tc, nbt.func_74767_n("aid"), nbt.func_74767_n("select"));
      }
   }

   private boolean isCategoryBlocked(String cat) {
      return this.categoriesBlocked.contains(cat);
   }

   public void drawCards(int draw, EntityPlayer pe) {
      if (draw == 3) {
         if (this.bonusDraws > 0) {
            --this.bonusDraws;
         } else {
            draw = 2;
         }
      }

      this.cardChoices.clear();
      this.player = pe.func_70005_c_();
      ArrayList<String> availCats = this.getAvailableCategories(pe);
      ArrayList<String> drawnCards = new ArrayList();
      int insp = getAvailableInspiration(pe) - this.aidsChosen;
      boolean aidDrawn = false;

      while(true) {
         while(true) {
            if (draw <= 0) {
               return;
            }

            TheorycraftCard card;
            if (!aidDrawn && !this.aidCards.isEmpty() && (double)pe.func_70681_au().nextFloat() <= 0.25D) {
               int idx = pe.func_70681_au().nextInt(this.aidCards.size());
               String key = (String)this.aidCards.get(idx);
               card = this.generateCard(key, -1L, pe);
               if (card != null && card.getInspirationCost() <= insp && !this.isCategoryBlocked(card.getResearchCategory()) && !drawnCards.contains(key)) {
                  drawnCards.add(key);
                  this.cardChoices.add(new ResearchTableData.CardChoice(key, card, true, false));
                  this.aidCards.remove(idx);
                  break;
               }
            } else {
               try {
                  String[] cards = (String[])TheorycraftManager.cards.keySet().toArray(new String[0]);
                  int idx = pe.func_70681_au().nextInt(cards.length);
                  card = this.generateCard(cards[idx], -1L, pe);
                  if (card != null && !card.isAidOnly() && card.getInspirationCost() <= insp) {
                     if (card.getResearchCategory() != null) {
                        boolean found = false;
                        Iterator var11 = availCats.iterator();

                        while(var11.hasNext()) {
                           String cn = (String)var11.next();
                           if (cn.equals(card.getResearchCategory())) {
                              found = true;
                              break;
                           }
                        }

                        if (!found) {
                           continue;
                        }
                     }

                     if (!drawnCards.contains(cards[idx])) {
                        drawnCards.add(cards[idx]);
                        this.cardChoices.add(new ResearchTableData.CardChoice(cards[idx], card, false, false));
                        break;
                     }
                  }
               } catch (Exception var13) {
                  var13.printStackTrace();
               }
            }
         }

         --draw;
      }
   }

   private TheorycraftCard generateCard(String key, long seed, EntityPlayer pe) {
      if (key == null) {
         return null;
      } else {
         Class<TheorycraftCard> tcc = (Class)TheorycraftManager.cards.get(key);
         if (tcc == null) {
            return null;
         } else {
            TheorycraftCard tc = null;

            try {
               tc = (TheorycraftCard)tcc.newInstance();
               if (seed < 0L) {
                  if (pe != null) {
                     tc.setSeed(pe.func_70681_au().nextLong());
                  } else {
                     tc.setSeed(System.nanoTime());
                  }
               } else {
                  tc.setSeed(seed);
               }

               if (pe != null && !tc.initialize(pe, this)) {
                  return null;
               }
            } catch (Exception var8) {
               var8.printStackTrace();
            }

            return tc;
         }
      }
   }

   private TheorycraftCard generateCardWithNBT(String key, NBTTagCompound nbt) {
      if (key == null) {
         return null;
      } else {
         Class<TheorycraftCard> tcc = (Class)TheorycraftManager.cards.get(key);
         if (tcc == null) {
            return null;
         } else {
            TheorycraftCard tc = null;

            try {
               tc = (TheorycraftCard)tcc.newInstance();
               tc.deserialize(nbt);
            } catch (Exception var6) {
               var6.printStackTrace();
            }

            return tc;
         }
      }
   }

   public void initialize(EntityPlayer player1, Set<String> aids) {
      this.inspirationStart = getAvailableInspiration(player1);
      this.inspiration = this.inspirationStart - aids.size();
      Iterator var3 = aids.iterator();

      while(true) {
         ITheorycraftAid mu;
         do {
            if (!var3.hasNext()) {
               return;
            }

            String muk = (String)var3.next();
            mu = (ITheorycraftAid)TheorycraftManager.aids.get(muk);
         } while(mu == null);

         Class[] var6 = mu.getCards();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Class clazz = var6[var8];
            this.aidCards.add(clazz.getName());
         }
      }
   }

   public ArrayList<String> getAvailableCategories(EntityPlayer player) {
      ArrayList<String> cats = new ArrayList();
      Iterator var3 = ResearchCategories.researchCategories.keySet().iterator();

      while(true) {
         String rck;
         ResearchCategory rc;
         do {
            do {
               do {
                  if (!var3.hasNext()) {
                     return cats;
                  }

                  rck = (String)var3.next();
                  rc = ResearchCategories.getResearchCategory(rck);
               } while(rc == null);
            } while(this.isCategoryBlocked(rck));
         } while(rc.researchKey != null && !ThaumcraftCapabilities.knowsResearchStrict(player, rc.researchKey));

         cats.add(rck);
      }
   }

   public static int getAvailableInspiration(EntityPlayer player) {
      float tot = 5.0F;
      IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
      Iterator var3 = knowledge.getResearchList().iterator();

      while(var3.hasNext()) {
         String s = (String)var3.next();
         if (ThaumcraftCapabilities.knowsResearchStrict(player, s)) {
            ResearchEntry re = ResearchCategories.getResearch(s);
            if (re != null) {
               if (re.hasMeta(ResearchEntry.EnumResearchMeta.SPIKY)) {
                  tot += 0.5F;
               }

               if (re.hasMeta(ResearchEntry.EnumResearchMeta.HIDDEN)) {
                  tot += 0.1F;
               }
            }
         }
      }

      return Math.min(15, Math.round(tot));
   }

   public class CardChoice {
      public TheorycraftCard card;
      public String key;
      public boolean fromAid;
      public boolean selected;

      public CardChoice(String key, TheorycraftCard card, boolean aid, boolean selected) {
         this.key = key;
         this.card = card;
         this.fromAid = aid;
         this.selected = selected;
      }

      public String toString() {
         return "key:" + this.key + " card:" + this.card.getSeed() + " fromAid:" + this.fromAid + " selected:" + this.selected;
      }
   }
}
