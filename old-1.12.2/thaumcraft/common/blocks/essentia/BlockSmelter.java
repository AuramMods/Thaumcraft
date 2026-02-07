package thaumcraft.common.blocks.essentia;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.Thaumcraft;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.blocks.IBlockEnabled;
import thaumcraft.common.blocks.IBlockFacingHorizontal;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.essentia.TileSmelter;

public class BlockSmelter extends BlockTCDevice implements IBlockEnabled, IBlockFacingHorizontal {
   public BlockSmelter() {
      super(Material.field_151573_f, TileSmelter.class);
      this.func_149672_a(SoundType.field_185852_e);
      IBlockState bs = this.field_176227_L.func_177621_b();
      bs.func_177226_a(IBlockFacingHorizontal.FACING, EnumFacing.NORTH);
      bs.func_177226_a(IBlockEnabled.ENABLED, false);
      this.func_180632_j(bs);
   }

   public void func_176213_c(World worldIn, BlockPos pos, IBlockState state) {
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState bs = this.func_176223_P();
      bs = bs.func_177226_a(IBlockFacingHorizontal.FACING, placer.func_174811_aO().func_176734_d());
      bs = bs.func_177226_a(IBlockEnabled.ENABLED, false);
      return bs;
   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
      TileEntity te = worldIn.func_175625_s(pos);
      if (te != null && te instanceof TileSmelter) {
         ((TileSmelter)te).checkNeighbours();
      }

   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (!world.field_72995_K && !player.func_70093_af()) {
         player.openGui(Thaumcraft.instance, 9, world, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
      }

      return true;
   }

   public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
      return BlockStateUtils.isEnabled(world.func_180495_p(pos).func_177230_c().func_176201_c(world.func_180495_p(pos))) ? 13 : super.getLightValue(state, world, pos);
   }

   public boolean func_149740_M(IBlockState state) {
      return true;
   }

   public int func_180651_a(IBlockState state) {
      return 0;
   }

   public int func_180641_l(IBlockState state, World world, BlockPos pos) {
      TileEntity te = world.func_175625_s(pos);
      return te != null && te instanceof IInventory ? Container.func_94526_b((IInventory)te) : 0;
   }

   public static void setFurnaceState(World world, BlockPos pos, boolean state) {
      if (state != BlockStateUtils.isEnabled(world.func_180495_p(pos).func_177230_c().func_176201_c(world.func_180495_p(pos)))) {
         TileEntity tileentity = world.func_175625_s(pos);
         keepInventory = true;
         world.func_180501_a(pos, world.func_180495_p(pos).func_177226_a(IBlockEnabled.ENABLED, state), 3);
         world.func_180501_a(pos, world.func_180495_p(pos).func_177226_a(IBlockEnabled.ENABLED, state), 3);
         if (tileentity != null) {
            tileentity.func_145829_t();
            world.func_175690_a(pos, tileentity);
         }

         keepInventory = false;
      }
   }

   public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
      TileEntity tileentity = worldIn.func_175625_s(pos);
      if (tileentity instanceof TileSmelter && !worldIn.field_72995_K && ((TileSmelter)tileentity).vis > 0) {
         int ess = ((TileSmelter)tileentity).vis;
         AuraHelper.polluteAura(worldIn, pos, (float)ess, true);
      }

      super.func_180663_b(worldIn, pos, state);
   }

   @SideOnly(Side.CLIENT)
   public void func_180655_c(IBlockState state, World w, BlockPos pos, Random r) {
      if (BlockStateUtils.isEnabled(state)) {
         float f = (float)pos.func_177958_n() + 0.5F;
         float f1 = (float)pos.func_177956_o() + 0.2F + r.nextFloat() * 5.0F / 16.0F;
         float f2 = (float)pos.func_177952_p() + 0.5F;
         float f3 = 0.52F;
         float f4 = r.nextFloat() * 0.5F - 0.25F;
         if (BlockStateUtils.getFacing(state) == EnumFacing.WEST) {
            w.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, (double)(f - f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D, new int[0]);
            w.func_175688_a(EnumParticleTypes.FLAME, (double)(f - f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D, new int[0]);
         }

         if (BlockStateUtils.getFacing(state) == EnumFacing.EAST) {
            w.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, (double)(f + f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D, new int[0]);
            w.func_175688_a(EnumParticleTypes.FLAME, (double)(f + f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D, new int[0]);
         }

         if (BlockStateUtils.getFacing(state) == EnumFacing.NORTH) {
            w.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, (double)(f + f4), (double)f1, (double)(f2 - f3), 0.0D, 0.0D, 0.0D, new int[0]);
            w.func_175688_a(EnumParticleTypes.FLAME, (double)(f + f4), (double)f1, (double)(f2 - f3), 0.0D, 0.0D, 0.0D, new int[0]);
         }

         if (BlockStateUtils.getFacing(state) == EnumFacing.SOUTH) {
            w.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, (double)(f + f4), (double)f1, (double)(f2 + f3), 0.0D, 0.0D, 0.0D, new int[0]);
            w.func_175688_a(EnumParticleTypes.FLAME, (double)(f + f4), (double)f1, (double)(f2 + f3), 0.0D, 0.0D, 0.0D, new int[0]);
         }
      }

   }
}
