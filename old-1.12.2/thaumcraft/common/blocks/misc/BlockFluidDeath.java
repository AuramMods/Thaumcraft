package thaumcraft.common.blocks.misc;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSlimyBubble;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

public class BlockFluidDeath extends BlockFluidClassic {
   public static final Material FLUID_DEATH_MATERIAL;

   public BlockFluidDeath() {
      super(ConfigBlocks.FluidDeath.instance, FLUID_DEATH_MATERIAL);
      this.func_149663_c("liquid_death");
      this.func_149647_a(ConfigItems.TABTC);
      this.setQuantaPerBlock(4);
   }

   public void func_180634_a(World world, BlockPos pos, IBlockState state, Entity entity) {
      if (!world.field_72995_K && entity instanceof EntityLivingBase) {
         entity.func_70097_a(DamageSourceThaumcraft.dissolve, (float)(4 - this.func_176201_c(state) + 1));
      }

   }

   public int getQuanta() {
      return this.quantaPerBlock;
   }

   @SideOnly(Side.CLIENT)
   public void func_180655_c(IBlockState state, World world, BlockPos pos, Random rand) {
      if (rand.nextInt(20) == 0) {
         int meta = this.func_176201_c(state);
         float h = rand.nextFloat() * 0.075F;
         FXSlimyBubble ef = new FXSlimyBubble(world, (double)((float)pos.func_177958_n() + rand.nextFloat()), (double)((float)pos.func_177956_o() + 0.1F + 0.225F * (float)(4 - meta)), (double)((float)pos.func_177952_p() + rand.nextFloat()), 0.075F + h);
         ef.func_82338_g(0.8F);
         ef.func_70538_b(0.3F - rand.nextFloat() * 0.1F, 0.0F, 0.4F + rand.nextFloat() * 0.1F);
         ParticleEngine.INSTANCE.addEffect(world, ef);
      }

      if (rand.nextInt(50) == 0) {
         double var21 = (double)((float)pos.func_177958_n() + rand.nextFloat());
         double var22 = (double)pos.func_177956_o() + (double)((float)this.getMaxRenderHeightMeta() / 4.0F);
         double var23 = (double)((float)pos.func_177952_p() + rand.nextFloat());
         world.func_184134_a(var21, var22, var23, SoundEvents.field_187662_cZ, SoundCategory.BLOCKS, 0.1F + rand.nextFloat() * 0.1F, 0.9F + rand.nextFloat() * 0.15F, false);
      }

   }

   static {
      FLUID_DEATH_MATERIAL = new MaterialLiquid(MapColor.field_151678_z);
   }
}
