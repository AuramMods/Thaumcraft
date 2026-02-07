package thaumcraft.client.renderers.models.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBanner extends ModelBase {
   ModelRenderer B1;
   ModelRenderer B2;
   ModelRenderer Beam;
   public ModelRenderer Banner;
   ModelRenderer Pole;

   public ModelBanner() {
      this.field_78090_t = 128;
      this.field_78089_u = 64;
      this.B1 = new ModelRenderer(this, 0, 29);
      this.B1.func_78789_a(-5.0F, -7.5F, -1.5F, 2, 3, 3);
      this.B1.func_78793_a(0.0F, 0.0F, 0.0F);
      this.B1.func_78787_b(128, 64);
      this.B1.field_78809_i = true;
      this.setRotation(this.B1, 0.0F, 0.0F, 0.0F);
      this.B2 = new ModelRenderer(this, 0, 29);
      this.B2.func_78789_a(3.0F, -7.5F, -1.5F, 2, 3, 3);
      this.B2.func_78793_a(0.0F, 0.0F, 0.0F);
      this.B2.func_78787_b(128, 64);
      this.B2.field_78809_i = true;
      this.setRotation(this.B2, 0.0F, 0.0F, 0.0F);
      this.Beam = new ModelRenderer(this, 30, 0);
      this.Beam.func_78789_a(-7.0F, -7.0F, -1.0F, 14, 2, 2);
      this.Beam.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Beam.func_78787_b(128, 64);
      this.Beam.field_78809_i = true;
      this.setRotation(this.Beam, 0.0F, 0.0F, 0.0F);
      this.Banner = new ModelRenderer(this, 0, 0);
      this.Banner.func_78789_a(-7.0F, 0.0F, -0.5F, 14, 28, 1);
      this.Banner.func_78793_a(0.0F, -5.0F, 0.0F);
      this.Banner.func_78787_b(128, 64);
      this.Banner.field_78809_i = true;
      this.setRotation(this.Banner, 0.0F, 0.0F, 0.0F);
      this.Pole = new ModelRenderer(this, 62, 0);
      this.Pole.func_78789_a(0.0F, 0.0F, -1.0F, 2, 31, 2);
      this.Pole.func_78793_a(-1.0F, -7.0F, -2.0F);
      this.Pole.func_78787_b(128, 64);
      this.Pole.field_78809_i = true;
      this.setRotation(this.Pole, 0.0F, 0.0F, 0.0F);
   }

   public void renderPole() {
      this.Pole.func_78785_a(0.0625F);
   }

   public void renderBeam() {
      this.Beam.func_78785_a(0.0625F);
   }

   public void renderTabs() {
      this.B1.func_78785_a(0.0625F);
      this.B2.func_78785_a(0.0625F);
   }

   public void renderBanner() {
      this.Banner.func_78785_a(0.0625F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }
}
