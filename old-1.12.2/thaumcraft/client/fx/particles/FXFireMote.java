package thaumcraft.client.fx.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FXFireMote extends Particle {
   float baseScale = 0.0F;
   float baseAlpha = 1.0F;
   int glowlayer = 0;

   public FXFireMote(World worldIn, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float scale, int layer) {
      super(worldIn, x, y, z, 0.0D, 0.0D, 0.0D);
      float colorR = r;
      float colorG = g;
      float colorB = b;
      if ((double)r > 1.0D) {
         colorR = r / 255.0F;
      }

      if ((double)g > 1.0D) {
         colorG = g / 255.0F;
      }

      if ((double)b > 1.0D) {
         colorB = b / 255.0F;
      }

      this.glowlayer = layer;
      this.func_70538_b(colorR, colorG, colorB);
      this.field_70547_e = 16;
      this.field_70544_f = scale;
      this.baseScale = scale;
      this.field_187129_i = vx;
      this.field_187130_j = vy;
      this.field_187131_k = vz;
      this.field_190014_F = 6.2831855F;
      this.func_70536_a(7);
   }

   public void func_70536_a(int particleTextureIndex) {
      this.field_94054_b = particleTextureIndex % 64;
      this.field_94055_c = particleTextureIndex / 64;
   }

   public void func_180434_a(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
      float f = (float)this.field_94054_b / 64.0F;
      float f1 = f + 0.015625F;
      float f2 = (float)this.field_94055_c / 64.0F;
      float f3 = f2 + 0.015625F;
      float f4 = 0.1F * this.field_70544_f;
      if (this.field_187119_C != null) {
         f = this.field_187119_C.func_94209_e();
         f1 = this.field_187119_C.func_94212_f();
         f2 = this.field_187119_C.func_94206_g();
         f3 = this.field_187119_C.func_94210_h();
      }

      float f5 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)partialTicks - field_70556_an);
      float f6 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)partialTicks - field_70554_ao);
      float f7 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)partialTicks - field_70555_ap);
      int i = this.func_189214_a(partialTicks);
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      Vec3d[] avec3d = new Vec3d[]{new Vec3d((double)(-rotationX * f4 - rotationXY * f4), (double)(-rotationZ * f4), (double)(-rotationYZ * f4 - rotationXZ * f4)), new Vec3d((double)(-rotationX * f4 + rotationXY * f4), (double)(rotationZ * f4), (double)(-rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double)(rotationX * f4 + rotationXY * f4), (double)(rotationZ * f4), (double)(rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double)(rotationX * f4 - rotationXY * f4), (double)(-rotationZ * f4), (double)(rotationYZ * f4 - rotationXZ * f4))};
      if (this.field_190014_F != 0.0F) {
         float f8 = this.field_190014_F + (this.field_190014_F - this.field_190015_G) * partialTicks;
         float f9 = MathHelper.func_76134_b(f8 * 0.5F);
         float f10 = MathHelper.func_76126_a(f8 * 0.5F) * (float)field_190016_K.field_72450_a;
         float f11 = MathHelper.func_76126_a(f8 * 0.5F) * (float)field_190016_K.field_72448_b;
         float f12 = MathHelper.func_76126_a(f8 * 0.5F) * (float)field_190016_K.field_72449_c;
         Vec3d vec3d = new Vec3d((double)f10, (double)f11, (double)f12);

         for(int l = 0; l < 4; ++l) {
            avec3d[l] = vec3d.func_186678_a(2.0D * avec3d[l].func_72430_b(vec3d)).func_178787_e(avec3d[l].func_186678_a((double)(f9 * f9) - vec3d.func_72430_b(vec3d))).func_178787_e(vec3d.func_72431_c(avec3d[l]).func_186678_a((double)(2.0F * f9)));
         }
      }

      worldRendererIn.func_181662_b((double)f5 + avec3d[0].field_72450_a, (double)f6 + avec3d[0].field_72448_b, (double)f7 + avec3d[0].field_72449_c).func_187315_a((double)f1, (double)f3).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as * this.baseAlpha).func_187314_a(j, k).func_181675_d();
      worldRendererIn.func_181662_b((double)f5 + avec3d[1].field_72450_a, (double)f6 + avec3d[1].field_72448_b, (double)f7 + avec3d[1].field_72449_c).func_187315_a((double)f1, (double)f2).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as * this.baseAlpha).func_187314_a(j, k).func_181675_d();
      worldRendererIn.func_181662_b((double)f5 + avec3d[2].field_72450_a, (double)f6 + avec3d[2].field_72448_b, (double)f7 + avec3d[2].field_72449_c).func_187315_a((double)f, (double)f2).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as * this.baseAlpha).func_187314_a(j, k).func_181675_d();
      worldRendererIn.func_181662_b((double)f5 + avec3d[3].field_72450_a, (double)f6 + avec3d[3].field_72448_b, (double)f7 + avec3d[3].field_72449_c).func_187315_a((double)f, (double)f3).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, this.field_82339_as * this.baseAlpha).func_187314_a(j, k).func_181675_d();
   }

   public int func_189214_a(float pTicks) {
      return 255;
   }

   public int func_70537_b() {
      return this.glowlayer;
   }

   public void func_189213_a() {
      super.func_189213_a();
      if (this.field_187122_b.field_73012_v.nextInt(6) == 0) {
         ++this.field_70546_d;
      }

      if (this.field_70546_d >= this.field_70547_e) {
         this.func_187112_i();
      }

      float lifespan = (float)this.field_70546_d / (float)this.field_70547_e;
      this.field_70544_f = this.baseScale - this.baseScale * lifespan;
      this.baseAlpha = 1.0F - lifespan;
      this.field_190015_G = (float)(this.field_190014_F++);
   }
}
