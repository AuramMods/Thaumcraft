package thaumcraft.common.entities.construct.golem.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.StringUtils;

public class AINearestValidTarget extends EntityAITarget {
   protected final Class targetClass;
   private final int targetChance;
   protected final net.minecraft.entity.ai.EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
   protected Predicate targetEntitySelector;
   protected EntityLivingBase targetEntity;
   private int targetUnseenTicks;

   public AINearestValidTarget(EntityCreature p_i45878_1_, Class p_i45878_2_, boolean p_i45878_3_) {
      this(p_i45878_1_, p_i45878_2_, p_i45878_3_, false);
   }

   public AINearestValidTarget(EntityCreature p_i45879_1_, Class p_i45879_2_, boolean p_i45879_3_, boolean p_i45879_4_) {
      this(p_i45879_1_, p_i45879_2_, 10, p_i45879_3_, p_i45879_4_, (Predicate)null);
   }

   public AINearestValidTarget(EntityCreature p_i45880_1_, Class p_i45880_2_, int p_i45880_3_, boolean p_i45880_4_, boolean p_i45880_5_, final Predicate tselector) {
      super(p_i45880_1_, p_i45880_4_, p_i45880_5_);
      this.targetClass = p_i45880_2_;
      this.targetChance = p_i45880_3_;
      this.theNearestAttackableTargetSorter = new net.minecraft.entity.ai.EntityAINearestAttackableTarget.Sorter(p_i45880_1_);
      this.func_75248_a(1);
      this.targetEntitySelector = new Predicate() {
         private static final String __OBFID = "CL_00001621";

         public boolean applySelection(EntityLivingBase entity) {
            if (tselector != null && !tselector.apply(entity)) {
               return false;
            } else {
               if (entity instanceof EntityPlayer) {
                  double d0 = AINearestValidTarget.this.func_111175_f();
                  if (entity.func_70093_af()) {
                     d0 *= 0.800000011920929D;
                  }

                  if (entity.func_82150_aj()) {
                     float f = ((EntityPlayer)entity).func_82243_bO();
                     if (f < 0.1F) {
                        f = 0.1F;
                     }

                     d0 *= (double)(0.7F * f);
                  }

                  if ((double)entity.func_70032_d(AINearestValidTarget.this.field_75299_d) > d0) {
                     return false;
                  }
               }

               return AINearestValidTarget.this.func_75296_a(entity, false);
            }
         }

         public boolean apply(Object p_apply_1_) {
            return this.applySelection((EntityLivingBase)p_apply_1_);
         }
      };
   }

   public boolean func_75253_b() {
      EntityLivingBase entitylivingbase = this.field_75299_d.func_70638_az();
      if (entitylivingbase == null) {
         return false;
      } else if (!entitylivingbase.func_70089_S()) {
         return false;
      } else {
         Team team = this.field_75299_d.func_96124_cp();
         Team team1 = entitylivingbase.func_96124_cp();
         if (team != null && team1 == team && !((ITargets)this.field_75299_d).getTargetFriendly()) {
            return false;
         } else if (team != null && team1 != team && ((ITargets)this.field_75299_d).getTargetFriendly()) {
            return false;
         } else {
            double d0 = this.func_111175_f();
            if (this.field_75299_d.func_70068_e(entitylivingbase) > d0 * d0) {
               return false;
            } else {
               if (this.field_75297_f) {
                  if (this.field_75299_d.func_70635_at().func_75522_a(entitylivingbase)) {
                     this.targetUnseenTicks = 0;
                  } else if (++this.targetUnseenTicks > 60) {
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }

   protected boolean func_75296_a(EntityLivingBase p_75296_1_, boolean p_75296_2_) {
      if (!this.isGoodTarget(this.field_75299_d, p_75296_1_, p_75296_2_, this.field_75297_f)) {
         return false;
      } else {
         return this.field_75299_d.func_180485_d(new BlockPos(p_75296_1_));
      }
   }

   private boolean isGoodTarget(EntityLiving attacker, EntityLivingBase posTar, boolean p_179445_2_, boolean checkSight) {
      if (posTar == null) {
         return false;
      } else if (posTar == attacker) {
         return false;
      } else if (!posTar.func_70089_S()) {
         return false;
      } else if (!attacker.func_70686_a(posTar.getClass())) {
         return false;
      } else {
         Team team = attacker.func_96124_cp();
         Team team1 = posTar.func_96124_cp();
         if (team != null && team1 == team && !((ITargets)attacker).getTargetFriendly()) {
            return false;
         } else if (team != null && team1 != team && ((ITargets)attacker).getTargetFriendly()) {
            return false;
         } else {
            if (attacker instanceof IEntityOwnable && StringUtils.isNotEmpty(((IEntityOwnable)attacker).func_184753_b().toString())) {
               if (posTar instanceof IEntityOwnable && ((IEntityOwnable)attacker).func_184753_b().equals(((IEntityOwnable)posTar).func_184753_b()) && !((ITargets)attacker).getTargetFriendly()) {
                  return false;
               }

               if (!(posTar instanceof IEntityOwnable) && !(posTar instanceof EntityPlayer) && ((ITargets)attacker).getTargetFriendly()) {
                  return false;
               }

               if (posTar instanceof IEntityOwnable && !((IEntityOwnable)attacker).func_184753_b().equals(((IEntityOwnable)posTar).func_184753_b()) && ((ITargets)attacker).getTargetFriendly()) {
                  return false;
               }

               if (posTar == ((IEntityOwnable)attacker).func_70902_q() && !((ITargets)attacker).getTargetFriendly()) {
                  return false;
               }
            } else if (posTar instanceof EntityPlayer && !p_179445_2_ && ((EntityPlayer)posTar).field_71075_bZ.field_75102_a && !((ITargets)attacker).getTargetFriendly()) {
               return false;
            }

            return !checkSight || attacker.func_70635_at().func_75522_a(posTar);
         }
      }
   }

   public boolean func_75250_a() {
      if (this.targetChance > 0 && this.field_75299_d.func_70681_au().nextInt(this.targetChance) != 0) {
         return false;
      } else {
         double d0 = this.func_111175_f();
         List list = this.field_75299_d.field_70170_p.func_175647_a(this.targetClass, this.field_75299_d.func_174813_aQ().func_72314_b(d0, 4.0D, d0), Predicates.and(this.targetEntitySelector, EntitySelectors.field_180132_d));
         Collections.sort(list, this.theNearestAttackableTargetSorter);
         if (list.isEmpty()) {
            return false;
         } else {
            this.targetEntity = (EntityLivingBase)list.get(0);
            return true;
         }
      }
   }

   public void func_75249_e() {
      this.field_75299_d.func_70624_b(this.targetEntity);
      this.targetUnseenTicks = 0;
      super.func_75249_e();
   }

   public class Sorter implements Comparator {
      private final Entity theEntity;
      private static final String __OBFID = "CL_00001622";

      public Sorter(Entity p_i1662_1_) {
         this.theEntity = p_i1662_1_;
      }

      public int compare(Entity p_compare_1_, Entity p_compare_2_) {
         double d0 = this.theEntity.func_70068_e(p_compare_1_);
         double d1 = this.theEntity.func_70068_e(p_compare_2_);
         return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
      }

      public int compare(Object p_compare_1_, Object p_compare_2_) {
         return this.compare((Entity)p_compare_1_, (Entity)p_compare_2_);
      }
   }
}
