package thaumcraft.common.container.slot;

import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import thaumcraft.common.entities.construct.EntityTurretCrossbow;

public class SlotTurretBasic extends SlotMobEquipment {
   public SlotTurretBasic(EntityTurretCrossbow turret, int par3, int par4, int par5) {
      super(turret, par3, par4, par5);
   }

   public boolean func_75214_a(ItemStack stack) {
      return stack != null && stack.func_77973_b() != null && stack.func_77973_b() instanceof ItemArrow;
   }

   public void func_75218_e() {
   }
}
