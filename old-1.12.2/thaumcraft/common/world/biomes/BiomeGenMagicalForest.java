package thaumcraft.common.world.biomes;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.block.BlockTallGrass.EnumType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityPech;
import thaumcraft.common.entities.monster.EntityWisp;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.world.objects.WorldGenBigMagicTree;
import thaumcraft.common.world.objects.WorldGenGreatwoodTrees;
import thaumcraft.common.world.objects.WorldGenSilverwoodTrees;

public class BiomeGenMagicalForest extends Biome {
   protected WorldGenBigMagicTree bigTree = new WorldGenBigMagicTree(false);
   private static final WorldGenBlockBlob blobs;

   public BiomeGenMagicalForest(BiomeProperties par1) {
      super(par1);
      this.field_76762_K.add(new SpawnListEntry(EntityWolf.class, 2, 1, 3));
      this.field_76762_K.add(new SpawnListEntry(EntityHorse.class, 2, 1, 3));
      this.field_76761_J.add(new SpawnListEntry(EntityWitch.class, 3, 1, 1));
      this.field_76761_J.add(new SpawnListEntry(EntityEnderman.class, 3, 1, 1));
      if (Config.spawnPech) {
         this.field_76761_J.add(new SpawnListEntry(EntityPech.class, 10, 1, 2));
      }

      if (Config.spawnWisp) {
         this.field_76761_J.add(new SpawnListEntry(EntityWisp.class, 10, 1, 2));
      }

      this.field_76760_I.field_76832_z = 2;
      this.field_76760_I.field_76802_A = 10;
      this.field_76760_I.field_76803_B = 12;
      this.field_76760_I.field_76833_y = 6;
      this.field_76760_I.field_76798_D = 6;
   }

   public WorldGenAbstractTree func_150567_a(Random par1Random) {
      return (WorldGenAbstractTree)(par1Random.nextInt(14) == 0 ? new WorldGenSilverwoodTrees(false, 8, 5) : (par1Random.nextInt(10) == 0 ? new WorldGenGreatwoodTrees(false, par1Random.nextInt(8) == 0) : this.bigTree));
   }

   public WorldGenerator func_76730_b(Random par1Random) {
      return par1Random.nextInt(4) == 0 ? new WorldGenTallGrass(EnumType.FERN) : new WorldGenTallGrass(EnumType.GRASS);
   }

   @SideOnly(Side.CLIENT)
   public int func_180627_b(BlockPos p_180627_1_) {
      return Config.blueBiome ? 6728396 : 5635969;
   }

   @SideOnly(Side.CLIENT)
   public int func_180625_c(BlockPos p_180625_1_) {
      return Config.blueBiome ? 7851246 : 6750149;
   }

   public int getWaterColorMultiplier() {
      return 30702;
   }

   public void func_180624_a(World world, Random random, BlockPos pos) {
      int a;
      BlockPos p2;
      Block l2;
      for(a = 0; a < 3; ++a) {
         p2 = new BlockPos(pos);
         p2 = p2.func_177982_a(4 + random.nextInt(8), 0, 4 + random.nextInt(8));

         for(p2 = world.func_175645_m(p2); p2.func_177956_o() > 30 && world.func_180495_p(p2).func_177230_c() != Blocks.field_150349_c; p2 = p2.func_177977_b()) {
         }

         l2 = world.func_180495_p(p2).func_177230_c();
         if (l2 == Blocks.field_150349_c) {
            world.func_180501_a(p2, BlocksTC.grassAmbient.func_176223_P(), 2);
            break;
         }
      }

      int k = random.nextInt(3);

      int l;
      BlockPos p2;
      for(l = 0; l < k; ++l) {
         p2 = new BlockPos(pos);
         p2 = p2.func_177982_a(random.nextInt(16) + 8, 0, random.nextInt(16) + 8);
         p2 = world.func_175645_m(p2);
         blobs.func_180709_b(world, random, p2);
      }

      for(k = 0; k < 4; ++k) {
         for(l = 0; l < 4; ++l) {
            if (random.nextInt(40) == 0) {
               p2 = new BlockPos(pos);
               p2 = p2.func_177982_a(k * 4 + 1 + 8 + random.nextInt(3), 0, l * 4 + 1 + 8 + random.nextInt(3));
               p2 = world.func_175645_m(p2);
               WorldGenBigMushroom worldgenbigmushroom = new WorldGenBigMushroom();
               worldgenbigmushroom.func_180709_b(world, random, p2);
            }
         }
      }

      try {
         super.func_180624_a(world, random, pos);
      } catch (Exception var9) {
      }

      for(a = 0; a < 8; ++a) {
         p2 = new BlockPos(pos);
         p2 = p2.func_177982_a(random.nextInt(16), 0, random.nextInt(16));

         for(p2 = world.func_175645_m(p2); p2.func_177956_o() > 50 && world.func_180495_p(p2).func_177230_c() != Blocks.field_150349_c; p2 = p2.func_177977_b()) {
         }

         l2 = world.func_180495_p(p2).func_177230_c();
         if (l2 == Blocks.field_150349_c && world.func_180495_p(p2.func_177984_a()).func_177230_c().func_176200_f(world, p2.func_177984_a()) && this.isBlockAdjacentToWood(world, p2.func_177984_a())) {
            world.func_180501_a(p2.func_177984_a(), BlocksTC.vishroom.func_176223_P(), 2);
         }
      }

   }

   private boolean isBlockAdjacentToWood(IBlockAccess world, BlockPos pos) {
      int count = false;

      for(int xx = -1; xx <= 1; ++xx) {
         for(int yy = -1; yy <= 1; ++yy) {
            for(int zz = -1; zz <= 1; ++zz) {
               if ((xx != 0 || yy != 0 || zz != 0) && Utils.isWoodLog(world, pos.func_177982_a(xx, yy, zz))) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public EnumFlowerType func_180623_a(Random rand, BlockPos pos) {
      double d0 = MathHelper.func_151237_a((1.0D + field_180281_af.func_151601_a((double)pos.func_177958_n() / 48.0D, (double)pos.func_177952_p() / 48.0D)) / 2.0D, 0.0D, 0.9999D);
      return EnumFlowerType.values()[(int)(d0 * (double)EnumFlowerType.values().length)];
   }

   static {
      blobs = new WorldGenBlockBlob(Blocks.field_150341_Y, 0);
   }
}
