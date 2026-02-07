package thaumcraft.common.blocks.devices;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.blocks.IBlockEnabled;
import thaumcraft.common.blocks.IBlockFacing;
import thaumcraft.common.tiles.devices.TileBellows;

public class BlockBellows extends BlockTCDevice implements IBlockFacing, IBlockEnabled {
   public BlockBellows() {
      super(Material.field_151575_d, TileBellows.class);
      this.func_149672_a(SoundType.field_185848_a);
      this.func_149711_c(1.0F);
   }

   public int func_180651_a(IBlockState state) {
      return 0;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState bs = this.func_176223_P();
      if (this instanceof IBlockFacing) {
         bs = bs.func_177226_a(IBlockFacing.FACING, facing.func_176734_d());
      }

      if (this instanceof IBlockEnabled) {
         bs = bs.func_177226_a(IBlockEnabled.ENABLED, true);
      }

      return bs;
   }
}
