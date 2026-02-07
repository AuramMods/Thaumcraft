package thaumcraft.common.blocks.devices;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.blocks.IBlockEnabled;
import thaumcraft.common.blocks.IBlockFacing;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.devices.TileLampArcane;

public class BlockLamp extends BlockTCDevice implements IBlockFacing, IBlockEnabled {
   public BlockLamp(Class tc) {
      super(Material.field_151573_f, tc);
      this.func_149672_a(SoundType.field_185852_e);
      this.func_149711_c(1.0F);
      IBlockState bs = this.field_176227_L.func_177621_b();
      bs.func_177226_a(IBlockFacing.FACING, EnumFacing.DOWN);
      bs.func_177226_a(IBlockEnabled.ENABLED, true);
      this.func_180632_j(bs);
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public int func_180651_a(IBlockState state) {
      return 0;
   }

   public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
      return BlockStateUtils.isEnabled(world.func_180495_p(pos).func_177230_c().func_176201_c(world.func_180495_p(pos))) ? 15 : super.getLightValue(state, world, pos);
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState bs = this.func_176223_P();
      bs = bs.func_177226_a(IBlockFacing.FACING, facing.func_176734_d());
      bs = bs.func_177226_a(IBlockEnabled.ENABLED, false);
      return bs;
   }

   public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
      TileEntity te = worldIn.func_175625_s(pos);
      if (te != null && te instanceof TileLampArcane) {
         ((TileLampArcane)te).removeLights();
      }

      super.func_180663_b(worldIn, pos, state);
   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
      if (worldIn.func_175623_d(pos.func_177972_a(BlockStateUtils.getFacing(state)))) {
         this.func_176226_b(worldIn, pos, this.func_176223_P(), 0);
         worldIn.func_175698_g(pos);
      } else {
         TileEntity te = worldIn.func_175625_s(pos);
         if (te != null && te instanceof TileLampArcane && BlockStateUtils.isEnabled(state) && worldIn.func_175640_z(pos)) {
            ((TileLampArcane)te).removeLights();
         }

         super.func_189540_a(state, worldIn, pos, blockIn);
      }
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return new AxisAlignedBB(0.25D, 0.125D, 0.25D, 0.75D, 0.875D, 0.75D);
   }
}
