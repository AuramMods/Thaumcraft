package thaumcraft.client.renderers.entity.mob;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.monster.EntityMindSpider;

@SideOnly(Side.CLIENT)
public class RenderMindSpider extends RenderSpider {
   public RenderMindSpider(RenderManager rm) {
      super(rm);
      this.field_76989_e = 0.5F;
      this.func_177094_a(new LayerSpiderEyes(this));
   }

   protected void func_77041_b(EntityLivingBase par1EntityLiving, float par2) {
      GL11.glScalef(0.6F, 0.6F, 0.6F);
   }

   public void func_76986_a(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      if (((EntityMindSpider)p_76986_1_).getViewer().length() == 0 || ((EntityMindSpider)p_76986_1_).getViewer().equals(this.field_76990_c.field_78734_h.func_70005_c_())) {
         super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
      }

   }

   protected void func_77036_a(EntityLivingBase entity, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_) {
      this.func_180548_c(entity);
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, Math.min(0.1F, (float)entity.field_70173_aa / 100.0F));
      GL11.glDepthMask(false);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glAlphaFunc(516, 0.003921569F);
      this.field_77045_g.func_78088_a(entity, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3042);
      GL11.glAlphaFunc(516, 0.1F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
      GL11.glDepthMask(true);
   }
}
