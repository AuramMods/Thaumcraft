package thaumcraft.common.entities.monster.tainted;

import java.util.ArrayList;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
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
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.world.taint.TaintHelper;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.SoundsTC;

public class EntityTaintSwarm extends EntityMob implements ITaintedMob {
   private BlockPos currentFlightTarget;
   private static final DataParameter<Boolean> SUMMONED;
   public int damBonus = 0;
   public ArrayList swarm = new ArrayList();
   private int attackTime;

   public EntityTaintSwarm(World par1World) {
      super(par1World);
      this.func_70105_a(2.0F, 2.0F);
   }

   public boolean func_70686_a(Class clazz) {
      return !ITaintedMob.class.isAssignableFrom(clazz);
   }

   public boolean func_184191_r(Entity otherEntity) {
      return otherEntity instanceof ITaintedMob || super.func_184191_r(otherEntity);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.func_184212_Q().func_187214_a(SUMMONED, false);
   }

   @SideOnly(Side.CLIENT)
   public int func_70070_b(float par1) {
      return 15728880;
   }

   protected boolean func_70692_ba() {
      return true;
   }

   public float func_70013_c(float par1) {
      return 1.0F;
   }

   protected float func_70599_aP() {
      return 0.1F;
   }

   protected SoundEvent func_184639_G() {
      return SoundsTC.swarm;
   }

   protected SoundEvent func_184601_bQ() {
      return SoundsTC.swarmattack;
   }

   protected SoundEvent func_184615_bR() {
      return SoundsTC.swarmattack;
   }

