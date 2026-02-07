package thaumcraft.common.lib.research.theorycraft;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.theorycraft.ResearchTableData;
import thaumcraft.api.research.theorycraft.TheorycraftCard;
import thaumcraft.common.lib.utils.InventoryUtils;

public class CardReactions extends TheorycraftCard {
   Aspect aspect1;
   Aspect aspect2;

   public NBTTagCompound serialize() {
      NBTTagCompound nbt = super.serialize();
      nbt.func_74778_a("aspect1", this.aspect1.getTag());
      nbt.func_74778_a("aspect2", this.aspect2.getTag());
      return nbt;
   }

   public void deserialize(NBTTagCompound nbt) {
      super.deserialize(nbt);
      this.aspect1 = Aspect.getAspect(nbt.func_74779_i("aspect1"));
      this.aspect2 = Aspect.getAspect(nbt.func_74779_i("aspect2"));
   }

   public boolean initialize(EntityPlayer player, ResearchTableData data) {
      Random r = new Random(this.getSeed());
      int num = MathHelper.func_76136_a(r, 0, Aspect.getCompoundAspects().size() - 1);
      this.aspect1 = (Aspect)Aspect.getCompoundAspects().get(num);

      int num2;
      for(num2 = num; num2 == num; num2 = MathHelper.func_76136_a(r, 0, Aspect.getCompoundAspects().size() - 1)) {
      }

      this.aspect2 = (Aspect)Aspect.getCompoundAspects().get(num2);
      return true;
   }

   public int getInspirationCost() {
      return 1;
   }

   public String getResearchCategory() {
      return "ALCHEMY";
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.reactions.name", new Object[0])).func_150254_d();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.reactions.text", new Object[]{TextFormatting.BOLD + this.aspect1.getName() + TextFormatting.RESET, TextFormatting.BOLD + this.aspect2.getName() + TextFormatting.RESET})).func_150254_d();
   }

   public ItemStack[] getRequiredItems() {
      return new ItemStack[]{ThaumcraftApiHelper.makeCrystal(this.aspect1), ThaumcraftApiHelper.makeCrystal(this.aspect2)};
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      if (InventoryUtils.isPlayerCarryingAmount(player, this.getRequiredItems()[0], false) && InventoryUtils.isPlayerCarryingAmount(player, this.getRequiredItems()[1], false)) {
         InventoryUtils.consumeInventoryItem(player, this.getRequiredItems()[0], true, false);
         InventoryUtils.consumeInventoryItem(player, this.getRequiredItems()[1], true, false);
         data.addTotal(this.getResearchCategory(), 20);
         return true;
      } else {
         return false;
      }
   }
}
