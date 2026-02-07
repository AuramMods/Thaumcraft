package thaumcraft.common.entities.monster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.casters.FocusCore;
import thaumcraft.api.casters.FocusHelper;
import thaumcraft.api.casters.IFocusPart;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.ai.pech.AINearestAttackableTargetPech;
import thaumcraft.common.entities.ai.pech.AIPechItemEntityGoto;
import thaumcraft.common.entities.ai.pech.AIPechTradePlayer;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.world.biomes.BiomeHandler;

public class EntityPech extends EntityMob implements IRangedAttackMob {
   public ItemStack[] loot = new ItemStack[9];
   public boolean trading = false;
   private final EntityAIAttackRanged aiArrowAttack = new EntityAIAttackRanged(this, 0.6D, 20, 50, 15.0F);
   private final EntityAIAttackRanged aiBlastAttack = new EntityAIAttackRanged(this, 0.6D, 20, 30, 15.0F);
   private final EntityAIAttackMelee aiMeleeAttack = new EntityAIAttackMelee(this, 0.6D, false);
   private final EntityAIAvoidEntity aiAvoidPlayer = new EntityAIAvoidEntity(this, EntityPlayer.class, 8.0F, 0.5D, 0.6D);
   private static final DataParameter<Byte> TYPE;
   private static final DataParameter<Integer> ANGER;
   private static final DataParameter<Boolean> TAMED;
   public static final ResourceLocation LOOT;
   public float mumble = 0.0F;
   int chargecount = 0;
   static HashMap<Integer, Integer> valuedItems;
   public static HashMap<Integer, ArrayList<List>> tradeInventory;

   public String func_70005_c_() {
      if (this.func_145818_k_()) {
         return this.func_95999_t();
      } else {
         switch(this.getPechType()) {
         case 0:
            return I18n.func_74838_a("entity.Thaumcraft.Pech.name");
         case 1:
            return I18n.func_74838_a("entity.Thaumcraft.Pech.1.name");
         case 2:
            return I18n.func_74838_a("entity.Thaumcraft.Pech.2.name");
         default:
            return I18n.func_74838_a("entity.Thaumcraft.Pech.name");
         }
      }
   }

   public EntityPech(World world) {
      super(world);
      this.func_70105_a(0.6F, 1.8F);
      ((PathNavigateGround)this.func_70661_as()).func_179693_d(false);
      this.func_184642_a(EntityEquipmentSlot.MAINHAND, 0.2F);
      this.func_184642_a(EntityEquipmentSlot.OFFHAND, 0.2F);
   }

