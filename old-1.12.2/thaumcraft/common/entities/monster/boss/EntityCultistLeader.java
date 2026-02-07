package thaumcraft.common.entities.monster.boss;

import java.util.Iterator;
import java.util.List;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.entities.ai.combat.AICultistHurtByTarget;
import thaumcraft.common.entities.ai.combat.AILongRangeAttack;
import thaumcraft.common.entities.monster.cult.EntityCultist;
import thaumcraft.common.entities.monster.cult.EntityCultistCleric;
import thaumcraft.common.entities.monster.cult.EntityCultistKnight;
import thaumcraft.common.entities.monster.mods.ChampionModifier;
import thaumcraft.common.entities.projectile.EntityGolemOrb;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.EntityUtils;

public class EntityCultistLeader extends EntityThaumcraftBoss implements IRangedAttackMob {
   private static final DataParameter<Byte> NAME;
   String[] titles = new String[]{"Alberic", "Anselm", "Bastian", "Beturian", "Chabier", "Chorache", "Chuse", "Dodorol", "Ebardo", "Ferrando", "Fertus", "Guillen", "Larpe", "Obano", "Zelipe"};

   public EntityCultistLeader(World p_i1745_1_) {
      super(p_i1745_1_);
      this.func_70105_a(0.75F, 2.25F);
      this.field_70728_aV = 40;
   }

