package thaumcraft.common.lib.research.theorycraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;

public class CardSpellbinding extends TheorycraftCard {
   public int getInspirationCost() {
      return 1;
   }

   public String getResearchCategory() {
      return "THAUMATURGY";
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.spellbinding.name", new Object[0])).func_150254_d();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.spellbinding.text", new Object[0])).func_150254_d();
   }

   public boolean initialize(EntityPlayer player, ResearchTableData data) {
      return player.field_71068_ca > 0;
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      if (player.field_71068_ca <= 0) {
         return false;
      } else {
         int l = Math.min(5, player.field_71068_ca);
         data.addTotal(this.getResearchCategory(), l * 5);
         player.func_71013_b(l);
         return true;
      }
   }
}
