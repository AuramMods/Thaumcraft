package thaumcraft.client.renderers.models.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelTubeValve extends ModelBase {
   ModelRenderer ValveRod;
   ModelRenderer ValveRing;

   public ModelTubeValve() {
      this.field_78090_t = 64;
      this.field_78089_u = 32;
      this.ValveRod = new ModelRenderer(this, 0, 10);
      this.ValveRod.func_78789_a(-1.0F, 2.0F, -1.0F, 2, 2, 2);
      this.ValveRod.func_78793_a(0.0F, 0.0F, 0.0F);
      this.ValveRod.func_78787_b(64, 32);
      this.ValveRod.field_78809_i = true;
      this.setRotation(this.ValveRod, 0.0F, 0.0F, 0.0F);
      this.ValveRing = new ModelRenderer(this, 0, 0);
      this.ValveRing.func_78789_a(-2.0F, 4.0F, -2.0F, 4, 1, 4);
      this.ValveRing.func_78793_a(0.0F, 0.0F, 0.0F);
      this.ValveRing.func_78787_b(64, 32);
      this.ValveRing.field_78809_i = true;
      this.setRotation(this.ValveRing, 0.0F, 0.0F, 0.0F);
   }

   public void renderRod() {
      this.ValveRod.func_78785_a(0.0625F);
   }

   public void renderRing() {
      this.ValveRing.func_78785_a(0.0625F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }
}
