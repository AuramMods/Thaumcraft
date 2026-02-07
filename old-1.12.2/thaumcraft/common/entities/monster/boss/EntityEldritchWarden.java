package thaumcraft.common.entities.monster.boss;

import net.minecraft.block.state.IBlockState;
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
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.entities.ai.combat.AILongRangeAttack;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.cult.EntityCultist;
import thaumcraft.common.entities.monster.mods.ChampionModifier;
import thaumcraft.common.entities.projectile.EntityEldritchOrb;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockArc;
import thaumcraft.common.lib.network.fx.PacketFXSonic;
import thaumcraft.common.lib.utils.EntityUtils;

public class EntityEldritchWarden extends EntityThaumcraftBoss implements IRangedAttackMob, IEldritchMob {
   String[] titles = new String[]{"Aphoom-Zhah", "Basatan", "Chaugnar Faugn", "Mnomquah", "Nyogtha", "Oorn", "Shaikorth", "Rhan-Tegoth", "Rhogog", "Shudde M'ell", "Vulthoom", "Yag-Kosha", "Yibb-Tstll", "Zathog", "Zushakon"};
   private static final DataParameter<Byte> NAME;
   boolean fieldFrenzy = false;
   int fieldFrenzyCounter = 0;
   boolean lastBlast = false;
   public float armLiftL = 0.0F;
   public float armLiftR = 0.0F;

   public EntityEldritchWarden(World p_i1745_1_) {
      super(p_i1745_1_);
      this.func_70105_a(1.5F, 3.5F);
   }

   protected void func_184651_r() {
      this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
      this.field_70714_bg.func_75776_a(2, new AILongRangeAttack(this, 3.0D, 1.0D, 20, 40, 24.0F));
      this.field_70714_bg.func_75776_a(3, new EntityAIAttackMelee(this, 1.1D, false));
      this.field_70714_bg.func_75776_a(5, new EntityAIMoveTowardsRestriction(this, 0.8D));
      this.field_70714_bg.func_75776_a(7, new EntityAIWander(this, 1.0D));
      this.field_70714_bg.func_75776_a(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.field_70714_bg.func_75776_a(8, new EntityAILookIdle(this));
      this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, false, new Class[0]));
      this.field_70715_bh.func_75776_a(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
      this.field_70715_bh.func_75776_a(3, new EntityAINearestAttackableTarget(this, EntityCultist.class, true));
   }

   public void generateName() {
      int t = (int)this.func_110148_a(EntityUtils.CHAMPION_MOD).func_111126_e();
      if (t >= 0) {
         this.func_96094_a(String.format(I18n.func_74838_a("entity.Thaumcraft.EldritchWarden.name.custom"), this.getTitle(), ChampionModifier.mods[t].getModNameLocalized()));
      }

   }

   private String getTitle() {
      return this.titles[(Byte)this.func_184212_Q().func_187225_a(NAME)];
   }

