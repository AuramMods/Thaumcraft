package thaumcraft.common.lib.research.theorycraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;

public class CardTruth extends TheorycraftCard {
   public int getInspirationCost() {
      return 1;
   }

   public String getResearchCategory() {
      return "ELDRITCH";
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.truth.name", new Object[0])).func_150254_d();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.truth.text", new Object[0])).func_150254_d();
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      data.addTotal("ELDRITCH", MathHelper.func_76136_a(player.func_70681_au(), 5, 15));
      ++data.bonusDraws;
      ThaumcraftApi.internalMethods.addWarpToPlayer(player, 3, IPlayerWarp.EnumWarpType.TEMPORARY);
      return true;
   }
}
