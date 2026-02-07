package thaumcraft.common.blocks.devices;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
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
import thaumcraft.common.blocks.IBlockFacing;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.devices.TileLevitator;

public class BlockLevitator extends BlockTCDevice implements IBlockFacing, IBlockEnabled {
   private RayTracer rayTracer = new RayTracer();

   public BlockLevitator() {
      super(Material.field_151575_d, TileLevitator.class);
      this.func_149672_a(SoundType.field_185848_a);
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

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      RayTraceResult hit = RayTracer.retraceBlock(world, player, pos);
      if (hit == null) {
         return super.func_180639_a(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
      } else {
         TileEntity tile = world.func_175625_s(pos);
         if (hit.subHit == 0 && tile instanceof TileLevitator) {
            ((TileLevitator)tile).increaseRange(player);
            world.func_184148_a((EntityPlayer)null, (double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + 0.5D, (double)pos.func_177952_p() + 0.5D, SoundsTC.key, SoundCategory.BLOCKS, 0.5F, 1.0F);
            return true;
         } else {
            return super.func_180639_a(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
         }
      }
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB func_180640_a(IBlockState state, World world, BlockPos pos) {
      TileEntity tile = world.func_175625_s(pos);
      if (tile != null && tile instanceof TileLevitator) {
         RayTraceResult hit = RayTracer.retraceBlock(world, Minecraft.func_71410_x().field_71439_g, pos);
         if (hit != null && hit.subHit == 0) {
            Cuboid6 cubeoid = ((TileLevitator)tile).getCuboidByFacing(BlockStateUtils.getFacing(tile.func_145832_p()));
            Vector3 v = new Vector3(pos);
            Cuboid6 c = cubeoid.sub(v);
            return new AxisAlignedBB((double)((float)c.min.x), (double)((float)c.min.y), (double)((float)c.min.z), (double)((float)c.max.x), (double)((float)c.max.y), (double)((float)c.max.z));
         }
      }

      return super.func_180640_a(state, world, pos);
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      EnumFacing facing = BlockStateUtils.getFacing(state);
      float f = 0.125F;
      float minx = 0.0F + (facing.func_82601_c() > 0 ? f : 0.0F);
      float maxx = 1.0F - (facing.func_82601_c() < 0 ? f : 0.0F);
      float miny = 0.0F + (facing.func_96559_d() > 0 ? f : 0.0F);
      float maxy = 1.0F - (facing.func_96559_d() < 0 ? f : 0.0F);
      float minz = 0.0F + (facing.func_82599_e() > 0 ? f : 0.0F);
      float maxz = 1.0F - (facing.func_82599_e() < 0 ? f : 0.0F);
      return new AxisAlignedBB((double)minx, (double)miny, (double)minz, (double)maxx, (double)maxy, (double)maxz);
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
      if (tile != null && tile instanceof TileLevitator) {
         List<IndexedCuboid6> cuboids = new LinkedList();
         if (tile instanceof TileLevitator) {
            ((TileLevitator)tile).addTraceableCuboids(cuboids);
         }

         ArrayList<ExtendedMOP> list = new ArrayList();
         this.rayTracer.rayTraceCuboids(new Vector3(start), new Vector3(end), cuboids, new BlockCoord(pos), this, list);
         return list.size() > 0 ? (RayTraceResult)list.get(0) : super.func_180636_a(state, world, pos, start, end);
      } else {
         return super.func_180636_a(state, world, pos, start, end);
      }
   }
}
