package thaumcraft.common.world;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.common.IWorldGenerator;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.world.ore.ShardType;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.cult.EntityCultistPortalLesser;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.world.aura.AuraHandler;
import thaumcraft.common.world.aura.EntityAuraNode;
import thaumcraft.common.world.biomes.BiomeHandler;
import thaumcraft.common.world.objects.WorldGenCustomFlowers;
import thaumcraft.common.world.objects.WorldGenGreatwoodTrees;
import thaumcraft.common.world.objects.WorldGenMound;
import thaumcraft.common.world.objects.WorldGenSilverwoodTrees;

public class ThaumcraftWorldGenerator implements IWorldGenerator {
   public static ThaumcraftWorldGenerator INSTANCE = new ThaumcraftWorldGenerator();
   private final Predicate<IBlockState> predicate;

   public ThaumcraftWorldGenerator() {
      this.predicate = BlockMatcher.func_177642_a(Blocks.field_150348_b);
   }

   public void initialize() {
      if (Config.genMagicForest) {
         BiomeManager.addBiome(BiomeType.WARM, new BiomeEntry(BiomeHandler.MAGICAL_FOREST, Config.biomeMagicalForestWeight));
         BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(BiomeHandler.MAGICAL_FOREST, Config.biomeMagicalForestWeight));
      }

   }

   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
      this.worldGeneration(random, chunkX, chunkZ, world, true);
      AuraHandler.generateAura(chunkProvider.func_186025_d(chunkX, chunkZ), random);
   }

   public void worldGeneration(Random random, int chunkX, int chunkZ, World world, boolean newGen) {
      if (world.field_73011_w.getDimension() == Config.dimensionOuterId) {
         world.func_72964_e(chunkX, chunkZ).func_76630_e();
      } else {
         this.generateAll(world, random, chunkX, chunkZ, newGen);
         if (world.field_73011_w.getDimension() == -1) {
            this.generateNether(world, random, chunkX, chunkZ, newGen);
         } else if (world.field_73011_w.getDimension() == Config.overworldDim) {
            this.generateSurface(world, random, chunkX, chunkZ, newGen);
         }

         if (!newGen) {
            world.func_72964_e(chunkX, chunkZ).func_76630_e();
         }
      }

   }

   private void generateSurface(World world, Random random, int chunkX, int chunkZ, boolean newGen) {
      boolean auraGen = false;
      int blacklist = BiomeHandler.getDimBlacklist(world.field_73011_w.getDimension());
      if (blacklist == -1 && Config.genStructure && world.field_73011_w.getDimension() == Config.overworldDim && !world.func_72912_H().func_76067_t().func_77127_a().startsWith("flat") && (newGen || Config.regenStructure)) {
         int randPosX = chunkX * 16 + random.nextInt(16);
         int randPosZ = chunkZ * 16 + random.nextInt(16);
         BlockPos p = world.func_175725_q(new BlockPos(randPosX, 0, randPosZ)).func_177979_c(9);
         if (p.func_177956_o() < world.func_72940_L()) {
            if (random.nextInt(150) == 0) {
               WorldGenerator mound = new WorldGenMound();
               if (mound.func_180709_b(world, random, p)) {
                  auraGen = true;
                  spawnNode(world, p.func_177982_a(9, 8, 9), 1, 1.5F, false);
               }
            } else if (random.nextInt(66) != 0 && random.nextInt(500) == 0) {
               BlockPos p2 = p.func_177981_b(8);
               IBlockState bs = world.func_180495_p(p2);
               if (bs.func_185904_a() == Material.field_151578_c || bs.func_185904_a() == Material.field_151576_e || bs.func_185904_a() == Material.field_151595_p || bs.func_185904_a() == Material.field_151597_y) {
                  EntityCultistPortalLesser eg = new EntityCultistPortalLesser(world);
                  eg.func_70107_b((double)p2.func_177958_n() + 0.5D, (double)(p2.func_177956_o() + 1), (double)p2.func_177952_p() + 0.5D);
                  eg.func_180482_a(world.func_175649_E(new BlockPos(eg)), (IEntityLivingData)null);
                  world.func_72838_d(eg);
               }
            }
         }
      }

   }

   private void generateNodes(World world, Random random, int chunkX, int chunkZ, boolean newGen, int blacklist) {
      if (blacklist != 0 && blacklist != 2 && Config.genAura && (newGen || Config.regenAura)) {
         BlockPos var7 = null;

         try {
            var7 = (new MapGenScatteredFeature()).func_180706_b(world, world.func_175645_m(new BlockPos(chunkX * 16 + 8, 64, chunkZ * 16 + 8)));
         } catch (Exception var9) {
         }

         if (var7 != null) {
            BlockPos bp = var7.func_177981_b(3);
            spawnNode(world, bp, -1, 1.0F, true);
         }
      }

   }

   public static void spawnNode(World world, BlockPos bp, int type, float sizemod, boolean checkProximity) {
      if (checkProximity) {
         List<EntityAuraNode> neighbours = EntityUtils.getEntitiesInRange(world, bp, (Entity)null, EntityAuraNode.class, 64.0D);
         if (neighbours.size() > 0) {
            return;
         }
      }

      EntityAuraNode e = new EntityAuraNode(world);
      e.func_70012_b((double)bp.func_177958_n() + 0.5D, (double)bp.func_177956_o() + 0.5D, (double)bp.func_177952_p() + 0.5D, 0.0F, 0.0F);
      world.func_72838_d(e);
      e.randomizeNode();
      if (type >= 0) {
         e.setNodeType(type);
      }

      e.setNodeSize((int)((float)e.getNodeSize() * sizemod));
   }

   private void generateVegetation(World world, Random random, int chunkX, int chunkZ, boolean newGen) {
      Biome bgb = world.func_180494_b(new BlockPos(chunkX * 16 + 8, 50, chunkZ * 16 + 8));
      if (BiomeHandler.getBiomeBlacklist(Biome.func_185362_a(bgb)) == -1) {
         if (random.nextInt(60) == 3) {
            generateSilverwood(world, random, chunkX, chunkZ);
         }

         if (random.nextInt(25) == 7) {
            generateGreatwood(world, random, chunkX, chunkZ);
         }

         int randPosX = chunkX * 16 + random.nextInt(16);
         int randPosZ = chunkZ * 16 + random.nextInt(16);
         BlockPos bp = world.func_175645_m(new BlockPos(randPosX, 0, randPosZ));
         if (world.func_180494_b(bp).field_76752_A.func_177230_c() == Blocks.field_150354_m && world.func_180494_b(bp).func_185353_n() > 1.0F && random.nextInt(30) == 0) {
            generateFlowers(world, random, bp, BlocksTC.cinderpearl, 0);
         }

      }
   }

   private void generateOres(World world, Random random, int chunkX, int chunkZ, boolean newGen) {
      Biome bgb = world.func_180494_b(new BlockPos(chunkX * 16 + 8, 50, chunkZ * 16 + 8));
      if (BiomeHandler.getBiomeBlacklist(Biome.func_185362_a(bgb)) != 0 && BiomeHandler.getBiomeBlacklist(Biome.func_185362_a(bgb)) != 2) {
         float density = (float)Config.oreDensity / 100.0F;
         if (world.field_73011_w.getDimension() != -1) {
            int i;
            int maxCrystals;
            int cc;
            int i;
            BlockPos pos;
            IBlockState block;
            if (Config.genCinnibar && (newGen || Config.regenCinnibar)) {
               for(i = 0; i < Math.round(18.0F * density); ++i) {
                  maxCrystals = chunkX * 16 + random.nextInt(16);
                  cc = random.nextInt(world.func_72800_K() / 5);
                  i = chunkZ * 16 + random.nextInt(16);
                  pos = new BlockPos(maxCrystals, cc, i);
                  block = world.func_180495_p(pos);
                  if (block.func_177230_c().isReplaceableOreGen(block, world, pos, this.predicate)) {
                     world.func_180501_a(pos, BlocksTC.oreCinnabar.func_176223_P(), 2);
                  }
               }
            }

            if (Config.genQuartz && (newGen || Config.regenQuartz)) {
               for(i = 0; i < Math.round(18.0F * density); ++i) {
                  maxCrystals = chunkX * 16 + random.nextInt(16);
                  cc = random.nextInt(world.func_72800_K() / 4);
                  i = chunkZ * 16 + random.nextInt(16);
                  pos = new BlockPos(maxCrystals, cc, i);
                  block = world.func_180495_p(pos);
                  if (block.func_177230_c().isReplaceableOreGen(block, world, pos, this.predicate)) {
                     world.func_180501_a(pos, BlocksTC.oreQuartz.func_176223_P(), 2);
                  }
               }
            }

            if (Config.genAmber && (newGen || Config.regenAmber)) {
               for(i = 0; i < Math.round(20.0F * density); ++i) {
                  maxCrystals = chunkX * 16 + random.nextInt(16);
                  cc = chunkZ * 16 + random.nextInt(16);
                  i = world.func_175645_m(new BlockPos(maxCrystals, 0, cc)).func_177956_o() - random.nextInt(25);
                  pos = new BlockPos(maxCrystals, i, cc);
                  block = world.func_180495_p(pos);
                  if (block.func_177230_c().isReplaceableOreGen(block, world, pos, this.predicate)) {
                     world.func_180501_a(pos, BlocksTC.oreAmber.func_176223_P(), 2);
                  }
               }
            }

            if (Config.genCrystals && (newGen || Config.regenCrystals)) {
               int t = 8;
               maxCrystals = Math.round(64.0F * density);
               cc = 0;
               if (world.field_73011_w.getDimension() == -1) {
                  t = 1;
               }

               for(i = 0; i < Math.round((float)t * density); ++i) {
                  int randPosX = chunkX * 16 + random.nextInt(16);
                  int randPosZ = chunkZ * 16 + random.nextInt(16);
                  int randPosY = random.nextInt(Math.max(5, world.func_175645_m(new BlockPos(randPosX, 0, randPosZ)).func_177956_o() - 5));
                  BlockPos bp = new BlockPos(randPosX, randPosY, randPosZ);
                  int md = random.nextInt(6);
                  if (random.nextInt(3) == 0) {
                     Aspect tag = BiomeHandler.getRandomBiomeTag(Biome.func_185362_a(world.func_180494_b(bp)), random);
                     if (tag == null) {
                        md = random.nextInt(6);
                     } else {
                        md = ShardType.getMetaByAspect(tag);
                     }
                  }

                  Block oreBlock = ShardType.byMetadata(md).getOre();

                  for(int xx = -1; xx <= 1; ++xx) {
                     for(int yy = -1; yy <= 1; ++yy) {
                        for(int zz = -1; zz <= 1; ++zz) {
                           if (random.nextInt(3) != 0) {
                              IBlockState bs = world.func_180495_p(bp.func_177982_a(xx, yy, zz));
                              Material bm = bs.func_185904_a();
                              if (!bm.func_76224_d() && (world.func_175623_d(bp.func_177982_a(xx, yy, zz)) || bs.func_177230_c().func_176200_f(world, bp.func_177982_a(xx, yy, zz))) && BlockUtils.isBlockTouching(world, bp.func_177982_a(xx, yy, zz), Material.field_151576_e, true)) {
                                 int amt = 1 + random.nextInt(3);
                                 world.func_180501_a(bp.func_177982_a(xx, yy, zz), oreBlock.func_176203_a(amt), 0);
                                 cc += amt;
                              }
                           }
                        }
                     }
                  }

                  if (cc > maxCrystals) {
                     break;
                  }
               }
            }

         }
      }
   }

   private void generateAll(World world, Random random, int chunkX, int chunkZ, boolean newGen) {
      boolean auraGen = false;
      int blacklist = BiomeHandler.getDimBlacklist(world.field_73011_w.getDimension());
      if (blacklist == -1 && Config.genTrees && !world.func_72912_H().func_76067_t().func_77127_a().startsWith("flat") && (newGen || Config.regenTrees)) {
         this.generateVegetation(world, random, chunkX, chunkZ, newGen);
      }

      if (blacklist != 0 && blacklist != 2) {
         this.generateOres(world, random, chunkX, chunkZ, newGen);
      }

      if (!auraGen) {
         this.generateNodes(world, random, chunkX, chunkZ, newGen, blacklist);
      }

   }

   private void generateNether(World world, Random random, int chunkX, int chunkZ, boolean newGen) {
      boolean auraGen = false;
   }

   public static boolean generateFlowers(World world, Random random, BlockPos pos, Block block, int md) {
      WorldGenerator flowers = new WorldGenCustomFlowers(block, md);
      return flowers.func_180709_b(world, random, pos);
   }

   public static boolean generateGreatwood(World world, Random random, int chunkX, int chunkZ) {
      int x = chunkX * 16 + random.nextInt(16);
      int z = chunkZ * 16 + random.nextInt(16);
      BlockPos bp = world.func_175725_q(new BlockPos(x, 0, z));
      int bio = Biome.func_185362_a(world.func_180494_b(bp));
      if (BiomeHandler.getBiomeSupportsGreatwood(bio) > random.nextFloat()) {
         boolean t = (new WorldGenGreatwoodTrees(false, random.nextInt(8) == 0)).func_180709_b(world, random, bp);
         return t;
      } else {
         return false;
      }
   }

   public static boolean generateSilverwood(World world, Random random, int chunkX, int chunkZ) {
      int x = chunkX * 16 + random.nextInt(16);
      int z = chunkZ * 16 + random.nextInt(16);
      BlockPos bp = world.func_175725_q(new BlockPos(x, 0, z));
      Biome bio = world.func_180494_b(bp);
      if (bio.equals(BiomeHandler.MAGICAL_FOREST) || !BiomeDictionary.isBiomeOfType(bio, Type.MAGICAL) && bio != Biome.func_150568_d(18) && bio != Biome.func_150568_d(28)) {
         return false;
      } else {
         boolean t = (new WorldGenSilverwoodTrees(false, 7, 4)).func_180709_b(world, random, bp);
         return t;
      }
   }
}