   protected void func_184651_r() {
      this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
      this.field_70714_bg.func_75776_a(1, new AIPechTradePlayer(this));
      this.field_70714_bg.func_75776_a(3, new AIPechItemEntityGoto(this));
      this.field_70714_bg.func_75776_a(5, new EntityAIOpenDoor(this, true));
      this.field_70714_bg.func_75776_a(6, new EntityAIMoveTowardsRestriction(this, 0.5D));
      this.field_70714_bg.func_75776_a(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
      this.field_70714_bg.func_75776_a(9, new EntityAIWander(this, 0.6D));
      this.field_70714_bg.func_75776_a(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
      this.field_70714_bg.func_75776_a(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
      this.field_70714_bg.func_75776_a(11, new EntityAILookIdle(this));
      this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, false, new Class[0]));
      this.field_70715_bh.func_75776_a(2, new AINearestAttackableTargetPech(this, EntityPlayer.class, true));
   }

   public void setCombatTask() {
      if (this.field_70170_p != null && !this.field_70170_p.field_72995_K) {
         this.field_70714_bg.func_85156_a(this.aiMeleeAttack);
         this.field_70714_bg.func_85156_a(this.aiArrowAttack);
         this.field_70714_bg.func_85156_a(this.aiBlastAttack);
         ItemStack itemstack = this.func_184614_ca();
         if (itemstack != null && itemstack.func_77973_b() == Items.field_151031_f) {
            this.field_70714_bg.func_75776_a(2, this.aiArrowAttack);
         } else if (itemstack != null && itemstack.func_77973_b() == ItemsTC.pechWand) {
            this.field_70714_bg.func_75776_a(2, this.aiBlastAttack);
         } else {
            this.field_70714_bg.func_75776_a(2, this.aiMeleeAttack);
         }

         if (this.isTamed()) {
            this.field_70714_bg.func_85156_a(this.aiAvoidPlayer);
         } else {
            this.field_70714_bg.func_75776_a(4, this.aiAvoidPlayer);
         }
      }

   }

   public void func_82196_d(EntityLivingBase target, float f) {
      if (this.getPechType() == 2) {
         EntityTippedArrow entityarrow = new EntityTippedArrow(this.field_70170_p, this);
         if ((double)this.field_70170_p.field_73012_v.nextFloat() < 0.2D) {
            ItemStack itemstack = new ItemStack(Items.field_185167_i);
            PotionUtils.func_185184_a(itemstack, Collections.singletonList(new PotionEffect(MobEffects.field_76436_u, 40)));
            entityarrow.func_184555_a(itemstack);
         }

         double d0 = target.field_70165_t - this.field_70165_t;
         double d1 = target.func_174813_aQ().field_72338_b + (double)(target.field_70131_O / 3.0F) - entityarrow.field_70163_u;
         double d2 = target.field_70161_v - this.field_70161_v;
         double d3 = (double)MathHelper.func_76133_a(d0 * d0 + d2 * d2);
         entityarrow.func_70186_c(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.field_70170_p.func_175659_aa().func_151525_a() * 4));
         int i = EnchantmentHelper.func_185284_a(Enchantments.field_185309_u, this);
         int j = EnchantmentHelper.func_185284_a(Enchantments.field_185310_v, this);
         entityarrow.func_70239_b((double)(f * 2.0F) + this.field_70146_Z.nextGaussian() * 0.25D + (double)((float)this.field_70170_p.func_175659_aa().func_151525_a() * 0.11F));
         if (i > 0) {
            entityarrow.func_70239_b(entityarrow.func_70242_d() + (double)i * 0.5D + 0.5D);
         }

         if (j > 0) {
            entityarrow.func_70240_a(j);
         }

         if (EnchantmentHelper.func_185284_a(Enchantments.field_185311_w, this) > 0) {
            entityarrow.func_70015_d(100);
         }

         this.func_184185_a(SoundEvents.field_187737_v, 1.0F, 1.0F / (this.func_70681_au().nextFloat() * 0.4F + 0.8F));
         this.field_70170_p.func_72838_d(entityarrow);
      } else if (this.getPechType() == 1) {
         FocusCore focus = new FocusCore();
         focus.medium = FocusHelper.PROJECTILE;
         focus.effects[0].effect = this.field_70146_Z.nextBoolean() ? FocusHelper.CURSE : FocusHelper.MAGIC;
         if (this.field_70146_Z.nextInt(4) == 0) {
            focus.effects[0].modifiers = new IFocusPart[]{this.field_70146_Z.nextInt(3) == 0 ? FocusHelper.SCATTER : FocusHelper.POTENCY};
         }

         focus.medium.onMediumTrigger(this.field_70170_p, this, (ItemStack)null, focus, 1.0F);
         this.func_184609_a(this.func_184600_cs());
      }

   }

   public void func_184201_a(EntityEquipmentSlot slotIn, @Nullable ItemStack stack) {
      super.func_184201_a(slotIn, stack);
      if (!this.field_70170_p.field_72995_K && slotIn == EntityEquipmentSlot.MAINHAND) {
         this.setCombatTask();
      }

   }

