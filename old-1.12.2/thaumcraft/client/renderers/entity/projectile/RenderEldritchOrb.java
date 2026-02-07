package thaumcraft.client.renderers.entity.projectile;

import java.util.Random;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ParticleEngine;

public class RenderEldritchOrb extends Render {
   private Random random = new Random();

   public RenderEldritchOrb(RenderManager renderManager) {
      super(renderManager);
      this.field_76989_e = 0.0F;
   }

   public void renderEntityAt(Entity entity, double x, double y, double z, float fq, float pticks) {
      Tessellator tessellator = Tessellator.func_178181_a();
      this.random.setSeed(187L);
      GL11.glPushMatrix();
      RenderHelper.func_74518_a();
      float f1 = (float)entity.field_70173_aa / 80.0F;
      float f3 = 0.9F;
      float f2 = 0.0F;
      GL11.glTranslatef((float)x, (float)y, (float)z);
      GL11.glDisable(3553);
      GL11.glShadeModel(7425);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      GL11.glDisable(3008);
      GL11.glEnable(2884);
      GL11.glDepthMask(false);
      GL11.glPushMatrix();

      float f5;
      float f6;
      for(int i = 0; i < 12; ++i) {
         GL11.glRotatef(this.random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(this.random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(this.random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(this.random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(this.random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(this.random.nextFloat() * 360.0F + f1 * 360.0F, 0.0F, 0.0F, 1.0F);
         tessellator.func_178180_c().func_181668_a(6, DefaultVertexFormats.field_181706_f);
         f5 = this.random.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
         f6 = this.random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
         f5 /= 30.0F / ((float)Math.min(entity.field_70173_aa, 10) / 10.0F);
         f6 /= 30.0F / ((float)Math.min(entity.field_70173_aa, 10) / 10.0F);
         tessellator.func_178180_c().func_181662_b(0.0D, 0.0D, 0.0D).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F - f2).func_181675_d();
         tessellator.func_178180_c().func_181662_b(-0.866D * (double)f6, (double)f5, (double)(-0.5F * f6)).func_181666_a(64.0F, 64.0F, 64.0F, 255.0F * (1.0F - f2)).func_181675_d();
         tessellator.func_178180_c().func_181662_b(0.866D * (double)f6, (double)f5, (double)(-0.5F * f6)).func_181666_a(64.0F, 64.0F, 64.0F, 255.0F * (1.0F - f2)).func_181675_d();
         tessellator.func_178180_c().func_181662_b(0.0D, (double)f5, (double)(1.0F * f6)).func_181666_a(64.0F, 64.0F, 64.0F, 255.0F * (1.0F - f2)).func_181675_d();
         tessellator.func_178180_c().func_181662_b(-0.866D * (double)f6, (double)f5, (double)(-0.5F * f6)).func_181666_a(64.0F, 64.0F, 64.0F, 255.0F * (1.0F - f2)).func_181675_d();
         tessellator.func_78381_a();
      }

      GL11.glPopMatrix();
      GL11.glDepthMask(true);
      GL11.glDisable(2884);
      GL11.glDisable(3042);
      GL11.glShadeModel(7424);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      GL11.glEnable(3008);
      RenderHelper.func_74519_b();
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glTranslated(x, y, z);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      this.func_110776_a(ParticleEngine.particleTexture);
      f2 = (float)(entity.field_70173_aa % 13) / 64.0F;
      f3 = f2 + 0.015625F;
      float f4 = 0.046875F;
      f5 = f4 + 0.015625F;
      f6 = 1.0F;
      float f7 = 0.5F;
      float f8 = 0.5F;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glRotatef(180.0F - this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(1.0F, 1.0F, 1.0F);
      tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181710_j);
      tessellator.func_178180_c();
      tessellator.func_178180_c().func_181662_b((double)(0.0F - f7), (double)(0.0F - f8), 0.0D).func_187315_a((double)f2, (double)f5).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)(f6 - f7), (double)(0.0F - f8), 0.0D).func_187315_a((double)f3, (double)f5).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)(f6 - f7), (double)(1.0F - f8), 0.0D).func_187315_a((double)f3, (double)f4).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)(0.0F - f7), (double)(1.0F - f8), 0.0D).func_187315_a((double)f2, (double)f4).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_78381_a();
      GL11.glDisable(3042);
      GL11.glDisable(32826);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   public void func_76986_a(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderEntityAt(entity, d, d1, d2, f, f1);
   }

   protected ResourceLocation func_110775_a(Entity entity) {
      return ParticleEngine.particleTexture;
   }
}
