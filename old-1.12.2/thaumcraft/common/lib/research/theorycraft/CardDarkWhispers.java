package thaumcraft.common.lib.research.theorycraft;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;

public class CardDarkWhispers extends TheorycraftCard {
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
      return (new TextComponentTranslation("card.darkwhisper.name", new Object[0])).func_150254_d();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.darkwhisper.text", new Object[0])).func_150254_d();
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      int l = player.field_71068_ca;
      player.func_71013_b(10 + l);
      if (l > 0) {
         Iterator var4 = ResearchCategories.researchCategories.keySet().iterator();

         while(var4.hasNext()) {
            String k = (String)var4.next();
            if (!player.func_70681_au().nextBoolean()) {
               data.addTotal(k, MathHelper.func_76136_a(player.func_70681_au(), 0, Math.max(1, (int)Math.sqrt((double)l))));
            }
         }
      }

      data.addTotal("ELDRITCH", MathHelper.func_76136_a(player.func_70681_au(), Math.max(1, l / 5), Math.max(5, l / 2)));
      ThaumcraftApi.internalMethods.addWarpToPlayer(player, Math.max(1, (int)Math.sqrt((double)l)), IPlayerWarp.EnumWarpType.NORMAL);
      if (player.func_70681_au().nextBoolean()) {
         ++data.bonusDraws;
      }

      return true;
   }
}
