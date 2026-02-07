package thaumcraft.client.renderers.entity.projectile;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.entities.projectile.EntityHomingShard;

public class RenderHomingShard extends Render {
   private Random random = new Random();
   private static final ResourceLocation beamTexture = new ResourceLocation("thaumcraft", "textures/misc/beaml.png");

   public RenderHomingShard(RenderManager rm) {
      super(rm);
      this.field_76989_e = 0.0F;
   }

   public void renderEntityAt(EntityHomingShard entity, double x, double y, double z, float fq, float pticks) {
      Tessellator tessellator = Tessellator.func_178181_a();
      GL11.glPushMatrix();
      GL11.glDepthMask(false);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      this.func_110776_a(ParticleEngine.particleTexture);
      float f2 = (float)(8 + entity.field_70173_aa % 8) / 64.0F;
      float f3 = f2 + 0.015625F;
      float f4 = 0.0625F;
      float f5 = f4 + 0.015625F;
      float f6 = 1.0F;
      float f7 = 0.5F;
      float f8 = 0.5F;
      GL11.glColor4f(0.9F, 0.075F, 0.9525F, 1.0F);
      GL11.glPushMatrix();
      GL11.glTranslated(x, y, z);
      GL11.glRotatef(180.0F - this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(0.4F + 0.1F * (float)entity.getStrength(), 0.4F + 0.1F * (float)entity.getStrength(), 0.4F + 0.1F * (float)entity.getStrength());
      tessellator.func_178180_c().func_181668_a(7, UtilsFX.VERTEXFORMAT_POS_TEX_CO_LM_NO);
      int i = 240;
      int j = i >> 16 & '\uffff';
      int k = i & '\uffff';
      tessellator.func_178180_c().func_181662_b((double)(-f7), (double)(-f8), 0.0D).func_187315_a((double)f2, (double)f5).func_181666_a(0.9F, 0.075F, 0.9525F, 1.0F).func_187314_a(j, k).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)(f6 - f7), (double)(-f8), 0.0D).func_187315_a((double)f3, (double)f5).func_181666_a(0.9F, 0.075F, 0.9525F, 1.0F).func_187314_a(j, k).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)(f6 - f7), (double)(1.0F - f8), 0.0D).func_187315_a((double)f3, (double)f4).func_181666_a(0.9F, 0.075F, 0.9525F, 1.0F).func_187314_a(j, k).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)(-f7), (double)(1.0F - f8), 0.0D).func_187315_a((double)f2, (double)f4).func_181666_a(0.9F, 0.075F, 0.9525F, 1.0F).func_187314_a(j, k).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_78381_a();
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glBlendFunc(770, 1);
      this.func_110776_a(beamTexture);
      Minecraft mc = Minecraft.func_71410_x();
      EntityPlayerSP p = mc.field_71439_g;
      double doubleX = p.field_70142_S + (p.field_70165_t - p.field_70142_S) * (double)pticks;
      double doubleY = p.field_70137_T + (p.field_70163_u - p.field_70137_T) * (double)pticks;
      double doubleZ = p.field_70136_U + (p.field_70161_v - p.field_70136_U) * (double)pticks;
      UtilsFX.Vector player = new UtilsFX.Vector((float)doubleX, (float)doubleY, (float)doubleZ);
      double dX = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)pticks;
      double dY = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)pticks;
      double dZ = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)pticks;
      UtilsFX.Vector start = new UtilsFX.Vector((float)dX, (float)dY, (float)dZ);
      if (entity.vl.size() == 0) {
         entity.vl.add(start);
      }

      GL11.glTranslated(-doubleX, -doubleY, -doubleZ);
      UtilsFX.Vector vs = new UtilsFX.Vector((float)dX, (float)dY, (float)dZ);
      tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181711_k);
      int c = entity.vl.size();

      for(Iterator var39 = entity.vl.iterator(); var39.hasNext(); --c) {
         UtilsFX.Vector nv = (UtilsFX.Vector)var39.next();
         UtilsFX.drawBeam(vs, nv, player, 0.25F * ((float)c / (float)entity.vl.size()), 240, 0.405F, 0.075F, 0.525F, 0.5F);
         vs = nv;
      }

      tessellator.func_78381_a();
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3042);
      GL11.glDisable(32826);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
   }

   public void func_76986_a(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderEntityAt((EntityHomingShard)entity, d, d1, d2, f, f1);
   }

   protected ResourceLocation func_110775_a(Entity entity) {
      return TextureMap.field_110575_b;
   }
}
