package thaumcraft.common.container.slot;

import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class SlotMobEquipment extends Slot {
   EntityLiving entity;

   public SlotMobEquipment(EntityLiving entity, int par3, int par4, int par5) {
      super((IInventory)null, par3, par4, par5);
      this.entity = entity;
   }

   public ItemStack func_75211_c() {
      return this.entity.func_184586_b(EnumHand.MAIN_HAND);
   }

   public void func_75215_d(ItemStack stack) {
      this.entity.func_184611_a(EnumHand.MAIN_HAND, stack);
      if (stack != null && stack.field_77994_a > this.func_75219_a()) {
         stack.field_77994_a = this.func_75219_a();
      }

      this.func_75218_e();
   }

   public void func_75218_e() {
   }

   public int func_75219_a() {
      return 64;
   }

   public ItemStack func_75209_a(int amount) {
      if (this.func_75211_c() != null) {
         ItemStack itemstack;
         if (this.func_75211_c().field_77994_a <= amount) {
            itemstack = this.func_75211_c();
            this.func_75215_d((ItemStack)null);
            return itemstack;
         } else {
            itemstack = this.func_75211_c().func_77979_a(amount);
            if (this.func_75211_c().field_77994_a == 0) {
               this.func_75215_d((ItemStack)null);
            }

            return itemstack;
         }
      } else {
         return null;
      }
   }

   public boolean func_75217_a(IInventory inv, int slotIn) {
      return slotIn == this.getSlotIndex();
   }
}
