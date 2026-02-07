package thaumcraft.client.renderers.models.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import thaumcraft.common.entities.monster.EntityEldritchCrab;

public class ModelEldritchCrab extends ModelBase {
   ModelRenderer TailHelm;
   ModelRenderer TailBare;
   ModelRenderer RFLeg1;
   ModelRenderer RClaw1;
   ModelRenderer Head1;
   ModelRenderer RClaw0;
   ModelRenderer RClaw2;
   ModelRenderer LClaw2;
   ModelRenderer LClaw1;
   ModelRenderer RArm;
   ModelRenderer Torso;
   ModelRenderer RRLeg1;
   ModelRenderer Head0;
   ModelRenderer LRLeg1;
   ModelRenderer LFLeg1;
   ModelRenderer RRLeg0;
   ModelRenderer RFLeg0;
   ModelRenderer LFLeg0;
   ModelRenderer LRLeg0;
   ModelRenderer LClaw0;
   ModelRenderer LArm;

   public ModelEldritchCrab() {
      this.field_78090_t = 128;
      this.field_78089_u = 64;
      this.TailHelm = new ModelRenderer(this, 0, 0);
      this.TailHelm.func_78789_a(-4.5F, -4.5F, -0.4F, 9, 9, 9);
      this.TailHelm.func_78793_a(0.0F, 18.0F, 0.0F);
      this.setRotation(this.TailHelm, 0.1047198F, 0.0F, 0.0F);
      this.TailBare = new ModelRenderer(this, 64, 0);
      this.TailBare.func_78789_a(-4.0F, -4.0F, -0.4F, 8, 8, 8);
      this.TailBare.func_78793_a(0.0F, 18.0F, 0.0F);
      this.setRotation(this.TailBare, 0.1047198F, 0.0F, 0.0F);
      this.RClaw1 = new ModelRenderer(this, 0, 47);
      this.RClaw1.func_78789_a(-2.0F, -1.0F, -5.066667F, 4, 3, 5);
      this.RClaw1.func_78793_a(-6.0F, 15.5F, -10.0F);
      this.Head1 = new ModelRenderer(this, 0, 38);
      this.Head1.func_78789_a(-2.0F, -1.5F, -9.066667F, 4, 4, 1);
      this.Head1.func_78793_a(0.0F, 18.0F, 0.0F);
      this.RClaw0 = new ModelRenderer(this, 0, 55);
      this.RClaw0.func_78789_a(-2.0F, -2.5F, -3.066667F, 4, 5, 3);
      this.RClaw0.func_78793_a(-6.0F, 17.0F, -7.0F);
      this.RClaw2 = new ModelRenderer(this, 14, 54);
      this.RClaw2.func_78789_a(-1.5F, -1.0F, -4.066667F, 3, 2, 5);
      this.RClaw2.func_78793_a(-6.0F, 18.5F, -10.0F);
      this.setRotation(this.RClaw2, 0.3141593F, 0.0F, 0.0F);
      this.RArm = new ModelRenderer(this, 44, 4);
      this.RArm.func_78789_a(-1.0F, -1.0F, -5.066667F, 2, 2, 6);
      this.RArm.func_78793_a(-3.0F, 17.0F, -4.0F);
      this.setRotation(this.RArm, 0.0F, 0.7504916F, 0.0F);
      this.LClaw2 = new ModelRenderer(this, 14, 54);
      this.LClaw2.func_78789_a(-1.5F, -1.0F, -4.066667F, 3, 2, 5);
      this.LClaw2.func_78793_a(6.0F, 18.5F, -10.0F);
      this.setRotation(this.LClaw2, 0.3141593F, 0.0F, 0.0F);
      this.LClaw1 = new ModelRenderer(this, 0, 47);
      this.LClaw1.field_78809_i = true;
      this.LClaw1.func_78789_a(-2.0F, -1.0F, -5.066667F, 4, 3, 5);
      this.LClaw1.func_78793_a(6.0F, 15.5F, -10.0F);
      this.LClaw0 = new ModelRenderer(this, 0, 55);
      this.LClaw0.field_78809_i = true;
      this.LClaw0.func_78789_a(-2.0F, -2.5F, -3.066667F, 4, 5, 3);
      this.LClaw0.func_78793_a(6.0F, 17.0F, -7.0F);
      this.LArm = new ModelRenderer(this, 44, 4);
      this.LArm.func_78789_a(-1.0F, -1.0F, -4.066667F, 2, 2, 6);
      this.LArm.func_78793_a(4.0F, 17.0F, -5.0F);
      this.setRotation(this.LArm, 0.0F, -0.7504916F, 0.0F);
      this.Torso = new ModelRenderer(this, 0, 18);
      this.Torso.func_78789_a(-3.5F, -3.5F, -6.066667F, 7, 7, 6);
      this.Torso.func_78793_a(0.0F, 18.0F, 0.0F);
      this.setRotation(this.Torso, 0.0523599F, 0.0F, 0.0F);
      this.Head0 = new ModelRenderer(this, 0, 31);
      this.Head0.func_78789_a(-2.5F, -2.0F, -8.066667F, 5, 5, 2);
      this.Head0.func_78793_a(0.0F, 18.0F, 0.0F);
      this.RRLeg1 = new ModelRenderer(this, 36, 4);
      this.RRLeg1.func_78789_a(-4.5F, 1.0F, -0.9F, 2, 5, 2);
      this.RRLeg1.func_78793_a(-4.0F, 20.0F, -1.5F);
      this.RFLeg1 = new ModelRenderer(this, 36, 4);
      this.RFLeg1.func_78789_a(-5.0F, 1.0F, -1.066667F, 2, 5, 2);
      this.RFLeg1.func_78793_a(-4.0F, 20.0F, -3.5F);
      this.LRLeg1 = new ModelRenderer(this, 36, 4);
      this.LRLeg1.func_78789_a(2.5F, 1.0F, -0.9F, 2, 5, 2);
      this.LRLeg1.func_78793_a(4.0F, 20.0F, -1.5F);
      this.LFLeg1 = new ModelRenderer(this, 36, 4);
      this.LFLeg1.func_78789_a(3.0F, 1.0F, -1.066667F, 2, 5, 2);
      this.LFLeg1.func_78793_a(4.0F, 20.0F, -3.5F);
      this.RRLeg0 = new ModelRenderer(this, 36, 0);
      this.RRLeg0.func_78789_a(-4.5F, -1.0F, -0.9F, 6, 2, 2);
      this.RRLeg0.func_78793_a(-4.0F, 20.0F, -1.5F);
      this.RFLeg0 = new ModelRenderer(this, 36, 0);
      this.RFLeg0.func_78789_a(-5.0F, -1.0F, -1.066667F, 6, 2, 2);
      this.RFLeg0.func_78793_a(-4.0F, 20.0F, -3.5F);
      this.LFLeg0 = new ModelRenderer(this, 36, 0);
      this.LFLeg0.func_78789_a(-1.0F, -1.0F, -1.066667F, 6, 2, 2);
      this.LFLeg0.func_78793_a(4.0F, 20.0F, -3.5F);
      this.LRLeg0 = new ModelRenderer(this, 36, 0);
      this.LRLeg0.func_78789_a(-1.5F, -1.0F, -0.9F, 6, 2, 2);
      this.LRLeg0.func_78793_a(4.0F, 20.0F, -1.5F);
   }

