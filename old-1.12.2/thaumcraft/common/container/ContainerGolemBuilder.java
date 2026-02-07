package thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.container.slot.SlotOutput;
import thaumcraft.common.tiles.crafting.TileGolemBuilder;

public class ContainerGolemBuilder extends Container {
   private TileGolemBuilder builder;
   public static boolean redo = false;
   private int lastCost;
   private int lastMaxCost;

   public ContainerGolemBuilder(InventoryPlayer par1InventoryPlayer, TileGolemBuilder tileEntity) {
      this.builder = tileEntity;
      this.func_75146_a(new SlotOutput(tileEntity, 0, 160, 104));

      int i;
      for(i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(par1InventoryPlayer, j + i * 9 + 9, 24 + j * 18, 142 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new Slot(par1InventoryPlayer, i, 24 + i * 18, 200));
      }

   }

   public ItemStack func_184996_a(int slotId, int clickedButton, ClickType mode, EntityPlayer playerIn) {
      redo = true;
      return super.func_184996_a(slotId, clickedButton, mode, playerIn);
   }

   public void func_75141_a(int p_75141_1_, ItemStack p_75141_2_) {
      redo = true;
      super.func_75141_a(p_75141_1_, p_75141_2_);
   }

   public boolean func_75140_a(EntityPlayer p, int button) {
      if (button == 99) {
         redo = true;
      }

      return false;
   }

   public void func_75132_a(IContainerListener par1ICrafting) {
      super.func_75132_a(par1ICrafting);
      par1ICrafting.func_71112_a(this, 0, this.builder.cost);
   }

   public void func_75142_b() {
      super.func_75142_b();

      for(int i = 0; i < this.field_75149_d.size(); ++i) {
         IContainerListener icrafting = (IContainerListener)this.field_75149_d.get(i);
         if (this.lastCost != this.builder.cost) {
            icrafting.func_71112_a(this, 0, this.builder.cost);
         }

         if (this.lastMaxCost != this.builder.maxCost) {
            icrafting.func_71112_a(this, 1, this.builder.maxCost);
         }
      }

      this.lastCost = this.builder.cost;
      this.lastMaxCost = this.builder.maxCost;
   }

   @SideOnly(Side.CLIENT)
   public void func_75137_b(int par1, int par2) {
      if (par1 == 0) {
         this.builder.cost = par2;
      }

      if (par1 == 1) {
         this.builder.maxCost = par2;
      }

   }

   public boolean func_75145_c(EntityPlayer par1EntityPlayer) {
      return this.builder.func_70300_a(par1EntityPlayer);
   }

   public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int slot) {
      ItemStack stack = null;
      Slot slotObject = (Slot)this.field_75151_b.get(slot);
      if (slotObject != null && slotObject.func_75216_d()) {
         ItemStack stackInSlot = slotObject.func_75211_c();
         stack = stackInSlot.func_77946_l();
         if (slot == 0) {
            if (!this.builder.func_94041_b(slot, stackInSlot) || !this.func_75135_a(stackInSlot, 1, this.field_75151_b.size(), true)) {
               return null;
            }
         } else if (!this.builder.func_94041_b(slot, stackInSlot) || !this.func_75135_a(stackInSlot, 0, 1, false)) {
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
