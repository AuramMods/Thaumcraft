package thaumcraft.common.blocks.misc;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.potions.PotionWarpWard;

public class BlockFluidPure extends BlockFluidClassic {
   public static final Material FLUID_PURE_MATERIAL;

   public BlockFluidPure() {
      super(ConfigBlocks.FluidPure.instance, FLUID_PURE_MATERIAL);
      this.func_149663_c("purifying_fluid");
      this.func_149647_a(ConfigItems.TABTC);
   }

   public void func_180634_a(World world, BlockPos pos, IBlockState state, Entity entity) {
      if (!world.field_72995_K && this.isSourceBlock(world, pos) && entity instanceof EntityPlayer && !((EntityPlayer)entity).func_70644_a(PotionWarpWard.instance)) {
         int warp = ThaumcraftCapabilities.getWarp((EntityPlayer)entity).get(IPlayerWarp.EnumWarpType.PERMANENT);
         int div = 1;
         if (warp > 0) {
            div = (int)Math.sqrt((double)warp);
            if (div < 1) {
               div = 1;
            }
         }

         ((EntityPlayer)entity).func_70690_d(new PotionEffect(PotionWarpWard.instance, Math.min(32000, 200000 / div), 0, true, true));
         world.func_175698_g(pos);
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_180655_c(IBlockState state, World world, BlockPos pos, Random rand) {
      int meta = this.func_176201_c(state);
      if (rand.nextInt(10) == 0) {
         FXGeneric fb = new FXGeneric(world, (double)((float)pos.func_177958_n() + rand.nextFloat()), (double)((float)pos.func_177956_o() + 0.125F * (float)(8 - meta)), (double)((float)pos.func_177952_p() + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
         fb.func_187114_a(10 + world.field_73012_v.nextInt(10));
         fb.setScale(world.field_73012_v.nextFloat() * 0.3F + 0.3F);
         fb.func_70538_b(1.0F, 1.0F, 1.0F);
         fb.setRandomMovementScale(0.001F, 0.001F, 0.001F);
         fb.setGravity(-0.01F);
         fb.func_82338_g(0.25F);
         fb.setParticle(64);
         fb.setFinalFrames(65, 66);
         ParticleEngine.INSTANCE.addEffect(world, fb);
      }

      if (rand.nextInt(50) == 0) {
         double var21 = (double)((float)pos.func_177958_n() + rand.nextFloat());
         double var22 = (double)pos.func_177956_o() + 0.5D;
         double var23 = (double)((float)pos.func_177952_p() + rand.nextFloat());
         world.func_184134_a(var21, var22, var23, SoundEvents.field_187662_cZ, SoundCategory.BLOCKS, 0.1F + rand.nextFloat() * 0.1F, 0.9F + rand.nextFloat() * 0.15F, false);
      }

   }

   static {
      FLUID_PURE_MATERIAL = new MaterialLiquid(MapColor.field_151680_x);
   }
}
