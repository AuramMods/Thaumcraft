package thaumcraft.common.tiles.devices;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.tiles.essentia.TileJar;

public class TileJarBrain extends TileJar {
   public float field_40063_b;
   public float field_40061_d;
   public float field_40059_f;
   public float field_40066_q;
   public float rota;
   public float rotb;
   public int xp = 0;
   public int xpMax = 2000;
   public int eatDelay = 0;
   long lastsigh = System.currentTimeMillis() + 1500L;

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      this.xp = nbttagcompound.func_74762_e("XP");
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.func_74768_a("XP", this.xp);
      return nbttagcompound;
   }

   public void func_73660_a() {
      Entity entity = null;
      if (this.xp > this.xpMax) {
         this.xp = this.xpMax;
      }

      double d;
      double d1;
      if (this.xp < this.xpMax) {
         entity = this.getClosestXPOrb();
         if (entity != null && this.eatDelay == 0) {
            d = ((double)this.field_174879_c.func_177958_n() + 0.5D - ((Entity)entity).field_70165_t) / 25.0D;
            d1 = ((double)this.field_174879_c.func_177956_o() + 0.5D - ((Entity)entity).field_70163_u) / 25.0D;
            double var7 = ((double)this.field_174879_c.func_177952_p() + 0.5D - ((Entity)entity).field_70161_v) / 25.0D;
            double var9 = Math.sqrt(d * d + d1 * d1 + var7 * var7);
            double var11 = 1.0D - var9;
            if (var11 > 0.0D) {
               var11 *= var11;
               ((Entity)entity).field_70159_w += d / var9 * var11 * 0.3D;
               ((Entity)entity).field_70181_x += d1 / var9 * var11 * 0.5D;
               ((Entity)entity).field_70179_y += var7 / var9 * var11 * 0.3D;
            }
         }
      }

      if (this.field_145850_b.field_72995_K) {
         this.rotb = this.rota;
         if (entity == null) {
            entity = this.field_145850_b.func_184137_a((double)this.field_174879_c.func_177958_n() + 0.5D, (double)this.field_174879_c.func_177956_o() + 0.5D, (double)this.field_174879_c.func_177952_p() + 0.5D, 6.0D, false);
            if (entity != null && this.lastsigh < System.currentTimeMillis()) {
               this.field_145850_b.func_184134_a((double)this.field_174879_c.func_177958_n() + 0.5D, (double)this.field_174879_c.func_177956_o() + 0.5D, (double)this.field_174879_c.func_177952_p() + 0.5D, SoundsTC.brain, SoundCategory.AMBIENT, 0.15F, 0.8F + this.field_145850_b.field_73012_v.nextFloat() * 0.4F, false);
               this.lastsigh = System.currentTimeMillis() + 5000L + (long)this.field_145850_b.field_73012_v.nextInt(25000);
            }
         }

         if (entity != null) {
            d = ((Entity)entity).field_70165_t - (double)((float)this.field_174879_c.func_177958_n() + 0.5F);
            d1 = ((Entity)entity).field_70161_v - (double)((float)this.field_174879_c.func_177952_p() + 0.5F);
            this.field_40066_q = (float)Math.atan2(d1, d);
            this.field_40059_f += 0.1F;
            if (this.field_40059_f < 0.5F || rand.nextInt(40) == 0) {
               float f3 = this.field_40061_d;

               do {
                  this.field_40061_d += (float)(rand.nextInt(4) - rand.nextInt(4));
               } while(f3 == this.field_40061_d);
            }
         } else {
            this.field_40066_q += 0.01F;
         }

         while(this.rota >= 3.141593F) {
            this.rota -= 6.283185F;
         }

         while(this.rota < -3.141593F) {
            this.rota += 6.283185F;
         }

         while(this.field_40066_q >= 3.141593F) {
            this.field_40066_q -= 6.283185F;
         }

         while(this.field_40066_q < -3.141593F) {
            this.field_40066_q += 6.283185F;
         }

         float f;
         for(f = this.field_40066_q - this.rota; f >= 3.141593F; f -= 6.283185F) {
         }

         while(f < -3.141593F) {
            f += 6.283185F;
         }

         this.rota += f * 0.04F;
      }

      if (this.eatDelay > 0) {
         --this.eatDelay;
      } else if (this.xp < this.xpMax) {
         List ents = this.field_145850_b.func_72872_a(EntityXPOrb.class, new AxisAlignedBB((double)this.field_174879_c.func_177958_n() - 0.1D, (double)this.field_174879_c.func_177956_o() - 0.1D, (double)this.field_174879_c.func_177952_p() - 0.1D, (double)this.field_174879_c.func_177958_n() + 1.1D, (double)this.field_174879_c.func_177956_o() + 1.1D, (double)this.field_174879_c.func_177952_p() + 1.1D));
         if (ents.size() > 0) {
            Iterator var3 = ents.iterator();

            while(var3.hasNext()) {
               Object ent = var3.next();
               EntityXPOrb eo = (EntityXPOrb)ent;
               this.xp += eo.func_70526_d();
               eo.func_184185_a(SoundEvents.field_187537_bA, 0.1F, (this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.2F + 1.0F);
               eo.func_70106_y();
            }

            this.syncTile(false);
            this.func_70296_d();
         }
      }

   }

   public Entity getClosestXPOrb() {
      double cdist = Double.MAX_VALUE;
      Entity orb = null;
      List ents = this.field_145850_b.func_72872_a(EntityXPOrb.class, (new AxisAlignedBB((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), (double)(this.field_174879_c.func_177958_n() + 1), (double)(this.field_174879_c.func_177956_o() + 1), (double)(this.field_174879_c.func_177952_p() + 1))).func_72314_b(8.0D, 8.0D, 8.0D));
      if (ents.size() > 0) {
         Iterator var5 = ents.iterator();

         while(var5.hasNext()) {
            Object ent = var5.next();
            EntityXPOrb eo = (EntityXPOrb)ent;
            double d = this.func_145835_a(eo.field_70165_t, eo.field_70163_u, eo.field_70161_v);
            if (d < cdist) {
               orb = eo;
               cdist = d;
            }
         }
      }

      return orb;
   }
}
