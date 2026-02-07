package thaumcraft.common.lib.research.theorycraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;

public class CardGlyphs extends TheorycraftCard {
   public boolean isAidOnly() {
      return true;
   }

   public int getInspirationCost() {
      return 1;
   }

   public String getResearchCategory() {
      return "ELDRITCH";
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.glyph.name", new Object[0])).func_150254_d();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.glyph.text", new Object[0])).func_150254_d();
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      String[] s = (String[])ResearchCategories.researchCategories.keySet().toArray(new String[0]);
      data.addTotal(s[player.func_70681_au().nextInt(s.length)], 5);
      data.addTotal("ELDRITCH", MathHelper.func_76136_a(player.func_70681_au(), 10, 20));
      ThaumcraftApi.internalMethods.addWarpToPlayer(player, 5, IPlayerWarp.EnumWarpType.TEMPORARY);
      return true;
   }
}
