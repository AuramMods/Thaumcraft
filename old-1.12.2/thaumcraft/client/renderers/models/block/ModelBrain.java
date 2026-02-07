package thaumcraft.client.renderers.models.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBrain extends ModelBase {
   ModelRenderer Shape1;
   ModelRenderer Shape2;
   ModelRenderer Shape3;

   public ModelBrain() {
      this.field_78090_t = 128;
      this.field_78089_u = 64;
      this.Shape1 = new ModelRenderer(this, 0, 0);
      this.Shape1.func_78789_a(0.0F, 0.0F, 0.0F, 12, 10, 16);
      this.Shape1.func_78793_a(-6.0F, 8.0F, -8.0F);
      this.Shape1.func_78787_b(128, 64);
      this.Shape1.field_78809_i = true;
      this.setRotation(this.Shape1, 0.0F, 0.0F, 0.0F);
      this.Shape2 = new ModelRenderer(this, 64, 0);
      this.Shape2.func_78789_a(0.0F, 0.0F, 0.0F, 8, 3, 7);
      this.Shape2.func_78793_a(-4.0F, 18.0F, 0.0F);
      this.Shape2.func_78787_b(128, 64);
      this.Shape2.field_78809_i = true;
      this.setRotation(this.Shape2, 0.0F, 0.0F, 0.0F);
      this.Shape3 = new ModelRenderer(this, 0, 32);
      this.Shape3.func_78789_a(0.0F, 0.0F, 0.0F, 2, 6, 2);
      this.Shape3.func_78793_a(-1.0F, 18.0F, -2.0F);
      this.Shape3.func_78787_b(128, 64);
      this.Shape3.field_78809_i = true;
      this.setRotation(this.Shape3, 0.4089647F, 0.0F, 0.0F);
   }

   public void render() {
      this.Shape1.func_78785_a(0.0625F);
      this.Shape2.func_78785_a(0.0625F);
      this.Shape3.func_78785_a(0.0625F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }
}