   protected void func_180481_a(DifficultyInstance difficulty) {
      super.func_180481_a(difficulty);
      switch(this.field_70146_Z.nextInt(20)) {
      case 0:
      case 12:
         this.func_184611_a(this.func_184600_cs(), new ItemStack(ItemsTC.pechWand));
         break;
      case 1:
         this.func_184611_a(this.func_184600_cs(), new ItemStack(Items.field_151052_q));
         break;
      case 2:
      case 4:
      case 10:
      case 11:
      case 13:
         this.func_184611_a(this.func_184600_cs(), new ItemStack(Items.field_151031_f));
         break;
      case 3:
         this.func_184611_a(this.func_184600_cs(), new ItemStack(Items.field_151049_t));
         break;
      case 5:
         this.func_184611_a(this.func_184600_cs(), new ItemStack(Items.field_151040_l));
         break;
      case 6:
         this.func_184611_a(this.func_184600_cs(), new ItemStack(Items.field_151036_c));
         break;
      case 7:
         this.func_184611_a(this.func_184600_cs(), new ItemStack(Items.field_151112_aM));
         break;
      case 8:
         this.func_184611_a(this.func_184600_cs(), new ItemStack(Items.field_151050_s));
         break;
      case 9:
         this.func_184611_a(this.func_184600_cs(), new ItemStack(Items.field_151035_b));
      }

   }

   public IEntityLivingData func_180482_a(DifficultyInstance diff, IEntityLivingData data) {
      this.func_180481_a(diff);
      ItemStack itemstack = this.func_184586_b(this.func_184600_cs());
      if (itemstack != null && itemstack.func_77973_b() == ItemsTC.pechWand) {
         this.setPechType(1);
         this.func_184642_a(this.func_184600_cs() == EnumHand.MAIN_HAND ? EntityEquipmentSlot.MAINHAND : EntityEquipmentSlot.OFFHAND, 0.1F);
      } else if (itemstack != null) {
         if (itemstack.func_77973_b() == Items.field_151031_f) {
            this.setPechType(2);
         }

         this.func_180483_b(diff);
      }

      float f = diff.func_180170_c();
      this.func_98053_h(this.field_70146_Z.nextFloat() < 0.75F * f);
      this.setCombatTask();
      return super.func_180482_a(diff, data);
   }

   public boolean func_70601_bi() {
      Biome biome = this.field_70170_p.func_180494_b(new BlockPos(this));
      boolean magicBiome = false;
      if (biome != null) {
         magicBiome = BiomeDictionary.isBiomeOfType(biome, Type.MAGICAL);
      }

      int count = 0;

      try {
         List l = this.field_70170_p.func_72872_a(EntityPech.class, this.func_174813_aQ().func_72314_b(16.0D, 16.0D, 16.0D));
         if (l != null) {
            count = l.size();
         }
      } catch (Exception var5) {
      }

      if (this.field_70170_p.field_73011_w.getDimension() != Config.overworldDim && biome != BiomeHandler.MAGICAL_FOREST && biome != BiomeHandler.EERIE) {
         magicBiome = false;
      }

      return count < 4 && magicBiome && super.func_70601_bi();
   }

   public float func_70047_e() {
      return this.field_70131_O * 0.66F;
   }

   public void func_70636_d() {
      super.func_70636_d();
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.func_184212_Q().func_187214_a(TYPE, (byte)0);
      this.func_184212_Q().func_187214_a(ANGER, 0);
      this.func_184212_Q().func_187214_a(TAMED, false);
   }

   public int getPechType() {
      return (Byte)this.func_184212_Q().func_187225_a(TYPE);
   }

   public int getAnger() {
      return (Integer)this.func_184212_Q().func_187225_a(ANGER);
   }

   public boolean isTamed() {
      return (Boolean)this.func_184212_Q().func_187225_a(TAMED);
   }

   public void setPechType(int par1) {
      this.func_184212_Q().func_187227_b(TYPE, (byte)par1);
   }

   public void setAnger(int par1) {
      this.func_184212_Q().func_187227_b(ANGER, par1);
   }

   public void setTamed(boolean par1) {
      this.func_184212_Q().func_187227_b(TAMED, par1);
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(30.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(6.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.5D);
   }

   public void func_70014_b(NBTTagCompound par1NBTTagCompound) {
      super.func_70014_b(par1NBTTagCompound);
      par1NBTTagCompound.func_74774_a("PechType", (byte)this.getPechType());
      par1NBTTagCompound.func_74777_a("Anger", (short)this.getAnger());
      par1NBTTagCompound.func_74757_a("Tamed", this.isTamed());
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.loot.length; ++i) {
         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
         if (this.loot[i] != null) {
            this.loot[i].func_77955_b(nbttagcompound1);
         }

         nbttaglist.func_74742_a(nbttagcompound1);
      }

      par1NBTTagCompound.func_74782_a("Loot", nbttaglist);
   }

