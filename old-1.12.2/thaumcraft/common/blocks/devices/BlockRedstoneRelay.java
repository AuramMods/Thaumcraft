package thaumcraft.common.blocks.devices;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.codechicken.lib.raytracer.ExtendedMOP;
import thaumcraft.codechicken.lib.raytracer.IndexedCuboid6;
import thaumcraft.codechicken.lib.raytracer.RayTracer;
import thaumcraft.codechicken.lib.vec.BlockCoord;
import thaumcraft.codechicken.lib.vec.Cuboid6;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.blocks.IBlockEnabled;
import thaumcraft.common.blocks.IBlockFacingHorizontal;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.devices.TileRedstoneRelay;

public class BlockRedstoneRelay extends BlockTCDevice implements IBlockFacingHorizontal, IBlockEnabled {
   private RayTracer rayTracer = new RayTracer();

   public BlockRedstoneRelay() {
      super(Material.field_151594_q, TileRedstoneRelay.class);
      this.func_149711_c(0.0F);
      this.func_149752_b(0.0F);
      this.func_149672_a(SoundType.field_185848_a);
      this.func_149649_H();
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public int func_180651_a(IBlockState state) {
      return 0;
   }

   public boolean func_176196_c(World worldIn, BlockPos pos) {
      return worldIn.func_180495_p(pos.func_177977_b()).func_185896_q() ? super.func_176196_c(worldIn, pos) : false;
   }

   public boolean canBlockStay(World worldIn, BlockPos pos) {
      return worldIn.func_180495_p(pos.func_177977_b()).func_185896_q();
   }

   public void func_180645_a(World worldIn, BlockPos pos, IBlockState state, Random random) {
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (!player.field_71075_bZ.field_75099_e) {
         return false;
      } else {
         RayTraceResult hit = RayTracer.retraceBlock(world, player, pos);
         if (hit == null) {
            return super.func_180639_a(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
         } else {
            TileEntity tile = world.func_175625_s(pos);
            if (tile != null && tile instanceof TileRedstoneRelay) {
               if (hit.subHit == 0) {
                  ((TileRedstoneRelay)tile).increaseOut();
                  world.func_184133_a((EntityPlayer)null, pos, SoundsTC.key, SoundCategory.BLOCKS, 0.5F, 1.0F);
                  this.updateState(world, pos, state);
                  this.notifyNeighbors(world, pos, state);
               }

               if (hit.subHit == 1) {
                  ((TileRedstoneRelay)tile).increaseIn();
                  world.func_184133_a((EntityPlayer)null, pos, SoundsTC.key, SoundCategory.BLOCKS, 0.5F, 1.0F);
                  this.updateState(world, pos, state);
                  this.notifyNeighbors(world, pos, state);
               }

               return true;
            } else {
               return super.func_180639_a(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
            }
         }
      }
   }

   public void func_180650_b(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      boolean flag = this.shouldBePowered(worldIn, pos, state);
      if (this.isPowered(state) && !flag) {
         worldIn.func_180501_a(pos, this.getUnpoweredState(state), 2);
         this.notifyNeighbors(worldIn, pos, state);
      } else if (!this.isPowered(state)) {
         worldIn.func_180501_a(pos, this.getPoweredState(state), 2);
         this.notifyNeighbors(worldIn, pos, state);
         if (!flag) {
            worldIn.func_175654_a(pos, this.getPoweredState(state).func_177230_c(), this.getTickDelay(state), -1);
         }
      }

   }

   public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
      super.func_180663_b(worldIn, pos, state);
      this.notifyNeighbors(worldIn, pos, state);
   }

   @SideOnly(Side.CLIENT)
   public boolean func_176225_a(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
      return side.func_176740_k() != Axis.Y;
   }

   protected boolean isPowered(IBlockState state) {
      return BlockStateUtils.isEnabled(state);
   }

   public int func_176211_b(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
      return this.func_180656_a(state, worldIn, pos, side);
   }

   public int func_180656_a(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
      return !this.isPowered(state) ? 0 : (state.func_177229_b(FACING) == side ? this.getActiveSignal(worldIn, pos, state) : 0);
   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
      if (this.canBlockStay(worldIn, pos)) {
         this.updateState(worldIn, pos, state);
      } else {
         this.func_176226_b(worldIn, pos, state, 0);
         worldIn.func_175698_g(pos);
         EnumFacing[] aenumfacing = EnumFacing.values();
         int i = aenumfacing.length;

         for(int j = 0; j < i; ++j) {
            EnumFacing enumfacing = aenumfacing[j];
            worldIn.func_175685_c(pos.func_177972_a(enumfacing), this);
         }
      }

   }

   protected void updateState(World worldIn, BlockPos pos, IBlockState state) {
      boolean flag = this.shouldBePowered(worldIn, pos, state);
      if ((this.isPowered(state) && !flag || !this.isPowered(state) && flag) && !worldIn.func_175691_a(pos, this)) {
         byte b0 = -1;
         if (this.isFacingTowardsRepeater(worldIn, pos, state)) {
            b0 = -3;
         } else if (this.isPowered(state)) {
            b0 = -2;
         }

         worldIn.func_175654_a(pos, this, this.getTickDelay(state), b0);
      }

   }

   protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state) {
      int pr = 1;
      TileEntity tile = worldIn.func_175625_s(pos);
      if (tile != null && tile instanceof TileRedstoneRelay) {
         pr = ((TileRedstoneRelay)tile).getIn();
      }

      return this.calculateInputStrength(worldIn, pos, state) >= pr;
   }

   protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state) {
      EnumFacing enumfacing = (EnumFacing)state.func_177229_b(FACING);
      BlockPos blockpos1 = pos.func_177972_a(enumfacing);
      int i = worldIn.func_175651_c(blockpos1, enumfacing);
      if (i >= 15) {
         return i;
      } else {
         IBlockState iblockstate1 = worldIn.func_180495_p(blockpos1);
         return Math.max(i, iblockstate1.func_177230_c() == Blocks.field_150488_af ? (Integer)iblockstate1.func_177229_b(BlockRedstoneWire.field_176351_O) : 0);
      }
   }

