package thaumcraft.common.blocks.devices;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.blocks.IBlockFacing;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.devices.TileMirror;
import thaumcraft.common.tiles.devices.TileMirrorEssentia;

public class BlockMirror extends BlockTCDevice implements IBlockFacing {
   public BlockMirror(Class cls) {
      super(Material.field_151573_f, cls);
      this.func_149672_a(SoundsTC.JAR);
      this.func_149711_c(0.1F);
      this.setHarvestLevel((String)null, 0);
      IBlockState bs = this.field_176227_L.func_177621_b();
      bs.func_177226_a(IBlockFacing.FACING, EnumFacing.UP);
      this.func_180632_j(bs);
   }

   public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
      return true;
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public int func_180651_a(IBlockState state) {
      return 0;
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState bs = this.func_176223_P();
      bs = bs.func_177226_a(IBlockFacing.FACING, facing);
      return bs;
   }

   public void func_176213_c(World worldIn, BlockPos pos, IBlockState state) {
   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
      EnumFacing d = BlockStateUtils.getFacing(state);
      if (!worldIn.func_180495_p(pos.func_177972_a(d.func_176734_d())).isSideSolid(worldIn, pos.func_177972_a(d.func_176734_d()), d)) {
         this.func_176226_b(worldIn, pos, this.func_176223_P(), 0);
         worldIn.func_175698_g(pos);
      }

   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      EnumFacing facing = BlockStateUtils.getFacing(state);
      switch(facing.ordinal()) {
      case 1:
         return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
      case 2:
         return new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
      case 3:
         return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
      case 4:
         return new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
      case 5:
         return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);
      default:
         return new AxisAlignedBB(0.0D, 0.875D, 0.0D, 1.0D, 1.0D, 1.0D);
      }
   }

   public boolean func_176198_a(World worldIn, BlockPos pos, EnumFacing side) {
      return worldIn.func_180495_p(pos.func_177972_a(side.func_176734_d())).isSideSolid(worldIn, pos.func_177972_a(side.func_176734_d()), side);
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      return world.field_72995_K ? true : true;
   }

   public void func_180653_a(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
      TileEntity te = worldIn.func_175625_s(pos);
      if (!(te instanceof TileMirror) && !(te instanceof TileMirrorEssentia)) {
         super.func_180653_a(worldIn, pos, state, chance, fortune);
      } else {
         this.dropMirror(worldIn, pos, state, te);
      }

   }

   public void func_180657_a(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
      if (!(te instanceof TileMirror) && !(te instanceof TileMirrorEssentia)) {
         super.func_180657_a(worldIn, player, pos, state, (TileEntity)null, stack);
      } else {
         this.dropMirror(worldIn, pos, state, te);
      }

   }

   public void dropMirror(World world, BlockPos pos, IBlockState state, TileEntity te) {
      ItemStack drop;
      if (this.tileClass == TileMirror.class) {
         TileMirror tm = (TileMirror)te;
         drop = new ItemStack(this, 1, 0);
         if (tm != null && tm instanceof TileMirror) {
            if (tm.linked) {
               drop.func_77964_b(1);
               drop.func_77983_a("linkX", new NBTTagInt(tm.linkX));
               drop.func_77983_a("linkY", new NBTTagInt(tm.linkY));
               drop.func_77983_a("linkZ", new NBTTagInt(tm.linkZ));
               drop.func_77983_a("linkDim", new NBTTagInt(tm.linkDim));
               tm.invalidateLink();
            }

            func_180635_a(world, pos, drop);
         }
      } else {
         TileMirrorEssentia tm = (TileMirrorEssentia)te;
         drop = new ItemStack(this, 1, 0);
         if (tm != null && tm instanceof TileMirrorEssentia) {
            if (tm.linked) {
               drop.func_77964_b(1);
               drop.func_77983_a("linkX", new NBTTagInt(tm.linkX));
               drop.func_77983_a("linkY", new NBTTagInt(tm.linkY));
               drop.func_77983_a("linkZ", new NBTTagInt(tm.linkZ));
               drop.func_77983_a("linkDim", new NBTTagInt(tm.linkDim));
               tm.invalidateLink();
            }

            func_180635_a(world, pos, drop);
         }
      }

   }

   public void func_180634_a(World world, BlockPos pos, IBlockState state, Entity entity) {
      if (!world.field_72995_K && this.tileClass == TileMirror.class && entity instanceof EntityItem && !entity.field_70128_L && ((EntityItem)entity).field_71088_bW == 0) {
         TileMirror taf = (TileMirror)world.func_175625_s(pos);
         if (taf != null) {
            taf.transport((EntityItem)entity);
         }
      }

      super.func_180634_a(world, pos, state, entity);
   }
}
