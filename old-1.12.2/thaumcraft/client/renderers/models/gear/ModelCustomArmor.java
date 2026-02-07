package thaumcraft.client.renderers.models.gear;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.MathHelper;
import thaumcraft.client.lib.UtilsFX;

public class ModelCustomArmor extends ModelBiped {
   public ModelCustomArmor(float f, int i, int j, int k) {
      super(f, (float)i, j, k);
   }

   public void func_78087_a(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
      if (entityIn instanceof EntityLivingBase) {
         this.field_78095_p = ((EntityLivingBase)entityIn).func_70678_g(UtilsFX.sysPartialTicks);
      }

      if (entityIn instanceof EntityArmorStand) {
         this.setRotationAnglesStand(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
      } else if (!(entityIn instanceof EntitySkeleton) && !(entityIn instanceof EntityZombie)) {
         super.func_78087_a(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
      } else {
         this.setRotationAnglesZombie(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
      }

   }

   public void setRotationAnglesZombie(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
      super.func_78087_a(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
      boolean flag = entityIn instanceof EntityZombie && ((EntityZombie)entityIn).func_184734_db();
      float f = MathHelper.func_76126_a(this.field_78095_p * 3.1415927F);
      float f1 = MathHelper.func_76126_a((1.0F - (1.0F - this.field_78095_p) * (1.0F - this.field_78095_p)) * 3.1415927F);
      this.field_178723_h.field_78808_h = 0.0F;
      this.field_178724_i.field_78808_h = 0.0F;
      this.field_178723_h.field_78796_g = -(0.1F - f * 0.6F);
      this.field_178724_i.field_78796_g = 0.1F - f * 0.6F;
      float f2 = -3.1415927F / (flag ? 1.5F : 2.25F);
      this.field_178723_h.field_78795_f = f2;
      this.field_178724_i.field_78795_f = f2;
      ModelRenderer var10000 = this.field_178723_h;
      var10000.field_78795_f += f * 1.2F - f1 * 0.4F;
      var10000 = this.field_178724_i;
      var10000.field_78795_f += f * 1.2F - f1 * 0.4F;
      var10000 = this.field_178723_h;
      var10000.field_78808_h += MathHelper.func_76134_b(ageInTicks * 0.09F) * 0.05F + 0.05F;
      var10000 = this.field_178724_i;
      var10000.field_78808_h -= MathHelper.func_76134_b(ageInTicks * 0.09F) * 0.05F + 0.05F;
      var10000 = this.field_178723_h;
      var10000.field_78795_f += MathHelper.func_76126_a(ageInTicks * 0.067F) * 0.05F;
      var10000 = this.field_178724_i;
      var10000.field_78795_f -= MathHelper.func_76126_a(ageInTicks * 0.067F) * 0.05F;
   }

   public void setRotationAnglesStand(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
      if (entityIn instanceof EntityArmorStand) {
         EntityArmorStand entityarmorstand = (EntityArmorStand)entityIn;
         this.field_78116_c.field_78795_f = 0.017453292F * entityarmorstand.func_175418_s().func_179415_b();
         this.field_78116_c.field_78796_g = 0.017453292F * entityarmorstand.func_175418_s().func_179416_c();
         this.field_78116_c.field_78808_h = 0.017453292F * entityarmorstand.func_175418_s().func_179413_d();
         this.field_78116_c.func_78793_a(0.0F, 1.0F, 0.0F);
         this.field_78115_e.field_78795_f = 0.017453292F * entityarmorstand.func_175408_t().func_179415_b();
         this.field_78115_e.field_78796_g = 0.017453292F * entityarmorstand.func_175408_t().func_179416_c();
         this.field_78115_e.field_78808_h = 0.017453292F * entityarmorstand.func_175408_t().func_179413_d();
         this.field_178724_i.field_78795_f = 0.017453292F * entityarmorstand.func_175404_u().func_179415_b();
         this.field_178724_i.field_78796_g = 0.017453292F * entityarmorstand.func_175404_u().func_179416_c();
         this.field_178724_i.field_78808_h = 0.017453292F * entityarmorstand.func_175404_u().func_179413_d();
         this.field_178723_h.field_78795_f = 0.017453292F * entityarmorstand.func_175411_v().func_179415_b();
         this.field_178723_h.field_78796_g = 0.017453292F * entityarmorstand.func_175411_v().func_179416_c();
         this.field_178723_h.field_78808_h = 0.017453292F * entityarmorstand.func_175411_v().func_179413_d();
         this.field_178722_k.field_78795_f = 0.017453292F * entityarmorstand.func_175403_w().func_179415_b();
         this.field_178722_k.field_78796_g = 0.017453292F * entityarmorstand.func_175403_w().func_179416_c();
         this.field_178722_k.field_78808_h = 0.017453292F * entityarmorstand.func_175403_w().func_179413_d();
         this.field_178722_k.func_78793_a(1.9F, 11.0F, 0.0F);
         this.field_178721_j.field_78795_f = 0.017453292F * entityarmorstand.func_175407_x().func_179415_b();
         this.field_178721_j.field_78796_g = 0.017453292F * entityarmorstand.func_175407_x().func_179416_c();
         this.field_178721_j.field_78808_h = 0.017453292F * entityarmorstand.func_175407_x().func_179413_d();
         this.field_178721_j.func_78793_a(-1.9F, 11.0F, 0.0F);
         func_178685_a(this.field_78116_c, this.field_178720_f);
      }

   }
}
