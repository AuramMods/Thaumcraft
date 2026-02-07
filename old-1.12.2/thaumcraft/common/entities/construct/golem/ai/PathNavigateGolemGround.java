package thaumcraft.common.entities.construct.golem.ai;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateGolemGround extends PathNavigateGround {
   protected GolemNodeProcessor field_179695_a;
   private boolean field_179694_f;

   public PathNavigateGolemGround(EntityLiving p_i45875_1_, World worldIn) {
      super(p_i45875_1_, worldIn);
   }

   protected PathFinder func_179679_a() {
      this.field_179695_a = new GolemNodeProcessor();
      return new PathFinder(this.field_179695_a);
   }

   private int func_179687_p() {
      if (this.field_75515_a.func_70090_H() && this.func_179684_h()) {
         int i = (int)this.field_75515_a.func_174813_aQ().field_72338_b;
         Block block = this.field_75513_b.func_180495_p(new BlockPos(MathHelper.func_76128_c(this.field_75515_a.field_70165_t), i, MathHelper.func_76128_c(this.field_75515_a.field_70161_v))).func_177230_c();
         int j = 0;

         do {
            if (block != Blocks.field_150358_i && block != Blocks.field_150355_j) {
               return i;
            }

            ++i;
            block = this.field_75513_b.func_180495_p(new BlockPos(MathHelper.func_76128_c(this.field_75515_a.field_70165_t), i, MathHelper.func_76128_c(this.field_75515_a.field_70161_v))).func_177230_c();
            ++j;
         } while(j <= 16);

         return (int)this.field_75515_a.func_174813_aQ().field_72338_b;
      } else {
         return (int)(this.field_75515_a.func_174813_aQ().field_72338_b + 0.5D);
      }
   }

   protected void func_75487_m() {
      super.func_75487_m();
      if (this.field_179694_f) {
         if (this.field_75513_b.func_175678_i(new BlockPos(MathHelper.func_76128_c(this.field_75515_a.field_70165_t), (int)(this.field_75515_a.func_174813_aQ().field_72338_b + 0.5D), MathHelper.func_76128_c(this.field_75515_a.field_70161_v)))) {
            return;
         }

         for(int i = 0; i < this.field_75514_c.func_75874_d(); ++i) {
            PathPoint pathpoint = this.field_75514_c.func_75877_a(i);
            if (this.field_75513_b.func_175678_i(new BlockPos(pathpoint.field_75839_a, pathpoint.field_75837_b, pathpoint.field_75838_c))) {
               this.field_75514_c.func_75871_b(i - 1);
               return;
            }
         }
      }

   }

   private boolean func_179683_a(int p_179683_1_, int p_179683_2_, int p_179683_3_, int p_179683_4_, int p_179683_5_, int p_179683_6_, Vec3d p_179683_7_, double p_179683_8_, double p_179683_10_) {
      int k1 = p_179683_1_ - p_179683_4_ / 2;
      int l1 = p_179683_3_ - p_179683_6_ / 2;
      if (!this.func_179692_b(k1, p_179683_2_, l1, p_179683_4_, p_179683_5_, p_179683_6_, p_179683_7_, p_179683_8_, p_179683_10_)) {
         return false;
      } else {
         for(int i2 = k1; i2 < k1 + p_179683_4_; ++i2) {
            for(int j2 = l1; j2 < l1 + p_179683_6_; ++j2) {
               double d2 = (double)i2 + 0.5D - p_179683_7_.field_72450_a;
               double d3 = (double)j2 + 0.5D - p_179683_7_.field_72449_c;
               if (d2 * p_179683_8_ + d3 * p_179683_10_ >= 0.0D) {
                  IBlockState block = this.field_75513_b.func_180495_p(new BlockPos(i2, p_179683_2_ - 1, j2));
                  Material material = block.func_185904_a();
                  if (material == Material.field_151579_a) {
                     return false;
                  }

                  if (material == Material.field_151586_h && !this.field_75515_a.func_70090_H()) {
                     return false;
                  }

                  if (material == Material.field_151587_i) {
                     return false;
                  }
               }
            }
         }

         return true;
      }
   }

   private boolean func_179692_b(int p_179692_1_, int p_179692_2_, int p_179692_3_, int p_179692_4_, int p_179692_5_, int p_179692_6_, Vec3d p_179692_7_, double p_179692_8_, double p_179692_10_) {
      Iterator iterator = BlockPos.func_177980_a(new BlockPos(p_179692_1_, p_179692_2_, p_179692_3_), new BlockPos(p_179692_1_ + p_179692_4_ - 1, p_179692_2_ + p_179692_5_ - 1, p_179692_3_ + p_179692_6_ - 1)).iterator();

      while(iterator.hasNext()) {
         BlockPos blockpos = (BlockPos)iterator.next();
         double d2 = (double)blockpos.func_177958_n() + 0.5D - p_179692_7_.field_72450_a;
         double d3 = (double)blockpos.func_177952_p() + 0.5D - p_179692_7_.field_72449_c;
         if (d2 * p_179692_8_ + d3 * p_179692_10_ >= 0.0D) {
            Block block = this.field_75513_b.func_180495_p(blockpos).func_177230_c();
            if (!block.func_176205_b(this.field_75513_b, blockpos)) {
               return false;
            }
         }
      }

      return true;
   }
}
