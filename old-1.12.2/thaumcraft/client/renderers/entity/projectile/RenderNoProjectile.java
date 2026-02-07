package thaumcraft.client.renderers.entity.projectile;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.ResourceLocation;

public class RenderNoProjectile extends Render {
   public RenderNoProjectile(RenderManager rm) {
      super(rm);
      this.field_76989_e = 0.1F;
   }

   public void renderEntityAt(EntityThrowable tg, double x, double y, double z, float fq) {
   }

   public void func_76986_a(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderEntityAt((EntityThrowable)entity, d, d1, d2, f);
   }

   protected ResourceLocation func_110775_a(Entity entity) {
      return TextureMap.field_110575_b;
   }
}
