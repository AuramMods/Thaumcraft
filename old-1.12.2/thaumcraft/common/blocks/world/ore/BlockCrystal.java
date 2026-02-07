package thaumcraft.common.blocks.world.ore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
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
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.world.aura.AuraHandler;

public class BlockCrystal extends Block {
   public static final PropertyInteger SIZE = PropertyInteger.func_177719_a("size", 0, 3);
   public static final PropertyInteger GENERATION = PropertyInteger.func_177719_a("gen", 1, 4);
   public static final IUnlistedProperty<Boolean> NORTH = new PropertyAdapter(PropertyBool.func_177716_a("north"));
   public static final IUnlistedProperty<Boolean> EAST = new PropertyAdapter(PropertyBool.func_177716_a("east"));
   public static final IUnlistedProperty<Boolean> SOUTH = new PropertyAdapter(PropertyBool.func_177716_a("south"));
   public static final IUnlistedProperty<Boolean> WEST = new PropertyAdapter(PropertyBool.func_177716_a("west"));
   public static final IUnlistedProperty<Boolean> UP = new PropertyAdapter(PropertyBool.func_177716_a("up"));
   public static final IUnlistedProperty<Boolean> DOWN = new PropertyAdapter(PropertyBool.func_177716_a("down"));
   public Aspect aspect;

