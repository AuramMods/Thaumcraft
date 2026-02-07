package thaumcraft.common.blocks.world.plants;

import java.util.Random;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.config.ConfigItems;

public class BlockPlantCinderpearl extends BlockBush {
   public BlockPlantCinderpearl() {
      super(Material.field_151585_k);
      this.func_149647_a(ConfigItems.TABTC);
      this.func_149672_a(SoundType.field_185850_c);
      this.func_149715_a(0.5F);
   }

   protected boolean func_185514_i(IBlockState state) {
      return state.func_177230_c() == Blocks.field_150354_m || state.func_177230_c() == Blocks.field_150346_d || state.func_177230_c() == Blocks.field_150406_ce || state.func_177230_c() == Blocks.field_150405_ch;
   }

   public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
      return EnumPlantType.Desert;
   }

   @SideOnly(Side.CLIENT)
   public void func_180655_c(IBlockState state, World world, BlockPos pos, Random rand) {
      if (rand.nextBoolean()) {
         float xr = (float)pos.func_177958_n() + 0.5F + (rand.nextFloat() - rand.nextFloat()) * 0.1F;
         float yr = (float)pos.func_177956_o() + 0.6F + (rand.nextFloat() - rand.nextFloat()) * 0.1F;
         float zr = (float)pos.func_177952_p() + 0.5F + (rand.nextFloat() - rand.nextFloat()) * 0.1F;
         world.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, (double)xr, (double)yr, (double)zr, 0.0D, 0.0D, 0.0D, new int[0]);
         world.func_175688_a(EnumParticleTypes.FLAME, (double)xr, (double)yr, (double)zr, 0.0D, 0.0D, 0.0D, new int[0]);
      }

   }
}
