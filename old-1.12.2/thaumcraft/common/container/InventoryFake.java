package thaumcraft.common.container;

import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class InventoryFake implements IInventory {
   private ItemStack[] stackList;

   public InventoryFake(ItemStack[] stackList) {
      this.stackList = stackList;
   }

   public InventoryFake(ArrayList<ItemStack> stackList) {
      this.stackList = (ItemStack[])stackList.toArray(new ItemStack[0]);
   }

   public int func_70302_i_() {
      return this.stackList.length;
   }

   public ItemStack func_70301_a(int par1) {
      return par1 >= this.func_70302_i_() ? null : this.stackList[par1];
   }

   public ItemStack func_70304_b(int par1) {
      if (this.stackList[par1] != null) {
         ItemStack var2 = this.stackList[par1];
         this.stackList[par1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public ItemStack func_70298_a(int par1, int par2) {
      if (this.stackList[par1] != null) {
         ItemStack var3;
         if (this.stackList[par1].field_77994_a <= par2) {
            var3 = this.stackList[par1];
            this.stackList[par1] = null;
            return var3;
         } else {
            var3 = this.stackList[par1].func_77979_a(par2);
            if (this.stackList[par1].field_77994_a == 0) {
               this.stackList[par1] = null;
            }

            return var3;
         }
      } else {
         return null;
      }
   }

   public void func_70299_a(int par1, ItemStack par2ItemStack) {
      this.stackList[par1] = par2ItemStack;
   }

   public int func_70297_j_() {
      return 64;
   }

   public boolean func_70300_a(EntityPlayer par1EntityPlayer) {
      return true;
   }

   public boolean func_94041_b(int i, ItemStack itemstack) {
      return true;
   }

   public void func_70296_d() {
   }

   public String func_70005_c_() {
      return "container.fake";
   }

   public boolean func_145818_k_() {
      return false;
   }

   public ITextComponent func_145748_c_() {
      return null;
   }

   public void func_174889_b(EntityPlayer player) {
   }

   public void func_174886_c(EntityPlayer player) {
   }

   public int func_174887_a_(int id) {
      return 0;
   }

   public void func_174885_b(int id, int value) {
   }

   public int func_174890_g() {
      return 0;
   }

   public void func_174888_l() {
   }
}
