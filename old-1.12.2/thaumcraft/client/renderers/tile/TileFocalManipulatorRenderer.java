package thaumcraft.client.renderers.tile;

import java.awt.Color;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.tiles.crafting.TileFocalManipulator;

@SideOnly(Side.CLIENT)
public class TileFocalManipulatorRenderer extends TileEntitySpecialRenderer {
   EntityItem entityitem = null;

   public void renderTileEntityAt(TileFocalManipulator table, double par2, double par4, double par6, float par8) {
      if (table.func_145831_w() != null) {
         float ticks = (float)Minecraft.func_71410_x().func_175606_aa().field_70173_aa + par8;
         if (table.func_70301_a(0) != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.0F, (float)par6 + 0.5F);
            GL11.glRotatef(ticks % 360.0F, 0.0F, 1.0F, 0.0F);
            ItemStack is = table.func_70301_a(0).func_77946_l();
            this.entityitem = new EntityItem(table.func_145831_w(), 0.0D, 0.0D, 0.0D, is);
            this.entityitem.field_70290_d = MathHelper.func_76126_a(ticks / 14.0F) * 0.2F + 0.2F;
            RenderManager rendermanager = Minecraft.func_71410_x().func_175598_ae();
            rendermanager.func_188391_a(this.entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, false);
            GL11.glPopMatrix();
         }

         if (table.crystalsSync.getAspects().length > 0) {
            int q = table.crystalsSync.getAspects().length;
            float ang = (float)(360 / q);

            for(int a = 0; a < q; ++a) {
               float angle = ticks % 720.0F / 2.0F + ang * (float)a;
               float bob = MathHelper.func_76126_a((ticks + (float)(a * 10)) / 12.0F) * 0.02F + 0.02F;
               GL11.glPushMatrix();
               GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.5F, (float)par6 + 0.5F);
               GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
               GL11.glTranslatef(0.0F, bob, 0.5F);
               GL11.glRotatef(-angle, 0.0F, 1.0F, 0.0F);
               this.func_147499_a(ParticleEngine.particleTexture);
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 1);
               GL11.glAlphaFunc(516, 0.003921569F);
               GL11.glDepthMask(false);
               Color c = new Color(table.crystalsSync.getAspects()[a].getColor());
               float r = (float)c.getRed() / 255.0F;
               float g = (float)c.getGreen() / 255.0F;
               float b = (float)c.getBlue() / 255.0F;
               GL11.glColor4f(r, g, b, 0.66F);
               UtilsFX.renderBillboardQuad(0.17499999701976776D, 64, 64, 320 + Minecraft.func_71410_x().func_175606_aa().field_70173_aa % 16);
               GL11.glDepthMask(true);
               GL11.glBlendFunc(770, 771);
               GL11.glDisable(3042);
               GlStateManager.func_179092_a(516, 0.1F);
               GL11.glPopMatrix();
               GL11.glPushMatrix();
               GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.25F, (float)par6 + 0.5F);
               GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
               GL11.glTranslatef(0.0F, bob, 0.5F);
               GL11.glScaled(0.5D, 0.5D, 0.5D);
               ItemStack is = ThaumcraftApiHelper.makeCrystal(table.crystalsSync.getAspects()[a]);
               this.entityitem = new EntityItem(table.func_145831_w(), 0.0D, 0.0D, 0.0D, is);
               this.entityitem.field_70290_d = 0.0F;
               this.renderRay(angle, a, bob, r, g, b, ticks);
               this.renderRay(angle, (a + 1) * 5, bob, r, g, b, ticks);
               RenderManager rendermanager = Minecraft.func_71410_x().func_175598_ae();
               rendermanager.func_188391_a(this.entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, false);
               GL11.glPopMatrix();
            }
         }
      }

   }

   private void renderRay(float angle, int num, float lift, float r, float g, float b, float ticks) {
      Random random = new Random(187L + (long)(num * num));
      GL11.glPushMatrix();
      float pan = MathHelper.func_76126_a((ticks + (float)(num * 10)) / 15.0F) * 15.0F;
      float aparture = MathHelper.func_76126_a((ticks + (float)(num * 10)) / 14.0F) * 2.0F;
      Tessellator tessellator = Tessellator.func_178181_a();
      VertexBuffer wr = tessellator.func_178180_c();
      RenderHelper.func_74518_a();
      GL11.glTranslatef(0.0F, 0.475F + lift, 0.0F);
      GL11.glDisable(3553);
      GL11.glShadeModel(7425);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      GL11.glDisable(3008);
      GL11.glEnable(2884);
      GL11.glDepthMask(false);
      GL11.glPushMatrix();
      GL11.glRotatef(90.0F, -1.0F, 0.0F, 0.0F);
      GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotated((double)pan, 1.0D, 0.0D, 0.0D);
      wr.func_181668_a(6, DefaultVertexFormats.field_181706_f);
      float fa = random.nextFloat() * 20.0F + 10.0F;
      float f4 = random.nextFloat() * 4.0F + 6.0F + aparture;
      fa /= 30.0F / (Math.min(ticks, 10.0F) / 10.0F);
      f4 /= 30.0F / (Math.min(ticks, 10.0F) / 10.0F);
      wr.func_181662_b(0.0D, 0.0D, 0.0D).func_181666_a(r, g, b, 0.66F).func_181675_d();
      wr.func_181662_b(-0.8D * (double)f4, (double)fa, (double)(-0.5F * f4)).func_181666_a(r, g, b, 0.0F).func_181675_d();
      wr.func_181662_b(0.8D * (double)f4, (double)fa, (double)(-0.5F * f4)).func_181666_a(r, g, b, 0.0F).func_181675_d();
      wr.func_181662_b(0.0D, (double)fa, (double)(1.0F * f4)).func_181666_a(r, g, b, 0.0F).func_181675_d();
      wr.func_181662_b(-0.8D * (double)f4, (double)fa, (double)(-0.5F * f4)).func_181666_a(r, g, b, 0.0F).func_181675_d();
      tessellator.func_78381_a();
      GL11.glPopMatrix();
      GL11.glDepthMask(true);
      GL11.glDisable(2884);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3042);
      GL11.glShadeModel(7424);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      GL11.glEnable(3008);
      RenderHelper.func_74519_b();
      GL11.glPopMatrix();
   }

   public void func_180535_a(TileEntity par1TileEntity, double par2, double par4, double par6, float par8, int p) {
      this.renderTileEntityAt((TileFocalManipulator)par1TileEntity, par2, par4, par6, par8);
   }
}
