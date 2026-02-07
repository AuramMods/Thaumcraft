package thaumcraft.common.blocks.world.taint;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties.PropertyAdapter;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.potions.PotionFluxTaint;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.SoundsTC;

public class BlockTaintFibre extends Block implements ITaintBlock {
   public static final IUnlistedProperty<Boolean> NORTH = new PropertyAdapter(PropertyBool.func_177716_a("north"));
   public static final IUnlistedProperty<Boolean> EAST = new PropertyAdapter(PropertyBool.func_177716_a("east"));
   public static final IUnlistedProperty<Boolean> SOUTH = new PropertyAdapter(PropertyBool.func_177716_a("south"));
   public static final IUnlistedProperty<Boolean> WEST = new PropertyAdapter(PropertyBool.func_177716_a("west"));
   public static final IUnlistedProperty<Boolean> UP = new PropertyAdapter(PropertyBool.func_177716_a("up"));
   public static final IUnlistedProperty<Boolean> DOWN = new PropertyAdapter(PropertyBool.func_177716_a("down"));
   public static final IUnlistedProperty<Integer> GROWTH = new PropertyAdapter(PropertyInteger.func_177719_a("growth", 0, 4));
   protected static final AxisAlignedBB AABB_UP = new AxisAlignedBB(0.0D, 0.949999988079071D, 0.0D, 1.0D, 1.0D, 1.0D);
   protected static final AxisAlignedBB AABB_DOWN = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.05000000074505806D, 1.0D);
   protected static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.949999988079071D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
   protected static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.05000000074505806D, 1.0D, 1.0D);
   protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.949999988079071D, 1.0D, 1.0D, 1.0D);
   protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.05000000074505806D);

   public BlockTaintFibre() {
      super(ThaumcraftMaterials.MATERIAL_TAINT);
      this.func_149711_c(1.0F);
      this.func_149672_a(SoundsTC.GORE);
      this.func_149675_a(true);
      this.func_149647_a(ConfigItems.TABTC);
   }

   public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
      return 3;
   }

   public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
      return 3;
   }

   public MapColor func_180659_g(IBlockState state) {
      return MapColor.field_151678_z;
   }

   public void die(World world, BlockPos pos, IBlockState blockState) {
      world.func_175698_g(pos);
   }

   protected boolean func_149700_E() {
      return false;
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return Item.func_150899_d(0);
   }

   public void func_180653_a(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
      state = this.getExtendedState(state, worldIn, pos);
      if (state instanceof IExtendedBlockState && (Integer)((IExtendedBlockState)state).getValue(GROWTH) == 3) {
         if (worldIn.field_73012_v.nextInt(5) <= fortune) {
            func_180635_a(worldIn, pos, ConfigItems.FLUX_CRYSTAL.func_77946_l());
         }

         AuraHelper.polluteAura(worldIn, pos, 1.0F, true);
      }

   }

   public void func_180650_b(World world, BlockPos pos, IBlockState state, Random random) {
      if (!world.field_72995_K) {
         state = this.getExtendedState(state, world, pos);
         if (state instanceof IExtendedBlockState) {
            if ((Integer)((IExtendedBlockState)state).getValue(GROWTH) == 0 && isOnlyAdjacentToTaint(world, pos)) {
               this.die(world, pos, state);
            } else if (!TaintHelper.isNearTaintSeed(world, pos)) {
               this.die(world, pos, state);
            } else {
               TaintHelper.spreadFibres(world, pos);
            }
         }
      }

   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
      state = this.getExtendedState(state, worldIn, pos);
      if (state instanceof IExtendedBlockState && (Integer)((IExtendedBlockState)state).getValue(GROWTH) == 0 && isOnlyAdjacentToTaint(worldIn, pos)) {
         worldIn.func_175698_g(pos);
      }

   }

   public static int getAdjacentTaint(IBlockAccess world, BlockPos pos) {
      int count = 0;
      EnumFacing[] var3 = EnumFacing.field_82609_l;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumFacing dir = var3[var5];
         if (world.func_180495_p(pos.func_177972_a(dir)).func_185904_a() != ThaumcraftMaterials.MATERIAL_TAINT) {
            ++count;
         }
      }

      return count;
   }

   public static boolean isOnlyAdjacentToTaint(World world, BlockPos pos) {
      EnumFacing[] var2 = EnumFacing.field_82609_l;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumFacing dir = var2[var4];
         if (!world.func_175623_d(pos.func_177972_a(dir)) && world.func_180495_p(pos.func_177972_a(dir)).func_185904_a() != ThaumcraftMaterials.MATERIAL_TAINT && world.func_180495_p(pos.func_177972_a(dir)).func_177230_c().func_176212_b(world, pos.func_177972_a(dir), dir.func_176734_d())) {
            return false;
         }
      }

      return true;
   }

   public static boolean isHemmedByTaint(World world, BlockPos pos) {
      int c = 0;
      EnumFacing[] var3 = EnumFacing.field_82609_l;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumFacing dir = var3[var5];
         IBlockState block = world.func_180495_p(pos.func_177972_a(dir));
         if (block.func_185904_a() == ThaumcraftMaterials.MATERIAL_TAINT) {
            ++c;
         } else if (world.func_175623_d(pos.func_177972_a(dir))) {
            --c;
         } else if (!block.func_185904_a().func_76224_d() && !block.isSideSolid(world, pos.func_177972_a(dir), dir.func_176734_d())) {
            --c;
         }
      }

      return c > 0;
   }

   public void func_176199_a(World world, BlockPos pos, Entity entity) {
      if (!world.field_72995_K && entity instanceof EntityLivingBase && !((EntityLivingBase)entity).func_70662_br() && world.field_73012_v.nextInt(750) == 0) {
         ((EntityLivingBase)entity).func_70690_d(new PotionEffect(PotionFluxTaint.instance, 200, 0, false, true));
      }

   }

   public boolean func_189539_a(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam) {
      if (eventID == 1) {
         if (worldIn.field_72995_K) {
            worldIn.func_184133_a((EntityPlayer)null, pos, SoundsTC.roots, SoundCategory.BLOCKS, 0.1F, 0.9F + worldIn.field_73012_v.nextFloat() * 0.2F);
         }

         return true;
      } else {
         return super.func_189539_a(state, worldIn, pos, eventID, eventParam);
      }
   }

   @SideOnly(Side.CLIENT)
   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.CUTOUT;
   }

   public boolean func_176212_b(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
      return false;
   }

   private boolean drawAt(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
      Block b = worldIn.func_180495_p(pos).func_177230_c();
      return b != BlocksTC.taintFibre && b != BlocksTC.taintFeature && b.func_176212_b(worldIn, pos, side.func_176734_d());
   }

   public AxisAlignedBB func_185496_a(IBlockState s, IBlockAccess iblockaccess, BlockPos pos) {
      IBlockState state = this.getExtendedState(iblockaccess.func_180495_p(pos), iblockaccess, pos);
      if (state.func_177230_c() == this && state instanceof IExtendedBlockState) {
         int c = 0;
         if ((Boolean)((IExtendedBlockState)state).getValue(UP)) {
            ++c;
         }

         if ((Boolean)((IExtendedBlockState)state).getValue(DOWN)) {
            ++c;
         }

         if ((Boolean)((IExtendedBlockState)state).getValue(EAST)) {
            ++c;
         }

         if ((Boolean)((IExtendedBlockState)state).getValue(WEST)) {
            ++c;
         }

         if ((Boolean)((IExtendedBlockState)state).getValue(SOUTH)) {
            ++c;
         }

         if ((Boolean)((IExtendedBlockState)state).getValue(NORTH)) {
            ++c;
         }

         if (c > 1) {
            return field_185505_j;
         }

         if ((Integer)((IExtendedBlockState)state).getValue(GROWTH) == 1 || (Integer)((IExtendedBlockState)state).getValue(GROWTH) == 2) {
            return new AxisAlignedBB(0.20000000298023224D, 0.0D, 0.20000000298023224D, 0.800000011920929D, 0.800000011920929D, 0.800000011920929D);
         }

         if ((Integer)((IExtendedBlockState)state).getValue(GROWTH) == 3) {
            return new AxisAlignedBB(0.20000000298023224D, 0.0D, 0.20000000298023224D, 0.800000011920929D, 0.375D, 0.800000011920929D);
         }

         if ((Integer)((IExtendedBlockState)state).getValue(GROWTH) == 4) {
            return new AxisAlignedBB(0.20000000298023224D, 0.20000000298023224D, 0.20000000298023224D, 0.800000011920929D, 1.0D, 0.800000011920929D);
         }

         if ((Boolean)((IExtendedBlockState)state).getValue(UP)) {
            return new AxisAlignedBB(0.0D, 0.949999988079071D, 0.0D, 1.0D, 1.0D, 1.0D);
         }

         if ((Boolean)((IExtendedBlockState)state).getValue(DOWN)) {
            return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.05000000074505806D, 1.0D);
         }

         if ((Boolean)((IExtendedBlockState)state).getValue(EAST)) {
            return new AxisAlignedBB(0.949999988079071D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
         }

         if ((Boolean)((IExtendedBlockState)state).getValue(WEST)) {
            return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.05000000074505806D, 1.0D, 1.0D);
         }

         if ((Boolean)((IExtendedBlockState)state).getValue(SOUTH)) {
            return new AxisAlignedBB(0.0D, 0.0D, 0.949999988079071D, 1.0D, 1.0D, 1.0D);
         }

         if ((Boolean)((IExtendedBlockState)state).getValue(NORTH)) {
            return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.05000000074505806D);
         }
      }

      return super.func_185496_a(s, iblockaccess, pos);
   }

   public void func_185477_a(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
      if (this.drawAt(worldIn, pos.func_177984_a(), EnumFacing.UP)) {
         func_185492_a(pos, entityBox, collidingBoxes, AABB_UP);
      }

      if (this.drawAt(worldIn, pos.func_177977_b(), EnumFacing.DOWN)) {
         func_185492_a(pos, entityBox, collidingBoxes, AABB_DOWN);
      }

      if (this.drawAt(worldIn, pos.func_177974_f(), EnumFacing.EAST)) {
         func_185492_a(pos, entityBox, collidingBoxes, AABB_EAST);
      }

      if (this.drawAt(worldIn, pos.func_177976_e(), EnumFacing.WEST)) {
         func_185492_a(pos, entityBox, collidingBoxes, AABB_WEST);
      }

      if (this.drawAt(worldIn, pos.func_177968_d(), EnumFacing.SOUTH)) {
         func_185492_a(pos, entityBox, collidingBoxes, AABB_SOUTH);
      }

      if (this.drawAt(worldIn, pos.func_177978_c(), EnumFacing.NORTH)) {
         func_185492_a(pos, entityBox, collidingBoxes, AABB_NORTH);
      }

   }

   public boolean func_176200_f(IBlockAccess worldIn, BlockPos pos) {
      return true;
   }

   public boolean func_176205_b(IBlockAccess worldIn, BlockPos pos) {
      return true;
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

   public int getLightValue(IBlockState state2, IBlockAccess world, BlockPos pos) {
      IBlockState state = this.getExtendedState(world.func_180495_p(pos), world, pos);
      if (state.func_177230_c() == this && state instanceof IExtendedBlockState) {
         return (Integer)((IExtendedBlockState)state).getValue(GROWTH) == 3 ? 12 : ((Integer)((IExtendedBlockState)state).getValue(GROWTH) != 2 && (Integer)((IExtendedBlockState)state).getValue(GROWTH) != 4 ? super.getLightValue(state2, world, pos) : 6);
      } else {
         return super.getLightValue(state2, world, pos);
      }
   }

   public IBlockState func_176221_a(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      return state;
   }

   public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
      if (state.func_177230_c() == this && state instanceof IExtendedBlockState) {
         IExtendedBlockState retval = (IExtendedBlockState)state;
         boolean d = this.drawAt(world, pos.func_177977_b(), EnumFacing.DOWN);
         boolean u = this.drawAt(world, pos.func_177984_a(), EnumFacing.UP);
         int growth = 0;
         Random r = new Random(pos.func_177986_g());
         int q = r.nextInt(50);
         if (d) {
            if (q < 4) {
               growth = 1;
            } else if (q != 4 && q != 5) {
               if (q == 6) {
                  growth = 3;
               }
            } else {
               growth = 2;
            }
         }

         if (u && q > 47) {
            growth = 4;
         }

         return retval.withProperty(UP, this.drawAt(world, pos.func_177984_a(), EnumFacing.UP)).withProperty(DOWN, this.drawAt(world, pos.func_177977_b(), EnumFacing.DOWN)).withProperty(NORTH, this.drawAt(world, pos.func_177978_c(), EnumFacing.NORTH)).withProperty(EAST, this.drawAt(world, pos.func_177974_f(), EnumFacing.EAST)).withProperty(SOUTH, this.drawAt(world, pos.func_177968_d(), EnumFacing.SOUTH)).withProperty(WEST, this.drawAt(world, pos.func_177976_e(), EnumFacing.WEST)).withProperty(GROWTH, Integer.valueOf(growth));
      } else {
         return state;
      }
   }

   protected BlockStateContainer func_180661_e() {
      IProperty[] listedProperties = new IProperty[0];
      IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{UP, DOWN, NORTH, EAST, WEST, SOUTH, GROWTH};
      return new ExtendedBlockState(this, listedProperties, unlistedProperties);
   }
}
