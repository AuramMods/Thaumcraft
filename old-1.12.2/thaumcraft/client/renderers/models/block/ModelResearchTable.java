package thaumcraft.client.renderers.models.block;

import java.awt.Color;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

public class ModelResearchTable extends ModelBase {
   ModelRenderer Inkwell;
   ModelRenderer ScrollTube;
   ModelRenderer ScrollRibbon;

   public ModelResearchTable() {
      this.field_78090_t = 64;
      this.field_78089_u = 32;
      this.Inkwell = new ModelRenderer(this, 0, 16);
      this.Inkwell.func_78789_a(0.0F, 0.0F, 0.0F, 3, 2, 3);
      this.Inkwell.func_78793_a(-6.0F, -2.0F, 3.0F);
      this.Inkwell.field_78809_i = true;
      this.setRotation(this.Inkwell, 0.0F, 0.0F, 0.0F);
      this.ScrollTube = new ModelRenderer(this, 0, 0);
      this.ScrollTube.func_78789_a(-8.0F, -0.5F, 0.0F, 8, 2, 2);
      this.ScrollTube.func_78793_a(-2.0F, -2.0F, 2.0F);
      this.ScrollTube.field_78809_i = true;
      this.setRotation(this.ScrollTube, 0.0F, 10.0F, 0.0F);
      this.ScrollRibbon = new ModelRenderer(this, 0, 4);
      this.ScrollRibbon.func_78789_a(-4.25F, -0.275F, 0.0F, 1, 2, 2);
      this.ScrollRibbon.func_78793_a(-2.0F, -2.0F, 2.0F);
      this.ScrollRibbon.field_78809_i = true;
      this.setRotation(this.ScrollRibbon, 0.0F, 10.0F, 0.0F);
   }

   public void renderInkwell() {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      this.Inkwell.func_78785_a(0.0625F);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public void renderScroll(int color) {
      GL11.glPushMatrix();
      this.ScrollTube.func_78785_a(0.0625F);
      Color c = new Color(color);
      GL11.glColor4f((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 1.0F);
      GL11.glScalef(1.2F, 1.2F, 1.2F);
      this.ScrollRibbon.func_78785_a(0.0625F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }
}
