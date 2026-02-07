package thaumcraft.client.lib;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.common.config.Config;

public class UtilsFX {
   public static final ResourceLocation nodeTexture = new ResourceLocation("thaumcraft", "textures/misc/auranodes.png");
   public static final VertexFormat VERTEXFORMAT_POS_TEX_CO_LM_NO;
   public static final String[] colorNames;
   public static final String[] colorCodes;
   public static final int[] colors;
   public static float sysPartialTicks;
   static DecimalFormat myFormatter;
   public static boolean hideStackOverlay;

   public static void renderFacingQuad(double px, double py, double pz, int gridX, int gridY, int frame, float scale, int color, float alpha, int blend, float partialTicks) {
      if (Minecraft.func_71410_x().func_175606_aa() instanceof EntityPlayer) {
         Tessellator tessellator = Tessellator.func_178181_a();
         VertexBuffer wr = tessellator.func_178180_c();
         float arX = ActiveRenderInfo.func_178808_b();
         float arZ = ActiveRenderInfo.func_178803_d();
         float arYZ = ActiveRenderInfo.func_178805_e();
         float arXY = ActiveRenderInfo.func_178807_f();
         float arXZ = ActiveRenderInfo.func_178809_c();
         EntityPlayer player = (EntityPlayer)Minecraft.func_71410_x().func_175606_aa();
         double iPX = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * (double)partialTicks;
         double iPY = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * (double)partialTicks;
         double iPZ = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * (double)partialTicks;
         GlStateManager.func_179094_E();
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, blend);
         GlStateManager.func_179092_a(516, 0.003921569F);
         GlStateManager.func_179132_a(false);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glTranslated(-iPX, -iPY, -iPZ);
         Vec3d v1 = new Vec3d((double)(-arX * scale - arYZ * scale), (double)(-arXZ * scale), (double)(-arZ * scale - arXY * scale));
         Vec3d v2 = new Vec3d((double)(-arX * scale + arYZ * scale), (double)(arXZ * scale), (double)(-arZ * scale + arXY * scale));
         Vec3d v3 = new Vec3d((double)(arX * scale + arYZ * scale), (double)(arXZ * scale), (double)(arZ * scale + arXY * scale));
         Vec3d v4 = new Vec3d((double)(arX * scale - arYZ * scale), (double)(-arXZ * scale), (double)(arZ * scale - arXY * scale));
         int xm = frame % gridX;
         int ym = frame / gridY;
         float f1 = (float)xm / (float)gridX;
         float f2 = f1 + 1.0F / (float)gridX;
         float f3 = (float)ym / (float)gridY;
         float f4 = f3 + 1.0F / (float)gridY;
         TexturedQuadTC quad = new TexturedQuadTC(new PositionTextureVertex[]{new PositionTextureVertex((float)px + (float)v1.field_72450_a, (float)py + (float)v1.field_72448_b, (float)pz + (float)v1.field_72449_c, f2, f4), new PositionTextureVertex((float)px + (float)v2.field_72450_a, (float)py + (float)v2.field_72448_b, (float)pz + (float)v2.field_72449_c, f2, f3), new PositionTextureVertex((float)px + (float)v3.field_72450_a, (float)py + (float)v3.field_72448_b, (float)pz + (float)v3.field_72449_c, f1, f3), new PositionTextureVertex((float)px + (float)v4.field_72450_a, (float)py + (float)v4.field_72448_b, (float)pz + (float)v4.field_72449_c, f1, f4)});
         quad.draw(tessellator.func_178180_c(), 1.0F, 220, color, alpha);
         GlStateManager.func_179132_a(true);
         GlStateManager.func_179112_b(770, 771);
         GlStateManager.func_179084_k();
         GlStateManager.func_179092_a(516, 0.1F);
         GlStateManager.func_179121_F();
      }

   }

   public static void drawTexturedQuad(float par1, float par2, float par3, float par4, float par5, float par6, double zLevel) {
      float var7 = 0.00390625F;
      float var8 = 0.00390625F;
      Tessellator var9 = Tessellator.func_178181_a();
      var9.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181707_g);
      var9.func_178180_c().func_181662_b((double)(par1 + 0.0F), (double)(par2 + par6), zLevel).func_187315_a((double)((par3 + 0.0F) * var7), (double)((par4 + par6) * var8)).func_181675_d();
      var9.func_178180_c().func_181662_b((double)(par1 + par5), (double)(par2 + par6), zLevel).func_187315_a((double)((par3 + par5) * var7), (double)((par4 + par6) * var8)).func_181675_d();
      var9.func_178180_c().func_181662_b((double)(par1 + par5), (double)(par2 + 0.0F), zLevel).func_187315_a((double)((par3 + par5) * var7), (double)((par4 + 0.0F) * var8)).func_181675_d();
      var9.func_178180_c().func_181662_b((double)(par1 + 0.0F), (double)(par2 + 0.0F), zLevel).func_187315_a((double)((par3 + 0.0F) * var7), (double)((par4 + 0.0F) * var8)).func_181675_d();
      var9.func_78381_a();
   }

   public static void drawTexturedQuadF(float par1, float par2, float par3, float par4, float par5, float par6, double zLevel) {
      float d = 0.0625F;
      Tessellator var9 = Tessellator.func_178181_a();
      var9.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181707_g);
      var9.func_178180_c().func_181662_b((double)(par1 + 0.0F), (double)(par2 + 16.0F), zLevel).func_187315_a((double)((par3 + 0.0F) * d), (double)((par4 + par6) * d)).func_181675_d();
      var9.func_178180_c().func_181662_b((double)(par1 + 16.0F), (double)(par2 + 16.0F), zLevel).func_187315_a((double)((par3 + par5) * d), (double)((par4 + par6) * d)).func_181675_d();
      var9.func_178180_c().func_181662_b((double)(par1 + 16.0F), (double)(par2 + 0.0F), zLevel).func_187315_a((double)((par3 + par5) * d), (double)((par4 + 0.0F) * d)).func_181675_d();
      var9.func_178180_c().func_181662_b((double)(par1 + 0.0F), (double)(par2 + 0.0F), zLevel).func_187315_a((double)((par3 + 0.0F) * d), (double)((par4 + 0.0F) * d)).func_181675_d();
      var9.func_78381_a();
   }

   public static void drawTexturedQuadFull(float par1, float par2, double zLevel) {
      Tessellator var9 = Tessellator.func_178181_a();
      var9.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181707_g);
      var9.func_178180_c().func_181662_b((double)(par1 + 0.0F), (double)(par2 + 16.0F), zLevel).func_187315_a(0.0D, 1.0D).func_181675_d();
      var9.func_178180_c().func_181662_b((double)(par1 + 16.0F), (double)(par2 + 16.0F), zLevel).func_187315_a(1.0D, 1.0D).func_181675_d();
      var9.func_178180_c().func_181662_b((double)(par1 + 16.0F), (double)(par2 + 0.0F), zLevel).func_187315_a(1.0D, 0.0D).func_181675_d();
      var9.func_178180_c().func_181662_b((double)(par1 + 0.0F), (double)(par2 + 0.0F), zLevel).func_187315_a(0.0D, 0.0D).func_181675_d();
      var9.func_78381_a();
   }

   public static void renderItemInGUI(int x, int y, int z, ItemStack stack) {
      Minecraft mc = Minecraft.func_71410_x();

      try {
         GlStateManager.func_179094_E();
         RenderHelper.func_74520_c();
         GlStateManager.func_179140_f();
         GlStateManager.func_179091_B();
         GlStateManager.func_179142_g();
         GlStateManager.func_179145_e();
         mc.func_175599_af().field_77023_b = (float)z;
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
         mc.func_175599_af().func_180450_b(stack, x, y);
         mc.func_175599_af().field_77023_b = 0.0F;
         GlStateManager.func_179140_f();
         GlStateManager.func_179121_F();
         GlStateManager.func_179145_e();
         GlStateManager.func_179126_j();
         RenderHelper.func_74519_b();
      } catch (Exception var6) {
      }

   }

   public static void renderQuadCentered(ResourceLocation texture, float scale, float red, float green, float blue, int brightness, int blend, float opacity) {
      Minecraft.func_71410_x().field_71446_o.func_110577_a(texture);
      renderQuadCentered(1, 1, 0, scale, red, green, blue, brightness, blend, opacity);
   }

   public static void renderQuadCentered(ResourceLocation texture, int gridX, int gridY, int frame, float scale, float red, float green, float blue, int brightness, int blend, float opacity) {
      Minecraft.func_71410_x().field_71446_o.func_110577_a(texture);
      renderQuadCentered(gridX, gridY, frame, scale, red, green, blue, brightness, blend, opacity);
   }

   public static void renderQuadCentered() {
      renderQuadCentered(1, 1, 0, 1.0F, 1.0F, 1.0F, 1.0F, 200, 771, 1.0F);
   }

   public static void renderQuadCentered(int gridX, int gridY, int frame, float scale, float red, float green, float blue, int brightness, int blend, float opacity) {
      Tessellator tessellator = Tessellator.func_178181_a();
      boolean blendon = GL11.glIsEnabled(3042);
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, blend);
      int xm = frame % gridX;
      int ym = frame / gridY;
      float f1 = (float)xm / (float)gridX;
      float f2 = f1 + 1.0F / (float)gridX;
      float f3 = (float)ym / (float)gridY;
      float f4 = f3 + 1.0F / (float)gridY;
      Color c = new Color(red, green, blue);
      TexturedQuadTC quad = new TexturedQuadTC(new PositionTextureVertex[]{new PositionTextureVertex(-0.5F, 0.5F, 0.0F, f2, f4), new PositionTextureVertex(0.5F, 0.5F, 0.0F, f2, f3), new PositionTextureVertex(0.5F, -0.5F, 0.0F, f1, f3), new PositionTextureVertex(-0.5F, -0.5F, 0.0F, f1, f4)});
      quad.draw(tessellator.func_178180_c(), scale, brightness, c.getRGB(), opacity);
      GlStateManager.func_179112_b(770, 771);
      if (!blendon) {
         GlStateManager.func_179084_k();
      }

   }

   public static void renderQuadFromIcon(TextureAtlasSprite icon, float scale, float red, float green, float blue, int brightness, int blend, float opacity) {
      boolean blendon = GL11.glIsEnabled(3042);
      Minecraft.func_71410_x().field_71446_o.func_110577_a(TextureMap.field_110575_b);
      Tessellator tessellator = Tessellator.func_178181_a();
      float f1 = icon.func_94212_f();
      float f2 = icon.func_94206_g();
      float f3 = icon.func_94209_e();
      float f4 = icon.func_94210_h();
      GL11.glScalef(scale, scale, scale);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, blend);
      GL11.glColor4f(red, green, blue, opacity);
      if (brightness > -1) {
         tessellator.func_178180_c().func_181668_a(7, VERTEXFORMAT_POS_TEX_CO_LM_NO);
      } else {
         tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181712_l);
      }

      int j = brightness >> 16 & '\uffff';
      int k = brightness & '\uffff';
      tessellator.func_178180_c().func_181662_b(0.0D, 0.0D, 0.0D).func_187315_a((double)f1, (double)f4).func_181666_a(red, green, blue, opacity);
      if (brightness > -1) {
         tessellator.func_178180_c().func_187314_a(j, k);
      }

      tessellator.func_178180_c().func_181663_c(0.0F, 0.0F, 1.0F);
      tessellator.func_178180_c().func_181675_d();
      tessellator.func_178180_c().func_181662_b(1.0D, 0.0D, 0.0D).func_187315_a((double)f3, (double)f4).func_181666_a(red, green, blue, opacity);
      if (brightness > -1) {
         tessellator.func_178180_c().func_187314_a(j, k);
      }

      tessellator.func_178180_c().func_181663_c(0.0F, 0.0F, 1.0F);
      tessellator.func_178180_c().func_181675_d();
      tessellator.func_178180_c().func_181662_b(1.0D, 1.0D, 0.0D).func_187315_a((double)f3, (double)f2).func_181666_a(red, green, blue, opacity);
      if (brightness > -1) {
         tessellator.func_178180_c().func_187314_a(j, k);
      }

      tessellator.func_178180_c().func_181663_c(0.0F, 0.0F, 1.0F);
      tessellator.func_178180_c().func_181675_d();
      tessellator.func_178180_c().func_181662_b(0.0D, 1.0D, 0.0D).func_187315_a((double)f1, (double)f2).func_181666_a(red, green, blue, opacity);
      if (brightness > -1) {
         tessellator.func_178180_c().func_187314_a(j, k);
      }

      tessellator.func_178180_c().func_181663_c(0.0F, 0.0F, 1.0F);
      tessellator.func_178180_c().func_181675_d();
      tessellator.func_78381_a();
      GlStateManager.func_179112_b(770, 771);
      if (!blendon) {
         GL11.glDisable(3042);
      }

   }

   public static void drawTag(int x, int y, Aspect aspect, float amount, int bonus, double z, int blend, float alpha) {
      drawTag(x, y, aspect, amount, bonus, z, blend, alpha, false);
   }

   public static void drawTag(int x, int y, Aspect aspect, float amt, int bonus, double z) {
      drawTag(x, y, aspect, amt, bonus, z, 771, 1.0F, false);
   }

   public static void drawTag(int x, int y, Aspect aspect) {
      drawTag(x, y, aspect, 0.0F, 0, 0.0D, 771, 1.0F, true);
   }

   public static void drawTag(int x, int y, Aspect aspect, float amount, int bonus, double z, int blend, float alpha, boolean bw) {
      drawTag((double)x, (double)y, aspect, amount, bonus, z, blend, alpha, bw);
   }

   public static void drawTag(double x, double y, Aspect aspect, float amount, int bonus, double z, int blend, float alpha, boolean bw) {
      if (aspect != null) {
         boolean blendon = GL11.glIsEnabled(3042);
         Minecraft mc = Minecraft.func_71410_x();
         boolean isLightingEnabled = GL11.glIsEnabled(2896);
         Color color = new Color(aspect.getColor());
         GL11.glPushMatrix();
         GL11.glDisable(2896);
         GL11.glAlphaFunc(516, 0.003921569F);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, blend);
         GL11.glPushMatrix();
         mc.field_71446_o.func_110577_a(aspect.getImage());
         if (!bw) {
            GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, alpha);
         } else {
            GL11.glColor4f(0.1F, 0.1F, 0.1F, alpha * 0.8F);
         }

         Tessellator var9 = Tessellator.func_178181_a();
         var9.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181709_i);
         if (!bw) {
            var9.func_178180_c().func_181662_b(x + 0.0D, y + 16.0D, z).func_187315_a(0.0D, 1.0D).func_181666_a((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, alpha).func_181675_d();
            var9.func_178180_c().func_181662_b(x + 16.0D, y + 16.0D, z).func_187315_a(1.0D, 1.0D).func_181666_a((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, alpha).func_181675_d();
            var9.func_178180_c().func_181662_b(x + 16.0D, y + 0.0D, z).func_187315_a(1.0D, 0.0D).func_181666_a((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, alpha).func_181675_d();
            var9.func_178180_c().func_181662_b(x + 0.0D, y + 0.0D, z).func_187315_a(0.0D, 0.0D).func_181666_a((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, alpha).func_181675_d();
         } else {
            var9.func_178180_c().func_181662_b(x + 0.0D, y + 16.0D, z).func_187315_a(0.0D, 1.0D).func_181666_a(0.1F, 0.1F, 0.1F, alpha * 0.8F).func_181675_d();
            var9.func_178180_c().func_181662_b(x + 16.0D, y + 16.0D, z).func_187315_a(1.0D, 1.0D).func_181666_a(0.1F, 0.1F, 0.1F, alpha * 0.8F).func_181675_d();
            var9.func_178180_c().func_181662_b(x + 16.0D, y + 0.0D, z).func_187315_a(1.0D, 0.0D).func_181666_a(0.1F, 0.1F, 0.1F, alpha * 0.8F).func_181675_d();
            var9.func_178180_c().func_181662_b(x + 0.0D, y + 0.0D, z).func_187315_a(0.0D, 0.0D).func_181666_a(0.1F, 0.1F, 0.1F, alpha * 0.8F).func_181675_d();
         }

         var9.func_78381_a();
         GL11.glPopMatrix();
         if (amount > 0.0F) {
            GL11.glPushMatrix();
            float q = 0.5F;
            if (!Config.largeTagText) {
               GL11.glScalef(0.5F, 0.5F, 0.5F);
               q = 1.0F;
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            String am = myFormatter.format((double)amount);
            int sw = mc.field_71466_p.func_78256_a(am);
            mc.field_71466_p.func_175063_a(am, (float)(32 - sw + (int)x * 2) * q, (float)(32 - mc.field_71466_p.field_78288_b + (int)y * 2) * q, 16777215);
            GL11.glPopMatrix();
         }

         if (bonus > 0) {
            GL11.glPushMatrix();
            mc.field_71446_o.func_110577_a(ParticleEngine.particleTexture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int px = 16 * (mc.field_71439_g.field_70173_aa % 16);
            drawTexturedQuad((float)((int)x - 4), (float)((int)y - 4), (float)px, 80.0F, 16.0F, 16.0F, z);
            if (bonus > 1) {
               float q = 0.5F;
               if (!Config.largeTagText) {
                  GL11.glScalef(0.5F, 0.5F, 0.5F);
                  q = 1.0F;
               }

               String am = "" + bonus;
               int sw = mc.field_71466_p.func_78256_a(am) / 2;
               GL11.glTranslated(0.0D, 0.0D, -1.0D);
               mc.field_71466_p.func_175063_a(am, (float)(8 - sw + (int)x * 2) * q, (float)(15 - mc.field_71466_p.field_78288_b + (int)y * 2) * q, 16777215);
            }

            GL11.glPopMatrix();
         }

         GlStateManager.func_179112_b(770, 771);
         if (!blendon) {
            GL11.glDisable(3042);
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glAlphaFunc(516, 0.1F);
         if (isLightingEnabled) {
            GL11.glEnable(2896);
         } else {
            GL11.glDisable(2896);
         }

         GL11.glPopMatrix();
      }
   }

   public static void drawCustomTooltip(GuiScreen gui, FontRenderer fr, List textList, int x, int y, int subTipColor) {
      drawCustomTooltip(gui, fr, textList, x, y, subTipColor, false);
   }

   public static void drawCustomTooltip(GuiScreen gui, FontRenderer fr, List textList, int x, int y, int subTipColor, boolean ignoremouse) {
      if (!((List)textList).isEmpty()) {
         Minecraft mc = Minecraft.func_71410_x();
         ScaledResolution scaledresolution = new ScaledResolution(mc);
         int sf = scaledresolution.func_78325_e();
         GlStateManager.func_179101_C();
         RenderHelper.func_74518_a();
         GlStateManager.func_179140_f();
         GlStateManager.func_179097_i();
         int max = 240;
         int mx = Mouse.getEventX();
         boolean flip = false;
         if (!ignoremouse && (max + 24) * sf + mx > mc.field_71443_c) {
            max = (mc.field_71443_c - mx) / sf - 24;
            if (max < 120) {
               max = 240;
               flip = true;
            }
         }

         int widestLineWidth = 0;
         Iterator textLineEntry = ((List)textList).iterator();
         boolean b = false;

         while(textLineEntry.hasNext()) {
            String textLine = (String)textLineEntry.next();
            if (fr.func_78256_a(textLine) > max) {
               b = true;
               break;
            }
         }

         if (b) {
            List tl = new ArrayList();
            Iterator var17 = ((List)textList).iterator();

            while(true) {
               if (!var17.hasNext()) {
                  textList = tl;
                  break;
               }

               Object o = var17.next();
               String textLine = (String)o;
               List tl2 = fr.func_78271_c(textLine, textLine.startsWith("@@") ? max * 2 : max);

               String textLine2;
               for(Iterator var21 = tl2.iterator(); var21.hasNext(); tl.add(textLine2)) {
                  Object o2 = var21.next();
                  textLine2 = ((String)o2).trim();
                  if (textLine.startsWith("@@")) {
                     textLine2 = "@@" + textLine2;
                  }
               }
            }
         }

         Iterator textLines = ((List)textList).iterator();

         int totalHeight;
         String textLine;
         int sY;
         for(totalHeight = -2; textLines.hasNext(); totalHeight += textLine.startsWith("@@") && !fr.func_82883_a() ? 7 : 10) {
            textLine = (String)textLines.next();
            sY = fr.func_78256_a(textLine);
            if (textLine.startsWith("@@") && !fr.func_82883_a()) {
               sY /= 2;
            }

            if (sY > widestLineWidth) {
               widestLineWidth = sY;
            }
         }

         int sX = x + 12;
         sY = y - 12;
         if (((List)textList).size() > 1) {
            totalHeight += 2;
         }

         if (flip) {
            sX -= widestLineWidth + 24;
         }

         Minecraft.func_71410_x().func_175599_af().field_77023_b = 300.0F;
         int var10 = -267386864;
         drawGradientRect(sX - 3, sY - 4, sX + widestLineWidth + 3, sY - 3, var10, var10);
         drawGradientRect(sX - 3, sY + totalHeight + 3, sX + widestLineWidth + 3, sY + totalHeight + 4, var10, var10);
         drawGradientRect(sX - 3, sY - 3, sX + widestLineWidth + 3, sY + totalHeight + 3, var10, var10);
         drawGradientRect(sX - 4, sY - 3, sX - 3, sY + totalHeight + 3, var10, var10);
         drawGradientRect(sX + widestLineWidth + 3, sY - 3, sX + widestLineWidth + 4, sY + totalHeight + 3, var10, var10);
         int var11 = 1347420415;
         int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
         drawGradientRect(sX - 3, sY - 3 + 1, sX - 3 + 1, sY + totalHeight + 3 - 1, var11, var12);
         drawGradientRect(sX + widestLineWidth + 2, sY - 3 + 1, sX + widestLineWidth + 3, sY + totalHeight + 3 - 1, var11, var12);
         drawGradientRect(sX - 3, sY - 3, sX + widestLineWidth + 3, sY - 3 + 1, var11, var11);
         drawGradientRect(sX - 3, sY + totalHeight + 2, sX + widestLineWidth + 3, sY + totalHeight + 3, var12, var12);

         for(int i = 0; i < ((List)textList).size(); ++i) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)sX, (float)sY, 0.0F);
            String tl = (String)((List)textList).get(i);
            boolean shift = false;
            GL11.glPushMatrix();
            if (tl.startsWith("@@") && !fr.func_82883_a()) {
               sY += 7;
               GL11.glScalef(0.5F, 0.5F, 1.0F);
               shift = true;
            } else {
               sY += 10;
            }

            tl = tl.replaceAll("@@", "");
            if (subTipColor != -99) {
               if (i == 0) {
                  tl = "§" + Integer.toHexString(subTipColor) + tl;
               } else {
                  tl = "§7" + tl;
               }
            }

            GL11.glTranslated(0.0D, 0.0D, 301.0D);
            fr.func_175063_a(tl, 0.0F, shift ? 3.0F : 0.0F, -1);
            GL11.glPopMatrix();
            if (i == 0) {
               sY += 2;
            }

            GL11.glPopMatrix();
         }

         Minecraft.func_71410_x().func_175599_af().field_77023_b = 0.0F;
         GlStateManager.func_179145_e();
         GlStateManager.func_179126_j();
         RenderHelper.func_74519_b();
         GlStateManager.func_179091_B();
      }

   }

   public static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
      boolean blendon = GL11.glIsEnabled(3042);
      float var7 = (float)(par5 >> 24 & 255) / 255.0F;
      float var8 = (float)(par5 >> 16 & 255) / 255.0F;
      float var9 = (float)(par5 >> 8 & 255) / 255.0F;
      float var10 = (float)(par5 & 255) / 255.0F;
      float var11 = (float)(par6 >> 24 & 255) / 255.0F;
      float var12 = (float)(par6 >> 16 & 255) / 255.0F;
      float var13 = (float)(par6 >> 8 & 255) / 255.0F;
      float var14 = (float)(par6 & 255) / 255.0F;
      GL11.glDisable(3553);
      GL11.glEnable(3042);
      GL11.glDisable(3008);
      GL11.glBlendFunc(770, 771);
      GL11.glShadeModel(7425);
      Tessellator var15 = Tessellator.func_178181_a();
      var15.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181706_f);
      var15.func_178180_c().func_181662_b((double)par3, (double)par2, 300.0D).func_181666_a(var8, var9, var10, var7).func_181675_d();
      var15.func_178180_c().func_181662_b((double)par1, (double)par2, 300.0D).func_181666_a(var8, var9, var10, var7).func_181675_d();
      var15.func_178180_c().func_181662_b((double)par1, (double)par4, 300.0D).func_181666_a(var12, var13, var14, var11).func_181675_d();
      var15.func_178180_c().func_181662_b((double)par3, (double)par4, 300.0D).func_181666_a(var12, var13, var14, var11).func_181675_d();
      var15.func_78381_a();
      GL11.glShadeModel(7424);
      GlStateManager.func_179112_b(770, 771);
      if (!blendon) {
         GL11.glDisable(3042);
      }

      GL11.glEnable(3008);
      GL11.glEnable(3553);
   }

   public static void renderBillboardQuad(double scale) {
      GL11.glPushMatrix();
      rotateToPlayer();
      Tessellator tessellator = Tessellator.func_178181_a();
      tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181710_j);
      tessellator.func_178180_c().func_181662_b(-scale, -scale, 0.0D).func_187315_a(0.0D, 0.0D).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(-scale, scale, 0.0D).func_187315_a(0.0D, 1.0D).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(scale, scale, 0.0D).func_187315_a(1.0D, 1.0D).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(scale, -scale, 0.0D).func_187315_a(1.0D, 0.0D).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_78381_a();
      GL11.glPopMatrix();
   }

   public static void renderBillboardQuad(double scale, int gridX, int gridY, int frame) {
      GL11.glPushMatrix();
      rotateToPlayer();
      int xm = frame % gridX;
      int ym = frame / gridY;
      float f1 = (float)xm / (float)gridX;
      float f2 = f1 + 1.0F / (float)gridX;
      float f3 = (float)ym / (float)gridY;
      float f4 = f3 + 1.0F / (float)gridY;
      Tessellator tessellator = Tessellator.func_178181_a();
      tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181710_j);
      tessellator.func_178180_c().func_181662_b(-scale, -scale, 0.0D).func_187315_a((double)f2, (double)f4).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(-scale, scale, 0.0D).func_187315_a((double)f2, (double)f3).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(scale, scale, 0.0D).func_187315_a((double)f1, (double)f3).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_178180_c().func_181662_b(scale, -scale, 0.0D).func_187315_a((double)f1, (double)f4).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_78381_a();
      GL11.glPopMatrix();
   }

   public static void renderBillboardQuad(double scale, int gridX, int gridY, int frame, float r, float g, float b, float a, int bright) {
      GL11.glPushMatrix();
      rotateToPlayer();
      int xm = frame % gridX;
      int ym = frame / gridY;
      float f1 = (float)xm / (float)gridX;
      float f2 = f1 + 1.0F / (float)gridX;
      float f3 = (float)ym / (float)gridY;
      float f4 = f3 + 1.0F / (float)gridY;
      int j = bright >> 16 & '\uffff';
      int k = bright & '\uffff';
      Tessellator tessellator = Tessellator.func_178181_a();
      tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181704_d);
      tessellator.func_178180_c().func_181662_b(-scale, -scale, 0.0D).func_187315_a((double)f2, (double)f4).func_181666_a(r, g, b, a).func_187314_a(j, k).func_181675_d();
      tessellator.func_178180_c().func_181662_b(-scale, scale, 0.0D).func_187315_a((double)f2, (double)f3).func_181666_a(r, g, b, a).func_187314_a(j, k).func_181675_d();
      tessellator.func_178180_c().func_181662_b(scale, scale, 0.0D).func_187315_a((double)f1, (double)f3).func_181666_a(r, g, b, a).func_187314_a(j, k).func_181675_d();
      tessellator.func_178180_c().func_181662_b(scale, -scale, 0.0D).func_187315_a((double)f1, (double)f4).func_181666_a(r, g, b, a).func_187314_a(j, k).func_181675_d();
      tessellator.func_78381_a();
      GL11.glPopMatrix();
   }

   public static void rotateToPlayer() {
      GL11.glRotatef(-Minecraft.func_71410_x().func_175598_ae().field_78735_i, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(Minecraft.func_71410_x().func_175598_ae().field_78732_j, 1.0F, 0.0F, 0.0F);
   }

   public static boolean renderItemStack(Minecraft mc, ItemStack itm, int x, int y, String txt) {
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      RenderItem itemRender = mc.func_175599_af();
      boolean isLightingEnabled = GL11.glIsEnabled(2896);
      boolean rc = false;
      if (itm != null && itm.func_77973_b() != null) {
         rc = true;
         boolean isRescaleNormalEnabled = GL11.glIsEnabled(32826);
         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, 0.0F, 32.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glEnable(32826);
         GL11.glEnable(2896);
         short short1 = 240;
         short short2 = 240;
         RenderHelper.func_74520_c();
         OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)short1 / 1.0F, (float)short2 / 1.0F);
         itemRender.func_180450_b(itm, x, y);
         if (!hideStackOverlay) {
            itemRender.func_180453_a(mc.field_71466_p, itm, x, y, txt);
         }

         GL11.glPopMatrix();
         if (isRescaleNormalEnabled) {
            GL11.glEnable(32826);
         } else {
            GL11.glDisable(32826);
         }
      }

      if (isLightingEnabled) {
         GL11.glEnable(2896);
      } else {
         GL11.glDisable(2896);
      }

      return rc;
   }

   public static boolean renderItemStackShaded(Minecraft mc, ItemStack itm, int x, int y, String txt, float shade) {
      GlStateManager.func_179131_c(shade, shade, shade, shade);
      RenderItem itemRender = mc.func_175599_af();
      boolean isLightingEnabled = GL11.glIsEnabled(2896);
      boolean rc = false;
      if (itm != null && itm.func_77973_b() != null) {
         rc = true;
         boolean isRescaleNormalEnabled = GL11.glIsEnabled(32826);
         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, 0.0F, 32.0F);
         GlStateManager.func_179131_c(shade, shade, shade, shade);
         GL11.glEnable(32826);
         GL11.glEnable(2896);
         short short1 = 240;
         short short2 = 240;
         RenderHelper.func_74520_c();
         OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)short1 / 1.0F, (float)short2 / 1.0F);
         itemRender.func_180450_b(itm, x, y);
         itemRender.func_180453_a(mc.field_71466_p, itm, x, y, txt);
         GL11.glPopMatrix();
         if (isRescaleNormalEnabled) {
            GL11.glEnable(32826);
         } else {
            GL11.glDisable(32826);
         }
      }

      if (isLightingEnabled) {
         GL11.glEnable(2896);
      } else {
         GL11.glDisable(2896);
      }

      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      return rc;
   }

   public static void drawBeam(UtilsFX.Vector S, UtilsFX.Vector E, UtilsFX.Vector P, float width, int bright) {
      drawBeam(S, E, P, width, bright, 1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void drawBeam(UtilsFX.Vector S, UtilsFX.Vector E, UtilsFX.Vector P, float width, int bright, float r, float g, float b, float a) {
      UtilsFX.Vector PS = Sub(S, P);
      UtilsFX.Vector SE = Sub(E, S);
      UtilsFX.Vector normal = Cross(PS, SE);
      normal = normal.normalize();
      UtilsFX.Vector half = Mul(normal, width);
      UtilsFX.Vector p1 = Add(S, half);
      UtilsFX.Vector p2 = Sub(S, half);
      UtilsFX.Vector p3 = Add(E, half);
      UtilsFX.Vector p4 = Sub(E, half);
      drawQuad(Tessellator.func_178181_a(), p1, p3, p4, p2, bright, r, g, b, a);
   }

   public static void drawQuad(Tessellator tessellator, UtilsFX.Vector p1, UtilsFX.Vector p2, UtilsFX.Vector p3, UtilsFX.Vector p4, int bright, float r, float g, float b, float a) {
      int j = bright >> 16 & '\uffff';
      int k = bright & '\uffff';
      tessellator.func_178180_c().func_181662_b((double)p1.getX(), (double)p1.getY(), (double)p1.getZ()).func_187315_a(0.0D, 0.0D).func_187314_a(j, k).func_181666_a(r, g, b, a).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)p2.getX(), (double)p2.getY(), (double)p2.getZ()).func_187315_a(1.0D, 0.0D).func_187314_a(j, k).func_181666_a(r, g, b, a).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)p3.getX(), (double)p3.getY(), (double)p3.getZ()).func_187315_a(1.0D, 1.0D).func_187314_a(j, k).func_181666_a(r, g, b, a).func_181675_d();
      tessellator.func_178180_c().func_181662_b((double)p4.getX(), (double)p4.getY(), (double)p4.getZ()).func_187315_a(0.0D, 1.0D).func_187314_a(j, k).func_181666_a(r, g, b, a).func_181675_d();
   }

   private static UtilsFX.Vector Cross(UtilsFX.Vector a, UtilsFX.Vector b) {
      float x = a.y * b.z - a.z * b.y;
      float y = a.z * b.x - a.x * b.z;
      float z = a.x * b.y - a.y * b.x;
      return new UtilsFX.Vector(x, y, z);
   }

   public static UtilsFX.Vector Sub(UtilsFX.Vector a, UtilsFX.Vector b) {
      return new UtilsFX.Vector(a.x - b.x, a.y - b.y, a.z - b.z);
   }

   private static UtilsFX.Vector Add(UtilsFX.Vector a, UtilsFX.Vector b) {
      return new UtilsFX.Vector(a.x + b.x, a.y + b.y, a.z + b.z);
   }

   private static UtilsFX.Vector Mul(UtilsFX.Vector a, float f) {
      return new UtilsFX.Vector(a.x * f, a.y * f, a.z * f);
   }

   public static void renderItemIn2D(String sprite, float thickness) {
      renderItemIn2D(Minecraft.func_71410_x().func_147117_R().func_110572_b(sprite), thickness);
   }

   public static void renderItemIn2D(TextureAtlasSprite icon, float thickness) {
      GL11.glPushMatrix();
      float f1 = icon.func_94212_f();
      float f2 = icon.func_94206_g();
      float f3 = icon.func_94209_e();
      float f4 = icon.func_94210_h();
      Minecraft.func_71410_x().field_71446_o.func_110577_a(TextureMap.field_110575_b);
      renderTextureIn3D(f1, f2, f3, f4, 16, 16, thickness);
      GL11.glPopMatrix();
   }

   public static void renderTextureIn3D(float maxu, float maxv, float minu, float minv, int width, int height, float thickness) {
      Tessellator tess = Tessellator.func_178181_a();
      VertexBuffer wr = tess.func_178180_c();
      wr.func_181668_a(7, DefaultVertexFormats.field_181710_j);
      wr.func_181662_b(0.0D, 0.0D, 0.0D).func_187315_a((double)maxu, (double)minv).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
      wr.func_181662_b(1.0D, 0.0D, 0.0D).func_187315_a((double)minu, (double)minv).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
      wr.func_181662_b(1.0D, 1.0D, 0.0D).func_187315_a((double)minu, (double)maxv).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
      wr.func_181662_b(0.0D, 1.0D, 0.0D).func_187315_a((double)maxu, (double)maxv).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
      tess.func_78381_a();
      wr.func_181668_a(7, DefaultVertexFormats.field_181710_j);
      wr.func_181662_b(0.0D, 1.0D, (double)(0.0F - thickness)).func_187315_a((double)maxu, (double)maxv).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
      wr.func_181662_b(1.0D, 1.0D, (double)(0.0F - thickness)).func_187315_a((double)minu, (double)maxv).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
      wr.func_181662_b(1.0D, 0.0D, (double)(0.0F - thickness)).func_187315_a((double)minu, (double)minv).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
      wr.func_181662_b(0.0D, 0.0D, (double)(0.0F - thickness)).func_187315_a((double)maxu, (double)minv).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
      tess.func_78381_a();
      float f5 = 0.5F * (maxu - minu) / (float)width;
      float f6 = 0.5F * (minv - maxv) / (float)height;
      wr.func_181668_a(7, DefaultVertexFormats.field_181710_j);

      int k;
      float f7;
      float f8;
      for(k = 0; k < width; ++k) {
         f7 = (float)k / (float)width;
         f8 = maxu + (minu - maxu) * f7 - f5;
         wr.func_181662_b((double)f7, 0.0D, (double)(0.0F - thickness)).func_187315_a((double)f8, (double)minv).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
         wr.func_181662_b((double)f7, 0.0D, 0.0D).func_187315_a((double)f8, (double)minv).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
         wr.func_181662_b((double)f7, 1.0D, 0.0D).func_187315_a((double)f8, (double)maxv).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
         wr.func_181662_b((double)f7, 1.0D, (double)(0.0F - thickness)).func_187315_a((double)f8, (double)maxv).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
      }

      tess.func_78381_a();
      wr.func_181668_a(7, DefaultVertexFormats.field_181710_j);

      float f9;
      for(k = 0; k < width; ++k) {
         f7 = (float)k / (float)width;
         f8 = maxu + (minu - maxu) * f7 - f5;
         f9 = f7 + 1.0F / (float)width;
         wr.func_181662_b((double)f9, 1.0D, (double)(0.0F - thickness)).func_187315_a((double)f8, (double)maxv).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
         wr.func_181662_b((double)f9, 1.0D, 0.0D).func_187315_a((double)f8, (double)maxv).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
         wr.func_181662_b((double)f9, 0.0D, 0.0D).func_187315_a((double)f8, (double)minv).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
         wr.func_181662_b((double)f9, 0.0D, (double)(0.0F - thickness)).func_187315_a((double)f8, (double)minv).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
      }

      tess.func_78381_a();
      wr.func_181668_a(7, DefaultVertexFormats.field_181710_j);

      for(k = 0; k < height; ++k) {
         f7 = (float)k / (float)height;
         f8 = minv + (maxv - minv) * f7 - f6;
         f9 = f7 + 1.0F / (float)height;
         wr.func_181662_b(0.0D, (double)f9, 0.0D).func_187315_a((double)maxu, (double)f8).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
         wr.func_181662_b(1.0D, (double)f9, 0.0D).func_187315_a((double)minu, (double)f8).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
         wr.func_181662_b(1.0D, (double)f9, (double)(0.0F - thickness)).func_187315_a((double)minu, (double)f8).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
         wr.func_181662_b(0.0D, (double)f9, (double)(0.0F - thickness)).func_187315_a((double)maxu, (double)f8).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      }

      tess.func_78381_a();
      wr.func_181668_a(7, DefaultVertexFormats.field_181710_j);

      for(k = 0; k < height; ++k) {
         f7 = (float)k / (float)height;
         f8 = minv + (maxv - minv) * f7 - f6;
         wr.func_181662_b(1.0D, (double)f7, 0.0D).func_187315_a((double)minu, (double)f8).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
         wr.func_181662_b(0.0D, (double)f7, 0.0D).func_187315_a((double)maxu, (double)f8).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
         wr.func_181662_b(0.0D, (double)f7, (double)(0.0F - thickness)).func_187315_a((double)maxu, (double)f8).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
         wr.func_181662_b(1.0D, (double)f7, (double)(0.0F - thickness)).func_187315_a((double)minu, (double)f8).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
      }

      tess.func_78381_a();
   }

   static {
      VERTEXFORMAT_POS_TEX_CO_LM_NO = (new VertexFormat()).func_181721_a(DefaultVertexFormats.field_181713_m).func_181721_a(DefaultVertexFormats.field_181715_o).func_181721_a(DefaultVertexFormats.field_181714_n).func_181721_a(DefaultVertexFormats.field_181716_p).func_181721_a(DefaultVertexFormats.field_181717_q).func_181721_a(DefaultVertexFormats.field_181718_r);
      colorNames = new String[]{"White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"};
      colorCodes = new String[]{"§f", "§6", "§d", "§9", "§e", "§a", "§d", "§8", "§7", "§b", "§5", "§9", "§4", "§2", "§c", "§8"};
      colors = new int[]{15790320, 15435844, 12801229, 6719955, 14602026, 4312372, 14188952, 4408131, 10526880, 2651799, 8073150, 2437522, 5320730, 3887386, 11743532, 1973019};
      sysPartialTicks = 0.0F;
      myFormatter = new DecimalFormat("#######.##");
      hideStackOverlay = false;
   }

   public static class Vector {
      public final float x;
      public final float y;
      public final float z;

      public Vector(float x, float y, float z) {
         this.x = x;
         this.y = y;
         this.z = z;
      }

      public float getX() {
         return this.x;
      }

      public float getY() {
         return this.y;
      }

      public float getZ() {
         return this.z;
      }

      public float norm() {
         return (float)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
      }

      public UtilsFX.Vector normalize() {
         float n = this.norm();
         return new UtilsFX.Vector(this.x / n, this.y / n, this.z / n);
      }
   }
}