   private void setTitle(int title) {
      this.func_184212_Q().func_187227_b(NAME, (byte)title);
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(400.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.33D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(10.0D);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.func_184212_Q().func_187214_a(NAME, (byte)0);
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      nbt.func_74774_a("title", (Byte)this.func_184212_Q().func_187225_a(NAME));
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.setTitle(nbt.func_74771_c("title"));
   }

   public int func_70658_aO() {
      return super.func_70658_aO() + 4;
   }

   protected void func_70619_bc() {
      if (this.fieldFrenzyCounter == 0) {
         super.func_70619_bc();
      }

      if (this.field_70172_ad <= 0 && this.field_70173_aa % 25 == 0) {
         int bh = (int)(this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111125_b() * 0.66D);
         if (this.func_110139_bj() < (float)bh) {
            this.func_110149_m(this.func_110139_bj() + 1.0F);
         }
      }

   }

   public void func_70071_h_() {
      if (this.getSpawnTimer() == 150) {
         this.field_70170_p.func_72960_a(this, (byte)18);
      }

      super.func_70071_h_();
      if (this.field_70170_p.field_72995_K) {
         if (this.armLiftL > 0.0F) {
            this.armLiftL -= 0.05F;
         }

         if (this.armLiftR > 0.0F) {
            this.armLiftR -= 0.05F;
         }

         float x = (float)(this.field_70165_t + (double)((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F));
         float z = (float)(this.field_70161_v + (double)((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F));
         FXDispatcher.INSTANCE.wispFXEG((double)x, (double)((float)(this.field_70163_u + 0.25D * (double)this.field_70131_O)), (double)z, this);
         if (this.spawnTimer > 0) {
            float he = Math.max(1.0F, this.field_70131_O * ((float)(150 - this.spawnTimer) / 150.0F));

            for(int a = 0; a < 33; ++a) {
               FXDispatcher.INSTANCE.smokeSpiral(this.field_70165_t, this.func_174813_aQ().field_72338_b + (double)(he / 2.0F), this.field_70161_v, he, this.field_70146_Z.nextInt(360), MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b) - 1, 2232623);
            }
         }
      }

   }

   public void func_70636_d() {
      super.func_70636_d();
      int i = MathHelper.func_76128_c(this.field_70165_t);
      int j = MathHelper.func_76128_c(this.field_70163_u);
      int k = MathHelper.func_76128_c(this.field_70161_v);

      for(int l = 0; l < 4; ++l) {
         i = MathHelper.func_76128_c(this.field_70165_t + (double)((float)(l % 2 * 2 - 1) * 0.25F));
         j = MathHelper.func_76128_c(this.field_70163_u);
         k = MathHelper.func_76128_c(this.field_70161_v + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25F));
         BlockPos bp = new BlockPos(i, j, k);
         if (this.field_70170_p.func_175623_d(bp) && BlocksTC.effect != null) {
            this.field_70170_p.func_175656_a(bp, BlocksTC.effect.func_176203_a(1));
         }
      }

      if (!this.field_70170_p.field_72995_K && this.fieldFrenzyCounter > 0) {
         if (this.fieldFrenzyCounter == 150) {
            this.teleportHome();
         }

         this.performFieldFrenzy();
      }

   }

   private void performFieldFrenzy() {
      if (this.fieldFrenzyCounter < 121 && this.fieldFrenzyCounter % 10 == 0) {
         this.field_70170_p.func_72960_a(this, (byte)17);
         double radius = (double)(150 - this.fieldFrenzyCounter) / 8.0D;
         int d = 1 + this.fieldFrenzyCounter / 8;
         int i = MathHelper.func_76128_c(this.field_70165_t);
         int j = MathHelper.func_76128_c(this.field_70163_u);
         int k = MathHelper.func_76128_c(this.field_70161_v);

         for(int q = 0; q < 180 / d; ++q) {
            double radians = Math.toRadians((double)(q * 2 * d));
            int deltaX = (int)(radius * Math.cos(radians));
            int deltaZ = (int)(radius * Math.sin(radians));
            BlockPos bp = new BlockPos(i + deltaX, j, k + deltaZ);
            if (this.field_70170_p.func_175623_d(bp) && this.field_70170_p.func_175677_d(bp.func_177977_b(), false)) {
               this.field_70170_p.func_175656_a(bp, BlocksTC.effect.func_176203_a(1));
               this.field_70170_p.func_175684_a(bp, BlocksTC.effect, 250 + this.field_70146_Z.nextInt(150));
               if (this.field_70146_Z.nextFloat() < 0.3F) {
                  PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockArc(bp, this, 0.5F + this.field_70146_Z.nextFloat() * 0.2F, 0.0F, 0.5F + this.field_70146_Z.nextFloat() * 0.2F), new TargetPoint(this.field_70170_p.field_73011_w.getDimension(), this.field_70165_t, this.field_70163_u, this.field_70161_v, 32.0D));
               }
            }
         }

         this.func_184185_a(SoundsTC.zap, 1.0F, 0.9F + this.field_70146_Z.nextFloat() * 0.1F);
      }

      --this.fieldFrenzyCounter;
   }

