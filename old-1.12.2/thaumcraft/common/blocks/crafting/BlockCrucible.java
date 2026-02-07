package thaumcraft.common.blocks.crafting;

import java.util.List;
import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.blocks.BlockTCTile;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.items.casters.ICaster;
import thaumcraft.common.tiles.crafting.TileCrucible;

public class BlockCrucible extends BlockTCTile {
   private int delay = 0;
   protected static final AxisAlignedBB AABB_LEGS = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D);
   protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
   protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
   protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
   protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

   public BlockCrucible() {
      super(Material.field_151573_f, TileCrucible.class);
      this.func_149672_a(SoundType.field_185852_e);
   }

   public void func_180634_a(World world, BlockPos pos, IBlockState state, Entity entity) {
      if (!world.field_72995_K) {
         TileCrucible tile = (TileCrucible)world.func_175625_s(pos);
         if (tile != null && entity instanceof EntityItem && !(entity instanceof EntitySpecialItem) && tile.heat > 150 && tile.tank.getFluidAmount() > 0) {
            tile.attemptSmelt((EntityItem)entity);
         } else {
            ++this.delay;
            if (this.delay < 10) {
               return;
            }

            this.delay = 0;
            if (entity instanceof EntityLivingBase && tile != null && tile.heat > 150 && tile.tank.getFluidAmount() > 0) {
               entity.func_70097_a(DamageSource.field_76372_a, 1.0F);
               world.func_184148_a((EntityPlayer)null, (double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + 0.5D, (double)pos.func_177952_p() + 0.5D, SoundEvents.field_187659_cY, SoundCategory.BLOCKS, 0.4F, 2.0F + world.field_73012_v.nextFloat() * 0.4F);
            }
         }
      }

      super.func_180634_a(world, pos, state, entity);
   }

   public void func_185477_a(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB AABB, List<AxisAlignedBB> list, Entity p_185477_6_) {
      func_185492_a(pos, AABB, list, AABB_LEGS);
      func_185492_a(pos, AABB, list, AABB_WALL_WEST);
      func_185492_a(pos, AABB, list, AABB_WALL_NORTH);
      func_185492_a(pos, AABB, list, AABB_WALL_EAST);
      func_185492_a(pos, AABB, list, AABB_WALL_SOUTH);
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return field_185505_j;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
      TileEntity te = worldIn.func_175625_s(pos);
      if (te != null && te instanceof TileCrucible) {
         ((TileCrucible)te).spillRemnants();
      }

      super.func_180663_b(worldIn, pos, state);
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (world.field_72995_K) {
         return true;
      } else {
         FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(player.field_71071_by.func_70448_g());
         TileEntity te;
         TileCrucible tile;
         ItemStack emptyContainer;
         if (fs != null && fs.isFluidEqual(new FluidStack(FluidRegistry.WATER, 1000))) {
            te = world.func_175625_s(pos);
            if (te != null && te instanceof TileCrucible) {
               tile = (TileCrucible)te;
               if (tile.tank.getFluidAmount() < tile.tank.getCapacity()) {
                  tile.fill(EnumFacing.UP, FluidContainerRegistry.getFluidForFilledItem(player.field_71071_by.func_70448_g()), true);
                  emptyContainer = null;
                  FluidContainerData[] fcs = FluidContainerRegistry.getRegisteredFluidContainerData();
                  FluidContainerData[] var16 = fcs;
                  int var17 = fcs.length;

                  for(int var18 = 0; var18 < var17; ++var18) {
                     FluidContainerData fcd = var16[var18];
                     if (fcd.filledContainer.func_77969_a(player.field_71071_by.func_70448_g())) {
                        emptyContainer = fcd.emptyContainer.func_77946_l();
                     }
                  }

                  player.field_71071_by.func_70298_a(player.field_71071_by.field_70461_c, 1);
                  if (emptyContainer != null && !player.field_71071_by.func_70441_a(emptyContainer)) {
                     player.func_71019_a(emptyContainer, false);
                  }

                  player.field_71069_bz.func_75142_b();
                  te.func_70296_d();
                  world.markAndNotifyBlock(pos, world.func_175726_f(pos), state, state, 3);
                  world.func_184148_a((EntityPlayer)null, (double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + 0.5D, (double)pos.func_177952_p() + 0.5D, SoundEvents.field_187615_H, SoundCategory.BLOCKS, 0.33F, 1.0F + (world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.3F);
                  return true;
               }
            }
         } else if (heldItem != null && !player.func_70093_af() && !(heldItem.func_77973_b() instanceof ICaster) && side == EnumFacing.UP) {
            te = world.func_175625_s(pos);
            if (te != null && te instanceof TileCrucible) {
               tile = (TileCrucible)te;
               emptyContainer = heldItem.func_77946_l();
               emptyContainer.field_77994_a = 1;
               if (tile.heat > 150 && tile.tank.getFluidAmount() > 0 && tile.attemptSmelt(emptyContainer, player.func_70005_c_()) == null) {
                  player.field_71071_by.func_70298_a(player.field_71071_by.field_70461_c, 1);
                  return true;
               }
            }
         } else if (heldItem == null && player.func_70093_af()) {
            te = world.func_175625_s(pos);
            if (te != null && te instanceof TileCrucible) {
               tile = (TileCrucible)te;
               tile.spillRemnants();
               return true;
            }
         }

         return super.func_180639_a(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
      }
   }

   public boolean func_149740_M(IBlockState state) {
      return true;
   }

   public int func_180641_l(IBlockState state, World world, BlockPos pos) {
      TileEntity te = world.func_175625_s(pos);
      if (te != null && te instanceof TileCrucible) {
         float var10000 = (float)((TileCrucible)te).aspects.visSize();
         ((TileCrucible)te).getClass();
         float r = var10000 / 1000.0F;
         return MathHelper.func_76141_d(r * 14.0F) + (((TileCrucible)te).aspects.visSize() > 0 ? 1 : 0);
      } else {
         return 0;
      }
   }

   @SideOnly(Side.CLIENT)
   public void func_180655_c(IBlockState state, World w, BlockPos pos, Random r) {
      if (r.nextInt(10) == 0) {
         TileEntity te = w.func_175625_s(pos);
         if (te != null && te instanceof TileCrucible && ((TileCrucible)te).tank.getFluidAmount() > 0 && ((TileCrucible)te).heat > 150) {
            w.func_184134_a((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p(), SoundEvents.field_187662_cZ, SoundCategory.BLOCKS, 0.1F + r.nextFloat() * 0.1F, 1.2F + r.nextFloat() * 0.2F, false);
         }
      }

   }
}
