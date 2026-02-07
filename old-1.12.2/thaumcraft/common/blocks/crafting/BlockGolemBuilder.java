package thaumcraft.common.blocks.crafting;

import java.util.Random;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.Thaumcraft;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.blocks.IBlockFacingHorizontal;
import thaumcraft.common.blocks.misc.BlockPlaceholder;
import thaumcraft.common.tiles.crafting.TileGolemBuilder;

public class BlockGolemBuilder extends BlockTCDevice implements IBlockFacingHorizontal {
   public static boolean ignore = false;

   public BlockGolemBuilder() {
      super(Material.field_151576_e, TileGolemBuilder.class);
      this.func_149672_a(SoundType.field_185851_d);
      this.func_149647_a((CreativeTabs)null);
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return Item.func_150898_a(Blocks.field_150331_J);
   }

   public int func_180651_a(IBlockState state) {
      return 0;
   }

   public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
      destroy(worldIn, pos, state, pos);
      super.func_180663_b(worldIn, pos, state);
   }

   public static void destroy(World worldIn, BlockPos pos, IBlockState state, BlockPos startpos) {
      if (!ignore && !worldIn.field_72995_K) {
         ignore = true;

         for(int a = -1; a <= 1; ++a) {
            for(int b = 0; b <= 1; ++b) {
               for(int c = -1; c <= 1; ++c) {
                  if (pos.func_177982_a(a, b, c) != startpos) {
                     IBlockState bs = worldIn.func_180495_p(pos.func_177982_a(a, b, c));
                     if (bs.func_177230_c() == BlocksTC.placeholder && bs.func_177229_b(BlockPlaceholder.VARIANT) == BlockPlaceholder.PlaceholderType.GB_BARS) {
                        worldIn.func_175656_a(pos.func_177982_a(a, b, c), Blocks.field_150411_aY.func_176223_P());
                     }

                     if (bs.func_177230_c() == BlocksTC.placeholder && bs.func_177229_b(BlockPlaceholder.VARIANT) == BlockPlaceholder.PlaceholderType.GB_ANVIL) {
                        worldIn.func_175656_a(pos.func_177982_a(a, b, c), Blocks.field_150467_bQ.func_176223_P());
                     }

                     if (bs.func_177230_c() == BlocksTC.placeholder && bs.func_177229_b(BlockPlaceholder.VARIANT) == BlockPlaceholder.PlaceholderType.GB_CAULDRON) {
                        worldIn.func_175656_a(pos.func_177982_a(a, b, c), Blocks.field_150383_bp.func_176223_P());
                     }

                     if (bs.func_177230_c() == BlocksTC.placeholder && bs.func_177229_b(BlockPlaceholder.VARIANT) == BlockPlaceholder.PlaceholderType.GB_TABLE) {
                        worldIn.func_175656_a(pos.func_177982_a(a, b, c), BlocksTC.tableStone.func_176223_P());
                     }
                  }
               }
            }
         }

         if (pos != startpos) {
            worldIn.func_175656_a(pos, Blocks.field_150331_J.func_176223_P().func_177226_a(BlockPistonBase.field_176387_N, EnumFacing.UP));
         }

         ignore = false;
      }
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (world.field_72995_K) {
         return true;
      } else {
         player.openGui(Thaumcraft.instance, 19, world, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
         return true;
      }
   }
}
