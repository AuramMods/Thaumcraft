package thaumcraft.api.research.theorycraft;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;

public class CardPonder extends TheorycraftCard {
   public int getInspirationCost() {
      return 2;
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.ponder.name", new Object[0])).func_150260_c();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.ponder.text", new Object[0])).func_150260_c();
   }

   public boolean initialize(EntityPlayer player, ResearchTableData data) {
      return data.categoriesBlocked.size() < data.categoryTotals.size();
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      int a = 20;

      while(a > 0) {
         Iterator var4 = data.categoryTotals.keySet().iterator();

         while(var4.hasNext()) {
            String category = (String)var4.next();
            if (data.categoriesBlocked.contains(category)) {
               if (data.categoryTotals.size() <= 1) {
                  return false;
               }
            } else {
               data.addTotal(category, 1);
               --a;
               if (a <= 0) {
                  break;
               }
            }
         }
      }

      return a != 20;
   }
}
