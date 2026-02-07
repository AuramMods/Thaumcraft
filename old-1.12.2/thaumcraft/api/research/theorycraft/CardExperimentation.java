package thaumcraft.api.research.theorycraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import thaumcraft.api.research.ResearchCategories;

public class CardExperimentation extends TheorycraftCard {
   public int getInspirationCost() {
      return 1;
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.experimentation.name", new Object[0])).func_150260_c();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.experimentation.text", new Object[0])).func_150260_c();
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      try {
         String[] s = (String[])ResearchCategories.researchCategories.keySet().toArray(new String[0]);
         String cat = s[player.func_70681_au().nextInt(s.length)];
         data.addTotal(cat, MathHelper.func_76136_a(player.func_70681_au(), 10, 25));
         return true;
      } catch (Exception var5) {
         return false;
      }
   }
}
