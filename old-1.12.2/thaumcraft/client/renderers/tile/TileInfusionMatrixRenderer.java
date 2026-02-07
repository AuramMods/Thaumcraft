package thaumcraft.client.renderers.tile;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.ModelCube;
import thaumcraft.common.blocks.basic.BlockPillar;
import thaumcraft.common.tiles.crafting.TileInfusionMatrix;

@SideOnly(Side.CLIENT)
public class TileInfusionMatrixRenderer extends TileEntitySpecialRenderer<TileInfusionMatrix> {
   private ModelCube model = new ModelCube(0);
   private ModelCube model_over = new ModelCube(32);
   int type = 0;
   private static final ResourceLocation tex1 = new ResourceLocation("thaumcraft", "textures/blocks/infuser_normal.png");
   private static final ResourceLocation tex2 = new ResourceLocation("thaumcraft", "textures/blocks/infuser_ancient.png");
   private static final ResourceLocation tex3 = new ResourceLocation("thaumcraft", "textures/blocks/infuser_eldritch.png");

   public TileInfusionMatrixRenderer(int type) {
      this.type = type;
   }

   private void drawHalo(TileEntity is, double x, double y, double z, float par8, int count) {
      GL11.glPushMatrix();
      GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
      int q = !FMLClientHandler.instance().getClient().field_71474_y.field_74347_j ? 10 : 20;
      Tessellator tessellator = Tessellator.func_178181_a();
      RenderHelper.func_74518_a();
      float f1 = (float)count / 500.0F;
      float f3 = 0.9F;
      float f2 = 0.0F;
      Random random = new Random(245L);
      GL11.glDisable(3553);
      GL11.glShadeModel(7425);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      GL11.glDisable(3008);
      GL11.glEnable(2884);
      GL11.glDepthMask(false);
      GL11.glPushMatrix();

      for(int i = 0; i < q; ++i) {
         GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F + f1 * 360.0F, 0.0F, 0.0F, 1.0F);
         tessellator.func_178180_c().func_181668_a(6, DefaultVertexFormats.field_181706_f);
         float fa = random.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
         float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
         fa /= 20.0F / ((float)Math.min(count, 50) / 50.0F);
         f4 /= 20.0F / ((float)Math.min(count, 50) / 50.0F);
         tessellator.func_178180_c().func_181662_b(0.0D, 0.0D, 0.0D).func_181669_b(255, 255, 255, (int)(255.0F * (1.0F - f1))).func_181675_d();
         tessellator.func_178180_c().func_181662_b(-0.866D * (double)f4, (double)fa, (double)(-0.5F * f4)).func_181669_b(255, 0, 255, 0).func_181675_d();
         tessellator.func_178180_c().func_181662_b(0.866D * (double)f4, (double)fa, (double)(-0.5F * f4)).func_181669_b(255, 0, 255, 0).func_181675_d();
         tessellator.func_178180_c().func_181662_b(0.0D, (double)fa, (double)(1.0F * f4)).func_181669_b(255, 0, 255, 0).func_181675_d();
         tessellator.func_178180_c().func_181662_b(-0.866D * (double)f4, (double)fa, (double)(-0.5F * f4)).func_181669_b(255, 0, 255, 0).func_181675_d();
         tessellator.func_78381_a();
      }

