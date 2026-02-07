package thaumcraft.common.world.objects;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.storage.loot.LootTableList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.lib.utils.BlockUtils;

public class WorldGenGreatwoodTrees extends WorldGenAbstractTree {
   static final byte[] otherCoordPairs = new byte[]{2, 0, 0, 1, 2, 1};
   Random rand = new Random();
   World world;
   int[] basePos = new int[]{0, 0, 0};
   int heightLimit = 0;
   int height;
   double heightAttenuation = 0.618D;
   double branchDensity = 1.0D;
   double branchSlope = 0.38D;
   double scaleWidth = 1.2D;
   double leafDensity = 0.9D;
   int trunkSize = 2;
   int heightLimitLimit = 11;
   int leafDistanceLimit = 4;
   int[][] leafNodes;
   boolean spiders = false;

   public WorldGenGreatwoodTrees(boolean par1, boolean spiders) {
      super(par1);
      this.spiders = spiders;
   }

   void generateLeafNodeList() {
      this.height = (int)((double)this.heightLimit * this.heightAttenuation);
      if (this.height >= this.heightLimit) {
         this.height = this.heightLimit - 1;
      }

      int var1 = (int)(1.382D + Math.pow(this.leafDensity * (double)this.heightLimit / 13.0D, 2.0D));
      if (var1 < 1) {
         var1 = 1;
      }

      int[][] var2 = new int[var1 * this.heightLimit][4];
      int var3 = this.basePos[1] + this.heightLimit - this.leafDistanceLimit;
      int var4 = 1;
      int var5 = this.basePos[1] + this.height;
      int var6 = var3 - this.basePos[1];
      var2[0][0] = this.basePos[0];
      var2[0][1] = var3;
      var2[0][2] = this.basePos[2];
      var2[0][3] = var5;
      --var3;

      while(true) {
         while(var6 >= 0) {
            int var7 = 0;
            float var8 = this.layerSize(var6);
            if (var8 < 0.0F) {
               --var3;
               --var6;
            } else {
               for(double var9 = 0.5D; var7 < var1; ++var7) {
                  double var11 = this.scaleWidth * (double)var8 * ((double)this.rand.nextFloat() + 0.328D);
                  double var13 = (double)this.rand.nextFloat() * 2.0D * 3.141592653589793D;
                  int var15 = MathHelper.func_76128_c(var11 * Math.sin(var13) + (double)this.basePos[0] + var9);
                  int var16 = MathHelper.func_76128_c(var11 * Math.cos(var13) + (double)this.basePos[2] + var9);
                  int[] var17 = new int[]{var15, var3, var16};
                  int[] var18 = new int[]{var15, var3 + this.leafDistanceLimit, var16};
                  if (this.checkBlockLine(var17, var18) == -1) {
                     int[] var19 = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]};
                     double var20 = Math.sqrt(Math.pow((double)Math.abs(this.basePos[0] - var17[0]), 2.0D) + Math.pow((double)Math.abs(this.basePos[2] - var17[2]), 2.0D));
                     double var22 = var20 * this.branchSlope;
                     if ((double)var17[1] - var22 > (double)var5) {
                        var19[1] = var5;
                     } else {
                        var19[1] = (int)((double)var17[1] - var22);
                     }

                     if (this.checkBlockLine(var19, var17) == -1) {
                        var2[var4][0] = var15;
                        var2[var4][1] = var3;
                        var2[var4][2] = var16;
                        var2[var4][3] = var19[1];
                        ++var4;
                     }
                  }
               }

               --var3;
               --var6;
            }
         }

