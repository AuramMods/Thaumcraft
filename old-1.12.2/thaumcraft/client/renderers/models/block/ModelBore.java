package thaumcraft.client.renderers.models.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBore extends ModelBase {
   ModelRenderer Base;
   ModelRenderer Side1;
   ModelRenderer Side2;
   ModelRenderer NozCrossbar;
   ModelRenderer NozFront;
   ModelRenderer NozMid;

   public ModelBore() {
      this.field_78090_t = 128;
      this.field_78089_u = 64;
      this.Base = new ModelRenderer(this, 0, 32);
      this.Base.func_78789_a(-6.0F, 0.0F, -6.0F, 12, 2, 12);
      this.Base.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Base.func_78787_b(64, 32);
      this.Base.field_78809_i = true;
      this.setRotation(this.Base, 0.0F, 0.0F, 0.0F);
      this.Side1 = new ModelRenderer(this, 0, 0);
      this.Side1.func_78789_a(-2.0F, 2.0F, -5.5F, 4, 8, 1);
      this.Side1.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Side1.func_78787_b(64, 32);
      this.Side1.field_78809_i = true;
      this.setRotation(this.Side1, 0.0F, 0.0F, 0.0F);
      this.Side2 = new ModelRenderer(this, 0, 0);
      this.Side2.func_78789_a(-2.0F, 2.0F, 4.5F, 4, 8, 1);
      this.Side2.func_78793_a(0.0F, 0.0F, 0.0F);
      this.Side2.func_78787_b(64, 32);
      this.Side2.field_78809_i = true;
      this.setRotation(this.Side2, 0.0F, 0.0F, 0.0F);
      this.NozCrossbar = new ModelRenderer(this, 0, 48);
      this.NozCrossbar.func_78789_a(-1.0F, -1.0F, -6.0F, 2, 2, 12);
      this.NozCrossbar.func_78793_a(0.0F, 8.0F, 0.0F);
      this.NozCrossbar.func_78787_b(64, 32);
      this.NozCrossbar.field_78809_i = true;
      this.setRotation(this.NozCrossbar, 0.0F, 0.0F, 0.0F);
      this.NozFront = new ModelRenderer(this, 30, 14);
      this.NozFront.func_78789_a(4.0F, -2.5F, -2.5F, 4, 5, 5);
      this.NozFront.func_78793_a(0.0F, 8.0F, 0.0F);
      this.NozFront.func_78787_b(64, 32);
      this.NozFront.field_78809_i = true;
      this.setRotation(this.NozFront, 0.0F, 0.0F, 0.0F);
      this.NozMid = new ModelRenderer(this, 0, 14);
      this.NozMid.func_78789_a(-2.0F, -4.0F, -4.0F, 6, 8, 8);
      this.NozMid.func_78793_a(0.0F, 8.0F, 0.0F);
      this.NozMid.func_78787_b(64, 32);
      this.NozMid.field_78809_i = true;
      this.setRotation(this.NozMid, 0.0F, 0.0F, 0.0F);
   }

   public void renderBase() {
      float f5 = 0.0625F;
      this.Base.func_78785_a(f5);
      this.Side1.func_78785_a(f5);
      this.Side2.func_78785_a(f5);
      this.NozCrossbar.func_78785_a(f5);
   }

   public void renderNozzle() {
      float f5 = 0.0625F;
      this.NozFront.func_78785_a(f5);
      this.NozMid.func_78785_a(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }
}
