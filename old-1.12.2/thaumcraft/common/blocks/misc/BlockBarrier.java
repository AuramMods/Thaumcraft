package thaumcraft.common.blocks.misc;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.tiles.misc.TileBarrierStone;

public class BlockBarrier extends Block {
   public static final Material barrierMat = new BlockBarrier.MaterialBarrier();

   public BlockBarrier() {
      super(barrierMat);
      this.func_149647_a((CreativeTabs)null);
      this.func_149713_g(0);
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   public void func_149666_a(Item itemIn, CreativeTabs tab, List list) {
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return null;
   }

   public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing o) {
      return false;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
   }

   public void func_185477_a(IBlockState state, World world, BlockPos pos, AxisAlignedBB mask, List list, Entity collidingEntity) {
      if (collidingEntity != null && collidingEntity instanceof EntityLivingBase && !(collidingEntity instanceof EntityPlayer)) {
         int a = 1;
         if (world.func_180495_p(pos.func_177979_c(a)).func_177230_c() != BlocksTC.pavingStone) {
            ++a;
         }

         if (world.func_175687_A(pos.func_177979_c(a)) == 0) {
            list.add(field_185505_j.func_186670_a(pos));
         }
      }

   }

   public void func_189540_a(IBlockState state, World world, BlockPos pos, Block neighborBlock) {
      if (world.func_180495_p(pos.func_177979_c(1)) != BlocksTC.pavingStone.func_176223_P() && world.func_180495_p(pos.func_177979_c(1)) != this.func_176223_P()) {
         world.func_175698_g(pos);
      }

   }

   public boolean func_176205_b(IBlockAccess worldIn, BlockPos pos) {
      for(int a = 1; a < 3; ++a) {
         TileEntity te = worldIn.func_175625_s(pos.func_177979_c(a));
         if (te != null && te instanceof TileBarrierStone) {
            return te.func_145831_w().func_175687_A(pos.func_177979_c(a)) > 0;
         }
      }

      return true;
   }

   public boolean func_176200_f(IBlockAccess worldIn, BlockPos pos) {
      return true;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return Item.func_150899_d(0);
   }

   public boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos) {
      return false;
   }

   private static class MaterialBarrier extends Material {
      public MaterialBarrier() {
         super(MapColor.field_151660_b);
      }

      public boolean func_76230_c() {
         return true;
      }

      public boolean func_76220_a() {
         return false;
      }

      public boolean func_76228_b() {
         return false;
      }
   }
}