         this.leafNodes = new int[var4][4];
         System.arraycopy(var2, 0, this.leafNodes, 0, var4);
         return;
      }
   }

   void genTreeLayer(int par1, int par2, int par3, float par4, byte par5, Block par6) {
      int var7 = (int)((double)par4 + 0.618D);
      byte var8 = otherCoordPairs[par5];
      byte var9 = otherCoordPairs[par5 + 3];
      int[] var10 = new int[]{par1, par2, par3};
      int[] var11 = new int[]{0, 0, 0};
      int var12 = -var7;
      int var13 = -var7;

      label47:
      for(var11[par5] = var10[par5]; var12 <= var7; ++var12) {
         var11[var8] = var10[var8] + var12;
         var13 = -var7;

         while(true) {
            while(true) {
               if (var13 > var7) {
                  continue label47;
               }

               double var15 = Math.pow((double)Math.abs(var12) + 0.5D, 2.0D) + Math.pow((double)Math.abs(var13) + 0.5D, 2.0D);
               if (var15 > (double)(par4 * par4)) {
                  ++var13;
               } else {
                  try {
                     var11[var9] = var10[var9] + var13;
                     IBlockState state = this.world.func_180495_p(new BlockPos(var11[0], var11[1], var11[2]));
                     Block block = state.func_177230_c();
                     if ((block == Blocks.field_150350_a || block == BlocksTC.leaf) && (block == null || block.canBeReplacedByLeaves(state, this.world, new BlockPos(var11[0], var11[1], var11[2])))) {
                        this.func_175903_a(this.world, new BlockPos(var11[0], var11[1], var11[2]), par6.func_176223_P());
                     }
                  } catch (Exception var18) {
                  }

                  ++var13;
               }
            }
         }
      }

   }

   float layerSize(int par1) {
      if ((double)par1 < (double)((float)this.heightLimit) * 0.3D) {
         return -1.618F;
      } else {
         float var2 = (float)this.heightLimit / 2.0F;
         float var3 = (float)this.heightLimit / 2.0F - (float)par1;
         float var4;
         if (var3 == 0.0F) {
            var4 = var2;
         } else if (Math.abs(var3) >= var2) {
            var4 = 0.0F;
         } else {
            var4 = (float)Math.sqrt(Math.pow((double)Math.abs(var2), 2.0D) - Math.pow((double)Math.abs(var3), 2.0D));
         }

         var4 *= 0.5F;
         return var4;
      }
   }

   float leafSize(int par1) {
      return par1 >= 0 && par1 < this.leafDistanceLimit ? (par1 != 0 && par1 != this.leafDistanceLimit - 1 ? 3.0F : 2.0F) : -1.0F;
   }

   void generateLeafNode(int par1, int par2, int par3) {
      int var4 = par2;

      for(int var5 = par2 + this.leafDistanceLimit; var4 < var5; ++var4) {
         float var6 = this.leafSize(var4 - par2);
         this.genTreeLayer(par1, var4, par3, var6, (byte)1, BlocksTC.leaf);
      }

   }

   void placeBlockLine(int[] par1ArrayOfInteger, int[] par2ArrayOfInteger, Block par3) {
      int[] var4 = new int[]{0, 0, 0};
      byte var5 = 0;

      byte var6;
      for(var6 = 0; var5 < 3; ++var5) {
         var4[var5] = par2ArrayOfInteger[var5] - par1ArrayOfInteger[var5];
         if (Math.abs(var4[var5]) > Math.abs(var4[var6])) {
            var6 = var5;
         }
      }

      if (var4[var6] != 0) {
         byte var7 = otherCoordPairs[var6];
         byte var8 = otherCoordPairs[var6 + 3];
         byte var9;
         if (var4[var6] > 0) {
            var9 = 1;
         } else {
            var9 = -1;
         }

         double var10 = (double)var4[var7] / (double)var4[var6];
         double var12 = (double)var4[var8] / (double)var4[var6];
         int[] var14 = new int[]{0, 0, 0};
         int var15 = 0;

         for(int var16 = var4[var6] + var9; var15 != var16; var15 += var9) {
            var14[var6] = MathHelper.func_76128_c((double)(par1ArrayOfInteger[var6] + var15) + 0.5D);
            var14[var7] = MathHelper.func_76128_c((double)par1ArrayOfInteger[var7] + (double)var15 * var10 + 0.5D);
            var14[var8] = MathHelper.func_76128_c((double)par1ArrayOfInteger[var8] + (double)var15 * var12 + 0.5D);
            byte var17 = 1;
            int var18 = Math.abs(var14[0] - par1ArrayOfInteger[0]);
            int var19 = Math.abs(var14[2] - par1ArrayOfInteger[2]);
            int var20 = Math.max(var18, var19);
            if (var20 > 0) {
               if (var18 == var20) {
                  var17 = 0;
               } else if (var19 == var20) {
                  var17 = 2;
               }
            }

            if (this.isReplaceable(this.world, new BlockPos(var14[0], var14[1], var14[2]))) {
               this.func_175903_a(this.world, new BlockPos(var14[0], var14[1], var14[2]), par3.func_176203_a(var17));
            }
         }
      }

   }

   void generateLeaves() {
      int var1 = 0;

      for(int var2 = this.leafNodes.length; var1 < var2; ++var1) {
         int var3 = this.leafNodes[var1][0];
         int var4 = this.leafNodes[var1][1];
         int var5 = this.leafNodes[var1][2];
         this.generateLeafNode(var3, var4, var5);
      }

   }

   boolean leafNodeNeedsBase(int par1) {
      return (double)par1 >= (double)this.heightLimit * 0.2D;
   }

   void generateTrunk() {
      int var1 = this.basePos[0];
      int var2 = this.basePos[1];
      int var3 = this.basePos[1] + this.height;
      int var4 = this.basePos[2];
      int[] var5 = new int[]{var1, var2, var4};
      int[] var6 = new int[]{var1, var3, var4};
      this.placeBlockLine(var5, var6, BlocksTC.log);
      if (this.trunkSize == 2) {
         int var10002 = var5[0]++;
         var10002 = var6[0]++;
         this.placeBlockLine(var5, var6, BlocksTC.log);
         var10002 = var5[2]++;
         var10002 = var6[2]++;
         this.placeBlockLine(var5, var6, BlocksTC.log);
         var5[0] += -1;
         var6[0] += -1;
         this.placeBlockLine(var5, var6, BlocksTC.log);
      }

   }

   void generateLeafNodeBases() {
      int var1 = 0;
      int var2 = this.leafNodes.length;

      for(int[] var3 = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]}; var1 < var2; ++var1) {
         int[] var4 = this.leafNodes[var1];
         int[] var5 = new int[]{var4[0], var4[1], var4[2]};
         var3[1] = var4[3];
         int var6 = var3[1] - this.basePos[1];
         if (this.leafNodeNeedsBase(var6)) {
            this.placeBlockLine(var3, var5, BlocksTC.log);
         }
      }

   }

   int checkBlockLine(int[] par1ArrayOfInteger, int[] par2ArrayOfInteger) {
      int[] var3 = new int[]{0, 0, 0};
      byte var4 = 0;

      byte var5;
      for(var5 = 0; var4 < 3; ++var4) {
         var3[var4] = par2ArrayOfInteger[var4] - par1ArrayOfInteger[var4];
         if (Math.abs(var3[var4]) > Math.abs(var3[var5])) {
            var5 = var4;
         }
      }

      if (var3[var5] == 0) {
         return -1;
      } else {
         byte var6 = otherCoordPairs[var5];
         byte var7 = otherCoordPairs[var5 + 3];
         byte var8;
         if (var3[var5] > 0) {
            var8 = 1;
         } else {
            var8 = -1;
         }

         double var9 = (double)var3[var6] / (double)var3[var5];
         double var11 = (double)var3[var7] / (double)var3[var5];
         int[] var13 = new int[]{0, 0, 0};
         int var14 = 0;

         int var15;
         for(var15 = var3[var5] + var8; var14 != var15; var14 += var8) {
            var13[var5] = par1ArrayOfInteger[var5] + var14;
            var13[var6] = MathHelper.func_76128_c((double)par1ArrayOfInteger[var6] + (double)var14 * var9);
            var13[var7] = MathHelper.func_76128_c((double)par1ArrayOfInteger[var7] + (double)var14 * var11);

            try {
               Block var16 = this.world.func_180495_p(new BlockPos(var13[0], var13[1], var13[2])).func_177230_c();
               if (var16 != Blocks.field_150350_a && var16 != BlocksTC.leaf) {
                  break;
               }
            } catch (Exception var17) {
            }
         }

         return var14 == var15 ? -1 : Math.abs(var14);
      }
   }

   boolean validTreeLocation(int x, int z) {
      int[] var1 = new int[]{this.basePos[0] + x, this.basePos[1], this.basePos[2] + z};
      int[] var2 = new int[]{this.basePos[0] + x, this.basePos[1] + this.heightLimit - 1, this.basePos[2] + z};

      try {
         IBlockState state = this.world.func_180495_p(new BlockPos(this.basePos[0] + x, this.basePos[1] - 1, this.basePos[2] + z));
         Block var3 = state.func_177230_c();
         boolean isSoil = var3.canSustainPlant(state, this.world, new BlockPos(this.basePos[0] + x, this.basePos[1] - 1, this.basePos[2] + z), EnumFacing.UP, (BlockSapling)Blocks.field_150345_g);
         if (!isSoil) {
            return false;
         } else {
            int var4 = this.checkBlockLine(var1, var2);
            if (var4 == -1) {
               return true;
            } else if (var4 < 6) {
               return false;
            } else {
               this.heightLimit = var4;
               return true;
            }
         }
      } catch (Exception var9) {
         return false;
      }
   }

   public void setScale(double par1, double par3, double par5) {
   }

   public boolean func_180709_b(World par1World, Random par2Random, BlockPos pos) {
      this.world = par1World;
      long var6 = par2Random.nextLong();
      this.rand.setSeed(var6);
      this.basePos[0] = pos.func_177958_n();
      this.basePos[1] = pos.func_177956_o();
      this.basePos[2] = pos.func_177952_p();
      if (this.heightLimit == 0) {
         this.heightLimit = this.heightLimitLimit + this.rand.nextInt(this.heightLimitLimit);
      }

      int x = false;
      int z = false;
      boolean valid = false;

      int a;
      label77:
      for(int a = -1; a < 2; ++a) {
         label75:
         for(a = -1; a < 2; ++a) {
            for(int x = 0; x < this.trunkSize; ++x) {
               for(int z = 0; z < this.trunkSize; ++z) {
                  if (!this.validTreeLocation(x + a, z + a)) {
                     continue label75;
                  }
               }
            }

            valid = true;
            this.basePos[0] += a;
            this.basePos[2] += a;
            break label77;
         }
      }

      if (!valid) {
         return false;
      } else {
         this.generateLeafNodeList();
         this.generateLeaves();
         this.generateLeafNodeBases();
         this.generateTrunk();
         this.scaleWidth = 1.66D;
         this.basePos[0] = pos.func_177958_n();
         this.basePos[1] = pos.func_177956_o() + this.height;
         this.basePos[2] = pos.func_177952_p();
         this.generateLeafNodeList();
         this.generateLeaves();
         this.generateLeafNodeBases();
         this.generateTrunk();
         if (this.spiders) {
            this.world.func_175656_a(pos.func_177977_b(), Blocks.field_150474_ac.func_176223_P());
            TileEntityMobSpawner var14 = (TileEntityMobSpawner)par1World.func_175625_s(pos.func_177977_b());
            if (var14 != null) {
               var14.func_145881_a().func_98272_a("CaveSpider");

               for(a = 0; a < 50; ++a) {
                  int xx = pos.func_177958_n() - 7 + par2Random.nextInt(14);
                  int yy = pos.func_177956_o() + par2Random.nextInt(10);
                  int zz = pos.func_177952_p() - 7 + par2Random.nextInt(14);
                  if (par1World.func_175623_d(new BlockPos(xx, yy, zz)) && (BlockUtils.isBlockTouching(par1World, new BlockPos(xx, yy, zz), (Block)BlocksTC.leaf) || BlockUtils.isBlockTouching(par1World, new BlockPos(xx, yy, zz), (Block)BlocksTC.log))) {
                     this.world.func_175656_a(new BlockPos(xx, yy, zz), Blocks.field_150321_G.func_176223_P());
                  }
               }

               par1World.func_175656_a(pos.func_177979_c(2), Blocks.field_150486_ae.func_176223_P());
               TileEntityChest var16 = (TileEntityChest)par1World.func_175625_s(pos.func_177979_c(2));
               if (var16 != null) {
                  var16.func_189404_a(LootTableList.field_186422_d, this.rand.nextLong());
               }
            }
         }

         return true;
      }
   }
}
