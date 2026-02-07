package thaumcraft.common.blocks.basic;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import thaumcraft.common.config.ConfigItems;

public class BlockStairsTC extends BlockStairs {
   public BlockStairsTC(IBlockState modelState) {
      super(modelState);
      this.func_149647_a(ConfigItems.TABTC);
      this.func_149713_g(0);
   }

   public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
      return this.func_149688_o(this.func_176223_P()) == Material.field_151575_d ? 20 : super.getFlammability(world, pos, face);
   }

   public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
      return this.func_149688_o(this.func_176223_P()) == Material.field_151575_d ? 5 : super.getFireSpreadSpeed(world, pos, face);
   }
}
