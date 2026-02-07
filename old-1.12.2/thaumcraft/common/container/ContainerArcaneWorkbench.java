package thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.blocks.world.ore.ShardType;
import thaumcraft.common.container.slot.SlotCraftingArcaneWorkbench;
import thaumcraft.common.container.slot.SlotCrystal;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.tiles.crafting.TileArcaneWorkbench;

public class ContainerArcaneWorkbench extends Container {
   private TileArcaneWorkbench tileEntity;
   private InventoryPlayer ip;
   public IInventory craftResult = new InventoryCraftResult();
   public static int[] xx = new int[]{64, 17, 112, 17, 112, 64};
   public static int[] yy = new int[]{13, 35, 35, 93, 93, 115};
   private int lastVis = -1;
   private long lastCheck = 0L;

   public ContainerArcaneWorkbench(InventoryPlayer par1InventoryPlayer, TileArcaneWorkbench e) {
      this.tileEntity = e;
      this.tileEntity.inventory.field_70465_c = this;
      this.ip = par1InventoryPlayer;
      this.func_75146_a(new SlotCraftingArcaneWorkbench(this.tileEntity, par1InventoryPlayer.field_70458_d, this.tileEntity.inventory, this.craftResult, 15, 160, 64));

      int var6;
      int var7;
      for(var6 = 0; var6 < 3; ++var6) {
         for(var7 = 0; var7 < 3; ++var7) {
            this.func_75146_a(new Slot(this.tileEntity.inventory, var7 + var6 * 3, 40 + var7 * 24, 40 + var6 * 24));
         }
      }

      ShardType[] var5 = ShardType.values();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         ShardType st = var5[var7];
         if (st.getMetadata() < 6) {
            this.func_75146_a(new SlotCrystal(st.getAspect(), this.tileEntity.inventory, 9 + st.getMetadata(), xx[st.getMetadata()], yy[st.getMetadata()]));
         }
      }

      for(var6 = 0; var6 < 3; ++var6) {
         for(var7 = 0; var7 < 9; ++var7) {
            this.func_75146_a(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 16 + var7 * 18, 151 + var6 * 18));
         }
      }

      for(var6 = 0; var6 < 9; ++var6) {
         this.func_75146_a(new Slot(par1InventoryPlayer, var6, 16 + var6 * 18, 209));
      }

      this.func_75130_a(this.tileEntity.inventory);
   }

   public void func_75132_a(IContainerListener par1ICrafting) {
      super.func_75132_a(par1ICrafting);
      this.tileEntity.getAura();
      par1ICrafting.func_71112_a(this, 0, this.tileEntity.auraVisServer);
   }

   public void func_75142_b() {
      super.func_75142_b();
      long t = System.currentTimeMillis();
      if (t > this.lastCheck) {
         this.lastCheck = t + 500L;
         this.tileEntity.getAura();
      }

      if (this.lastVis != this.tileEntity.auraVisServer) {
         this.func_75130_a(this.tileEntity.inventory);
      }

      for(int i = 0; i < this.field_75149_d.size(); ++i) {
         IContainerListener icrafting = (IContainerListener)this.field_75149_d.get(i);
         if (this.lastVis != this.tileEntity.auraVisServer) {
            icrafting.func_71112_a(this, 0, this.tileEntity.auraVisServer);
         }
      }

      this.lastVis = this.tileEntity.auraVisServer;
   }

   @SideOnly(Side.CLIENT)
   public void func_75137_b(int par1, int par2) {
      if (par1 == 0) {
         this.tileEntity.auraVisClient = par2;
      }

   }

   public void func_75130_a(IInventory par1IInventory) {
      int vis = ThaumcraftCraftingManager.findMatchingArcaneRecipeVis(this.tileEntity.inventory, this.ip.field_70458_d);
      boolean hasVis = this.tileEntity.func_145831_w().field_72995_K ? this.tileEntity.auraVisClient >= vis : this.tileEntity.auraVisServer >= vis;
      if (hasVis) {
         this.craftResult.func_70299_a(0, ThaumcraftCraftingManager.findMatchingArcaneRecipeResult(this.tileEntity.inventory, this.ip.field_70458_d));
      }

      if (this.craftResult.func_70301_a(0) == null) {
         InventoryCrafting ic = new InventoryCrafting(new ContainerDummy(), 3, 3);

         for(int a = 0; a < 9; ++a) {
            ic.func_70299_a(a, this.tileEntity.inventory.func_70301_a(a));
         }

         this.craftResult.func_70299_a(0, CraftingManager.func_77594_a().func_82787_a(ic, this.tileEntity.func_145831_w()));
      }

   }

   public void func_75134_a(EntityPlayer par1EntityPlayer) {
      super.func_75134_a(par1EntityPlayer);
      if (!this.tileEntity.func_145831_w().field_72995_K) {
         this.tileEntity.inventory.field_70465_c = null;
      }

   }

   public boolean func_75145_c(EntityPlayer par1EntityPlayer) {
      return this.tileEntity.func_145831_w().func_175625_s(this.tileEntity.func_174877_v()) != this.tileEntity ? false : par1EntityPlayer.func_174831_c(this.tileEntity.func_174877_v()) <= 64.0D;
   }

   public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int par1) {
      ItemStack var2 = null;
      Slot var3 = (Slot)this.field_75151_b.get(par1);
      if (var3 != null && var3.func_75216_d()) {
         ItemStack var4 = var3.func_75211_c();
         var2 = var4.func_77946_l();
         if (par1 == 0) {
            if (!this.func_75135_a(var4, 16, 52, true)) {
               return null;
            }

            var3.func_75220_a(var4, var2);
         } else if (par1 >= 16 && par1 < 52) {
            ShardType[] var6 = ShardType.values();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               ShardType st = var6[var8];
               if (st.getMetadata() < 6 && SlotCrystal.isValidCrystal(var4, st.getAspect())) {
                  if (!this.func_75135_a(var4, 10 + st.getMetadata(), 11 + st.getMetadata(), false)) {
                     return null;
                  }

                  if (var4.field_77994_a == 0) {
                     break;
                  }
               }
            }

            if (var4.field_77994_a != 0) {
               if (par1 >= 16 && par1 < 43) {
                  if (!this.func_75135_a(var4, 43, 52, false)) {
                     return null;
                  }
               } else if (par1 >= 43 && par1 < 52 && !this.func_75135_a(var4, 16, 43, false)) {
                  return null;
               }
            }
         } else if (!this.func_75135_a(var4, 16, 52, false)) {
            return null;
         }

         if (var4.field_77994_a == 0) {
            var3.func_75215_d((ItemStack)null);
         } else {
            var3.func_75218_e();
         }

         if (var4.field_77994_a == var2.field_77994_a) {
            return null;
         }

         var3.func_82870_a(this.ip.field_70458_d, var4);
      }

      return var2;
   }

   public boolean func_94530_a(ItemStack stack, Slot slot) {
      return slot.field_75224_c != this.craftResult && super.func_94530_a(stack, slot);
   }
}
