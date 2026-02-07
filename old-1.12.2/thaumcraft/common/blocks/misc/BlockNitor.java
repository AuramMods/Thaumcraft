package thaumcraft.common.blocks.misc;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.tiles.misc.TileNitor;

public class BlockNitor extends BlockTC implements ITileEntityProvider {
   public BlockNitor() {
      super(Material.field_151594_q, EnumDyeColor.class);
      this.func_149711_c(0.1F);
      this.func_149672_a(SoundType.field_185854_g);
      this.func_149715_a(1.0F);
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileNitor();
   }

   public boolean hasTileEntity(IBlockState state) {
      return true;
   }

   public MapColor func_180659_g(IBlockState state) {
      return ((EnumDyeColor)state.func_177229_b(this.TYPE)).func_176768_e();
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return new AxisAlignedBB(0.33000001311302185D, 0.33000001311302185D, 0.33000001311302185D, 0.6600000262260437D, 0.6600000262260437D, 0.6600000262260437D);
   }

   public AxisAlignedBB func_180646_a(IBlockState state, World worldIn, BlockPos pos) {
      return null;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }
}
