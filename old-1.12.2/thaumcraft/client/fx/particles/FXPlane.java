package thaumcraft.client.fx.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXPlane extends Particle {
   float angle;
   float angleYaw;
   float anglePitch;

   public FXPlane(World world, double d, double d1, double d2, double m, double m1, double m2, int life) {
      super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
      this.field_70552_h = 1.0F;
      this.field_70553_i = 1.0F;
      this.field_70551_j = 1.0F;
      this.field_70545_g = 0.0F;
      this.field_187129_i = this.field_187130_j = this.field_187131_k = 0.0D;
      this.field_70547_e = life;
      this.func_187115_a(0.01F, 0.01F);
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      this.field_70544_f = 1.0F;
      this.field_82339_as = 0.0F;
      double dx = m - this.field_187126_f;
      double dy = m1 - this.field_187127_g;
      double dz = m2 - this.field_187128_h;
      this.field_187129_i = dx / (double)this.field_70547_e;
      this.field_187130_j = dy / (double)this.field_70547_e;
      this.field_187131_k = dz / (double)this.field_70547_e;
      this.field_94054_b = 22;
      this.field_94055_c = 10;
      double d3 = (double)MathHelper.func_76133_a(dx * dx + dz * dz);
      this.angleYaw = 0.0F;
      this.anglePitch = 0.0F;
      if (d3 >= 1.0E-7D) {
         this.angleYaw = (float)(MathHelper.func_181159_b(dz, dx) * 180.0D / 3.141592653589793D) - 90.0F;
         this.anglePitch = (float)(-(MathHelper.func_181159_b(dy, d3) * 180.0D / 3.141592653589793D));
      }

      this.angle = (float)(this.field_187136_p.nextGaussian() * 20.0D);
   }

   public int func_70537_b() {
      return 0;
   }

   public void func_180434_a(VertexBuffer wr, Entity p_180434_2_, float f, float f1, float f2, float f3, float f4, float f5) {
      Tessellator.func_178181_a().func_78381_a();
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, this.field_82339_as / 2.0F);
      float var13 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)f - field_70556_an);
      float var14 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)f - field_70554_ao);
      float var15 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)f - field_70555_ap);
      GL11.glTranslated((double)var13, (double)var14, (double)var15);
      GL11.glRotatef(-this.angleYaw + 90.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(this.anglePitch + 90.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(this.angle, 0.0F, 1.0F, 0.0F);
      this.field_94054_b = 22 + Math.round(((float)this.field_70546_d + f) / (float)this.field_70547_e * 8.0F);
      float var8 = (float)this.field_94054_b / 32.0F;
      float var9 = var8 + 0.03125F;
      float var10 = (float)this.field_94055_c / 32.0F;
      float var11 = var10 + 0.03125F;
      float var12 = this.field_70544_f * (0.5F + ((float)this.field_70546_d + f) / (float)this.field_70547_e);
      float var16 = 1.0F;
      int i = 240;
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      GL11.glDisable(2884);
      wr.func_181668_a(7, DefaultVertexFormats.field_181704_d);
      wr.func_181662_b(-0.5D * (double)var12, 0.5D * (double)var12, 0.0D).func_187315_a((double)var9, (double)var11).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, this.field_82339_as / 2.0F).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b(0.5D * (double)var12, 0.5D * (double)var12, 0.0D).func_187315_a((double)var9, (double)var10).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, this.field_82339_as / 2.0F).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b(0.5D * (double)var12, -0.5D * (double)var12, 0.0D).func_187315_a((double)var8, (double)var10).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, this.field_82339_as / 2.0F).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b(-0.5D * (double)var12, -0.5D * (double)var12, 0.0D).func_187315_a((double)var8, (double)var11).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, this.field_82339_as / 2.0F).func_187314_a(j, k).func_181675_d();
      Tessellator.func_178181_a().func_78381_a();
      GL11.glEnable(2884);
      GL11.glPopMatrix();
      wr.func_181668_a(7, DefaultVertexFormats.field_181704_d);
   }

   public void func_189213_a() {
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      float threshold = (float)this.field_70547_e / 5.0F;
      if ((float)this.field_70546_d <= threshold) {
         this.field_82339_as = (float)this.field_70546_d / threshold;
      } else {
         this.field_82339_as = (float)(this.field_70547_e - this.field_70546_d) / (float)this.field_70547_e;
      }

      if (this.field_70546_d++ >= this.field_70547_e) {
         this.func_187112_i();
      }

      this.field_187126_f += this.field_187129_i;
      this.field_187127_g += this.field_187130_j;
      this.field_187128_h += this.field_187131_k;
   }

   public void setGravity(float value) {
      this.field_70545_g = value;
   }
}
