package thaumcraft.common.blocks.devices;

import java.util.Random;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.devices.TileHungryChest;

public class BlockHungryChest extends BlockContainer {
   public static final PropertyDirection FACING;
   private final Random rand = new Random();

   public BlockHungryChest() {
      super(Material.field_151575_d);
      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(FACING, EnumFacing.NORTH));
      this.func_149711_c(2.5F);
      this.func_149672_a(SoundType.field_185848_a);
      this.func_149647_a(ConfigItems.TABTC);
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

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
   }

   public AxisAlignedBB func_180646_a(IBlockState state, World worldIn, BlockPos pos) {
      return new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.func_176223_P().func_177226_a(FACING, placer.func_174811_aO());
   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      EnumFacing enumfacing = EnumFacing.func_176731_b(MathHelper.func_76128_c((double)(placer.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3).func_176734_d();
      state = state.func_177226_a(FACING, enumfacing);
      worldIn.func_180501_a(pos, state, 3);
   }

   public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
      TileEntity tileentity = worldIn.func_175625_s(pos);
      if (tileentity instanceof IInventory) {
         InventoryHelper.func_180175_a(worldIn, pos, (IInventory)tileentity);
         worldIn.func_175666_e(pos, this);
      }

      super.func_180663_b(worldIn, pos, state);
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (world.field_72995_K) {
         return true;
      } else {
         TileEntity tileentity = world.func_175625_s(pos);
         if (tileentity instanceof IInventory) {
            player.func_71007_a((IInventory)tileentity);
         }

         return true;
      }
   }

   public void func_180634_a(World world, BlockPos pos, IBlockState state, Entity entity) {
      Object var10 = (TileHungryChest)world.func_175625_s(pos);
      if (var10 != null) {
         if (!world.field_72995_K) {
            if (entity instanceof EntityItem && !entity.field_70128_L) {
               ItemStack leftovers = InventoryUtils.placeItemStackIntoInventory(((EntityItem)entity).func_92059_d(), (IInventory)var10, EnumFacing.UP, true);
               if (leftovers == null || leftovers.field_77994_a != ((EntityItem)entity).func_92059_d().field_77994_a) {
                  entity.func_184185_a(SoundEvents.field_187537_bA, 0.25F, (world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.2F + 1.0F);
               }

               if (leftovers != null) {
                  ((EntityItem)entity).func_92058_a(leftovers);
               } else {
                  entity.func_70106_y();
               }

               ((TileHungryChest)var10).func_70296_d();
            }

         }
      }
   }

   public boolean func_149740_M(IBlockState state) {
      return true;
   }

   public int func_180641_l(IBlockState state, World worldIn, BlockPos pos) {
      Object var10 = worldIn.func_175625_s(pos);
      return var10 instanceof TileHungryChest ? Container.func_94526_b((IInventory)var10) : 0;
   }

   public IBlockState func_176203_a(int meta) {
      EnumFacing enumfacing = EnumFacing.func_82600_a(meta);
      if (enumfacing.func_176740_k() == Axis.Y) {
         enumfacing = EnumFacing.NORTH;
      }

      return this.func_176223_P().func_177226_a(FACING, enumfacing);
   }

   public int func_176201_c(IBlockState state) {
      return ((EnumFacing)state.func_177229_b(FACING)).func_176745_a();
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{FACING});
   }

   public TileEntity func_149915_a(World par1World, int m) {
      return new TileHungryChest();
   }

   static {
      FACING = PropertyDirection.func_177712_a("facing", Plane.HORIZONTAL);
   }
}
