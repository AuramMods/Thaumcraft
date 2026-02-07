package thaumcraft.client.renderers.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.items.tools.ItemThaumometer;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.world.aura.EntityAuraNode;

@SideOnly(Side.CLIENT)
public class RenderAuraNode extends Render {
   public RenderAuraNode(RenderManager rm) {
      super(rm);
      this.field_76989_e = 0.0F;
   }

   private void doRender(EntityAuraNode entity, double x, double y, double z, float fq, float pticks) {
      if (!entity.field_70128_L) {
         double vr = 2048.0D;
         long t = System.currentTimeMillis();
         boolean canSee = EntityUtils.hasRevealer(Minecraft.func_71410_x().func_175606_aa());
         if (!canSee) {
            canSee = (Minecraft.func_71410_x().field_71439_g.func_184614_ca() != null && Minecraft.func_71410_x().field_71439_g.func_184614_ca().func_77973_b() instanceof ItemThaumometer || Minecraft.func_71410_x().field_71439_g.func_184592_cb() != null && Minecraft.func_71410_x().field_71439_g.func_184592_cb().func_77973_b() instanceof ItemThaumometer) && EntityUtils.isVisibleTo(1.15F, Minecraft.func_71410_x().func_175606_aa(), entity, 24.0F);
            vr = 576.0D;
         }

         if (canSee) {
            double d = entity.func_70068_e(Minecraft.func_71410_x().func_175606_aa());
            if (!(d > vr)) {
               float alpha = 1.0F - (float)Math.min(1.0D, d / (vr * 0.8999999761581421D));
               int color = 8947848;
               int blend = 1;
               int type = 1;
               float size = 0.15F + (float)entity.getNodeSize() / 75.0F;
               if (entity.getAspect() != null) {
                  color = entity.getAspect().getColor();
                  blend = entity.getAspect().getBlend();
                  type = 1 + entity.getNodeType();
               }

               GlStateManager.func_179094_E();
               this.func_110776_a(UtilsFX.nodeTexture);
               GlStateManager.func_179097_i();
               UtilsFX.renderFacingQuad(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, 32, 32, entity.field_70173_aa % 32, size, color, 0.75F * alpha, blend, pticks);
               float s = 1.0F - MathHelper.func_76126_a(((float)entity.field_70173_aa + pticks) / 8.0F) / 5.0F;
               UtilsFX.renderFacingQuad(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, 32, 32, 800 + entity.field_70173_aa % 16, s * size * 0.7F, color, 0.9F * alpha, blend, pticks);
               UtilsFX.renderFacingQuad(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, 32, 32, 32 * type + entity.field_70173_aa % 32, size / 3.0F, 16777215, 1.0F * alpha, type == 2 ? 771 : 1, pticks);
               GlStateManager.func_179126_j();
               GlStateManager.func_179121_F();
               if (d < 30.0D) {
                  float sc = 1.0F - (float)Math.min(1.0D, d / 25.0D);
                  GlStateManager.func_179094_E();
                  GlStateManager.func_179137_b(x, y, z);
                  GlStateManager.func_179139_a(0.025D * (double)sc, 0.025D * (double)sc, 0.025D * (double)sc);
                  UtilsFX.rotateToPlayer();
                  GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                  int var19 = 210;
                  int var20 = var19 % 65536;
                  int var21 = var19 / 65536;
                  OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)var20 / 1.0F, (float)var21 / 1.0F);
                  UtilsFX.drawTag(-8, -32, entity.getAspect(), (float)entity.getNodeSize(), 0, 0.0D);
                  GlStateManager.func_179139_a(0.5D, 0.5D, 0.5D);
                  String text = I18n.func_74838_a("nodetype." + entity.getNodeType());
                  int sw = Minecraft.func_71410_x().field_71466_p.func_78256_a(text);
                  Minecraft.func_71410_x().field_71466_p.func_175065_a(text, (float)(-sw) / 2.0F, -72.0F, 16777215, false);
                  GlStateManager.func_179121_F();
               }

            }
         }
      }
   }

   public void func_76986_a(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.doRender((EntityAuraNode)entity, d, d1, d2, f, f1);
   }

   protected ResourceLocation func_110775_a(Entity entity) {
      return UtilsFX.nodeTexture;
   }
}
