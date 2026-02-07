package thaumcraft.common.entities.construct.golem.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiGolemCraftButton extends GuiButton {
   static ResourceLocation tex = new ResourceLocation("thaumcraft", "textures/gui/gui_golembuilder.png");

   public GuiGolemCraftButton(int buttonId, int x, int y) {
      super(buttonId, x, y, 24, 16, "");
   }

   public void func_146112_a(Minecraft mc, int xx, int yy) {
      if (this.field_146125_m) {
         FontRenderer fontrenderer = mc.field_71466_p;
         mc.func_110434_K().func_110577_a(tex);
         GlStateManager.func_179131_c(0.9F, 0.9F, 0.9F, 0.9F);
         this.field_146123_n = xx >= this.field_146128_h && yy >= this.field_146129_i && xx < this.field_146128_h + this.field_146120_f && yy < this.field_146129_i + this.field_146121_g;
         int k = this.func_146114_a(this.field_146123_n);
         GlStateManager.func_179147_l();
         GlStateManager.func_179120_a(770, 771, 1, 0);
         GlStateManager.func_179112_b(770, 771);
         if (this.field_146124_l && k == 2) {
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         }

         this.func_73729_b(this.field_146128_h, this.field_146129_i, 216, 64, 24, 16);
         if (!this.field_146124_l) {
            this.func_73729_b(this.field_146128_h, this.field_146129_i, 216, 40, 24, 16);
         }

         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.func_146119_b(mc, xx, yy);
      }

   }
}