   public BlockCrystal(Aspect aspect) {
      super(Material.field_151592_s);
      this.aspect = aspect;
      this.func_149711_c(0.25F);
      this.func_149672_a(SoundsTC.CRYSTAL);
      this.func_149675_a(true);
      this.func_149647_a(ConfigItems.TABTC);
      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(SIZE, 0).func_177226_a(GENERATION, 1));
   }

   public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) {
      System.out.println(this.func_185467_w());
      return super.getSoundType(state, world, pos, entity);
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.MODEL;
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return Item.func_150899_d(0);
   }

   protected boolean func_149700_E() {
      return false;
   }

   public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      List<ItemStack> ret = new ArrayList();
      int count = this.getGrowth(state) + 1;

      for(int i = 0; i < count; ++i) {
         ret.add(ThaumcraftApiHelper.makeCrystal(this.aspect));
      }

      return ret;
   }

   public void func_180650_b(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if (!worldIn.field_72995_K && rand.nextInt(3 + this.getGeneration(state)) == 0) {
         int threshold = 25;
         int growth = this.getGrowth(state);
         int generation = this.getGeneration(state);
         if (this.aspect != Aspect.FLUX && AuraHelper.getVis(worldIn, pos) < (float)threshold) {
            if (growth > 0) {
               worldIn.func_175656_a(pos, state.func_177226_a(SIZE, growth - 1));
               AuraHelper.addVis(worldIn, pos, (float)threshold);
            } else if (BlockUtils.isBlockTouching(worldIn, pos, (Block)this)) {
               worldIn.func_175698_g(pos);
               AuraHandler.addVis(worldIn, pos, (float)threshold);
               AuraHandler.addFlux(worldIn, pos, 1.0F);
            }
         } else if (AuraHelper.getVis(worldIn, pos) >= (float)(AuraHandler.getAuraBase(worldIn, pos) + threshold)) {
            if (growth < 3 && (long)growth < (long)(5 - generation) + pos.func_177986_g() % 3L) {
               if (AuraHelper.drainVis(worldIn, pos, (float)threshold, false) > 0.0F) {
                  worldIn.func_175656_a(pos, state.func_177226_a(SIZE, growth + 1));
               }
            } else if (generation < 4) {
               BlockPos p2 = spreadCrystal(worldIn, pos);
               if (p2 != null && AuraHelper.drainVis(worldIn, pos, (float)threshold, false) > 0.0F) {
                  if (rand.nextInt(6) == 0) {
                     --generation;
                  }

                  worldIn.func_175656_a(p2, this.func_176223_P().func_177226_a(GENERATION, generation + 1));
                  AuraHandler.addFlux(worldIn, pos, 1.0F);
               }
            }
         } else if (this.aspect != Aspect.FLUX && AuraHelper.getFlux(worldIn, pos) > (float)threshold && AuraHelper.getFlux(worldIn, pos) > AuraHelper.getVis(worldIn, pos) && AuraHelper.getFlux(worldIn, pos) > (float)(AuraHandler.getAuraBase(worldIn, pos) / 2) && AuraHandler.drainFlux(worldIn, pos, (float)threshold, false) > 0.0F) {
            worldIn.func_175656_a(pos, BlocksTC.crystalTaint.func_176203_a(this.func_176201_c(state)));
         }
      }

   }

   public static BlockPos spreadCrystal(World world, BlockPos pos) {
      int xx = pos.func_177958_n() + world.field_73012_v.nextInt(3) - 1;
      int yy = pos.func_177956_o() + world.field_73012_v.nextInt(3) - 1;
      int zz = pos.func_177952_p() + world.field_73012_v.nextInt(3) - 1;
      BlockPos t = new BlockPos(xx, yy, zz);
      if (t.equals(pos)) {
         return null;
      } else {
         IBlockState bs = world.func_180495_p(t);
         Material bm = bs.func_185904_a();
         return !bm.func_76224_d() && (world.func_175623_d(t) || bs.func_177230_c().func_176200_f(world, t)) && world.field_73012_v.nextInt(16) == 0 && BlockUtils.isBlockTouching(world, t, Material.field_151576_e, true) ? t : null;
      }
   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
      if (!BlockUtils.isBlockTouching(worldIn, pos, Material.field_151576_e, true)) {
         this.func_176226_b(worldIn, pos, state, 0);
         worldIn.func_175698_g(pos);
      }

   }

   public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing o) {
      return false;
   }

   private boolean drawAt(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
      IBlockState fbs = worldIn.func_180495_p(pos);
      return fbs.func_185904_a() == Material.field_151576_e && fbs.func_177230_c().isSideSolid(fbs, worldIn, pos, side.func_176734_d());
   }

   public AxisAlignedBB func_185496_a(IBlockState bs, IBlockAccess iblockaccess, BlockPos pos) {
      IBlockState state = this.getExtendedState(bs, iblockaccess, pos);
      if (state instanceof IExtendedBlockState) {
         IExtendedBlockState es = (IExtendedBlockState)state;
         int c = 0;
         if ((Boolean)es.getValue(UP)) {
            ++c;
         }

         if ((Boolean)es.getValue(DOWN)) {
            ++c;
         }

         if ((Boolean)es.getValue(EAST)) {
            ++c;
         }

         if ((Boolean)es.getValue(WEST)) {
            ++c;
         }

         if ((Boolean)es.getValue(SOUTH)) {
            ++c;
         }

         if ((Boolean)es.getValue(NORTH)) {
            ++c;
         }

         if (c > 1) {
            return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
         }

         if ((Boolean)es.getValue(UP)) {
            return new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
         }

         if ((Boolean)es.getValue(DOWN)) {
            return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
         }

         if ((Boolean)es.getValue(EAST)) {
            return new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
         }

         if ((Boolean)es.getValue(WEST)) {
            return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D);
         }

         if ((Boolean)es.getValue(SOUTH)) {
            return new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D);
         }

         if ((Boolean)es.getValue(NORTH)) {
            return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
         }
      }

      return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
   }

   public AxisAlignedBB func_180646_a(IBlockState state, World worldIn, BlockPos pos) {
      return null;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
      IBlockState st = world.func_180495_p(pos);
      return 2 + (st.func_177230_c().func_176201_c(st) & 3) * 3;
   }

   public int func_185484_c(IBlockState state, IBlockAccess source, BlockPos pos) {
      int i = source.func_175626_b(pos, state.getLightValue(source, pos));
      int j = 180;
      int k = i & 255;
      int l = j & 255;
      int i1 = i >> 16 & 255;
      int j1 = j >> 16 & 255;
      return (k > l ? k : l) | (i1 > j1 ? i1 : j1) << 16;
   }

   protected BlockStateContainer func_180661_e() {
      IProperty[] listedProperties = new IProperty[]{SIZE, GENERATION};
      IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[]{UP, DOWN, NORTH, EAST, WEST, SOUTH};
      return new ExtendedBlockState(this, listedProperties, unlistedProperties);
   }

   public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
      if (state instanceof IExtendedBlockState) {
         IExtendedBlockState retval = (IExtendedBlockState)state;
         return retval.withProperty(UP, this.drawAt(world, pos.func_177984_a(), EnumFacing.UP)).withProperty(DOWN, this.drawAt(world, pos.func_177977_b(), EnumFacing.DOWN)).withProperty(NORTH, this.drawAt(world, pos.func_177978_c(), EnumFacing.NORTH)).withProperty(EAST, this.drawAt(world, pos.func_177974_f(), EnumFacing.EAST)).withProperty(SOUTH, this.drawAt(world, pos.func_177968_d(), EnumFacing.SOUTH)).withProperty(WEST, this.drawAt(world, pos.func_177976_e(), EnumFacing.WEST));
      } else {
         return state;
      }
   }

   public IBlockState func_176221_a(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      return state;
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(SIZE, meta & 3).func_177226_a(GENERATION, 1 + (meta >> 2));
   }

   public int func_176201_c(IBlockState state) {
      int i = 0;
      int i = i | (Integer)state.func_177229_b(SIZE);
      i |= (Integer)state.func_177229_b(GENERATION) - 1 << 2;
      return i;
   }

   public int getGrowth(IBlockState state) {
      return this.func_176201_c(state) & 3;
   }

   public int getGeneration(IBlockState state) {
      return 1 + (this.func_176201_c(state) >> 2);
   }

   @SideOnly(Side.CLIENT)
   public void func_149666_a(Item itemIn, CreativeTabs tab, List list) {
      list.add(new ItemStack(itemIn, 1, 0));
   }

   public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
      return false;
   }

   public boolean func_176196_c(World worldIn, BlockPos pos) {
      return BlockUtils.isBlockTouching(worldIn, pos, Material.field_151576_e, true) && super.func_176196_c(worldIn, pos);
   }
}
