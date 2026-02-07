package thaumcraft.common.blocks.world.taint;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.blocks.IBlockFacing;
import thaumcraft.common.entities.monster.tainted.EntityTaintCrawler;
import thaumcraft.common.lib.utils.BlockStateUtils;

public class BlockTaintFeature extends BlockTC implements ITaintBlock {
   public BlockTaintFeature() {
      super(ThaumcraftMaterials.MATERIAL_TAINT);
      this.func_149711_c(0.1F);
      this.func_149715_a(0.625F);
      IBlockState bs = this.field_176227_L.func_177621_b();
      bs.func_177226_a(IBlockFacing.FACING, EnumFacing.UP);
      this.func_180632_j(bs);
      this.func_149675_a(true);
   }

   protected boolean func_149700_E() {
      return false;
   }

   public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
      if (!worldIn.field_72995_K) {
         if (worldIn.field_73012_v.nextFloat() < 0.333F) {
            Entity e = new EntityTaintCrawler(worldIn);
            e.func_70012_b((double)((float)pos.func_177958_n() + 0.5F), (double)((float)pos.func_177956_o() + 0.5F), (double)((float)pos.func_177952_p() + 0.5F), (float)worldIn.field_73012_v.nextInt(360), 0.0F);
            worldIn.func_72838_d(e);
         } else {
            AuraHelper.polluteAura(worldIn, pos, 1.0F, true);
         }
      }

      super.func_180663_b(worldIn, pos, state);
   }

   public void die(World world, BlockPos pos, IBlockState blockState) {
      world.func_175656_a(pos, BlocksTC.fluxGoo.func_176223_P());
   }

   public void func_180650_b(World world, BlockPos pos, IBlockState state, Random random) {
      if (!world.field_72995_K) {
         if (!TaintHelper.isNearTaintSeed(world, pos) && random.nextInt(10) == 0) {
            this.die(world, pos, state);
            return;
         }

         TaintHelper.spreadFibres(world, pos);
         if (world.func_180495_p(pos.func_177977_b()).func_177230_c() == BlocksTC.taintLog && world.func_180495_p(pos.func_177977_b()).func_177229_b(BlockTaintLog.AXIS) == Axis.Y && world.field_73012_v.nextInt(100) == 0) {
            world.func_175656_a(pos, BlocksTC.taintBlock.func_176203_a(2));
         }
      }

   }

   public int func_180651_a(IBlockState state) {
      return 0;
   }

   public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
      return true;
   }

   public int func_185484_c(IBlockState state, IBlockAccess source, BlockPos pos) {
      return 200;
   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
      if (!worldIn.field_72995_K && !worldIn.func_180495_p(pos.func_177972_a(BlockStateUtils.getFacing(state).func_176734_d())).func_177230_c().func_176212_b(worldIn, pos.func_177972_a(BlockStateUtils.getFacing(state).func_176734_d()), BlockStateUtils.getFacing(state))) {
         worldIn.func_175698_g(pos);
      }

   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState bs = this.func_176223_P();
      bs = bs.func_177226_a(IBlockFacing.FACING, facing);
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
      ArrayList<IProperty> ip = new ArrayList();
      ip.add(IBlockFacing.FACING);
      return new BlockStateContainer(this, (IProperty[])ip.toArray(new IProperty[ip.size()]));
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      EnumFacing facing = BlockStateUtils.getFacing(this.func_176201_c(state));
      switch(facing.ordinal()) {
      case 0:
         return new AxisAlignedBB(0.125D, 0.625D, 0.125D, 0.875D, 1.0D, 0.875D);
      case 1:
         return new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.375D, 0.875D);
      case 2:
         return new AxisAlignedBB(0.125D, 0.125D, 0.625D, 0.875D, 0.875D, 1.0D);
      case 3:
         return new AxisAlignedBB(0.125D, 0.125D, 0.0D, 0.875D, 0.875D, 0.375D);
      case 4:
         return new AxisAlignedBB(0.625D, 0.125D, 0.125D, 1.0D, 0.875D, 0.875D);
      case 5:
         return new AxisAlignedBB(0.0D, 0.125D, 0.125D, 0.375D, 0.875D, 0.875D);
      default:
         return super.func_185496_a(state, source, pos);
      }
   }
}
