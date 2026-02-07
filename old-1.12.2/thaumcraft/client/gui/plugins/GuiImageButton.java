package thaumcraft.client.gui.plugins;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;

public class GuiImageButton extends GuiButton {
   GuiScreen screen;
   ResourceLocation loc;
   int lx;
   int ly;
   int ww;
   int hh;
   public String description;
   public int color;
   public boolean active = true;

   public GuiImageButton(GuiScreen screen, int buttonId, int x, int y, int width, int height, String buttonText, String description, ResourceLocation loc, int lx, int ly, int ww, int hh) {
      super(buttonId, x, y, width, height, buttonText);
      this.description = description;
      this.screen = screen;
      this.color = 16777215;
      this.loc = loc;
      this.lx = lx;
      this.ly = ly;
      this.ww = ww;
      this.hh = hh;
   }

   public GuiImageButton(GuiScreen screen, int buttonId, int x, int y, int width, int height, String buttonText, String description, ResourceLocation loc, int lx, int ly, int ww, int hh, int color) {
      super(buttonId, x, y, width, height, buttonText);
      this.description = description;
      this.screen = screen;
      this.color = color;
      this.loc = loc;
      this.lx = lx;
      this.ly = ly;
      this.ww = ww;
      this.hh = hh;
   }

   public void func_146112_a(Minecraft mc, int xx, int yy) {
      if (this.field_146125_m) {
         FontRenderer fontrenderer = mc.field_71466_p;
         this.field_146123_n = xx >= this.field_146128_h - this.field_146120_f / 2 && yy >= this.field_146129_i - this.field_146121_g / 2 && xx < this.field_146128_h - this.field_146120_f / 2 + this.field_146120_f && yy < this.field_146129_i - this.field_146121_g / 2 + this.field_146121_g;
         int k = this.func_146114_a(this.field_146123_n);
         GlStateManager.func_179147_l();
         GlStateManager.func_179120_a(770, 771, 1, 0);
         GlStateManager.func_179112_b(770, 771);
         Color c = new Color(this.color);
         float cc = 0.9F;
         float ac = 1.0F;
         if (k == 2) {
            ac = 1.0F;
            cc = 1.0F;
         }

         if (!this.active) {
            cc = 0.5F;
            ac = 0.9F;
         }

         GlStateManager.func_179131_c(cc * ((float)c.getRed() / 255.0F), cc * ((float)c.getGreen() / 255.0F), cc * ((float)c.getBlue() / 255.0F), ac);
         mc.func_110434_K().func_110577_a(this.loc);
         this.func_73729_b(this.field_146128_h - this.ww / 2, this.field_146129_i - this.hh / 2, this.lx, this.ly, this.ww, this.hh);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         if (this.field_146126_j != null) {
            int j = 16777215;
            if (!this.field_146124_l) {
               j = 10526880;
            } else if (this.field_146123_n) {
               j = 16777120;
            }

            GL11.glPushMatrix();
            GL11.glTranslated((double)this.field_146128_h, (double)this.field_146129_i, 0.0D);
            GL11.glScaled(0.5D, 0.5D, 0.0D);
            this.func_73732_a(fontrenderer, (new TextComponentTranslation(this.field_146126_j, new Object[0])).func_150254_d(), 0, -4, j);
            GL11.glPopMatrix();
         }

         this.func_146119_b(mc, xx, yy);
      }

   }

   public void func_146111_b(int xx, int yy) {
      FontRenderer fontrenderer = Minecraft.func_71410_x().field_71466_p;
      this.field_73735_i += 90.0F;
      ArrayList<String> text = new ArrayList();
      text.add(this.field_146126_j);
      int m = 8;
      if (this.description != null) {
         m = 0;
         text.add("§o§9" + this.description);
      }

      UtilsFX.drawCustomTooltip(this.screen, fontrenderer, text, xx + 4, yy + m, -99);
      this.field_73735_i -= 90.0F;
      RenderHelper.func_74518_a();
      GlStateManager.func_179140_f();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
      return this.active && this.field_146124_l && this.field_146125_m && mouseX >= this.field_146128_h - this.field_146120_f / 2 && mouseY >= this.field_146129_i - this.field_146121_g / 2 && mouseX < this.field_146128_h - this.field_146120_f / 2 + this.field_146120_f && mouseY < this.field_146129_i - this.field_146121_g / 2 + this.field_146121_g;
   }
}
