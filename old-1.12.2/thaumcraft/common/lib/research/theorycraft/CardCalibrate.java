package thaumcraft.common.lib.research.theorycraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;

public class CardCalibrate extends TheorycraftCard {
   public int getInspirationCost() {
      return 1;
   }

   public String getResearchCategory() {
      return "ARTIFICE";
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.calibrate.name", new Object[0])).func_150254_d();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.calibrate.text", new Object[0])).func_150254_d();
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      data.addTotal(this.getResearchCategory(), 10);
      ++data.bonusDraws;
      return true;
   }
}
