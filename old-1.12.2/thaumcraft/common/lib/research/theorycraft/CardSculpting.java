package thaumcraft.common.lib.research.theorycraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;
import thaumcraft.common.lib.utils.InventoryUtils;

public class CardSculpting extends TheorycraftCard {
   public int getInspirationCost() {
      return 1;
   }

   public String getResearchCategory() {
      return "GOLEMANCY";
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.sculpting.name", new Object[0])).func_150254_d();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.sculpting.text", new Object[0])).func_150254_d();
   }

   public ItemStack[] getRequiredItems() {
      return new ItemStack[]{new ItemStack(Items.field_151119_aD)};
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      if (InventoryUtils.consumeInventoryItem(player, this.getRequiredItems()[0], false, false)) {
         data.addTotal(this.getResearchCategory(), 15);
         ++data.bonusDraws;
         return true;
      } else {
         return false;
      }
   }
}
