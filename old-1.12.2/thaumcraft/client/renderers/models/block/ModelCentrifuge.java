package thaumcraft.client.renderers.models.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCentrifuge extends ModelBase {
   ModelRenderer Crossbar;
   ModelRenderer Dingus1;
   ModelRenderer Dingus2;
   ModelRenderer Core;
   ModelRenderer Top;
   ModelRenderer Bottom;

   public ModelCentrifuge() {
      this.field_78090_t = 64;
      this.field_78089_u = 32;
      this.Crossbar = new ModelRenderer(this, 16, 0);
      this.Crossbar.func_78789_a(-4.0F, -1.0F, -1.0F, 8, 2, 2);
      this.Crossbar.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Crossbar.func_78787_b(64, 32);
      this.Crossbar.field_78809_i = true;
      this.setRotation(this.Crossbar, 0.0F, 0.0F, 0.0F);
      this.Dingus1 = new ModelRenderer(this, 0, 16);
      this.Dingus1.func_78789_a(4.0F, -3.0F, -2.0F, 4, 6, 4);
      this.Dingus1.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Dingus1.func_78787_b(64, 32);
      this.Dingus1.field_78809_i = true;
      this.setRotation(this.Dingus1, 0.0F, 0.0F, 0.0F);
      this.Dingus2 = new ModelRenderer(this, 0, 16);
      this.Dingus2.func_78789_a(-8.0F, -3.0F, -2.0F, 4, 6, 4);
      this.Dingus2.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Dingus2.func_78787_b(64, 32);
      this.Dingus2.field_78809_i = true;
      this.setRotation(this.Dingus2, 0.0F, 0.0F, 0.0F);
      this.Core = new ModelRenderer(this, 0, 0);
      this.Core.func_78789_a(-1.5F, -4.0F, -1.5F, 3, 8, 3);
      this.Core.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Core.func_78787_b(64, 32);
      this.Core.field_78809_i = true;
      this.setRotation(this.Core, 0.0F, 0.0F, 0.0F);
      this.Top = new ModelRenderer(this, 20, 16);
      this.Top.func_78789_a(-4.0F, -8.0F, -4.0F, 8, 4, 8);
      this.Top.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Top.func_78787_b(64, 32);
      this.Top.field_78809_i = true;
      this.setRotation(this.Top, 0.0F, 0.0F, 0.0F);
      this.Bottom = new ModelRenderer(this, 20, 16);
      this.Bottom.func_78789_a(-4.0F, 4.0F, -4.0F, 8, 4, 8);
      this.Bottom.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Bottom.func_78787_b(64, 32);
      this.Bottom.field_78809_i = true;
      this.setRotation(this.Bottom, 0.0F, 0.0F, 0.0F);
   }

   public void renderBoxes() {
      this.Top.func_78785_a(0.0625F);
      this.Bottom.func_78785_a(0.0625F);
   }

   public void renderSpinnyBit() {
      this.Crossbar.func_78785_a(0.0625F);
      this.Dingus1.func_78785_a(0.0625F);
      this.Dingus2.func_78785_a(0.0625F);
      this.Core.func_78785_a(0.0625F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }
}
