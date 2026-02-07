package thaumcraft.common.tiles.devices;

import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileBellows extends TileThaumcraft implements ITickable {
   public float inflation = 1.0F;
   boolean direction = false;
   boolean firstrun = true;
   public int delay = 0;

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return new AxisAlignedBB((double)this.func_174877_v().func_177958_n() - 0.3D, (double)this.func_174877_v().func_177956_o() - 0.3D, (double)this.func_174877_v().func_177952_p() - 0.3D, (double)this.func_174877_v().func_177958_n() + 1.3D, (double)this.func_174877_v().func_177956_o() + 1.3D, (double)this.func_174877_v().func_177952_p() + 1.3D);
   }

   public void func_73660_a() {
      if (this.field_145850_b.field_72995_K) {
         if (BlockStateUtils.isEnabled(this.func_145832_p())) {
            if (this.firstrun) {
               this.inflation = 0.35F + this.field_145850_b.field_73012_v.nextFloat() * 0.55F;
            }

            this.firstrun = false;
            if (this.inflation > 0.35F && !this.direction) {
               this.inflation -= 0.075F;
            }

            if (this.inflation <= 0.35F && !this.direction) {
               this.direction = true;
            }

            if (this.inflation < 1.0F && this.direction) {
               this.inflation += 0.025F;
            }

            if (this.inflation >= 1.0F && this.direction) {
               this.direction = false;
               this.field_145850_b.func_184134_a((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), SoundEvents.field_187557_bK, SoundCategory.AMBIENT, 0.01F, 0.5F + (this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.2F, false);
            }
         }
      } else if (BlockStateUtils.isEnabled(this.func_145832_p())) {
         ++this.delay;
         if (this.delay >= 2) {
            this.delay = 0;
            TileEntity tile = this.field_145850_b.func_175625_s(this.field_174879_c.func_177972_a(BlockStateUtils.getFacing(this.func_145832_p())));
            if (tile != null && tile instanceof TileEntityFurnace) {
               TileEntityFurnace tf = (TileEntityFurnace)tile;
               int ct = this.getCooktime(tf);
               if (ct > 0 && ct < 199) {
                  this.setCooktime(tf, ct + 1);
               }
            }
         }
      }

   }

   public void setCooktime(TileEntityFurnace ent, int hit) {
      ent.field_174906_k = hit;
   }

   public int getCooktime(TileEntityFurnace ent) {
      return ent.field_174906_k;
   }

   public static int getBellows(World world, BlockPos pos, EnumFacing[] directions) {
      int bellows = 0;
      EnumFacing[] var4 = directions;
      int var5 = directions.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EnumFacing dir = var4[var6];
         TileEntity tile = world.func_175625_s(pos.func_177972_a(dir));

         try {
            if (tile != null && tile instanceof TileBellows && BlockStateUtils.getFacing(tile.func_145832_p()) == dir.func_176734_d() && BlockStateUtils.isEnabled(tile.func_145832_p())) {
               ++bellows;
            }
         } catch (Exception var10) {
         }
      }

      return bellows;
   }
}
