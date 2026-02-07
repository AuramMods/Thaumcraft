package thaumcraft.client.renderers.tile;

import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.devices.TileMirror;
import thaumcraft.common.tiles.devices.TileMirrorEssentia;

public class TileMirrorRenderer extends TileEntitySpecialRenderer {
   FloatBuffer fBuffer = GLAllocation.func_74529_h(16);
   private static final ResourceLocation t1 = new ResourceLocation("thaumcraft", "textures/misc/tunnel.png");
   private static final ResourceLocation t2 = new ResourceLocation("thaumcraft", "textures/misc/particlefield.png");
   private static ResourceLocation mp = new ResourceLocation("thaumcraft", "textures/blocks/mirrorpane.png");
   private static ResourceLocation mpt = new ResourceLocation("thaumcraft", "textures/blocks/mirrorpanetrans.png");

   public void drawPlaneYPos(TileEntity tileentityendportal, double x, double y, double z, float f) {
      float px = (float)TileEntityRendererDispatcher.field_147554_b;
      float py = (float)TileEntityRendererDispatcher.field_147555_c;
      float pz = (float)TileEntityRendererDispatcher.field_147552_d;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.99F;
      float p = 0.1875F;

      for(int i = 0; i < 16; ++i) {
         GL11.glPushMatrix();
         float f5 = (float)(16 - i);
         float f6 = 0.0625F;
         float f7 = 1.0F / (f5 + 1.0F);
         if (i == 0) {
            this.func_147499_a(t1);
            f7 = 0.1F;
            f5 = 65.0F;
            f6 = 0.125F;
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
         }

         if (i == 1) {
            this.func_147499_a(t2);
            GL11.glEnable(3042);
            GL11.glBlendFunc(1, 1);
            f6 = 0.5F;
         }

         float f8 = (float)(y + (double)offset);
         float f9 = (float)((double)f8 - ActiveRenderInfo.func_178804_a().field_72448_b);
         float f10 = (float)((double)(f8 + f5) - ActiveRenderInfo.func_178804_a().field_72448_b);
         float f11 = f9 / f10;
         f11 += (float)(y + (double)offset);
         GL11.glTranslatef(px, f11, pz);
         GL11.glTexGeni(8192, 9472, 9217);
         GL11.glTexGeni(8193, 9472, 9217);
         GL11.glTexGeni(8194, 9472, 9217);
         GL11.glTexGeni(8195, 9472, 9216);
         GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
         GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
         GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
         GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
         GL11.glEnable(3168);
         GL11.glEnable(3169);
         GL11.glEnable(3170);
         GL11.glEnable(3171);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5890);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
         GL11.glScalef(f6, f6, f6);
         GL11.glTranslatef(0.5F, 0.5F, 0.0F);
         GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
         GL11.glTranslatef(-px, -pz, -py);
         GL11.glTranslated(ActiveRenderInfo.func_178804_a().field_72450_a * (double)f5 / (double)f9, ActiveRenderInfo.func_178804_a().field_72449_c * (double)f5 / (double)f9, (double)(-py));
         Tessellator tessellator = Tessellator.func_178181_a();
         tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181706_f);
         f11 = random.nextFloat() * 0.5F + 0.1F;
         float f12 = random.nextFloat() * 0.5F + 0.4F;
         float f13 = random.nextFloat() * 0.5F + 0.5F;
         if (i == 0) {
            f13 = 1.0F;
            f12 = 1.0F;
            f11 = 1.0F;
         }