   public void func_70037_a(NBTTagCompound par1NBTTagCompound) {
      super.func_70037_a(par1NBTTagCompound);
      if (par1NBTTagCompound.func_74764_b("PechType")) {
         byte b0 = par1NBTTagCompound.func_74771_c("PechType");
         this.setPechType(b0);
      }

      this.setAnger(par1NBTTagCompound.func_74765_d("Anger"));
      this.setTamed(par1NBTTagCompound.func_74767_n("Tamed"));
      if (par1NBTTagCompound.func_74764_b("Loot")) {
         NBTTagList nbttaglist = par1NBTTagCompound.func_150295_c("Loot", 10);

         for(int i = 0; i < this.loot.length; ++i) {
            this.loot[i] = ItemStack.func_77949_a(nbttaglist.func_150305_b(i));
         }
      }

      this.setCombatTask();
   }

   protected boolean func_70692_ba() {
      try {
         if (this.loot == null) {
            return true;
         } else {
            int q = 0;
            ItemStack[] var2 = this.loot;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               ItemStack is = var2[var4];
               if (is != null && is.field_77994_a > 0) {
                  ++q;
               }
            }

            return q < 5;
         }
      } catch (Exception var6) {
         return true;
      }
   }

   public boolean func_184652_a(EntityPlayer player) {
      return false;
   }

   protected ResourceLocation func_184647_J() {
      return LOOT;
   }

   protected void func_70628_a(boolean flag, int i) {
      for(int a = 0; a < this.loot.length; ++a) {
         if (this.loot[a] != null && this.field_70170_p.field_73012_v.nextFloat() < 0.33F) {
            this.func_70099_a(this.loot[a].func_77946_l(), 1.5F);
         }
      }

      super.func_70628_a(flag, i);
   }

   @SideOnly(Side.CLIENT)
   public void func_70103_a(byte par1) {
      int i;
      double d0;
      double d1;
      double d2;
      if (par1 == 16) {
         this.mumble = 3.1415927F;
      } else if (par1 == 17) {
         this.mumble = 6.2831855F;
      } else if (par1 == 18) {
         for(i = 0; i < 5; ++i) {
            d0 = this.field_70146_Z.nextGaussian() * 0.02D;
            d1 = this.field_70146_Z.nextGaussian() * 0.02D;
            d2 = this.field_70146_Z.nextGaussian() * 0.02D;
            this.field_70170_p.func_175688_a(EnumParticleTypes.VILLAGER_HAPPY, this.field_70165_t + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N, this.field_70163_u + 0.5D + (double)(this.field_70146_Z.nextFloat() * this.field_70131_O), this.field_70161_v + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N, d0, d1, d2, new int[0]);
         }
      }

      if (par1 == 19) {
         for(i = 0; i < 5; ++i) {
            d0 = this.field_70146_Z.nextGaussian() * 0.02D;
            d1 = this.field_70146_Z.nextGaussian() * 0.02D;
            d2 = this.field_70146_Z.nextGaussian() * 0.02D;
            this.field_70170_p.func_175688_a(EnumParticleTypes.VILLAGER_ANGRY, this.field_70165_t + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N, this.field_70163_u + 0.5D + (double)(this.field_70146_Z.nextFloat() * this.field_70131_O), this.field_70161_v + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N, d0, d1, d2, new int[0]);
         }

         this.mumble = 6.2831855F;
      } else {
         super.func_70103_a(par1);
      }

   }

   public void func_70642_aH() {
      if (!this.field_70170_p.field_72995_K) {
         if (this.field_70146_Z.nextInt(3) == 0) {
            List list = this.field_70170_p.func_72839_b(this, this.func_174813_aQ().func_72314_b(4.0D, 2.0D, 4.0D));

            for(int i = 0; i < list.size(); ++i) {
               Entity entity1 = (Entity)list.get(i);
               if (entity1 instanceof EntityPech) {
                  this.field_70170_p.func_72960_a(this, (byte)17);
                  this.func_184185_a(SoundsTC.pech_trade, this.func_70599_aP(), this.func_70647_i());
                  return;
               }
            }
         }

         this.field_70170_p.func_72960_a(this, (byte)16);
      }

      super.func_70642_aH();
   }

   public int func_70627_aG() {
      return 120;
   }

   protected float func_70599_aP() {
      return 0.4F;
   }

   protected SoundEvent func_184639_G() {
      return SoundsTC.pech_idle;
   }

   protected SoundEvent func_184601_bQ() {
      return SoundsTC.pech_hit;
   }

   protected SoundEvent func_184615_bR() {
      return SoundsTC.pech_death;
   }

   private void becomeAngryAt(Entity par1Entity) {
      if (this.getAnger() <= 0) {
         this.field_70170_p.func_72960_a(this, (byte)19);
         this.func_184185_a(SoundsTC.pech_charge, this.func_70599_aP(), this.func_70647_i());
      }

      this.func_70624_b((EntityLivingBase)par1Entity);
      this.setAnger(400 + this.field_70146_Z.nextInt(400));
      this.setTamed(false);
      this.setCombatTask();
   }

   public int func_70658_aO() {
      int i = super.func_70658_aO() + 2;
      if (i > 20) {
         i = 20;
      }

      return i;
   }

   public boolean func_70097_a(DamageSource damSource, float par2) {
      if (this.func_180431_b(damSource)) {
         return false;
      } else {
         Entity entity = damSource.func_76346_g();
         if (entity instanceof EntityPlayer) {
            List list = this.field_70170_p.func_72839_b(this, this.func_174813_aQ().func_72314_b(32.0D, 16.0D, 32.0D));

            for(int i = 0; i < list.size(); ++i) {
               Entity entity1 = (Entity)list.get(i);
               if (entity1 instanceof EntityPech) {
                  EntityPech entitypech = (EntityPech)entity1;
                  entitypech.becomeAngryAt(entity);
               }
            }

            this.becomeAngryAt(entity);
         }

         return super.func_70097_a(damSource, par2);
      }
   }

   public void func_70071_h_() {
      if (this.mumble > 0.0F) {
         this.mumble *= 0.75F;
      }

      if (this.getAnger() > 0) {
         this.setAnger(this.getAnger() - 1);
      }

      if (this.getAnger() > 0 && this.func_70638_az() != null) {
         if (this.chargecount > 0) {
            --this.chargecount;
         }

         if (this.chargecount == 0) {
            this.chargecount = 100;
            this.func_184185_a(SoundsTC.pech_charge, this.func_70599_aP(), this.func_70647_i());
         }

         this.field_70170_p.func_72960_a(this, (byte)17);
      }

      double d0;
      double d1;
      double d2;
      if (this.field_70170_p.field_72995_K && this.field_70146_Z.nextInt(15) == 0 && this.getAnger() > 0) {
         d0 = this.field_70146_Z.nextGaussian() * 0.02D;
         d1 = this.field_70146_Z.nextGaussian() * 0.02D;
         d2 = this.field_70146_Z.nextGaussian() * 0.02D;
         this.field_70170_p.func_175688_a(EnumParticleTypes.VILLAGER_ANGRY, this.field_70165_t + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N, this.field_70163_u + 0.5D + (double)(this.field_70146_Z.nextFloat() * this.field_70131_O), this.field_70161_v + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N, d0, d1, d2, new int[0]);
      }

      if (this.field_70170_p.field_72995_K && this.field_70146_Z.nextInt(25) == 0 && this.isTamed()) {
         d0 = this.field_70146_Z.nextGaussian() * 0.02D;
         d1 = this.field_70146_Z.nextGaussian() * 0.02D;
         d2 = this.field_70146_Z.nextGaussian() * 0.02D;
         this.field_70170_p.func_175688_a(EnumParticleTypes.VILLAGER_HAPPY, this.field_70165_t + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N, this.field_70163_u + 0.5D + (double)(this.field_70146_Z.nextFloat() * this.field_70131_O), this.field_70161_v + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double)this.field_70130_N, d0, d1, d2, new int[0]);
      }

      super.func_70071_h_();
   }

   public void func_70619_bc() {
      super.func_70619_bc();
      if (this.field_70173_aa % 40 == 0) {
         this.func_70691_i(1.0F);
      }

   }

   public boolean canPickup(ItemStack entityItem) {
      if (entityItem == null) {
         return false;
      } else if (!this.isTamed() && valuedItems.containsKey(Item.func_150891_b(entityItem.func_77973_b()))) {
         return true;
      } else {
         for(int a = 0; a < this.loot.length; ++a) {
            if (this.loot[a] != null && this.loot[a].field_77994_a <= 0) {
               this.loot[a] = null;
            }

            if (this.loot[a] == null) {
               return true;
            }

            if (InventoryUtils.areItemStacksEqualStrict(entityItem, this.loot[a]) && entityItem.field_77994_a + this.loot[a].field_77994_a <= this.loot[a].func_77976_d()) {
               return true;
            }
         }

         return false;
      }
   }

   public ItemStack pickupItem(ItemStack entityItem) {
      if (entityItem == null) {
         return entityItem;
      } else if (!this.isTamed() && this.isValued(entityItem)) {
         if (this.field_70146_Z.nextInt(10) < this.getValue(entityItem)) {
            this.setTamed(true);
            this.setCombatTask();
            this.field_70170_p.func_72960_a(this, (byte)18);
         }

         --entityItem.field_77994_a;
         return entityItem.field_77994_a <= 0 ? null : entityItem;
      } else {
         int a;
         for(a = 0; a < this.loot.length; ++a) {
            if (this.loot[a] != null && this.loot[a].field_77994_a <= 0) {
               this.loot[a] = null;
            }

            if (entityItem != null && entityItem.field_77994_a > 0 && this.loot[a] != null && this.loot[a].field_77994_a < this.loot[a].func_77976_d() && InventoryUtils.areItemStacksEqualStrict(entityItem, this.loot[a])) {
               ItemStack var10000;
               if (entityItem.field_77994_a + this.loot[a].field_77994_a <= this.loot[a].func_77976_d()) {
                  var10000 = this.loot[a];
                  var10000.field_77994_a += entityItem.field_77994_a;
                  return null;
               }

               int sz = Math.min(entityItem.field_77994_a, this.loot[a].func_77976_d() - this.loot[a].field_77994_a);
               var10000 = this.loot[a];
               var10000.field_77994_a += sz;
               entityItem.field_77994_a -= sz;
            }

            if (entityItem != null && entityItem.field_77994_a <= 0) {
               entityItem = null;
            }
         }

         for(a = 0; a < this.loot.length; ++a) {
            if (this.loot[a] != null && this.loot[a].field_77994_a <= 0) {
               this.loot[a] = null;
            }

            if (entityItem != null && entityItem.field_77994_a > 0 && this.loot[a] == null) {
               this.loot[a] = entityItem.func_77946_l();
               return null;
            }
         }

         if (entityItem != null && entityItem.field_77994_a <= 0) {
            entityItem = null;
         }

         return entityItem;
      }
   }

   protected boolean func_184645_a(EntityPlayer player, EnumHand hand, ItemStack stack) {
      if (player.func_70093_af() || player.func_184586_b(hand) != null && player.func_184586_b(hand).func_77973_b() instanceof ItemNameTag) {
         return false;
      } else if (this.isTamed()) {
         if (!this.field_70170_p.field_72995_K) {
            player.openGui(Thaumcraft.instance, 1, this.field_70170_p, this.func_145782_y(), 0, 0);
         }

         return true;
      } else {
         return super.func_184645_a(player, hand, stack);
      }
   }

   public boolean isValued(ItemStack item) {
      if (item == null) {
         return false;
      } else {
         boolean value = valuedItems.containsKey(Item.func_150891_b(item.func_77973_b()));
         if (!value) {
            AspectList al = ThaumcraftCraftingManager.getObjectTags(item);
            if (al.getAmount(Aspect.DESIRE) > 1) {
               value = true;
            }
         }

         return value;
      }
   }

   public int getValue(ItemStack item) {
      if (item == null) {
         return 0;
      } else {
         int value = valuedItems.containsKey(Item.func_150891_b(item.func_77973_b())) ? (Integer)valuedItems.get(Item.func_150891_b(item.func_77973_b())) : 0;
         if (value == 0) {
            AspectList al = ThaumcraftCraftingManager.getObjectTags(item);
            value = Math.min(32, al.getAmount(Aspect.DESIRE) / 5);
         }

         return value;
      }
   }

   static {
      TYPE = EntityDataManager.func_187226_a(EntityPech.class, DataSerializers.field_187191_a);
      ANGER = EntityDataManager.func_187226_a(EntityPech.class, DataSerializers.field_187192_b);
      TAMED = EntityDataManager.func_187226_a(EntityPech.class, DataSerializers.field_187198_h);
      LOOT = LootTableList.func_186375_a(new ResourceLocation("thaumcraft", "pech"));
      valuedItems = new HashMap();
      tradeInventory = new HashMap();
      valuedItems.put(Item.func_150891_b(Items.field_151043_k), 2);
      valuedItems.put(Item.func_150891_b(Items.field_151153_ao), 2);
      valuedItems.put(Item.func_150891_b(Items.field_151079_bi), 3);
      valuedItems.put(Item.func_150891_b(Items.field_151045_i), 4);
      valuedItems.put(Item.func_150891_b(Items.field_151166_bC), 5);
      ArrayList<List> forInv = new ArrayList();
      forInv.add(Arrays.asList(1, new ItemStack(ItemsTC.clusters, 1, 0)));
      forInv.add(Arrays.asList(1, new ItemStack(ItemsTC.clusters, 1, 1)));
      forInv.add(Arrays.asList(1, new ItemStack(ItemsTC.clusters, 1, 6)));
      if (Config.foundCopperIngot) {
         forInv.add(Arrays.asList(1, new ItemStack(ItemsTC.clusters, 1, 2)));
      }

      if (Config.foundTinIngot) {
         forInv.add(Arrays.asList(1, new ItemStack(ItemsTC.clusters, 1, 3)));
      }

      if (Config.foundSilverIngot) {
         forInv.add(Arrays.asList(1, new ItemStack(ItemsTC.clusters, 1, 4)));
      }

      if (Config.foundLeadIngot) {
         forInv.add(Arrays.asList(1, new ItemStack(ItemsTC.clusters, 1, 5)));
      }

      forInv.add(Arrays.asList(2, new ItemStack(Items.field_151072_bj)));
      forInv.add(Arrays.asList(2, new ItemStack(BlocksTC.sapling, 1, 0)));
      forInv.add(Arrays.asList(2, new ItemStack(Items.field_151068_bn, 1, 8201)));
      forInv.add(Arrays.asList(2, new ItemStack(Items.field_151068_bn, 1, 8194)));
      forInv.add(Arrays.asList(3, new ItemStack(Items.field_151062_by)));
      forInv.add(Arrays.asList(3, new ItemStack(Items.field_151062_by)));
      forInv.add(Arrays.asList(3, new ItemStack(Items.field_151153_ao, 1, 0)));
      forInv.add(Arrays.asList(3, new ItemStack(Items.field_151068_bn, 1, 8265)));
      forInv.add(Arrays.asList(3, new ItemStack(Items.field_151068_bn, 1, 8262)));
      forInv.add(Arrays.asList(4, new ItemStack(ItemsTC.thaumiumPick)));
      forInv.add(Arrays.asList(4, new ItemStack(ItemsTC.thaumiumAxe)));
      forInv.add(Arrays.asList(4, new ItemStack(ItemsTC.thaumiumHoe)));
      forInv.add(Arrays.asList(5, new ItemStack(Items.field_151153_ao, 1, 1)));
      forInv.add(Arrays.asList(5, new ItemStack(BlocksTC.sapling, 1, 1)));
      forInv.add(Arrays.asList(5, new ItemStack(ItemsTC.curio, 1, 4)));
      tradeInventory.put(0, forInv);
      ArrayList<List> forMag = new ArrayList();
      forMag.add(Arrays.asList(1, ThaumcraftApiHelper.makeCrystal(Aspect.AIR)));
      forMag.add(Arrays.asList(1, ThaumcraftApiHelper.makeCrystal(Aspect.EARTH)));
      forMag.add(Arrays.asList(1, ThaumcraftApiHelper.makeCrystal(Aspect.FIRE)));
      forMag.add(Arrays.asList(1, ThaumcraftApiHelper.makeCrystal(Aspect.WATER)));
      forMag.add(Arrays.asList(1, ThaumcraftApiHelper.makeCrystal(Aspect.ORDER)));
      forMag.add(Arrays.asList(1, ThaumcraftApiHelper.makeCrystal(Aspect.ENTROPY)));
      forMag.add(Arrays.asList(2, new ItemStack(Items.field_151068_bn, 1, 8193)));
      forMag.add(Arrays.asList(2, new ItemStack(Items.field_151068_bn, 1, 8261)));
      forMag.add(Arrays.asList(2, ThaumcraftApiHelper.makeCrystal(Aspect.FLUX)));
      forMag.add(Arrays.asList(3, new ItemStack(Items.field_151062_by)));
      forMag.add(Arrays.asList(3, new ItemStack(Items.field_151062_by)));
      forMag.add(Arrays.asList(3, ThaumcraftApiHelper.makeCrystal(Aspect.AURA)));
      forMag.add(Arrays.asList(3, new ItemStack(Items.field_151153_ao, 1, 0)));
      forMag.add(Arrays.asList(3, new ItemStack(Items.field_151068_bn, 1, 8225)));
      forMag.add(Arrays.asList(3, new ItemStack(Items.field_151068_bn, 1, 8229)));
      forMag.add(Arrays.asList(4, new ItemStack(ItemsTC.clothBoots)));
      forMag.add(Arrays.asList(4, new ItemStack(ItemsTC.clothChest)));
      forMag.add(Arrays.asList(4, new ItemStack(ItemsTC.clothLegs)));
      forMag.add(Arrays.asList(5, new ItemStack(Items.field_151153_ao, 1, 1)));
      forMag.add(Arrays.asList(5, new ItemStack(ItemsTC.pechWand)));
      forMag.add(Arrays.asList(5, new ItemStack(ItemsTC.curio, 1, 4)));
      forMag.add(Arrays.asList(5, new ItemStack(ItemsTC.amuletVis, 1, 0)));
      tradeInventory.put(1, forMag);
      ArrayList<List> forArc = new ArrayList();

      for(int a = 0; a < 15; ++a) {
         forArc.add(Arrays.asList(1, new ItemStack(BlocksTC.candle, 1, a)));
      }

      forArc.add(Arrays.asList(2, new ItemStack(Items.field_151073_bk)));
      forArc.add(Arrays.asList(2, new ItemStack(Items.field_151068_bn, 1, 8194)));
      forArc.add(Arrays.asList(2, new ItemStack(Items.field_151068_bn, 1, 8201)));
      forArc.add(Arrays.asList(2, Items.field_151134_bR.func_92111_a(new EnchantmentData(Enchantments.field_185309_u, 1))));
      forArc.add(Arrays.asList(3, new ItemStack(Items.field_151062_by)));
      forArc.add(Arrays.asList(3, new ItemStack(Items.field_151062_by)));
      forArc.add(Arrays.asList(3, new ItemStack(Items.field_151068_bn, 1, 8270)));
      forArc.add(Arrays.asList(3, new ItemStack(Items.field_151068_bn, 1, 8225)));
      forArc.add(Arrays.asList(3, new ItemStack(Items.field_151153_ao, 1, 0)));
      forArc.add(Arrays.asList(4, new ItemStack(ItemsTC.eldritchEye)));
      forArc.add(Arrays.asList(4, new ItemStack(Items.field_151153_ao, 1, 1)));
      forArc.add(Arrays.asList(5, new ItemStack(ItemsTC.baubles, 1, 3)));
      forArc.add(Arrays.asList(5, Items.field_151134_bR.func_92111_a(new EnchantmentData(Enchantments.field_185311_w, 1))));
      forArc.add(Arrays.asList(5, Items.field_151134_bR.func_92111_a(new EnchantmentData(Enchantments.field_185312_x, 1))));
      forArc.add(Arrays.asList(5, new ItemStack(ItemsTC.curio, 1, 4)));
      tradeInventory.put(2, forArc);
   }
}