   protected void teleportHome() {
      EnderTeleportEvent event = new EnderTeleportEvent(this, (double)this.func_180486_cf().func_177958_n(), (double)this.func_180486_cf().func_177956_o(), (double)this.func_180486_cf().func_177952_p(), 0.0F);
      if (!MinecraftForge.EVENT_BUS.post(event)) {
         double d3 = this.field_70165_t;
         double d4 = this.field_70163_u;
         double d5 = this.field_70161_v;
         this.field_70165_t = event.getTargetX();
         this.field_70163_u = event.getTargetY();
         this.field_70161_v = event.getTargetZ();
         boolean flag = false;
         int i = MathHelper.func_76128_c(this.field_70165_t);
         int j = MathHelper.func_76128_c(this.field_70163_u);
         int k = MathHelper.func_76128_c(this.field_70161_v);
         BlockPos bp = new BlockPos(i, j, k);
         int l;
         if (this.field_70170_p.func_175667_e(bp)) {
            bp = new BlockPos(i, j, k);
            boolean flag1 = false;
            l = 20;

            while(true) {
               while(!flag1 && l > 0) {
                  IBlockState block = this.field_70170_p.func_180495_p(bp.func_177977_b());
                  IBlockState block2 = this.field_70170_p.func_180495_p(bp);
                  if (block.func_185904_a().func_76230_c() && !block2.func_185904_a().func_76230_c()) {
                     flag1 = true;
                  } else {
                     i = MathHelper.func_76128_c(this.field_70165_t) + this.field_70146_Z.nextInt(8) - this.field_70146_Z.nextInt(8);
                     k = MathHelper.func_76128_c(this.field_70161_v) + this.field_70146_Z.nextInt(8) - this.field_70146_Z.nextInt(8);
                     --l;
                  }
               }

               if (flag1) {
                  this.func_70107_b((double)i + 0.5D, (double)j + 0.1D, (double)k + 0.5D);
                  if (this.field_70170_p.func_147461_a(this.func_174813_aQ()).isEmpty()) {
                     flag = true;
                  }
               }
               break;
            }
         }

         if (!flag) {
            this.func_70107_b(d3, d4, d5);
         } else {
            short short1 = 128;

            for(l = 0; l < short1; ++l) {
               double d6 = (double)l / ((double)short1 - 1.0D);
               float f = (this.field_70146_Z.nextFloat() - 0.5F) * 0.2F;
               float f1 = (this.field_70146_Z.nextFloat() - 0.5F) * 0.2F;
               float f2 = (this.field_70146_Z.nextFloat() - 0.5F) * 0.2F;
               double d7 = d3 + (this.field_70165_t - d3) * d6 + (this.field_70146_Z.nextDouble() - 0.5D) * (double)this.field_70130_N * 2.0D;
               double d8 = d4 + (this.field_70163_u - d4) * d6 + this.field_70146_Z.nextDouble() * (double)this.field_70131_O;
               double d9 = d5 + (this.field_70161_v - d5) * d6 + (this.field_70146_Z.nextDouble() - 0.5D) * (double)this.field_70130_N * 2.0D;
               this.field_70170_p.func_175688_a(EnumParticleTypes.PORTAL, d7, d8, d9, (double)f, (double)f1, (double)f2, new int[0]);
            }

            this.func_184185_a(SoundEvents.field_187534_aX, 1.0F, 1.0F);
         }
      }
   }

   public boolean func_180431_b(DamageSource ds) {
      return this.fieldFrenzyCounter > 0 || super.func_180431_b(ds);
   }

   public boolean func_70097_a(DamageSource source, float damage) {
      if (!this.func_180431_b(source) && source != DamageSource.field_76369_e && source != DamageSource.field_82727_n) {
         boolean aef = super.func_70097_a(source, damage);
         if (!this.field_70170_p.field_72995_K && aef && !this.fieldFrenzy && this.func_110139_bj() <= 0.0F) {
            this.fieldFrenzy = true;
            this.fieldFrenzyCounter = 150;
         }

         return aef;
      } else {
         return false;
      }
   }

   public IEntityLivingData func_180482_a(DifficultyInstance diff, IEntityLivingData data) {
      this.spawnTimer = 150;
      this.setTitle(this.field_70146_Z.nextInt(this.titles.length));
      this.func_110149_m((float)((double)this.func_110139_bj() + this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111125_b() * 0.66D));
      return super.func_180482_a(diff, data);
   }

