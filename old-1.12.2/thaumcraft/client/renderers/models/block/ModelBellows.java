package thaumcraft.client.renderers.models.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBellows extends ModelBase {
   public ModelRenderer BottomPlank;
   public ModelRenderer MiddlePlank;
   public ModelRenderer TopPlank;
   public ModelRenderer Bag;
   public ModelRenderer Nozzle;

   public ModelBellows() {
      this.field_78090_t = 128;
      this.field_78089_u = 128;
      this.BottomPlank = new ModelRenderer(this, 0, 0);
      this.BottomPlank.func_78789_a(-6.0F, 0.0F, -6.0F, 12, 2, 12);
      this.BottomPlank.func_78793_a(0.0F, 22.0F, 0.0F);
      this.BottomPlank.func_78787_b(128, 128);
      this.BottomPlank.field_78809_i = true;
      this.setRotation(this.BottomPlank, 0.0F, 0.0F, 0.0F);
      this.MiddlePlank = new ModelRenderer(this, 0, 0);
      this.MiddlePlank.func_78789_a(-6.0F, -1.0F, -6.0F, 12, 2, 12);
      this.MiddlePlank.func_78793_a(0.0F, 16.0F, 0.0F);
      this.MiddlePlank.func_78787_b(128, 128);
      this.MiddlePlank.field_78809_i = true;
      this.setRotation(this.MiddlePlank, 0.0F, 0.0F, 0.0F);
      this.TopPlank = new ModelRenderer(this, 0, 0);
      this.TopPlank.func_78789_a(-6.0F, 0.0F, -6.0F, 12, 2, 12);
      this.TopPlank.func_78793_a(0.0F, 8.0F, 0.0F);
      this.TopPlank.func_78787_b(128, 128);
      this.TopPlank.field_78809_i = true;
      this.setRotation(this.TopPlank, 0.0F, 0.0F, 0.0F);
      this.Bag = new ModelRenderer(this, 48, 0);
      this.Bag.func_78789_a(-10.0F, -12.03333F, -10.0F, 20, 24, 20);
      this.Bag.func_78793_a(0.0F, 16.0F, 0.0F);
      this.Bag.func_78787_b(64, 32);
      this.Bag.field_78809_i = true;
      this.setRotation(this.Bag, 0.0F, 0.0F, 0.0F);
      this.Nozzle = new ModelRenderer(this, 0, 36);
      this.Nozzle.func_78789_a(-2.0F, -2.0F, 0.0F, 4, 4, 2);
      this.Nozzle.func_78793_a(0.0F, 16.0F, 6.0F);
      this.Nozzle.func_78787_b(128, 128);
      this.Nozzle.field_78809_i = true;
      this.setRotation(this.Nozzle, 0.0F, 0.0F, 0.0F);
   }

   public void render() {
      this.MiddlePlank.func_78785_a(0.0625F);
      this.Nozzle.func_78785_a(0.0625F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }
}
