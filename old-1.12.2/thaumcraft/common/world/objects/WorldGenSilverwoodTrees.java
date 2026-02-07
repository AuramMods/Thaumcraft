package thaumcraft.common.world.objects;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.world.ThaumcraftWorldGenerator;

public class WorldGenSilverwoodTrees extends WorldGenAbstractTree {
   private final int minTreeHeight;
   private final int randomTreeHeight;
   boolean worldgen = false;

   public WorldGenSilverwoodTrees(boolean doBlockNotify, int minTreeHeight, int randomTreeHeight) {
      super(doBlockNotify);
      this.worldgen = !doBlockNotify;
      this.minTreeHeight = minTreeHeight;
      this.randomTreeHeight = randomTreeHeight;
   }

   public boolean func_180709_b(World world, Random random, BlockPos pos) {
      int height = random.nextInt(this.randomTreeHeight) + this.minTreeHeight;
      boolean flag = true;
      int x = pos.func_177958_n();
      int y = pos.func_177956_o();
      int z = pos.func_177952_p();
      if (y >= 1 && y + height + 1 <= 256) {
         for(int i1 = y; i1 <= y + 1 + height; ++i1) {
            byte spread = 1;
            if (i1 == y) {
               spread = 0;
            }

            if (i1 >= y + 1 + height - 2) {
               spread = 3;
            }

            for(int j1 = x - spread; j1 <= x + spread && flag; ++j1) {
               for(int k1 = z - spread; k1 <= z + spread && flag; ++k1) {
                  if (i1 >= 0 && i1 < 256) {
                     IBlockState state = world.func_180495_p(new BlockPos(j1, i1, k1));
                     Block block = state.func_177230_c();
                     if (!block.isAir(state, world, new BlockPos(j1, i1, k1)) && !block.isLeaves(state, world, new BlockPos(j1, i1, k1)) && !block.func_176200_f(world, new BlockPos(j1, i1, k1)) && i1 > y) {
                        flag = false;
                     }
                  } else {
                     flag = false;
                  }
               }
            }
         }

         if (!flag) {
            return false;
         } else {
            IBlockState state = world.func_180495_p(new BlockPos(x, y - 1, z));
            Block block1 = state.func_177230_c();
            boolean isSoil = block1.canSustainPlant(state, world, new BlockPos(x, y - 1, z), EnumFacing.UP, (BlockSapling)Blocks.field_150345_g);
            if (isSoil && y < 256 - height - 1) {
               block1.onPlantGrow(state, world, new BlockPos(x, y - 1, z), new BlockPos(x, y, z));
               int start = y + height - 5;
               int end = y + height + 3 + random.nextInt(3);

               int k2;
               for(k2 = start; k2 <= end; ++k2) {
                  int cty = MathHelper.func_76125_a(k2, y + height - 3, y + height);

                  for(int xx = x - 5; xx <= x + 5; ++xx) {
                     for(int zz = z - 5; zz <= z + 5; ++zz) {
                        double d3 = (double)(xx - x);
                        double d4 = (double)(k2 - cty);
                        double d5 = (double)(zz - z);
                        double dist = d3 * d3 + d4 * d4 + d5 * d5;
                        IBlockState s2 = world.func_180495_p(new BlockPos(xx, k2, zz));
                        if (dist < (double)(10 + random.nextInt(8)) && s2.func_177230_c().canBeReplacedByLeaves(s2, world, new BlockPos(xx, k2, zz))) {
                           this.func_175903_a(world, new BlockPos(xx, k2, zz), BlocksTC.leaf.func_176203_a(1));
                        }
                     }
                  }
               }

               for(k2 = 0; k2 < height; ++k2) {
                  IBlockState s3 = world.func_180495_p(new BlockPos(x, y + k2, z));
                  Block block2 = s3.func_177230_c();
                  if (block2.isAir(s3, world, new BlockPos(x, y + k2, z)) || block2.isLeaves(s3, world, new BlockPos(x, y + k2, z)) || block2.func_176200_f(world, new BlockPos(x, y + k2, z))) {
                     this.func_175903_a(world, new BlockPos(x, y + k2, z), BlocksTC.log.func_176203_a(4));
                     this.func_175903_a(world, new BlockPos(x - 1, y + k2, z), BlocksTC.log.func_176203_a(4));
                     this.func_175903_a(world, new BlockPos(x + 1, y + k2, z), BlocksTC.log.func_176203_a(4));
                     this.func_175903_a(world, new BlockPos(x, y + k2, z - 1), BlocksTC.log.func_176203_a(4));
                     this.func_175903_a(world, new BlockPos(x, y + k2, z + 1), BlocksTC.log.func_176203_a(4));
                  }
               }

               if (random.nextFloat() < 0.3F) {
                  int q = height / 2 - 1 + random.nextInt(3);
                  ThaumcraftWorldGenerator.spawnNode(world, new BlockPos(x, y + q, z), 3, 0.33F, false);
               }

               this.func_175903_a(world, new BlockPos(x, y + k2, z), BlocksTC.log.func_176203_a(4));
               this.func_175903_a(world, new BlockPos(x - 1, y, z - 1), BlocksTC.log.func_176203_a(4));
               this.func_175903_a(world, new BlockPos(x + 1, y, z + 1), BlocksTC.log.func_176203_a(4));
               this.func_175903_a(world, new BlockPos(x - 1, y, z + 1), BlocksTC.log.func_176203_a(4));
               this.func_175903_a(world, new BlockPos(x + 1, y, z - 1), BlocksTC.log.func_176203_a(4));
               if (random.nextInt(3) != 0) {
                  this.func_175903_a(world, new BlockPos(x - 1, y + 1, z - 1), BlocksTC.log.func_176203_a(4));
               }

               if (random.nextInt(3) != 0) {
                  this.func_175903_a(world, new BlockPos(x + 1, y + 1, z + 1), BlocksTC.log.func_176203_a(4));
               }

               if (random.nextInt(3) != 0) {
                  this.func_175903_a(world, new BlockPos(x - 1, y + 1, z + 1), BlocksTC.log.func_176203_a(4));
               }

               if (random.nextInt(3) != 0) {
                  this.func_175903_a(world, new BlockPos(x + 1, y + 1, z - 1), BlocksTC.log.func_176203_a(4));
               }

               this.func_175903_a(world, new BlockPos(x - 2, y, z), BlocksTC.log.func_176203_a(3));
               this.func_175903_a(world, new BlockPos(x + 2, y, z), BlocksTC.log.func_176203_a(3));
               this.func_175903_a(world, new BlockPos(x, y, z - 2), BlocksTC.log.func_176203_a(5));
               this.func_175903_a(world, new BlockPos(x, y, z + 2), BlocksTC.log.func_176203_a(5));
               this.func_175903_a(world, new BlockPos(x - 2, y - 1, z), BlocksTC.log.func_176203_a(4));
               this.func_175903_a(world, new BlockPos(x + 2, y - 1, z), BlocksTC.log.func_176203_a(4));
               this.func_175903_a(world, new BlockPos(x, y - 1, z - 2), BlocksTC.log.func_176203_a(4));
               this.func_175903_a(world, new BlockPos(x, y - 1, z + 2), BlocksTC.log.func_176203_a(4));
               this.func_175903_a(world, new BlockPos(x - 1, y + (height - 4), z - 1), BlocksTC.log.func_176203_a(4));
               this.func_175903_a(world, new BlockPos(x + 1, y + (height - 4), z + 1), BlocksTC.log.func_176203_a(4));
               this.func_175903_a(world, new BlockPos(x - 1, y + (height - 4), z + 1), BlocksTC.log.func_176203_a(4));
               this.func_175903_a(world, new BlockPos(x + 1, y + (height - 4), z - 1), BlocksTC.log.func_176203_a(4));
               if (random.nextInt(3) == 0) {
                  this.func_175903_a(world, new BlockPos(x - 1, y + (height - 5), z - 1), BlocksTC.log.func_176203_a(4));
               }

               if (random.nextInt(3) == 0) {
                  this.func_175903_a(world, new BlockPos(x + 1, y + (height - 5), z + 1), BlocksTC.log.func_176203_a(4));
               }

               if (random.nextInt(3) == 0) {
                  this.func_175903_a(world, new BlockPos(x - 1, y + (height - 5), z + 1), BlocksTC.log.func_176203_a(4));
               }

               if (random.nextInt(3) == 0) {
                  this.func_175903_a(world, new BlockPos(x + 1, y + (height - 5), z - 1), BlocksTC.log.func_176203_a(4));
               }

               this.func_175903_a(world, new BlockPos(x - 2, y + (height - 4), z), BlocksTC.log.func_176203_a(3));
               this.func_175903_a(world, new BlockPos(x + 2, y + (height - 4), z), BlocksTC.log.func_176203_a(3));
               this.func_175903_a(world, new BlockPos(x, y + (height - 4), z - 2), BlocksTC.log.func_176203_a(5));
               this.func_175903_a(world, new BlockPos(x, y + (height - 4), z + 2), BlocksTC.log.func_176203_a(5));
               if (this.worldgen) {
                  WorldGenerator flowers = new WorldGenCustomFlowers(BlocksTC.shimmerleaf, 0);
                  flowers.func_180709_b(world, random, new BlockPos(x, y, z));
               }

               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }
}
