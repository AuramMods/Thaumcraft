package thaumcraft.api.research.theorycraft;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;

public class CardBalance extends TheorycraftCard {
   public int getInspirationCost() {
      return 1;
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.balance.name", new Object[0])).func_150260_c();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.balance.text", new Object[0])).func_150260_c();
   }

   public boolean initialize(EntityPlayer player, ResearchTableData data) {
      int total = 0;
      int size = 0;
      Iterator var5 = data.categoryTotals.keySet().iterator();

      while(var5.hasNext()) {
         String c = (String)var5.next();
         if (!data.categoriesBlocked.contains(c)) {
            total += (Integer)data.categoryTotals.get(c);
            ++size;
         }
      }

      return data.categoriesBlocked.size() < data.categoryTotals.size() - 1 && total >= size;
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      int total = 0;
      int size = 0;
      Iterator var5 = data.categoryTotals.keySet().iterator();

      String category;
      while(var5.hasNext()) {
         category = (String)var5.next();
         if (!data.categoriesBlocked.contains(category)) {
            total += (Integer)data.categoryTotals.get(category);
            ++size;
         }
      }

      if (data.categoriesBlocked.size() < data.categoryTotals.size() - 1 && total >= size) {
         var5 = data.categoryTotals.keySet().iterator();

         while(var5.hasNext()) {
            category = (String)var5.next();
            if (!data.categoriesBlocked.contains(category)) {
               data.categoryTotals.put(category, total / size);
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