         tessellator.func_178180_c().func_181662_b(x + (double)p, y + (double)offset, z + 1.0D - (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + (double)p, y + (double)offset, z + (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + 1.0D - (double)p, y + (double)offset, z + (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + 1.0D - (double)p, y + (double)offset, z + 1.0D - (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_78381_a();
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
      }

      GL11.glDisable(3042);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneYNeg(TileEntity tileentityendportal, double x, double y, double z, float f) {
      float f1 = (float)TileEntityRendererDispatcher.field_147554_b;
      float f2 = (float)TileEntityRendererDispatcher.field_147555_c;
      float f3 = (float)TileEntityRendererDispatcher.field_147552_d;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.01F;
      float p = 0.1875F;

      for(int i = 0; i < 16; ++i) {
         GL11.glPushMatrix();
         float f5 = (float)(16 - i);
         float f6 = 0.0625F;
         float f7 = 1.0F / (f5 + 1.0F);
         if (i == 0) {
            this.func_147499_a(t1);
            f7 = 0.1F;
            f5 = 65.0F;
            f6 = 0.125F;
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
         }

         if (i == 1) {
            this.func_147499_a(t2);
            GL11.glEnable(3042);
            GL11.glBlendFunc(1, 1);
            f6 = 0.5F;
         }

         float f8 = (float)(-(y + (double)offset));
         float f9 = (float)((double)f8 + ActiveRenderInfo.func_178804_a().field_72448_b);
         float f10 = (float)((double)(f8 + f5) + ActiveRenderInfo.func_178804_a().field_72448_b);
         float f11 = f9 / f10;
         f11 += (float)(y + (double)offset);
         GL11.glTranslatef(f1, f11, f3);
         GL11.glTexGeni(8192, 9472, 9217);
         GL11.glTexGeni(8193, 9472, 9217);
         GL11.glTexGeni(8194, 9472, 9217);
         GL11.glTexGeni(8195, 9472, 9216);
         GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
         GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
         GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
         GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
         GL11.glEnable(3168);
         GL11.glEnable(3169);
         GL11.glEnable(3170);
         GL11.glEnable(3171);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5890);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
         GL11.glScalef(f6, f6, f6);
         GL11.glTranslatef(0.5F, 0.5F, 0.0F);
         GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
         GL11.glTranslatef(-f1, -f3, -f2);
         GL11.glTranslated(ActiveRenderInfo.func_178804_a().field_72450_a * (double)f5 / (double)f9, ActiveRenderInfo.func_178804_a().field_72449_c * (double)f5 / (double)f9, (double)(-f2));
         Tessellator tessellator = Tessellator.func_178181_a();
         tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181706_f);
         f11 = random.nextFloat() * 0.5F + 0.1F;
         float f12 = random.nextFloat() * 0.5F + 0.4F;
         float f13 = random.nextFloat() * 0.5F + 0.5F;
         if (i == 0) {
            f13 = 1.0F;
            f12 = 1.0F;
            f11 = 1.0F;
         }

         tessellator.func_178180_c().func_181662_b(x + (double)p, y + (double)offset, z + (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + (double)p, y + (double)offset, z + 1.0D - (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + 1.0D - (double)p, y + (double)offset, z + 1.0D - (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + 1.0D - (double)p, y + (double)offset, z + (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_78381_a();
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
      }

      GL11.glDisable(3042);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneZNeg(TileEntity tileentityendportal, double x, double y, double z, float f) {
      float px = (float)TileEntityRendererDispatcher.field_147554_b;
      float py = (float)TileEntityRendererDispatcher.field_147555_c;
      float pz = (float)TileEntityRendererDispatcher.field_147552_d;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.01F;
      float p = 0.1875F;

      for(int i = 0; i < 16; ++i) {
         GL11.glPushMatrix();
         float f5 = (float)(16 - i);
         float f6 = 0.0625F;
         float f7 = 1.0F / (f5 + 1.0F);
         if (i == 0) {
            this.func_147499_a(t1);
            f7 = 0.1F;
            f5 = 65.0F;
            f6 = 0.125F;
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
         }

         if (i == 1) {
            this.func_147499_a(t2);
            GL11.glEnable(3042);
            GL11.glBlendFunc(1, 1);
            f6 = 0.5F;
         }

         float f8 = (float)(-(z + (double)offset));
         float f9 = (float)((double)f8 + ActiveRenderInfo.func_178804_a().field_72449_c);
         float f10 = (float)((double)(f8 + f5) + ActiveRenderInfo.func_178804_a().field_72449_c);
         float f11 = f9 / f10;
         f11 += (float)(z + (double)offset);
         GL11.glTranslatef(px, py, f11);
         GL11.glTexGeni(8192, 9472, 9217);
         GL11.glTexGeni(8193, 9472, 9217);
         GL11.glTexGeni(8194, 9472, 9217);
         GL11.glTexGeni(8195, 9472, 9216);
         GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
         GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
         GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
         GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
         GL11.glEnable(3168);
         GL11.glEnable(3169);
         GL11.glEnable(3170);
         GL11.glEnable(3171);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5890);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
         GL11.glScalef(f6, f6, f6);
         GL11.glTranslatef(0.5F, 0.5F, 0.0F);
         GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
         GL11.glTranslatef(-px, -py, -pz);
         GL11.glTranslated(ActiveRenderInfo.func_178804_a().field_72450_a * (double)f5 / (double)f9, ActiveRenderInfo.func_178804_a().field_72448_b * (double)f5 / (double)f9, (double)(-pz));
         Tessellator tessellator = Tessellator.func_178181_a();
         tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181706_f);
         f11 = random.nextFloat() * 0.5F + 0.1F;
         float f12 = random.nextFloat() * 0.5F + 0.4F;
         float f13 = random.nextFloat() * 0.5F + 0.5F;
         if (i == 0) {
            f13 = 1.0F;
            f12 = 1.0F;
            f11 = 1.0F;
         }

         tessellator.func_178180_c().func_181662_b(x + (double)p, y + 1.0D - (double)p, z + (double)offset).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + (double)p, y + (double)p, z + (double)offset).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + 1.0D - (double)p, y + (double)p, z + (double)offset).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + 1.0D - (double)p, y + 1.0D - (double)p, z + (double)offset).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_78381_a();
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
      }

      GL11.glDisable(3042);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneZPos(TileEntity tileentityendportal, double x, double y, double z, float f) {
      float px = (float)TileEntityRendererDispatcher.field_147554_b;
      float py = (float)TileEntityRendererDispatcher.field_147555_c;
      float pz = (float)TileEntityRendererDispatcher.field_147552_d;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.99F;
      float p = 0.1875F;

      for(int i = 0; i < 16; ++i) {
         GL11.glPushMatrix();
         float f5 = (float)(16 - i);
         float f6 = 0.0625F;
         float f7 = 1.0F / (f5 + 1.0F);
         if (i == 0) {
            this.func_147499_a(t1);
            f7 = 0.1F;
            f5 = 65.0F;
            f6 = 0.125F;
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
         }

         if (i == 1) {
            this.func_147499_a(t2);
            GL11.glEnable(3042);
            GL11.glBlendFunc(1, 1);
            f6 = 0.5F;
         }

         float f8 = (float)(z + (double)offset);
         float f9 = (float)((double)f8 - ActiveRenderInfo.func_178804_a().field_72449_c);
         float f10 = (float)((double)(f8 + f5) - ActiveRenderInfo.func_178804_a().field_72449_c);
         float f11 = f9 / f10;
         f11 += (float)(z + (double)offset);
         GL11.glTranslatef(px, py, f11);
         GL11.glTexGeni(8192, 9472, 9217);
         GL11.glTexGeni(8193, 9472, 9217);
         GL11.glTexGeni(8194, 9472, 9217);
         GL11.glTexGeni(8195, 9472, 9216);
         GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
         GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
         GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
         GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
         GL11.glEnable(3168);
         GL11.glEnable(3169);
         GL11.glEnable(3170);
         GL11.glEnable(3171);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5890);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
         GL11.glScalef(f6, f6, f6);
         GL11.glTranslatef(0.5F, 0.5F, 0.0F);
         GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
         GL11.glTranslatef(-px, -py, -pz);
         GL11.glTranslated(ActiveRenderInfo.func_178804_a().field_72450_a * (double)f5 / (double)f9, ActiveRenderInfo.func_178804_a().field_72448_b * (double)f5 / (double)f9, (double)(-pz));
         Tessellator tessellator = Tessellator.func_178181_a();
         tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181706_f);
         f11 = random.nextFloat() * 0.5F + 0.1F;
         float f12 = random.nextFloat() * 0.5F + 0.4F;
         float f13 = random.nextFloat() * 0.5F + 0.5F;
         if (i == 0) {
            f13 = 1.0F;
            f12 = 1.0F;
            f11 = 1.0F;
         }

         tessellator.func_178180_c().func_181662_b(x + (double)p, y + (double)p, z + (double)offset).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + (double)p, y + 1.0D - (double)p, z + (double)offset).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + 1.0D - (double)p, y + 1.0D - (double)p, z + (double)offset).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + 1.0D - (double)p, y + (double)p, z + (double)offset).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_78381_a();
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
      }

