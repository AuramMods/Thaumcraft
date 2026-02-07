package thaumcraft.common.entities.monster;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.SoundsTC;

public class EntityThaumicSlime extends EntitySlime implements ITaintedMob {
   int launched = 10;
   int spitCounter = 100;

   public EntityThaumicSlime(World par1World) {
      super(par1World);
      int i = 1 << 1 + this.field_70146_Z.nextInt(3);
      this.func_70799_a(i);
   }

   public EntityThaumicSlime(World par1World, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving) {
      super(par1World);
      this.func_70799_a(1);
      this.field_70163_u = (par2EntityLiving.func_174813_aQ().field_72338_b + par2EntityLiving.func_174813_aQ().field_72337_e) / 2.0D;
      double var6 = par3EntityLiving.field_70165_t - par2EntityLiving.field_70165_t;
      double var8 = par3EntityLiving.func_174813_aQ().field_72338_b + (double)(par3EntityLiving.field_70131_O / 3.0F) - this.field_70163_u;
      double var10 = par3EntityLiving.field_70161_v - par2EntityLiving.field_70161_v;
      double var12 = (double)MathHelper.func_76133_a(var6 * var6 + var10 * var10);
      if (var12 >= 1.0E-7D) {
         float var14 = (float)(Math.atan2(var10, var6) * 180.0D / 3.141592653589793D) - 90.0F;
         float var15 = (float)(-(Math.atan2(var8, var12) * 180.0D / 3.141592653589793D));
         double var16 = var6 / var12;
         double var18 = var10 / var12;
         this.func_70012_b(par2EntityLiving.field_70165_t + var16, this.field_70163_u, par2EntityLiving.field_70161_v + var18, var14, var15);
         float var20 = (float)var12 * 0.2F;
         this.setThrowableHeading(var6, var8 + (double)var20, var10, 1.5F, 1.0F);
      }

   }

