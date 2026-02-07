package thaumcraft.common.tiles.essentia;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileAlembic extends TileThaumcraft implements IAspectContainer, IEssentiaTransport {
   public Aspect aspect;
   public Aspect aspectFilter = null;
   public int amount = 0;
   public int maxAmount = 128;
   public int facing;
   public boolean aboveFurnace;
   EnumFacing fd;

   public TileAlembic() {
      this.facing = EnumFacing.DOWN.ordinal();
      this.aboveFurnace = false;
      this.fd = null;
   }

   public AspectList getAspects() {
      return this.aspect != null ? (new AspectList()).add(this.aspect, this.amount) : new AspectList();
   }

   public void setAspects(AspectList aspects) {
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return new AxisAlignedBB((double)this.func_174877_v().func_177958_n() - 0.1D, (double)this.func_174877_v().func_177956_o() - 0.1D, (double)this.func_174877_v().func_177952_p() - 0.1D, (double)this.func_174877_v().func_177958_n() + 1.1D, (double)this.func_174877_v().func_177956_o() + 1.1D, (double)this.func_174877_v().func_177952_p() + 1.1D);
   }

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      this.facing = nbttagcompound.func_74771_c("facing");
      this.aspectFilter = Aspect.getAspect(nbttagcompound.func_74779_i("AspectFilter"));
      String tag = nbttagcompound.func_74779_i("aspect");
      if (tag != null) {
         this.aspect = Aspect.getAspect(tag);
      }

      this.amount = nbttagcompound.func_74765_d("amount");
      this.fd = EnumFacing.field_82609_l[this.facing];
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      if (this.aspect != null) {
         nbttagcompound.func_74778_a("aspect", this.aspect.getTag());
      }

      if (this.aspectFilter != null) {
         nbttagcompound.func_74778_a("AspectFilter", this.aspectFilter.getTag());
      }

      nbttagcompound.func_74777_a("amount", (short)this.amount);
      nbttagcompound.func_74774_a("facing", (byte)this.facing);
      return nbttagcompound;
   }

   public int addToContainer(Aspect tt, int am) {
      if (this.aspectFilter != null && tt != this.aspectFilter) {
         return am;
      } else {
         if (this.amount < this.maxAmount && tt == this.aspect || this.amount == 0) {
            this.aspect = tt;
            int added = Math.min(am, this.maxAmount - this.amount);
            this.amount += added;
            am -= added;
         }

         this.func_70296_d();
         this.syncTile(false);
         return am;
      }
   }

   public boolean takeFromContainer(Aspect tt, int am) {
      if (this.amount == 0 || this.aspect == null) {
         this.aspect = null;
         this.amount = 0;
      }

      if (this.aspect != null && this.amount >= am && tt == this.aspect) {
         this.amount -= am;
         if (this.amount <= 0) {
            this.aspect = null;
            this.amount = 0;
         }

         this.func_70296_d();
         this.syncTile(false);
         return true;
      } else {
         return false;
      }
   }

   public boolean doesContainerContain(AspectList ot) {
      return this.amount > 0 && this.aspect != null && ot.getAmount(this.aspect) > 0;
   }

   public boolean doesContainerContainAmount(Aspect tt, int am) {
      return this.amount >= am && tt == this.aspect;
   }

   public int containerContains(Aspect tt) {
      return tt == this.aspect ? this.amount : 0;
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean isConnectable(EnumFacing face) {
      return face != EnumFacing.field_82609_l[this.facing] && face != EnumFacing.DOWN;
   }

   public boolean canInputFrom(EnumFacing face) {
      return false;
   }

   public boolean canOutputTo(EnumFacing face) {
      return face != EnumFacing.field_82609_l[this.facing] && face != EnumFacing.DOWN;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public Aspect getSuctionType(EnumFacing loc) {
      return null;
   }

   public int getSuctionAmount(EnumFacing loc) {
      return 0;
   }

   public Aspect getEssentiaType(EnumFacing loc) {
      return this.aspect;
   }

   public int getEssentiaAmount(EnumFacing loc) {
      return this.amount;
   }

   public int takeEssentia(Aspect aspect, int amount, EnumFacing face) {
      return this.canOutputTo(face) && this.takeFromContainer(aspect, amount) ? amount : 0;
   }

   public int addEssentia(Aspect aspect, int amount, EnumFacing loc) {
      return 0;
   }

   public int getMinimumSuction() {
      return 0;
   }

   protected static boolean processAlembics(World world, BlockPos pos, Aspect aspect) {
      int deep = 1;

      while(true) {
         TileEntity te = world.func_175625_s(pos.func_177981_b(deep));
         TileAlembic alembic;
         if (te == null || !(te instanceof TileAlembic)) {
            deep = 1;

            while(true) {
               te = world.func_175625_s(pos.func_177981_b(deep));
               if (te == null || !(te instanceof TileAlembic)) {
                  return false;
               }

               alembic = (TileAlembic)te;
               if ((alembic.aspectFilter == null || alembic.aspectFilter == aspect) && alembic.addToContainer(aspect, 1) == 0) {
                  return true;
               }

               ++deep;
            }
         }

         alembic = (TileAlembic)te;
         if (alembic.amount > 0 && alembic.aspect == aspect && alembic.addToContainer(aspect, 1) == 0) {
            return true;
         }

         ++deep;
      }
   }
}
