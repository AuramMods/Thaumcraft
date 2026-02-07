package thaumcraft.common.entities.construct.golem.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

class GuiGolemCategoryButton extends GuiButton {
   int icon;
   boolean active;
   static ResourceLocation tex = new ResourceLocation("thaumcraft", "textures/gui/gui_base.png");

   public GuiGolemCategoryButton(int buttonId, int x, int y, int width, int height, String buttonText, int i, boolean act) {
      super(buttonId, x, y, width, height, buttonText);
      this.icon = i;
      this.active = act;
   }

   public void func_146112_a(Minecraft mc, int xx, int yy) {
      if (this.field_146125_m) {
         FontRenderer fontrenderer = mc.field_71466_p;
         mc.func_110434_K().func_110577_a(tex);
         GlStateManager.func_179131_c(0.9F, 0.9F, 0.9F, 0.9F);
         this.field_146123_n = xx >= this.field_146128_h - 8 && yy >= this.field_146129_i - 8 && xx < this.field_146128_h - 8 + this.field_146120_f && yy < this.field_146129_i - 8 + this.field_146121_g;
         int k = this.func_146114_a(this.field_146123_n);
         GlStateManager.func_179147_l();
         GlStateManager.func_179120_a(770, 771, 1, 0);
         GlStateManager.func_179112_b(770, 771);
         if (this.active) {
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         } else if (k != 2) {
            GlStateManager.func_179131_c(0.7F, 0.7F, 0.7F, 0.7F);
         }

         this.func_73729_b(this.field_146128_h - 8, this.field_146129_i - 8, this.icon * 16, 120, 16, 16);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         if (k == 2) {
            this.field_73735_i += 90.0F;
            String s = I18n.func_74838_a(this.field_146126_j);
            this.func_73731_b(fontrenderer, s, this.field_146128_h - 10 - fontrenderer.func_78256_a(s), this.field_146129_i - 4, 16777215);
            this.field_73735_i -= 90.0F;
         }

         this.func_146119_b(mc, xx, yy);
      }

   }

   public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
      return this.field_146124_l && this.field_146125_m && mouseX >= this.field_146128_h - 8 && mouseY >= this.field_146129_i - 8 && mouseX < this.field_146128_h - 8 + this.field_146120_f && mouseY < this.field_146129_i - 8 + this.field_146121_g;
   }
}
