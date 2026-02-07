package thaumcraft.client.renderers.models.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

public class ModelJar extends ModelBase {
   public ModelRenderer Core;
   public ModelRenderer Brine;
   public ModelRenderer Lid;
   public ModelRenderer LidExtension;

   public ModelJar() {
      this.field_78090_t = 64;
      this.field_78089_u = 32;
      this.Core = new ModelRenderer(this, 0, 0);
      this.Core.func_78789_a(-5.0F, -12.0F, -5.0F, 10, 12, 10);
      this.Core.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Core.func_78787_b(64, 32);
      this.Core.field_78809_i = true;
      this.setRotation(this.Core, 0.0F, 0.0F, 0.0F);
      this.Brine = new ModelRenderer(this, 0, 0);
      this.Brine.func_78789_a(-4.0F, -11.0F, -4.0F, 8, 10, 8);
      this.Brine.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Brine.func_78787_b(64, 32);
      this.Brine.field_78809_i = true;
      this.setRotation(this.Brine, 0.0F, 0.0F, 0.0F);
      this.Lid = new ModelRenderer(this, 32, 24);
      this.Lid.func_78789_a(-3.0F, 0.0F, -3.0F, 6, 2, 6);
      this.Lid.func_78793_a(0.0F, -14.0F, 0.0F);
      this.Lid.func_78787_b(64, 32);
      this.Lid.field_78809_i = true;
      this.LidExtension = new ModelRenderer(this, 0, 23);
      this.LidExtension.func_78789_a(-2.0F, -16.0F, -2.0F, 4, 2, 4);
      this.LidExtension.func_78793_a(0.0F, 0.0F, 0.0F);
      this.LidExtension.func_78787_b(64, 32);
      this.LidExtension.field_78809_i = true;
   }

   public void renderBrine() {
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      this.Brine.func_78785_a(0.0625F);
      GL11.glDisable(3042);
   }

   public void renderLidExtension() {
      this.LidExtension.func_78785_a(0.0625F);
   }

   public void renderLidBrace() {
      this.Lid.func_78785_a(0.0625F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }
}
