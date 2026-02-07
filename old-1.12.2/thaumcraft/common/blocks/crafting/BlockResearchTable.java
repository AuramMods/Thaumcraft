package thaumcraft.common.blocks.crafting;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.Thaumcraft;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.blocks.IBlockFacingHorizontal;
import thaumcraft.common.tiles.crafting.TileResearchTable;

public class BlockResearchTable extends BlockTCDevice implements IBlockFacingHorizontal {
   public BlockResearchTable() {
      super(Material.field_151575_d, TileResearchTable.class);
      this.func_149672_a(SoundType.field_185848_a);
   }

   public int func_180651_a(IBlockState state) {
      return 0;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
      return false;
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (world.field_72995_K) {
         return true;
      } else {
         player.openGui(Thaumcraft.instance, 10, world, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
         return true;
      }
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState bs = this.func_176223_P();
      bs = bs.func_177226_a(IBlockFacingHorizontal.FACING, placer.func_174811_aO());
      return bs;
   }
}