      GL11.glDisable(3042);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneXNeg(TileEntity tileentityendportal, double x, double y, double z, float f) {
      float px = (float)TileEntityRendererDispatcher.field_147554_b;
      float py = (float)TileEntityRendererDispatcher.field_147555_c;
      float pz = (float)TileEntityRendererDispatcher.field_147552_d;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.01F;
      float p = 0.1875F;

      for(int i = 0; i < 16; ++i) {
         GL11.glPushMatrix();
         float f5 = (float)(16 - i);
         float f6 = 0.0625F;
         float f7 = 1.0F / (f5 + 1.0F);
         if (i == 0) {
            this.func_147499_a(t1);
            f7 = 0.1F;
            f5 = 65.0F;
            f6 = 0.125F;
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
         }

         if (i == 1) {
            this.func_147499_a(t2);
            GL11.glEnable(3042);
            GL11.glBlendFunc(1, 1);
            f6 = 0.5F;
         }

         float f8 = (float)(-(x + (double)offset));
         float f9 = (float)((double)f8 + ActiveRenderInfo.func_178804_a().field_72450_a);
         float f10 = (float)((double)(f8 + f5) + ActiveRenderInfo.func_178804_a().field_72450_a);
         float f11 = f9 / f10;
         f11 += (float)(x + (double)offset);
         GL11.glTranslatef(f11, py, pz);
         GL11.glTexGeni(8192, 9472, 9217);
         GL11.glTexGeni(8193, 9472, 9217);
         GL11.glTexGeni(8194, 9472, 9217);
         GL11.glTexGeni(8195, 9472, 9216);
         GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
         GL11.glTexGen(8192, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
         GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
         GL11.glTexGen(8195, 9474, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
         GL11.glEnable(3168);
         GL11.glEnable(3169);
         GL11.glEnable(3170);
         GL11.glEnable(3171);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5890);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
         GL11.glScalef(f6, f6, f6);
         GL11.glTranslatef(0.5F, 0.5F, 0.0F);
         GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
         GL11.glTranslatef(-pz, -py, -px);
         GL11.glTranslated(ActiveRenderInfo.func_178804_a().field_72449_c * (double)f5 / (double)f9, ActiveRenderInfo.func_178804_a().field_72448_b * (double)f5 / (double)f9, (double)(-px));
         Tessellator tessellator = Tessellator.func_178181_a();
         tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181706_f);
         f11 = random.nextFloat() * 0.5F + 0.1F;
         float f12 = random.nextFloat() * 0.5F + 0.4F;
         float f13 = random.nextFloat() * 0.5F + 0.5F;
         if (i == 0) {
            f13 = 1.0F;
            f12 = 1.0F;
            f11 = 1.0F;
         }

         tessellator.func_178180_c().func_181662_b(x + (double)offset, y + 1.0D - (double)p, z + (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + (double)offset, y + 1.0D - (double)p, z + 1.0D - (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + (double)offset, y + (double)p, z + 1.0D - (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + (double)offset, y + (double)p, z + (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_78381_a();
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
      }

      GL11.glDisable(3042);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneXPos(TileEntity tileentityendportal, double x, double y, double z, float f) {
      float px = (float)TileEntityRendererDispatcher.field_147554_b;
      float py = (float)TileEntityRendererDispatcher.field_147555_c;
      float pz = (float)TileEntityRendererDispatcher.field_147552_d;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.99F;
      float p = 0.1875F;

      for(int i = 0; i < 16; ++i) {
         GL11.glPushMatrix();
         float f5 = (float)(16 - i);
         float f6 = 0.0625F;
         float f7 = 1.0F / (f5 + 1.0F);
         if (i == 0) {
            this.func_147499_a(t1);
            f7 = 0.1F;
            f5 = 65.0F;
            f6 = 0.125F;
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
         }

         if (i == 1) {
            this.func_147499_a(t2);
            GL11.glEnable(3042);
            GL11.glBlendFunc(1, 1);
            f6 = 0.5F;
         }

         float f8 = (float)(x + (double)offset);
         float f9 = (float)((double)f8 - ActiveRenderInfo.func_178804_a().field_72450_a);
         float f10 = (float)((double)(f8 + f5) - ActiveRenderInfo.func_178804_a().field_72450_a);
         float f11 = f9 / f10;
         f11 += (float)(x + (double)offset);
         GL11.glTranslatef(f11, py, pz);
         GL11.glTexGeni(8192, 9472, 9217);
         GL11.glTexGeni(8193, 9472, 9217);
         GL11.glTexGeni(8194, 9472, 9217);
         GL11.glTexGeni(8195, 9472, 9216);
         GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
         GL11.glTexGen(8192, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
         GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
         GL11.glTexGen(8195, 9474, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
         GL11.glEnable(3168);
         GL11.glEnable(3169);
         GL11.glEnable(3170);
         GL11.glEnable(3171);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5890);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
         GL11.glScalef(f6, f6, f6);
         GL11.glTranslatef(0.5F, 0.5F, 0.0F);
         GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
         GL11.glTranslatef(-pz, -py, -px);
         GL11.glTranslated(ActiveRenderInfo.func_178804_a().field_72449_c * (double)f5 / (double)f9, ActiveRenderInfo.func_178804_a().field_72448_b * (double)f5 / (double)f9, (double)(-px));
         Tessellator tessellator = Tessellator.func_178181_a();
         tessellator.func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181706_f);
         f11 = random.nextFloat() * 0.5F + 0.1F;
         float f12 = random.nextFloat() * 0.5F + 0.4F;
         float f13 = random.nextFloat() * 0.5F + 0.5F;
         if (i == 0) {
            f13 = 1.0F;
            f12 = 1.0F;
            f11 = 1.0F;
         }

         tessellator.func_178180_c().func_181662_b(x + (double)offset, y + (double)p, z + (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + (double)offset, y + (double)p, z + 1.0D - (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + (double)offset, y + 1.0D - (double)p, z + 1.0D - (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_178180_c().func_181662_b(x + (double)offset, y + 1.0D - (double)p, z + (double)p).func_181666_a(f11 * f7, f12 * f7, f13 * f7, 1.0F).func_181675_d();
         tessellator.func_78381_a();
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
      }

      GL11.glDisable(3042);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   private FloatBuffer calcFloatBuffer(float f, float f1, float f2, float f3) {
      this.fBuffer.clear();
      this.fBuffer.put(f).put(f1).put(f2).put(f3);
      this.fBuffer.flip();
      return this.fBuffer;
   }

   public void func_180535_a(TileEntity te, double x, double y, double z, float f, int q) {
      EnumFacing dir = BlockStateUtils.getFacing(te.func_145832_p());
      boolean linked = false;
      if (te instanceof TileMirror) {
         linked = ((TileMirror)te).linked;
      }

      if (te instanceof TileMirrorEssentia) {
         linked = ((TileMirrorEssentia)te).linked;
      }

      int b = te.func_145838_q().func_185484_c(te.func_145831_w().func_180495_p(te.func_174877_v()), te.func_145831_w(), te.func_174877_v());
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      this.translateFromOrientation((float)x, (float)y, (float)z, dir.ordinal(), 0.01F);
      UtilsFX.renderItemIn2D(te.func_145838_q() == BlocksTC.mirror ? "thaumcraft:blocks/mirrorframe" : "thaumcraft:blocks/mirrorframe2", 0.0625F);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
      if (linked && FMLClientHandler.instance().getClient().field_71439_g.func_174831_c(te.func_174877_v()) < 1024.0D) {
         GL11.glPushMatrix();
         switch(dir) {
         case DOWN:
            this.drawPlaneYPos(te, x, y, z, f);
            break;
         case UP:
            this.drawPlaneYNeg(te, x, y, z, f);
            break;
         case WEST:
            this.drawPlaneXPos(te, x, y, z, f);
            break;
         case EAST:
            this.drawPlaneXNeg(te, x, y, z, f);
            break;
         case NORTH:
            this.drawPlaneZPos(te, x, y, z, f);
            break;
         case SOUTH:
            this.drawPlaneZNeg(te, x, y, z, f);
         }

         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         this.translateFromOrientation((float)x, (float)y, (float)z, dir.ordinal(), 0.02F);
         GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslated(0.5D, -0.5D, 0.0D);
         UtilsFX.renderQuadCentered(mpt, 1.0F, 1.0F, 1.0F, 1.0F, b, 771, 1.0F);
         GL11.glDisable(3042);
         GL11.glPopMatrix();
      } else {
         GL11.glPushMatrix();
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         this.translateFromOrientation((float)x, (float)y, (float)z, dir.ordinal(), 0.02F);
         GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslated(0.5D, -0.5D, 0.0D);
         UtilsFX.renderQuadCentered(mp, 1.0F, 1.0F, 1.0F, 1.0F, b, 771, 1.0F);
         GL11.glDisable(3042);
         GL11.glPopMatrix();
      }

   }

   private void translateFromOrientation(float x, float y, float z, int orientation, float off) {
      if (orientation == 0) {
         GL11.glTranslatef(x, y + 1.0F, z + 1.0F);
         GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
      } else if (orientation == 1) {
         GL11.glTranslatef(x, y, z);
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      } else if (orientation == 2) {
         GL11.glTranslatef(x, y, z + 1.0F);
      } else if (orientation == 3) {
         GL11.glTranslatef(x + 1.0F, y, z);
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      } else if (orientation == 4) {
         GL11.glTranslatef(x + 1.0F, y, z + 1.0F);
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
      } else if (orientation == 5) {
         GL11.glTranslatef(x, y, z);
         GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
      }

      GL11.glTranslatef(0.0F, 0.0F, -off);
   }
}
