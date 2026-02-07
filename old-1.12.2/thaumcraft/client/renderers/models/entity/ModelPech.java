package thaumcraft.client.renderers.models.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import thaumcraft.common.entities.monster.EntityPech;

public class ModelPech extends ModelBiped {
   ModelRenderer Jowls;
   ModelRenderer LowerPack;
   ModelRenderer UpperPack;

   public ModelPech() {
      this.field_78090_t = 128;
      this.field_78089_u = 64;
      this.field_78115_e = new ModelRenderer(this, 34, 12);
      this.field_78115_e.func_78789_a(-3.0F, 0.0F, 0.0F, 6, 10, 6);
      this.field_78115_e.func_78793_a(0.0F, 9.0F, -3.0F);
      this.field_78115_e.func_78787_b(128, 64);
      this.field_78115_e.field_78809_i = true;
      this.setRotation(this.field_78115_e, 0.3129957F, 0.0F, 0.0F);
      this.field_178721_j = new ModelRenderer(this, 35, 1);
      this.field_178721_j.field_78809_i = true;
      this.field_178721_j.func_78789_a(-2.9F, 0.0F, 0.0F, 3, 6, 3);
      this.field_178721_j.func_78793_a(0.0F, 18.0F, 0.0F);
      this.field_178721_j.func_78787_b(128, 64);
      this.field_178721_j.field_78809_i = true;
      this.setRotation(this.field_178721_j, 0.0F, 0.0F, 0.0F);
      this.field_178721_j.field_78809_i = false;
      this.field_178722_k = new ModelRenderer(this, 35, 1);
      this.field_178722_k.func_78789_a(-0.1F, 0.0F, 0.0F, 3, 6, 3);
      this.field_178722_k.func_78793_a(0.0F, 18.0F, 0.0F);
      this.field_178722_k.func_78787_b(128, 64);
      this.field_178722_k.field_78809_i = true;
      this.setRotation(this.field_178722_k, 0.0F, 0.0F, 0.0F);
      this.field_78116_c = new ModelRenderer(this, 2, 11);
      this.field_78116_c.func_78789_a(-3.5F, -5.0F, -5.0F, 7, 5, 5);
      this.field_78116_c.func_78793_a(0.0F, 8.0F, 0.0F);
      this.field_78116_c.func_78787_b(128, 64);
      this.field_78116_c.field_78809_i = true;
      this.setRotation(this.field_78116_c, 0.0F, 0.0F, 0.0F);
      this.Jowls = new ModelRenderer(this, 1, 21);
      this.Jowls.func_78789_a(-4.0F, -1.0F, -6.0F, 8, 3, 5);
      this.Jowls.func_78793_a(0.0F, 8.0F, 0.0F);
      this.Jowls.func_78787_b(128, 64);
      this.Jowls.field_78809_i = true;
      this.setRotation(this.Jowls, 0.0F, 0.0F, 0.0F);
      this.LowerPack = new ModelRenderer(this, 0, 0);
      this.LowerPack.func_78789_a(-5.0F, 0.0F, 0.0F, 10, 5, 5);
      this.LowerPack.func_78793_a(0.0F, 10.0F, 3.5F);
      this.LowerPack.func_78787_b(128, 64);
      this.LowerPack.field_78809_i = true;
      this.setRotation(this.LowerPack, 0.3013602F, 0.0F, 0.0F);
      this.UpperPack = new ModelRenderer(this, 64, 1);
      this.UpperPack.func_78789_a(-7.5F, -14.0F, 0.0F, 15, 14, 11);
      this.UpperPack.func_78793_a(0.0F, 10.0F, 3.0F);
      this.UpperPack.func_78787_b(128, 64);
      this.UpperPack.field_78809_i = true;
      this.setRotation(this.UpperPack, 0.4537856F, 0.0F, 0.0F);
      this.field_178723_h = new ModelRenderer(this, 52, 2);
      this.field_178723_h.field_78809_i = true;
      this.field_178723_h.func_78789_a(-2.0F, 0.0F, -1.0F, 2, 6, 2);
      this.field_178723_h.func_78793_a(-3.0F, 10.0F, -1.0F);
      this.field_178723_h.func_78787_b(128, 64);
      this.field_178723_h.field_78809_i = true;
      this.setRotation(this.field_178723_h, 0.0F, 0.0F, 0.0F);
      this.field_178723_h.field_78809_i = false;
      this.field_178724_i = new ModelRenderer(this, 52, 2);
      this.field_178724_i.func_78789_a(0.0F, 0.0F, -1.0F, 2, 6, 2);
      this.field_178724_i.func_78793_a(3.0F, 10.0F, -1.0F);
      this.field_178724_i.func_78787_b(128, 64);
      this.field_178724_i.field_78809_i = true;
      this.setRotation(this.field_178724_i, 0.0F, 0.0F, 0.0F);
   }

