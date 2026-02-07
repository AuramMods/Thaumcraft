package thaumcraft.common.container.slot;

import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import thaumcraft.common.entities.construct.EntityArcaneBore;

public class SlotArcaneBorePickaxe extends SlotMobEquipment {
   public SlotArcaneBorePickaxe(EntityArcaneBore turret, int par3, int par4, int par5) {
      super(turret, par3, par4, par5);
   }

   public boolean func_75214_a(ItemStack stack) {
      return stack != null && stack.func_77973_b() != null && stack.func_77973_b() instanceof ItemPickaxe;
   }

   public void func_75218_e() {
   }
}
