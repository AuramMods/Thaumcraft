package thaumcraft.client.renderers.models.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.entities.monster.tainted.EntityTaintSeed;
import thaumcraft.common.entities.monster.tainted.EntityTaintacle;

public class ModelTaintacle extends ModelBase {
   public ModelRenderer tentacle = new ModelRendererTaintacle(this);
   public ModelRenderer[] tents;
   public ModelRenderer orb = new ModelRendererTaintacle(this);
   private int length = 10;
   private boolean seed = false;

   public ModelTaintacle(int length, boolean seed) {
      this.seed = seed;
      int var3 = false;
      this.length = length;
      this.field_78089_u = 64;
      this.field_78090_t = 64;
      this.tentacle = new ModelRendererTaintacle(this, 0, 0);
      this.tentacle.func_78789_a(-4.0F, -4.0F, -4.0F, 8, 8, 8);
      this.tentacle.field_78800_c = 0.0F;
      this.tentacle.field_78798_e = 0.0F;
      this.tentacle.field_78797_d = 12.0F;
      this.tents = new ModelRendererTaintacle[length];

      for(int k = 0; k < length - 1; ++k) {
         this.tents[k] = new ModelRendererTaintacle(this, 0, 16);
         this.tents[k].func_78789_a(-4.0F, -4.0F, -4.0F, 8, 8, 8);
         this.tents[k].field_78797_d = -8.0F;
         if (k == 0) {
            this.tentacle.func_78792_a(this.tents[k]);
         } else {
            this.tents[k - 1].func_78792_a(this.tents[k]);
         }
      }

      if (!seed) {
         this.orb = new ModelRendererTaintacle(this, 0, 56);
         this.orb.func_78789_a(-2.0F, -2.0F, -2.0F, 4, 4, 4);
         this.orb.field_78797_d = -8.0F;
         this.tents[length - 2].func_78792_a(this.orb);
         this.tents[length - 1] = new ModelRendererTaintacle(this, 0, 32);
         this.tents[length - 1].func_78789_a(-6.0F, -6.0F, -6.0F, 12, 12, 12);
         this.tents[length - 1].field_78797_d = -8.0F;
         this.tents[length - 2].func_78792_a(this.tents[length - 1]);
      }

   }

   public void func_78087_a(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      float flail = 0.0F;
      float ht = 0.0F;
      int at = 0;
      float mod;
      float fi;
      int k;
      if (entity instanceof EntityTaintacle) {
         EntityTaintacle tentacle = (EntityTaintacle)entity;
         flail = tentacle.flailIntensity;
         ht = (float)tentacle.field_70737_aN;
         mod = par6 * 0.2F;
         float fs = flail > 1.0F ? 3.0F : 1.0F + (flail > 1.0F ? mod : -mod);
         fi = flail + (!(ht > 0.0F) && at <= 0 ? -mod : mod);
         this.tentacle.field_78795_f = 0.0F;

         for(k = 0; k < this.length - 1; ++k) {
            this.tents[k].field_78795_f = 0.15F * fi * MathHelper.func_76126_a(par3 * 0.1F * fs - (float)k / 2.0F);
            this.tents[k].field_78808_h = 0.1F / fi * MathHelper.func_76126_a(par3 * 0.15F - (float)k / 2.0F);
         }
      }

      if (entity instanceof EntityTaintSeed) {
         EntityTaintSeed seed = (EntityTaintSeed)entity;
         ht = (float)seed.field_70737_aN / 200.0F;
         flail = 0.1F;
         mod = par6 * 0.2F;
         float var10000;
         if (flail > 1.0F) {
            var10000 = 3.0F;
         } else {
            var10000 = 1.0F + (flail > 1.0F ? mod : -mod);
         }

         fi = flail + (!(ht > 0.0F) && at <= 0 ? -mod : mod);
         fi *= 3.0F;
         this.tentacle.field_78795_f = 0.0F;

         for(k = 0; k < this.length - 1; ++k) {
            this.tents[k].field_78795_f = 0.2F + 0.01F * (float)k * (float)k + ht + seed.attackAnim;
            this.tents[k].field_78808_h = 0.1F / fi * MathHelper.func_76126_a(par3 * 0.05F - (float)k / 2.0F) / 5.0F;
         }
      }

   }

   public void func_78088_a(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
      this.func_78087_a(par2, par3, par4, par5, par6, par7, par1Entity);
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      if (par1Entity instanceof EntityTaintSeed) {
         GL11.glTranslatef(0.0F, 1.0F, -0.2F);
         GL11.glScalef(par1Entity.field_70130_N * 0.6F, par1Entity.field_70131_O, par1Entity.field_70130_N * 0.6F);
         ((ModelRendererTaintacle)this.tentacle).render(par7, this.seed ? 0.82F : 0.85F);
      } else {
         float height = 0.0F;
         float hc = par1Entity.field_70131_O * 10.0F;
         if ((float)par1Entity.field_70173_aa < hc) {
            height = (hc - (float)par1Entity.field_70173_aa) / hc * par1Entity.field_70131_O;
         }

         GL11.glTranslatef(0.0F, (par1Entity.field_70131_O == 3.0F ? 0.6F : 1.2F) + height, 0.0F);
         GL11.glScalef(par1Entity.field_70131_O / 3.0F, par1Entity.field_70131_O / 3.0F, par1Entity.field_70131_O / 3.0F);
         ((ModelRendererTaintacle)this.tentacle).render(par7, 0.88F);
      }

      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }
}
