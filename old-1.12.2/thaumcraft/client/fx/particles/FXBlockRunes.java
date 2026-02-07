package thaumcraft.client.fx.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXBlockRunes extends Particle {
   double ofx = 0.0D;
   double ofy = 0.0D;
   float rotation = 0.0F;
   int runeIndex = 0;

   public FXBlockRunes(World world, double d, double d1, double d2, float f1, float f2, float f3, int m) {
      super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
      if (f1 == 0.0F) {
         f1 = 1.0F;
      }

      this.rotation = (float)(this.field_187136_p.nextInt(4) * 90);
      this.field_70552_h = f1;
      this.field_70553_i = f2;
      this.field_70551_j = f3;
      this.field_70545_g = 0.0F;
      this.field_187129_i = this.field_187130_j = this.field_187131_k = 0.0D;
      this.field_70547_e = 3 * m;
      this.func_187115_a(0.01F, 0.01F);
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      this.runeIndex = (int)(Math.random() * 16.0D + 224.0D);
      this.ofx = (double)this.field_187136_p.nextFloat() * 0.2D;
      this.ofy = -0.3D + (double)this.field_187136_p.nextFloat() * 0.6D;
      this.field_70544_f = (float)(1.0D + this.field_187136_p.nextGaussian() * 0.10000000149011612D);
      this.field_82339_as = 0.0F;
   }

   public void func_180434_a(VertexBuffer wr, Entity p_180434_2_, float f, float f1, float f2, float f3, float f4, float f5) {
      Tessellator.func_178181_a().func_78381_a();
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, this.field_82339_as / 2.0F);
      float var13 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)f - field_70556_an);
      float var14 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)f - field_70554_ao);
      float var15 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)f - field_70555_ap);
      GL11.glTranslated((double)var13, (double)var14, (double)var15);
      GL11.glRotatef(this.rotation, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslated(this.ofx, this.ofy, -0.51D);
      float var8 = (float)(this.runeIndex % 16) / 64.0F;
      float var9 = var8 + 0.015625F;
      float var10 = 0.09375F;
      float var11 = var10 + 0.015625F;
      float var12 = 0.3F * this.field_70544_f;
      float var16 = 1.0F;
      wr.func_181668_a(7, DefaultVertexFormats.field_181704_d);
      int i = 240;
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      wr.func_181662_b(-0.5D * (double)var12, 0.5D * (double)var12, 0.0D).func_187315_a((double)var9, (double)var11).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, this.field_82339_as / 2.0F).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b(0.5D * (double)var12, 0.5D * (double)var12, 0.0D).func_187315_a((double)var9, (double)var10).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, this.field_82339_as / 2.0F).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b(0.5D * (double)var12, -0.5D * (double)var12, 0.0D).func_187315_a((double)var8, (double)var10).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, this.field_82339_as / 2.0F).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b(-0.5D * (double)var12, -0.5D * (double)var12, 0.0D).func_187315_a((double)var8, (double)var11).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, this.field_82339_as / 2.0F).func_187314_a(j, k).func_181675_d();
      Tessellator.func_178181_a().func_78381_a();
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

      this.field_187130_j -= 0.04D * (double)this.field_70545_g;
      this.field_187126_f += this.field_187129_i;
      this.field_187127_g += this.field_187130_j;
      this.field_187128_h += this.field_187131_k;
   }

   public void setGravity(float value) {
      this.field_70545_g = value;
   }
}