   public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8) {
      float var9 = MathHelper.func_76133_a(par1 * par1 + par3 * par3 + par5 * par5);
      par1 /= (double)var9;
      par3 /= (double)var9;
      par5 /= (double)var9;
      par1 += this.field_70146_Z.nextGaussian() * 0.007499999832361937D * (double)par8;
      par3 += this.field_70146_Z.nextGaussian() * 0.007499999832361937D * (double)par8;
      par5 += this.field_70146_Z.nextGaussian() * 0.007499999832361937D * (double)par8;
      par1 *= (double)par7;
      par3 *= (double)par7;
      par5 *= (double)par7;
      this.field_70159_w = par1;
      this.field_70181_x = par3;
      this.field_70179_y = par5;
      float var10 = MathHelper.func_76133_a(par1 * par1 + par5 * par5);
      this.field_70126_B = this.field_70177_z = (float)(Math.atan2(par1, par5) * 180.0D / 3.141592653589793D);
      this.field_70127_C = this.field_70125_A = (float)(Math.atan2(par3, (double)var10) * 180.0D / 3.141592653589793D);
   }

   public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_) {
      int i = this.field_70146_Z.nextInt(3);
      if (i < 2 && this.field_70146_Z.nextFloat() < 0.5F * p_180482_1_.func_180170_c()) {
         ++i;
      }

      int j = 1 << i;
      this.func_70799_a(j);
      return super.func_180482_a(p_180482_1_, p_180482_2_);
   }

   public void func_70799_a(int par1) {
      super.func_70799_a(par1);
      this.field_70728_aV = par1 + 2;
   }

   public void func_70014_b(NBTTagCompound par1NBTTagCompound) {
      super.func_70014_b(par1NBTTagCompound);
   }

   public void func_70037_a(NBTTagCompound par1NBTTagCompound) {
      super.func_70037_a(par1NBTTagCompound);
   }

   public void func_70071_h_() {
      int i = this.func_70809_q();
      float ff;
      if (this.field_70122_E && !this.field_175452_bi) {
         this.field_175452_bi = true;
         ff = this.field_70813_a;
         super.func_70071_h_();
         this.field_70813_a = ff;
         if (this.field_70170_p.field_72995_K) {
            for(int j = 0; j < i * 2; ++j) {
               FXDispatcher.INSTANCE.slimeJumpFX(this, i);
            }
         }

         if (this.func_70807_r()) {
            this.func_184185_a(this.func_184710_cZ(), this.func_70599_aP(), ((this.func_70681_au().nextFloat() - this.func_70681_au().nextFloat()) * 0.2F + 1.0F) * 0.8F);
         }

         this.field_70813_a = -0.5F;
         this.field_175452_bi = this.field_70122_E;
         this.func_70808_l();
      } else {
         super.func_70071_h_();
      }

      if (this.field_70170_p.field_72995_K) {
         if (this.launched > 0) {
            --this.launched;

            for(int j = 0; j < i * (this.launched + 1); ++j) {
               FXDispatcher.INSTANCE.slimeJumpFX(this, i);
            }
         }

         ff = (float)this.func_70809_q();
         this.func_70105_a(0.6F * ff, 0.6F * ff);
         this.func_70105_a(0.51000005F * ff, 0.51000005F * ff);
      } else if (!this.field_70128_L) {
         EntityPlayer entityplayer = this.field_70170_p.func_72890_a(this, 16.0D);
         if (entityplayer != null) {
            if (this.spitCounter > 0) {
               --this.spitCounter;
            }

            this.func_70625_a(entityplayer, 10.0F, 20.0F);
            if (this.func_70032_d(entityplayer) > 4.0F && this.spitCounter <= 0 && this.func_70809_q() > 2) {
               this.spitCounter = 101;
               if (!this.field_70170_p.field_72995_K) {
                  EntityThaumicSlime flyslime = new EntityThaumicSlime(this.field_70170_p, this, entityplayer);
                  this.field_70170_p.func_72838_d(flyslime);
               }

               this.func_184185_a(SoundsTC.gore, 1.0F, ((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 1.0F) * 0.8F);
               this.func_70799_a(this.func_70809_q() - 1);
            }
         }
      }

   }

   protected EntityThaumicSlime createInstance() {
      return new EntityThaumicSlime(this.field_70170_p);
   }

   public void func_70106_y() {
      int i = this.func_70809_q();
      if (!this.field_70170_p.field_72995_K && i > 1 && this.func_110143_aJ() <= 0.0F) {
         for(int k = 0; k < i; ++k) {
            float f = ((float)(k % 2) - 0.5F) * (float)i / 4.0F;
            float f1 = ((float)(k / 2) - 0.5F) * (float)i / 4.0F;
            EntityThaumicSlime entityslime = this.createInstance();
            entityslime.func_70799_a(1);
            entityslime.func_70012_b(this.field_70165_t + (double)f, this.field_70163_u + 0.5D, this.field_70161_v + (double)f1, this.field_70146_Z.nextFloat() * 360.0F, 0.0F);
            this.field_70170_p.func_72838_d(entityslime);
         }
      }

      this.field_70128_L = true;
   }

   public boolean func_70601_bi() {
      return false;
   }

   protected int func_70805_n() {
      return this.func_70809_q() + 1;
   }

   protected boolean func_70800_m() {
      return true;
   }

   protected void func_175451_e(EntityLivingBase p_175451_1_) {
      int i = this.func_70809_q();
      if (this.launched > 0) {
         i += 2;
      }

      if (this.func_70685_l(p_175451_1_) && this.func_70068_e(p_175451_1_) < 0.6D * (double)i * 0.6D * (double)i && p_175451_1_.func_70097_a(DamageSource.func_76358_a(this), (float)this.func_70805_n())) {
         this.func_184185_a(SoundEvents.field_187870_fk, 1.0F, (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 1.0F);
         this.func_174815_a(this, p_175451_1_);
      }

   }

   protected Item func_146068_u() {
      return this.func_70809_q() > 1 ? ItemsTC.crystalEssence : Item.func_150899_d(0);
   }

   protected void func_70628_a(boolean flag, int i) {
      if (this.func_70809_q() > 1) {
         this.func_70099_a(ConfigItems.FLUX_CRYSTAL.func_77946_l(), this.field_70131_O / 2.0F);
      }

   }
}
