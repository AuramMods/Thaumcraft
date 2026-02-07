package thaumcraft.common.blocks.world.plants;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.blocks.IBlockTypes;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.world.objects.WorldGenGreatwoodTrees;
import thaumcraft.common.world.objects.WorldGenSilverwoodTrees;

public class BlockSaplingTC extends BlockBush implements IGrowable, IBlockTypes {
   public static final PropertyEnum TYPE = PropertyEnum.func_177709_a("type", BlockLogsTC.LogType.class);
   public static final PropertyInteger STAGE = PropertyInteger.func_177719_a("stage", 0, 1);
   protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

   public BlockSaplingTC() {
      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(TYPE, BlockLogsTC.LogType.GREATWOOD).func_177226_a(STAGE, 0));
      this.func_149647_a(ConfigItems.TABTC);
      this.func_149672_a(SoundType.field_185850_c);
   }

   public String getTypeName(IBlockState state) {
      IStringSerializable type = (IStringSerializable)state.func_177229_b(TYPE);
      return type.func_176610_l();
   }

   public boolean hasTypes() {
      return true;
   }

   public IProperty[] getTypes() {
      return new IProperty[]{TYPE};
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return SAPLING_AABB;
   }

   public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
      return 60;
   }

   public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
      return 30;
   }

   public void func_180650_b(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if (!worldIn.field_72995_K) {
         super.func_180650_b(worldIn, pos, state, rand);
         if (worldIn.func_175671_l(pos.func_177984_a()) >= 9 && rand.nextInt(7) == 0) {
            this.grow(worldIn, pos, state, rand);
         }
      }

   }

   public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if ((Integer)state.func_177229_b(STAGE) == 0) {
         worldIn.func_180501_a(pos, state.func_177231_a(STAGE), 4);
      } else {
         this.generateTree(worldIn, pos, state, rand);
      }

   }

   public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if (TerrainGen.saplingGrowTree(worldIn, rand, pos)) {
         Object object = rand.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
         int i = 0;
         int j = 0;
         switch((BlockLogsTC.LogType)state.func_177229_b(TYPE)) {
         case GREATWOOD:
            object = new WorldGenGreatwoodTrees(true, false);
            break;
         case SILVERWOOD:
            object = new WorldGenSilverwoodTrees(true, 7, 4);
         }

         IBlockState iblockstate1 = Blocks.field_150350_a.func_176223_P();
         worldIn.func_180501_a(pos, iblockstate1, 4);
         if (!((WorldGenerator)object).func_180709_b(worldIn, rand, pos.func_177982_a(i, 0, j))) {
            worldIn.func_180501_a(pos, state, 4);
         }

      }
   }

   public int func_180651_a(IBlockState state) {
      return ((BlockLogsTC.LogType)state.func_177229_b(TYPE)).ordinal();
   }

   @SideOnly(Side.CLIENT)
   public void func_149666_a(Item itemIn, CreativeTabs tab, List list) {
      BlockLogsTC.LogType[] aenumtype = BlockLogsTC.LogType.values();
      int i = aenumtype.length;

      for(int j = 0; j < i; ++j) {
         BlockLogsTC.LogType enumtype = aenumtype[j];
         list.add(new ItemStack(itemIn, 1, enumtype.ordinal()));
      }

   }

   public boolean func_176473_a(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
      return true;
   }

   public boolean func_180670_a(World worldIn, Random rand, BlockPos pos, IBlockState state) {
      return (double)worldIn.field_73012_v.nextFloat() < 0.25D;
   }

   public void func_176474_b(World worldIn, Random rand, BlockPos pos, IBlockState state) {
      this.grow(worldIn, pos, state, rand);
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(TYPE, BlockLogsTC.LogType.values()[meta & 7]).func_177226_a(STAGE, (meta & 8) >> 3);
   }

   public int func_176201_c(IBlockState state) {
      byte b0 = 0;
      int i = b0 | ((BlockLogsTC.LogType)state.func_177229_b(TYPE)).ordinal();
      i |= (Integer)state.func_177229_b(STAGE) << 3;
      return i;
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{TYPE, STAGE});
   }

   public boolean isType(IBlockState state, IStringSerializable type) {
      return false;
   }

   static final class SwitchEnumType {
      static final int[] WOOD_TYPE_LOOKUP = new int[BlockLogsTC.LogType.values().length];

      static {
         try {
            WOOD_TYPE_LOOKUP[BlockLogsTC.LogType.GREATWOOD.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
         }

         try {
            WOOD_TYPE_LOOKUP[BlockLogsTC.LogType.SILVERWOOD.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
         }

      }
   }
}
