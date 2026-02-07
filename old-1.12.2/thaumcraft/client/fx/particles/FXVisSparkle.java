package thaumcraft.client.fx.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXVisSparkle extends Particle {
   private double targetX;
   private double targetY;
   private double targetZ;
   float sizeMod = 0.0F;

   public FXVisSparkle(World par1World, double par2, double par4, double par6, double tx, double ty, double tz) {
      super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
      this.field_70552_h = this.field_70553_i = this.field_70551_j = 0.6F;
      this.field_70544_f = 0.0F;
      this.targetX = tx;
      this.targetY = ty;
      this.targetZ = tz;
      this.field_70547_e = 1000;
      float f3 = 0.01F;
      this.field_187129_i = (double)((float)this.field_187136_p.nextGaussian() * f3);
      this.field_187130_j = (double)((float)this.field_187136_p.nextGaussian() * f3);
      this.field_187131_k = (double)((float)this.field_187136_p.nextGaussian() * f3);
      this.sizeMod = (float)(45 + this.field_187136_p.nextInt(15));
      this.field_70552_h = 0.2F;
      this.field_70553_i = 0.6F + this.field_187136_p.nextFloat() * 0.3F;
      this.field_70551_j = 0.2F;
      this.field_70545_g = 0.2F;
   }

   public void func_180434_a(VertexBuffer wr, Entity p_180434_2_, float f, float f1, float f2, float f3, float f4, float f5) {
      float bob = MathHelper.func_76126_a((float)this.field_70546_d / 3.0F) * 0.3F + 6.0F;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
      int part = this.field_70546_d % 16;
      float var8 = (float)part / 64.0F;
      float var9 = var8 + 0.015625F;
      float var10 = 0.125F;
      float var11 = var10 + 0.015625F;
      float var12 = 0.1F * this.field_70544_f * bob;
      float var13 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)f - field_70556_an);
      float var14 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)f - field_70554_ao);
      float var15 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)f - field_70555_ap);
      float var16 = 1.0F;
      int i = 240;
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      wr.func_181662_b((double)(var13 - f1 * var12 - f4 * var12), (double)(var14 - f2 * var12), (double)(var15 - f3 * var12 - f5 * var12)).func_187315_a((double)var9, (double)var11).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, 0.5F).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b((double)(var13 - f1 * var12 + f4 * var12), (double)(var14 + f2 * var12), (double)(var15 - f3 * var12 + f5 * var12)).func_187315_a((double)var9, (double)var10).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, 0.5F).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b((double)(var13 + f1 * var12 + f4 * var12), (double)(var14 + f2 * var12), (double)(var15 + f3 * var12 + f5 * var12)).func_187315_a((double)var8, (double)var10).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, 0.5F).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b((double)(var13 + f1 * var12 - f4 * var12), (double)(var14 - f2 * var12), (double)(var15 + f3 * var12 - f5 * var12)).func_187315_a((double)var8, (double)var11).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, 0.5F).func_187314_a(j, k).func_181675_d();
   }

   public void func_189213_a() {
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      this.func_187110_a(this.field_187129_i, this.field_187130_j, this.field_187131_k);
      this.field_187129_i *= 0.985D;
      this.field_187130_j *= 0.985D;
      this.field_187131_k *= 0.985D;
      double dx = this.targetX - this.field_187126_f;
      double dy = this.targetY - this.field_187127_g;
      double dz = this.targetZ - this.field_187128_h;
      double d13 = 0.10000000149011612D;
      double d11 = (double)MathHelper.func_76133_a(dx * dx + dy * dy + dz * dz);
      if (d11 < 2.0D) {
         this.field_70544_f *= 0.95F;
      }

      if (d11 < 0.2D) {
         this.field_70547_e = this.field_70546_d;
      }

      if (this.field_70546_d < 10) {
         this.field_70544_f = (float)this.field_70546_d / this.sizeMod;
      }

      dx /= d11;
      dy /= d11;
      dz /= d11;
      this.field_187129_i += dx * d13;
      this.field_187130_j += dy * d13;
      this.field_187131_k += dz * d13;
      this.field_187129_i = (double)MathHelper.func_76131_a((float)this.field_187129_i, -0.1F, 0.1F);
      this.field_187130_j = (double)MathHelper.func_76131_a((float)this.field_187130_j, -0.1F, 0.1F);
      this.field_187131_k = (double)MathHelper.func_76131_a((float)this.field_187131_k, -0.1F, 0.1F);
      if (this.field_70546_d++ >= this.field_70547_e) {
         this.func_187112_i();
      }

   }

   public void setGravity(float value) {
      this.field_70545_g = value;
   }
}
