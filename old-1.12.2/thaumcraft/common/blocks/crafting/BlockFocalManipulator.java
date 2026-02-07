package thaumcraft.common.blocks.crafting;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.Thaumcraft;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.tiles.crafting.TileFocalManipulator;

public class BlockFocalManipulator extends BlockTCDevice {
   public BlockFocalManipulator() {
      super(Material.field_151576_e, TileFocalManipulator.class);
      this.func_149672_a(SoundType.field_185851_d);
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (world.field_72995_K) {
         return true;
      } else {
         player.openGui(Thaumcraft.instance, 7, world, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
         return true;
      }
   }
}
