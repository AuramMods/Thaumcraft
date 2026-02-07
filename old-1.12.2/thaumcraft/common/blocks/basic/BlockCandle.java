package thaumcraft.common.blocks.basic;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.crafting.IInfusionStabiliser;
import thaumcraft.common.blocks.BlockTC;

public class BlockCandle extends BlockTC implements IInfusionStabiliser {
   public BlockCandle() {
      super(Material.field_151594_q, EnumDyeColor.class);
      this.func_149711_c(0.1F);
      this.func_149672_a(SoundType.field_185854_g);
      this.func_149715_a(0.9375F);
   }

   public MapColor func_180659_g(IBlockState state) {
      return ((EnumDyeColor)state.func_177229_b(this.TYPE)).func_176768_e();
   }

   public boolean func_176196_c(World par1World, BlockPos pos) {
      return par1World.isSideSolid(pos, EnumFacing.UP);
   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
      if (!this.func_176196_c(worldIn, pos.func_177977_b())) {
         this.func_176226_b(worldIn, pos, state, 0);
         worldIn.func_175698_g(pos);
      }

   }

   public boolean func_176198_a(World par1World, BlockPos pos, EnumFacing par5) {
      return this.func_176196_c(par1World, pos.func_177977_b());
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.5D, 0.625D);
   }

   public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
      return false;
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

   public void func_180655_c(IBlockState state, World par1World, BlockPos pos, Random par5Random) {
      double var7 = (double)((float)pos.func_177958_n() + 0.5F);
      double var9 = (double)((float)pos.func_177956_o() + 0.7F);
      double var11 = (double)((float)pos.func_177952_p() + 0.5F);
      par1World.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, var7, var9, var11, 0.0D, 0.0D, 0.0D, new int[0]);
      par1World.func_175688_a(EnumParticleTypes.FLAME, var7, var9, var11, 0.0D, 0.0D, 0.0D, new int[0]);
   }

   public boolean canStabaliseInfusion(World world, BlockPos pos) {
      return true;
   }
}
