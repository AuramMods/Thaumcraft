package thaumcraft.common.world.aura;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import thaumcraft.Thaumcraft;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.common.lib.utils.PosXY;
import thaumcraft.common.world.biomes.BiomeHandler;

public class AuraHandler {
   public static final int AURA_CEILING = 500;
   static ConcurrentHashMap<Integer, AuraWorld> auras = new ConcurrentHashMap();
   public static ConcurrentHashMap<Integer, CopyOnWriteArrayList<ChunkPos>> dirtyChunks = new ConcurrentHashMap();
   public static ConcurrentHashMap<Integer, BlockPos> taintTrigger = new ConcurrentHashMap();

   public static AuraWorld getAuraWorld(int dim) {
      return (AuraWorld)auras.get(dim);
   }

   public static AuraChunk getAuraChunk(int dim, int x, int y) {
      return auras.containsKey(dim) ? ((AuraWorld)auras.get(dim)).getAuraChunkAt(x, y) : null;
   }

   public static void addAuraWorld(int dim) {
      if (!auras.containsKey(dim)) {
         auras.put(dim, new AuraWorld(dim));
         Thaumcraft.log.info("Creating aura cache for world " + dim);
      }

   }

   public static void removeAuraWorld(int dim) {
      auras.remove(dim);
      Thaumcraft.log.info("Removing aura cache for world " + dim);
   }

   public static void addAuraChunk(int dim, Chunk chunk, short base, float vis, float flux) {
      AuraWorld aw = (AuraWorld)auras.get(dim);
      if (aw == null) {
         aw = new AuraWorld(dim);
      }

      aw.getAuraChunks().put(new PosXY(chunk.field_76635_g, chunk.field_76647_h), new AuraChunk(chunk, base, vis, flux));
      auras.put(dim, aw);
   }

   public static void removeAuraChunk(int dim, int x, int y) {
      AuraWorld aw = (AuraWorld)auras.get(dim);
      if (aw != null) {
         aw.getAuraChunks().remove(new PosXY(x, y));
      }

   }

   public static float getTotalAura(World world, BlockPos pos) {
      AuraChunk ac = getAuraChunk(world.field_73011_w.getDimension(), pos.func_177958_n() >> 4, pos.func_177952_p() >> 4);
      return ac != null ? ac.getVis() + ac.getFlux() : 0.0F;
   }

   public static float getFluxSaturation(World world, BlockPos pos) {
      AuraChunk ac = getAuraChunk(world.field_73011_w.getDimension(), pos.func_177958_n() >> 4, pos.func_177952_p() >> 4);
      return ac != null ? ac.getFlux() / (float)ac.getBase() : 0.0F;
   }

   public static float getVis(World world, BlockPos pos) {
      AuraChunk ac = getAuraChunk(world.field_73011_w.getDimension(), pos.func_177958_n() >> 4, pos.func_177952_p() >> 4);
      return ac != null ? ac.getVis() : 0.0F;
   }

   public static float getFlux(World world, BlockPos pos) {
      AuraChunk ac = getAuraChunk(world.field_73011_w.getDimension(), pos.func_177958_n() >> 4, pos.func_177952_p() >> 4);
      return ac != null ? ac.getFlux() : 0.0F;
   }

   public static int getAuraBase(World world, BlockPos pos) {
      AuraChunk ac = getAuraChunk(world.field_73011_w.getDimension(), pos.func_177958_n() >> 4, pos.func_177952_p() >> 4);
      return ac != null ? ac.getBase() : 0;
   }

   public static boolean shouldPreserveAura(World world, EntityPlayer player, BlockPos pos) {
      return (player == null || ThaumcraftCapabilities.getKnowledge(player).isResearchComplete("AURAPRESERVE")) && (double)(getVis(world, pos) / (float)getAuraBase(world, pos)) < 0.1D;
   }

   public static void addVis(World world, BlockPos pos, float amount) {
      if (!(amount < 0.0F)) {
         try {
            AuraChunk ac = getAuraChunk(world.field_73011_w.getDimension(), pos.func_177958_n() >> 4, pos.func_177952_p() >> 4);
            modifyVisInChunk(ac, amount, true);
         } catch (Exception var4) {
         }

      }
   }

   public static void addFlux(World world, BlockPos pos, float amount) {
      if (!(amount < 0.0F)) {
         try {
            AuraChunk ac = getAuraChunk(world.field_73011_w.getDimension(), pos.func_177958_n() >> 4, pos.func_177952_p() >> 4);
            modifyFluxInChunk(ac, amount, true);
         } catch (Exception var4) {
         }

      }
   }

   public static float drainVis(World world, BlockPos pos, float amount, boolean simulate) {
      boolean didit = false;

      try {
         AuraChunk ac = getAuraChunk(world.field_73011_w.getDimension(), pos.func_177958_n() >> 4, pos.func_177952_p() >> 4);
         if (amount > ac.getVis()) {
            amount = ac.getVis();
         }

         didit = modifyVisInChunk(ac, -amount, !simulate);
      } catch (Exception var6) {
      }

      return didit ? amount : 0.0F;
   }

   public static float drainFlux(World world, BlockPos pos, float amount, boolean simulate) {
      boolean didit = false;

      try {
         AuraChunk ac = getAuraChunk(world.field_73011_w.getDimension(), pos.func_177958_n() >> 4, pos.func_177952_p() >> 4);
         if (amount > ac.getFlux()) {
            amount = ac.getFlux();
         }

         didit = modifyFluxInChunk(ac, -amount, !simulate);
      } catch (Exception var6) {
      }

      return didit ? amount : 0.0F;
   }

   public static boolean modifyVisInChunk(AuraChunk ac, float amount, boolean doit) {
      if (ac != null) {
         if (doit) {
            ac.setVis(Math.max(0.0F, ac.getVis() + amount));
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean modifyFluxInChunk(AuraChunk ac, float amount, boolean doit) {
      if (ac != null) {
         if (doit) {
            ac.setFlux(Math.max(0.0F, ac.getFlux() + amount));
         }

         return true;
      } else {
         return false;
      }
   }

   public static void generateAura(Chunk chunk, Random rand) {
      Biome bgb = chunk.func_177412_p().func_180494_b(new BlockPos(chunk.field_76635_g * 16 + 8, 50, chunk.field_76647_h * 16 + 8));
      if (BiomeHandler.getBiomeBlacklist(Biome.func_185362_a(bgb)) == -1) {
         float life = BiomeHandler.getBiomeAuraModifier(bgb);

         for(int a = 0; a < 4; ++a) {
            EnumFacing dir = EnumFacing.func_176731_b(a);
            Biome bgb2 = chunk.func_177412_p().func_180494_b(new BlockPos((chunk.field_76635_g + dir.func_82601_c()) * 16 + 8, 50, (chunk.field_76647_h + dir.func_82599_e()) * 16 + 8));
            life += BiomeHandler.getBiomeAuraModifier(bgb2);
         }

         life /= 5.0F;
         float noise = (float)(1.0D + rand.nextGaussian() * 0.10000000149011612D);
         short base = (short)((int)(life * 500.0F * noise));
         base = (short)MathHelper.func_76125_a(base, 0, 500);
         addAuraChunk(chunk.func_177412_p().field_73011_w.getDimension(), chunk, base, (float)base, 0.0F);
      }
   }
}
