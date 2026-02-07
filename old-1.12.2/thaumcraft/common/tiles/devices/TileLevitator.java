package thaumcraft.common.tiles.devices;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.codechicken.lib.raytracer.IndexedCuboid6;
import thaumcraft.codechicken.lib.vec.Cuboid6;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileLevitator extends TileThaumcraft implements ITickable {
   private int[] ranges = new int[]{4, 8, 16, 32};
   private int range = 1;
   private int rangeActual = 0;
   private int counter = 0;
   private int vis = 0;

   public void func_73660_a() {
      EnumFacing facing = BlockStateUtils.getFacing(this.func_145832_p());
      if (this.rangeActual > this.ranges[this.range]) {
         this.rangeActual = 0;
      }

      int p = this.counter % this.ranges[this.range];
      if (this.field_145850_b.func_180495_p(this.field_174879_c.func_177967_a(facing, 1 + p)).func_185914_p()) {
         if (1 + p < this.rangeActual) {
            this.rangeActual = 1 + p;
         }

         this.counter = -1;
      } else if (1 + p > this.rangeActual) {
         this.rangeActual = 1 + p;
      }

      ++this.counter;
      if (!this.field_145850_b.field_72995_K && this.vis < 10) {
         this.vis = (int)((float)this.vis + AuraHelper.drainVis(this.field_145850_b, this.func_174877_v(), 1.0F, false) * 1200.0F);
         this.func_70296_d();
         this.syncTile(false);
      }

      if (this.rangeActual > 0 && this.vis > 0 && BlockStateUtils.isEnabled(this.func_145832_p())) {
         List<Entity> targets = this.field_145850_b.func_72872_a(Entity.class, new AxisAlignedBB((double)(this.field_174879_c.func_177958_n() - (facing.func_82601_c() < 0 ? this.rangeActual : 0)), (double)(this.field_174879_c.func_177956_o() - (facing.func_96559_d() < 0 ? this.rangeActual : 0)), (double)(this.field_174879_c.func_177952_p() - (facing.func_82599_e() < 0 ? this.rangeActual : 0)), (double)(this.field_174879_c.func_177958_n() + 1 + (facing.func_82601_c() > 0 ? this.rangeActual : 0)), (double)(this.field_174879_c.func_177956_o() + 1 + (facing.func_96559_d() > 0 ? this.rangeActual : 0)), (double)(this.field_174879_c.func_177952_p() + 1 + (facing.func_82599_e() > 0 ? this.rangeActual : 0))));
         boolean lifted = false;
         if (targets.size() > 0) {
            Iterator var5 = targets.iterator();

            label129:
            do {
               Entity e;
               do {
                  if (!var5.hasNext()) {
                     break label129;
                  }

                  e = (Entity)var5.next();
               } while(!(e instanceof EntityItem) && !e.func_70104_M() && !(e instanceof EntityHorse));

               lifted = true;
               this.drawFXAt(e);
               this.drawFX(facing, 0.6D);
               if (e.func_70093_af() && facing == EnumFacing.UP) {
                  if (e.field_70181_x < 0.0D) {
                     e.field_70181_x *= 0.8999999761581421D;
                  }
               } else {
                  e.field_70159_w += (double)(0.1F * (float)facing.func_82601_c());
                  e.field_70181_x += (double)(0.1F * (float)facing.func_96559_d());
                  e.field_70179_y += (double)(0.1F * (float)facing.func_82599_e());
                  if (facing.func_176740_k() != Axis.Y && !e.field_70122_E) {
                     if (e.field_70181_x < 0.0D) {
                        e.field_70181_x *= 0.8999999761581421D;
                     }

                     e.field_70181_x += 0.07999999821186066D;
                  }

                  if (e.field_70159_w > 0.3499999940395355D) {
                     e.field_70159_w = 0.3499999940395355D;
                  }

                  if (e.field_70181_x > 0.3499999940395355D) {
                     e.field_70181_x = 0.3499999940395355D;
                  }

                  if (e.field_70179_y > 0.3499999940395355D) {
                     e.field_70179_y = 0.3499999940395355D;
                  }

                  if (e.field_70159_w < -0.3499999940395355D) {
                     e.field_70159_w = -0.3499999940395355D;
                  }

                  if (e.field_70181_x < -0.3499999940395355D) {
                     e.field_70181_x = -0.3499999940395355D;
                  }

                  if (e.field_70179_y < -0.3499999940395355D) {
                     e.field_70179_y = -0.3499999940395355D;
                  }
               }

               e.field_70143_R = 0.0F;
               this.vis -= this.getCost();
            } while(this.vis > 0);
         }

         this.drawFX(facing, 0.1D);
         if (lifted && !this.field_145850_b.field_72995_K && this.counter % 20 == 0) {
            this.func_70296_d();
         }
      }

   }

   private void drawFX(EnumFacing facing, double c) {
      if (this.field_145850_b.field_72995_K && (double)this.field_145850_b.field_73012_v.nextFloat() < c) {
         float x = (float)this.field_174879_c.func_177958_n() + 0.25F + this.field_145850_b.field_73012_v.nextFloat() * 0.5F;
         float y = (float)this.field_174879_c.func_177956_o() + 0.25F + this.field_145850_b.field_73012_v.nextFloat() * 0.5F;
         float z = (float)this.field_174879_c.func_177952_p() + 0.25F + this.field_145850_b.field_73012_v.nextFloat() * 0.5F;
         FXDispatcher.INSTANCE.drawLevitatorParticles((double)x, (double)y, (double)z, (double)facing.func_82601_c() / 50.0D, (double)facing.func_96559_d() / 50.0D, (double)facing.func_82599_e() / 50.0D);
      }

   }

   private void drawFXAt(Entity e) {
      if (this.field_145850_b.field_72995_K && this.field_145850_b.field_73012_v.nextFloat() < 0.1F) {
         float x = (float)(e.field_70165_t + (double)((this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * e.field_70130_N));
         float y = (float)(e.field_70163_u + (double)(this.field_145850_b.field_73012_v.nextFloat() * e.field_70131_O));
         float z = (float)(e.field_70161_v + (double)((this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * e.field_70130_N));
         FXDispatcher.INSTANCE.drawLevitatorParticles((double)x, (double)y, (double)z, (double)(this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.01D, (double)(this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.01D, (double)(this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.01D);
      }

   }

   public void readSyncNBT(NBTTagCompound nbt) {
      this.range = nbt.func_74771_c("range");
      this.vis = nbt.func_74762_e("vis");
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
      nbt.func_74774_a("range", (byte)this.range);
      nbt.func_74768_a("vis", this.vis);
      return nbt;
   }

   public int getCost() {
      return this.ranges[this.range] * 2;
   }

   public void increaseRange(EntityPlayer playerIn) {
      this.rangeActual = 0;
      if (!this.field_145850_b.field_72995_K) {
         ++this.range;
         if (this.range >= this.ranges.length) {
            this.range = 0;
         }

         this.func_70296_d();
         this.syncTile(false);
         playerIn.func_145747_a(new TextComponentString(String.format(I18n.func_74838_a("tc.levitator"), this.ranges[this.range], this.getCost())));
      }

   }

   public RayTraceResult rayTrace(World world, Vec3d vec3d, Vec3d vec3d1, RayTraceResult fullblock) {
      return fullblock;
   }

   public void addTraceableCuboids(List<IndexedCuboid6> cuboids) {
      EnumFacing facing = BlockStateUtils.getFacing(this.func_145832_p());
      cuboids.add(new IndexedCuboid6(0, this.getCuboidByFacing(facing)));
   }

   public Cuboid6 getCuboidByFacing(EnumFacing facing) {
      switch(facing) {
      case DOWN:
         return new Cuboid6((double)this.func_174877_v().func_177958_n() + 0.375D, (double)this.func_174877_v().func_177956_o() + 0.875D, (double)this.func_174877_v().func_177952_p() + 0.375D, (double)this.func_174877_v().func_177958_n() + 0.625D, (double)this.func_174877_v().func_177956_o() + 0.9375D, (double)this.func_174877_v().func_177952_p() + 0.625D);
      case EAST:
         return new Cuboid6((double)this.func_174877_v().func_177958_n() + 0.0625D, (double)this.func_174877_v().func_177956_o() + 0.375D, (double)this.func_174877_v().func_177952_p() + 0.375D, (double)this.func_174877_v().func_177958_n() + 0.125D, (double)this.func_174877_v().func_177956_o() + 0.625D, (double)this.func_174877_v().func_177952_p() + 0.625D);
      case WEST:
         return new Cuboid6((double)this.func_174877_v().func_177958_n() + 0.875D, (double)this.func_174877_v().func_177956_o() + 0.375D, (double)this.func_174877_v().func_177952_p() + 0.375D, (double)this.func_174877_v().func_177958_n() + 0.9375D, (double)this.func_174877_v().func_177956_o() + 0.625D, (double)this.func_174877_v().func_177952_p() + 0.625D);
      case SOUTH:
         return new Cuboid6((double)this.func_174877_v().func_177958_n() + 0.375D, (double)this.func_174877_v().func_177956_o() + 0.375D, (double)this.func_174877_v().func_177952_p() + 0.0625D, (double)this.func_174877_v().func_177958_n() + 0.625D, (double)this.func_174877_v().func_177956_o() + 0.625D, (double)this.func_174877_v().func_177952_p() + 0.125D);
      case NORTH:
         return new Cuboid6((double)this.func_174877_v().func_177958_n() + 0.375D, (double)this.func_174877_v().func_177956_o() + 0.375D, (double)this.func_174877_v().func_177952_p() + 0.875D, (double)this.func_174877_v().func_177958_n() + 0.625D, (double)this.func_174877_v().func_177956_o() + 0.625D, (double)this.func_174877_v().func_177952_p() + 0.9375D);
      default:
         return new Cuboid6((double)this.func_174877_v().func_177958_n() + 0.375D, (double)this.func_174877_v().func_177956_o() + 0.0625D, (double)this.func_174877_v().func_177952_p() + 0.375D, (double)this.func_174877_v().func_177958_n() + 0.625D, (double)this.func_174877_v().func_177956_o() + 0.125D, (double)this.func_174877_v().func_177952_p() + 0.625D);
      }
   }
}
