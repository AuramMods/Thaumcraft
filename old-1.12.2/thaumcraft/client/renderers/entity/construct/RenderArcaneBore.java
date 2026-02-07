package thaumcraft.client.renderers.entity.construct;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.entity.ModelArcaneBore;
import thaumcraft.common.entities.construct.EntityArcaneBore;
import thaumcraft.common.lib.utils.Utils;

public class RenderArcaneBore extends RenderLiving {
   private static final ResourceLocation rl = new ResourceLocation("thaumcraft", "textures/models/arcanebore.png");
   ResourceLocation beam = new ResourceLocation("thaumcraft", "textures/misc/beam1.png");

   public RenderArcaneBore(RenderManager rm) {
      super(rm, new ModelArcaneBore(), 0.5F);
   }

   protected ResourceLocation func_110775_a(Entity entity) {
      return rl;
   }

   protected float func_77040_d(EntityLivingBase livingBase, float partialTickTime) {
      livingBase.field_70761_aq = 0.0F;
      livingBase.field_70760_ar = 0.0F;
      return super.func_77040_d(livingBase, partialTickTime);
   }

   protected void func_77041_b(EntityLivingBase entitylivingbaseIn, float partialTickTime) {
      entitylivingbaseIn.field_70761_aq = 0.0F;
      entitylivingbaseIn.field_70760_ar = 0.0F;
      super.func_77041_b(entitylivingbaseIn, partialTickTime);
   }

   public void func_76986_a(EntityLiving entity, double x, double y, double z, float entityYaw, float partialTicks) {
      super.func_76986_a(entity, x, y, z, entityYaw, partialTicks);
      EntityArcaneBore bore = (EntityArcaneBore)entity;
      if (bore.clientDigging && bore.isActive() && bore.validInventory()) {
         Tessellator tessellator = Tessellator.func_178181_a();
         GL11.glPushMatrix();
         GL11.glDepthMask(false);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 1);
         Minecraft.func_71410_x().field_71446_o.func_110577_a(UtilsFX.nodeTexture);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.66F);
         int part = entity.field_70173_aa % 32;
         Vec3d lv2 = new Vec3d(0.5D, 0.075D, 0.0D);
         Vec3d cv = new Vec3d(x, y + (double)bore.func_70047_e(), z);
         lv2 = Utils.rotateAroundZ(lv2, bore.field_70125_A / 180.0F * 3.1415927F);
         lv2 = Utils.rotateAroundY(lv2, -((bore.field_70759_as + 90.0F) / 180.0F * 3.1415927F));
         cv = cv.func_178787_e(lv2);
         double beamLength = 5.0D;
         GL11.glTranslated(cv.field_72450_a, cv.field_72448_b, cv.field_72449_c);
         GL11.glPushMatrix();
         UtilsFX.renderBillboardQuad(0.5D, 32, 32, 96 + part, 0.0F, 1.0F, 0.4F, 0.8F, 210);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         float var9 = 1.0F;
         float rot = (float)(bore.field_70170_p.field_73011_w.getWorldTime() % 72L * 5L) + 5.0F * partialTicks;
         float size = 1.0F;
         float op = 0.4F;
         Minecraft.func_71410_x().field_71446_o.func_110577_a(this.beam);
         GL11.glTexParameterf(3553, 10242, 10497.0F);
         GL11.glTexParameterf(3553, 10243, 10497.0F);
         GL11.glDisable(2884);
         float var11 = (float)entity.field_70173_aa + partialTicks;
         var11 *= -1.0F;
         float var12 = -var11 * 0.2F - (float)MathHelper.func_76141_d(-var11 * 0.1F);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 1);
         GL11.glDepthMask(false);
         float ry = bore.field_70126_B + (bore.field_70177_z - bore.field_70126_B) * partialTicks;
         float rp = bore.field_70127_C + (bore.field_70125_A - bore.field_70127_C) * partialTicks;
         GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
         GL11.glRotatef(180.0F + ry, 0.0F, 0.0F, -1.0F);
         GL11.glRotatef(rp, -1.0F, 0.0F, 0.0F);
         double var44 = -0.15D * (double)size;
         double var17 = 0.15D * (double)size;
         double var44b = 0.0D;
         double var17b = 0.0D;
         int i = 200;
         int j = i >> 16 & '\uffff';
         int k = i & '\uffff';
         GL11.glRotatef(rot, 0.0F, 1.0F, 0.0F);

         for(int t = 0; t < 3; ++t) {
            double var29 = beamLength * (double)size * (double)var9;
            double var31 = 0.0D;
            double var33 = 1.0D;
            double var35 = (double)(-1.0F + var12 + (float)t / 3.0F);
            double var37 = beamLength * (double)size * (double)var9 + var35;
            GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
            tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181704_d);
            tessellator.func_178180_c().func_181662_b(var44b, var29, 0.0D).func_187315_a(var33, var37).func_181666_a(0.0F, 1.0F, 0.4F, op).func_187314_a(j, k).func_181675_d();
            tessellator.func_178180_c().func_181662_b(var44, 0.0D, 0.0D).func_187315_a(var33, var35).func_181666_a(0.0F, 1.0F, 0.4F, op).func_187314_a(j, k).func_181675_d();
            tessellator.func_178180_c().func_181662_b(var17, 0.0D, 0.0D).func_187315_a(var31, var35).func_181666_a(0.0F, 1.0F, 0.4F, op).func_187314_a(j, k).func_181675_d();
            tessellator.func_178180_c().func_181662_b(var17b, var29, 0.0D).func_187315_a(var31, var37).func_181666_a(0.0F, 1.0F, 0.4F, op).func_187314_a(j, k).func_181675_d();
            Tessellator.func_178181_a().func_78381_a();
         }

         GL11.glPopMatrix();
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(3042);
         GL11.glDepthMask(true);
         GL11.glPopMatrix();
      }

   }
}
