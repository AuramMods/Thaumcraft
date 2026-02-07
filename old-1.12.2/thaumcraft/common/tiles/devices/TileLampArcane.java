package thaumcraft.common.tiles.devices;

import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileLampArcane extends TileThaumcraft implements ITickable {
   public int rad;
   public int rad1 = 0;

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K && this.field_145850_b.func_82737_E() % 5L == 0L && !this.gettingPower()) {
         int x = this.field_145850_b.field_73012_v.nextInt(16) - this.field_145850_b.field_73012_v.nextInt(16);
         int y = this.field_145850_b.field_73012_v.nextInt(16) - this.field_145850_b.field_73012_v.nextInt(16);
         int z = this.field_145850_b.field_73012_v.nextInt(16) - this.field_145850_b.field_73012_v.nextInt(16);
         BlockPos bp = this.field_174879_c.func_177982_a(x, y, z);
         if (bp.func_177956_o() > this.field_145850_b.func_175725_q(bp).func_177956_o() + 4) {
            bp = this.field_145850_b.func_175725_q(bp).func_177981_b(4);
         }

         if (bp.func_177956_o() < 5) {
            bp = new BlockPos(bp.func_177958_n(), 5, bp.func_177952_p());
         }

         if (this.field_145850_b.func_175623_d(bp) && this.field_145850_b.func_180495_p(bp) != BlocksTC.effect.func_176203_a(2) && this.field_145850_b.func_175642_b(EnumSkyBlock.BLOCK, bp) < 11 && BlockUtils.hasLOS(this.func_145831_w(), this.func_174877_v(), bp)) {
            this.field_145850_b.func_180501_a(bp, BlocksTC.effect.func_176203_a(2), 3);
         }
      }

   }

   public void removeLights() {
      for(int x = -15; x <= 15; ++x) {
         for(int y = -15; y <= 15; ++y) {
            for(int z = -15; z <= 15; ++z) {
               BlockPos bp = this.field_174879_c.func_177982_a(x, y, z);
               if (this.field_145850_b.func_180495_p(bp) == BlocksTC.effect.func_176203_a(2)) {
                  this.field_145850_b.func_175698_g(bp);
               }
            }
         }
      }

   }
}