   protected int getPowerOnSides(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
      EnumFacing enumfacing = (EnumFacing)state.func_177229_b(FACING);
      EnumFacing enumfacing1 = enumfacing.func_176746_e();
      EnumFacing enumfacing2 = enumfacing.func_176735_f();
      return Math.max(this.getPowerOnSide(worldIn, pos.func_177972_a(enumfacing1), enumfacing1), this.getPowerOnSide(worldIn, pos.func_177972_a(enumfacing2), enumfacing2));
   }

   protected int getPowerOnSide(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
      IBlockState iblockstate = worldIn.func_180495_p(pos);
      Block block = iblockstate.func_177230_c();
      return this.canPowerSide(block, iblockstate) ? (block == Blocks.field_150488_af ? (Integer)iblockstate.func_177229_b(BlockRedstoneWire.field_176351_O) : worldIn.func_175627_a(pos, side)) : 0;
   }

   public boolean func_149744_f(IBlockState state) {
      return true;
   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      if (this.shouldBePowered(worldIn, pos, state)) {
         worldIn.func_175684_a(pos, this, 1);
      }

   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState bs = this.func_176223_P();
      bs = bs.func_177226_a(FACING, placer.func_70093_af() ? placer.func_174811_aO() : placer.func_174811_aO().func_176734_d());
      bs = bs.func_177226_a(ENABLED, false);
      return bs;
   }

   public void func_176213_c(World worldIn, BlockPos pos, IBlockState state) {
      this.notifyNeighbors(worldIn, pos, state);
   }

   protected void notifyNeighbors(World worldIn, BlockPos pos, IBlockState state) {
      EnumFacing enumfacing = (EnumFacing)state.func_177229_b(FACING);
      BlockPos blockpos1 = pos.func_177972_a(enumfacing.func_176734_d());
      if (!ForgeEventFactory.onNeighborNotify(worldIn, pos, worldIn.func_180495_p(pos), EnumSet.of(enumfacing.func_176734_d())).isCanceled()) {
         worldIn.func_180496_d(blockpos1, this);
         worldIn.func_175695_a(blockpos1, this, enumfacing);
      }
   }

   public void func_176206_d(World worldIn, BlockPos pos, IBlockState state) {
      if (this.isPowered(state)) {
         EnumFacing[] aenumfacing = EnumFacing.values();
         int i = aenumfacing.length;

         for(int j = 0; j < i; ++j) {
            EnumFacing enumfacing = aenumfacing[j];
            worldIn.func_175685_c(pos.func_177972_a(enumfacing), this);
         }
      }

      super.func_176206_d(worldIn, pos, state);
   }

   protected boolean canPowerSide(Block blockIn, IBlockState iblockstate) {
      return blockIn.func_149744_f(iblockstate);
   }

   protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
      TileEntity tile = worldIn.func_175625_s(pos);
      return tile != null && tile instanceof TileRedstoneRelay ? ((TileRedstoneRelay)tile).getOut() : 0;
   }

   public static boolean isRedstoneRepeaterBlockID(Block blockIn) {
      return Blocks.field_150413_aR.func_149667_c(blockIn) || Blocks.field_150441_bU.func_149667_c(blockIn);
   }

   public boolean isAssociated(Block other) {
      return other == this.getPoweredState(this.func_176223_P()).func_177230_c() || other == this.getUnpoweredState(this.func_176223_P()).func_177230_c();
   }

   public boolean isFacingTowardsRepeater(World worldIn, BlockPos pos, IBlockState state) {
      EnumFacing enumfacing = ((EnumFacing)state.func_177229_b(FACING)).func_176734_d();
      BlockPos blockpos1 = pos.func_177972_a(enumfacing);
      return isRedstoneRepeaterBlockID(worldIn.func_180495_p(blockpos1).func_177230_c()) ? worldIn.func_180495_p(blockpos1).func_177229_b(FACING) != enumfacing : false;
   }

   protected int getTickDelay(IBlockState state) {
      return 2;
   }

   protected IBlockState getPoweredState(IBlockState unpoweredState) {
      EnumFacing enumfacing = (EnumFacing)unpoweredState.func_177229_b(FACING);
      return this.func_176223_P().func_177226_a(FACING, enumfacing).func_177226_a(ENABLED, true);
   }

   protected IBlockState getUnpoweredState(IBlockState poweredState) {
      EnumFacing enumfacing = (EnumFacing)poweredState.func_177229_b(FACING);
      return this.func_176223_P().func_177226_a(FACING, enumfacing).func_177226_a(ENABLED, false);
   }

   public boolean func_149667_c(Block other) {
      return this.isAssociated(other);
   }

   @SideOnly(Side.CLIENT)
   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.CUTOUT;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB func_180640_a(IBlockState state, World world, BlockPos pos) {
      TileEntity tile = world.func_175625_s(pos);
      if (tile != null && tile instanceof TileRedstoneRelay) {
         RayTraceResult hit = RayTracer.retraceBlock(world, Minecraft.func_71410_x().field_71439_g, pos);
         Cuboid6 cubeoid;
         Vector3 v;
         Cuboid6 c;
         if (hit != null && hit.subHit == 0) {
            cubeoid = ((TileRedstoneRelay)tile).getCuboid0(BlockStateUtils.getFacing(tile.func_145832_p()));
            v = new Vector3(pos);
            c = cubeoid.sub(v);
            return (new AxisAlignedBB((double)((float)c.min.x), (double)((float)c.min.y), (double)((float)c.min.z), (double)((float)c.max.x), (double)((float)c.max.y), (double)((float)c.max.z))).func_186670_a(pos);
         }

         if (hit != null && hit.subHit == 1) {
            cubeoid = ((TileRedstoneRelay)tile).getCuboid1(BlockStateUtils.getFacing(tile.func_145832_p()));
            v = new Vector3(pos);
            c = cubeoid.sub(v);
            return (new AxisAlignedBB((double)((float)c.min.x), (double)((float)c.min.y), (double)((float)c.min.z), (double)((float)c.max.x), (double)((float)c.max.y), (double)((float)c.max.z))).func_186670_a(pos);
         }
      }

      return super.func_180640_a(state, world, pos);
   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void onBlockHighlight(DrawBlockHighlightEvent event) {
      if (event.getTarget().field_72313_a == Type.BLOCK && event.getPlayer().field_70170_p.func_180495_p(event.getTarget().func_178782_a()).func_177230_c() == this) {
         RayTracer.retraceBlock(event.getPlayer().field_70170_p, event.getPlayer(), event.getTarget().func_178782_a());
      }

   }

   public RayTraceResult func_180636_a(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
      TileEntity tile = world.func_175625_s(pos);
      if (tile != null && tile instanceof TileRedstoneRelay) {
         List<IndexedCuboid6> cuboids = new LinkedList();
         if (tile instanceof TileRedstoneRelay) {
            ((TileRedstoneRelay)tile).addTraceableCuboids(cuboids);
         }

         ArrayList<ExtendedMOP> list = new ArrayList();
         this.rayTracer.rayTraceCuboids(new Vector3(start), new Vector3(end), cuboids, new BlockCoord(pos), this, list);
         return list.size() > 0 ? (RayTraceResult)list.get(0) : super.func_180636_a(state, world, pos, start, end);
      } else {
         return super.func_180636_a(state, world, pos, start, end);
      }
   }
}
