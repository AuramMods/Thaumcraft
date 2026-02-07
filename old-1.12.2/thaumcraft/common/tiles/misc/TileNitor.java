package thaumcraft.common.tiles.misc;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.client.fx.FXDispatcher;

public class TileNitor extends TileEntity implements ITickable {
   int count = 0;

   public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
      return oldState.func_177230_c() != newState.func_177230_c();
   }

   public void func_73660_a() {
      if (this.field_145850_b.field_72995_K) {
         IBlockState state = this.field_145850_b.func_180495_p(this.func_174877_v());
         if (state.func_177230_c() == BlocksTC.nitor) {
            FXDispatcher.INSTANCE.drawNitorFlames((double)((float)this.field_174879_c.func_177958_n() + 0.5F) + this.field_145850_b.field_73012_v.nextGaussian() * 0.025D, (double)((float)this.field_174879_c.func_177956_o() + 0.45F) + this.field_145850_b.field_73012_v.nextGaussian() * 0.025D, (double)((float)this.field_174879_c.func_177952_p() + 0.5F) + this.field_145850_b.field_73012_v.nextGaussian() * 0.025D, this.field_145850_b.field_73012_v.nextGaussian() * 0.0025D, (double)this.field_145850_b.field_73012_v.nextFloat() * 0.06D, this.field_145850_b.field_73012_v.nextGaussian() * 0.0025D, state.func_177230_c().func_180659_g(state).field_76291_p, 0);
            if (this.count++ % 10 == 0) {
               FXDispatcher.INSTANCE.drawNitorCore((double)((float)this.field_174879_c.func_177958_n() + 0.5F), (double)((float)this.field_174879_c.func_177956_o() + 0.5F), (double)((float)this.field_174879_c.func_177952_p() + 0.5F), 0.0D, 0.0D, 0.0D);
            }
         }
      }

   }
}
