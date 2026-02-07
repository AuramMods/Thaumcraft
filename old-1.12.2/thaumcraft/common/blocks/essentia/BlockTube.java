package thaumcraft.common.blocks.essentia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.codechicken.lib.raytracer.ExtendedMOP;
import thaumcraft.codechicken.lib.raytracer.IndexedCuboid6;
import thaumcraft.codechicken.lib.raytracer.RayTracer;
import thaumcraft.codechicken.lib.vec.BlockCoord;
import thaumcraft.codechicken.lib.vec.Cuboid6;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.blocks.BlockTCDevice;
import thaumcraft.common.items.casters.ICaster;
import thaumcraft.common.items.tools.ItemResonator;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.essentia.TileTube;
import thaumcraft.common.tiles.essentia.TileTubeBuffer;
import thaumcraft.common.tiles.essentia.TileTubeFilter;
import thaumcraft.common.tiles.essentia.TileTubeValve;

public class BlockTube extends BlockTCDevice {
   public static final PropertyBool NORTH = PropertyBool.func_177716_a("north");
   public static final PropertyBool EAST = PropertyBool.func_177716_a("east");
   public static final PropertyBool SOUTH = PropertyBool.func_177716_a("south");
   public static final PropertyBool WEST = PropertyBool.func_177716_a("west");
   public static final PropertyBool UP = PropertyBool.func_177716_a("up");
   public static final PropertyBool DOWN = PropertyBool.func_177716_a("down");
   private RayTracer rayTracer = new RayTracer();

