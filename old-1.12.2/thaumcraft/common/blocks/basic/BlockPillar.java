package thaumcraft.common.blocks.basic;

import java.util.List;
import java.util.Random;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.BlockTC;

public class BlockPillar extends BlockTC {
   public static final PropertyEnum VARIANT = PropertyEnum.func_177709_a("type", BlockPillar.PillarType.class);
   public static final PropertyDirection FACING;
   private final Random rand = new Random();

   public BlockPillar() {
      super(Material.field_151576_e);
      this.func_149711_c(2.5F);
      this.func_149672_a(SoundType.field_185851_d);
      IBlockState bs = this.field_176227_L.func_177621_b();
      bs.func_177226_a(FACING, EnumFacing.NORTH);
      bs.func_177226_a(VARIANT, BlockPillar.PillarType.NORMAL);
      this.func_180632_j(bs);
   }

   @SideOnly(Side.CLIENT)
   public void func_149666_a(Item item, CreativeTabs tab, List list) {
      list.add(new ItemStack(item, 1, calcMeta(BlockPillar.PillarType.NORMAL, EnumFacing.NORTH)));
      list.add(new ItemStack(item, 1, calcMeta(BlockPillar.PillarType.ANCIENT, EnumFacing.NORTH)));
      list.add(new ItemStack(item, 1, calcMeta(BlockPillar.PillarType.ELDRITCH, EnumFacing.NORTH)));
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
   }

   public AxisAlignedBB func_180646_a(IBlockState state, World worldIn, BlockPos pos) {
      return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 2.0D, 1.0D);
   }

   public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState bs = this.field_176227_L.func_177621_b();
      bs.func_177226_a(FACING, placer.func_174811_aO());
      bs.func_177226_a(VARIANT, BlockPillar.PillarType.values()[meta / 4]);
      return bs;
   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      EnumFacing enumfacing = EnumFacing.func_176731_b(MathHelper.func_76128_c((double)(placer.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3).func_176734_d();
      state = state.func_177226_a(FACING, enumfacing);
      state = state.func_177226_a(VARIANT, BlockPillar.PillarType.values()[stack.func_77952_i() / 4]);
      worldIn.func_180501_a(pos, state, 3);
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return Item.func_150899_d(0);
   }

   public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
      int t = ((BlockPillar.PillarType)state.func_177229_b(VARIANT)).ordinal();
      if (t == 0) {
         func_180635_a(worldIn, pos, new ItemStack(BlocksTC.stone, 2, 0));
      }

      if (t == 1) {
         func_180635_a(worldIn, pos, new ItemStack(BlocksTC.stone, 2, 2));
      }

      if (t == 2) {
         func_180635_a(worldIn, pos, new ItemStack(BlocksTC.stone, 2, 4));
      }

      super.func_180663_b(worldIn, pos, state);
   }

   public IBlockState func_176203_a(int meta) {
      EnumFacing enumfacing = EnumFacing.func_176731_b(meta);
      return this.func_176194_O().func_177621_b().func_177226_a(FACING, enumfacing).func_177226_a(VARIANT, BlockPillar.PillarType.values()[(meta - enumfacing.func_176736_b()) / 4]);
   }

   public static int calcMeta(BlockPillar.PillarType type, EnumFacing enumfacing) {
      if (enumfacing.func_176740_k() == Axis.Y) {
         enumfacing = EnumFacing.NORTH;
      }

      IBlockState state = BlocksTC.pillar.func_176194_O().func_177621_b();
      return BlocksTC.pillar.func_176201_c(state.func_177226_a(FACING, enumfacing).func_177226_a(VARIANT, type));
   }

   public int func_176201_c(IBlockState state) {
      int baseMeta = ((BlockPillar.PillarType)state.func_177229_b(VARIANT)).ordinal();
      return ((EnumFacing)state.func_177229_b(FACING)).func_176736_b() + baseMeta * 4;
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{FACING, VARIANT});
   }

   public String getTypeName(IBlockState state) {
      IStringSerializable type = (IStringSerializable)state.func_177229_b(VARIANT);
      return type.func_176610_l();
   }

   public IProperty[] getTypes() {
      return new IProperty[]{VARIANT};
   }

   public boolean hasTypes() {
      return true;
   }

   static {
      FACING = PropertyDirection.func_177712_a("facing", Plane.HORIZONTAL);
   }

   public static enum PillarType implements IStringSerializable {
      NORMAL,
      ANCIENT,
      ELDRITCH;

      public String func_176610_l() {
         return this.name().toLowerCase();
      }

      public String toString() {
         return this.func_176610_l();
      }
   }
}
