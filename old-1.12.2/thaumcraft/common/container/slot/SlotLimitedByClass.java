package thaumcraft.common.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotLimitedByClass extends Slot {
   Class clazz = Object.class;
   int limit = 64;

   public SlotLimitedByClass(Class clazz, IInventory par2IInventory, int par3, int par4, int par5) {
      super(par2IInventory, par3, par4, par5);
      this.clazz = clazz;
   }

   public SlotLimitedByClass(Class clazz, int limit, IInventory par2IInventory, int par3, int par4, int par5) {
      super(par2IInventory, par3, par4, par5);
      this.clazz = clazz;
      this.limit = limit;
   }

   public boolean func_75214_a(ItemStack par1ItemStack) {
      return par1ItemStack != null && par1ItemStack.func_77973_b() != null && this.clazz.isAssignableFrom(par1ItemStack.func_77973_b().getClass());
   }

   public int func_75219_a() {
      return this.limit;
   }
}