   public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
      this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
      if (entity instanceof EntityEldritchCrab && ((EntityEldritchCrab)entity).hasHelm()) {
         this.TailHelm.func_78785_a(f5);
      } else {
         this.TailBare.func_78785_a(f5);
      }

      this.RFLeg1.func_78785_a(f5);
      this.RClaw1.func_78785_a(f5);
      this.Head1.func_78785_a(f5);
      this.RClaw0.func_78785_a(f5);
      this.RClaw2.func_78785_a(f5);
      this.LClaw2.func_78785_a(f5);
      this.LClaw1.func_78785_a(f5);
      this.RArm.func_78785_a(f5);
      this.Torso.func_78785_a(f5);
      this.RRLeg1.func_78785_a(f5);
      this.Head0.func_78785_a(f5);
      this.LRLeg1.func_78785_a(f5);
      this.LFLeg1.func_78785_a(f5);
      this.RRLeg0.func_78785_a(f5);
      this.RFLeg0.func_78785_a(f5);
      this.LFLeg0.func_78785_a(f5);
      this.LRLeg0.func_78785_a(f5);
      this.LClaw0.func_78785_a(f5);
      this.LArm.func_78785_a(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.field_78795_f = x;
      model.field_78796_g = y;
      model.field_78808_h = z;
   }

