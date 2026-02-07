package thaumcraft.common.entities.construct.golem.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import thaumcraft.common.entities.construct.EntityOwnedConstruct;

public class AIOwnerHurtByTarget extends EntityAITarget {
   EntityOwnedConstruct theDefendingTameable;
   EntityLivingBase theOwnerAttacker;
   private int field_142051_e;

   public AIOwnerHurtByTarget(EntityOwnedConstruct p_i1667_1_) {
      super(p_i1667_1_, false);
      this.theDefendingTameable = p_i1667_1_;
      this.func_75248_a(1);
   }

   public boolean func_75250_a() {
      if (!this.theDefendingTameable.isOwned()) {
         return false;
      } else {
         EntityLivingBase entitylivingbase = this.theDefendingTameable.getOwnerEntity();
         if (entitylivingbase == null) {
            return false;
         } else {
            this.theOwnerAttacker = entitylivingbase.func_70643_av();
            int i = entitylivingbase.func_142015_aE();
            return i != this.field_142051_e && this.func_75296_a(this.theOwnerAttacker, false);
         }
      }
   }

   public void func_75249_e() {
      this.field_75299_d.func_70624_b(this.theOwnerAttacker);
      EntityLivingBase entitylivingbase = this.theDefendingTameable.getOwnerEntity();
      if (entitylivingbase != null) {
         this.field_142051_e = entitylivingbase.func_142015_aE();
      }

      super.func_75249_e();
   }
}