   public void func_78088_a(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
      this.func_78087_a(par2, par3, par4, par5, par6, par7, par1Entity);
      this.field_78115_e.func_78785_a(par7);
      this.field_178721_j.func_78785_a(par7);
      this.field_178722_k.func_78785_a(par7);
      this.field_78116_c.func_78785_a(par7);
      this.Jowls.func_78785_a(par7);
      this.LowerPack.func_78785_a(par7);
      this.UpperPack.func_78785_a(par7);
      this.field_178723_h.func_78785_a(par7);
      this.field_178724_i.func_78785_a(par7);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }

   public void func_78087_a(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      this.field_78116_c.field_78796_g = par4 / 57.295776F;
      this.field_78116_c.field_78795_f = par5 / 57.295776F;
      float mumble = 0.0F;
      if (entity instanceof EntityPech) {
         mumble = ((EntityPech)entity).mumble;
      }

      this.Jowls.field_78796_g = this.field_78116_c.field_78796_g;
      this.Jowls.field_78795_f = this.field_78116_c.field_78795_f + 0.2617994F + MathHelper.func_76134_b(par1 * 0.6662F) * par2 * 0.25F + 0.34906587F * Math.abs(MathHelper.func_76126_a(mumble));
      this.field_178723_h.field_78795_f = MathHelper.func_76134_b(par1 * 0.6662F + 3.1415927F) * 2.0F * par2 * 0.5F;
      this.field_178724_i.field_78795_f = MathHelper.func_76134_b(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
      this.field_178723_h.field_78808_h = 0.0F;
      this.field_178724_i.field_78808_h = 0.0F;
      this.field_178721_j.field_78795_f = MathHelper.func_76134_b(par1 * 0.6662F) * 1.4F * par2;
      this.field_178722_k.field_78795_f = MathHelper.func_76134_b(par1 * 0.6662F + 3.1415927F) * 1.4F * par2;
      this.field_178721_j.field_78796_g = 0.0F;
      this.field_178722_k.field_78796_g = 0.0F;
      this.LowerPack.field_78796_g = MathHelper.func_76134_b(par1 * 0.6662F) * 2.0F * par2 * 0.125F;
      this.LowerPack.field_78808_h = MathHelper.func_76134_b(par1 * 0.6662F) * 2.0F * par2 * 0.125F;
      ModelRenderer var10000;
      if (this.field_78093_q) {
         var10000 = this.field_178723_h;
         var10000.field_78795_f += -0.62831855F;
         var10000 = this.field_178724_i;
         var10000.field_78795_f += -0.62831855F;
         this.field_178721_j.field_78795_f = -1.2566371F;
         this.field_178722_k.field_78795_f = -1.2566371F;
         this.field_178721_j.field_78796_g = 0.31415927F;
         this.field_178722_k.field_78796_g = -0.31415927F;
      }

      this.field_178723_h.field_78796_g = 0.0F;
      this.field_178724_i.field_78796_g = 0.0F;
      if (this.field_78095_p > -9990.0F) {
         float f6 = this.field_78095_p;
         var10000 = this.field_178723_h;
         var10000.field_78796_g += this.field_78115_e.field_78796_g;
         var10000 = this.field_178724_i;
         var10000.field_78796_g += this.field_78115_e.field_78796_g;
         var10000 = this.field_178724_i;
         var10000.field_78795_f += this.field_78115_e.field_78796_g;
         f6 = 1.0F - this.field_78095_p;
         f6 *= f6;
         f6 *= f6;
         f6 = 1.0F - f6;
         float f7 = MathHelper.func_76126_a(f6 * 3.1415927F);
         float f8 = MathHelper.func_76126_a(this.field_78095_p * 3.1415927F) * -(this.field_78116_c.field_78795_f - 0.7F) * 0.75F;
         this.field_178723_h.field_78795_f = (float)((double)this.field_178723_h.field_78795_f - ((double)f7 * 1.2D + (double)f8));
         var10000 = this.field_178723_h;
         var10000.field_78796_g += this.field_78115_e.field_78796_g * 2.0F;
         this.field_178723_h.field_78808_h = MathHelper.func_76126_a(this.field_78095_p * 3.1415927F) * -0.4F;
      }

      if (entity.func_70093_af()) {
         var10000 = this.field_178723_h;
         var10000.field_78795_f += 0.4F;
         var10000 = this.field_178724_i;
         var10000.field_78795_f += 0.4F;
      }

      var10000 = this.field_178723_h;
      var10000.field_78808_h += MathHelper.func_76134_b(par3 * 0.09F) * 0.05F + 0.05F;
      var10000 = this.field_178724_i;
      var10000.field_78808_h -= MathHelper.func_76134_b(par3 * 0.09F) * 0.05F + 0.05F;
      var10000 = this.field_178723_h;
      var10000.field_78795_f += MathHelper.func_76126_a(par3 * 0.067F) * 0.05F;
      var10000 = this.field_178724_i;
      var10000.field_78795_f -= MathHelper.func_76126_a(par3 * 0.067F) * 0.05F;
   }
}
