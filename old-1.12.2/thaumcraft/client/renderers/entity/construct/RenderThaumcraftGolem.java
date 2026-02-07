package thaumcraft.client.renderers.entity.construct;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemProperties;
import thaumcraft.api.golems.parts.PartModel;
import thaumcraft.client.lib.obj.AdvancedModelLoader;
import thaumcraft.client.lib.obj.IModelCustom;
import thaumcraft.common.entities.construct.golem.EntityThaumcraftGolem;

@SideOnly(Side.CLIENT)
public class RenderThaumcraftGolem extends RenderBiped {
   private static final Logger logger = LogManager.getLogger();
   private HashMap<String, IModelCustom> models = new HashMap();
   private HashMap<Integer, HashMap<PartModel.EnumAttachPoint, ArrayList<PartModel>>> partsCache = new HashMap();
   private IModelCustom baseModel;
   float swingProgress = 0.0F;

   public RenderThaumcraftGolem(RenderManager p_i46127_1_) {
      super(p_i46127_1_, new ModelBiped(), 0.3F);
      this.field_177097_h.clear();
      this.baseModel = AdvancedModelLoader.loadModel(new ResourceLocation("thaumcraft", "models/obj/golem_base.obj"));
   }

   private void renderModel(EntityThaumcraftGolem entity, float p1, float p2, float p3, float p4, float p5, float p6, float partialTicks) {
      boolean flag = !entity.func_82150_aj();
      boolean flag1 = !flag && !entity.func_98034_c(Minecraft.func_71410_x().field_71439_g);
      if (flag || flag1) {
         if (flag1) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 0.15F);
            GlStateManager.func_179132_a(false);
            GlStateManager.func_179147_l();
            GlStateManager.func_179112_b(770, 771);
            GlStateManager.func_179092_a(516, 0.003921569F);
         }

