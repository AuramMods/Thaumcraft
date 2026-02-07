package thaumcraft.common.blocks.world.plants;

import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.blocks.IBlockTypes;
import thaumcraft.common.config.ConfigItems;

public class BlockLeavesTC extends BlockLeaves implements IBlockTypes {
   public static final PropertyEnum TYPE = PropertyEnum.func_177708_a("type", BlockLogsTC.LogType.class, new Predicate() {
      public boolean apply(BlockLogsTC.LogType type) {
         return type.ordinal() < 4;
      }

      public boolean apply(Object p_apply_1_) {
         return this.apply((BlockLogsTC.LogType)p_apply_1_);
      }
   });

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

   public BlockLeavesTC() {
      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(TYPE, BlockLogsTC.LogType.GREATWOOD).func_177226_a(field_176236_b, true).func_177226_a(field_176237_a, true));
      this.func_149647_a(ConfigItems.TABTC);
   }

   public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
      return 60;
   }

   public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
      return 30;
   }

   @SideOnly(Side.CLIENT)
   public BlockRenderLayer func_180664_k() {
      return Blocks.field_150362_t.func_180664_k();
   }

   public boolean func_149662_c(IBlockState state) {
      return Blocks.field_150362_t.func_149662_c(state);
   }

   public boolean func_176225_a(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      this.func_150122_b(!this.func_149662_c(blockState));
      return super.func_176225_a(blockState, blockAccess, pos, side);
   }

   public MapColor func_180659_g(IBlockState state) {
      return this.func_180651_a(state) == 1 ? MapColor.field_151674_s : super.func_180659_g(state);
   }

   public int func_180651_a(IBlockState state) {
      return state.func_177230_c() == this ? ((BlockLogsTC.LogType)state.func_177229_b(TYPE)).ordinal() : 0;
   }

   @SideOnly(Side.CLIENT)
   public void func_149666_a(Item itemIn, CreativeTabs tab, List list) {
      list.add(new ItemStack(itemIn, 1, 0));
      list.add(new ItemStack(itemIn, 1, 1));
   }

   protected ItemStack func_180643_i(IBlockState state) {
      return new ItemStack(Item.func_150898_a(this), 1, ((BlockLogsTC.LogType)state.func_177229_b(TYPE)).ordinal());
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(TYPE, this.getWoodTCType(meta)).func_177226_a(field_176237_a, (meta & 4) == 0).func_177226_a(field_176236_b, (meta & 8) > 0);
   }

   public int func_176201_c(IBlockState state) {
      byte b0 = 0;
      int i = b0 | ((BlockLogsTC.LogType)state.func_177229_b(TYPE)).ordinal();
      if (!(Boolean)state.func_177229_b(field_176237_a)) {
         i |= 4;
      }

      if ((Boolean)state.func_177229_b(field_176236_b)) {
         i |= 8;
      }

      return i;
   }

   protected int func_176232_d(IBlockState state) {
      return ((BlockLogsTC.LogType)state.func_177229_b(TYPE)).ordinal() == 0 ? 44 : 200;
   }

   protected void func_176234_a(World worldIn, BlockPos pos, IBlockState state, int chance) {
      if (state.func_177229_b(TYPE) == BlockLogsTC.LogType.SILVERWOOD && worldIn.field_73012_v.nextInt((int)((double)chance * 0.75D)) == 0) {
         func_180635_a(worldIn, pos, new ItemStack(ItemsTC.nuggets, 1, 5));
      }

   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return Item.func_150898_a(BlocksTC.sapling);
   }

   public BlockLogsTC.LogType getWoodTCType(int meta) {
      return BlockLogsTC.LogType.values()[meta & 3];
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{TYPE, field_176236_b, field_176237_a});
   }

   public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
      IBlockState state = world.func_180495_p(pos);
      return new ArrayList(Arrays.asList(new ItemStack(this, 1, ((BlockLogsTC.LogType)state.func_177229_b(TYPE)).ordinal())));
   }

   public EnumType func_176233_b(int meta) {
      return null;
   }

   public boolean isType(IBlockState state, IStringSerializable type) {
      return false;
   }
}
