package thaumcraft.client.fx.other;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXBlockWard extends Particle {
   ResourceLocation[] tex1 = new ResourceLocation[15];
   EnumFacing side;
   int rotation = 0;
   float sx = 0.0F;
   float sy = 0.0F;
   float sz = 0.0F;

   public FXBlockWard(World world, double d, double d1, double d2, EnumFacing side, float f, float f1, float f2) {
      super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
      this.side = side;
      this.field_70545_g = 0.0F;
      this.field_187129_i = this.field_187130_j = this.field_187131_k = 0.0D;
      this.field_70547_e = 12 + this.field_187136_p.nextInt(5);
      this.func_187115_a(0.01F, 0.01F);
      this.field_187123_c = this.field_187126_f;
      this.field_187124_d = this.field_187127_g;
      this.field_187125_e = this.field_187128_h;
      this.field_70544_f = (float)(1.4D + this.field_187136_p.nextGaussian() * 0.30000001192092896D);
      this.rotation = this.field_187136_p.nextInt(360);
      this.sx = MathHelper.func_76131_a(f - 0.6F + this.field_187136_p.nextFloat() * 0.2F, -0.4F, 0.4F);
      this.sy = MathHelper.func_76131_a(f1 - 0.6F + this.field_187136_p.nextFloat() * 0.2F, -0.4F, 0.4F);
      this.sz = MathHelper.func_76131_a(f2 - 0.6F + this.field_187136_p.nextFloat() * 0.2F, -0.4F, 0.4F);
      if (side.func_82601_c() != 0) {
         this.sx = 0.0F;
      }

      if (side.func_96559_d() != 0) {
         this.sy = 0.0F;
      }

      if (side.func_82599_e() != 0) {
         this.sz = 0.0F;
      }

      for(int a = 0; a < 15; ++a) {
         this.tex1[a] = new ResourceLocation("thaumcraft", "textures/models/hemis" + (a + 1) + ".png");
      }

   }

   public void func_180434_a(VertexBuffer wr, Entity p_180434_2_, float f, float f1, float f2, float f3, float f4, float f5) {
      Tessellator.func_178181_a().func_78381_a();
      GL11.glPushMatrix();
      float fade = ((float)this.field_70546_d + f) / (float)this.field_70547_e;
      int frame = Math.min(15, (int)(15.0F * fade));
      ResourceLocation var10001 = this.tex1[frame - 1];
      Minecraft.func_71410_x().field_71446_o.func_110577_a(var10001);
      GL11.glDepthMask(false);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, this.field_82339_as / 2.0F);
      float var13 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)f - field_70556_an);
      float var14 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)f - field_70554_ao);
      float var15 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)f - field_70555_ap);
      GL11.glTranslated((double)(var13 + this.sx), (double)(var14 + this.sy), (double)(var15 + this.sz));
      GL11.glRotatef(90.0F, (float)this.side.func_96559_d(), (float)(-this.side.func_82601_c()), (float)this.side.func_82599_e());
      GL11.glRotatef((float)this.rotation, 0.0F, 0.0F, 1.0F);
      if (this.side.func_82599_e() > 0) {
         GL11.glTranslated(0.0D, 0.0D, 0.5049999952316284D);
         GL11.glRotatef(180.0F, 0.0F, -1.0F, 0.0F);
      } else {
         GL11.glTranslated(0.0D, 0.0D, -0.5049999952316284D);
      }

      float var12 = this.field_70544_f;
      float var16 = 1.0F;
      wr.func_181668_a(7, DefaultVertexFormats.field_181704_d);
      int i = 240;
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      wr.func_181662_b(-0.5D * (double)var12, 0.5D * (double)var12, 0.0D).func_187315_a(0.0D, 1.0D).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, this.field_82339_as / 2.0F).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b(0.5D * (double)var12, 0.5D * (double)var12, 0.0D).func_187315_a(1.0D, 1.0D).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, this.field_82339_as / 2.0F).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b(0.5D * (double)var12, -0.5D * (double)var12, 0.0D).func_187315_a(1.0D, 0.0D).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, this.field_82339_as / 2.0F).func_187314_a(j, k).func_181675_d();
      wr.func_181662_b(-0.5D * (double)var12, -0.5D * (double)var12, 0.0D).func_187315_a(0.0D, 0.0D).func_181666_a(this.field_70552_h * var16, this.field_70553_i * var16, this.field_70551_j * var16, this.field_82339_as / 2.0F).func_187314_a(j, k).func_181675_d();
      Tessellator.func_178181_a().func_78381_a();
      GL11.glDisable(3042);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
      Minecraft.func_71410_x().field_71446_o.func_110577_a(ParticleManager.field_110737_b);
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
