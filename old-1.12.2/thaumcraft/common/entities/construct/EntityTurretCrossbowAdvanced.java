package thaumcraft.common.entities.construct;

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
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.lang3.StringUtils;
import thaumcraft.Thaumcraft;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.Utils;

public class EntityTurretCrossbowAdvanced extends EntityTurretCrossbow {
   private static final DataParameter<Byte> FLAGS;

   public EntityTurretCrossbowAdvanced(World worldIn) {
      super(worldIn);
      this.func_70105_a(0.95F, 1.5F);
      this.field_70138_W = 0.0F;
   }

   protected void func_184651_r() {
      this.field_70714_bg.field_75782_a.clear();
      this.field_70715_bh.field_75782_a.clear();
      this.field_70714_bg.func_75776_a(1, new EntityAIAttackRanged(this, 0.0D, 20, 40, 24.0F));
      this.field_70714_bg.func_75776_a(2, new EntityTurretCrossbowAdvanced.EntityAIWatchTarget(this));
      this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, false, new Class[0]));
      this.field_70715_bh.func_75776_a(2, new EntityTurretCrossbowAdvanced.EntityAINearestValidTarget(this, EntityLivingBase.class, 5, true, false, (Predicate)null));
      this.setTargetMob(true);
   }

   public float func_70047_e() {
      return 1.0F;
   }

   public EntityTurretCrossbowAdvanced(World worldIn, BlockPos pos) {
      this(worldIn);
      this.func_70080_a((double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o(), (double)pos.func_177952_p() + 0.5D, 0.0F, 0.0F);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.func_184212_Q().func_187214_a(FLAGS, (byte)0);
   }

   public boolean func_70686_a(Class clazz) {
      if (IAnimals.class.isAssignableFrom(clazz) && !IMob.class.isAssignableFrom(clazz) && this.getTargetAnimal()) {
         return true;
      } else if (IMob.class.isAssignableFrom(clazz) && this.getTargetMob()) {
         return true;
      } else if (EntityPlayer.class.isAssignableFrom(clazz) && this.getTargetPlayer()) {
         if (!this.field_70170_p.field_72995_K && !FMLCommonHandler.instance().getMinecraftServerInstance().func_71219_W() && !this.getTargetFriendly()) {
            this.setTargetPlayer(false);
            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean getTargetAnimal() {
      return Utils.getBit((Byte)this.func_184212_Q().func_187225_a(FLAGS), 0);
   }

   public void setTargetAnimal(boolean par1) {
      byte var2 = (Byte)this.func_184212_Q().func_187225_a(FLAGS);
      if (par1) {
         this.func_184212_Q().func_187227_b(FLAGS, (byte)Utils.setBit(var2, 0));
      } else {
         this.func_184212_Q().func_187227_b(FLAGS, (byte)Utils.clearBit(var2, 0));
      }

      this.func_70624_b((EntityLivingBase)null);
   }

   public boolean getTargetMob() {
      return Utils.getBit((Byte)this.func_184212_Q().func_187225_a(FLAGS), 1);
   }

   public void setTargetMob(boolean par1) {
      byte var2 = (Byte)this.func_184212_Q().func_187225_a(FLAGS);
      if (par1) {
         this.func_184212_Q().func_187227_b(FLAGS, (byte)Utils.setBit(var2, 1));
      } else {
         this.func_184212_Q().func_187227_b(FLAGS, (byte)Utils.clearBit(var2, 1));
      }

      this.func_70624_b((EntityLivingBase)null);
   }

   public boolean getTargetPlayer() {
      return Utils.getBit((Byte)this.func_184212_Q().func_187225_a(FLAGS), 2);
   }

   public void setTargetPlayer(boolean par1) {
      byte var2 = (Byte)this.func_184212_Q().func_187225_a(FLAGS);
      if (par1) {
         this.func_184212_Q().func_187227_b(FLAGS, (byte)Utils.setBit(var2, 2));
      } else {
         this.func_184212_Q().func_187227_b(FLAGS, (byte)Utils.clearBit(var2, 2));
      }

      this.func_70624_b((EntityLivingBase)null);
   }

   public boolean getTargetFriendly() {
      return Utils.getBit((Byte)this.func_184212_Q().func_187225_a(FLAGS), 3);
   }

   public void setTargetFriendly(boolean par1) {
      byte var2 = (Byte)this.func_184212_Q().func_187225_a(FLAGS);
      if (par1) {
         this.func_184212_Q().func_187227_b(FLAGS, (byte)Utils.setBit(var2, 3));
      } else {
         this.func_184212_Q().func_187227_b(FLAGS, (byte)Utils.clearBit(var2, 3));
      }

      this.func_70624_b((EntityLivingBase)null);
   }

   public Team func_96124_cp() {
      if (this.isOwned()) {
         EntityLivingBase entitylivingbase = this.getOwnerEntity();
         if (entitylivingbase != null) {
            return entitylivingbase.func_96124_cp();
         }
      }

      return super.func_96124_cp();
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(40.0D);
   }

   public int func_70658_aO() {
      return 8;
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.field_70170_p.field_72995_K && !FMLCommonHandler.instance().getMinecraftServerInstance().func_71219_W() && this.func_70638_az() != null && this.func_70638_az() instanceof EntityPlayer && this.func_70638_az() != this.getOwnerEntity()) {
         this.func_70624_b((EntityLivingBase)null);
      }

   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.func_184212_Q().func_187227_b(FLAGS, nbt.func_74771_c("targets"));
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      nbt.func_74774_a("targets", (Byte)this.func_184212_Q().func_187225_a(FLAGS));
   }

   public void func_70653_a(Entity p_70653_1_, float p_70653_2_, double p_70653_3_, double p_70653_5_) {
      super.func_70653_a(p_70653_1_, p_70653_2_, p_70653_3_ / 10.0D, p_70653_5_ / 10.0D);
   }

   protected boolean func_184645_a(EntityPlayer player, EnumHand hand, ItemStack stack) {
      if (!this.field_70170_p.field_72995_K && this.isOwner(player) && !this.field_70128_L) {
         if (player.func_70093_af()) {
            this.func_184185_a(SoundsTC.zap, 1.0F, 1.0F);
            this.dropAmmo();
            this.func_70099_a(new ItemStack(ItemsTC.turretPlacer, 1, 1), 0.5F);
            this.func_70106_y();
            player.func_184609_a(hand);
         } else {
            player.openGui(Thaumcraft.instance, 17, this.field_70170_p, this.func_145782_y(), 0, 0);
         }

         return true;
      } else {
         return super.func_184645_a(player, hand, stack);
      }
   }

   public void func_70091_d(double x, double y, double z) {
      super.func_70091_d(x / 15.0D, y, z / 15.0D);
   }

   protected void func_70628_a(boolean p_70628_1_, int p_70628_2_) {
      float b = (float)p_70628_2_ * 0.15F;
      if (this.field_70146_Z.nextFloat() < 0.2F + b) {
         this.func_70099_a(new ItemStack(ItemsTC.mind, 1, 1), 0.5F);
      }

      if (this.field_70146_Z.nextFloat() < 0.5F + b) {
         this.func_70099_a(new ItemStack(ItemsTC.gear), 0.5F);
      }

      if (this.field_70146_Z.nextFloat() < 0.5F + b) {
         this.func_70099_a(new ItemStack(BlocksTC.plank), 0.5F);
      }

      if (this.field_70146_Z.nextFloat() < 0.5F + b) {
         this.func_70099_a(new ItemStack(BlocksTC.plank), 0.5F);
      }

      if (this.field_70146_Z.nextFloat() < 0.3F + b) {
         this.func_70099_a(new ItemStack(ItemsTC.plate, 1, 0), 0.5F);
      }

      if (this.field_70146_Z.nextFloat() < 0.4F + b) {
         this.func_70099_a(new ItemStack(ItemsTC.plate, 1, 1), 0.5F);
      }

      if (this.field_70146_Z.nextFloat() < 0.4F + b) {
         this.func_70099_a(new ItemStack(ItemsTC.plate, 1, 1), 0.5F);
      }

   }

   static {
      FLAGS = EntityDataManager.func_187226_a(EntityTurretCrossbowAdvanced.class, DataSerializers.field_187191_a);
   }

   protected class EntityAINearestValidTarget extends EntityAITarget {
      protected final Class targetClass;
      private final int targetChance;
      protected final net.minecraft.entity.ai.EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
      protected Predicate targetEntitySelector;
      protected EntityLivingBase targetEntity;
      private int targetUnseenTicks;

      public EntityAINearestValidTarget(EntityCreature p_i45878_1_, Class p_i45878_2_, boolean p_i45878_3_) {
         this(p_i45878_1_, p_i45878_2_, p_i45878_3_, false);
      }

      public EntityAINearestValidTarget(EntityCreature p_i45879_1_, Class p_i45879_2_, boolean p_i45879_3_, boolean p_i45879_4_) {
         this(p_i45879_1_, p_i45879_2_, 10, p_i45879_3_, p_i45879_4_, (Predicate)null);
      }

      public EntityAINearestValidTarget(EntityCreature p_i45880_1_, Class p_i45880_2_, int p_i45880_3_, boolean p_i45880_4_, boolean p_i45880_5_, final Predicate tselector) {
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
                     double d0 = EntityAINearestValidTarget.this.func_111175_f();
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

                     if ((double)entity.func_70032_d(EntityAINearestValidTarget.this.field_75299_d) > d0) {
                        return false;
                     }
                  }

                  return EntityAINearestValidTarget.this.func_75296_a(entity, false);
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
            if (team != null && team1 == team && !((EntityTurretCrossbowAdvanced)this.field_75299_d).getTargetFriendly()) {
               return false;
            } else if (team != null && team1 != team && ((EntityTurretCrossbowAdvanced)this.field_75299_d).getTargetFriendly()) {
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
            if (team != null && team1 == team && !((EntityTurretCrossbowAdvanced)attacker).getTargetFriendly()) {
               return false;
            } else if (team != null && team1 != team && ((EntityTurretCrossbowAdvanced)attacker).getTargetFriendly()) {
               return false;
            } else {
               if (attacker instanceof IEntityOwnable && StringUtils.isNotEmpty(((IEntityOwnable)attacker).func_184753_b().toString())) {
                  if (posTar instanceof IEntityOwnable && ((IEntityOwnable)attacker).func_184753_b().equals(((IEntityOwnable)posTar).func_184753_b()) && !((EntityTurretCrossbowAdvanced)attacker).getTargetFriendly()) {
                     return false;
                  }

                  if (!(posTar instanceof IEntityOwnable) && !(posTar instanceof EntityPlayer) && ((EntityTurretCrossbowAdvanced)attacker).getTargetFriendly()) {
                     return false;
                  }

                  if (posTar instanceof IEntityOwnable && !((IEntityOwnable)attacker).func_184753_b().equals(((IEntityOwnable)posTar).func_184753_b()) && ((EntityTurretCrossbowAdvanced)attacker).getTargetFriendly()) {
                     return false;
                  }

                  if (posTar == ((IEntityOwnable)attacker).func_70902_q() && !((EntityTurretCrossbowAdvanced)attacker).getTargetFriendly()) {
                     return false;
                  }
               } else if (posTar instanceof EntityPlayer && !p_179445_2_ && ((EntityPlayer)posTar).field_71075_bZ.field_75102_a && !((EntityTurretCrossbowAdvanced)attacker).getTargetFriendly()) {
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

   protected class EntityAIWatchTarget extends EntityAIBase {
      protected EntityLiving theWatcher;
      protected Entity closestEntity;
      private int lookTime;

      public EntityAIWatchTarget(EntityLiving p_i1631_1_) {
         this.theWatcher = p_i1631_1_;
         this.func_75248_a(2);
      }

      public boolean func_75250_a() {
         if (this.theWatcher.func_70638_az() != null) {
            this.closestEntity = this.theWatcher.func_70638_az();
         }

         return this.closestEntity != null;
      }

      public boolean func_75253_b() {
         float d = (float)this.getTargetDistance();
         return !this.closestEntity.func_70089_S() ? false : (this.theWatcher.func_70068_e(this.closestEntity) > (double)(d * d) ? false : this.lookTime > 0);
      }

      public void func_75249_e() {
         this.lookTime = 40 + this.theWatcher.func_70681_au().nextInt(40);
      }

      public void func_75251_c() {
         this.closestEntity = null;
      }

      public void func_75246_d() {
         this.theWatcher.func_70671_ap().func_75650_a(this.closestEntity.field_70165_t, this.closestEntity.field_70163_u + (double)this.closestEntity.func_70047_e(), this.closestEntity.field_70161_v, 10.0F, (float)this.theWatcher.func_70646_bf());
         --this.lookTime;
      }

      protected double getTargetDistance() {
         IAttributeInstance iattributeinstance = this.theWatcher.func_110148_a(SharedMonsterAttributes.field_111265_b);
         return iattributeinstance == null ? 16.0D : iattributeinstance.func_111126_e();
      }
   }
}
