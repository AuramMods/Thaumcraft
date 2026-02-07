package thaumcraft.common.blocks.misc;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.BlockTC;
import thaumcraft.common.lib.SoundsTC;

public class BlockEffect extends BlockTC {
   public BlockEffect() {
      super(Material.field_151579_a, BlockEffect.EffType.class);
      this.func_149675_a(true);
      this.field_149781_w = 999.0F;
      this.func_149715_a(0.5F);
      this.func_149647_a((CreativeTabs)null);
   }

   public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
      if (state.func_177230_c() != this) {
         return super.getLightValue(state, world, pos);
      } else {
         return (BlockEffect.EffType)world.func_180495_p(pos).func_177229_b(this.TYPE) == BlockEffect.EffType.GLIMMER ? 15 : super.getLightValue(state, world, pos);
      }
   }

   public void func_180634_a(World world, BlockPos pos, IBlockState state, Entity entity) {
      PotionEffect pe;
      if ((BlockEffect.EffType)world.func_180495_p(pos).func_177229_b(this.TYPE) == BlockEffect.EffType.SHOCK) {
         entity.func_70097_a(DamageSource.field_76376_m, (float)(1 + world.field_73012_v.nextInt(2)));
         if (entity instanceof EntityLivingBase) {
            pe = new PotionEffect(MobEffects.field_76421_d, 20, 0, true, true);
            ((EntityLivingBase)entity).func_70690_d(pe);
         }

         if (!world.field_72995_K && world.field_73012_v.nextInt(100) == 0) {
            world.func_175698_g(pos);
         }
      } else if ((BlockEffect.EffType)world.func_180495_p(pos).func_177229_b(this.TYPE) == BlockEffect.EffType.SAPPING && !(entity instanceof IEldritchMob) && entity instanceof EntityLivingBase && !((EntityLivingBase)entity).func_70644_a(MobEffects.field_82731_v)) {
         pe = new PotionEffect(MobEffects.field_82731_v, 40, 0, true, true);
         ((EntityLivingBase)entity).func_70690_d(pe);
         PotionEffect pe1 = new PotionEffect(MobEffects.field_76421_d, 40, 1, true, true);
         ((EntityLivingBase)entity).func_70690_d(pe1);
         PotionEffect pe2 = new PotionEffect(MobEffects.field_76438_s, 40, 1, true, true);
         ((EntityLivingBase)entity).func_70690_d(pe2);
      }

   }

   public void func_180650_b(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      super.func_180650_b(worldIn, pos, state, rand);
      if (!worldIn.field_72995_K && ((BlockEffect.EffType)state.func_177229_b(this.TYPE) == BlockEffect.EffType.SAPPING || (BlockEffect.EffType)state.func_177229_b(this.TYPE) == BlockEffect.EffType.SHOCK)) {
         worldIn.func_175698_g(pos);
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_180655_c(IBlockState state, World w, BlockPos pos, Random r) {
      if ((BlockEffect.EffType)state.func_177229_b(this.TYPE) == BlockEffect.EffType.SAPPING || (BlockEffect.EffType)state.func_177229_b(this.TYPE) == BlockEffect.EffType.SHOCK) {
         float h = r.nextFloat() * 0.33F;
         if ((BlockEffect.EffType)state.func_177229_b(this.TYPE) == BlockEffect.EffType.SHOCK) {
            FXDispatcher.INSTANCE.spark((double)((float)pos.func_177958_n() + w.field_73012_v.nextFloat()), (double)((float)pos.func_177956_o() + 0.1515F + h / 2.0F), (double)((float)pos.func_177952_p() + w.field_73012_v.nextFloat()), 3.0F + h * 6.0F, 0.65F + w.field_73012_v.nextFloat() * 0.1F, 1.0F, 1.0F, 0.8F);
         } else {
            FXDispatcher.INSTANCE.spark((double)((float)pos.func_177958_n() + w.field_73012_v.nextFloat()), (double)((float)pos.func_177956_o() + 0.1515F + h / 2.0F), (double)((float)pos.func_177952_p() + w.field_73012_v.nextFloat()), 3.0F + h * 6.0F, 0.3F - w.field_73012_v.nextFloat() * 0.1F, 0.0F, 0.5F + w.field_73012_v.nextFloat() * 0.2F, 1.0F);
         }

         if (r.nextInt(50) == 0) {
            w.func_184134_a((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p(), SoundsTC.jacobs, SoundCategory.AMBIENT, 0.5F, 1.0F + (r.nextFloat() - r.nextFloat()) * 0.2F, false);
         }
      }

   }

   public boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos) {
      return true;
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.INVISIBLE;
   }

   public boolean func_176193_a(World worldIn, BlockPos pos, EnumFacing side, ItemStack stack) {
      return true;
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return null;
   }

   public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing o) {
      return false;
   }

   public boolean func_176205_b(IBlockAccess worldIn, BlockPos pos) {
      return true;
   }

   public AxisAlignedBB func_180646_a(IBlockState state, World worldIn, BlockPos pos) {
      return null;
   }

   public boolean func_176209_a(IBlockState state, boolean hitIfLiquid) {
      return false;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return Item.func_150899_d(0);
   }

   public static enum EffType implements IStringSerializable {
      SHOCK,
      SAPPING,
      GLIMMER;

      public String func_176610_l() {
         return this.name().toLowerCase();
      }

      public String toString() {
         return this.func_176610_l();
      }
   }
}
