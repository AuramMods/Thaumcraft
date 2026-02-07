package thaumcraft.common.entities.ai.combat;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.math.AxisAlignedBB;
import thaumcraft.common.entities.monster.cult.EntityCultist;
import thaumcraft.common.entities.monster.cult.EntityCultistCleric;

public class AICultistHurtByTarget extends EntityAITarget {
   boolean entityCallsForHelp;
   private int field_142052_b;

   public AICultistHurtByTarget(EntityCreature p_i1660_1_, boolean p_i1660_2_) {
      super(p_i1660_1_, false);
      this.entityCallsForHelp = p_i1660_2_;
      this.func_75248_a(1);
   }

   public boolean func_75250_a() {
      int i = this.field_75299_d.func_142015_aE();
      return i != this.field_142052_b && this.func_75296_a(this.field_75299_d.func_70643_av(), false);
   }

   public void func_75249_e() {
      this.field_75299_d.func_70624_b(this.field_75299_d.func_70643_av());
      this.field_142052_b = this.field_75299_d.func_142015_aE();
      if (this.entityCallsForHelp) {
         double d0 = this.func_111175_f() * 2.0D;
         List list = this.field_75299_d.field_70170_p.func_72872_a(EntityCultist.class, (new AxisAlignedBB(this.field_75299_d.field_70165_t, this.field_75299_d.field_70163_u, this.field_75299_d.field_70161_v, this.field_75299_d.field_70165_t + 1.0D, this.field_75299_d.field_70163_u + 1.0D, this.field_75299_d.field_70161_v + 1.0D)).func_72314_b(d0, 10.0D, d0));
         Iterator iterator = list.iterator();

         label39:
         while(true) {
            while(true) {
               EntityCreature entitycreature;
               do {
                  do {
                     do {
                        if (!iterator.hasNext()) {
                           break label39;
                        }

                        entitycreature = (EntityCreature)iterator.next();
                     } while(this.field_75299_d == entitycreature);
                  } while(entitycreature.func_70638_az() != null);
               } while(entitycreature.func_184191_r(this.field_75299_d.func_70643_av()));

               if (entitycreature instanceof EntityCultistCleric && ((EntityCultistCleric)entitycreature).getIsRitualist()) {
                  ++((EntityCultistCleric)entitycreature).rage;
                  this.field_75299_d.field_70170_p.func_72960_a(entitycreature, (byte)19);
                  if (this.field_75299_d.field_70170_p.field_73012_v.nextInt(3) == 0) {
                     ((EntityCultistCleric)entitycreature).setIsRitualist(false);
                     entitycreature.func_70624_b(this.field_75299_d.func_70643_av());
                  }
               } else {
                  entitycreature.func_70624_b(this.field_75299_d.func_70643_av());
               }
            }
         }
      }

      super.func_75249_e();
   }
}
