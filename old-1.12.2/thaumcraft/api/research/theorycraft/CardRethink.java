package thaumcraft.api.research.theorycraft;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;

public class CardRethink extends TheorycraftCard {
   public boolean initialize(EntityPlayer player, ResearchTableData data) {
      int a = 0;

      String category;
      for(Iterator var4 = data.categoryTotals.keySet().iterator(); var4.hasNext(); a += data.getTotal(category)) {
         category = (String)var4.next();
      }

      return a >= 10;
   }

   public int getInspirationCost() {
      return -1;
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.rethink.name", new Object[0])).func_150260_c();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.rethink.text", new Object[0])).func_150260_c();
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      if (!this.initialize(player, data)) {
         return false;
      } else {
         int a = 0;

         Iterator var4;
         String category;
         for(var4 = data.categoryTotals.keySet().iterator(); var4.hasNext(); a += data.getTotal(category)) {
            category = (String)var4.next();
         }

         a = Math.min(a, MathHelper.func_76136_a(player.func_70681_au(), 10, 20));

         while(a > 0) {
            var4 = data.categoryTotals.keySet().iterator();

            while(var4.hasNext()) {
               category = (String)var4.next();
               data.addTotal(category, -1);
               --a;
               if (a <= 0 || !data.hasTotal(category)) {
                  break;
               }
            }
         }

         if (player.func_70681_au().nextBoolean()) {
            ++data.bonusDraws;
         }

         return true;
      }
   }
}
