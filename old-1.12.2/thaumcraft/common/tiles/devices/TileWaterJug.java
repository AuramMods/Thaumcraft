package thaumcraft.common.tiles.devices;

import java.util.ArrayList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.tiles.TileThaumcraft;
import vazkii.botania.api.item.IPetalApothecary;

public class TileWaterJug extends TileThaumcraft implements ITickable {
   public int charge = 0;
   int zone = 0;
   int counter = 0;
   ArrayList<Integer> handlers = new ArrayList();
   int zc = 0;
   int tcount = 0;

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      this.charge = nbt.func_74762_e("charge");
      NBTTagList nbttaglist = nbt.func_150295_c("handlers", 3);
      this.handlers = new ArrayList();

      for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
         NBTTagInt tag = (NBTTagInt)nbttaglist.func_179238_g(i);
         this.handlers.add(tag.func_150287_d());
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      super.func_189515_b(nbt);
      nbt.func_74768_a("charge", this.charge);
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.handlers.size(); ++i) {
         new NBTTagInt((Integer)this.handlers.get(i));
      }

      nbt.func_74782_a("handlers", nbttaglist);
      return nbt;
   }

   public void func_73660_a() {
      ++this.counter;
      int x;
      int y;
      int z;
      if (this.field_145850_b.field_72995_K) {
         if (this.tcount > 0 || this.field_145850_b.field_73012_v.nextInt(15) == 0) {
            FXDispatcher.INSTANCE.jarSplashFx((double)this.func_174877_v().func_177958_n() + 0.5D, (double)(this.func_174877_v().func_177956_o() + 1), (double)this.func_174877_v().func_177952_p() + 0.5D);
         }

         if (this.tcount > 0) {
            if (this.tcount % 5 == 0) {
               x = this.zc / 5 % 5;
               y = this.zc / 5 / 5 % 3;
               z = this.zc % 5;
               FXDispatcher.INSTANCE.waterTrailFx(this.func_174877_v(), this.func_174877_v().func_177982_a(x - 2, y - 1, z - 2), this.counter, 2650102, 0.1F);
            }

            --this.tcount;
         }
      } else if (this.counter % 5 == 0) {
         ++this.zone;
         x = this.zone / 5 % 5;
         y = this.zone / 5 / 5 % 3;
         z = this.zone % 5;
         TileEntity te = this.field_145850_b.func_175625_s(this.func_174877_v().func_177982_a(x - 2, y - 1, z - 2));
         if (te != null && (te instanceof IFluidHandler || te instanceof IPetalApothecary) && !this.handlers.contains(this.zone)) {
            this.handlers.add(this.zone);
            this.func_70296_d();
         }

         int i = 0;

         label66:
         while(true) {
            while(true) {
               if (i >= this.handlers.size() || this.charge <= 0) {
                  break label66;
               }

               int zz = (Integer)this.handlers.get(i);
               x = zz / 5 % 5;
               y = zz / 5 / 5 % 3;
               z = zz % 5;
               TileEntity tile = this.field_145850_b.func_175625_s(this.func_174877_v().func_177982_a(x - 2, y - 1, z - 2));
               if (tile != null && tile instanceof IFluidHandler) {
                  IFluidHandler fh = (IFluidHandler)tile;
                  if (fh.canFill(EnumFacing.UP, FluidRegistry.WATER)) {
                     int q = fh.fill(EnumFacing.UP, new FluidStack(FluidRegistry.WATER, 25), true);
                     this.charge -= q;
                     this.func_70296_d();
                     if (q > 0) {
                        this.field_145850_b.func_175641_c(this.func_174877_v(), this.func_145838_q(), 1, zz);
                        break label66;
                     }
                  }
                  break;
               }

               if (tile != null && tile instanceof IPetalApothecary) {
                  IPetalApothecary pa = (IPetalApothecary)tile;
                  if (!pa.hasWater()) {
                     pa.setWater(true);
                     this.field_145850_b.func_175641_c(this.func_174877_v(), this.func_145838_q(), 2, zz);
                     this.charge -= 250;
                  }
                  break;
               }

               this.handlers.remove(i);
               this.func_70296_d();
            }

            ++i;
         }

         if (this.charge <= 0 && AuraHelper.drainVis(this.func_145831_w(), this.func_174877_v(), 1.0F, false) > 0.0F) {
            this.charge += 1000;
            this.func_70296_d();
         }
      }

   }

   public boolean func_145842_c(int i, int j) {
      if (i == 1) {
         if (this.field_145850_b.field_72995_K) {
            this.zc = j;
            this.tcount = 5;
         }

         return true;
      } else {
         return super.func_145842_c(i, j);
      }
   }
}
