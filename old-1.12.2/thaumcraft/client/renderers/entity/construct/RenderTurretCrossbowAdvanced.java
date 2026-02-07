package thaumcraft.client.renderers.entity.construct;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.obj.AdvancedModelLoader;
import thaumcraft.client.lib.obj.IModelCustom;
import thaumcraft.common.entities.construct.EntityTurretCrossbow;

public class RenderTurretCrossbowAdvanced extends RenderLiving {
   private IModelCustom model;
   private static final ResourceLocation TURMODEL = new ResourceLocation("thaumcraft", "models/obj/crossbow_advanced.obj");
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/models/crossbow_advanced.png");

   public RenderTurretCrossbowAdvanced(RenderManager rm) {
      super(rm, (ModelBase)null, 0.5F);
      this.model = AdvancedModelLoader.loadModel(TURMODEL);
   }

   public void renderTurret(EntityTurretCrossbow turret, double x, double y, double z, float par8, float pTicks) {
      this.func_180548_c(turret);
      GL11.glPushMatrix();
      GL11.glEnable(32826);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glTranslated(x, y + 0.75D, z);
      GL11.glPushMatrix();
      if (turret.func_184218_aH() && turret.func_184187_bx() != null && turret.func_184187_bx() instanceof EntityMinecart) {
         GL11.glScaled(0.66D, 0.75D, 0.66D);
      }

      this.model.renderPart("legs");
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      float sp;
      if (turret.field_70737_aN > 0) {
         GlStateManager.func_179131_c(1.0F, 0.5F, 0.5F, 1.0F);
         sp = (float)turret.field_70737_aN / 500.0F;
         GL11.glTranslated(turret.func_70681_au().nextGaussian() * (double)sp, turret.func_70681_au().nextGaussian() * (double)sp, turret.func_70681_au().nextGaussian() * (double)sp);
      }

      GL11.glRotatef(turret.field_70758_at + (turret.field_70759_as - turret.field_70758_at) * pTicks, 0.0F, -1.0F, 0.0F);
      GL11.glRotatef(turret.field_70127_C + (turret.field_70125_A - turret.field_70127_C) * pTicks, 1.0F, 0.0F, 0.0F);
      this.model.renderPart("mech");
      this.model.renderPart("box");
      this.model.renderPart("shield");
      this.model.renderPart("brain");
      GL11.glPushMatrix();
      GL11.glTranslated(0.0D, 0.0D, (double)(MathHelper.func_76126_a(MathHelper.func_76129_c(turret.loadProgressForRender) * 3.1415927F * 2.0F) / 12.0F));
      this.model.renderPart("loader");
      GL11.glPopMatrix();
      sp = 0.0F;
      if (this.func_77040_d(turret, pTicks) > -9990.0F) {
         sp = MathHelper.func_76126_a(MathHelper.func_76129_c(this.func_77040_d(turret, pTicks)) * 3.1415927F * 2.0F) * 20.0F;
      }

      GL11.glTranslated(0.0D, 0.0D, 0.375D);
      GL11.glPushMatrix();
      GL11.glRotatef(sp, 0.0F, 1.0F, 0.0F);
      this.model.renderPart("bow1");
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glRotatef(sp, 0.0F, -1.0F, 0.0F);
      this.model.renderPart("bow2");
      GL11.glPopMatrix();
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3042);
      GL11.glDisable(32826);
      GL11.glPopMatrix();
   }

   protected float func_77040_d(EntityLivingBase e, float p_77040_2_) {
      ((EntityTurretCrossbow)e).loadProgressForRender = ((EntityTurretCrossbow)e).getLoadProgress(p_77040_2_);
      return super.func_77040_d(e, p_77040_2_);
   }

   private void translateFromOrientation(int orientation) {
      GL11.glTranslated(0.0D, 0.5D, 0.0D);
      if (orientation == 0) {
         GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      } else if (orientation != 1) {
         if (orientation == 2) {
            GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
         } else if (orientation == 3) {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         } else if (orientation == 4) {
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
         } else if (orientation == 5) {
            GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
         }
      }

      GL11.glTranslated(0.0D, -0.5D, 0.0D);
   }

   public void func_76986_a(EntityLiving par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderTurret((EntityTurretCrossbow)par1Entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation func_110775_a(Entity entity) {
      return rl;
   }
}
