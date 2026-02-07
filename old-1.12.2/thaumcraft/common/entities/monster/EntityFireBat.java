package thaumcraft.common.entities.monster;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;

public class EntityFireBat extends EntityMob {
   private BlockPos currentFlightTarget;
   public EntityLivingBase owner = null;
   private static final DataParameter<Boolean> HANGING;
   private static final DataParameter<Boolean> EXPLOSIVE;
   private static final DataParameter<Boolean> DEVIL;
   private static final DataParameter<Boolean> SUMMONED;
   private static final DataParameter<Boolean> VAMPIRE;
   public int damBonus = 0;
   private int attackTime;

   public EntityFireBat(World par1World) {
      super(par1World);
      this.func_70105_a(0.5F, 0.9F);
      this.setIsBatHanging(true);
      this.field_70178_ae = true;
   }

   public void func_70088_a() {
      super.func_70088_a();
      this.func_184212_Q().func_187214_a(HANGING, false);
      this.func_184212_Q().func_187214_a(EXPLOSIVE, false);
      this.func_184212_Q().func_187214_a(DEVIL, false);
      this.func_184212_Q().func_187214_a(SUMMONED, false);
      this.func_184212_Q().func_187214_a(VAMPIRE, false);
   }

   @SideOnly(Side.CLIENT)
   public int func_70070_b(float par1) {
      return 15728880;
   }

   public float func_70013_c(float par1) {
      return 1.0F;
   }

   protected float func_70599_aP() {
      return 0.1F;
   }

   protected float func_70647_i() {
      return super.func_70647_i() * 0.95F;
   }

   protected SoundEvent func_184639_G() {
      return this.getIsBatHanging() && this.field_70146_Z.nextInt(4) != 0 ? null : SoundEvents.field_187740_w;
   }

   protected SoundEvent func_184601_bQ() {
      return SoundEvents.field_187743_y;
   }

   protected SoundEvent func_184615_bR() {
      return SoundEvents.field_187742_x;
   }