   public void func_78087_a(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      this.setRotation(this.RRLeg1, 0.0F, 0.2094395F, 0.4363323F);
      this.setRotation(this.RFLeg1, 0.0F, -0.2094395F, 0.4363323F);
      this.setRotation(this.LRLeg1, 0.0F, -0.2094395F, -0.4363323F);
      this.setRotation(this.LFLeg1, 0.0F, 0.2094395F, -0.4363323F);
      this.setRotation(this.RRLeg0, 0.0F, 0.2094395F, 0.4363323F);
      this.setRotation(this.RFLeg0, 0.0F, -0.2094395F, 0.4363323F);
      this.setRotation(this.LFLeg0, 0.0F, 0.2094395F, -0.4363323F);
      this.setRotation(this.LRLeg0, 0.0F, -0.2094395F, -0.4363323F);
      float f9 = -(MathHelper.func_76134_b(par1 * 0.6662F * 2.0F + 0.0F) * 0.4F) * par2;
      float f10 = -(MathHelper.func_76134_b(par1 * 0.6662F * 2.0F + 3.1415927F) * 0.4F) * par2;
      ModelRenderer var10000 = this.RRLeg1;
      var10000.field_78796_g += f9;
      var10000 = this.RRLeg0;
      var10000.field_78796_g += f9;
      var10000 = this.LRLeg1;
      var10000.field_78796_g += -f9;
      var10000 = this.LRLeg0;
      var10000.field_78796_g += -f9;
      var10000 = this.RFLeg1;
      var10000.field_78796_g += f10;
      var10000 = this.RFLeg0;
      var10000.field_78796_g += f10;
      var10000 = this.LFLeg1;
      var10000.field_78796_g += -f10;
      var10000 = this.LFLeg0;
      var10000.field_78796_g += -f10;
      var10000 = this.RRLeg1;
      var10000.field_78808_h += f9;
      var10000 = this.RRLeg0;
      var10000.field_78808_h += f9;
      var10000 = this.LRLeg1;
      var10000.field_78808_h += -f9;
      var10000 = this.LRLeg0;
      var10000.field_78808_h += -f9;
      var10000 = this.RFLeg1;
      var10000.field_78808_h += f10;
      var10000 = this.RFLeg0;
      var10000.field_78808_h += f10;
      var10000 = this.LFLeg1;
      var10000.field_78808_h += -f10;
      var10000 = this.LFLeg0;
      var10000.field_78808_h += -f10;
      this.TailBare.field_78796_g = this.TailHelm.field_78796_g = MathHelper.func_76134_b(par1 * 0.6662F) * 2.0F * par2 * 0.125F;
      this.TailBare.field_78808_h = this.TailHelm.field_78808_h = MathHelper.func_76134_b(par1 * 0.6662F) * par2 * 0.125F;
      this.RClaw2.field_78795_f = 0.3141593F - MathHelper.func_76126_a((float)entity.field_70173_aa / 4.0F) * 0.25F;
      this.LClaw2.field_78795_f = 0.3141593F + MathHelper.func_76126_a((float)entity.field_70173_aa / 4.1F) * 0.25F;
      this.RClaw1.field_78795_f = MathHelper.func_76126_a((float)entity.field_70173_aa / 4.0F) * 0.125F;
      this.LClaw1.field_78795_f = -MathHelper.func_76126_a((float)entity.field_70173_aa / 4.1F) * 0.125F;
   }
}
