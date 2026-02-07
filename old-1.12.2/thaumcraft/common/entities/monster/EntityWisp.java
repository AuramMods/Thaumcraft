package thaumcraft.common.entities.monster;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXWispZap;

public class EntityWisp extends EntityFlying implements IMob {
   public int courseChangeCooldown = 0;
   public double waypointX;
   public double waypointY;
   public double waypointZ;
   private int aggroCooldown = 0;
   public int prevAttackCounter = 0;
   public int attackCounter = 0;
   private BlockPos currentFlightTarget;
   private static final DataParameter<String> TYPE;

   public EntityWisp(World world) {
      super(world);
      this.func_70105_a(0.9F, 0.9F);
      this.field_70728_aV = 5;
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(22.0D);
      this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_111264_e);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(3.0D);
   }

   protected boolean func_70041_e_() {
      return false;
   }

   public int func_70682_h(int par1) {
      return par1;
   }

   public boolean func_70097_a(DamageSource damagesource, float i) {
      if (damagesource.func_76364_f() instanceof EntityLivingBase) {
         this.func_70624_b((EntityLivingBase)damagesource.func_76364_f());
         this.aggroCooldown = 200;
      }

      if (damagesource.func_76346_g() instanceof EntityLivingBase) {
         this.func_70624_b((EntityLivingBase)damagesource.func_76346_g());
         this.aggroCooldown = 200;
      }

      return super.func_70097_a(damagesource, i);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.func_184212_Q().func_187214_a(TYPE, String.valueOf(""));
   }

   public void func_70645_a(DamageSource par1DamageSource) {
      super.func_70645_a(par1DamageSource);
      if (this.field_70170_p.field_72995_K) {
         FXDispatcher.INSTANCE.burst(this.field_70165_t, this.field_70163_u + 0.44999998807907104D, this.field_70161_v, 1.0F);
      }

   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.field_70170_p.field_72995_K && this.field_70173_aa <= 1) {
         FXDispatcher.INSTANCE.burst(this.field_70165_t, this.field_70163_u, this.field_70161_v, 10.0F);
      }

      if (this.field_70170_p.field_72995_K && this.field_70170_p.field_73012_v.nextBoolean() && Aspect.getAspect(this.getType()) != null) {
         FXDispatcher.INSTANCE.drawWispParticles(this.field_70165_t + (double)((this.field_70170_p.field_73012_v.nextFloat() - this.field_70170_p.field_73012_v.nextFloat()) * 0.7F), this.field_70163_u + (double)((this.field_70170_p.field_73012_v.nextFloat() - this.field_70170_p.field_73012_v.nextFloat()) * 0.7F), this.field_70161_v + (double)((this.field_70170_p.field_73012_v.nextFloat() - this.field_70170_p.field_73012_v.nextFloat()) * 0.7F), 0.0D, 0.0D, 0.0D, Aspect.getAspect(this.getType()).getColor(), 0);
      }

      this.field_70181_x *= 0.6000000238418579D;
   }

   public String getType() {
      return (String)this.func_184212_Q().func_187225_a(TYPE);
   }

   public void setType(String t) {
      this.func_184212_Q().func_187227_b(TYPE, String.valueOf(t));
   }

   public void func_70636_d() {
      super.func_70636_d();
      if (this.func_70613_aW()) {
         if (!this.field_70170_p.field_72995_K && Aspect.getAspect(this.getType()) == null) {
            ArrayList as;
            if (this.field_70170_p.field_73012_v.nextInt(10) != 0) {
               as = Aspect.getPrimalAspects();
               this.setType(((Aspect)as.get(this.field_70170_p.field_73012_v.nextInt(as.size()))).getTag());
            } else {
               as = Aspect.getCompoundAspects();
               this.setType(((Aspect)as.get(this.field_70170_p.field_73012_v.nextInt(as.size()))).getTag());
            }
         }

         if (!this.field_70170_p.field_72995_K && this.field_70170_p.func_175659_aa() == EnumDifficulty.PEACEFUL) {
            this.func_70106_y();
         }

         this.prevAttackCounter = this.attackCounter;
         double attackrange = 16.0D;
         double d5;
         double d6;
         double d7;
         float damage;
         float var8;
         if (this.func_70638_az() != null && this.func_70685_l(this.func_70638_az())) {
            if (this.func_70068_e(this.func_70638_az()) > attackrange * attackrange / 2.0D && this.func_70685_l(this.func_70638_az())) {
               d5 = this.func_70638_az().field_70165_t - this.field_70165_t;
               d6 = this.func_70638_az().field_70163_u + (double)(this.func_70638_az().func_70047_e() * 0.66F) - this.field_70163_u;
               d7 = this.func_70638_az().field_70161_v - this.field_70161_v;
               this.field_70159_w += (Math.signum(d5) * 0.5D - this.field_70159_w) * 0.10000000149011612D;
               this.field_70181_x += (Math.signum(d6) * 0.699999988079071D - this.field_70181_x) * 0.10000000149011612D;
               this.field_70179_y += (Math.signum(d7) * 0.5D - this.field_70179_y) * 0.10000000149011612D;
               damage = (float)(Math.atan2(this.field_70179_y, this.field_70159_w) * 180.0D / 3.141592653589793D) - 90.0F;
               var8 = MathHelper.func_76142_g(damage - this.field_70177_z);
               this.field_70701_bs = 0.5F;
               this.field_70177_z += var8;
            }
         } else {
            if (this.currentFlightTarget != null && (!this.field_70170_p.func_175623_d(this.currentFlightTarget) || this.currentFlightTarget.func_177956_o() < 1 || this.currentFlightTarget.func_177956_o() > this.field_70170_p.func_175725_q(this.currentFlightTarget).func_177981_b(8).func_177956_o())) {
               this.currentFlightTarget = null;
            }

            if (this.currentFlightTarget == null || this.field_70146_Z.nextInt(30) == 0 || this.func_174831_c(this.currentFlightTarget) < 4.0D) {
               this.currentFlightTarget = new BlockPos((int)this.field_70165_t + this.field_70146_Z.nextInt(7) - this.field_70146_Z.nextInt(7), (int)this.field_70163_u + this.field_70146_Z.nextInt(6) - 2, (int)this.field_70161_v + this.field_70146_Z.nextInt(7) - this.field_70146_Z.nextInt(7));
            }

            d5 = (double)this.currentFlightTarget.func_177958_n() + 0.5D - this.field_70165_t;
            d6 = (double)this.currentFlightTarget.func_177956_o() + 0.1D - this.field_70163_u;
            d7 = (double)this.currentFlightTarget.func_177952_p() + 0.5D - this.field_70161_v;
            this.field_70159_w += (Math.signum(d5) * 0.5D - this.field_70159_w) * 0.10000000149011612D;
            this.field_70181_x += (Math.signum(d6) * 0.699999988079071D - this.field_70181_x) * 0.10000000149011612D;
            this.field_70179_y += (Math.signum(d7) * 0.5D - this.field_70179_y) * 0.10000000149011612D;
            damage = (float)(Math.atan2(this.field_70179_y, this.field_70159_w) * 180.0D / 3.141592653589793D) - 90.0F;
            var8 = MathHelper.func_76142_g(damage - this.field_70177_z);
            this.field_70701_bs = 0.15F;
            this.field_70177_z += var8;
         }

         if (this.func_70638_az() instanceof EntityPlayer && ((EntityPlayer)this.func_70638_az()).field_71075_bZ.field_75102_a) {
            this.func_70624_b((EntityLivingBase)null);
         }

         if (this.func_70638_az() != null && this.func_70638_az().field_70128_L) {
            this.func_70624_b((EntityLivingBase)null);
         }

         --this.aggroCooldown;
         if (this.field_70170_p.field_73012_v.nextInt(1000) == 0 && (this.func_70638_az() == null || this.aggroCooldown-- <= 0)) {
            this.func_70624_b(this.field_70170_p.func_72890_a(this, 16.0D));
            if (this.func_70638_az() != null) {
               this.aggroCooldown = 50;
            }
         }

         if (!this.field_70128_L && this.func_70638_az() != null && this.func_70638_az().func_70068_e(this) < attackrange * attackrange) {
            d5 = this.func_70638_az().field_70165_t - this.field_70165_t;
            d6 = this.func_70638_az().func_174813_aQ().field_72338_b + (double)(this.func_70638_az().field_70131_O / 2.0F) - (this.field_70163_u + (double)(this.field_70131_O / 2.0F));
            d7 = this.func_70638_az().field_70161_v - this.field_70161_v;
            this.field_70761_aq = this.field_70177_z = -((float)Math.atan2(d5, d7)) * 180.0F / 3.141593F;
            if (this.func_70685_l(this.func_70638_az())) {
               ++this.attackCounter;
               if (this.attackCounter == 20) {
                  this.func_184185_a(SoundsTC.zap, 1.0F, 1.1F);
                  PacketHandler.INSTANCE.sendToAllAround(new PacketFXWispZap(this.func_145782_y(), this.func_70638_az().func_145782_y()), new TargetPoint(this.field_70170_p.field_73011_w.getDimension(), this.field_70165_t, this.field_70163_u, this.field_70161_v, 32.0D));
                  damage = (float)this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e();
                  if (!(Math.abs(this.func_70638_az().field_70159_w) > 0.10000000149011612D) && !(Math.abs(this.func_70638_az().field_70181_x) > 0.10000000149011612D) && !(Math.abs(this.func_70638_az().field_70179_y) > 0.10000000149011612D)) {
                     if (this.field_70170_p.field_73012_v.nextFloat() < 0.66F) {
                        this.func_70638_az().func_70097_a(DamageSource.func_76358_a(this), damage + 1.0F);
                     }
                  } else if (this.field_70170_p.field_73012_v.nextFloat() < 0.4F) {
                     this.func_70638_az().func_70097_a(DamageSource.func_76358_a(this), damage);
                  }

                  this.attackCounter = -20 + this.field_70170_p.field_73012_v.nextInt(20);
               }
            } else if (this.attackCounter > 0) {
               --this.attackCounter;
            }
         }
      }

   }

   protected SoundEvent func_184639_G() {
      return SoundsTC.wisplive;
   }

   protected SoundEvent func_184601_bQ() {
      return SoundEvents.field_187659_cY;
   }

   protected SoundEvent func_184615_bR() {
      return SoundsTC.wispdead;
   }

   protected Item func_146068_u() {
      return Item.func_150899_d(0);
   }

   protected void func_70628_a(boolean flag, int i) {
      if (Aspect.getAspect(this.getType()) != null) {
         this.func_70099_a(ThaumcraftApiHelper.makeCrystal(Aspect.getAspect(this.getType())), 0.0F);
      }

   }

   protected float func_70599_aP() {
      return 0.25F;
   }

   protected boolean func_70692_ba() {
      return true;
   }

   public boolean func_70601_bi() {
      int count = 0;

      try {
         List l = this.field_70170_p.func_72872_a(EntityWisp.class, this.func_174813_aQ().func_72314_b(16.0D, 16.0D, 16.0D));
         if (l != null) {
            count = l.size();
         }
      } catch (Exception var3) {
      }

      return count < 8 && this.field_70170_p.func_175659_aa() != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.func_70601_bi();
   }

   protected boolean isValidLightLevel() {
      BlockPos blockpos = new BlockPos(this.field_70165_t, this.func_174813_aQ().field_72338_b, this.field_70161_v);
      if (this.field_70170_p.func_175642_b(EnumSkyBlock.SKY, blockpos) > this.field_70146_Z.nextInt(32)) {
         return false;
      } else {
         int i = this.field_70170_p.func_175671_l(blockpos);
         if (this.field_70170_p.func_72911_I()) {
            int j = this.field_70170_p.func_175657_ab();
            this.field_70170_p.func_175692_b(10);
            i = this.field_70170_p.func_175671_l(blockpos);
            this.field_70170_p.func_175692_b(j);
         }

         return i <= this.field_70146_Z.nextInt(8);
      }
   }

   public void func_70014_b(NBTTagCompound nbttagcompound) {
      super.func_70014_b(nbttagcompound);
      nbttagcompound.func_74778_a("Type", this.getType());
   }

   public void func_70037_a(NBTTagCompound nbttagcompound) {
      super.func_70037_a(nbttagcompound);
      this.setType(nbttagcompound.func_74779_i("Type"));
   }

   public int func_70641_bl() {
      return 2;
   }

   static {
      TYPE = EntityDataManager.func_187226_a(EntityWisp.class, DataSerializers.field_187194_d);
   }
}
