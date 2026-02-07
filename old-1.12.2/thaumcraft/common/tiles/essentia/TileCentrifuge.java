package thaumcraft.common.tiles.essentia;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileCentrifuge extends TileThaumcraft implements IAspectContainer, IEssentiaTransport, ITickable {
   public Aspect aspectOut = null;
   public Aspect aspectIn = null;
   int count = 0;
   int process = 0;
   float rotationSpeed = 0.0F;
   public float rotation = 0.0F;

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      this.aspectIn = Aspect.getAspect(nbttagcompound.func_74779_i("aspectIn"));
      this.aspectOut = Aspect.getAspect(nbttagcompound.func_74779_i("aspectOut"));
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      if (this.aspectIn != null) {
         nbttagcompound.func_74778_a("aspectIn", this.aspectIn.getTag());
      }

      if (this.aspectOut != null) {
         nbttagcompound.func_74778_a("aspectOut", this.aspectOut.getTag());
      }

      return nbttagcompound;
   }

   public AspectList getAspects() {
      AspectList al = new AspectList();
      if (this.aspectOut != null) {
         al.add(this.aspectOut, 1);
      }

      return al;
   }

   public int addToContainer(Aspect tt, int am) {
      if (am > 0 && this.aspectOut == null) {
         this.aspectOut = tt;
         this.func_70296_d();
         this.field_145850_b.markAndNotifyBlock(this.func_174877_v(), this.field_145850_b.func_175726_f(this.func_174877_v()), this.field_145850_b.func_180495_p(this.func_174877_v()), this.field_145850_b.func_180495_p(this.func_174877_v()), 3);
         --am;
      }

      return am;
   }

   public boolean takeFromContainer(Aspect tt, int am) {
      if (this.aspectOut != null && tt == this.aspectOut) {
         this.aspectOut = null;
         this.func_70296_d();
         this.field_145850_b.markAndNotifyBlock(this.func_174877_v(), this.field_145850_b.func_175726_f(this.func_174877_v()), this.field_145850_b.func_180495_p(this.func_174877_v()), this.field_145850_b.func_180495_p(this.func_174877_v()), 3);
         return true;
      } else {
         return false;
      }
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amt) {
      return amt == 1 && tag == this.aspectOut;
   }

   public boolean doesContainerContain(AspectList ot) {
      Aspect[] var2 = ot.getAspects();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Aspect tt = var2[var4];
         if (tt == this.aspectOut) {
            return true;
         }
      }

      return false;
   }

   public int containerContains(Aspect tag) {
      return tag == this.aspectOut ? 1 : 0;
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }

   public boolean isConnectable(EnumFacing face) {
      return face == EnumFacing.UP || face == EnumFacing.DOWN;
   }

   public boolean canInputFrom(EnumFacing face) {
      return face == EnumFacing.DOWN;
   }

   public boolean canOutputTo(EnumFacing face) {
      return face == EnumFacing.UP;
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public int getMinimumSuction() {
      return 0;
   }

   public Aspect getSuctionType(EnumFacing face) {
      return null;
   }

   public int getSuctionAmount(EnumFacing face) {
      return face == EnumFacing.DOWN ? (this.gettingPower() ? 0 : (this.aspectIn == null ? 128 : 64)) : 0;
   }

   public Aspect getEssentiaType(EnumFacing loc) {
      return this.aspectOut;
   }

   public int getEssentiaAmount(EnumFacing loc) {
      return this.aspectOut != null ? 1 : 0;
   }

   public int takeEssentia(Aspect aspect, int amount, EnumFacing face) {
      return this.canOutputTo(face) && this.takeFromContainer(aspect, amount) ? amount : 0;
   }

   public int addEssentia(Aspect aspect, int amount, EnumFacing face) {
      if (this.aspectIn == null && !aspect.isPrimal()) {
         this.aspectIn = aspect;
         this.process = 39;
         this.func_70296_d();
         this.field_145850_b.markAndNotifyBlock(this.func_174877_v(), this.field_145850_b.func_175726_f(this.func_174877_v()), this.field_145850_b.func_180495_p(this.func_174877_v()), this.field_145850_b.func_180495_p(this.func_174877_v()), 3);
         return 1;
      } else {
         return 0;
      }
   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K) {
         if (!this.gettingPower()) {
            if (this.aspectOut == null && this.aspectIn == null && ++this.count % 5 == 0) {
               this.drawEssentia();
            }

            if (this.process > 0) {
               --this.process;
            }

            if (this.aspectOut == null && this.aspectIn != null && this.process == 0) {
               this.processEssentia();
            }
         }
      } else {
         if (this.aspectIn != null && !this.gettingPower() && this.rotationSpeed < 20.0F) {
            this.rotationSpeed += 2.0F;
         }

         if ((this.aspectIn == null || this.gettingPower()) && this.rotationSpeed > 0.0F) {
            this.rotationSpeed -= 0.5F;
         }

         int pr = (int)this.rotation;
         this.rotation += this.rotationSpeed;
         if (this.rotation % 180.0F <= 20.0F && pr % 180 >= 160 && this.rotationSpeed > 0.0F) {
            this.field_145850_b.func_184134_a((double)this.func_174877_v().func_177958_n() + 0.5D, (double)this.func_174877_v().func_177956_o() + 0.5D, (double)this.func_174877_v().func_177952_p() + 0.5D, SoundsTC.pump, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
         }
      }

   }

   void processEssentia() {
      Aspect[] comps = this.aspectIn.getComponents();
      this.aspectOut = comps[this.field_145850_b.field_73012_v.nextInt(2)];
      this.aspectIn = null;
      this.func_70296_d();
      this.field_145850_b.markAndNotifyBlock(this.func_174877_v(), this.field_145850_b.func_175726_f(this.func_174877_v()), this.field_145850_b.func_180495_p(this.func_174877_v()), this.field_145850_b.func_180495_p(this.func_174877_v()), 3);
   }

   void drawEssentia() {
      TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.field_145850_b, this.func_174877_v(), EnumFacing.DOWN);
      if (te != null) {
         IEssentiaTransport ic = (IEssentiaTransport)te;
         if (!ic.canOutputTo(EnumFacing.UP)) {
            return;
         }

         Aspect ta = null;
         if (ic.getEssentiaAmount(EnumFacing.UP) > 0 && ic.getSuctionAmount(EnumFacing.UP) < this.getSuctionAmount(EnumFacing.DOWN) && this.getSuctionAmount(EnumFacing.DOWN) >= ic.getMinimumSuction()) {
            ta = ic.getEssentiaType(EnumFacing.UP);
         }

         if (ta != null && !ta.isPrimal() && ic.getSuctionAmount(EnumFacing.UP) < this.getSuctionAmount(EnumFacing.DOWN) && ic.takeEssentia(ta, 1, EnumFacing.UP) == 1) {
            this.aspectIn = ta;
            this.process = 39;
            this.func_70296_d();
            this.field_145850_b.markAndNotifyBlock(this.func_174877_v(), this.field_145850_b.func_175726_f(this.func_174877_v()), this.field_145850_b.func_180495_p(this.func_174877_v()), this.field_145850_b.func_180495_p(this.func_174877_v()), 3);
         }
      }

   }

   public void setAspects(AspectList aspects) {
   }
}