      GL11.glPopMatrix();
      GL11.glDepthMask(true);
      GL11.glDisable(2884);
      GL11.glDisable(3042);
      GL11.glShadeModel(7424);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      GL11.glEnable(3008);
      RenderHelper.func_74519_b();
      GL11.glBlendFunc(770, 771);
      GL11.glPopMatrix();
   }

   public void renderInfusionMatrix(TileInfusionMatrix is, double par2, double par4, double par6, float par8) {
      GL11.glPushMatrix();
      ResourceLocation t = tex1;
      GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 0.5F, (float)par6 + 0.5F);
      float ticks = (float)Minecraft.func_71410_x().func_175606_aa().field_70173_aa + par8;
      int inst = 0;
      int craftcount = 0;
      float startup = 0.0F;
      boolean active = false;
      boolean crafting = false;
      if (is != null && is.func_145831_w() != null) {
         GL11.glRotatef(ticks % 360.0F * is.startUp, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(35.0F * is.startUp, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(45.0F * is.startUp, 0.0F, 0.0F, 1.0F);
         IBlockState bs = is.func_145831_w().func_180495_p(is.func_174877_v().func_177982_a(-1, -2, -1));
         if (bs.func_177228_b().containsKey(BlockPillar.VARIANT)) {
            Comparable p = bs.func_177229_b(BlockPillar.VARIANT);
            if (p == BlockPillar.PillarType.ANCIENT) {
               t = tex2;
            }

            if (p == BlockPillar.PillarType.ELDRITCH) {
               t = tex3;
            }
         }

         inst = is.instability;
         craftcount = is.craftCount;
         startup = is.startUp;
         active = is.active;
         crafting = is.crafting;
      }

      this.func_147499_a(t);
      float instability = Math.min(6.0F, 1.0F + (float)inst * 0.66F * ((float)Math.min(craftcount, 50) / 50.0F));
      float b1 = 0.0F;
      float b2 = 0.0F;
      float b3 = 0.0F;
      int aa = false;
      int bb = false;
      int cc = false;

      int a;
      int b;
      int c;
      int aa;
      int bb;
      int cc;
      for(a = 0; a < 2; ++a) {
         for(b = 0; b < 2; ++b) {
            for(c = 0; c < 2; ++c) {
               if (active) {
                  b1 = MathHelper.func_76126_a((ticks + (float)(a * 10)) / (15.0F - instability / 2.0F)) * 0.01F * startup * instability;
                  b2 = MathHelper.func_76126_a((ticks + (float)(b * 10)) / (14.0F - instability / 2.0F)) * 0.01F * startup * instability;
                  b3 = MathHelper.func_76126_a((ticks + (float)(c * 10)) / (13.0F - instability / 2.0F)) * 0.01F * startup * instability;
               }

               aa = a == 0 ? -1 : 1;
               bb = b == 0 ? -1 : 1;
               cc = c == 0 ? -1 : 1;
               GL11.glPushMatrix();
               GL11.glTranslatef(b1 + (float)aa * 0.25F, b2 + (float)bb * 0.25F, b3 + (float)cc * 0.25F);
               if (a > 0) {
                  GL11.glRotatef(90.0F, (float)a, 0.0F, 0.0F);
               }

               if (b > 0) {
                  GL11.glRotatef(90.0F, 0.0F, (float)b, 0.0F);
               }

               if (c > 0) {
                  GL11.glRotatef(90.0F, 0.0F, 0.0F, (float)c);
               }

               GL11.glScaled(0.45D, 0.45D, 0.45D);
               this.model.render();
               GL11.glPopMatrix();
            }
         }
      }

      if (active) {
         GL11.glPushMatrix();
         GL11.glAlphaFunc(516, 0.003921569F);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 1);

         for(a = 0; a < 2; ++a) {
            for(b = 0; b < 2; ++b) {
               for(c = 0; c < 2; ++c) {
                  b1 = MathHelper.func_76126_a((ticks + (float)(a * 10)) / (15.0F - instability / 2.0F)) * 0.01F * startup * instability;
                  b2 = MathHelper.func_76126_a((ticks + (float)(b * 10)) / (14.0F - instability / 2.0F)) * 0.01F * startup * instability;
                  b3 = MathHelper.func_76126_a((ticks + (float)(c * 10)) / (13.0F - instability / 2.0F)) * 0.01F * startup * instability;
                  aa = a == 0 ? -1 : 1;
                  bb = b == 0 ? -1 : 1;
                  cc = c == 0 ? -1 : 1;
                  GL11.glPushMatrix();
                  GL11.glTranslatef(b1 + (float)aa * 0.25F, b2 + (float)bb * 0.25F, b3 + (float)cc * 0.25F);
                  if (a > 0) {
                     GL11.glRotatef(90.0F, (float)a, 0.0F, 0.0F);
                  }

                  if (b > 0) {
                     GL11.glRotatef(90.0F, 0.0F, (float)b, 0.0F);
                  }

                  if (c > 0) {
                     GL11.glRotatef(90.0F, 0.0F, 0.0F, (float)c);
                  }

                  GL11.glScaled(0.45D, 0.45D, 0.45D);
                  int j = 15728880;
                  int k = j % 65536;
                  int l = j / 65536;
                  OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)k / 1.0F, (float)l / 1.0F);
                  GL11.glColor4f(0.8F, 0.1F, 1.0F, (MathHelper.func_76126_a((ticks + (float)(a * 2) + (float)(b * 3) + (float)(c * 4)) / 4.0F) * 0.1F + 0.2F) * startup);
                  this.model_over.render();
                  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                  GL11.glPopMatrix();
               }
            }
         }

         GL11.glBlendFunc(770, 771);
         GL11.glDisable(3042);
         GL11.glAlphaFunc(516, 0.1F);
         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
      if (crafting) {
         this.drawHalo(is, par2, par4, par6, par8, craftcount);
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public void renderTileEntityAt(TileInfusionMatrix par1TileEntity, double par2, double par4, double par6, float par8, int q) {
      switch(this.type) {
      case 0:
         this.renderInfusionMatrix(par1TileEntity, par2, par4, par6, par8);
         break;
      case 1:
         this.renderTileEntityAt(par1TileEntity, par2, par4, par6, par8, q);
      }

   }
}
