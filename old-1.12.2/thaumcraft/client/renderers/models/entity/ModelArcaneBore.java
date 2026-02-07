package thaumcraft.client.renderers.models.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelArcaneBore extends ModelBase {
   ModelRenderer crystal;
   ModelRenderer leg2;
   ModelRenderer tripod;
   ModelRenderer leg3;
   ModelRenderer leg4;
   ModelRenderer leg1;
   ModelRenderer magbase;
   ModelRenderer base;
   ModelRenderer domebase;
   ModelRenderer dome;
   ModelRenderer tip;

   public ModelArcaneBore() {
      this.field_78090_t = 64;
      this.field_78089_u = 32;
      this.leg2 = new ModelRenderer(this, 20, 10);
      this.leg2.func_78789_a(-1.0F, 1.0F, -1.0F, 2, 13, 2);
      this.leg2.func_78793_a(0.0F, 12.0F, 0.0F);
      this.leg2.func_78787_b(64, 32);
      this.setRotation(this.leg2, 0.5235988F, 1.570796F, 0.0F);
      this.tripod = new ModelRenderer(this, 13, 0);
      this.tripod.func_78789_a(-1.5F, 0.0F, -1.5F, 3, 2, 3);
      this.tripod.func_78793_a(0.0F, 12.0F, 0.0F);
      this.tripod.func_78787_b(64, 32);
      this.setRotation(this.tripod, 0.0F, 0.0F, 0.0F);
      this.leg3 = new ModelRenderer(this, 20, 10);
      this.leg3.func_78789_a(-1.0F, 1.0F, -1.0F, 2, 13, 2);
      this.leg3.func_78793_a(0.0F, 12.0F, 0.0F);
      this.leg3.func_78787_b(64, 32);
      this.setRotation(this.leg3, 0.5235988F, 3.141593F, 0.0F);
      this.leg4 = new ModelRenderer(this, 20, 10);
      this.leg4.func_78789_a(-1.0F, 1.0F, -1.0F, 2, 13, 2);
      this.leg4.func_78793_a(0.0F, 12.0F, 0.0F);
      this.leg4.func_78787_b(64, 32);
      this.setRotation(this.leg4, 0.5235988F, 4.712389F, 0.0F);
      this.leg1 = new ModelRenderer(this, 20, 10);
      this.leg1.func_78789_a(-1.0F, 1.0F, -1.0F, 2, 13, 2);
      this.leg1.func_78793_a(0.0F, 12.0F, 0.0F);
      this.leg1.func_78787_b(64, 32);
      this.setRotation(this.leg1, 0.5235988F, 0.0F, 0.0F);
      this.base = new ModelRenderer(this, 32, 0);
      this.base.func_78789_a(-3.0F, -6.0F, -3.0F, 6, 6, 6);
      this.base.func_78793_a(0.0F, 13.0F, 0.0F);
      this.base.func_78787_b(64, 32);
      this.setRotation(this.base, 0.0F, 0.0F, 0.0F);
      this.crystal = new ModelRenderer(this, 32, 25);
      this.crystal.func_78789_a(-1.0F, -4.0F, 5.0F, 2, 2, 2);
      this.crystal.func_78793_a(0.0F, 0.0F, 0.0F);
      this.crystal.func_78787_b(64, 32);
      this.setRotation(this.crystal, 0.0F, 0.0F, 0.0F);
      this.domebase = new ModelRenderer(this, 32, 19);
      this.domebase.func_78789_a(-2.0F, -5.0F, 3.0F, 4, 4, 1);
      this.domebase.func_78793_a(0.0F, 0.0F, 0.0F);
      this.domebase.func_78787_b(64, 32);
      this.setRotation(this.domebase, 0.0F, 0.0F, 0.0F);
      this.dome = new ModelRenderer(this, 44, 16);
      this.dome.func_78789_a(-2.0F, -5.0F, 4.0F, 4, 4, 4);
      this.dome.func_78793_a(0.0F, 0.0F, 0.0F);
      this.dome.func_78787_b(64, 32);
      this.setRotation(this.dome, 0.0F, 0.0F, 0.0F);
      this.magbase = new ModelRenderer(this, 0, 18);
      this.magbase.func_78789_a(-1.0F, -4.0F, -6.0F, 2, 2, 3);
      this.magbase.func_78793_a(0.0F, 0.0F, 0.0F);
      this.magbase.func_78787_b(64, 32);
      this.magbase.field_78809_i = true;
      this.setRotation(this.magbase, 0.0F, 0.0F, 0.0F);
      this.tip = new ModelRenderer(this, 0, 9);
      this.tip.func_78789_a(-1.5F, 0.0F, -1.5F, 3, 3, 3);
      this.tip.func_78793_a(0.0F, -3.0F, -6.0F);
      this.tip.func_78787_b(64, 32);
      this.tip.field_78809_i = true;
      this.setRotation(this.tip, -1.570796F, 0.0F, 0.0F);
      this.base.func_78792_a(this.crystal);
      this.base.func_78792_a(this.dome);
      this.base.func_78792_a(this.domebase);
      this.base.func_78792_a(this.magbase);
      this.base.func_78792_a(this.tip);
   }

   public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
      this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
      this.leg2.func_78785_a(f5);
      this.tripod.func_78785_a(f5);
      this.leg3.func_78785_a(f5);
      this.leg4.func_78785_a(f5);
      this.leg1.func_78785_a(f5);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      this.base.func_78785_a(f5);
      GL11.glDisable(3042);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }

   public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float headpitch, float headyaw, float p_78087_6_, Entity entity) {
      this.base.field_78796_g = headpitch / 57.295776F;
      this.base.field_78795_f = headyaw / 57.295776F;
   }
}
