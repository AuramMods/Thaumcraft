package thaumcraft.common.blocks;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.common.lib.utils.BlockStateUtils;

public class BlockTCDevice extends BlockTCTile {
   public BlockTCDevice(Material mat, Class tc) {
      super(mat, tc);
      IBlockState bs = this.field_176227_L.func_177621_b();
      if (this instanceof IBlockFacingHorizontal) {
         bs.func_177226_a(IBlockFacingHorizontal.FACING, EnumFacing.NORTH);
      } else if (this instanceof IBlockFacing) {
         bs.func_177226_a(IBlockFacing.FACING, EnumFacing.UP);
      }

      if (this instanceof IBlockEnabled) {
         bs.func_177226_a(IBlockEnabled.ENABLED, true);
      }

      this.func_180632_j(bs);
   }

   public void func_176213_c(World worldIn, BlockPos pos, IBlockState state) {
      super.func_176213_c(worldIn, pos, state);
      this.updateState(worldIn, pos, state);
   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
      this.updateState(worldIn, pos, state);
      super.func_189540_a(state, worldIn, pos, blockIn);
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState bs = this.func_176223_P();
      if (this instanceof IBlockFacingHorizontal) {
         bs = bs.func_177226_a(IBlockFacingHorizontal.FACING, placer.func_70093_af() ? placer.func_174811_aO() : placer.func_174811_aO().func_176734_d());
      }

      if (this instanceof IBlockFacing) {
         bs = bs.func_177226_a(IBlockFacing.FACING, placer.func_70093_af() ? BlockPistonBase.func_185647_a(pos, placer).func_176734_d() : BlockPistonBase.func_185647_a(pos, placer));
      }

      if (this instanceof IBlockEnabled) {
         bs = bs.func_177226_a(IBlockEnabled.ENABLED, true);
      }

      return bs;
   }

   protected void updateState(World worldIn, BlockPos pos, IBlockState state) {
      if (this instanceof IBlockEnabled) {
         boolean flag = !worldIn.func_175640_z(pos);
         if (flag != (Boolean)state.func_177229_b(IBlockEnabled.ENABLED)) {
            worldIn.func_180501_a(pos, state.func_177226_a(IBlockEnabled.ENABLED, flag), 3);
         }
      }

   }

   public void updateFacing(World world, BlockPos pos, EnumFacing face) {
      if (this instanceof IBlockFacing || this instanceof IBlockFacingHorizontal) {
         if (face == BlockStateUtils.getFacing(world.func_180495_p(pos))) {
            return;
         }

         if (this instanceof IBlockFacingHorizontal && face.func_176736_b() >= 0) {
            world.func_180501_a(pos, world.func_180495_p(pos).func_177226_a(IBlockFacingHorizontal.FACING, face), 3);
         }

         if (this instanceof IBlockFacing) {
            world.func_180501_a(pos, world.func_180495_p(pos).func_177226_a(IBlockFacing.FACING, face), 3);
         }
      }

   }

   public IBlockState func_176203_a(int meta) {
      IBlockState bs = this.func_176223_P();

      try {
         if (this instanceof IBlockFacingHorizontal) {
            bs = bs.func_177226_a(IBlockFacingHorizontal.FACING, BlockStateUtils.getFacing(meta));
         }

         if (this instanceof IBlockFacing) {
            bs = bs.func_177226_a(IBlockFacing.FACING, BlockStateUtils.getFacing(meta));
         }

         if (this instanceof IBlockEnabled) {
            bs = bs.func_177226_a(IBlockEnabled.ENABLED, BlockStateUtils.isEnabled(meta));
         }
      } catch (Exception var4) {
      }

      return bs;
   }

   public int func_176201_c(IBlockState state) {
      byte b0 = 0;
      int i = this instanceof IBlockFacingHorizontal ? b0 | ((EnumFacing)state.func_177229_b(IBlockFacingHorizontal.FACING)).func_176745_a() : (this instanceof IBlockFacing ? b0 | ((EnumFacing)state.func_177229_b(IBlockFacing.FACING)).func_176745_a() : b0);
      if (this instanceof IBlockEnabled && !(Boolean)state.func_177229_b(IBlockEnabled.ENABLED)) {
         i |= 8;
      }

      return i;
   }

   protected BlockStateContainer func_180661_e() {
      ArrayList<IProperty> ip = new ArrayList();
      if (this instanceof IBlockFacingHorizontal) {
         ip.add(IBlockFacingHorizontal.FACING);
      }

      if (this instanceof IBlockFacing) {
         ip.add(IBlockFacing.FACING);
      }

      if (this instanceof IBlockEnabled) {
         ip.add(IBlockEnabled.ENABLED);
      }

      return ip.size() == 0 ? super.func_180661_e() : new BlockStateContainer(this, (IProperty[])ip.toArray(new IProperty[ip.size()]));
   }
}
