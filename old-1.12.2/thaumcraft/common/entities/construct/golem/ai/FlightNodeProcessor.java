package thaumcraft.common.entities.construct.golem.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.IBlockAccess;

public class FlightNodeProcessor extends NodeProcessor {
   public PathPoint func_186325_a(double x, double y, double z) {
      return this.func_176159_a(MathHelper.func_76128_c(x - (double)(this.field_186326_b.field_70130_N / 2.0F)), MathHelper.func_76128_c(y + 0.5D), MathHelper.func_76128_c(z - (double)(this.field_186326_b.field_70130_N / 2.0F)));
   }

   public PathPoint func_186318_b() {
      return this.func_176159_a(MathHelper.func_76128_c(this.field_186326_b.func_174813_aQ().field_72340_a), MathHelper.func_76128_c(this.field_186326_b.func_174813_aQ().field_72338_b + 0.5D), MathHelper.func_76128_c(this.field_186326_b.func_174813_aQ().field_72339_c));
   }

   public int func_186320_a(PathPoint[] p_186320_1_, PathPoint p_186320_2_, PathPoint p_186320_3_, float p_186320_4_) {
      int i = 0;
      EnumFacing[] var6 = EnumFacing.values();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         EnumFacing enumfacing = var6[var8];
         PathPoint pathpoint = this.func_186328_b(p_186320_2_.field_75839_a + enumfacing.func_82601_c(), p_186320_2_.field_75837_b + enumfacing.func_96559_d(), p_186320_2_.field_75838_c + enumfacing.func_82599_e());
         if (pathpoint != null && !pathpoint.field_75842_i && pathpoint.func_75829_a(p_186320_3_) < p_186320_4_) {
            p_186320_1_[i++] = pathpoint;
         }
      }

      return i;
   }

   public PathNodeType func_186319_a(IBlockAccess p_186319_1_, int p_186319_2_, int p_186319_3_, int p_186319_4_, EntityLiving p_186319_5_, int p_186319_6_, int p_186319_7_, int p_186319_8_, boolean p_186319_9_, boolean p_186319_10_) {
      return PathNodeType.WATER;
   }

   public PathNodeType func_186330_a(IBlockAccess x, int y, int z, int p_186330_4_) {
      return PathNodeType.WATER;
   }

   private PathPoint func_186328_b(int p_186328_1_, int p_186328_2_, int p_186328_3_) {
      PathNodeType pathnodetype = this.func_186327_c(p_186328_1_, p_186328_2_, p_186328_3_);
      return pathnodetype == PathNodeType.WALKABLE ? this.func_176159_a(p_186328_1_, p_186328_2_, p_186328_3_) : null;
   }

   private PathNodeType func_186327_c(int p_186327_1_, int p_186327_2_, int p_186327_3_) {
      MutableBlockPos blockpos$mutableblockpos = new MutableBlockPos();

      for(int i = p_186327_1_; i < p_186327_1_ + this.field_176168_c; ++i) {
         for(int j = p_186327_2_; j < p_186327_2_ + this.field_176165_d; ++j) {
            for(int k = p_186327_3_; k < p_186327_3_ + this.field_176166_e; ++k) {
               IBlockState iblockstate = this.field_176169_a.func_180495_p(blockpos$mutableblockpos.func_181079_c(i, j, k));
               if (!this.field_176169_a.func_175623_d(blockpos$mutableblockpos.func_181079_c(i, j, k)) && !iblockstate.func_177230_c().func_176205_b(this.field_176169_a, blockpos$mutableblockpos.func_181079_c(i, j, k))) {
                  return PathNodeType.BLOCKED;
               }
            }
         }
      }

      return PathNodeType.WALKABLE;
   }
}
