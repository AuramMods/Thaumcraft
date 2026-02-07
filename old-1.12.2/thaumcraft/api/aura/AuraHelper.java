package thaumcraft.api.aura;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;

public class AuraHelper {
   public static float drainVis(World world, BlockPos pos, float amount, boolean simulate) {
      return ThaumcraftApi.internalMethods.drainVis(world, pos, amount, simulate);
   }

   public static float drainFlux(World world, BlockPos pos, float amount, boolean simulate) {
      return ThaumcraftApi.internalMethods.drainFlux(world, pos, amount, simulate);
   }

   public static void addVis(World world, BlockPos pos, float amount) {
      ThaumcraftApi.internalMethods.addVis(world, pos, amount);
   }

   public static float getVis(World world, BlockPos pos) {
      return ThaumcraftApi.internalMethods.getVis(world, pos);
   }

   public static void polluteAura(World world, BlockPos pos, float amount, boolean showEffect) {
      ThaumcraftApi.internalMethods.addFlux(world, pos, amount, showEffect);
   }

   public static float getFlux(World world, BlockPos pos) {
      return ThaumcraftApi.internalMethods.getFlux(world, pos);
   }

   public static int getAuraBase(World world, BlockPos pos) {
      return ThaumcraftApi.internalMethods.getAuraBase(world, pos);
   }

   public static boolean shouldPreserveAura(World world, EntityPlayer player, BlockPos pos) {
      return ThaumcraftApi.internalMethods.shouldPreserveAura(world, player, pos);
   }
}
