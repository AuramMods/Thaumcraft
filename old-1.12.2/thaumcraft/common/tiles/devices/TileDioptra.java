package thaumcraft.common.tiles.devices;

import java.util.Arrays;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.world.aura.AuraChunk;
import thaumcraft.common.world.aura.AuraHandler;

public class TileDioptra extends TileThaumcraft implements ITickable {
   public int counter = 0;
   public byte[] grid_amt = new byte[169];
   public byte[] grid_flux = new byte[169];
   private byte[] grid_amt_p = new byte[169];

   public TileDioptra() {
      Arrays.fill(this.grid_amt, (byte)0);
      Arrays.fill(this.grid_flux, (byte)-128);
      Arrays.fill(this.grid_amt_p, (byte)0);
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return new AxisAlignedBB((double)this.func_174877_v().func_177958_n() - 0.3D, (double)this.func_174877_v().func_177956_o() - 0.3D, (double)this.func_174877_v().func_177952_p() - 0.3D, (double)this.func_174877_v().func_177958_n() + 1.3D, (double)this.func_174877_v().func_177956_o() + 2.3D, (double)this.func_174877_v().func_177952_p() + 1.3D);
   }

   public void func_73660_a() {
      ++this.counter;
      if (BlockStateUtils.isEnabled(this.func_145832_p())) {
         if (!this.field_145850_b.field_72995_K) {
            if (this.counter % 20 == 0) {
               Arrays.fill(this.grid_amt, (byte)0);
               Arrays.fill(this.grid_flux, (byte)-128);

               for(int xx = 0; xx < 13; ++xx) {
                  for(int zz = 0; zz < 13; ++zz) {
                     AuraChunk ac = AuraHandler.getAuraChunk(this.field_145850_b.field_73011_w.getDimension(), (this.field_174879_c.func_177958_n() >> 4) + xx - 6, (this.field_174879_c.func_177952_p() >> 4) + zz - 6);
                     if (ac != null) {
                        this.grid_amt[xx + zz * 13] = (byte)((int)Math.min(64.0F, ac.getVis() / 500.0F * 64.0F));
                        this.grid_flux[xx + zz * 13] = (byte)((int)Math.min(64.0F, ac.getFlux() / 500.0F * 64.0F));
                     }
                  }
               }

               this.func_70296_d();
               this.syncTile(false);
            }
         } else {
            this.drawFX();
         }
      } else {
         this.counter = 0;
      }

   }

   private void drawFX() {
   }

   public void readSyncNBT(NBTTagCompound nbt) {
      if (nbt.func_74764_b("grid_a")) {
         this.grid_amt = nbt.func_74770_j("grid_a");
      }

      if (nbt.func_74764_b("grid_b")) {
         this.grid_flux = nbt.func_74770_j("grid_b");
      }

      if (this.grid_flux == null || this.grid_flux.length < this.grid_amt.length) {
         this.grid_flux = new byte[169];
         Arrays.fill(this.grid_flux, (byte)-128);
      }

   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
      nbt.func_74773_a("grid_a", this.grid_amt);
      nbt.func_74773_a("grid_b", this.grid_flux);
      return nbt;
   }
}
