package thaumcraft.client.renderers.tile;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.block.ModelBanner;
import thaumcraft.common.tiles.misc.TileBanner;

@SideOnly(Side.CLIENT)
public class TileBannerRenderer extends TileEntitySpecialRenderer {
   private ModelBanner model = new ModelBanner();
   private static final ResourceLocation TEX_CULT = new ResourceLocation("thaumcraft", "textures/models/banner_cultist.png");
   private static final ResourceLocation TEX_BLANK = new ResourceLocation("thaumcraft", "textures/models/banner_blank.png");

   public void renderTileEntityAt(TileBanner banner, double par2, double par4, double par6, float par8) {
      GL11.glPushMatrix();
      if (banner.getAspect() == null && banner.getColor() == -1) {
         this.func_147499_a(TEX_CULT);
      } else {
         this.func_147499_a(TEX_BLANK);
      }

      GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.5F, (float)par6 + 0.5F);
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (banner.func_145831_w() != null) {
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         float f2 = (float)(banner.getBannerFacing() * 360) / 16.0F;
         GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);
      }

      if (!banner.getWall()) {
         this.model.renderPole();
      } else {
         GL11.glTranslated(0.0D, 1.0D, -0.4125D);
      }

      this.model.renderBeam();
      if (banner.getColor() != -1) {
         Color c = new Color(EnumDyeColor.func_176766_a(banner.getColor()).func_176768_e().field_76291_p);
         GL11.glColor4f((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 1.0F);
      }

      this.model.renderTabs();
      EntityPlayer p = Minecraft.func_71410_x().field_71439_g;
      float f3 = (float)(banner.func_174877_v().func_177958_n() * 7 + banner.func_174877_v().func_177956_o() * 9 + banner.func_174877_v().func_177952_p() * 13) + (float)p.field_70173_aa + par8;
      float rx = 0.02F - MathHelper.func_76126_a(f3 / 11.0F) * 0.02F;
      this.model.Banner.field_78795_f = rx;
      this.model.renderBanner();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (banner.getAspect() != null) {
         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, 0.0F, 0.05001F);
         GL11.glScaled(0.0375D, 0.0375D, 0.0375D);
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-rx * 57.295776F * 2.0F, 1.0F, 0.0F, 0.0F);
         UtilsFX.drawTag(-8, 0, banner.getAspect(), 0.0F, 0, 0.0D, 771, 0.75F, false);
         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
   }

   public void func_180535_a(TileEntity par1TileEntity, double par2, double par4, double par6, float par8, int p_180535_9_) {
      this.renderTileEntityAt((TileBanner)par1TileEntity, par2, par4, par6, par8);
   }
}
