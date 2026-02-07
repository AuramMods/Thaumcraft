package thaumcraft.common.entities.construct;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Team;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.Thaumcraft;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.lib.SoundsTC;

public class EntityTurretCrossbow extends EntityOwnedConstruct implements IRangedAttackMob {
   int loadProgressInt;
   boolean isLoadInProgress;
   float loadProgress;
   float prevLoadProgress;
   public float loadProgressForRender;
   boolean attackedLastTick;
   int attackCount;

   public EntityTurretCrossbow(World worldIn) {
      super(worldIn);
      this.loadProgressInt = 0;
      this.isLoadInProgress = false;
      this.loadProgress = 0.0F;
      this.prevLoadProgress = 0.0F;
      this.loadProgressForRender = 0.0F;
      this.attackedLastTick = false;
      this.attackCount = 0;
      this.func_70105_a(0.95F, 1.25F);
      this.field_70138_W = 0.0F;
   }

   protected void func_184651_r() {
      this.field_70714_bg.func_75776_a(1, new EntityAIAttackRanged(this, 0.0D, 20, 60, 24.0F));
      this.field_70714_bg.func_75776_a(2, new EntityTurretCrossbow.EntityAIWatchTarget(this));
      this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, false, new Class[0]));
      this.field_70715_bh.func_75776_a(2, new EntityTurretCrossbow.EntityAINearestValidTarget(this, EntityLiving.class, 5, true, false, IMob.field_82192_a));
   }

   public EntityTurretCrossbow(World worldIn, BlockPos pos) {
      this(worldIn);
      this.func_70080_a((double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o(), (double)pos.func_177952_p() + 0.5D, 0.0F, 0.0F);
   }

   public void func_82196_d(EntityLivingBase target, float range) {
      if (this.func_184614_ca() != null && this.func_184614_ca().field_77994_a > 0) {
         EntityTippedArrow entityarrow = new EntityTippedArrow(this.field_70170_p, this);
         entityarrow.func_70239_b(2.25D + (double)(range * 2.0F) + this.field_70146_Z.nextGaussian() * 0.25D);
         entityarrow.func_184555_a(this.func_184614_ca());
         Vec3d vec3d = this.func_70676_i(1.0F);
         if (!this.func_184218_aH()) {
            entityarrow.field_70165_t -= vec3d.field_72450_a * 0.8999999761581421D;
            entityarrow.field_70163_u -= vec3d.field_72448_b * 0.8999999761581421D;
            entityarrow.field_70161_v -= vec3d.field_72449_c * 0.8999999761581421D;
         } else {
            entityarrow.field_70165_t += vec3d.field_72450_a * 1.75D;
            entityarrow.field_70163_u += vec3d.field_72448_b * 1.75D;
            entityarrow.field_70161_v += vec3d.field_72449_c * 1.75D;
         }

         entityarrow.field_70251_a = PickupStatus.DISALLOWED;
         double d0 = target.field_70165_t - this.field_70165_t;
         double d1 = target.func_174813_aQ().field_72338_b + (double)target.func_70047_e() + (double)(range * range * 3.0F) - entityarrow.field_70163_u;
         double d2 = target.field_70161_v - this.field_70161_v;
         entityarrow.func_70186_c(d0, d1, d2, 2.0F, 2.0F);
         this.field_70170_p.func_72838_d(entityarrow);
         this.field_70170_p.func_72960_a(this, (byte)16);
         this.func_184185_a(SoundEvents.field_187737_v, 1.0F, 1.0F / (this.func_70681_au().nextFloat() * 0.4F + 0.8F));
         --this.func_184614_ca().field_77994_a;
         if (this.func_184614_ca().field_77994_a <= 0) {
            this.func_184611_a(EnumHand.MAIN_HAND, (ItemStack)null);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_70103_a(byte par1) {
      if (par1 == 16) {
         if (!this.field_82175_bq) {
            this.field_110158_av = -1;
            this.field_82175_bq = true;
         }
      } else if (par1 == 17) {
         if (!this.isLoadInProgress) {
            this.loadProgressInt = -1;
            this.isLoadInProgress = true;
         }
      } else {
         super.func_70103_a(par1);
      }

   }

   @SideOnly(Side.CLIENT)
   public float getLoadProgress(float pt) {
      float f1 = this.loadProgress - this.prevLoadProgress;
      if (f1 < 0.0F) {
         ++f1;
      }

      return this.prevLoadProgress + f1 * pt;
   }

   protected void func_82168_bl() {
      if (this.field_82175_bq) {
         ++this.field_110158_av;
         if (this.field_110158_av >= 6) {
            this.field_110158_av = 0;
            this.field_82175_bq = false;
         }
      } else {
         this.field_110158_av = 0;
      }

      this.field_70733_aJ = (float)this.field_110158_av / 6.0F;
      if (this.isLoadInProgress) {
         ++this.loadProgressInt;
         if (this.loadProgressInt >= 10) {
            this.loadProgressInt = 0;
            this.isLoadInProgress = false;
         }
      } else {
         this.loadProgressInt = 0;
      }

      this.loadProgress = (float)this.loadProgressInt / 10.0F;
   }

   public void func_70030_z() {
      this.prevLoadProgress = this.loadProgress;
      if (!this.field_70170_p.field_72995_K && (this.func_184614_ca() == null || this.func_184614_ca().field_77994_a <= 0)) {
         BlockPos p = this.func_180425_c().func_177977_b();
         TileEntity t = this.field_70170_p.func_175625_s(p);
         if (t != null && t instanceof TileEntityDispenser && EnumFacing.func_82600_a(t.func_145832_p() & 7) == EnumFacing.UP) {
            TileEntityDispenser d = (TileEntityDispenser)t;

            for(int a = 0; a < d.func_70302_i_(); ++a) {
               if (d.func_70301_a(a) != null && d.func_70301_a(a).func_77973_b() instanceof ItemArrow) {
                  this.func_184611_a(EnumHand.MAIN_HAND, d.func_70298_a(a, d.func_70301_a(a).field_77994_a));
                  this.func_184185_a(SoundsTC.ticks, 1.0F, 1.0F);
                  this.field_70170_p.func_72960_a(this, (byte)17);
                  break;
               }
            }
         }
      }

      super.func_70030_z();
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

   public float func_70047_e() {
      return this.field_70131_O * 0.66F;
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(30.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(24.0D);
   }

   public int func_70658_aO() {
      return 2;
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.func_70638_az() != null && this.func_184191_r(this.func_70638_az())) {
         this.func_70624_b((EntityLivingBase)null);
      }

      if (!this.field_70170_p.field_72995_K) {
         this.field_70177_z = this.field_70759_as;
         if (this.field_70173_aa % 80 == 0) {
            this.func_70691_i(1.0F);
         }

         int k = MathHelper.func_76128_c(this.field_70165_t);
         int l = MathHelper.func_76128_c(this.field_70163_u);
         int i1 = MathHelper.func_76128_c(this.field_70161_v);
         if (BlockRailBase.func_176562_d(this.field_70170_p, new BlockPos(k, l - 1, i1))) {
            --l;
         }

         BlockPos blockpos = new BlockPos(k, l, i1);
         IBlockState iblockstate = this.field_70170_p.func_180495_p(blockpos);
         if (BlockRailBase.func_176563_d(iblockstate) && iblockstate.func_177230_c() == BlocksTC.activatorRail) {
            boolean ac = (Boolean)iblockstate.func_177229_b(BlockRailPowered.field_176569_M);
            this.func_94061_f(ac);
         }
      } else {
         this.func_82168_bl();
      }

   }

   public boolean func_70104_M() {
      return true;
   }

   public boolean func_70067_L() {
      return true;
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
   }

   public boolean func_70097_a(DamageSource source, float amount) {
      this.field_70177_z = (float)((double)this.field_70177_z + this.func_70681_au().nextGaussian() * 45.0D);
      this.field_70125_A = (float)((double)this.field_70125_A + this.func_70681_au().nextGaussian() * 20.0D);
      return super.func_70097_a(source, amount);
   }

   public void func_70653_a(Entity p_70653_1_, float p_70653_2_, double p_70653_3_, double p_70653_5_) {
      super.func_70653_a(p_70653_1_, p_70653_2_, p_70653_3_ / 10.0D, p_70653_5_ / 10.0D);
      if (this.field_70181_x > 0.1D) {
         this.field_70181_x = 0.1D;
      }

   }

   protected boolean func_184645_a(EntityPlayer player, EnumHand hand, ItemStack stack) {
      if (!this.field_70170_p.field_72995_K && this.isOwner(player) && !this.field_70128_L) {
         if (player.func_70093_af()) {
            this.func_184185_a(SoundsTC.zap, 1.0F, 1.0F);
            this.dropAmmo();
            this.func_70099_a(new ItemStack(ItemsTC.turretPlacer, 1, 0), 0.5F);
            this.func_70106_y();
            player.func_184609_a(hand);
         } else {
            player.openGui(Thaumcraft.instance, 16, this.field_70170_p, this.func_145782_y(), 0, 0);
         }

         return true;
      } else {
         return super.func_184645_a(player, hand, stack);
      }
   }

   public void func_70091_d(double x, double y, double z) {
      super.func_70091_d(x / 20.0D, y, z / 20.0D);
   }

   public void func_70645_a(DamageSource cause) {
      super.func_70645_a(cause);
      if (!this.field_70170_p.field_72995_K) {
         this.dropAmmo();
      }

   }

   protected void dropAmmo() {
      if (this.func_184614_ca() != null) {
         this.func_70099_a(this.func_184614_ca(), 0.5F);
      }

   }

   protected void func_70628_a(boolean p_70628_1_, int p_70628_2_) {
      float b = (float)p_70628_2_ * 0.15F;
      if (this.field_70146_Z.nextFloat() < 0.2F + b) {
         this.func_70099_a(new ItemStack(ItemsTC.mind), 0.5F);
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

   }

   protected RayTraceResult getRayTraceResult() {
      float f = this.field_70127_C + (this.field_70125_A - this.field_70127_C);
      float f1 = this.field_70126_B + (this.field_70177_z - this.field_70126_B);
      double d0 = this.field_70169_q + (this.field_70165_t - this.field_70169_q);
      double d1 = this.field_70167_r + (this.field_70163_u - this.field_70167_r) + (double)this.func_70047_e();
      double d2 = this.field_70166_s + (this.field_70161_v - this.field_70166_s);
      Vec3d vec3 = new Vec3d(d0, d1, d2);
      float f2 = MathHelper.func_76134_b(-f1 * 0.017453292F - 3.1415927F);
      float f3 = MathHelper.func_76126_a(-f1 * 0.017453292F - 3.1415927F);
      float f4 = -MathHelper.func_76134_b(-f * 0.017453292F);
      float f5 = MathHelper.func_76126_a(-f * 0.017453292F);
      float f6 = f3 * f4;
      float f7 = f2 * f4;
      double d3 = 5.0D;
      Vec3d vec31 = vec3.func_72441_c((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
      return this.field_70170_p.func_147447_a(vec3, vec31, true, false, false);
   }

   public int func_70646_bf() {
      return 20;
   }

   protected class EntityAINearestValidTarget extends EntityAITarget {
      protected final Class targetClass;
      private final int targetChance;
      protected final net.minecraft.entity.ai.EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
      protected Predicate targetEntitySelector;
      protected EntityLivingBase targetEntity;

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

      protected boolean func_75296_a(EntityLivingBase p_75296_1_, boolean p_75296_2_) {
         if (!func_179445_a(this.field_75299_d, p_75296_1_, p_75296_2_, this.field_75297_f)) {
            return false;
         } else {
            return this.field_75299_d.func_180485_d(new BlockPos(p_75296_1_));
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