   public boolean func_70104_M() {
      return false;
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(this.getIsDevil() ? 15.0D : 5.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(this.getIsSummoned() ? (double)((this.getIsDevil() ? 3 : 2) + this.damBonus) : 1.0D);
   }

   public boolean getIsBatHanging() {
      return (Boolean)this.func_184212_Q().func_187225_a(HANGING);
   }

   public void setIsBatHanging(boolean par1) {
      this.func_184212_Q().func_187227_b(HANGING, par1);
   }

   public boolean getIsSummoned() {
      return (Boolean)this.func_184212_Q().func_187225_a(SUMMONED);
   }

   public void setIsSummoned(boolean par1) {
      this.func_184212_Q().func_187227_b(SUMMONED, par1);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(par1 ? (double)((this.getIsDevil() ? 3 : 2) + this.damBonus) : 1.0D);
   }

   public boolean getIsExplosive() {
      return (Boolean)this.func_184212_Q().func_187225_a(EXPLOSIVE);
   }

   public void setIsExplosive(boolean par1) {
      this.func_184212_Q().func_187227_b(EXPLOSIVE, par1);
   }

   public boolean getIsDevil() {
      return (Boolean)this.func_184212_Q().func_187225_a(DEVIL);
   }

   public void setIsDevil(boolean par1) {
      this.func_184212_Q().func_187227_b(DEVIL, par1);
      if (par1) {
         this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(this.getIsSummoned() ? (double)((par1 ? 3 : 2) + this.damBonus) : 1.0D);
      }

   }

   public boolean getIsVampire() {
      return (Boolean)this.func_184212_Q().func_187225_a(VAMPIRE);
   }

   public void setIsVampire(boolean par1) {
      this.func_184212_Q().func_187227_b(VAMPIRE, par1);
   }

   public void func_70636_d() {
      if (this.func_70026_G()) {
         this.func_70097_a(DamageSource.field_76369_e, 1.0F);
      }

      super.func_70636_d();
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.field_70170_p.field_72995_K && this.getIsExplosive()) {
         FXDispatcher.INSTANCE.drawGenericParticles(this.field_70169_q + (double)((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.1F), this.field_70167_r + (double)(this.field_70131_O / 2.0F) + (double)((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.1F), this.field_70166_s + (double)((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.1F), 0.0D, 0.0D, 0.0D, 1.0F, 1.0F, 1.0F, 0.8F, false, 151, 9, 1, 7 + this.field_70146_Z.nextInt(5), 0, 1.0F + this.field_70146_Z.nextFloat() * 0.5F, 0.0F, 0);
      }

      if (this.getIsBatHanging()) {
         this.field_70159_w = this.field_70181_x = this.field_70179_y = 0.0D;
         this.field_70163_u = (double)MathHelper.func_76128_c(this.field_70163_u) + 1.0D - (double)this.field_70131_O;
      } else {
         this.field_70181_x *= 0.6000000238418579D;
      }

      if (this.field_70170_p.field_72995_K && !this.getIsVampire()) {
         for(double i = 0.0D; i < 9.0D; ++i) {
            double coeff = i / 9.0D;
            FXDispatcher.INSTANCE.drawFireMote((float)(this.field_70169_q + (this.field_70165_t - this.field_70169_q) * coeff), (float)(this.field_70167_r + (this.field_70163_u - this.field_70167_r) * coeff) + this.field_70131_O / 2.0F, (float)(this.field_70166_s + (this.field_70161_v - this.field_70166_s) * coeff), 0.0125F * (this.field_70146_Z.nextFloat() - 0.5F), 0.0125F * (this.field_70146_Z.nextFloat() - 0.5F), 0.0125F * (this.field_70146_Z.nextFloat() - 0.5F), 255.0F, 64.0F, 16.0F, 0.1F, 5.0F);
         }
      }

   }

   protected void func_70619_bc() {
      super.func_70619_bc();
      if (this.attackTime > 0) {
         --this.attackTime;
      }

      BlockPos blockpos = new BlockPos(this);
      BlockPos blockpos1 = blockpos.func_177984_a();
      if (this.getIsBatHanging()) {
         if (!this.field_70170_p.func_180495_p(blockpos1).func_185915_l()) {
            this.setIsBatHanging(false);
            this.field_70170_p.func_180498_a((EntityPlayer)null, 1015, blockpos, 0);
         } else {
            if (this.field_70146_Z.nextInt(200) == 0) {
               this.field_70759_as = (float)this.field_70146_Z.nextInt(360);
            }

            if (this.field_70170_p.func_72890_a(this, 4.0D) != null) {
               this.setIsBatHanging(false);
               this.field_70170_p.func_180498_a((EntityPlayer)null, 1015, blockpos, 0);
            }
         }
      } else {
         double var1;
         double var3;
         double var5;
         float var7;
         float var8;
         if (this.func_70638_az() == null) {
            if (this.getIsSummoned()) {
               this.func_70097_a(DamageSource.field_76377_j, 2.0F);
            }

            if (this.currentFlightTarget != null && (!this.field_70170_p.func_175623_d(this.currentFlightTarget) || this.currentFlightTarget.func_177956_o() < 1)) {
               this.currentFlightTarget = null;
            }

            if (this.currentFlightTarget == null || this.field_70146_Z.nextInt(30) == 0 || this.func_174831_c(this.currentFlightTarget) < 4.0D) {
               this.currentFlightTarget = new BlockPos((int)this.field_70165_t + this.field_70146_Z.nextInt(7) - this.field_70146_Z.nextInt(7), (int)this.field_70163_u + this.field_70146_Z.nextInt(6) - 2, (int)this.field_70161_v + this.field_70146_Z.nextInt(7) - this.field_70146_Z.nextInt(7));
            }

            var1 = (double)this.currentFlightTarget.func_177958_n() + 0.5D - this.field_70165_t;
            var3 = (double)this.currentFlightTarget.func_177956_o() + 0.1D - this.field_70163_u;
            var5 = (double)this.currentFlightTarget.func_177952_p() + 0.5D - this.field_70161_v;
            this.field_70159_w += (Math.signum(var1) * 0.5D - this.field_70159_w) * 0.10000000149011612D;
            this.field_70181_x += (Math.signum(var3) * 0.699999988079071D - this.field_70181_x) * 0.10000000149011612D;
            this.field_70179_y += (Math.signum(var5) * 0.5D - this.field_70179_y) * 0.10000000149011612D;
            var7 = (float)(Math.atan2(this.field_70179_y, this.field_70159_w) * 180.0D / 3.141592653589793D) - 90.0F;
            var8 = MathHelper.func_76142_g(var7 - this.field_70177_z);
            this.field_70701_bs = 0.5F;
            this.field_70177_z += var8;
            if (this.field_70146_Z.nextInt(100) == 0 && this.field_70170_p.func_180495_p(blockpos1).func_185915_l()) {
               this.setIsBatHanging(true);
            }
         } else {
            var1 = this.func_70638_az().field_70165_t - this.field_70165_t;
            var3 = this.func_70638_az().field_70163_u + (double)(this.func_70638_az().func_70047_e() * 0.66F) - this.field_70163_u;
            var5 = this.func_70638_az().field_70161_v - this.field_70161_v;
            this.field_70159_w += (Math.signum(var1) * 0.5D - this.field_70159_w) * 0.10000000149011612D;
            this.field_70181_x += (Math.signum(var3) * 0.699999988079071D - this.field_70181_x) * 0.10000000149011612D;
            this.field_70179_y += (Math.signum(var5) * 0.5D - this.field_70179_y) * 0.10000000149011612D;
            var7 = (float)(Math.atan2(this.field_70179_y, this.field_70159_w) * 180.0D / 3.141592653589793D) - 90.0F;
            var8 = MathHelper.func_76142_g(var7 - this.field_70177_z);
            this.field_70701_bs = 0.5F;
            this.field_70177_z += var8;
         }
      }

      if (this.func_70638_az() == null) {
         this.func_70624_b(this.findPlayerToAttack());
      } else if (this.func_70638_az().func_70089_S()) {
         float f = this.func_70638_az().func_70032_d(this);
         if (this.func_70685_l(this.func_70638_az())) {
            this.attackEntity(this.func_70638_az(), f);
         }
      } else {
         this.func_70624_b((EntityLivingBase)null);
      }

      if (this.func_70638_az() instanceof EntityPlayer && ((EntityPlayer)this.func_70638_az()).field_71075_bZ.field_75102_a) {
         this.func_70624_b((EntityLivingBase)null);
      }

   }

   protected boolean func_70041_e_() {
      return false;
   }

   public void func_180430_e(float par1, float damageMultiplier) {
   }

   protected void func_184231_a(double p_180433_1_, boolean p_180433_3_, IBlockState state, BlockPos pos) {
   }

   public boolean func_145773_az() {
      return true;
   }

   public boolean func_70097_a(DamageSource par1DamageSource, float par2) {
      if (!this.func_180431_b(par1DamageSource) && !par1DamageSource.func_76347_k() && !par1DamageSource.func_94541_c()) {
         if (!this.field_70170_p.field_72995_K && this.getIsBatHanging()) {
            this.setIsBatHanging(false);
         }

         return super.func_70097_a(par1DamageSource, par2);
      } else {
         return false;
      }
   }

   protected void attackEntity(Entity par1Entity, float par2) {
      if (this.attackTime <= 0 && par2 < Math.max(2.5F, par1Entity.field_70130_N * 1.1F) && par1Entity.func_174813_aQ().field_72337_e > this.func_174813_aQ().field_72338_b && par1Entity.func_174813_aQ().field_72338_b < this.func_174813_aQ().field_72337_e) {
         if (this.getIsSummoned()) {
            ((EntityLivingBase)par1Entity).field_70718_bc = 100;
         }

         if (this.getIsVampire()) {
            if (this.owner != null && !this.owner.func_70644_a(MobEffects.field_76428_l)) {
               this.owner.func_70690_d(new PotionEffect(MobEffects.field_76428_l, 26, 1));
            }

            this.func_70691_i(1.0F);
         }

         this.attackTime = 20;
         if ((this.getIsExplosive() || this.field_70170_p.field_73012_v.nextInt(10) == 0) && !this.field_70170_p.field_72995_K && !this.getIsDevil()) {
            par1Entity.field_70172_ad = 0;
            this.field_70170_p.func_72885_a(this, this.field_70165_t, this.field_70163_u, this.field_70161_v, 1.5F + (this.getIsExplosive() ? (float)this.damBonus * 0.33F : 0.0F), false, false);
            this.func_70106_y();
         } else if (!this.getIsVampire() && !this.field_70170_p.field_73012_v.nextBoolean()) {
            par1Entity.func_70015_d(this.getIsSummoned() ? 4 : 2);
         } else {
            double mx = par1Entity.field_70159_w;
            double my = par1Entity.field_70181_x;
            double mz = par1Entity.field_70179_y;
            this.func_70652_k(par1Entity);
            par1Entity.field_70160_al = false;
            par1Entity.field_70159_w = mx;
            par1Entity.field_70181_x = my;
            par1Entity.field_70179_y = mz;
         }

         this.func_184185_a(SoundEvents.field_187743_y, 0.5F, 0.9F + this.field_70170_p.field_73012_v.nextFloat() * 0.2F);
      }

   }

   protected EntityLivingBase findPlayerToAttack() {
      double var1 = 12.0D;
      return this.getIsSummoned() ? null : this.field_70170_p.func_72890_a(this, var1);
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.setIsBatHanging(nbt.func_74767_n("hang"));
      this.setIsDevil(nbt.func_74767_n("devil"));
      this.setIsVampire(nbt.func_74767_n("vamp"));
      this.setIsSummoned(nbt.func_74767_n("summ"));
      this.setIsExplosive(nbt.func_74767_n("expl"));
      this.damBonus = nbt.func_74771_c("damBonus");
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      nbt.func_74757_a("hang", this.getIsBatHanging());
      nbt.func_74757_a("devil", this.getIsDevil());
      nbt.func_74757_a("vamp", this.getIsVampire());
      nbt.func_74757_a("summ", this.getIsSummoned());
      nbt.func_74757_a("expl", this.getIsExplosive());
      nbt.func_74774_a("damBonus", (byte)this.damBonus);
   }

   public boolean func_70601_bi() {
      int i = MathHelper.func_76128_c(this.field_70165_t);
      int j = MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b);
      int k = MathHelper.func_76128_c(this.field_70161_v);
      BlockPos blockpos = new BlockPos(i, j, k);
      int var4 = this.field_70170_p.func_175699_k(blockpos);
      byte var5 = 7;
      return var4 > this.field_70146_Z.nextInt(var5) ? false : super.func_70601_bi();
   }

   protected Item func_146068_u() {
      return !this.getIsSummoned() ? Items.field_151016_H : Item.func_150899_d(0);
   }

   protected boolean func_70814_o() {
      return true;
   }

   static {
      HANGING = EntityDataManager.func_187226_a(EntityFireBat.class, DataSerializers.field_187198_h);
      EXPLOSIVE = EntityDataManager.func_187226_a(EntityFireBat.class, DataSerializers.field_187198_h);
      DEVIL = EntityDataManager.func_187226_a(EntityFireBat.class, DataSerializers.field_187198_h);
      SUMMONED = EntityDataManager.func_187226_a(EntityFireBat.class, DataSerializers.field_187198_h);
      VAMPIRE = EntityDataManager.func_187226_a(EntityFireBat.class, DataSerializers.field_187198_h);
   }
}
