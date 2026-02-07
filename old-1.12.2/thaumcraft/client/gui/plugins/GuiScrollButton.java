package thaumcraft.client.gui.plugins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiScrollButton extends GuiButton {
   boolean minus = false;
   static ResourceLocation tex = new ResourceLocation("thaumcraft", "textures/gui/gui_base.png");

   public GuiScrollButton(int buttonId, int x, int y, int width, int height, boolean minus) {
      super(buttonId, x, y, width, height, "");
      this.minus = minus;
   }

   public void func_146112_a(Minecraft mc, int xx, int yy) {
      if (this.field_146125_m) {
         FontRenderer fontrenderer = mc.field_71466_p;
         mc.func_110434_K().func_110577_a(tex);
         GlStateManager.func_179131_c(0.9F, 0.9F, 0.9F, 0.9F);
         this.field_146123_n = xx >= this.field_146128_h && yy >= this.field_146129_i && xx < this.field_146128_h + this.field_146120_f && yy < this.field_146129_i + this.field_146121_g;
         int k = this.func_146114_a(this.field_146123_n);
         if (k == 2) {
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         }

         GlStateManager.func_179147_l();
         GlStateManager.func_179120_a(770, 771, 1, 0);
         GlStateManager.func_179112_b(770, 771);
         this.func_73729_b(this.field_146128_h, this.field_146129_i, this.minus ? 20 : 30, 0, 10, 10);
         this.func_146119_b(mc, xx, yy);
      }

   }
}
