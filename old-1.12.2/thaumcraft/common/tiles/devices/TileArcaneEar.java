package thaumcraft.common.tiles.devices;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.WeakHashMap;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.common.blocks.IBlockEnabled;
import thaumcraft.common.lib.utils.BlockStateUtils;

public class TileArcaneEar extends TileEntity implements ITickable {
   public byte note = 0;
   public byte tone = 0;
   public int redstoneSignal = 0;
   public static WeakHashMap<Integer, ArrayList<Integer[]>> noteBlockEvents = new WeakHashMap();

   public NBTTagCompound func_189515_b(NBTTagCompound par1NBTTagCompound) {
      super.func_189515_b(par1NBTTagCompound);
      par1NBTTagCompound.func_74774_a("note", this.note);
      par1NBTTagCompound.func_74774_a("tone", this.tone);
      return par1NBTTagCompound;
   }

   public void func_145839_a(NBTTagCompound par1NBTTagCompound) {
      super.func_145839_a(par1NBTTagCompound);
      this.note = par1NBTTagCompound.func_74771_c("note");
      this.tone = par1NBTTagCompound.func_74771_c("tone");
      if (this.note < 0) {
         this.note = 0;
      }

      if (this.note > 24) {
         this.note = 24;
      }

   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K) {
         if (this.redstoneSignal > 0) {
            --this.redstoneSignal;
            if (this.redstoneSignal == 0) {
               EnumFacing facing = BlockStateUtils.getFacing(this.func_145832_p()).func_176734_d();
               TileEntity tileentity = this.field_145850_b.func_175625_s(this.field_174879_c);
               this.field_145850_b.func_180501_a(this.field_174879_c, this.field_145850_b.func_180495_p(this.field_174879_c).func_177226_a(IBlockEnabled.ENABLED, false), 3);
               if (tileentity != null) {
                  tileentity.func_145829_t();
                  this.field_145850_b.func_175690_a(this.field_174879_c, tileentity);
               }

               this.field_145850_b.func_180496_d(this.field_174879_c, this.func_145838_q());
               this.field_145850_b.func_180496_d(this.field_174879_c.func_177972_a(facing), this.func_145838_q());
               IBlockState state = this.field_145850_b.func_180495_p(this.field_174879_c);
               this.field_145850_b.markAndNotifyBlock(this.field_174879_c, this.field_145850_b.func_175726_f(this.field_174879_c), state, state, 3);
            }
         }

         ArrayList<Integer[]> nbe = (ArrayList)noteBlockEvents.get(this.field_145850_b.field_73011_w.getDimension());
         if (nbe != null) {
            Iterator var8 = nbe.iterator();

            while(var8.hasNext()) {
               Integer[] dat = (Integer[])var8.next();
               if (dat[3] == this.tone && dat[4] == this.note && this.func_145835_a((double)dat[0] + 0.5D, (double)dat[1] + 0.5D, (double)dat[2] + 0.5D) <= 4096.0D) {
                  EnumFacing facing = BlockStateUtils.getFacing(this.func_145832_p()).func_176734_d();
                  this.triggerNote(this.field_145850_b, this.field_174879_c, false);
                  this.redstoneSignal = 10;
                  TileEntity tileentity = this.field_145850_b.func_175625_s(this.field_174879_c);
                  this.field_145850_b.func_180501_a(this.field_174879_c, this.field_145850_b.func_180495_p(this.field_174879_c).func_177226_a(IBlockEnabled.ENABLED, true), 3);
                  if (tileentity != null) {
                     tileentity.func_145829_t();
                     this.field_145850_b.func_175690_a(this.field_174879_c, tileentity);
                  }

                  this.field_145850_b.func_180496_d(this.field_174879_c, this.func_145838_q());
                  this.field_145850_b.func_180496_d(this.field_174879_c.func_177972_a(facing), this.func_145838_q());
                  IBlockState state = this.field_145850_b.func_180495_p(this.field_174879_c);
                  this.field_145850_b.markAndNotifyBlock(this.field_174879_c, this.field_145850_b.func_175726_f(this.field_174879_c), state, state, 3);
                  break;
               }
            }
         }
      }

   }

   public void updateTone() {
      try {
         EnumFacing facing = BlockStateUtils.getFacing(this.func_145832_p()).func_176734_d();
         Material var5 = this.field_145850_b.func_180495_p(this.field_174879_c.func_177972_a(facing)).func_185904_a();
         this.tone = 0;
         if (var5 == Material.field_151576_e) {
            this.tone = 1;
         }

         if (var5 == Material.field_151595_p) {
            this.tone = 2;
         }

         if (var5 == Material.field_151592_s) {
            this.tone = 3;
         }

         if (var5 == Material.field_151575_d) {
            this.tone = 4;
         }

         this.func_70296_d();
      } catch (Exception var3) {
      }

   }

   public void changePitch() {
      this.note = (byte)((this.note + 1) % 25);
      this.func_70296_d();
   }

   public void triggerNote(World world, BlockPos pos, boolean sound) {
      byte var6 = -1;
      if (sound) {
         EnumFacing facing = BlockStateUtils.getFacing(this.func_145832_p()).func_176734_d();
         Material var5 = world.func_180495_p(pos.func_177972_a(facing)).func_185904_a();
         var6 = 0;
         if (var5 == Material.field_151576_e) {
            var6 = 1;
         }

         if (var5 == Material.field_151595_p) {
            var6 = 2;
         }

         if (var5 == Material.field_151592_s) {
            var6 = 3;
         }

         if (var5 == Material.field_151575_d) {
            var6 = 4;
         }
      }

      world.func_175641_c(pos, this.func_145838_q(), var6, this.note);
   }
}
