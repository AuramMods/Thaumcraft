package thaumcraft.common.entities.monster;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
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
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.ai.combat.AILongRangeAttack;
import thaumcraft.common.entities.monster.cult.EntityCultist;
import thaumcraft.common.entities.projectile.EntityEldritchOrb;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXSonic;
import thaumcraft.common.lib.network.misc.PacketMiscEvent;

public class EntityEldritchGuardian extends EntityMob implements IRangedAttackMob, IEldritchMob {
   public float armLiftL = 0.0F;
   public float armLiftR = 0.0F;
   boolean lastBlast = false;

   public EntityEldritchGuardian(World p_i1745_1_) {
      super(p_i1745_1_);
      ((PathNavigateGround)this.func_70661_as()).func_179688_b(true);
      this.func_70105_a(0.8F, 2.25F);
      this.field_70728_aV = 20;
   }

   protected void func_184651_r() {
      this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
      this.field_70714_bg.func_75776_a(2, new AILongRangeAttack(this, 8.0D, 1.0D, 20, 40, 24.0F));
      this.field_70714_bg.func_75776_a(3, new EntityAIAttackMelee(this, 1.0D, false));
      this.field_70714_bg.func_75776_a(5, new EntityAIMoveTowardsRestriction(this, 0.8D));
      this.field_70714_bg.func_75776_a(7, new EntityAIWander(this, 1.0D));
      this.field_70714_bg.func_75776_a(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.field_70714_bg.func_75776_a(8, new EntityAILookIdle(this));
      this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, false, new Class[0]));
      this.field_70715_bh.func_75776_a(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
      this.field_70715_bh.func_75776_a(3, new EntityAINearestAttackableTarget(this, EntityCultist.class, true));
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(50.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(40.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.28D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(7.0D);
   }

   protected void func_70088_a() {
      super.func_70088_a();
   }

   public int func_70658_aO() {
      return 4;
   }

   public boolean func_98052_bS() {
      return false;
   }

   public boolean func_70097_a(DamageSource source, float damage) {
      if (source.func_82725_o()) {
         damage /= 2.0F;
      }

      return super.func_70097_a(source, damage);
   }

   public void func_70071_h_() {
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
         FXDispatcher.INSTANCE.wispFXEG((double)x, (double)((float)(this.field_70163_u + 0.22D * (double)this.field_70131_O)), (double)z, this);
      } else if (this.field_70170_p.field_73011_w.getDimension() != Config.dimensionOuterId && (this.field_70173_aa == 0 || this.field_70173_aa % 100 == 0) && this.field_70170_p.func_175659_aa() != EnumDifficulty.EASY) {
         double d6 = this.field_70170_p.func_175659_aa() == EnumDifficulty.HARD ? 576.0D : 256.0D;

         for(int i = 0; i < this.field_70170_p.field_73010_i.size(); ++i) {
            EntityPlayer entityplayer1 = (EntityPlayer)this.field_70170_p.field_73010_i.get(i);
            if (entityplayer1.func_70089_S()) {
               double d5 = entityplayer1.func_70092_e(this.field_70165_t, this.field_70163_u, this.field_70161_v);
               if (d5 < d6) {
                  PacketHandler.INSTANCE.sendTo(new PacketMiscEvent((byte)2), (EntityPlayerMP)entityplayer1);
               }
            }
         }
      }

   }

   public boolean func_70652_k(Entity p_70652_1_) {
      boolean flag = super.func_70652_k(p_70652_1_);
      if (flag) {
         int i = this.field_70170_p.func_175659_aa().func_151525_a();
         if (this.func_184614_ca() == null && this.func_70027_ad() && this.field_70146_Z.nextFloat() < (float)i * 0.3F) {
            p_70652_1_.func_70015_d(2 * i);
         }
      }

      return flag;
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

   protected Item func_146068_u() {
      return Item.func_150899_d(0);
   }

   protected void func_70628_a(boolean flag, int i) {
      super.func_70628_a(flag, i);
   }

   public EnumCreatureAttribute func_70668_bt() {
      return EnumCreatureAttribute.UNDEAD;
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      if (this.func_180486_cf() != null && this.func_110174_bM() > 0.0F) {
         nbt.func_74768_a("HomeD", (int)this.func_110174_bM());
         nbt.func_74768_a("HomeX", this.func_180486_cf().func_177958_n());
         nbt.func_74768_a("HomeY", this.func_180486_cf().func_177956_o());
         nbt.func_74768_a("HomeZ", this.func_180486_cf().func_177952_p());
      }

   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      if (nbt.func_74764_b("HomeD")) {
         this.func_175449_a(new BlockPos(nbt.func_74762_e("HomeX"), nbt.func_74762_e("HomeY"), nbt.func_74762_e("HomeZ")), nbt.func_74762_e("HomeD"));
      }

   }

   public IEntityLivingData func_180482_a(DifficultyInstance diff, IEntityLivingData data) {
      IEntityLivingData dd = super.func_180482_a(diff, data);
      float f = diff.func_180170_c();
      if (this.field_70170_p.field_73011_w.getDimension() == Config.dimensionOuterId) {
         int bh = (int)this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111125_b() / 2;
         this.func_110149_m(this.func_110139_bj() + (float)bh);
      }

      return dd;
   }

   protected void func_70619_bc() {
      super.func_70619_bc();
      if (this.field_70170_p.field_73011_w.getDimension() == Config.dimensionOuterId && this.field_70172_ad <= 0 && this.field_70173_aa % 25 == 0) {
         int bh = (int)this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111125_b() / 2;
         if (this.func_110139_bj() < (float)bh) {
            this.func_110149_m(this.func_110139_bj() + 1.0F);
         }
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
      } else {
         super.func_70103_a(p_70103_1_);
      }

   }

   protected boolean func_70692_ba() {
      return !this.func_110175_bO();
   }

   public float func_70047_e() {
      return 2.1F;
   }

   public boolean func_70601_bi() {
      List ents = this.field_70170_p.func_72872_a(EntityEldritchGuardian.class, (new AxisAlignedBB(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70165_t + 1.0D, this.field_70163_u + 1.0D, this.field_70161_v + 1.0D)).func_72314_b(32.0D, 16.0D, 32.0D));
      return ents.size() > 0 ? false : super.func_70601_bi();
   }

   protected boolean func_70814_o() {
      return true;
   }

   protected float func_70599_aP() {
      return 1.5F;
   }

   public void func_82196_d(EntityLivingBase entitylivingbase, float f) {
      if (this.field_70146_Z.nextFloat() > 0.1F) {
         EntityEldritchOrb blast = new EntityEldritchOrb(this.field_70170_p, this);
         this.lastBlast = !this.lastBlast;
         this.field_70170_p.func_72960_a(this, (byte)(this.lastBlast ? 16 : 15));
         int rr = this.lastBlast ? 90 : 180;
         double xx = (double)(MathHelper.func_76134_b((this.field_70177_z + (float)rr) % 360.0F / 180.0F * 3.1415927F) * 0.5F);
         double yy = 0.057777777D * (double)this.field_70131_O;
         double zz = (double)(MathHelper.func_76126_a((this.field_70177_z + (float)rr) % 360.0F / 180.0F * 3.1415927F) * 0.5F);
         blast.func_70107_b(blast.field_70165_t - xx, blast.field_70163_u, blast.field_70161_v - zz);
         Vec3d v = this.func_70040_Z();
         blast.func_70186_c(v.field_72450_a, v.field_72448_b, v.field_72449_c, 1.0F, 2.0F);
         this.func_184185_a(SoundsTC.egattack, 2.0F, 1.0F + this.field_70146_Z.nextFloat() * 0.1F);
         this.field_70170_p.func_72838_d(blast);
      } else if (this.func_70685_l(entitylivingbase)) {
         PacketHandler.INSTANCE.sendToAllAround(new PacketFXSonic(this.func_145782_y()), new TargetPoint(this.field_70170_p.field_73011_w.getDimension(), this.field_70165_t, this.field_70163_u, this.field_70161_v, 32.0D));

         try {
            entitylivingbase.func_70690_d(new PotionEffect(MobEffects.field_82731_v, 400, 0));
         } catch (Exception var12) {
         }

         if (entitylivingbase instanceof EntityPlayer) {
            ThaumcraftApi.internalMethods.addWarpToPlayer((EntityPlayer)entitylivingbase, 1 + this.field_70170_p.field_73012_v.nextInt(3), IPlayerWarp.EnumWarpType.TEMPORARY);
         }

         this.func_184185_a(SoundsTC.egscreech, 3.0F, 1.0F + this.field_70146_Z.nextFloat() * 0.1F);
      }

   }

   public boolean func_184191_r(Entity el) {
      return el instanceof IEldritchMob;
   }
}
