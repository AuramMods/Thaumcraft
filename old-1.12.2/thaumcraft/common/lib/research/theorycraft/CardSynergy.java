package thaumcraft.common.lib.research.theorycraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;

public class CardSynergy extends TheorycraftCard {
   public int getInspirationCost() {
      return 1;
   }

   public String getResearchCategory() {
      return "GOLEMANCY";
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.synergy.name", new Object[0])).func_150254_d();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.synergy.text", new Object[0])).func_150254_d();
   }

   public boolean initialize(EntityPlayer player, ResearchTableData data) {
      int tot = 0;
      int tot = tot + data.getTotal("ARTIFICE");
      tot += data.getTotal("ALCHEMY");
      tot += data.getTotal("THAUMATURGY");
      return tot >= 15;
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      int tot = 0;
      int tot = tot + data.getTotal("ARTIFICE");
      tot += data.getTotal("ALCHEMY");
      tot += data.getTotal("THAUMATURGY");
      if (tot < 15) {
         return false;
      } else {
         tot = 15;
         String[] cats = new String[]{"ARTIFICE", "ALCHEMY", "THAUMATURGY"};

         while(tot > 0) {
            String[] var5 = cats;
            int var6 = cats.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String category = var5[var7];
               if (data.getTotal(category) > 0) {
                  data.addTotal(category, -1);
                  --tot;
                  if (tot <= 0) {
                     break;
                  }
               }
            }
         }

         data.addTotal("GOLEMANCY", 30);
         return true;
      }
   }
}
