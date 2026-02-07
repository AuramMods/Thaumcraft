package thaumcraft.client.renderers.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCube extends ModelBase {
   ModelRenderer cube;

   public ModelCube() {
      this.field_78090_t = 64;
      this.field_78089_u = 32;
      this.cube = new ModelRenderer(this, 0, 0);
      this.cube.func_78789_a(-8.0F, -8.0F, -8.0F, 16, 16, 16);
      this.cube.func_78793_a(8.0F, 8.0F, 8.0F);
      this.cube.func_78787_b(64, 32);
      this.cube.field_78809_i = true;
   }

   public ModelCube(int shift) {
      this.field_78090_t = 64;
      this.field_78089_u = 64;
      this.cube = new ModelRenderer(this, 0, shift);
      this.cube.func_78789_a(-8.0F, -8.0F, -8.0F, 16, 16, 16);
      this.cube.func_78793_a(0.0F, 0.0F, 0.0F);
      this.cube.func_78787_b(64, 64);
      this.cube.field_78809_i = true;
   }

   public void render() {
      this.cube.func_78785_a(0.0625F);
   }

   public void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }
}
