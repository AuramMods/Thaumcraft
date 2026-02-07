package thaumcraft.common.blocks.misc;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.tiles.misc.TileHole;

public class BlockHole extends BlockContainer {
   public BlockHole() {
      super(Material.field_151576_e);
      this.func_149722_s();
      this.func_149752_b(6000000.0F);
      this.func_149672_a(SoundType.field_185854_g);
      this.func_149715_a(0.7F);
      this.func_149675_a(true);
      this.func_149647_a((CreativeTabs)null);
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return null;
   }

   @SideOnly(Side.CLIENT)
   public void func_149666_a(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
   }

   public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing o) {
      return true;
   }

   public AxisAlignedBB func_180646_a(IBlockState state, World worldIn, BlockPos pos) {
      return null;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return field_185505_j;
   }

   public AxisAlignedBB func_180640_a(IBlockState blockState, World worldIn, BlockPos pos) {
      return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
   }

   public boolean func_149686_d(IBlockState blockState) {
      return false;
   }

   public boolean func_149662_c(IBlockState blockState) {
      return false;
   }

   public TileEntity func_149915_a(World var1, int var2) {
      return new TileHole();
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return Item.func_150899_d(0);
   }
}
