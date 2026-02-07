package thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.common.container.slot.SlotLimitedByClass;
import thaumcraft.common.items.casters.ItemFocus;
import thaumcraft.common.items.casters.ItemFocusPouch;

public class ContainerFocusPouch extends Container {
   private World worldObj;
   private int posX;
   private int posY;
   private int posZ;
   private int blockSlot;
   public IInventory input = new InventoryFocusPouch(this);
   ItemStack pouch = null;
   EntityPlayer player = null;

   public ContainerFocusPouch(InventoryPlayer iinventory, World par2World, int par3, int par4, int par5) {
      this.worldObj = par2World;
      this.posX = par3;
      this.posY = par4;
      this.posZ = par5;
      this.player = iinventory.field_70458_d;
      this.pouch = iinventory.func_70448_g();
      this.blockSlot = iinventory.field_70461_c + 45;

      for(int a = 0; a < 18; ++a) {
         this.func_75146_a(new SlotLimitedByClass(ItemFocus.class, this.input, a, 37 + a % 6 * 18, 51 + a / 6 * 18));
      }

      this.bindPlayerInventory(iinventory);
      if (!par2World.field_72995_K) {
         try {
            ((InventoryFocusPouch)this.input).stackList = ((ItemFocusPouch)this.pouch.func_77973_b()).getInventory(this.pouch);
         } catch (Exception var7) {
         }
      }

      this.func_75130_a(this.input);
   }

   protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
      int i;
      for(i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 151 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new Slot(inventoryPlayer, i, 8 + i * 18, 209));
      }

   }

   public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int slot) {
      if (slot == this.blockSlot) {
         return null;
      } else {
         ItemStack stack = null;
         Slot slotObject = (Slot)this.field_75151_b.get(slot);
         if (slotObject != null && slotObject.func_75216_d()) {
            ItemStack stackInSlot = slotObject.func_75211_c();
            stack = stackInSlot.func_77946_l();
            if (slot < 18) {
               if (!this.input.func_94041_b(slot, stackInSlot) || !this.func_75135_a(stackInSlot, 18, this.field_75151_b.size(), true)) {
                  return null;
               }
            } else if (!this.input.func_94041_b(slot, stackInSlot) || !this.func_75135_a(stackInSlot, 0, 18, false)) {
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
   }

   public boolean func_75145_c(EntityPlayer var1) {
      return true;
   }

   public ItemStack func_184996_a(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
      return slotId == this.blockSlot ? null : super.func_184996_a(slotId, dragType, clickTypeIn, player);
   }

   public void func_75134_a(EntityPlayer par1EntityPlayer) {
      super.func_75134_a(par1EntityPlayer);
      if (!this.worldObj.field_72995_K) {
         ((ItemFocusPouch)this.pouch.func_77973_b()).setInventory(this.pouch, ((InventoryFocusPouch)this.input).stackList);
         if (this.player == null) {
            return;
         }

         if (this.player.func_184586_b(this.player.func_184600_cs()) != null && this.player.func_184586_b(this.player.func_184600_cs()).func_77969_a(this.pouch)) {
            this.player.func_184611_a(this.player.func_184600_cs(), this.pouch);
         }

         this.player.field_71071_by.func_70296_d();
      }

   }
}
