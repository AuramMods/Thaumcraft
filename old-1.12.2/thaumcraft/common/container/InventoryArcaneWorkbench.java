package thaumcraft.common.container;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import thaumcraft.api.crafting.IArcaneWorkbench;

public class InventoryArcaneWorkbench extends InventoryCrafting implements IInventory, IArcaneWorkbench {
   public ItemStack[] field_70466_a;
   public Container field_70465_c;
   private final int inventoryWidth;
   private final int inventoryHeight;

   public InventoryArcaneWorkbench(Container p_i1807_1_, int width, int height) {
      super(p_i1807_1_, width, height);
      int var10000 = width * height;
      this.field_70466_a = new ItemStack[15];
      this.field_70465_c = p_i1807_1_;
      this.inventoryWidth = width;
      this.inventoryHeight = height;
   }

   public int func_70302_i_() {
      return this.field_70466_a.length;
   }

   public ItemStack func_70301_a(int index) {
      return index >= 15 ? null : this.field_70466_a[index];
   }

   public ItemStack func_70463_b(int row, int column) {
      return row >= 0 && row < this.inventoryWidth && column >= 0 && column <= this.inventoryHeight ? this.func_70301_a(row + column * this.inventoryWidth) : null;
   }

   public String func_70005_c_() {
      return "container.arcaneworkbench";
   }

   public ItemStack func_70304_b(int index) {
      return ItemStackHelper.func_188383_a(this.field_70466_a, index);
   }

   public ItemStack func_70298_a(int index, int count) {
      ItemStack itemstack = ItemStackHelper.func_188382_a(this.field_70466_a, index, count);
      if (itemstack != null) {
         try {
            this.field_70465_c.func_75130_a(this);
         } catch (Exception var5) {
         }
      }

      return itemstack;
   }

   public void func_70299_a(int index, ItemStack stack) {
      this.field_70466_a[index] = stack;

      try {
         this.field_70465_c.func_75130_a(this);
      } catch (Exception var4) {
      }

   }

   public void func_174888_l() {
      for(int i = 0; i < this.field_70466_a.length; ++i) {
         this.field_70466_a[i] = null;
      }

   }

   public int func_174923_h() {
      return this.inventoryHeight;
   }

   public int func_174922_i() {
      return this.inventoryWidth;
   }

   public boolean func_94041_b(int i, ItemStack itemstack) {
      return true;
   }
}
