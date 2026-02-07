package thaumcraft.common.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.resources.ItemCrystalEssence;

public class SlotCrystal extends Slot {
   private Aspect aspect;

   public SlotCrystal(Aspect aspect, IInventory par2IInventory, int par3, int par4, int par5) {
      super(par2IInventory, par3, par4, par5);
      this.aspect = aspect;
   }

   public boolean func_75214_a(ItemStack stack) {
      return isValidCrystal(stack, this.aspect);
   }

   public static boolean isValidCrystal(ItemStack stack, Aspect aspect) {
      return stack != null && stack.func_77973_b() != null && stack.func_77973_b() instanceof ItemCrystalEssence && ((ItemCrystalEssence)stack.func_77973_b()).getAspects(stack).getAspects()[0] == aspect;
   }
}
