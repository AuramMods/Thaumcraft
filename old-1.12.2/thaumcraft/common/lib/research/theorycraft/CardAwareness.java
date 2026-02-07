package thaumcraft.common.lib.research.theorycraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;

public class CardAwareness extends TheorycraftCard {
   public int getInspirationCost() {
      return 1;
   }

   public String getResearchCategory() {
      return "THAUMATURGY";
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.awareness.name", new Object[0])).func_150254_d();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.awareness.text", new Object[0])).func_150254_d();
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      data.addTotal(this.getResearchCategory(), 15);
      if ((double)player.func_70681_au().nextFloat() < 0.33D) {
         data.addTotal("ELDRITCH", MathHelper.func_76136_a(player.func_70681_au(), 1, 5));
         ThaumcraftApi.internalMethods.addWarpToPlayer(player, 1, IPlayerWarp.EnumWarpType.NORMAL);
      }

      return true;
   }
}
