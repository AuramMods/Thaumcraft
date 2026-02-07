package thaumcraft.common.blocks.world.plants;

import java.util.Random;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.config.ConfigItems;

public class BlockPlantShimmerleaf extends BlockBush {
   public BlockPlantShimmerleaf() {
      super(Material.field_151585_k);
      this.func_149647_a(ConfigItems.TABTC);
      this.func_149672_a(SoundType.field_185850_c);
      this.func_149715_a(0.4F);
   }

   protected boolean func_185514_i(IBlockState state) {
      return state.func_177230_c() == Blocks.field_150349_c || state.func_177230_c() == Blocks.field_150346_d;
   }

   public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
      return EnumPlantType.Plains;
   }

   @SideOnly(Side.CLIENT)
   public void func_180655_c(IBlockState state, World world, BlockPos pos, Random rand) {
      if (rand.nextInt(3) == 0) {
         float xr = (float)((double)((float)pos.func_177958_n() + 0.5F) + rand.nextGaussian() * 0.1D);
         float yr = (float)((double)((float)pos.func_177956_o() + 0.4F) + rand.nextGaussian() * 0.1D);
         float zr = (float)((double)((float)pos.func_177952_p() + 0.5F) + rand.nextGaussian() * 0.1D);
         FXDispatcher.INSTANCE.drawWispyMotes((double)xr, (double)yr, (double)zr, rand.nextGaussian() * 0.01D, rand.nextGaussian() * 0.01D, rand.nextGaussian() * 0.01D, 10, 0.3F + world.field_73012_v.nextFloat() * 0.3F, 0.7F + world.field_73012_v.nextFloat() * 0.3F, 0.7F + world.field_73012_v.nextFloat() * 0.3F, 0.0F);
      }

   }
}
