package thaumcraft.common.entities.monster.boss;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.world.BlockLoot;
import thaumcraft.common.entities.ai.combat.AILongRangeAttack;
import thaumcraft.common.entities.monster.mods.ChampionModifier;
import thaumcraft.common.entities.projectile.EntityGolemOrb;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.EntityUtils;

public class EntityEldritchGolem extends EntityThaumcraftBoss implements IEldritchMob, IRangedAttackMob {
   private static final DataParameter<Boolean> HEADLESS;
   int beamCharge = 0;
   boolean chargingBeam = false;
   int arcing = 0;
   int ax = 0;
   int ay = 0;
   int az = 0;
   private int attackTimer;

   public EntityEldritchGolem(World p_i1745_1_) {
      super(p_i1745_1_);
      this.func_70105_a(1.75F, 3.5F);
      this.field_70178_ae = true;
   }

   protected void func_184651_r() {
      this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
      this.field_70714_bg.func_75776_a(3, new EntityAIAttackMelee(this, 1.1D, false));
      this.field_70714_bg.func_75776_a(6, new EntityAIMoveTowardsRestriction(this, 0.8D));
      this.field_70714_bg.func_75776_a(7, new EntityAIWander(this, 0.8D));
      this.field_70714_bg.func_75776_a(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.field_70714_bg.func_75776_a(8, new EntityAILookIdle(this));
      this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, true, new Class[0]));
      this.field_70715_bh.func_75776_a(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
   }

   public void generateName() {
      int t = (int)this.func_110148_a(EntityUtils.CHAMPION_MOD).func_111126_e();
      if (t >= 0) {
         this.func_96094_a(String.format(I18n.func_74838_a("entity.Thaumcraft.EldritchGolem.name.custom"), ChampionModifier.mods[t].getModNameLocalized()));
      }

   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.func_184212_Q().func_187214_a(HEADLESS, false);
   }

   public boolean isHeadless() {
      return (Boolean)this.func_184212_Q().func_187225_a(HEADLESS);
   }

   public void setHeadless(boolean par1) {
      this.func_184212_Q().func_187227_b(HEADLESS, par1);
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      nbt.func_74757_a("headless", this.isHeadless());
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.setHeadless(nbt.func_74767_n("headless"));
      if (this.isHeadless()) {
         this.makeHeadless();
      }

   }

   public float func_70047_e() {
      return this.isHeadless() ? 3.33F : 3.0F;
   }

