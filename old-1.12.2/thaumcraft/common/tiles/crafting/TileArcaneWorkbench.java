package thaumcraft.common.tiles.crafting;

import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.container.InventoryArcaneWorkbench;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.world.aura.AuraChunk;
import thaumcraft.common.world.aura.AuraHandler;

public class TileArcaneWorkbench extends TileThaumcraft {
   public InventoryArcaneWorkbench inventory = new InventoryArcaneWorkbench((Container)null, 3, 3);
   public int auraVisServer = 0;
   public int auraVisClient = 0;

   public void readSyncNBT(NBTTagCompound par1NBTTagCompound) {
      NBTTagList var2 = par1NBTTagCompound.func_150295_c("Inventory", 10);
      this.inventory.field_70466_a = new ItemStack[17];

      for(int var3 = 0; var3 < var2.func_74745_c(); ++var3) {
         NBTTagCompound var4 = var2.func_150305_b(var3);
         int var5 = var4.func_74771_c("Slot") & 255;
         if (var5 >= 0 && var5 < this.inventory.field_70466_a.length) {
            this.inventory.field_70466_a[var5] = ItemStack.func_77949_a(var4);
         }
      }

   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound par1NBTTagCompound) {
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.inventory.field_70466_a.length; ++var3) {
         if (this.inventory.field_70466_a[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.func_74774_a("Slot", (byte)var3);
            this.inventory.field_70466_a[var3].func_77955_b(var4);
            var2.func_74742_a(var4);
         }
      }

      par1NBTTagCompound.func_74782_a("Inventory", var2);
      return par1NBTTagCompound;
   }

   public void getAura() {
      if (!this.func_145831_w().field_72995_K) {
         int t = 0;
         if (this.field_145850_b.func_180495_p(this.func_174877_v().func_177984_a()).func_177230_c() != BlocksTC.arcaneWorkbenchCharger) {
            t = (int)AuraHandler.getVis(this.func_145831_w(), this.func_174877_v());
         } else {
            int sx = this.field_174879_c.func_177958_n() >> 4;
            int sz = this.field_174879_c.func_177952_p() >> 4;

            for(int xx = -1; xx <= 1; ++xx) {
               for(int zz = -1; zz <= 1; ++zz) {
                  AuraChunk ac = AuraHandler.getAuraChunk(this.field_145850_b.field_73011_w.getDimension(), sx + xx, sz + zz);
                  if (ac != null) {
                     t = (int)((float)t + ac.getVis());
                  }
               }
            }
         }

         this.auraVisServer = t;
      }

   }

   public void spendAura(int vis) {
      if (!this.func_145831_w().field_72995_K) {
         if (this.field_145850_b.func_180495_p(this.func_174877_v().func_177984_a()).func_177230_c() != BlocksTC.arcaneWorkbenchCharger) {
            AuraHandler.drainVis(this.func_145831_w(), this.func_174877_v(), (float)vis, false);
         } else {
            int q = vis;
            int z = Math.max(1, vis / 9);

            label44:
            while(q > 0) {
               for(int xx = -1; xx <= 1; ++xx) {
                  for(int zz = -1; zz <= 1; ++zz) {
                     if (z > q) {
                        z = q;
                     }

                     q = (int)((float)q - AuraHandler.drainVis(this.func_145831_w(), this.func_174877_v().func_177982_a(xx * 16, 0, zz * 16), (float)z, false));
                     if (q <= 0) {
                        break label44;
                     }
                  }
               }
            }
         }

         this.auraVisServer -= vis;
      } else {
         this.auraVisClient -= vis;
      }

   }
}
