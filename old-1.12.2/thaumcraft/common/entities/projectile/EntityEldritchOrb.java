package thaumcraft.common.entities.projectile;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import thaumcraft.client.fx.FXDispatcher;

public class EntityEldritchOrb extends EntityThrowable {
   public EntityEldritchOrb(World par1World) {
      super(par1World);
   }

   public EntityEldritchOrb(World par1World, EntityLivingBase p) {
      super(par1World, p);
      this.func_184538_a(p, p.field_70125_A, p.field_70177_z, -5.0F, 0.75F, 0.0F);
   }

   protected float func_70185_h() {
      return 0.0F;
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.field_70173_aa > 100) {
         this.func_70106_y();
      }

   }

   public void func_70103_a(byte b) {
      if (b == 16) {
         if (this.field_70170_p.field_72995_K) {
            for(int a = 0; a < 30; ++a) {
               float fx = (this.field_70170_p.field_73012_v.nextFloat() - this.field_70170_p.field_73012_v.nextFloat()) * 0.3F;
               float fy = (this.field_70170_p.field_73012_v.nextFloat() - this.field_70170_p.field_73012_v.nextFloat()) * 0.3F;
               float fz = (this.field_70170_p.field_73012_v.nextFloat() - this.field_70170_p.field_73012_v.nextFloat()) * 0.3F;
               FXDispatcher.INSTANCE.drawGenericParticles(this.field_70165_t + (double)fx, this.field_70163_u + (double)fy, this.field_70161_v + (double)fz, this.field_70165_t + (double)(fx * 8.0F), this.field_70163_u + (double)(fy * 8.0F), this.field_70161_v + (double)(fz * 8.0F), 0.3F + this.field_70170_p.field_73012_v.nextFloat() * 0.3F, 0.0F, 0.3F + this.field_70170_p.field_73012_v.nextFloat() * 0.3F, 0.7F, true, 264, 8, 1, 5, 0, 0.3F, 0.0F, 0);
            }
         }
      } else {
         super.func_70103_a(b);
      }

   }

   protected void func_70184_a(RayTraceResult mop) {
      if (!this.field_70170_p.field_72995_K && this.func_85052_h() != null) {
         List list = this.field_70170_p.func_72839_b(this.func_85052_h(), this.func_174813_aQ().func_72314_b(2.0D, 2.0D, 2.0D));

         for(int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity)list.get(i);
            if (entity1 != null && entity1 instanceof EntityLivingBase && !((EntityLivingBase)entity1).func_70662_br()) {
               ((EntityLivingBase)entity1).func_70097_a(DamageSource.func_76354_b(this, this.func_85052_h()), (float)this.func_85052_h().func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e() * 0.666F);

               try {
                  ((EntityLivingBase)entity1).func_70690_d(new PotionEffect(MobEffects.field_76437_t, 160, 0));
               } catch (Exception var6) {
               }
            }
         }

         this.func_184185_a(SoundEvents.field_187659_cY, 0.5F, 2.6F + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.8F);
         this.field_70173_aa = 100;
         this.field_70170_p.func_72960_a(this, (byte)16);
      }

   }
}
