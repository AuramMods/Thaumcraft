package thaumcraft.common.entities.ai.combat;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIAttackRanged;

public class AILongRangeAttack extends EntityAIAttackRanged {
   private final EntityLiving wielder;
   double minDistance = 0.0D;

   public AILongRangeAttack(IRangedAttackMob par1IRangedAttackMob, double min, double p_i1650_2_, int p_i1650_4_, int p_i1650_5_, float p_i1650_6_) {
      super(par1IRangedAttackMob, p_i1650_2_, p_i1650_4_, p_i1650_5_, p_i1650_6_);
      this.minDistance = min;
      this.wielder = (EntityLiving)par1IRangedAttackMob;
   }

   public boolean func_75250_a() {
      boolean ex = super.func_75250_a();
      if (ex) {
         EntityLivingBase var1 = this.wielder.func_70638_az();
         if (var1 == null) {
            return false;
         }

         if (var1.field_70128_L) {
            this.wielder.func_70624_b((EntityLivingBase)null);
            return false;
         }

         double ra = this.wielder.func_70092_e(var1.field_70165_t, var1.func_174813_aQ().field_72338_b, var1.field_70161_v);
         if (ra < this.minDistance * this.minDistance) {
            return false;
         }
      }

      return ex;
   }
}
