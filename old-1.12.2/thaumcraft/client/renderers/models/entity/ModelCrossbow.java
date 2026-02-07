package thaumcraft.client.renderers.models.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.math.MathHelper;
import thaumcraft.common.entities.construct.EntityTurretCrossbow;

public class ModelCrossbow extends ModelBase {
   ModelRenderer crossl3;
   ModelRenderer crossr3;
   ModelRenderer loadbarcross;
   ModelRenderer loadbarl;
   ModelRenderer loadbarr;
   ModelRenderer barrel;
   ModelRenderer basebarcross;
   ModelRenderer ammobox;
   ModelRenderer crossbow;
   ModelRenderer basebarr;
   ModelRenderer basebarl;
   ModelRenderer crossl1;
   ModelRenderer crossl2;
   ModelRenderer crossr1;
   ModelRenderer crossr2;
   ModelRenderer tripod;
   ModelRenderer leg3;
   ModelRenderer leg4;
   ModelRenderer leg1;
   ModelRenderer leg2;

   public ModelCrossbow() {
      this.field_78090_t = 64;
      this.field_78089_u = 32;
      this.crossbow = new ModelRenderer(this, 28, 14);
      this.crossbow.func_78789_a(-2.0F, 0.0F, -7.0F, 4, 2, 14);
      this.crossbow.func_78793_a(0.0F, 10.0F, 0.0F);
      this.crossbow.func_78787_b(64, 32);
      this.crossbow.field_78809_i = true;
      this.setRotation(this.crossbow, 0.0F, 0.0F, 0.0F);
      this.basebarr = new ModelRenderer(this, 40, 23);
      this.basebarr.func_78789_a(-1.0F, 0.0F, 7.0F, 1, 2, 5);
      this.basebarr.func_78793_a(0.0F, 0.0F, 0.0F);
      this.basebarr.func_78787_b(64, 32);
      this.basebarr.field_78809_i = true;
      this.setRotation(this.basebarr, 0.0F, -0.1396263F, 0.0F);
      this.basebarl = new ModelRenderer(this, 40, 23);
      this.basebarl.func_78789_a(0.0F, 0.0F, 7.0F, 1, 2, 5);
      this.basebarl.func_78793_a(0.0F, 0.0F, 0.0F);
      this.basebarl.func_78787_b(64, 32);
      this.basebarl.field_78809_i = true;
      this.setRotation(this.basebarl, 0.0F, 0.1396263F, 0.0F);
      this.barrel = new ModelRenderer(this, 20, 28);
      this.barrel.func_78789_a(-1.0F, -1.0F, -8.0F, 2, 2, 2);
      this.barrel.func_78793_a(0.0F, 0.0F, 0.0F);
      this.barrel.func_78787_b(64, 32);
      this.barrel.field_78809_i = true;
      this.setRotation(this.barrel, 0.0F, 0.0F, 0.0F);
      this.basebarcross = new ModelRenderer(this, 0, 13);
      this.basebarcross.func_78789_a(-2.0F, 0.5F, 10.0F, 4, 1, 1);
      this.basebarcross.func_78793_a(0.0F, 0.0F, 0.0F);
      this.basebarcross.func_78787_b(64, 32);
      this.basebarcross.field_78809_i = true;
      this.setRotation(this.basebarcross, 0.0F, 0.0F, 0.0F);
      this.ammobox = new ModelRenderer(this, 38, 0);
      this.ammobox.func_78789_a(-2.0F, -5.0F, -6.0F, 4, 5, 9);
      this.ammobox.func_78793_a(0.0F, 0.0F, 0.0F);
      this.ammobox.func_78787_b(64, 32);
      this.ammobox.field_78809_i = true;
      this.setRotation(this.ammobox, 0.0F, 0.0F, 0.0F);
      this.loadbarcross = new ModelRenderer(this, 0, 13);
      this.loadbarcross.func_78789_a(-2.0F, -8.5F, -0.5F, 4, 1, 1);
      this.loadbarcross.func_78793_a(0.0F, 0.0F, 0.0F);
      this.loadbarcross.func_78787_b(64, 32);
      this.loadbarcross.field_78809_i = true;
      this.setRotation(this.loadbarcross, -0.5585054F, 0.0F, 0.0F);
      this.loadbarl = new ModelRenderer(this, 0, 15);
      this.loadbarl.func_78789_a(2.0F, -9.0F, -1.0F, 1, 11, 2);
      this.loadbarl.func_78793_a(0.0F, 0.0F, 0.0F);
      this.loadbarl.func_78787_b(64, 32);
      this.loadbarl.field_78809_i = true;
      this.setRotation(this.loadbarl, -0.5585054F, 0.0F, 0.0F);
      this.loadbarr = new ModelRenderer(this, 0, 15);
      this.loadbarr.func_78789_a(-3.0F, -9.0F, -1.0F, 1, 11, 2);
      this.loadbarr.func_78793_a(0.0F, 0.0F, 0.0F);
      this.loadbarr.func_78787_b(64, 32);
      this.loadbarr.field_78809_i = true;
      this.setRotation(this.loadbarr, -0.5585054F, 0.0F, 0.0F);
      this.crossl1 = new ModelRenderer(this, 0, 0);
      this.crossl1.func_78789_a(0.0F, 0.0F, -6.0F, 5, 2, 1);
      this.crossl1.func_78793_a(0.0F, 0.0F, 0.0F);
      this.crossl1.func_78787_b(64, 32);
      this.crossl1.field_78809_i = true;
      this.setRotation(this.crossl1, 0.0F, -0.2443461F, 0.0F);
      this.crossl2 = new ModelRenderer(this, 0, 0);
      this.crossl2.func_78789_a(4.0F, 0.0F, -5.0F, 3, 2, 1);
      this.crossl2.func_78793_a(0.0F, 0.0F, 0.0F);
      this.crossl2.func_78787_b(64, 32);
      this.crossl2.field_78809_i = true;
      this.setRotation(this.crossl2, 0.0F, -0.2443461F, 0.0F);
      this.crossl3 = new ModelRenderer(this, 0, 0);
      this.crossl3.func_78789_a(6.0F, 0.0F, -4.0F, 2, 2, 1);
      this.crossl3.func_78793_a(0.0F, 0.0F, 0.0F);
      this.crossl3.func_78787_b(64, 32);
      this.crossl3.field_78809_i = true;
      this.setRotation(this.crossl3, 0.0F, -0.2443461F, 0.0F);
      this.crossr1 = new ModelRenderer(this, 0, 0);
      this.crossr1.func_78789_a(-5.0F, 0.0F, -6.0F, 5, 2, 1);
      this.crossr1.func_78793_a(0.0F, 0.0F, 0.0F);
      this.crossr1.func_78787_b(64, 32);
      this.crossr1.field_78809_i = true;
      this.setRotation(this.crossr1, 0.0F, 0.2443461F, 0.0F);
      this.crossr2 = new ModelRenderer(this, 0, 0);
      this.crossr2.func_78789_a(-7.0F, 0.0F, -5.0F, 3, 2, 1);
      this.crossr2.func_78793_a(0.0F, 0.0F, 0.0F);
      this.crossr2.func_78787_b(64, 32);
      this.crossr2.field_78809_i = true;
      this.setRotation(this.crossr2, 0.0F, 0.2443461F, 0.0F);
      this.crossr3 = new ModelRenderer(this, 0, 0);
      this.crossr3.func_78789_a(-8.0F, 0.0F, -4.0F, 2, 2, 1);
      this.crossr3.func_78793_a(0.0F, 0.0F, 0.0F);
      this.crossr3.func_78787_b(64, 32);
      this.crossr3.field_78809_i = true;
      this.setRotation(this.crossr3, 0.0F, 0.2443461F, 0.0F);
      this.leg2 = new ModelRenderer(this, 20, 10);
      this.leg2.func_78789_a(-1.0F, 1.0F, -1.0F, 2, 13, 2);
      this.leg2.func_78793_a(0.0F, 12.0F, 0.0F);
      this.leg2.func_78787_b(64, 32);
      this.leg2.field_78809_i = true;
      this.setRotation(this.leg2, 0.5235988F, 1.570796F, 0.0F);
      this.tripod = new ModelRenderer(this, 13, 0);
      this.tripod.func_78789_a(-1.5F, 0.0F, -1.5F, 3, 2, 3);
      this.tripod.func_78793_a(0.0F, 12.0F, 0.0F);
      this.tripod.func_78787_b(64, 32);
      this.tripod.field_78809_i = true;
      this.setRotation(this.tripod, 0.0F, 0.0F, 0.0F);
      this.leg3 = new ModelRenderer(this, 20, 10);
      this.leg3.func_78789_a(-1.0F, 1.0F, -1.0F, 2, 13, 2);
      this.leg3.func_78793_a(0.0F, 12.0F, 0.0F);
      this.leg3.func_78787_b(64, 32);
      this.leg3.field_78809_i = true;
      this.setRotation(this.leg3, 0.5235988F, 3.141593F, 0.0F);
      this.leg4 = new ModelRenderer(this, 20, 10);
      this.leg4.func_78789_a(-1.0F, 1.0F, -1.0F, 2, 13, 2);
      this.leg4.func_78793_a(0.0F, 12.0F, 0.0F);
      this.leg4.func_78787_b(64, 32);
      this.leg4.field_78809_i = true;
      this.setRotation(this.leg4, 0.5235988F, 4.712389F, 0.0F);
      this.leg1 = new ModelRenderer(this, 20, 10);
      this.leg1.func_78789_a(-1.0F, 1.0F, -1.0F, 2, 13, 2);
      this.leg1.func_78793_a(0.0F, 12.0F, 0.0F);
      this.leg1.func_78787_b(64, 32);
      this.leg1.field_78809_i = true;
      this.setRotation(this.leg1, 0.5235988F, 0.0F, 0.0F);
      this.crossbow.func_78792_a(this.ammobox);
      this.crossbow.func_78792_a(this.barrel);
      this.crossbow.func_78792_a(this.basebarcross);
      this.crossbow.func_78792_a(this.basebarr);
      this.crossbow.func_78792_a(this.basebarl);
      this.crossbow.func_78792_a(this.loadbarcross);
      this.crossbow.func_78792_a(this.loadbarl);
      this.crossbow.func_78792_a(this.loadbarr);
      this.crossbow.func_78792_a(this.crossl1);
      this.crossbow.func_78792_a(this.crossl2);
      this.crossbow.func_78792_a(this.crossl3);
      this.crossbow.func_78792_a(this.crossr1);
      this.crossbow.func_78792_a(this.crossr2);
      this.crossbow.func_78792_a(this.crossr3);
   }

