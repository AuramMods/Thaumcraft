package thaumcraft.client.gui.plugins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.translation.I18n;
import thaumcraft.client.gui.GuiTurretAdvanced;

public class GuiToggleButton extends GuiButton {
   Runnable runnable;
   public static boolean toggled;

   public GuiToggleButton(int buttonId, int x, int y, int width, int height, String buttonText, Runnable runnable) {
      super(buttonId, x, y, width, height, buttonText);
      this.runnable = runnable;
   }

   public void func_146112_a(Minecraft mc, int xx, int yy) {
      if (this.field_146125_m) {
         this.runnable.run();
         FontRenderer fontrenderer = mc.field_71466_p;
         mc.func_110434_K().func_110577_a(GuiTurretAdvanced.tex);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.field_146123_n = xx >= this.field_146128_h && yy >= this.field_146129_i && xx < this.field_146128_h + this.field_146120_f && yy < this.field_146129_i + this.field_146121_g;
         GlStateManager.func_179147_l();
         GlStateManager.func_179120_a(770, 771, 1, 0);
         GlStateManager.func_179112_b(770, 771);
         this.func_73729_b(this.field_146128_h, this.field_146129_i, 192, 16, 8, 8);
         if (toggled) {
            this.func_73729_b(this.field_146128_h, this.field_146129_i, 192, 24, 8, 8);
         }

         this.func_73731_b(fontrenderer, I18n.func_74838_a(this.field_146126_j), this.field_146128_h + 12, this.field_146129_i, 16777215);
         this.func_146119_b(mc, xx, yy);
      }

   }
}
