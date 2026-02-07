package thaumcraft.common.tiles.essentia;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.codechicken.lib.raytracer.RayTracer;
import thaumcraft.common.lib.SoundsTC;

public class TileTubeValve extends TileTube {
   public boolean allowFlow = true;
   boolean wasPoweredLastTick = false;
   public float rotation = 0.0F;

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K && this.count % 5 == 0) {
         boolean gettingPower = this.gettingPower();
         if (this.wasPoweredLastTick && !gettingPower && !this.allowFlow) {
            this.allowFlow = true;
            this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundsTC.squeek, SoundCategory.BLOCKS, 0.7F, 0.9F + this.field_145850_b.field_73012_v.nextFloat() * 0.2F);
            this.syncTile(true);
            this.func_70296_d();
         }

         if (!this.wasPoweredLastTick && gettingPower && this.allowFlow) {
            this.allowFlow = false;
            this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundsTC.squeek, SoundCategory.BLOCKS, 0.7F, 0.9F + this.field_145850_b.field_73012_v.nextFloat() * 0.2F);
            this.syncTile(true);
            this.func_70296_d();
         }

         this.wasPoweredLastTick = gettingPower;
      }

      if (this.field_145850_b.field_72995_K) {
         if (!this.allowFlow && this.rotation < 360.0F) {
            this.rotation += 20.0F;
         } else if (this.allowFlow && this.rotation > 0.0F) {
            this.rotation -= 20.0F;
         }
      }

      super.func_73660_a();
   }

   public boolean onCasterRightClick(World world, ItemStack wandstack, EntityPlayer player, BlockPos bp, EnumFacing side, EnumHand hand) {
      RayTraceResult hit = RayTracer.retraceBlock(world, player, this.field_174879_c);
      if (hit == null) {
         return false;
      } else {
         if (hit.subHit >= 0 && hit.subHit < 6) {
            player.field_70170_p.func_184134_a((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), SoundsTC.tool, SoundCategory.BLOCKS, 0.5F, 0.9F + player.field_70170_p.field_73012_v.nextFloat() * 0.2F, false);
            player.func_184609_a(hand);
            this.func_70296_d();
            this.syncTile(true);
            this.openSides[hit.subHit] = !this.openSides[hit.subHit];
            EnumFacing dir = EnumFacing.field_82609_l[hit.subHit];
            TileEntity tile = world.func_175625_s(this.field_174879_c.func_177972_a(dir));
            if (tile != null && tile instanceof TileTube) {
               ((TileTube)tile).openSides[dir.func_176734_d().ordinal()] = this.openSides[hit.subHit];
               this.syncTile(true);
               tile.func_70296_d();
            }
         }

         if (hit.subHit == 6) {
            player.field_70170_p.func_184134_a((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), SoundsTC.tool, SoundCategory.BLOCKS, 0.5F, 0.9F + player.field_70170_p.field_73012_v.nextFloat() * 0.2F, false);
            player.func_184609_a(hand);
            int a = this.facing.ordinal();
            this.func_70296_d();

            while(true) {
               ++a;
               if (a >= 20) {
                  break;
               }

               if (!this.canConnectSide(EnumFacing.field_82609_l[a % 6])) {
                  a %= 6;
                  this.facing = EnumFacing.field_82609_l[a];
                  this.syncTile(true);
                  this.func_70296_d();
                  break;
               }
            }
         }

         return !world.field_72995_K;
      }
   }

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      super.readSyncNBT(nbttagcompound);
      this.allowFlow = nbttagcompound.func_74767_n("flow");
      this.wasPoweredLastTick = nbttagcompound.func_74767_n("hadpower");
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound = super.writeSyncNBT(nbttagcompound);
      nbttagcompound.func_74757_a("flow", this.allowFlow);
      nbttagcompound.func_74757_a("hadpower", this.wasPoweredLastTick);
      return nbttagcompound;
   }

   public boolean isConnectable(EnumFacing face) {
      return face != this.facing && super.isConnectable(face);
   }

   public void setSuction(Aspect aspect, int amount) {
      if (this.allowFlow) {
         super.setSuction(aspect, amount);
      }

   }

   public boolean gettingPower() {
      return this.field_145850_b.func_175687_A(this.field_174879_c) > 0;
   }
}
