package thaumcraft.common.blocks.world.plants;

import java.util.Random;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.config.ConfigItems;

public class BlockPlantVishroom extends BlockBush {
   public BlockPlantVishroom() {
      super(Material.field_151585_k);
      this.func_149647_a(ConfigItems.TABTC);
      this.func_149672_a(SoundType.field_185850_c);
      this.func_149715_a(0.4F);
   }

   public boolean func_149686_d(IBlockState state) {
      return state.func_185913_b();
   }

   public void func_180634_a(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
      if (!worldIn.field_72995_K && entityIn instanceof EntityLivingBase && worldIn.field_73012_v.nextInt(5) == 0) {
         ((EntityLivingBase)entityIn).func_70690_d(new PotionEffect(MobEffects.field_76431_k, 200, 0));
      }

   }

   public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
      return EnumPlantType.Cave;
   }

   @SideOnly(Side.CLIENT)
   public void func_180655_c(IBlockState state, World world, BlockPos pos, Random rand) {
      if (rand.nextInt(3) == 0) {
         float xr = (float)pos.func_177958_n() + 0.5F + (rand.nextFloat() - rand.nextFloat()) * 0.4F;
         float yr = (float)pos.func_177956_o() + 0.3F;
         float zr = (float)pos.func_177952_p() + 0.5F + (rand.nextFloat() - rand.nextFloat()) * 0.4F;
         FXDispatcher.INSTANCE.drawWispyMotes((double)xr, (double)yr, (double)zr, 0.0D, 0.0D, 0.0D, 10, 0.5F, 0.3F, 0.8F, 0.001F);
      }

   }
}