   public BlockTube(Class tile) {
      super(Material.field_151573_f, tile);
      this.func_149711_c(0.5F);
      this.func_149752_b(5.0F);
      this.func_149672_a(SoundType.field_185852_e);
      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(NORTH, false).func_177226_a(EAST, false).func_177226_a(SOUTH, false).func_177226_a(WEST, false).func_177226_a(UP, false).func_177226_a(DOWN, false));
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{NORTH, EAST, SOUTH, WEST, UP, DOWN});
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public int func_176201_c(IBlockState state) {
      return 0;
   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      TileEntity tile = worldIn.func_175625_s(pos);
      if (tile != null && tile instanceof TileTube) {
         ((TileTube)tile).facing = BlockPistonBase.func_185647_a(pos, placer);
         tile.func_70296_d();
      }

      super.func_180633_a(worldIn, pos, state, placer, stack);
   }

   public IBlockState func_176221_a(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      Boolean[] cons = this.makeConnections(state, worldIn, pos);
      return state.func_177226_a(DOWN, cons[0]).func_177226_a(UP, cons[1]).func_177226_a(NORTH, cons[2]).func_177226_a(SOUTH, cons[3]).func_177226_a(WEST, cons[4]).func_177226_a(EAST, cons[5]);
   }

   private Boolean[] makeConnections(IBlockState state, IBlockAccess world, BlockPos pos) {
      Boolean[] cons = new Boolean[]{false, false, false, false, false, false};
      TileEntity t = world.func_175625_s(pos);
      if (t != null && t instanceof IEssentiaTransport) {
         IEssentiaTransport tube = (IEssentiaTransport)t;
         int a = 0;
         EnumFacing[] var8 = EnumFacing.field_82609_l;
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            EnumFacing face = var8[var10];
            if (tube.isConnectable(face) && ThaumcraftApiHelper.getConnectableTile(world, pos, face) != null) {
               cons[a] = true;
            }

            ++a;
         }
      }

      return cons;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB func_180640_a(IBlockState state, World world, BlockPos pos) {
      boolean noDoodads = InventoryUtils.isHoldingItem(Minecraft.func_71410_x().field_71439_g, (Class)ICaster.class) == null && InventoryUtils.isHoldingItem(Minecraft.func_71410_x().field_71439_g, (Class)ItemResonator.class) == null;
      TileEntity tile = world.func_175625_s(pos);
      if (tile != null && tile instanceof TileTube) {
         RayTraceResult hit = RayTracer.retraceBlock(world, Minecraft.func_71410_x().field_71439_g, pos);
         List<IndexedCuboid6> cuboids = new LinkedList();
         ((TileTube)tile).addTraceableCuboids(cuboids);
         if (hit != null && hit.subHit >= 0 && hit.subHit <= 6 && !noDoodads) {
            Iterator var8 = cuboids.iterator();

            while(var8.hasNext()) {
               IndexedCuboid6 cc = (IndexedCuboid6)var8.next();
               if ((Integer)cc.data == hit.subHit) {
                  Vector3 v = new Vector3(pos);
                  Cuboid6 c = cc.sub(v);
                  return (new AxisAlignedBB((double)((float)c.min.x), (double)((float)c.min.y), (double)((float)c.min.z), (double)((float)c.max.x), (double)((float)c.max.y), (double)((float)c.max.z))).func_186670_a(pos);
               }
            }
         }
      }

      return super.func_180640_a(state, world, pos);
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      float minx = 0.3125F;
      float maxx = 0.6875F;
      float miny = 0.3125F;
      float maxy = 0.6875F;
      float minz = 0.3125F;
      float maxz = 0.6875F;
      EnumFacing fd = null;

      for(int side = 0; side < 6; ++side) {
         fd = EnumFacing.field_82609_l[side];
         TileEntity te = ThaumcraftApiHelper.getConnectableTile(source, pos, fd);
         if (te != null) {
            switch(side) {
            case 0:
               miny = 0.0F;
               break;
            case 1:
               maxy = 1.0F;
               break;
            case 2:
               minz = 0.0F;
               break;
            case 3:
               maxz = 1.0F;
               break;
            case 4:
               minx = 0.0F;
               break;
            case 5:
               maxx = 1.0F;
            }
         }
      }

      return new AxisAlignedBB((double)minx, (double)miny, (double)minz, (double)maxx, (double)maxy, (double)maxz);
   }

   public boolean func_149740_M(IBlockState state) {
      return true;
   }

   public int func_180641_l(IBlockState state, World world, BlockPos pos) {
      TileEntity te = world.func_175625_s(pos);
      if (te != null && te instanceof TileTubeBuffer) {
         float var10000 = (float)((TileTubeBuffer)te).aspects.visSize();
         ((TileTubeBuffer)te).getClass();
         float r = var10000 / 8.0F;
         return MathHelper.func_76141_d(r * 14.0F) + (((TileTubeBuffer)te).aspects.visSize() > 0 ? 1 : 0);
      } else {
         return 0;
      }
   }

   public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
      TileEntity te = worldIn.func_175625_s(pos);
      if (te != null && te instanceof TileTube && ((TileTube)te).getEssentiaAmount(EnumFacing.UP) > 0) {
         if (!worldIn.field_72995_K) {
            AuraHelper.polluteAura(worldIn, pos, (float)((TileTube)te).getEssentiaAmount(EnumFacing.UP), true);
         } else {
            worldIn.func_184134_a((double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + 0.5D, (double)pos.func_177952_p() + 0.5D, SoundEvents.field_187659_cY, SoundCategory.BLOCKS, 0.1F, 1.0F + worldIn.field_73012_v.nextFloat() * 0.1F, false);

            for(int a = 0; a < 5; ++a) {
               FXDispatcher.INSTANCE.drawVentParticles((double)pos.func_177958_n() + 0.33D + (double)worldIn.field_73012_v.nextFloat() * 0.33D, (double)pos.func_177956_o() + 0.33D + (double)worldIn.field_73012_v.nextFloat() * 0.33D, (double)pos.func_177952_p() + 0.33D + (double)worldIn.field_73012_v.nextFloat() * 0.33D, 0.0D, 0.0D, 0.0D, Aspect.FLUX.getColor());
            }
         }
      }

      super.func_180663_b(worldIn, pos, state);
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      TileEntity te;
      if (state.func_177230_c() == BlocksTC.tubeValve) {
         if (heldItem != null && (heldItem.func_77973_b() instanceof ICaster || heldItem.func_77973_b() instanceof ItemResonator || heldItem.func_77973_b() == Item.func_150898_a(this))) {
            return false;
         }

         te = world.func_175625_s(pos);
         if (te instanceof TileTubeValve) {
            ((TileTubeValve)te).allowFlow = !((TileTubeValve)te).allowFlow;
            world.markAndNotifyBlock(pos, world.func_175726_f(pos), state, state, 3);
            te.func_70296_d();
            if (!world.field_72995_K) {
               world.func_184133_a((EntityPlayer)null, pos, SoundsTC.squeek, SoundCategory.BLOCKS, 0.7F, 0.9F + world.field_73012_v.nextFloat() * 0.2F);
            }

            return true;
         }
      }

      if (state.func_177230_c() == BlocksTC.tubeFilter) {
         te = world.func_175625_s(pos);
         if (te != null && te instanceof TileTubeFilter && player.func_70093_af() && ((TileTubeFilter)te).aspectFilter != null) {
            ((TileTubeFilter)te).aspectFilter = null;
            world.markAndNotifyBlock(pos, world.func_175726_f(pos), state, state, 3);
            te.func_70296_d();
            if (world.field_72995_K) {
               world.func_184134_a((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p(), SoundsTC.key, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            return true;
         }

         if (te != null && te instanceof TileTubeFilter && heldItem != null && ((TileTubeFilter)te).aspectFilter == null && heldItem.func_77973_b() instanceof IEssentiaContainerItem) {
            if (((IEssentiaContainerItem)((IEssentiaContainerItem)heldItem.func_77973_b())).getAspects(heldItem) != null) {
               ((TileTubeFilter)te).aspectFilter = ((IEssentiaContainerItem)((IEssentiaContainerItem)heldItem.func_77973_b())).getAspects(heldItem).getAspects()[0];
               world.markAndNotifyBlock(pos, world.func_175726_f(pos), state, state, 3);
               te.func_70296_d();
               if (world.field_72995_K) {
                  world.func_184134_a((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p(), SoundsTC.key, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
               }
            }

            return true;
         }
      }

      return super.func_180639_a(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public void onBlockHighlight(DrawBlockHighlightEvent event) {
      if (event.getTarget().field_72313_a == Type.BLOCK && event.getPlayer().field_70170_p.func_180495_p(event.getTarget().func_178782_a()).func_177230_c() == this && (InventoryUtils.isHoldingItem(event.getPlayer(), ICaster.class) != null || InventoryUtils.isHoldingItem(event.getPlayer(), ItemResonator.class) != null)) {
         RayTracer.retraceBlock(event.getPlayer().field_70170_p, event.getPlayer(), event.getTarget().func_178782_a());
      }

   }

   public RayTraceResult func_180636_a(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
      TileEntity tile = world.func_175625_s(pos);
      if (tile != null && (tile instanceof TileTube || tile instanceof TileTubeBuffer)) {
         List<IndexedCuboid6> cuboids = new LinkedList();
         if (tile instanceof TileTube) {
            ((TileTube)tile).addTraceableCuboids(cuboids);
         } else if (tile instanceof TileTubeBuffer) {
            ((TileTubeBuffer)tile).addTraceableCuboids(cuboids);
         }

         ArrayList<ExtendedMOP> list = new ArrayList();
         this.rayTracer.rayTraceCuboids(new Vector3(start), new Vector3(end), cuboids, new BlockCoord(pos), this, list);
         return list.size() > 0 ? (RayTraceResult)list.get(0) : super.func_180636_a(state, world, pos, start, end);
      } else {
         return super.func_180636_a(state, world, pos, start, end);
      }
   }
}