         this.renderParts(entity, p1, p2, p3, p4, p5, p6, partialTicks);
         if (flag1) {
            GlStateManager.func_179084_k();
            GlStateManager.func_179092_a(516, 0.1F);
            GlStateManager.func_179121_F();
            GlStateManager.func_179132_a(true);
         }
      }

   }

   private void renderParts(EntityThaumcraftGolem entity, float limbSwing, float prevLimbSwing, float rotFloat, float headPitch, float headYaw, float p_78087_6_, float partialTicks) {
      ResourceLocation matTexture = entity.getProperties().getMaterial().texture;
      boolean holding = entity.func_184614_ca() != null;
      boolean aflag = entity.getProperties().hasTrait(EnumGolemTrait.WHEELED) || entity.getProperties().hasTrait(EnumGolemTrait.FLYER);
      Vec3d v1 = new Vec3d(entity.field_70165_t, 0.0D, entity.field_70161_v);
      Vec3d v2 = new Vec3d(entity.field_70169_q, 0.0D, entity.field_70166_s);
      double speed = v1.func_72436_e(v2);
      if (!this.partsCache.containsKey(entity.func_145782_y())) {
         this.createPartsCache(entity);
      }

      float f1 = 0.0F;
      float bry = 0.0F;
      float rx = (float)Math.toDegrees((double)(MathHelper.func_76126_a(rotFloat * 0.067F) * 0.03F));
      float rz = (float)Math.toDegrees((double)(MathHelper.func_76134_b(rotFloat * 0.09F) * 0.05F + 0.05F));
      float rrx = 0.0F;
      float rry = 0.0F;
      float rrz = 0.0F;
      float rlx = 0.0F;
      float rly = 0.0F;
      float rlz = 0.0F;
      if (holding) {
         rrx = 90.0F - rz / 2.0F;
         rrz = -2.0F;
         rlx = 90.0F - rz / 2.0F;
         rlz = 2.0F;
      } else {
         if (aflag) {
            rrx = rx * 2.0F;
            rlx = -rx * 2.0F;
         } else {
            f1 = MathHelper.func_76134_b(limbSwing * 0.6662F + 3.1415927F) * 2.0F * prevLimbSwing * 0.5F;
            rrx = (float)(Math.toDegrees((double)f1) + (double)rx);
            f1 = MathHelper.func_76134_b(limbSwing * 0.6662F) * 2.0F * prevLimbSwing * 0.5F;
            rlx = (float)(Math.toDegrees((double)f1) - (double)rx);
         }

         rrz += rz + 2.0F;
         rlz -= rz + 2.0F;
      }

      float lean;
      if (this.swingProgress > 0.0F) {
         lean = -MathHelper.func_76126_a(MathHelper.func_76129_c(this.swingProgress) * 3.1415927F * 2.0F) * 0.2F;
         bry = (float)Math.toDegrees((double)lean);
         rrz = -((float)Math.toDegrees((double)(MathHelper.func_76126_a(lean) * 3.0F)));
         rrx = (float)Math.toDegrees((double)(-MathHelper.func_76134_b(lean) * 5.0F));
         rry += bry;
      }

      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GlStateManager.func_179114_b(bry, 0.0F, 1.0F, 0.0F);
      lean = 25.0F;
      if (aflag) {
         lean = 75.0F;
      }

      GlStateManager.func_179114_b((float)(speed * (double)lean), -1.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b((float)(speed * (double)lean * 0.06D * (double)(entity.field_70177_z - entity.field_70126_B)), 0.0F, 0.0F, -1.0F);
      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(0.0D, 0.5D, 0.0D);
      this.func_110776_a(matTexture);
      this.baseModel.renderPart("chest");
      this.baseModel.renderPart("waist");
      if (entity.getGolemColor() > 0) {
         Color c = new Color(EnumDyeColor.func_176764_b(entity.getGolemColor() - 1).func_176768_e().field_76291_p);
         GL11.glColor4f((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 1.0F);
         this.baseModel.renderPart("flag");
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      }

      Iterator var29 = ((ArrayList)((HashMap)this.partsCache.get(entity.func_145782_y())).get(PartModel.EnumAttachPoint.BODY)).iterator();

      PartModel part;
      while(var29.hasNext()) {
         part = (PartModel)var29.next();
         this.renderPart(entity, part.getObjModel().toString(), part, matTexture, partialTicks, PartModel.EnumLimbSide.MIDDLE);
      }

      GlStateManager.func_179121_F();
      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(0.0D, 0.75D, -0.03125D);
      GlStateManager.func_179114_b(headPitch, 0.0F, -1.0F, 0.0F);
      GlStateManager.func_179114_b(headYaw, -1.0F, 0.0F, 0.0F);
      var29 = ((ArrayList)((HashMap)this.partsCache.get(entity.func_145782_y())).get(PartModel.EnumAttachPoint.HEAD)).iterator();

      while(var29.hasNext()) {
         part = (PartModel)var29.next();
         this.renderPart(entity, part.getObjModel().toString(), part, matTexture, partialTicks, PartModel.EnumLimbSide.MIDDLE);
      }

      this.func_110776_a(matTexture);
      this.baseModel.renderPart("head");
      GlStateManager.func_179121_F();
      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(0.20625D, 0.6875D, 0.0D);
      var29 = ((ArrayList)((HashMap)this.partsCache.get(entity.func_145782_y())).get(PartModel.EnumAttachPoint.ARMS)).iterator();
      if (var29.hasNext()) {
         part = (PartModel)var29.next();
         rrx = part.preRenderArmRotationX(entity, partialTicks, PartModel.EnumLimbSide.RIGHT, rrx);
         rry = part.preRenderArmRotationY(entity, partialTicks, PartModel.EnumLimbSide.RIGHT, rry);
         rrz = part.preRenderArmRotationZ(entity, partialTicks, PartModel.EnumLimbSide.RIGHT, rrz);
      }

      GlStateManager.func_179114_b(rrx, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(rry, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(rrz, 0.0F, 0.0F, 1.0F);
      this.func_110776_a(matTexture);
      this.baseModel.renderPart("arm");
      var29 = ((ArrayList)((HashMap)this.partsCache.get(entity.func_145782_y())).get(PartModel.EnumAttachPoint.ARMS)).iterator();

      while(var29.hasNext()) {
         part = (PartModel)var29.next();
         this.renderPart(entity, part.getObjModel().toString(), part, matTexture, partialTicks, PartModel.EnumLimbSide.RIGHT);
      }

      GlStateManager.func_179121_F();
      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(-0.20625D, 0.6875D, 0.0D);
      var29 = ((ArrayList)((HashMap)this.partsCache.get(entity.func_145782_y())).get(PartModel.EnumAttachPoint.ARMS)).iterator();
      if (var29.hasNext()) {
         part = (PartModel)var29.next();
         rlx = part.preRenderArmRotationX(entity, partialTicks, PartModel.EnumLimbSide.LEFT, rlx);
         rly = part.preRenderArmRotationY(entity, partialTicks, PartModel.EnumLimbSide.LEFT, rly);
         rlz = part.preRenderArmRotationZ(entity, partialTicks, PartModel.EnumLimbSide.LEFT, rlz);
      }

      GlStateManager.func_179114_b(rlx, 1.0F, 0.0F, 0.0F);
      GlStateManager.func_179114_b(rly + 180.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(rlz, 0.0F, 0.0F, -1.0F);
      this.func_110776_a(matTexture);
      this.baseModel.renderPart("arm");
      var29 = ((ArrayList)((HashMap)this.partsCache.get(entity.func_145782_y())).get(PartModel.EnumAttachPoint.ARMS)).iterator();

      while(var29.hasNext()) {
         part = (PartModel)var29.next();
         this.renderPart(entity, part.getObjModel().toString(), part, matTexture, partialTicks, PartModel.EnumLimbSide.LEFT);
      }

      GlStateManager.func_179121_F();
      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(0.09375D, 0.375D, 0.0D);
      f1 = MathHelper.func_76134_b(limbSwing * 0.6662F) * prevLimbSwing;
      GlStateManager.func_179114_b((float)Math.toDegrees((double)f1), 1.0F, 0.0F, 0.0F);
      var29 = ((ArrayList)((HashMap)this.partsCache.get(entity.func_145782_y())).get(PartModel.EnumAttachPoint.LEGS)).iterator();

      while(var29.hasNext()) {
         part = (PartModel)var29.next();
         this.renderPart(entity, part.getObjModel().toString(), part, matTexture, partialTicks, PartModel.EnumLimbSide.RIGHT);
      }

      GlStateManager.func_179121_F();
      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(-0.09375D, 0.375D, 0.0D);
      f1 = MathHelper.func_76134_b(limbSwing * 0.6662F + 3.1415927F) * prevLimbSwing;
      GlStateManager.func_179114_b((float)Math.toDegrees((double)f1), 1.0F, 0.0F, 0.0F);
      var29 = ((ArrayList)((HashMap)this.partsCache.get(entity.func_145782_y())).get(PartModel.EnumAttachPoint.LEGS)).iterator();

      while(var29.hasNext()) {
         part = (PartModel)var29.next();
         this.renderPart(entity, part.getObjModel().toString(), part, matTexture, partialTicks, PartModel.EnumLimbSide.LEFT);
      }

      GlStateManager.func_179121_F();
      GL11.glDisable(3042);
      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(0.0D, 0.625D, 0.0D);
      GlStateManager.func_179114_b(90.0F - rz * 0.5F, 1.0F, 0.0F, 0.0F);
      this.drawHeldItem(entity);
      GlStateManager.func_179121_F();
   }

   private void drawHeldItem(EntityThaumcraftGolem entity) {
      ItemStack itemstack = entity.func_184614_ca();
      if (itemstack != null) {
         GlStateManager.func_179094_E();
         Item item = itemstack.func_77973_b();
         Minecraft minecraft = Minecraft.func_71410_x();
         GlStateManager.func_179114_b(90.0F, -1.0F, 0.0F, 0.0F);
         GlStateManager.func_179139_a(0.375D, 0.375D, 0.375D);
         GlStateManager.func_179109_b(0.0F, 0.33F, -1.0F);
         if (!(item instanceof ItemBlock)) {
            GlStateManager.func_179109_b(0.0F, 0.0F, 0.25F);
         }

         minecraft.func_175597_ag().func_178099_a(entity, itemstack, TransformType.HEAD);
         GlStateManager.func_179121_F();
      }

   }

   private void renderPart(EntityThaumcraftGolem golem, String partName, PartModel part, ResourceLocation matTexture, float partialTicks, PartModel.EnumLimbSide side) {
      IModelCustom model = (IModelCustom)this.models.get(partName);
      if (model == null) {
         model = AdvancedModelLoader.loadModel(part.getObjModel());
         if (model == null) {
            return;
         }

         this.models.put(partName, model);
      }

      String[] var8 = model.getPartNames();
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         String op = var8[var10];
         GL11.glPushMatrix();
         if (part.useMaterialTextureForObjectPart(op)) {
            this.func_110776_a(matTexture);
         } else {
            this.func_110776_a(part.getTexture());
         }

         part.preRenderObjectPart(op, golem, partialTicks, side);
         model.renderPart(op);
         part.postRenderObjectPart(op, golem, partialTicks, side);
         GL11.glPopMatrix();
      }

   }

   private void doRender(EntityThaumcraftGolem entity, double x, double y, double z, float p_76986_8_, float partialTicks) {
      if (!MinecraftForge.EVENT_BUS.post(new Pre(entity, this, x, y, z))) {
         GlStateManager.func_179094_E();
         GlStateManager.func_179129_p();
         this.swingProgress = this.func_77040_d(entity, partialTicks);

         try {
            float f2 = this.func_77034_a(entity.field_70760_ar, entity.field_70761_aq, partialTicks);
            float f3 = this.func_77034_a(entity.field_70758_at, entity.field_70759_as, partialTicks);
            float f4 = f3 - f2;
            float f5;
            if (entity.func_184218_aH() && entity.func_184187_bx() instanceof EntityLivingBase) {
               EntityLivingBase entitylivingbase1 = (EntityLivingBase)entity.func_184187_bx();
               f2 = this.func_77034_a(entitylivingbase1.field_70760_ar, entitylivingbase1.field_70761_aq, partialTicks);
               f4 = f3 - f2;
               f5 = MathHelper.func_76142_g(f4);
               if (f5 < -85.0F) {
                  f5 = -85.0F;
               }

               if (f5 >= 85.0F) {
                  f5 = 85.0F;
               }

               f2 = f3 - f5;
               if (f5 * f5 > 2500.0F) {
                  f2 += f5 * 0.2F;
               }
            }

            float f9 = entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * partialTicks;
            this.func_77039_a(entity, x, y, z);
            f5 = this.func_77044_a(entity, partialTicks);
            this.func_77043_a(entity, f5, f2, partialTicks);
            GlStateManager.func_179091_B();
            this.func_77041_b(entity, partialTicks);
            float f7 = entity.field_184618_aE + (entity.field_70721_aZ - entity.field_184618_aE) * partialTicks;
            float f8 = entity.field_184619_aG - entity.field_70721_aZ * (1.0F - partialTicks);
            if (f7 > 1.0F) {
               f7 = 1.0F;
            }

            GlStateManager.func_179141_d();
            boolean flag;
            if (this.field_188301_f) {
               flag = this.func_177088_c(entity);
               this.renderModel(entity, f8, f7, f5, f4, f9, 0.0625F, partialTicks);
               if (flag) {
                  this.func_180565_e();
               }
            } else {
               flag = this.func_177090_c(entity, partialTicks);
               this.renderModel(entity, f8, f7, f5, f4, f9, 0.0625F, partialTicks);
               if (flag) {
                  this.func_177091_f();
               }

               GlStateManager.func_179132_a(true);
               this.func_177093_a(entity, f8, f7, partialTicks, f5, f4, f9, 0.0625F);
            }

            GlStateManager.func_179101_C();
         } catch (Exception var18) {
            logger.error("Couldn't render entity", var18);
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
         this.func_110827_b(entity, x, y, z, p_76986_8_, partialTicks);
      }
   }

   private void createPartsCache(EntityThaumcraftGolem golem) {
      HashMap<PartModel.EnumAttachPoint, ArrayList<PartModel>> pl = new HashMap();
      pl.put(PartModel.EnumAttachPoint.BODY, new ArrayList());
      pl.put(PartModel.EnumAttachPoint.HEAD, new ArrayList());
      pl.put(PartModel.EnumAttachPoint.ARMS, new ArrayList());
      pl.put(PartModel.EnumAttachPoint.LEGS, new ArrayList());
      IGolemProperties props = golem.getProperties();
      if (props.getHead().model != null) {
         ((ArrayList)pl.get(props.getHead().model.getAttachPoint())).add(props.getHead().model);
      }

      if (props.getArms().model != null) {
         ((ArrayList)pl.get(props.getArms().model.getAttachPoint())).add(props.getArms().model);
      }

      if (props.getLegs().model != null) {
         ((ArrayList)pl.get(props.getLegs().model.getAttachPoint())).add(props.getLegs().model);
      }

      if (props.getAddon().model != null) {
         ((ArrayList)pl.get(props.getAddon().model.getAttachPoint())).add(props.getAddon().model);
      }

      this.partsCache.put(golem.func_145782_y(), pl);
   }

   public void func_76986_a(EntityLiving entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      this.doRender((EntityThaumcraftGolem)entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }

   protected ResourceLocation func_110775_a(EntityLiving p_110775_1_) {
      return null;
   }
}
