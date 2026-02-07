package thaumcraft.client.renderers.entity.projectile;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ParticleEngine;

@SideOnly(Side.CLIENT)
public class RenderDart extends Render {
   private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");
   int size1 = 0;
   int size2 = 0;

   public RenderDart(RenderManager renderManager) {
      super(renderManager);
   }

   public void renderArrow(EntityArrow arrow, double x, double y, double z, float ns, float prt) {
      this.func_180548_c(arrow);
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glTranslatef((float)x, (float)y, (float)z);
      GL11.glRotatef(arrow.field_70126_B + (arrow.field_70177_z - arrow.field_70126_B) * prt - 90.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(arrow.field_70127_C + (arrow.field_70125_A - arrow.field_70127_C) * prt, 0.0F, 0.0F, 1.0F);
      Tessellator tessellator = Tessellator.func_178181_a();
      byte b0 = 0;
      float f2 = 0.0F;
      float f3 = 0.5F;
      float f4 = (float)(0 + b0 * 10) / 32.0F;
      float f5 = (float)(5 + b0 * 10) / 32.0F;
      float f6 = 0.0F;
      float f7 = 0.15625F;
      float f8 = (float)(5 + b0 * 10) / 32.0F;
      float f9 = (float)(10 + b0 * 10) / 32.0F;
      float f10 = 0.033F;
      GL11.glEnable(32826);
      float f11 = (float)arrow.field_70249_b - prt;
      if (f11 > 0.0F) {
         float f12 = -MathHelper.func_76126_a(f11 * 3.0F) * f11;
         GL11.glRotatef(f12, 0.0F, 0.0F, 1.0F);
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(f10, f10, f10);
      GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
      GL11.glNormal3f(f10, 0.0F, 0.0F);
      tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181707_g);
      tessellator.func_178180_c().func_181662_b(-7.0D, -2.0D, -2.0D).func_187315_a((double)f6, (double)f8).func_181675_d();
      tessellator.func_178180_c().func_181662_b(-7.0D, -2.0D, 2.0D).func_187315_a((double)f7, (double)f8).func_181675_d();
      tessellator.func_178180_c().func_181662_b(-7.0D, 2.0D, 2.0D).func_187315_a((double)f7, (double)f9).func_181675_d();
      tessellator.func_178180_c().func_181662_b(-7.0D, 2.0D, -2.0D).func_187315_a((double)f6, (double)f9).func_181675_d();
      tessellator.func_78381_a();
      GL11.glNormal3f(-f10, 0.0F, 0.0F);
      tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181707_g);
      tessellator.func_178180_c().func_181662_b(-7.0D, 2.0D, -2.0D).func_187315_a((double)f6, (double)f8).func_181675_d();
      tessellator.func_178180_c().func_181662_b(-7.0D, 2.0D, 2.0D).func_187315_a((double)f7, (double)f8).func_181675_d();
      tessellator.func_178180_c().func_181662_b(-7.0D, -2.0D, 2.0D).func_187315_a((double)f7, (double)f9).func_181675_d();
      tessellator.func_178180_c().func_181662_b(-7.0D, -2.0D, -2.0D).func_187315_a((double)f6, (double)f9).func_181675_d();
      tessellator.func_78381_a();

      for(int i = 0; i < 4; ++i) {
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         GL11.glNormal3f(0.0F, 0.0F, f10);
         tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181707_g);
         tessellator.func_178180_c().func_181662_b(-8.0D, -2.0D, 0.0D).func_187315_a((double)f2, (double)f4).func_181675_d();
         tessellator.func_178180_c().func_181662_b(8.0D, -2.0D, 0.0D).func_187315_a((double)f3, (double)f4).func_181675_d();
         tessellator.func_178180_c().func_181662_b(8.0D, 2.0D, 0.0D).func_187315_a((double)f3, (double)f5).func_181675_d();
         tessellator.func_178180_c().func_181662_b(-8.0D, 2.0D, 0.0D).func_187315_a((double)f2, (double)f5).func_181675_d();
         tessellator.func_78381_a();
      }

      GL11.glDisable(32826);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      this.func_110776_a(ParticleEngine.particleTexture);
      GL11.glPopMatrix();
   }

   protected ResourceLocation getArrowTextures(EntityArrow par1EntityArrow) {
      return arrowTextures;
   }

   protected ResourceLocation func_110775_a(Entity par1Entity) {
      return this.getArrowTextures((EntityArrow)par1Entity);
   }

   public void func_76986_a(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderArrow((EntityArrow)par1Entity, par2, par4, par6, par8, par9);
   }
}
