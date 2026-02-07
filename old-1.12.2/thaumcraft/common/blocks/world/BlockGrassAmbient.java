package thaumcraft.common.blocks.world;

import java.util.Random;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.config.ConfigItems;

public class BlockGrassAmbient extends BlockGrass {
   public BlockGrassAmbient() {
      this.func_149647_a(ConfigItems.TABTC);
      this.func_149711_c(0.6F);
      this.func_149672_a(SoundType.field_185849_b);
   }

   @SideOnly(Side.CLIENT)
   public void func_180655_c(IBlockState state, World worldIn, BlockPos pos, Random rand) {
      worldIn.func_180494_b(pos);
      int i = worldIn.func_175642_b(EnumSkyBlock.SKY, pos.func_177984_a()) - worldIn.func_175657_ab();
      float f = worldIn.func_72929_e(1.0F);
      float f1 = f < 3.1415927F ? 0.0F : 6.2831855F;
      f += (f1 - f) * 0.2F;
      i = Math.round((float)i * MathHelper.func_76134_b(f));
      i = MathHelper.func_76125_a(i, 0, 15);
      if (4 + i * 2 < 1 + rand.nextInt(13)) {
         int x = MathHelper.func_76136_a(rand, -8, 8);
         int z = MathHelper.func_76136_a(rand, -8, 8);
         BlockPos pp = pos.func_177982_a(x, 5, z);

         for(int q = 0; q < 10 && pp.func_177956_o() > 50 && worldIn.func_180495_p(pp).func_177230_c() != Blocks.field_150349_c; ++q) {
            pp = pp.func_177977_b();
         }

         if (worldIn.func_180495_p(pp).func_177230_c() == Blocks.field_150349_c) {
            FXDispatcher.INSTANCE.drawWispyMotesOnBlock(pp.func_177984_a(), 400, -0.01F);
         }
      }

   }
}
