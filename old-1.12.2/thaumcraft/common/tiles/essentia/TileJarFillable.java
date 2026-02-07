package thaumcraft.common.tiles.essentia;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.aspects.IEssentiaTransport;

public class TileJarFillable extends TileJar implements IAspectSource, IEssentiaTransport {
   public Aspect aspect = null;
   public Aspect aspectFilter = null;
   public int amount = 0;
   public int maxAmount = 256;
   public int facing = 2;
   public boolean blocked = false;
   int count = 0;

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      this.aspect = Aspect.getAspect(nbttagcompound.func_74779_i("Aspect"));
      this.aspectFilter = Aspect.getAspect(nbttagcompound.func_74779_i("AspectFilter"));
      this.amount = nbttagcompound.func_74765_d("Amount");
      this.facing = nbttagcompound.func_74771_c("facing");
      this.blocked = nbttagcompound.func_74767_n("blocked");
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      if (this.aspect != null) {
         nbttagcompound.func_74778_a("Aspect", this.aspect.getTag());
      }

      if (this.aspectFilter != null) {
         nbttagcompound.func_74778_a("AspectFilter", this.aspectFilter.getTag());
      }

      nbttagcompound.func_74777_a("Amount", (short)this.amount);
      nbttagcompound.func_74774_a("facing", (byte)this.facing);
      nbttagcompound.func_74757_a("blocked", this.blocked);
      return nbttagcompound;
   }

   public AspectList getAspects() {
      AspectList al = new AspectList();
      if (this.aspect != null && this.amount > 0) {
         al.add(this.aspect, this.amount);
      }

      return al;
   }

   public void setAspects(AspectList aspects) {
      if (aspects != null && aspects.size() > 0) {
         this.aspect = aspects.getAspectsSortedByAmount()[0];
         this.amount = aspects.getAmount(aspects.getAspectsSortedByAmount()[0]);
      }

   }

   public int addToContainer(Aspect tt, int am) {
      if (am == 0) {
         return am;
      } else {
         if (this.amount < this.maxAmount && tt == this.aspect || this.amount == 0) {
            this.aspect = tt;
            int added = Math.min(am, this.maxAmount - this.amount);
            this.amount += added;
            am -= added;
         }

         this.syncTile(false);
         this.func_70296_d();
         return am;
      }
   }

   public boolean takeFromContainer(Aspect tt, int am) {
      if (this.amount >= am && tt == this.aspect) {
         this.amount -= am;
         if (this.amount <= 0) {
            this.aspect = null;
            this.amount = 0;
         }

         this.syncTile(false);
         this.func_70296_d();
         return true;
      } else {
         return false;
      }
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amt) {
      return this.amount >= amt && tag == this.aspect;
   }

   public boolean doesContainerContain(AspectList ot) {
      Aspect[] var2 = ot.getAspects();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Aspect tt = var2[var4];
         if (this.amount > 0 && tt == this.aspect) {
            return true;
         }
      }

      return false;
   }

   public int containerContains(Aspect tag) {
      return tag == this.aspect ? this.amount : 0;
   }

   public boolean doesContainerAccept(Aspect tag) {
      return this.aspectFilter != null ? tag.equals(this.aspectFilter) : true;
   }

   public boolean isConnectable(EnumFacing face) {
      return face == EnumFacing.UP;
   }

   public boolean canInputFrom(EnumFacing face) {
      return face == EnumFacing.UP;
   }

   public boolean canOutputTo(EnumFacing face) {
      return face == EnumFacing.UP;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public int getMinimumSuction() {
      return this.aspectFilter != null ? 64 : 32;
   }

   public Aspect getSuctionType(EnumFacing loc) {
      return this.aspectFilter != null ? this.aspectFilter : this.aspect;
   }

   public int getSuctionAmount(EnumFacing loc) {
      if (this.amount < this.maxAmount) {
         return this.aspectFilter != null ? 64 : 32;
      } else {
         return 0;
      }
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

   public int addEssentia(Aspect aspect, int amount, EnumFacing face) {
      return this.canInputFrom(face) ? amount - this.addToContainer(aspect, amount) : 0;
   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K && ++this.count % 5 == 0 && this.amount < this.maxAmount) {
         this.fillJar();
      }

   }

   void fillJar() {
      TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.field_145850_b, this.field_174879_c, EnumFacing.UP);
      if (te != null) {
         IEssentiaTransport ic = (IEssentiaTransport)te;
         if (!ic.canOutputTo(EnumFacing.DOWN)) {
            return;
         }

         Aspect ta = null;
         if (this.aspectFilter != null) {
            ta = this.aspectFilter;
         } else if (this.aspect != null && this.amount > 0) {
            ta = this.aspect;
         } else if (ic.getEssentiaAmount(EnumFacing.DOWN) > 0 && ic.getSuctionAmount(EnumFacing.DOWN) < this.getSuctionAmount(EnumFacing.UP) && this.getSuctionAmount(EnumFacing.UP) >= ic.getMinimumSuction()) {
            ta = ic.getEssentiaType(EnumFacing.DOWN);
         }

         if (ta != null && ic.getSuctionAmount(EnumFacing.DOWN) < this.getSuctionAmount(EnumFacing.UP)) {
            this.addToContainer(ta, ic.takeEssentia(ta, 1, EnumFacing.DOWN));
         }
      }

   }

   public boolean isBlocked() {
      return this.blocked;
   }
}