   public boolean func_70104_M() {
      return false;
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(30.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a((double)(2 + this.damBonus));
   }

   public boolean getIsSummoned() {
      return (Boolean)this.func_184212_Q().func_187225_a(SUMMONED);
   }

   public void setIsSummoned(boolean par1) {
      this.func_184212_Q().func_187227_b(SUMMONED, par1);
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      this.field_70181_x *= 0.6000000238418579D;
      if (this.field_70170_p.field_72995_K) {
         for(int a = 0; a < this.swarm.size(); ++a) {
            if (this.swarm.get(a) == null || !((Particle)this.swarm.get(a)).func_187113_k()) {
               this.swarm.remove(a);
               break;
            }
         }

         if (this.swarm.size() < Math.max(FXDispatcher.INSTANCE.particleCount(25), 10)) {
            this.swarm.add(FXDispatcher.INSTANCE.swarmParticleFX(this, 0.22F, 15.0F, 0.08F));
         }
      }

   }

   public void func_70636_d() {
      super.func_70636_d();
      double var1;
      double var3;
      double var5;
      float var7;
      float var8;
      if (this.func_70638_az() == null) {
         if (this.getIsSummoned()) {
            this.func_70097_a(DamageSource.field_76377_j, 5.0F);
         }

         if (this.currentFlightTarget != null && (!this.field_70170_p.func_175623_d(this.currentFlightTarget) || this.currentFlightTarget.func_177956_o() < 1 || this.currentFlightTarget.func_177956_o() > this.field_70170_p.func_175725_q(this.currentFlightTarget).func_177981_b(2).func_177956_o() || !TaintHelper.isNearTaintSeed(this.field_70170_p, this.currentFlightTarget))) {
            this.currentFlightTarget = null;
         }

         if (this.currentFlightTarget == null || this.field_70146_Z.nextInt(30) == 0 || this.func_174831_c(this.currentFlightTarget) < 4.0D) {
            this.currentFlightTarget = new BlockPos((int)this.field_70165_t + this.field_70146_Z.nextInt(7) - this.field_70146_Z.nextInt(7), (int)this.field_70163_u + this.field_70146_Z.nextInt(6) - 2, (int)this.field_70161_v + this.field_70146_Z.nextInt(7) - this.field_70146_Z.nextInt(7));
         }

         var1 = (double)this.currentFlightTarget.func_177958_n() + 0.5D - this.field_70165_t;
         var3 = (double)this.currentFlightTarget.func_177956_o() + 0.1D - this.field_70163_u;
         var5 = (double)this.currentFlightTarget.func_177952_p() + 0.5D - this.field_70161_v;
         this.field_70159_w += (Math.signum(var1) * 0.5D - this.field_70159_w) * 0.015000000014901161D;
         this.field_70181_x += (Math.signum(var3) * 0.699999988079071D - this.field_70181_x) * 0.10000000149011612D;
         this.field_70179_y += (Math.signum(var5) * 0.5D - this.field_70179_y) * 0.015000000014901161D;
         var7 = (float)(Math.atan2(this.field_70179_y, this.field_70159_w) * 180.0D / 3.141592653589793D) - 90.0F;
         var8 = MathHelper.func_76142_g(var7 - this.field_70177_z);
         this.field_70701_bs = 0.1F;
         this.field_70177_z += var8;
      } else if (this.func_70638_az() != null) {
         var1 = this.func_70638_az().field_70165_t - this.field_70165_t;
         var3 = this.func_70638_az().field_70163_u + (double)this.func_70638_az().func_70047_e() - this.field_70163_u;
         var5 = this.func_70638_az().field_70161_v - this.field_70161_v;
         this.field_70159_w += (Math.signum(var1) * 0.5D - this.field_70159_w) * 0.025000000149011613D;
         this.field_70181_x += (Math.signum(var3) * 0.699999988079071D - this.field_70181_x) * 0.10000000149011612D;
         this.field_70179_y += (Math.signum(var5) * 0.5D - this.field_70179_y) * 0.02500000001490116D;
         var7 = (float)(Math.atan2(this.field_70179_y, this.field_70159_w) * 180.0D / 3.141592653589793D) - 90.0F;
         var8 = MathHelper.func_76142_g(var7 - this.field_70177_z);
         this.field_70701_bs = 0.1F;
         this.field_70177_z += var8;
      }

      if (this.func_70638_az() == null) {
         this.func_70624_b((EntityLivingBase)this.findPlayerToAttack());
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

   protected void func_70619_bc() {
      super.func_70619_bc();
   }

   protected boolean func_70041_e_() {
      return false;
   }

   public void func_180430_e(float distance, float damageMultiplier) {
   }

   public boolean func_145773_az() {
      return true;
   }

   public boolean func_70097_a(DamageSource par1DamageSource, float par2) {
      return this.func_180431_b(par1DamageSource) ? false : super.func_70097_a(par1DamageSource, par2);
   }

   protected void attackEntity(Entity par1Entity, float par2) {
      if (this.attackTime <= 0 && par2 < 3.0F && par1Entity.func_174813_aQ().field_72337_e > this.func_174813_aQ().field_72338_b && par1Entity.func_174813_aQ().field_72338_b < this.func_174813_aQ().field_72337_e) {
         if (this.getIsSummoned()) {
            ((EntityLivingBase)par1Entity).field_70718_bc = 100;
         }

         this.attackTime = 10 + this.field_70146_Z.nextInt(5);
         double mx = par1Entity.field_70159_w;
         double my = par1Entity.field_70181_x;
         double mz = par1Entity.field_70179_y;
         if (this.func_70652_k(par1Entity) && !this.field_70170_p.field_72995_K && par1Entity instanceof EntityLivingBase) {
            ((EntityLivingBase)par1Entity).func_70690_d(new PotionEffect(MobEffects.field_76437_t, 100, 0));
         }

         par1Entity.field_70160_al = false;
         par1Entity.field_70159_w = mx;
         par1Entity.field_70181_x = my;
         par1Entity.field_70179_y = mz;
         this.func_184185_a(SoundsTC.swarmattack, 0.3F, 0.9F + this.field_70170_p.field_73012_v.nextFloat() * 0.2F);
      }

   }

   protected Entity findPlayerToAttack() {
      double var1 = 8.0D;
      return this.getIsSummoned() ? null : this.field_70170_p.func_72890_a(this, var1);
   }

   public void func_70037_a(NBTTagCompound par1NBTTagCompound) {
      super.func_70037_a(par1NBTTagCompound);
      this.setIsSummoned(par1NBTTagCompound.func_74767_n("summoned"));
      this.damBonus = par1NBTTagCompound.func_74771_c("damBonus");
   }

   public void func_70014_b(NBTTagCompound par1NBTTagCompound) {
      super.func_70014_b(par1NBTTagCompound);
      par1NBTTagCompound.func_74757_a("summoned", this.getIsSummoned());
      par1NBTTagCompound.func_74774_a("damBonus", (byte)this.damBonus);
   }

   public boolean func_70601_bi() {
      int var1 = MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b);
      int var2 = MathHelper.func_76128_c(this.field_70165_t);
      int var3 = MathHelper.func_76128_c(this.field_70161_v);
      int var4 = this.field_70170_p.func_175699_k(new BlockPos(var2, var1, var3));
      byte var5 = 7;
      return var4 > this.field_70146_Z.nextInt(var5) ? false : super.func_70601_bi();
   }

   protected boolean func_70814_o() {
      return true;
   }

   protected Item func_146068_u() {
      return Item.func_150899_d(0);
   }

   protected void func_70628_a(boolean flag, int i) {
      if (this.field_70170_p.field_73012_v.nextBoolean()) {
         this.func_70099_a(ConfigItems.FLUX_CRYSTAL.func_77946_l(), this.field_70131_O / 2.0F);
      }

   }

   static {
      SUMMONED = EntityDataManager.func_187226_a(EntityTaintSwarm.class, DataSerializers.field_187198_h);
   }
}