   public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
      this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
      this.crossbow.func_78785_a(f5);
      this.leg2.func_78785_a(f5);
      this.tripod.func_78785_a(f5);
      this.leg3.func_78785_a(f5);
      this.leg4.func_78785_a(f5);
      this.leg1.func_78785_a(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }

   public void func_78087_a(float p_78087_1_, float p_78087_2_, float p_78087_3_, float headpitch, float headyaw, float p_78087_6_, Entity entity) {
      this.crossbow.field_78796_g = headpitch / 57.295776F;
      this.crossbow.field_78795_f = headyaw / 57.295776F;
      float lp;
      if (this.field_78095_p > -9990.0F) {
         lp = this.field_78095_p;
         this.crossl1.field_78796_g = this.crossl2.field_78796_g = this.crossl3.field_78796_g = -0.2F + MathHelper.func_76126_a(MathHelper.func_76129_c(lp) * 3.1415927F * 2.0F) * 0.2F;
         this.crossr1.field_78796_g = this.crossr2.field_78796_g = this.crossr3.field_78796_g = 0.2F - MathHelper.func_76126_a(MathHelper.func_76129_c(lp) * 3.1415927F * 2.0F) * 0.2F;
      }

      lp = ((EntityTurretCrossbow)entity).loadProgressForRender;
      this.loadbarcross.field_78795_f = this.loadbarl.field_78795_f = this.loadbarr.field_78795_f = -0.5F + MathHelper.func_76126_a(MathHelper.func_76129_c(lp) * 3.1415927F * 2.0F) * 0.5F;
      if (entity.func_184218_aH() && entity.func_184187_bx() != null && entity.func_184187_bx() instanceof EntityMinecart) {
         this.leg1.field_82908_p = -0.5F;
         this.leg2.field_82908_p = -0.5F;
         this.leg3.field_82908_p = -0.5F;
         this.leg4.field_82908_p = -0.5F;
         this.leg1.field_78795_f = 0.1F;
         this.leg2.field_78795_f = 0.1F;
         this.leg3.field_78795_f = 0.1F;
         this.leg4.field_78795_f = 0.1F;
      } else {
         this.leg1.field_82908_p = 0.0F;
         this.leg2.field_82908_p = 0.0F;
         this.leg3.field_82908_p = 0.0F;
         this.leg4.field_82908_p = 0.0F;
         this.leg1.field_78795_f = 0.5F;
         this.leg2.field_78795_f = 0.5F;
         this.leg3.field_78795_f = 0.5F;
         this.leg4.field_78795_f = 0.5F;
      }

   }
}
