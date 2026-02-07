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

public class CardSynthesis extends TheorycraftCard {
   Aspect aspect1;
   Aspect aspect2;
   Aspect aspect3;

   public NBTTagCompound serialize() {
      NBTTagCompound nbt = super.serialize();
      nbt.func_74778_a("aspect1", this.aspect1.getTag());
      nbt.func_74778_a("aspect2", this.aspect2.getTag());
      nbt.func_74778_a("aspect3", this.aspect3.getTag());
      return nbt;
   }

   public void deserialize(NBTTagCompound nbt) {
      super.deserialize(nbt);
      this.aspect1 = Aspect.getAspect(nbt.func_74779_i("aspect1"));
      this.aspect2 = Aspect.getAspect(nbt.func_74779_i("aspect2"));
      this.aspect3 = Aspect.getAspect(nbt.func_74779_i("aspect3"));
   }

   public boolean initialize(EntityPlayer player, ResearchTableData data) {
      Random r = new Random(this.getSeed());
      int num = MathHelper.func_76136_a(r, 0, Aspect.getCompoundAspects().size() - 1);
      this.aspect3 = (Aspect)Aspect.getCompoundAspects().get(num);
      this.aspect1 = this.aspect3.getComponents()[0];
      this.aspect2 = this.aspect3.getComponents()[1];
      return true;
   }

   public int getInspirationCost() {
      return 1;
   }

   public String getResearchCategory() {
      return "ALCHEMY";
   }

   public String getLocalizedName() {
      return (new TextComponentTranslation("card.synthesis.name", new Object[0])).func_150260_c();
   }

   public String getLocalizedText() {
      return (new TextComponentTranslation("card.synthesis.text", new Object[]{TextFormatting.BOLD + this.aspect1.getName() + TextFormatting.RESET, TextFormatting.BOLD + this.aspect2.getName() + TextFormatting.RESET})).func_150260_c();
   }

   public ItemStack[] getRequiredItems() {
      return new ItemStack[]{ThaumcraftApiHelper.makeCrystal(this.aspect1), ThaumcraftApiHelper.makeCrystal(this.aspect2), null};
   }

   public boolean activate(EntityPlayer player, ResearchTableData data) {
      ItemStack res = ThaumcraftApiHelper.makeCrystal(this.aspect3);
      if (InventoryUtils.isPlayerCarryingAmount(player, this.getRequiredItems()[0], false) && InventoryUtils.isPlayerCarryingAmount(player, this.getRequiredItems()[1], false) && InventoryUtils.isPlayerCarryingAmount(player, res, false)) {
         InventoryUtils.consumeInventoryItem(player, this.getRequiredItems()[0], true, false);
         InventoryUtils.consumeInventoryItem(player, this.getRequiredItems()[1], true, false);
         InventoryUtils.consumeInventoryItem(player, res, true, false);
         data.addTotal(this.getResearchCategory(), 30);
         return true;
      } else {
         return false;
      }
   }
}
