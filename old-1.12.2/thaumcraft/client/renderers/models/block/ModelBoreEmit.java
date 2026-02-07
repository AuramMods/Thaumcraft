package thaumcraft.client.renderers.models.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBoreEmit extends ModelBase {
   ModelRenderer Knob;
   ModelRenderer Cross1;
   ModelRenderer Cross3;
   ModelRenderer Cross2;
   ModelRenderer Rod;

   public ModelBoreEmit() {
      this.field_78090_t = 128;
      this.field_78089_u = 64;
      this.Knob = new ModelRenderer(this, 66, 0);
      this.Knob.func_78789_a(-2.0F, 12.0F, -2.0F, 4, 4, 4);
      this.Knob.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Knob.func_78787_b(128, 64);
      this.Knob.field_78809_i = true;
      this.setRotation(this.Knob, 0.0F, 0.0F, 0.0F);
      this.Cross1 = new ModelRenderer(this, 56, 16);
      this.Cross1.func_78789_a(-2.0F, 0.0F, -2.0F, 4, 1, 4);
      this.Cross1.func_78793_a(0.0F, 8.0F, 0.0F);
      this.Cross1.func_78787_b(128, 64);
      this.Cross1.field_78809_i = true;
      this.setRotation(this.Cross1, 0.0F, 0.0F, 0.0F);
      this.Cross3 = new ModelRenderer(this, 56, 16);
      this.Cross3.func_78789_a(-2.0F, 0.0F, -2.0F, 4, 1, 4);
      this.Cross3.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Cross3.func_78787_b(128, 64);
      this.Cross3.field_78809_i = true;
      this.setRotation(this.Cross3, 0.0F, 0.0F, 0.0F);
      this.Cross2 = new ModelRenderer(this, 56, 24);
      this.Cross2.func_78789_a(-3.0F, 4.0F, -3.0F, 6, 1, 6);
      this.Cross2.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Cross2.func_78787_b(128, 64);
      this.Cross2.field_78809_i = true;
      this.setRotation(this.Cross2, 0.0F, 0.0F, 0.0F);
      this.Rod = new ModelRenderer(this, 56, 0);
      this.Rod.func_78789_a(-1.0F, 1.0F, -1.0F, 2, 11, 2);
      this.Rod.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Rod.func_78787_b(128, 64);
      this.Rod.field_78809_i = true;
      this.setRotation(this.Rod, 0.0F, 0.0F, 0.0F);
   }

   public void render(boolean focus) {
      float f5 = 0.0625F;
      if (focus) {
         this.Knob.func_78785_a(f5);
      }

      this.Cross1.func_78785_a(f5);
      this.Cross3.func_78785_a(f5);
      this.Cross2.func_78785_a(f5);
      this.Rod.func_78785_a(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }
}
