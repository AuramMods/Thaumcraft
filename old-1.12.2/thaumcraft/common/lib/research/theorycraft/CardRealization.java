package thaumcraft.common.lib.research.theorycraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;

public class CardRealization extends TheorycraftCard {
   public int getInspirationCost() {
      return 1;
   }

   public String getResearchCategory() {
      return "ELDRITCH";
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.realization.name", new Object[0])).func_150254_d();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.realization.text", new Object[0])).func_150254_d();
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      String[] s = (String[])ResearchCategories.researchCategories.keySet().toArray(new String[0]);
      data.addTotal(s[player.func_70681_au().nextInt(s.length)], 5);
      data.addTotal(s[player.func_70681_au().nextInt(s.length)], 5);
      data.addTotal("ELDRITCH", 10);
      ThaumcraftApi.internalMethods.addWarpToPlayer(player, 5, IPlayerWarp.EnumWarpType.TEMPORARY);
      if (player.func_70681_au().nextBoolean()) {
         ThaumcraftApi.internalMethods.addWarpToPlayer(player, 1, IPlayerWarp.EnumWarpType.NORMAL);
      }

      return true;
   }
}
