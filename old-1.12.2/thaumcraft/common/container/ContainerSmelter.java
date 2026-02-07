package thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.container.slot.SlotLimitedHasAspects;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.tiles.essentia.TileSmelter;

public class ContainerSmelter extends Container {
   private TileSmelter furnace;
   private int lastCookTime;
   private int lastBurnTime;
   private int lastItemBurnTime;
   private int lastVis;
   private int lastSmelt;
   private int lastFlux;

   public ContainerSmelter(InventoryPlayer par1InventoryPlayer, TileSmelter tileEntity) {
      this.furnace = tileEntity;
      this.func_75146_a(new SlotLimitedHasAspects(tileEntity, 0, 80, 8));
      this.func_75146_a(new Slot(tileEntity, 1, 80, 48));

      int i;
      for(i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
      }

   }

   public void func_75132_a(IContainerListener listener) {
      super.func_75132_a(listener);
      listener.func_175173_a(this, this.furnace);
      listener.func_71112_a(this, 0, this.furnace.furnaceCookTime);
      listener.func_71112_a(this, 1, this.furnace.furnaceBurnTime);
      listener.func_71112_a(this, 2, this.furnace.currentItemBurnTime);
      listener.func_71112_a(this, 3, this.furnace.vis);
      listener.func_71112_a(this, 4, this.furnace.smeltTime);
   }

   public void func_75142_b() {
      super.func_75142_b();

      for(int i = 0; i < this.field_75149_d.size(); ++i) {
         IContainerListener icrafting = (IContainerListener)this.field_75149_d.get(i);
         if (this.lastCookTime != this.furnace.furnaceCookTime) {
            icrafting.func_71112_a(this, 0, this.furnace.furnaceCookTime);
         }

         if (this.lastBurnTime != this.furnace.furnaceBurnTime) {
            icrafting.func_71112_a(this, 1, this.furnace.furnaceBurnTime);
         }

         if (this.lastItemBurnTime != this.furnace.currentItemBurnTime) {
            icrafting.func_71112_a(this, 2, this.furnace.currentItemBurnTime);
         }

         if (this.lastVis != this.furnace.vis) {
            icrafting.func_71112_a(this, 3, this.furnace.vis);
         }

         if (this.lastSmelt != this.furnace.smeltTime) {
            icrafting.func_71112_a(this, 4, this.furnace.smeltTime);
         }
      }

      this.lastCookTime = this.furnace.furnaceCookTime;
      this.lastBurnTime = this.furnace.furnaceBurnTime;
      this.lastItemBurnTime = this.furnace.currentItemBurnTime;
      this.lastVis = this.furnace.vis;
      this.lastSmelt = this.furnace.smeltTime;
   }

   @SideOnly(Side.CLIENT)
   public void func_75137_b(int par1, int par2) {
      if (par1 == 0) {
         this.furnace.furnaceCookTime = par2;
      }

      if (par1 == 1) {
         this.furnace.furnaceBurnTime = par2;
      }

      if (par1 == 2) {
         this.furnace.currentItemBurnTime = par2;
      }

      if (par1 == 3) {
         this.furnace.vis = par2;
      }

      if (par1 == 4) {
         this.furnace.smeltTime = par2;
      }

   }

   public boolean func_75145_c(EntityPlayer par1EntityPlayer) {
      return this.furnace.func_70300_a(par1EntityPlayer);
   }

   public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int par2) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.field_75151_b.get(par2);
      if (slot != null && slot.func_75216_d()) {
         ItemStack itemstack1 = slot.func_75211_c();
         itemstack = itemstack1.func_77946_l();
         if (par2 != 1 && par2 != 0) {
            AspectList al = ThaumcraftCraftingManager.getObjectTags(itemstack1);
            if (TileSmelter.isItemFuel(itemstack1)) {
               if (!this.func_75135_a(itemstack1, 1, 2, false) && !this.func_75135_a(itemstack1, 0, 1, false)) {
                  return null;
               }
            } else if (al != null && al.size() > 0) {
               if (!this.func_75135_a(itemstack1, 0, 1, false)) {
                  return null;
               }
            } else if (par2 >= 2 && par2 < 29) {
               if (!this.func_75135_a(itemstack1, 29, 38, false)) {
                  return null;
               }
            } else if (par2 >= 29 && par2 < 38 && !this.func_75135_a(itemstack1, 2, 29, false)) {
               return null;
            }
         } else if (!this.func_75135_a(itemstack1, 2, 38, false)) {
            return null;
         }

         if (itemstack1.field_77994_a == 0) {
            slot.func_75215_d((ItemStack)null);
         } else {
            slot.func_75218_e();
         }

         if (itemstack1.field_77994_a == itemstack.field_77994_a) {
            return null;
         }

         slot.func_82870_a(par1EntityPlayer, itemstack1);
      }

      return itemstack;
   }
}
