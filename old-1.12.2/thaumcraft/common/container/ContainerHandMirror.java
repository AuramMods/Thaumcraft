package thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.common.items.tools.ItemHandMirror;

public class ContainerHandMirror extends Container {
   private World worldObj;
   private int posX;
   private int posY;
   private int posZ;
   public IInventory input = new InventoryHandMirror(this);
   ItemStack mirror = null;
   EntityPlayer player = null;

   public ContainerHandMirror(InventoryPlayer iinventory, World par2World, int par3, int par4, int par5) {
      this.worldObj = par2World;
      this.posX = par3;
      this.posY = par4;
      this.posZ = par5;
      this.player = iinventory.field_70458_d;
      this.mirror = iinventory.func_70448_g();
      this.func_75146_a(new Slot(this.input, 0, 80, 24));
      this.bindPlayerInventory(iinventory);
      this.func_75130_a(this.input);
   }

   protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
      int i;
      for(i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
      }

   }

   public void func_75130_a(IInventory par1IInventory) {
      if (this.input.func_70301_a(0) != null && ItemStack.func_77989_b(this.input.func_70301_a(0), this.mirror)) {
         this.player.field_71070_bA = this.player.field_71069_bz;
      } else if (!this.worldObj.field_72995_K && this.input.func_70301_a(0) != null && this.player != null && ItemHandMirror.transport(this.mirror, this.input.func_70301_a(0), this.player, this.worldObj)) {
         this.input.func_70299_a(0, (ItemStack)null);

         for(int var4 = 0; var4 < this.field_75149_d.size(); ++var4) {
            ((IContainerListener)this.field_75149_d.get(var4)).func_71111_a(this, 0, (ItemStack)null);
         }
      }

   }

   public ItemStack func_184996_a(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
      try {
         ItemStack s = this.func_75139_a(slotId).func_75211_c();
         if (s != null && s.func_77973_b() instanceof ItemHandMirror) {
            return null;
         }
      } catch (Exception var6) {
      }

      return super.func_184996_a(slotId, dragType, clickTypeIn, player);
   }

   public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int slot) {
      ItemStack stack = null;
      Slot slotObject = (Slot)this.field_75151_b.get(slot);
      if (slotObject != null && slotObject.func_75216_d() && !(slotObject.func_75211_c().func_77973_b() instanceof ItemHandMirror)) {
         ItemStack stackInSlot = slotObject.func_75211_c();
         stack = stackInSlot.func_77946_l();
         if (slot == 0) {
            if (!this.mergeItemStack(stackInSlot, 1, this.field_75151_b.size(), true, 64)) {
               return null;
            }
         } else if (!this.mergeItemStack(stackInSlot, 0, 1, false, 64)) {
            return null;
         }

         if (stackInSlot.field_77994_a == 0) {
            slotObject.func_75215_d((ItemStack)null);
         } else {
            slotObject.func_75218_e();
         }
      }

      return stack;
   }

   public boolean func_75145_c(EntityPlayer var1) {
      return true;
   }

   public void func_75134_a(EntityPlayer par1EntityPlayer) {
      super.func_75134_a(par1EntityPlayer);
      if (!this.worldObj.field_72995_K) {
         for(int var2 = 0; var2 < 1; ++var2) {
            ItemStack var3 = this.input.func_70304_b(var2);
            if (var3 != null) {
               par1EntityPlayer.func_71019_a(var3, false);
            }
         }
      }

   }

   protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4, int limit) {
      boolean var5 = false;
      int var6 = par2;
      if (par4) {
         var6 = par3 - 1;
      }

      Slot var7;
      ItemStack var8;
      if (par1ItemStack.func_77985_e()) {
         while(par1ItemStack.field_77994_a > 0 && (!par4 && var6 < par3 || par4 && var6 >= par2)) {
            var7 = (Slot)this.field_75151_b.get(var6);
            var8 = var7.func_75211_c();
            if (var8 != null && var8.func_77973_b() == par1ItemStack.func_77973_b() && (!par1ItemStack.func_77981_g() || par1ItemStack.func_77952_i() == var8.func_77952_i()) && ItemStack.func_77970_a(par1ItemStack, var8)) {
               int var9 = var8.field_77994_a + par1ItemStack.field_77994_a;
               if (var9 <= Math.min(par1ItemStack.func_77976_d(), limit)) {
                  par1ItemStack.field_77994_a = 0;
                  var8.field_77994_a = var9;
                  var7.func_75218_e();
                  var5 = true;
               } else if (var8.field_77994_a < Math.min(par1ItemStack.func_77976_d(), limit)) {
                  par1ItemStack.field_77994_a -= Math.min(par1ItemStack.func_77976_d(), limit) - var8.field_77994_a;
                  var8.field_77994_a = Math.min(par1ItemStack.func_77976_d(), limit);
                  var7.func_75218_e();
                  var5 = true;
               }
            }

            if (par4) {
               --var6;
            } else {
               ++var6;
            }
         }
      }

      if (par1ItemStack.field_77994_a > 0) {
         if (par4) {
            var6 = par3 - 1;
         } else {
            var6 = par2;
         }

         while(!par4 && var6 < par3 || par4 && var6 >= par2) {
            var7 = (Slot)this.field_75151_b.get(var6);
            var8 = var7.func_75211_c();
            if (var8 == null) {
               ItemStack res = par1ItemStack.func_77946_l();
               res.field_77994_a = Math.min(res.field_77994_a, limit);
               var7.func_75215_d(res);
               var7.func_75218_e();
               par1ItemStack.field_77994_a -= res.field_77994_a;
               var5 = true;
               break;
            }

            if (par4) {
               --var6;
            } else {
               ++var6;
            }
         }
      }

      return var5;
   }
}
