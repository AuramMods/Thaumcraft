package thaumcraft.common.blocks.world.taint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.IBlockFacing;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.tainted.EntityTaintSeed;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.world.aura.AuraHandler;

public class TaintHelper {
   private static ConcurrentHashMap<Integer, ArrayList<BlockPos>> taintSeeds = new ConcurrentHashMap();

   public static void addTaintSeed(World world, BlockPos pos) {
      ArrayList<BlockPos> locs = (ArrayList)taintSeeds.get(world.field_73011_w.getDimension());
      if (locs == null) {
         locs = new ArrayList();
      }

      locs.add(pos);
      taintSeeds.put(world.field_73011_w.getDimension(), locs);
   }

   public static void removeTaintSeed(World world, BlockPos pos) {
      ArrayList<BlockPos> locs = (ArrayList)taintSeeds.get(world.field_73011_w.getDimension());
      if (locs != null && !locs.isEmpty()) {
         locs.remove(pos);
      }

   }

   public static boolean isNearTaintSeed(World world, BlockPos pos) {
      double area = (double)(Config.taintSpreadArea * Config.taintSpreadArea);
      ArrayList<BlockPos> locs = (ArrayList)taintSeeds.get(world.field_73011_w.getDimension());
      if (locs != null && !locs.isEmpty()) {
         Iterator var5 = locs.iterator();

         while(var5.hasNext()) {
            BlockPos p = (BlockPos)var5.next();
            if (p.func_177951_i(pos) <= area) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean isAtTaintSeedEdge(World world, BlockPos pos) {
      double area = (double)(Config.taintSpreadArea * Config.taintSpreadArea);
      double fringe = area * 0.9D;
      ArrayList<BlockPos> locs = (ArrayList)taintSeeds.get(world.field_73011_w.getDimension());
      if (locs != null && !locs.isEmpty()) {
         Iterator var7 = locs.iterator();

         while(var7.hasNext()) {
            BlockPos p = (BlockPos)var7.next();
            double d = p.func_177951_i(pos);
            if (d < area && d > fringe) {
               return true;
            }
         }
      }

      return false;
   }

   public static void spreadFibres(World world, BlockPos pos) {
      spreadFibres(world, pos, false);
   }

   public static void spreadFibres(World world, BlockPos pos, boolean ignore) {
      if (ignore || !Config.wuss) {
         float mod = 0.5F + AuraHandler.getFluxSaturation(world, pos);
         if (ignore || !(world.field_73012_v.nextFloat() > Config.taintSpreadRate * mod)) {
            if (isNearTaintSeed(world, pos)) {
               int xx = pos.func_177958_n() + world.field_73012_v.nextInt(3) - 1;
               int yy = pos.func_177956_o() + world.field_73012_v.nextInt(3) - 1;
               int zz = pos.func_177952_p() + world.field_73012_v.nextInt(3) - 1;
               BlockPos t = new BlockPos(xx, yy, zz);
               if (t.equals(pos)) {
                  return;
               }

               IBlockState bs = world.func_180495_p(t);
               Material bm = bs.func_177230_c().func_149688_o(bs);
               float bh = bs.func_177230_c().func_176195_g(bs, world, t);
               if (bh < 0.0F || bh > 10.0F) {
                  return;
               }

               if (!bs.func_177230_c().isLeaves(bs, world, t) && !bm.func_76224_d() && (world.func_175623_d(t) || bs.func_177230_c().func_176200_f(world, t) || bs.func_177230_c() instanceof BlockFlower || bs.func_177230_c() instanceof IPlantable) && BlockUtils.isAdjacentToSolidBlock(world, t) && !BlockTaintFibre.isOnlyAdjacentToTaint(world, t)) {
                  world.func_175656_a(t, BlocksTC.taintFibre.func_176223_P());
                  world.func_175641_c(t, BlocksTC.taintFibre, 1, 0);
                  return;
               }

               EntityTaintSeed e;
               if (bs.func_177230_c().isLeaves(bs, world, t)) {
                  e = null;
                  EnumFacing face;
                  if ((double)world.field_73012_v.nextFloat() < 0.6D && (face = BlockUtils.getFaceBlockTouching(world, t, BlocksTC.taintLog)) != null) {
                     world.func_175656_a(t, BlocksTC.taintFeature.func_176223_P().func_177226_a(IBlockFacing.FACING, face.func_176734_d()));
                  } else {
                     world.func_175656_a(t, BlocksTC.taintFibre.func_176223_P());
                     world.func_175641_c(t, BlocksTC.taintFibre, 1, 0);
                  }

                  return;
               }

               if (BlockTaintFibre.isHemmedByTaint(world, t)) {
                  if (Utils.isWoodLog(world, t) && bs.func_185904_a() != ThaumcraftMaterials.MATERIAL_TAINT) {
                     world.func_175656_a(t, BlocksTC.taintLog.func_176223_P().func_177226_a(BlockTaintLog.VARIANT, BlockTaintLog.LogType.values()[0]).func_177226_a(BlockTaintLog.AXIS, BlockUtils.getBlockAxis(world, t)));
                     return;
                  }

                  if (bs.func_177230_c() == Blocks.field_150419_aX || bs.func_177230_c() == Blocks.field_150420_aW || bm == Material.field_151572_C || bm == Material.field_151570_A || bm == Material.field_151589_v || bm == Material.field_151583_m || bm == Material.field_151575_d) {
                     world.func_175656_a(t, BlocksTC.taintBlock.func_176203_a(0));
                     world.func_175641_c(t, BlocksTC.taintBlock, 1, 0);
                     return;
                  }

                  if (bm == Material.field_151595_p || bm == Material.field_151578_c || bm == Material.field_151577_b || bm == Material.field_151571_B) {
                     world.func_175656_a(t, BlocksTC.taintBlock.func_176203_a(1));
                     world.func_175641_c(t, BlocksTC.taintBlock, 1, 0);
                     return;
                  }

                  if (bm == Material.field_151576_e) {
                     world.func_175656_a(t, BlocksTC.taintBlock.func_176203_a(3));
                     world.func_175641_c(t, BlocksTC.taintBlock, 1, 0);
                     return;
                  }
               }

               if (bs.func_177230_c() == BlocksTC.taintBlock && world.func_175623_d(t.func_177984_a()) && (double)world.field_73012_v.nextFloat() < (double)Config.taintSpreadRate * 0.33D && isAtTaintSeedEdge(world, t)) {
                  e = new EntityTaintSeed(world);
                  e.func_70012_b((double)((float)t.func_177958_n() + 0.5F), (double)t.func_177984_a().func_177956_o(), (double)((float)t.func_177952_p() + 0.5F), (float)world.field_73012_v.nextInt(360), 0.0F);
                  if (e.func_70601_bi()) {
                     world.func_72838_d(e);
                     return;
                  }
               }
            }

         }
      }
   }
}
