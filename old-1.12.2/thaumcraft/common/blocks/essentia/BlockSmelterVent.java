package thaumcraft.common.blocks.essentia;

import java.util.ArrayList;
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
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.blocks.IBlockFacingHorizontal;
import thaumcraft.common.lib.utils.BlockStateUtils;

public class BlockSmelterVent extends BlockTC implements IBlockFacingHorizontal {
   public BlockSmelterVent() {
      super(Material.field_151573_f);
      this.func_149672_a(SoundType.field_185852_e);
      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(IBlockFacingHorizontal.FACING, EnumFacing.NORTH));
      this.func_149711_c(1.0F);
      this.func_149752_b(10.0F);
   }

   public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
      return true;
   }

   public int func_180651_a(IBlockState state) {
      return 0;
   }

   public void func_176213_c(World worldIn, BlockPos pos, IBlockState state) {
   }

   public boolean func_176198_a(World worldIn, BlockPos pos, EnumFacing side) {
      return super.func_176198_a(worldIn, pos, side) && side.func_176740_k().func_176722_c() && worldIn.func_180495_p(pos.func_177972_a(side.func_176734_d())).func_177230_c() instanceof BlockSmelter && BlockStateUtils.getFacing(worldIn.func_180495_p(pos.func_177972_a(side.func_176734_d()))) != side;
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState bs = this.func_176223_P();
      if (!facing.func_176740_k().func_176722_c()) {
         facing = EnumFacing.NORTH;
      }

      bs = bs.func_177226_a(IBlockFacingHorizontal.FACING, facing.func_176734_d());
      return bs;
   }

   public IBlockState func_176203_a(int meta) {
      IBlockState bs = this.func_176223_P();
      bs = bs.func_177226_a(IBlockFacingHorizontal.FACING, BlockStateUtils.getFacing(meta));
      return bs;
   }

   public int func_176201_c(IBlockState state) {
      return 0 | ((EnumFacing)state.func_177229_b(IBlockFacingHorizontal.FACING)).func_176745_a();
   }

   protected BlockStateContainer func_180661_e() {
      ArrayList<IProperty> ip = new ArrayList();
      ip.add(IBlockFacingHorizontal.FACING);
      return ip.size() == 0 ? super.func_180661_e() : new BlockStateContainer(this, (IProperty[])ip.toArray(new IProperty[ip.size()]));
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      EnumFacing facing = BlockStateUtils.getFacing(state);
      switch(facing.ordinal()) {
      case 3:
         return new AxisAlignedBB(0.125D, 0.125D, 0.5D, 0.875D, 0.875D, 1.0D);
      case 4:
         return new AxisAlignedBB(0.0D, 0.125D, 0.125D, 0.5D, 0.875D, 0.875D);
      case 5:
         return new AxisAlignedBB(0.5D, 0.125D, 0.125D, 1.0D, 0.875D, 0.875D);
      default:
         return new AxisAlignedBB(0.125D, 0.125D, 0.0D, 0.875D, 0.875D, 0.5D);
      }
   }
}
