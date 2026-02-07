package thaumcraft.common.blocks.basic;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.IBlockTypes;
import thaumcraft.common.config.ConfigItems;

public class BlockWoodSlabTC extends BlockSlab implements IBlockTypes {
   public static final PropertyEnum VARIANT = PropertyEnum.func_177709_a("type", BlockWoodSlabTC.PlankType.class);

   public BlockWoodSlabTC() {
      super(Material.field_151575_d);
      IBlockState iblockstate = this.field_176227_L.func_177621_b();
      if (!this.func_176552_j()) {
         iblockstate = iblockstate.func_177226_a(field_176554_a, EnumBlockHalf.BOTTOM);
         this.func_149647_a(ConfigItems.TABTC);
      }

      this.func_149672_a(SoundType.field_185848_a);
      this.func_180632_j(iblockstate.func_177226_a(VARIANT, BlockWoodSlabTC.PlankType.GREATWOOD));
      this.field_149783_u = !this.func_176552_j();
   }

   public String getTypeName(IBlockState state) {
      IStringSerializable type = (IStringSerializable)state.func_177229_b(VARIANT);
      return type.func_176610_l();
   }

   public boolean hasTypes() {
      return true;
   }

   public IProperty[] getTypes() {
      return new IProperty[]{VARIANT};
   }

   public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
      return 20;
   }

   public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
      return 5;
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return Item.func_150898_a(BlocksTC.slabWood);
   }

   @SideOnly(Side.CLIENT)
   public ItemStack func_185473_a(World worldIn, BlockPos pos, IBlockState state) {
      return new ItemStack(BlocksTC.slabWood, 1, this.func_180651_a(state));
   }

   public String func_150002_b(int meta) {
      return this.func_149739_a();
   }

   public IProperty func_176551_l() {
      return VARIANT;
   }

   public Comparable<?> func_185674_a(ItemStack stack) {
      return (BlockWoodSlabTC.PlankType)this.func_176203_a(stack.func_77960_j() & 7).func_177229_b(VARIANT);
   }

   @SideOnly(Side.CLIENT)
   public void func_149666_a(Item itemIn, CreativeTabs tab, List list) {
      if (itemIn != Item.func_150898_a(BlocksTC.doubleSlabWood)) {
         BlockWoodSlabTC.PlankType[] aenumtype = BlockWoodSlabTC.PlankType.values();
         int i = aenumtype.length;

         for(int j = 0; j < i; ++j) {
            BlockWoodSlabTC.PlankType enumtype = aenumtype[j];
            list.add(new ItemStack(itemIn, 1, enumtype.ordinal()));
         }
      }

   }

   public IBlockState func_176203_a(int meta) {
      IBlockState iblockstate = this.func_176223_P().func_177226_a(VARIANT, BlockWoodSlabTC.PlankType.values()[meta & 7]);
      if (!this.func_176552_j()) {
         iblockstate = iblockstate.func_177226_a(field_176554_a, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
      }

      return iblockstate;
   }

   public int func_176201_c(IBlockState state) {
      byte b0 = 0;
      int i = b0 | ((BlockWoodSlabTC.PlankType)state.func_177229_b(VARIANT)).ordinal();
      if (!this.func_176552_j() && state.func_177229_b(field_176554_a) == EnumBlockHalf.TOP) {
         i |= 8;
      }

      return i;
   }

   protected BlockStateContainer func_180661_e() {
      return this.func_176552_j() ? new BlockStateContainer(this, new IProperty[]{VARIANT}) : new BlockStateContainer(this, new IProperty[]{field_176554_a, VARIANT});
   }

   public int func_180651_a(IBlockState state) {
      return ((BlockWoodSlabTC.PlankType)state.func_177229_b(VARIANT)).ordinal();
   }

   public boolean func_176552_j() {
      return this == BlocksTC.doubleSlabWood;
   }

   public boolean isType(IBlockState state, IStringSerializable type) {
      return false;
   }

   public static enum PlankType implements IStringSerializable {
      GREATWOOD,
      SILVERWOOD;

      public String func_176610_l() {
         return this.name().toLowerCase();
      }

      public String toString() {
         return this.func_176610_l();
      }
   }
}
