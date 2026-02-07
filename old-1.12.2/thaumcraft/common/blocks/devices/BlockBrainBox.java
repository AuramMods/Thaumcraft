package thaumcraft.common.blocks.devices;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.blocks.IBlockEnabled;
import thaumcraft.common.blocks.IBlockFacing;
import thaumcraft.common.blocks.IBlockFacingHorizontal;
import thaumcraft.common.lib.utils.BlockStateUtils;

public class BlockBrainBox extends BlockTC implements IBlockFacingHorizontal, IBlockEnabled {
   public BlockBrainBox() {
      super(Material.field_151573_f);
      this.func_149672_a(SoundType.field_185852_e);
      IBlockState bs = this.field_176227_L.func_177621_b();
      bs.func_177226_a(IBlockFacing.FACING, EnumFacing.UP);
      this.func_180632_j(bs);
      this.func_149711_c(1.0F);
      this.func_149752_b(10.0F);
   }

   public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
      return true;
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

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
      if (worldIn.func_180495_p(pos.func_177972_a(BlockStateUtils.getFacing(state))).func_177230_c() != BlocksTC.thaumatorium) {
         this.func_176226_b(worldIn, pos, BlocksTC.brainBox.func_176223_P(), 0);
         worldIn.func_175698_g(pos);
      }

   }

   public boolean func_176198_a(World worldIn, BlockPos pos, EnumFacing side) {
      if (worldIn.func_180495_p(pos.func_177972_a(side.func_176734_d())).func_177230_c() != BlocksTC.thaumatorium) {
         return false;
      } else {
         return worldIn.func_180495_p(pos.func_177972_a(side.func_176734_d())).func_177229_b(FACING) == side ? false : super.func_176198_a(worldIn, pos, side);
      }
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState bs = this.func_176223_P();
      bs = bs.func_177226_a(IBlockFacing.FACING, facing.func_176734_d());
      return bs;
   }

   public IBlockState func_176203_a(int meta) {
      IBlockState bs = this.func_176223_P();
      bs = bs.func_177226_a(IBlockFacing.FACING, BlockStateUtils.getFacing(meta));
      return bs;
   }

   public int func_176201_c(IBlockState state) {
      byte b0 = 0;
      int i = b0 | ((EnumFacing)state.func_177229_b(IBlockFacing.FACING)).func_176745_a();
      return i;
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{IBlockFacing.FACING});
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return new AxisAlignedBB(0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.8125D, 0.8125D);
   }
}
