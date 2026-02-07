package thaumcraft.client.fx.particles;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class FXGenericP2E extends FXGeneric {
   private Entity target;

   public FXGenericP2E(World world, double x, double y, double z, Entity target) {
      super(world, x, y, z, 0.0D, 0.0D, 0.0D);
      this.func_187115_a(0.1F, 0.1F);
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      this.target = target;
      double dx = target.field_70165_t - this.field_187126_f;
      double dy = target.field_70163_u - this.field_187127_g;
      double dz = target.field_70161_v - this.field_187128_h;
      int base = (int)(MathHelper.func_76133_a(dx * dx + dy * dy + dz * dz) * 5.0F);
      if (base < 1) {
         base = 1;
      }

      this.field_70547_e = base;
      this.field_187123_c = x;
      this.field_187124_d = y;
      this.field_187125_e = z;
      this.field_70548_b = 0.0F;
      this.field_70549_c = 0.0F;
      float f3 = 0.01F;
      this.field_187129_i = (double)((float)world.field_73012_v.nextGaussian() * f3);
      this.field_187130_j = (double)((float)world.field_73012_v.nextGaussian() * f3);
      this.field_187131_k = (double)((float)world.field_73012_v.nextGaussian() * f3);
      this.field_70545_g = 0.2F;
   }

   public void func_189213_a() {
      super.func_189213_a();
      double dx = this.target.field_70165_t - this.field_187126_f;
      double dy = this.target.field_70163_u - this.field_187127_g;
      double dz = this.target.field_70161_v - this.field_187128_h;
      double d13 = 0.3D;
      double d11 = (double)MathHelper.func_76133_a(dx * dx + dy * dy + dz * dz);
      if (d11 < 4.0D) {
         this.field_70544_f *= 0.9F;
         d13 = 0.6D;
      }

      if (d11 < 0.25D) {
         this.func_187112_i();
      }

      dx /= d11;
      dy /= d11;
      dz /= d11;
      this.field_187129_i += dx * d13;
      this.field_187130_j += dy * d13;
      this.field_187131_k += dz * d13;
      this.field_187129_i = (double)MathHelper.func_76131_a((float)this.field_187129_i, -0.35F, 0.35F);
      this.field_187130_j = (double)MathHelper.func_76131_a((float)this.field_187130_j, -0.35F, 0.35F);
      this.field_187131_k = (double)MathHelper.func_76131_a((float)this.field_187131_k, -0.35F, 0.35F);
   }
}
