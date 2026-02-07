package thaumcraft.common.blocks.devices;

import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.blocks.IBlockFacingHorizontal;
import thaumcraft.common.blocks.misc.BlockPlaceholder;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.devices.TileInfernalFurnace;

public class BlockInfernalFurnace extends BlockTCDevice implements IBlockFacingHorizontal {
   public static boolean ignore = false;

   public BlockInfernalFurnace() {
      super(Material.field_151576_e, TileInfernalFurnace.class);
      this.func_149672_a(SoundType.field_185851_d);
      this.func_149715_a(0.9F);
      IBlockState bs = this.field_176227_L.func_177621_b();
      bs.func_177226_a(IBlockFacingHorizontal.FACING, EnumFacing.NORTH);
      this.func_180632_j(bs);
      this.func_149647_a((CreativeTabs)null);
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
   }

   public void func_176213_c(World worldIn, BlockPos pos, IBlockState state) {
   }

   @SideOnly(Side.CLIENT)
   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.CUTOUT_MIPPED;
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState bs = this.func_176223_P();
      bs = bs.func_177226_a(IBlockFacingHorizontal.FACING, placer.func_174811_aO().func_176734_d());
      return bs;
   }

   public static void destroyFurnace(World worldIn, BlockPos pos, IBlockState state, BlockPos startpos) {
      if (!ignore && !worldIn.field_72995_K) {
         ignore = true;

         for(int a = -1; a <= 1; ++a) {
            for(int b = -1; b <= 1; ++b) {
               for(int c = -1; c <= 1; ++c) {
                  if (pos.func_177982_a(a, b, c) != startpos) {
                     IBlockState bs = worldIn.func_180495_p(pos.func_177982_a(a, b, c));
                     if (bs.func_177230_c() == BlocksTC.placeholder && bs.func_177229_b(BlockPlaceholder.VARIANT) == BlockPlaceholder.PlaceholderType.FURNACE_BRICK) {
                        worldIn.func_175656_a(pos.func_177982_a(a, b, c), Blocks.field_150385_bj.func_176223_P());
                     }

                     if (bs.func_177230_c() == BlocksTC.placeholder && bs.func_177229_b(BlockPlaceholder.VARIANT) == BlockPlaceholder.PlaceholderType.FURNACE_OBSIDIAN) {
                        worldIn.func_175656_a(pos.func_177982_a(a, b, c), Blocks.field_150343_Z.func_176223_P());
                     }
                  }
               }
            }
         }

         if (worldIn.func_175623_d(pos.func_177972_a(BlockStateUtils.getFacing(state).func_176734_d()))) {
            worldIn.func_175656_a(pos.func_177972_a(BlockStateUtils.getFacing(state).func_176734_d()), Blocks.field_150411_aY.func_176223_P());
         }

         worldIn.func_175656_a(pos, Blocks.field_150353_l.func_176223_P());
         ignore = false;
      }
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return Item.func_150899_d(0);
   }

   public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
      destroyFurnace(worldIn, pos, state, pos);
      super.func_180663_b(worldIn, pos, state);
   }

   public void func_180634_a(World world, BlockPos pos, IBlockState state, Entity entity) {
      if (entity.field_70165_t < (double)((float)pos.func_177958_n() + 0.3F)) {
         entity.field_70159_w += 9.999999747378752E-5D;
      }

      if (entity.field_70165_t > (double)((float)pos.func_177958_n() + 0.7F)) {
         entity.field_70159_w -= 9.999999747378752E-5D;
      }

      if (entity.field_70161_v < (double)((float)pos.func_177952_p() + 0.3F)) {
         entity.field_70179_y += 9.999999747378752E-5D;
      }

      if (entity.field_70161_v > (double)((float)pos.func_177952_p() + 0.7F)) {
         entity.field_70179_y -= 9.999999747378752E-5D;
      }

      if (entity instanceof EntityItem) {
         entity.field_70181_x = 0.02500000037252903D;
         if (entity.field_70122_E) {
            TileInfernalFurnace taf = (TileInfernalFurnace)world.func_175625_s(pos);
            if (taf.addItemsToInventory(((EntityItem)entity).func_92059_d())) {
               entity.func_70106_y();
            }
         }
      } else if (entity instanceof EntityLivingBase && !entity.func_70045_F()) {
         entity.func_70097_a(DamageSource.field_76371_c, 3.0F);
         entity.func_70015_d(10);
      }

      super.func_180634_a(world, pos, state, entity);
   }
}
