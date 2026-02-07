package thaumcraft.common.entities.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpider.GroupData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.entities.monster.cult.EntityCultist;
import thaumcraft.common.lib.SoundsTC;

public class EntityEldritchCrab extends EntityMob implements IEldritchMob {
   private static final DataParameter<Boolean> HELM;
   private int attackTime = 0;

   public EntityEldritchCrab(World par1World) {
      super(par1World);
      this.func_70105_a(0.8F, 0.6F);
      this.field_70728_aV = 6;
      ((PathNavigateGround)this.func_70661_as()).func_179688_b(true);
   }

   protected void func_184651_r() {
      this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
      this.field_70714_bg.func_75776_a(2, new EntityAILeapAtTarget(this, 0.63F));
      this.field_70714_bg.func_75776_a(3, new EntityAIAttackMelee(this, 1.0D, false));
      this.field_70714_bg.func_75776_a(7, new EntityAIWander(this, 0.8D));
      this.field_70714_bg.func_75776_a(8, new EntityAILookIdle(this));
      this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, true, new Class[0]));
      this.field_70715_bh.func_75776_a(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
      this.field_70715_bh.func_75776_a(3, new EntityAINearestAttackableTarget(this, EntityCultist.class, true));
   }

   public double func_70033_W() {
      return this.func_184218_aH() ? 0.5D : 0.0D;
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(20.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(4.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(this.hasHelm() ? 0.275D : 0.3D);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.func_184212_Q().func_187214_a(HELM, false);
   }

   public boolean func_98052_bS() {
      return false;
   }

   public int func_70658_aO() {
      return this.hasHelm() ? 5 : 0;
   }

   public IEntityLivingData func_180482_a(DifficultyInstance diff, IEntityLivingData data) {
      if (this.field_70170_p.func_175659_aa() == EnumDifficulty.HARD) {
         this.setHelm(true);
      } else {
         this.setHelm(this.field_70146_Z.nextFloat() < 0.33F);
      }

      if (data == null) {
         data = new GroupData();
         if (this.field_70170_p.func_175659_aa() == EnumDifficulty.HARD && this.field_70170_p.field_73012_v.nextFloat() < 0.1F * diff.func_180170_c()) {
            ((GroupData)data).func_111104_a(this.field_70170_p.field_73012_v);
         }
      }

      if (data instanceof GroupData) {
         Potion potion = ((GroupData)data).field_188478_a;
         if (potion != null) {
            this.func_70690_d(new PotionEffect(potion, Integer.MAX_VALUE));
         }
      }

      return super.func_180482_a(diff, (IEntityLivingData)data);
   }

   public boolean hasHelm() {
      return (Boolean)this.func_184212_Q().func_187225_a(HELM);
   }

   public void setHelm(boolean par1) {
      this.func_184212_Q().func_187227_b(HELM, par1);
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      --this.attackTime;
      if (this.field_70173_aa < 20) {
         this.field_70143_R = 0.0F;
      }

      if (this.func_184187_bx() == null && this.func_70643_av() != null && this.func_70643_av().func_184187_bx() == null && !this.field_70122_E && !this.hasHelm() && !this.func_70643_av().field_70128_L && this.field_70163_u - this.func_70643_av().field_70163_u >= (double)(this.func_70643_av().field_70131_O / 2.0F) && this.func_70068_e(this.func_70643_av()) < 4.0D) {
         this.func_184205_a(this.func_70643_av(), true);
      }

      if (!this.field_70170_p.field_72995_K && this.func_184187_bx() != null && this.attackTime <= 0) {
         this.attackTime = 10 + this.field_70146_Z.nextInt(10);
         this.func_70652_k(this.func_184187_bx());
         if (this.func_184187_bx() != null && (double)this.field_70146_Z.nextFloat() < 0.2D) {
            this.func_110145_l(this.func_184187_bx());
         }
      }

   }

   protected Item func_146068_u() {
      return Item.func_150899_d(0);
   }

   protected void func_70628_a(boolean p_70628_1_, int p_70628_2_) {
      super.func_70628_a(p_70628_1_, p_70628_2_);
      if (p_70628_1_ && (this.field_70146_Z.nextInt(3) == 0 || this.field_70146_Z.nextInt(1 + p_70628_2_) > 0)) {
         this.func_145779_a(Items.field_151079_bi, 1);
      }

   }

   public boolean func_70652_k(Entity p_70652_1_) {
      if (super.func_70652_k(p_70652_1_)) {
         this.func_184185_a(SoundsTC.crabclaw, 1.0F, 0.9F + this.field_70170_p.field_73012_v.nextFloat() * 0.2F);
         return true;
      } else {
         return false;
      }
   }

   public boolean func_70097_a(DamageSource source, float damage) {
      boolean b = super.func_70097_a(source, damage);
      if (this.hasHelm() && this.func_110143_aJ() / this.func_110138_aP() <= 0.5F) {
         this.setHelm(false);
         this.func_70669_a(new ItemStack(ItemsTC.crimsonPlateChest));
         this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.3D);
      }

      return b;
   }

   public void func_70037_a(NBTTagCompound par1NBTTagCompound) {
      super.func_70037_a(par1NBTTagCompound);
      this.setHelm(par1NBTTagCompound.func_74767_n("helm"));
      if (!this.hasHelm()) {
         this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.3D);
      }

   }

   public void func_70014_b(NBTTagCompound par1NBTTagCompound) {
      super.func_70014_b(par1NBTTagCompound);
      par1NBTTagCompound.func_74757_a("helm", this.hasHelm());
   }

   public int func_70627_aG() {
      return 160;
   }

   protected SoundEvent func_184639_G() {
      return SoundsTC.crabtalk;
   }

   protected SoundEvent func_184601_bQ() {
      return SoundEvents.field_187543_bD;
   }

   protected SoundEvent func_184615_bR() {
      return SoundsTC.crabdeath;
   }

   protected void func_180429_a(BlockPos p_180429_1_, Block p_180429_2_) {
      this.func_184185_a(SoundEvents.field_187823_fN, 0.15F, 1.0F);
   }

   public EnumCreatureAttribute func_70668_bt() {
      return EnumCreatureAttribute.ARTHROPOD;
   }

   public boolean func_70687_e(PotionEffect p_70687_1_) {
      return p_70687_1_.func_188419_a().equals(MobEffects.field_76436_u) ? false : super.func_70687_e(p_70687_1_);
   }

   public boolean func_184191_r(Entity el) {
      return el instanceof EntityEldritchCrab;
   }

   static {
      HELM = EntityDataManager.func_187226_a(EntityEldritchCrab.class, DataSerializers.field_187198_h);
   }
}
