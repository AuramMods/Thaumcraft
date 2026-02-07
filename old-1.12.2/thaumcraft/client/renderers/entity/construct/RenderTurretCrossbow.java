package thaumcraft.client.renderers.entity.construct;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import thaumcraft.client.renderers.models.entity.ModelCrossbow;
import thaumcraft.common.entities.construct.EntityTurretCrossbow;

public class RenderTurretCrossbow extends RenderLiving {
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/models/crossbow.png");

   public RenderTurretCrossbow(RenderManager rm) {
      super(rm, new ModelCrossbow(), 0.5F);
   }

   protected float func_77040_d(EntityLivingBase e, float p_77040_2_) {
      ((EntityTurretCrossbow)e).loadProgressForRender = ((EntityTurretCrossbow)e).getLoadProgress(p_77040_2_);
      e.field_70761_aq = 0.0F;
      e.field_70760_ar = 0.0F;
      return super.func_77040_d(e, p_77040_2_);
   }

   protected void func_77041_b(EntityLivingBase entitylivingbaseIn, float partialTickTime) {
      entitylivingbaseIn.field_70761_aq = 0.0F;
      entitylivingbaseIn.field_70760_ar = 0.0F;
      super.func_77041_b(entitylivingbaseIn, partialTickTime);
   }

   protected ResourceLocation func_110775_a(Entity entity) {
      return rl;
   }
}
