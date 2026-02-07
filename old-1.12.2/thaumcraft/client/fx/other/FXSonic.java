package thaumcraft.client.fx.other;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.obj.AdvancedModelLoader;
import thaumcraft.client.lib.obj.IModelCustom;

public class FXSonic extends Particle {
   Entity target = null;
   float yaw = 0.0F;
   float pitch = 0.0F;
   private IModelCustom model;
   private static final ResourceLocation MODEL = new ResourceLocation("thaumcraft", "models/obj/hemis.obj");

   public FXSonic(World world, double d, double d1, double d2, Entity target, int age) {
      super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
      this.field_70552_h = 1.0F;
      this.field_70553_i = 1.0F;
      this.field_70551_j = 1.0F;
      this.field_70545_g = 0.0F;
      this.field_187129_i = this.field_187130_j = this.field_187131_k = 0.0D;
      this.field_70547_e = age + this.field_187136_p.nextInt(age / 2);
      this.func_187115_a(0.01F, 0.01F);
      this.field_70544_f = 1.0F;
      this.target = target;
      this.yaw = target.func_70079_am();
      this.pitch = target.field_70125_A;
      this.field_187123_c = this.field_187126_f = target.field_70165_t;
      this.field_187124_d = this.field_187127_g = target.field_70163_u + (double)target.func_70047_e();
      this.field_187125_e = this.field_187128_h = target.field_70161_v;
   }

   public void func_180434_a(VertexBuffer wr, Entity p_180434_2_, float f, float f1, float f2, float f3, float f4, float f5) {
      Tessellator.func_178181_a().func_78381_a();
      GL11.glPushMatrix();
      GL11.glDepthMask(false);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      if (this.model == null) {
         this.model = AdvancedModelLoader.loadModel(MODEL);
      }

      float fade = ((float)this.field_70546_d + f) / (float)this.field_70547_e;
      float xx = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)f - field_70556_an);
      float yy = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)f - field_70554_ao);
      float zz = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)f - field_70555_ap);
      GL11.glTranslated((double)xx, (double)yy, (double)zz);
      float b = 1.0F;
      int frame = Math.min(15, (int)(14.0F * fade) + 1);
      Minecraft.func_71410_x().field_71446_o.func_110577_a(new ResourceLocation("thaumcraft", "textures/models/ripple" + frame + ".png"));
      b = 0.5F;
      int i = 220;
      int j = i % 65536;
      int k = i / 65536;
      OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)j / 1.0F, (float)k / 1.0F);
      GL11.glRotatef(-this.yaw, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(this.pitch, 1.0F, 0.0F, 0.0F);
      GL11.glTranslated(0.0D, 0.0D, (double)(2.0F * this.target.field_70131_O + this.target.field_70130_N / 2.0F));
      GL11.glScaled(0.25D * (double)this.target.field_70131_O, 0.25D * (double)this.target.field_70131_O, (double)(-1.0F * this.target.field_70131_O));
      GL11.glColor4f(b, b, b, 1.0F);
      this.model.renderAll();
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
      if (this.field_70546_d++ >= this.field_70547_e) {
         this.func_187112_i();
      }

      this.field_187126_f = this.target.field_70165_t;
      this.field_187127_g = this.target.field_70163_u + (double)this.target.func_70047_e();
      this.field_187128_h = this.target.field_70161_v;
   }
}
