package thaumcraft.client.renderers.entity.mob;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.entities.monster.EntityWisp;

public class RenderWisp extends Render {
   public RenderWisp(RenderManager rm) {
      super(rm);
      this.field_76989_e = 0.0F;
   }

   public void renderEntityAt(Entity entity, double x, double y, double z, float fq, float pticks) {
      if (!(((EntityLiving)entity).func_110143_aJ() <= 0.0F)) {
         double xx = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)pticks;
         double yy = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)pticks;
         double zz = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)pticks;
         int color = 0;
         if (Aspect.getAspect(((EntityWisp)entity).getType()) != null) {
            color = Aspect.getAspect(((EntityWisp)entity).getType()).getColor();
         }

         GL11.glPushMatrix();
         GL11.glAlphaFunc(516, 0.003921569F);
         GL11.glDepthMask(false);
         this.func_110776_a(ParticleEngine.particleTexture);
         UtilsFX.renderFacingQuad(xx, yy, zz, 64, 64, 512 + entity.field_70173_aa % 16, 0.4F, 16777215, 1.0F, 1, pticks);
         UtilsFX.renderFacingQuad(xx, yy, zz, 64, 64, 320 + entity.field_70173_aa % 16, 0.75F, 16777215, 0.25F, 1, pticks);
         this.func_110776_a(UtilsFX.nodeTexture);
         UtilsFX.renderFacingQuad(xx, yy, zz, 32, 32, 800 + entity.field_70173_aa % 16, 0.75F, color, 0.5F, 1, pticks);
         GL11.glDepthMask(true);
         GL11.glAlphaFunc(516, 0.1F);
         GL11.glPopMatrix();
      }
   }

   public void func_76986_a(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderEntityAt(entity, d, d1, d2, f, f1);
   }

   protected ResourceLocation func_110775_a(Entity entity) {
      return TextureMap.field_110575_b;
   }
}
