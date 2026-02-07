package thaumcraft.common.blocks.devices;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.tiles.devices.TileWaterJug;

public class BlockWaterJug extends BlockTCDevice {
   public BlockWaterJug() {
      super(Material.field_151576_e, TileWaterJug.class);
      this.func_149672_a(SoundType.field_185851_d);
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 1.0D, 0.8125D);
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && FluidContainerRegistry.isEmptyContainer(player.field_71071_by.func_70448_g())) {
         int cap = FluidContainerRegistry.getContainerCapacity(new FluidStack(FluidRegistry.WATER, 1), player.field_71071_by.func_70448_g());
         ItemStack res = FluidContainerRegistry.fillFluidContainer(new FluidStack(FluidRegistry.WATER, cap), player.field_71071_by.func_70448_g());
         if (res != null) {
            player.field_71071_by.func_70298_a(player.field_71071_by.field_70461_c, 1);
            if (!player.field_71071_by.func_70441_a(res)) {
               player.func_71019_a(res, false);
            }

            player.field_71069_bz.func_75142_b();
            TileEntity te = world.func_175625_s(pos);
            if (te != null && te instanceof TileWaterJug) {
               TileWaterJug tile = (TileWaterJug)te;
               tile.charge -= cap;
               te.func_70296_d();
            }

            world.func_184133_a((EntityPlayer)null, pos, SoundEvents.field_187615_H, SoundCategory.BLOCKS, 0.33F, 1.0F + (world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.3F);
         }
      }

      return true;
   }
}
