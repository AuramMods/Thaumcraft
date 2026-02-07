package thaumcraft.client.renderers.entity.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.renderers.models.entity.ModelTaintacle;
import thaumcraft.common.entities.monster.tainted.EntityTaintSeed;

@SideOnly(Side.CLIENT)
public class RenderTaintSeed extends RenderLiving<EntityTaintSeed> {
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/models/creature/taintacle.png");

   public RenderTaintSeed(RenderManager rm) {
      super(rm, new ModelTaintacle(12, true), 0.6F);
   }

   public RenderTaintSeed(RenderManager rm, ModelBase modelbase, float sz) {
      super(rm, modelbase, sz);
   }

   protected ResourceLocation getEntityTexture(EntityTaintSeed entity) {
      return rl;
   }

   public void doRender(EntityTaintSeed entity, double x, double y, double z, float entityYaw, float partialTicks) {
      if (!MinecraftForge.EVENT_BUS.post(new Pre(entity, this, x, y, z))) {
         GlStateManager.func_179094_E();
         GlStateManager.func_179129_p();
         this.field_77045_g.field_78095_p = this.func_77040_d(entity, partialTicks);
         boolean shouldSit = entity.func_184218_aH() && entity.func_184187_bx() != null && entity.func_184187_bx().shouldRiderSit();
         this.field_77045_g.field_78093_q = shouldSit;
         this.field_77045_g.field_78091_s = entity.func_70631_g_();

         try {
            for(int a = 0; a < 5; ++a) {
               GlStateManager.func_179094_E();
               float f = (float)(45 + 72 * a);
               float f7 = entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * partialTicks;
               this.func_77039_a(entity, x, y, z);
               float f8 = this.func_77044_a(entity, partialTicks);
               this.func_77043_a(entity, f8, f, partialTicks);
               float f4 = this.func_188322_c(entity, partialTicks);
               float f5 = 0.0F;
               float f6 = 0.0F;
               f5 = entity.field_184618_aE + (entity.field_70721_aZ - entity.field_184618_aE) * partialTicks;
               f6 = entity.field_184619_aG - entity.field_70721_aZ * (1.0F - partialTicks);
               if (f5 > 1.0F) {
                  f5 = 1.0F;
               }

               GlStateManager.func_179141_d();
               this.field_77045_g.func_78086_a(entity, f6, f5, partialTicks);
               this.field_77045_g.func_78087_a(f6, f5, f8, f, f7, f4, entity);
               boolean flag1;
               if (this.field_188301_f) {
                  flag1 = this.func_177088_c(entity);
                  GlStateManager.func_179142_g();
                  GlStateManager.func_187431_e(this.func_188298_c(entity));
                  if (!this.field_188323_j) {
                     this.func_77036_a(entity, f6, f5, f8, f, f7, f4);
                  }

                  this.func_177093_a(entity, f6, f5, partialTicks, f8, f, f7, f4);
                  GlStateManager.func_187417_n();
                  GlStateManager.func_179119_h();
                  if (flag1) {
                     this.func_180565_e();
                  }
               } else {
                  flag1 = this.func_177090_c(entity, partialTicks);
                  this.func_77036_a(entity, f6, f5, f8, f, f7, f4);
                  if (flag1) {
                     this.func_177091_f();
                  }

                  GlStateManager.func_179132_a(true);
                  this.func_177093_a(entity, f6, f5, partialTicks, f8, f, f7, f4);
               }

               GlStateManager.func_179121_F();
            }

            GlStateManager.func_179101_C();
         } catch (Exception var19) {
         }

         GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
         GlStateManager.func_179098_w();
         GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
         GlStateManager.func_179089_o();
         GlStateManager.func_179121_F();
         if (!this.field_188301_f) {
            this.func_177067_a(entity, x, y, z);
         }

         MinecraftForge.EVENT_BUS.post(new Post(entity, this, x, y, z));
      }
   }
}
