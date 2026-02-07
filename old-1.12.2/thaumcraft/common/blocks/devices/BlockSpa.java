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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import thaumcraft.Thaumcraft;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.tiles.devices.TileSpa;

public class BlockSpa extends BlockTCDevice {
   public BlockSpa() {
      super(Material.field_151576_e, TileSpa.class);
      this.func_149672_a(SoundType.field_185851_d);
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (world.field_72995_K) {
         return true;
      } else {
         TileEntity tileEntity = world.func_175625_s(pos);
         if (tileEntity instanceof TileSpa && !player.func_70093_af()) {
            FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(heldItem);
            if (fs != null) {
               TileSpa tile = (TileSpa)tileEntity;
               if (tile.tank.getFluidAmount() < tile.tank.getCapacity() && (tile.tank.getFluid() == null || tile.tank.getFluid().isFluidEqual(fs))) {
                  tile.fill(EnumFacing.UP, FluidContainerRegistry.getFluidForFilledItem(heldItem), true);
                  ItemStack emptyContainer = null;
                  FluidContainerData[] fcs = FluidContainerRegistry.getRegisteredFluidContainerData();
                  FluidContainerData[] var16 = fcs;
                  int var17 = fcs.length;

                  for(int var18 = 0; var18 < var17; ++var18) {
                     FluidContainerData fcd = var16[var18];
                     if (fcd.filledContainer.func_77969_a(heldItem)) {
                        emptyContainer = fcd.emptyContainer.func_77946_l();
                     }
                  }

                  player.field_71071_by.func_70298_a(player.field_71071_by.field_70461_c, 1);
                  if (emptyContainer != null && !player.field_71071_by.func_70441_a(emptyContainer)) {
                     player.func_71019_a(emptyContainer, false);
                  }

                  player.field_71069_bz.func_75142_b();
                  tile.func_70296_d();
                  world.markAndNotifyBlock(pos, world.func_175726_f(pos), state, state, 3);
                  world.func_184133_a((EntityPlayer)null, pos, SoundEvents.field_187615_H, SoundCategory.BLOCKS, 0.33F, 1.0F + (world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.3F);
               }
            } else {
               player.openGui(Thaumcraft.instance, 6, world, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
            }
         }

         return true;
      }
   }
}