   protected void func_184651_r() {
      this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
      this.field_70714_bg.func_75776_a(2, new AILongRangeAttack(this, 16.0D, 1.0D, 30, 40, 24.0F));
      this.field_70714_bg.func_75776_a(3, new EntityAIAttackMelee(this, 1.1D, false));
      this.field_70714_bg.func_75776_a(6, new EntityAIMoveTowardsRestriction(this, 0.8D));
      this.field_70714_bg.func_75776_a(7, new EntityAIWander(this, 0.8D));
      this.field_70714_bg.func_75776_a(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.field_70714_bg.func_75776_a(8, new EntityAILookIdle(this));
      this.field_70715_bh.func_75776_a(1, new AICultistHurtByTarget(this, true));
      this.field_70715_bh.func_75776_a(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.32D);
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(150.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(5.0D);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.func_184212_Q().func_187214_a(NAME, (byte)0);
   }

   public void generateName() {
      int t = (int)this.func_110148_a(EntityUtils.CHAMPION_MOD).func_111126_e();
      if (t >= 0) {
         this.func_96094_a(String.format(I18n.func_74838_a("entity.Thaumcraft.CultistLeader.name.custom"), this.getTitle(), ChampionModifier.mods[t].getModNameLocalized()));
      }

   }

   private String getTitle() {
      return this.titles[(Byte)this.func_184212_Q().func_187225_a(NAME)];
   }

   private void setTitle(int title) {
      this.func_184212_Q().func_187227_b(NAME, (byte)title);
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      nbt.func_74774_a("title", (Byte)this.func_184212_Q().func_187225_a(NAME));
   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.setTitle(nbt.func_74771_c("title"));
   }

   protected void func_180481_a(DifficultyInstance difficulty) {
      this.func_184201_a(EntityEquipmentSlot.HEAD, new ItemStack(ItemsTC.crimsonPraetorHelm));
      this.func_184201_a(EntityEquipmentSlot.CHEST, new ItemStack(ItemsTC.crimsonPraetorChest));
      this.func_184201_a(EntityEquipmentSlot.LEGS, new ItemStack(ItemsTC.crimsonPraetorLegs));
      this.func_184201_a(EntityEquipmentSlot.FEET, new ItemStack(ItemsTC.crimsonBoots));
      if (this.field_70170_p.func_175659_aa() == EnumDifficulty.EASY) {
         this.func_184611_a(this.func_184600_cs(), new ItemStack(ItemsTC.voidSword));
      } else {
         this.func_184611_a(this.func_184600_cs(), new ItemStack(ItemsTC.crimsonBlade));
      }

   }

   protected void func_180483_b(DifficultyInstance diff) {
      float f = diff.func_180170_c();
      if (this.func_184614_ca() != null && this.field_70146_Z.nextFloat() < 0.5F * f) {
         EnchantmentHelper.func_77504_a(this.field_70146_Z, this.func_184614_ca(), (int)(7.0F + f * (float)this.field_70146_Z.nextInt(22)), false);
      }

   }

   public boolean func_184191_r(Entity el) {
      return el instanceof EntityCultist || el instanceof EntityCultistLeader;
   }

   public boolean func_70686_a(Class clazz) {
      return clazz != EntityCultistCleric.class && clazz != EntityCultistLeader.class && clazz != EntityCultistKnight.class ? super.func_70686_a(clazz) : false;
   }

   protected Item func_146068_u() {
      return Item.func_150899_d(0);
   }

   protected void func_70628_a(boolean flag, int i) {
      this.func_70099_a(new ItemStack(ItemsTC.lootBag, 1, 2), 1.5F);
   }

   public IEntityLivingData func_180482_a(DifficultyInstance diff, IEntityLivingData data) {
      this.func_180481_a(diff);
      this.func_180483_b(diff);
      this.setTitle(this.field_70146_Z.nextInt(this.titles.length));
      return super.func_180482_a(diff, data);
   }

   protected void func_70619_bc() {
      super.func_70619_bc();
      List<Entity> list = EntityUtils.getEntitiesInRange(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, this, EntityCultist.class, 8.0D);
      Iterator var2 = list.iterator();

      while(var2.hasNext()) {
         Entity e = (Entity)var2.next();

         try {
            if (e instanceof EntityCultist && !((EntityCultist)e).func_70644_a(MobEffects.field_76428_l)) {
               ((EntityCultist)e).func_70690_d(new PotionEffect(MobEffects.field_76428_l, 60, 1));
            }
         } catch (Exception var5) {
         }
      }

   }

   public void func_82196_d(EntityLivingBase entitylivingbase, float f) {
      if (this.func_70685_l(entitylivingbase)) {
         this.func_184609_a(this.func_184600_cs());
         this.func_70671_ap().func_75650_a(entitylivingbase.field_70165_t, entitylivingbase.func_174813_aQ().field_72338_b + (double)(entitylivingbase.field_70131_O / 2.0F), entitylivingbase.field_70161_v, 30.0F, 30.0F);
         EntityGolemOrb blast = new EntityGolemOrb(this.field_70170_p, this, entitylivingbase, true);
         blast.field_70165_t += blast.field_70159_w / 2.0D;
         blast.field_70161_v += blast.field_70179_y / 2.0D;
         blast.func_70107_b(blast.field_70165_t, blast.field_70163_u, blast.field_70161_v);
         double d0 = entitylivingbase.field_70165_t - this.field_70165_t;
         double d1 = entitylivingbase.func_174813_aQ().field_72338_b + (double)(entitylivingbase.field_70131_O / 2.0F) - (this.field_70163_u + (double)(this.field_70131_O / 2.0F));
         double d2 = entitylivingbase.field_70161_v - this.field_70161_v;
         blast.func_70186_c(d0, d1 + 2.0D, d2, 0.66F, 3.0F);
         this.func_184185_a(SoundsTC.egattack, 1.0F, 1.0F + this.field_70146_Z.nextFloat() * 0.1F);
         this.field_70170_p.func_72838_d(blast);
      }

   }

   public void func_70656_aK() {
      if (this.field_70170_p.field_72995_K) {
         for(int i = 0; i < 20; ++i) {
            double d0 = this.field_70146_Z.nextGaussian() * 0.05D;
            double d1 = this.field_70146_Z.nextGaussian() * 0.05D;
            double d2 = this.field_70146_Z.nextGaussian() * 0.05D;
            double d3 = 2.0D;
            FXDispatcher.INSTANCE.cultistSpawn(this.field_70165_t + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N + d0 * d3, this.field_70163_u + (double)(this.field_70146_Z.nextFloat() * this.field_70131_O) + d1 * d3, this.field_70161_v + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N + d2 * d3, d0, d1, d2);
         }
      } else {
         this.field_70170_p.func_72960_a(this, (byte)20);
      }

   }

   static {
      NAME = EntityDataManager.func_187226_a(EntityCultistLeader.class, DataSerializers.field_187191_a);
   }
}