   public float func_70047_e() {
      return 3.1F;
   }

   public void func_82196_d(EntityLivingBase entitylivingbase, float f) {
      if (this.field_70146_Z.nextFloat() > 0.2F) {
         EntityEldritchOrb blast = new EntityEldritchOrb(this.field_70170_p, this);
         this.lastBlast = !this.lastBlast;
         this.field_70170_p.func_72960_a(this, (byte)(this.lastBlast ? 16 : 15));
         int rr = this.lastBlast ? 90 : 180;
         double xx = (double)(MathHelper.func_76134_b((this.field_70177_z + (float)rr) % 360.0F / 180.0F * 3.1415927F) * 0.5F);
         double yy = 0.13D;
         double zz = (double)(MathHelper.func_76126_a((this.field_70177_z + (float)rr) % 360.0F / 180.0F * 3.1415927F) * 0.5F);
         blast.func_70107_b(blast.field_70165_t - xx, blast.field_70163_u - yy, blast.field_70161_v - zz);
         double d0 = entitylivingbase.field_70165_t + entitylivingbase.field_70159_w - this.field_70165_t;
         double d1 = entitylivingbase.field_70163_u - this.field_70163_u - (double)(entitylivingbase.field_70131_O / 2.0F);
         double d2 = entitylivingbase.field_70161_v + entitylivingbase.field_70179_y - this.field_70161_v;
         blast.func_70186_c(d0, d1, d2, 1.0F, 2.0F);
         this.func_184185_a(SoundsTC.egattack, 2.0F, 1.0F + this.field_70146_Z.nextFloat() * 0.1F);
         this.field_70170_p.func_72838_d(blast);
      } else if (this.func_70685_l(entitylivingbase)) {
         PacketHandler.INSTANCE.sendToAllAround(new PacketFXSonic(this.func_145782_y()), new TargetPoint(this.field_70170_p.field_73011_w.getDimension(), this.field_70165_t, this.field_70163_u, this.field_70161_v, 32.0D));
         entitylivingbase.func_70024_g((double)(-MathHelper.func_76126_a(this.field_70177_z * 3.1415927F / 180.0F) * 1.5F), 0.1D, (double)(MathHelper.func_76134_b(this.field_70177_z * 3.1415927F / 180.0F) * 1.5F));

         try {
            entitylivingbase.func_70690_d(new PotionEffect(MobEffects.field_82731_v, 400, 0));
            entitylivingbase.func_70690_d(new PotionEffect(MobEffects.field_76437_t, 400, 0));
         } catch (Exception var17) {
         }

         if (entitylivingbase instanceof EntityPlayer) {
            ThaumcraftApi.internalMethods.addWarpToPlayer((EntityPlayer)entitylivingbase, 3 + this.field_70170_p.field_73012_v.nextInt(3), IPlayerWarp.EnumWarpType.TEMPORARY);
         }

         this.func_184185_a(SoundsTC.egscreech, 4.0F, 1.0F + this.field_70146_Z.nextFloat() * 0.1F);
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_70103_a(byte p_70103_1_) {
      if (p_70103_1_ == 15) {
         this.armLiftL = 0.5F;
      } else if (p_70103_1_ == 16) {
         this.armLiftR = 0.5F;
      } else if (p_70103_1_ == 17) {
         this.armLiftL = 0.9F;
         this.armLiftR = 0.9F;
      } else if (p_70103_1_ == 18) {
         this.spawnTimer = 150;
      } else {
         super.func_70103_a(p_70103_1_);
      }

   }

   public boolean func_70686_a(Class clazz) {
      return clazz == EntityEldritchGuardian.class ? false : super.func_70686_a(clazz);
   }

   protected SoundEvent func_184639_G() {
      return SoundsTC.egidle;
   }

   protected SoundEvent func_184615_bR() {
      return SoundsTC.egdeath;
   }

   public int func_70627_aG() {
      return 500;
   }

   static {
      NAME = EntityDataManager.func_187226_a(EntityEldritchWarden.class, DataSerializers.field_187191_a);
   }
}
