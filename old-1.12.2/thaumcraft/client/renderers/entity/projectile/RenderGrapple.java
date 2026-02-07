package thaumcraft.client.renderers.entity.projectile;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.entities.projectile.EntityGrapple;

public class RenderGrapple extends Render {
   ResourceLocation beam = new ResourceLocation("thaumcraft", "textures/misc/particles.png");
   public ArrayList<Vec3d> points = new ArrayList();
   public float length = 1.0F;

   public RenderGrapple(RenderManager rm) {
      super(rm);
      this.field_76989_e = 0.0F;
   }

   public void renderEntityAt(Entity entity, double x, double y, double z, float fq, float pticks) {
      Tessellator tessellator = Tessellator.func_178181_a();
      boolean sticky = ((EntityGrapple)entity).sticky;
      GL11.glPushMatrix();
      GL11.glTranslated(x, y, z);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      GL11.glDisable(2884);
      this.func_110776_a(ParticleEngine.particleTexture);
      float f2 = (float)(1 + entity.field_70173_aa % 6) / 8.0F;
      float f3 = f2 + 0.125F;
      float f4 = 0.875F;
      float f5 = f4 + 0.125F;
      float f7 = 0.5F;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
      GL11.glRotatef(180.0F - this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
      float bob = MathHelper.func_76126_a((float)entity.field_70173_aa / 5.0F) * 0.2F + 0.2F;
      GL11.glScalef(1.0F + bob, 1.0F + bob, 1.0F + bob);
      tessellator.func_178180_c().func_181668_a(7, UtilsFX.VERTEXFORMAT_POS_TEX_CO_LM_NO);
      int i = 220;
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      tessellator.func_178180_c().func_181662_b((double)(-f7), (double)(-f7), 0.0D).func_187315_a((double)f2, (double)f5).func_181666_a(sticky ? 0.0F : 1.0F, 1.0F, sticky ? 0.2F : 1.0F, 1.0F).func_187314_a(j, k).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)f7, (double)(-f7), 0.0D).func_187315_a((double)f3, (double)f5).func_181666_a(sticky ? 0.0F : 1.0F, 1.0F, sticky ? 0.2F : 1.0F, 1.0F).func_187314_a(j, k).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)f7, (double)f7, 0.0D).func_187315_a((double)f3, (double)f4).func_181666_a(sticky ? 0.0F : 1.0F, 1.0F, sticky ? 0.2F : 1.0F, 1.0F).func_187314_a(j, k).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)(-f7), (double)f7, 0.0D).func_187315_a((double)f2, (double)f4).func_181666_a(sticky ? 0.0F : 1.0F, 1.0F, sticky ? 0.2F : 1.0F, 1.0F).func_187314_a(j, k).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_78381_a();
      GL11.glEnable(2884);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3042);
      GL11.glDisable(32826);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
      this.calcPoints(((EntityGrapple)entity).func_85052_h(), (EntityGrapple)entity, pticks);
      GL11.glPushMatrix();
      Minecraft.func_71410_x().field_71446_o.func_110577_a(this.beam);
      GL11.glDepthMask(false);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      GL11.glDisable(2884);

      for(int c = 0; c < this.points.size(); ++c) {
         int pp = (c + entity.field_70173_aa) % 13;
         float alpha = Math.min(1.0F, 1.0F - (float)c / ((float)this.points.size() / 4.0F));
         Vec3d vc = (Vec3d)this.points.get(c);
         GL11.glPushMatrix();
         GL11.glTranslated(x + vc.field_72450_a, y + vc.field_72448_b, z + vc.field_72449_c);
         UtilsFX.renderBillboardQuad(0.07500000298023224D, 16, 16, 32 + pp);
         GL11.glPopMatrix();
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(2884);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3042);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
   }

   private void calcPoints(EntityLivingBase thrower, EntityGrapple grapple, float pt) {
      if (thrower != null && grapple != null) {
         double tx = thrower.field_70142_S + (thrower.field_70165_t - thrower.field_70142_S) * (double)pt;
         double ty = thrower.field_70137_T + (thrower.field_70163_u - thrower.field_70137_T) * (double)pt;
         double tz = thrower.field_70136_U + (thrower.field_70161_v - thrower.field_70136_U) * (double)pt;
         double gx = grapple.field_70142_S + (grapple.field_70165_t - grapple.field_70142_S) * (double)pt;
         double gy = grapple.field_70137_T + (grapple.field_70163_u - grapple.field_70137_T) * (double)pt;
         double gz = grapple.field_70136_U + (grapple.field_70161_v - grapple.field_70136_U) * (double)pt;
         this.points.clear();
         Vec3d vs = new Vec3d(0.0D, 0.0D, 0.0D);
         Vec3d ve = new Vec3d(tx - gx, ty - gy + (double)(thrower.field_70131_O / 2.0F), tz - gz);
         this.length = (float)(ve.func_72433_c() * 5.0D);
         int steps = (int)this.length;
         this.points.add(vs);

         for(int a = 1; a < steps - 1; ++a) {
            float dist = (float)a * (this.length / (float)steps);
            double dx = (tx - gx) / (double)steps * (double)a + (double)(MathHelper.func_76126_a(dist / 10.0F) * grapple.ampl);
            double dy = (ty - gy + (double)(thrower.field_70131_O / 2.0F)) / (double)steps * (double)a + (double)(MathHelper.func_76126_a(dist / 8.0F) * grapple.ampl);
            double dz = (tz - gz) / (double)steps * (double)a + (double)(MathHelper.func_76126_a(dist / 6.0F) * grapple.ampl);
            Vec3d vp = new Vec3d(dx, dy, dz);
            this.points.add(vp);
         }

         this.points.add(ve);
      }
   }

   public void func_76986_a(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderEntityAt(entity, d, d1, d2, f, f1);
   }

   protected ResourceLocation func_110775_a(Entity entity) {
      return TextureMap.field_110575_b;
   }
}