   public int func_70658_aO() {
      return super.func_70658_aO() + 6;
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.3D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(10.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(400.0D);
   }

   protected SoundEvent func_184601_bQ() {
      return SoundEvents.field_187602_cF;
   }

   protected SoundEvent func_184615_bR() {
      return SoundEvents.field_187599_cE;
   }

   protected void func_180429_a(BlockPos p_180429_1_, Block p_180429_2_) {
      this.func_184185_a(SoundEvents.field_187605_cG, 1.0F, 1.0F);
   }

   public IEntityLivingData func_180482_a(DifficultyInstance diff, IEntityLivingData data) {
      this.spawnTimer = 100;
      return super.func_180482_a(diff, data);
   }

   public void func_70636_d() {
      super.func_70636_d();
      if (this.attackTimer > 0) {
         --this.attackTimer;
      }

      IBlockState bs;
      if (this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y > 2.500000277905201E-7D && this.field_70146_Z.nextInt(5) == 0) {
         bs = this.field_70170_p.func_180495_p(this.func_180425_c());
         if (bs.func_185904_a() != Material.field_151579_a) {
            this.field_70170_p.func_175688_a(EnumParticleTypes.BLOCK_CRACK, this.field_70165_t + ((double)this.field_70146_Z.nextFloat() - 0.5D) * (double)this.field_70130_N, this.func_174813_aQ().field_72338_b + 0.1D, this.field_70161_v + ((double)this.field_70146_Z.nextFloat() - 0.5D) * (double)this.field_70130_N, 4.0D * ((double)this.field_70146_Z.nextFloat() - 0.5D), 0.5D, ((double)this.field_70146_Z.nextFloat() - 0.5D) * 4.0D, new int[]{Block.func_176210_f(bs)});
         }

         if (!this.field_70170_p.field_72995_K && bs.func_177230_c() instanceof BlockLoot) {
            this.field_70170_p.func_175655_b(this.func_180425_c(), true);
         }
      }

      if (!this.field_70170_p.field_72995_K) {
         bs = this.field_70170_p.func_180495_p(this.func_180425_c());
         float h = bs.func_185887_b(this.field_70170_p, this.func_180425_c());
         if (h >= 0.0F && h <= 0.15F) {
            this.field_70170_p.func_175655_b(this.func_180425_c(), true);
         }
      }

   }

   public boolean func_70097_a(DamageSource source, float damage) {
      if (!this.field_70170_p.field_72995_K && damage > this.func_110143_aJ() && !this.isHeadless()) {
         this.setHeadless(true);
         this.spawnTimer = 100;
         double xx = (double)(MathHelper.func_76134_b(this.field_70177_z % 360.0F / 180.0F * 3.1415927F) * 0.75F);
         double zz = (double)(MathHelper.func_76126_a(this.field_70177_z % 360.0F / 180.0F * 3.1415927F) * 0.75F);
         this.field_70170_p.func_72876_a(this, this.field_70165_t + xx, this.field_70163_u + (double)this.func_70047_e(), this.field_70161_v + zz, 2.0F, false);
         this.makeHeadless();
         return false;
      } else {
         return super.func_70097_a(source, damage);
      }
   }

   void makeHeadless() {
      this.field_70714_bg.func_75776_a(2, new AILongRangeAttack(this, 3.0D, 1.0D, 5, 5, 24.0F));
   }

   public boolean func_70652_k(Entity target) {
      if (this.attackTimer > 0) {
         return false;
      } else {
         this.attackTimer = 10;
         this.field_70170_p.func_72960_a(this, (byte)4);
         boolean flag = target.func_70097_a(DamageSource.func_76358_a(this), (float)this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e() * 0.75F);
         if (flag) {
            target.field_70181_x += 0.2000000059604645D;
            if (this.isHeadless()) {
               target.func_70024_g((double)(-MathHelper.func_76126_a(this.field_70177_z * 3.1415927F / 180.0F) * 1.5F), 0.1D, (double)(MathHelper.func_76134_b(this.field_70177_z * 3.1415927F / 180.0F) * 1.5F));
            }
         }

         return flag;
      }
   }

   public void func_82196_d(EntityLivingBase entitylivingbase, float f) {
      if (this.func_70685_l(entitylivingbase) && !this.chargingBeam && this.beamCharge > 0) {
         this.beamCharge -= 15 + this.field_70146_Z.nextInt(5);
         this.func_70671_ap().func_75650_a(entitylivingbase.field_70165_t, entitylivingbase.func_174813_aQ().field_72338_b + (double)(entitylivingbase.field_70131_O / 2.0F), entitylivingbase.field_70161_v, 30.0F, 30.0F);
         Vec3d v = this.func_70676_i(1.0F);
         EntityGolemOrb blast = new EntityGolemOrb(this.field_70170_p, this, entitylivingbase, false);
         blast.field_70165_t += v.field_72450_a;
         blast.field_70161_v += v.field_72449_c;
         blast.func_70107_b(blast.field_70165_t, blast.field_70163_u, blast.field_70161_v);
         double d0 = entitylivingbase.field_70165_t + entitylivingbase.field_70159_w - this.field_70165_t;
         double d1 = entitylivingbase.field_70163_u - this.field_70163_u - (double)(entitylivingbase.field_70131_O / 2.0F);
         double d2 = entitylivingbase.field_70161_v + entitylivingbase.field_70179_y - this.field_70161_v;
         blast.func_70186_c(d0, d1, d2, 0.66F, 5.0F);
         this.func_184185_a(SoundsTC.egattack, 1.0F, 1.0F + this.field_70146_Z.nextFloat() * 0.1F);
         this.field_70170_p.func_72838_d(blast);
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_70103_a(byte p_70103_1_) {
      if (p_70103_1_ == 4) {
         this.attackTimer = 10;
         this.func_184185_a(SoundEvents.field_187596_cD, 1.0F, 1.0F);
      } else if (p_70103_1_ == 18) {
         this.spawnTimer = 150;
      } else if (p_70103_1_ == 19) {
         if (this.arcing == 0) {
            float radius = 2.0F + this.field_70146_Z.nextFloat() * 2.0F;
            double radians = Math.toRadians((double)this.field_70146_Z.nextInt(360));
            double deltaX = (double)radius * Math.cos(radians);
            double deltaZ = (double)radius * Math.sin(radians);
            int bx = MathHelper.func_76128_c(this.field_70165_t + deltaX);
            int by = MathHelper.func_76128_c(this.field_70163_u);
            int bz = MathHelper.func_76128_c(this.field_70161_v + deltaZ);
            BlockPos bp = new BlockPos(bx, by, bz);

            for(int c = 0; c < 5 && this.field_70170_p.func_175623_d(bp); --by) {
               ++c;
            }

            if (this.field_70170_p.func_175623_d(bp.func_177984_a()) && !this.field_70170_p.func_175623_d(bp)) {
               this.ax = bx;
               this.ay = by;
               this.az = bz;
               this.arcing = 8 + this.field_70146_Z.nextInt(5);
               this.func_184185_a(SoundsTC.jacobs, 0.8F, 1.0F + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.05F);
            }
         }
      } else {
         super.func_70103_a(p_70103_1_);
      }

   }

   public void func_70071_h_() {
      if (this.getSpawnTimer() == 150) {
         this.field_70170_p.func_72960_a(this, (byte)18);
      }

      if (this.getSpawnTimer() > 0) {
         this.func_70691_i(2.0F);
      }

      super.func_70071_h_();
      if (this.field_70170_p.field_72995_K) {
         if (this.isHeadless()) {
            this.field_70125_A = 0.0F;
            float f1 = MathHelper.func_76134_b(-this.field_70761_aq * 0.017453292F - 3.1415927F);
            float f2 = MathHelper.func_76126_a(-this.field_70761_aq * 0.017453292F - 3.1415927F);
            float f3 = -MathHelper.func_76134_b(-this.field_70125_A * 0.017453292F);
            float f4 = MathHelper.func_76126_a(-this.field_70125_A * 0.017453292F);
            Vec3d v = new Vec3d((double)(f2 * f3), (double)f4, (double)(f1 * f3));
            if (this.field_70146_Z.nextInt(20) == 0) {
               float a = (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) / 2.0F;
               float b = (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) / 2.0F;
               FXDispatcher.INSTANCE.spark((double)((float)(this.field_70165_t + v.field_72450_a + (double)a)), (double)((float)this.field_70163_u + this.func_70047_e() - 0.25F), (double)((float)(this.field_70161_v + v.field_72449_c + (double)b)), 0.3F, 0.65F + this.field_70146_Z.nextFloat() * 0.1F, 1.0F, 1.0F, 0.8F);
            }

            FXDispatcher.INSTANCE.drawVentParticles((double)((float)this.field_70165_t) + v.field_72450_a * 0.66D, (double)((float)this.field_70163_u + this.func_70047_e() - 0.75F), (double)((float)this.field_70161_v) + v.field_72449_c * 0.66D, 0.0D, 0.001D, 0.0D, 5592405, 4.0F);
            if (this.arcing > 0) {
               FXDispatcher.INSTANCE.arcLightning(this.field_70165_t, this.field_70163_u + (double)(this.field_70131_O / 2.0F), this.field_70161_v, (double)this.ax + 0.5D, (double)(this.ay + 1), (double)this.az + 0.5D, 0.65F + this.field_70146_Z.nextFloat() * 0.1F, 1.0F, 1.0F, 1.0F - (float)this.arcing / 10.0F);
               --this.arcing;
            }
         }
      } else {
         if (this.isHeadless() && this.beamCharge <= 0) {
            this.chargingBeam = true;
         }

         if (this.isHeadless() && this.chargingBeam) {
            ++this.beamCharge;
            this.field_70170_p.func_72960_a(this, (byte)19);
            if (this.beamCharge == 150) {
               this.chargingBeam = false;
            }
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public int getAttackTimer() {
      return this.attackTimer;
   }

   static {
      HEADLESS = EntityDataManager.func_187226_a(EntityEldritchGolem.class, DataSerializers.field_187198_h);
   }
}
