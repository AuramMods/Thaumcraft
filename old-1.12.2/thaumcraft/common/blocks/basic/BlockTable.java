package thaumcraft.common.blocks.basic;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.IScribeTools;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.blocks.IBlockFacingHorizontal;
import thaumcraft.common.tiles.crafting.TileResearchTable;

public class BlockTable extends BlockTC {
   public BlockTable(Material mat, SoundType st) {
      super(mat);
      this.func_149672_a(st);
   }

   public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
      return side == EnumFacing.UP;
   }

   public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
      return true;
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
         if (heldItem != null && this == BlocksTC.tableWood && heldItem.func_77973_b() instanceof IScribeTools) {
            IBlockState bs = BlocksTC.researchTable.func_176223_P();
            bs = bs.func_177226_a(IBlockFacingHorizontal.FACING, player.func_174811_aO());
            world.func_175656_a(pos, bs);
            TileResearchTable tile = (TileResearchTable)world.func_175625_s(pos);
            tile.func_70299_a(0, heldItem.func_77946_l());
            --heldItem.field_77994_a;
            heldItem = null;
            player.field_71071_by.func_70296_d();
            tile.func_70296_d();
            world.markAndNotifyBlock(pos, world.func_175726_f(pos), bs, bs, 3);
         }

         return true;
      }
   }
}
