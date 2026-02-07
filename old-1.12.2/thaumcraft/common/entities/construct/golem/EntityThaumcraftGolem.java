package thaumcraft.common.entities.construct.golem;

import java.nio.ByteBuffer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.IGolemProperties;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigAspects;
import thaumcraft.common.entities.construct.EntityOwnedConstruct;
import thaumcraft.common.entities.construct.golem.ai.AIFollowOwner;
import thaumcraft.common.entities.construct.golem.ai.AIGotoBlock;
import thaumcraft.common.entities.construct.golem.ai.AIGotoEntity;
import thaumcraft.common.entities.construct.golem.ai.AIGotoHome;
import thaumcraft.common.entities.construct.golem.ai.AIOwnerHurtByTarget;
import thaumcraft.common.entities.construct.golem.ai.AIOwnerHurtTarget;
import thaumcraft.common.entities.construct.golem.ai.PathNavigateGolemAir;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.Utils;

public class EntityThaumcraftGolem extends EntityOwnedConstruct implements IGolemAPI, IRangedAttackMob {
   int rankXp = 0;
   private static final DataParameter<Integer> PROPS1;
   private static final DataParameter<Integer> PROPS2;
   private static final DataParameter<Integer> PROPS3;
   protected Task task = null;
   protected int taskID = Integer.MAX_VALUE;
   public static final int XPM = 1000;

   public EntityThaumcraftGolem(World worldIn) {
      super(worldIn);
      this.func_70105_a(0.4F, 0.9F);
      this.field_70728_aV = 5;
   }

   protected void func_184651_r() {
      this.field_70715_bh.field_75782_a.clear();
      this.field_70714_bg.func_75776_a(2, new AIGotoEntity(this));
      this.field_70714_bg.func_75776_a(3, new AIGotoBlock(this));
      this.field_70714_bg.func_75776_a(4, new AIGotoHome(this));
      this.field_70714_bg.func_75776_a(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.field_70714_bg.func_75776_a(6, new EntityAILookIdle(this));
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.func_184212_Q().func_187214_a(PROPS1, 0);
      this.func_184212_Q().func_187214_a(PROPS2, 0);
      this.func_184212_Q().func_187214_a(PROPS3, 0);
   }

   public IGolemProperties getProperties() {
      ByteBuffer bb = ByteBuffer.allocate(8);
      bb.putInt((Integer)this.func_184212_Q().func_187225_a(PROPS1));
      bb.putInt((Integer)this.func_184212_Q().func_187225_a(PROPS2));
      return GolemProperties.fromLong(bb.getLong(0));
   }

   public void setProperties(IGolemProperties prop) {
      ByteBuffer bb = ByteBuffer.allocate(8);
      bb.putLong(prop.toLong());
      bb.rewind();
      this.func_184212_Q().func_187227_b(PROPS1, bb.getInt());
      this.func_184212_Q().func_187227_b(PROPS2, bb.getInt());
   }

   public byte getGolemColor() {
      byte[] ba = Utils.intToByteArray((Integer)this.func_184212_Q().func_187225_a(PROPS3));
      return ba[0];
   }

   public void setGolemColor(byte b) {
      byte[] ba = Utils.intToByteArray((Integer)this.func_184212_Q().func_187225_a(PROPS3));
      ba[0] = b;
      this.func_184212_Q().func_187227_b(PROPS3, Utils.byteArraytoInt(ba));
   }

   public byte getFlags() {
      byte[] ba = Utils.intToByteArray((Integer)this.func_184212_Q().func_187225_a(PROPS3));
      return ba[1];
   }

   public void setFlags(byte b) {
      byte[] ba = Utils.intToByteArray((Integer)this.func_184212_Q().func_187225_a(PROPS3));
      ba[1] = b;
      this.func_184212_Q().func_187227_b(PROPS3, Utils.byteArraytoInt(ba));
   }

   public float func_70047_e() {
      return 0.7F;
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.3D);
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(10.0D);
      this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_111264_e);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(0.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(32.0D);
   }

   private void updateEntityAttributes() {
      int mh = 10 + this.getProperties().getMaterial().healthMod;
      if (this.getProperties().hasTrait(EnumGolemTrait.FRAGILE)) {
         mh = (int)((double)mh * 0.75D);
      }

      mh += this.getProperties().getRank();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double)mh);
      this.field_70138_W = this.getProperties().hasTrait(EnumGolemTrait.WHEELED) ? 0.5F : 0.6F;
      this.func_175449_a(this.func_180486_cf() == BlockPos.field_177992_a ? this.func_180425_c() : this.func_180486_cf(), this.getProperties().hasTrait(EnumGolemTrait.SCOUT) ? 32 : 16);
      this.field_70699_by = this.func_70661_as();
      if (this.getProperties().hasTrait(EnumGolemTrait.FIGHTER)) {
         double da = (double)this.getProperties().getMaterial().damage;
         if (this.getProperties().hasTrait(EnumGolemTrait.BRUTAL)) {
            da = Math.max(da * 1.5D, da + 1.0D);
         }

         da += (double)this.getProperties().getRank() * 0.25D;
         this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(da);
      } else {
         this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(0.0D);
      }

      this.createAI();
   }

   private void createAI() {
      this.field_70714_bg.field_75782_a.clear();
      this.field_70715_bh.field_75782_a.clear();
      if (this.isFollowingOwner()) {
         this.field_70714_bg.func_75776_a(4, new AIFollowOwner(this, 1.0D, 10.0F, 2.0F));
      } else {
         this.field_70714_bg.func_75776_a(3, new AIGotoEntity(this));
         this.field_70714_bg.func_75776_a(4, new AIGotoBlock(this));
         this.field_70714_bg.func_75776_a(5, new AIGotoHome(this));
      }

      this.field_70714_bg.func_75776_a(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.field_70714_bg.func_75776_a(9, new EntityAILookIdle(this));
      if (this.getProperties().hasTrait(EnumGolemTrait.FIGHTER)) {
         if (this.func_70661_as() instanceof PathNavigateGround) {
            this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
         }

         if (this.getProperties().hasTrait(EnumGolemTrait.RANGED)) {
            EntityAIAttackRanged aa = null;
            if (this.getProperties().getArms().function != null) {
               aa = this.getProperties().getArms().function.getRangedAttackAI(this);
            }

            if (aa != null) {
               this.field_70714_bg.func_75776_a(1, aa);
            }
         }

         this.field_70714_bg.func_75776_a(2, new EntityAIAttackMelee(this, 1.15D, false));
         if (this.isFollowingOwner()) {
            this.field_70715_bh.func_75776_a(1, new AIOwnerHurtByTarget(this));
            this.field_70715_bh.func_75776_a(2, new AIOwnerHurtTarget(this));
         }

         this.field_70715_bh.func_75776_a(3, new EntityAIHurtByTarget(this, false, new Class[0]));
      }

   }

   public boolean func_70617_f_() {
      return this.isBesideClimbableBlock();
   }

   public IEntityLivingData func_180482_a(DifficultyInstance diff, IEntityLivingData ld) {
      this.func_175449_a(this.func_180425_c(), 16);
      this.updateEntityAttributes();
      return ld;
   }

   public int func_70658_aO() {
      int armor = this.getProperties().getMaterial().armor;
      if (this.getProperties().hasTrait(EnumGolemTrait.ARMORED)) {
         armor = (int)Math.max((double)armor * 1.5D, (double)(armor + 1));
      }

      if (this.getProperties().hasTrait(EnumGolemTrait.FRAGILE)) {
         armor = (int)((double)armor * 0.75D);
      }

      return armor;
   }

   public void func_70636_d() {
      this.func_82168_bl();
      super.func_70636_d();
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.field_70170_p.field_72995_K) {
         if (this.task != null && this.task.isSuspended()) {
            this.task = null;
         }

         if (this.func_70638_az() != null && this.func_70638_az().field_70128_L) {
            this.func_70624_b((EntityLivingBase)null);
         }

         if (this.func_70638_az() != null && this.getProperties().hasTrait(EnumGolemTrait.RANGED) && this.func_70068_e(this.func_70638_az()) > 1024.0D) {
            this.func_70624_b((EntityLivingBase)null);
         }

         if (!FMLCommonHandler.instance().getMinecraftServerInstance().func_71219_W() && this.func_70638_az() != null && this.func_70638_az() instanceof EntityPlayer) {
            this.func_70624_b((EntityLivingBase)null);
         }

         if (this.field_70173_aa % (this.getProperties().hasTrait(EnumGolemTrait.REPAIR) ? 40 : 100) == 0) {
            this.func_70691_i(1.0F);
         }

         if (this.getProperties().hasTrait(EnumGolemTrait.CLIMBER)) {
            this.setBesideClimbableBlock(!this.field_82175_bq && this.field_70123_F);
         }
      }

      if (this.getProperties().getHead().function != null) {
         this.getProperties().getHead().function.onUpdateTick(this);
      }

      if (this.getProperties().getArms().function != null) {
         this.getProperties().getArms().function.onUpdateTick(this);
      }

      if (this.getProperties().getLegs().function != null) {
         this.getProperties().getLegs().function.onUpdateTick(this);
      }

      if (this.getProperties().getAddon().function != null) {
         this.getProperties().getAddon().function.onUpdateTick(this);
      }

   }

   @SideOnly(Side.CLIENT)
   public void func_70103_a(byte par1) {
      if (par1 == 5) {
         FXDispatcher.INSTANCE.drawGenericParticles(this.field_70165_t, this.field_70163_u + (double)this.field_70131_O + 0.1D, this.field_70161_v, 0.0D, 0.0D, 0.0D, 1.0F, 1.0F, 1.0F, 0.5F, false, 704 + (this.field_70146_Z.nextBoolean() ? 0 : 3), 3, 1, 6, 0, 2.0F, 0.0F, 1);
      } else if (par1 == 6) {
         FXDispatcher.INSTANCE.drawGenericParticles(this.field_70165_t, this.field_70163_u + (double)this.field_70131_O + 0.1D, this.field_70161_v, 0.0D, 0.025D, 0.0D, 0.1F, 1.0F, 1.0F, 0.5F, false, 15, 1, 1, 10, 0, 2.0F, 0.0F, 1);
      } else if (par1 == 7) {
         FXDispatcher.INSTANCE.drawGenericParticles(this.field_70165_t, this.field_70163_u + (double)this.field_70131_O + 0.1D, this.field_70161_v, 0.0D, 0.05D, 0.0D, 1.0F, 1.0F, 1.0F, 0.5F, false, 640, 10, 1, 10, 0, 2.0F, 0.0F, 1);
      } else if (par1 == 8) {
         FXDispatcher.INSTANCE.drawGenericParticles(this.field_70165_t, this.field_70163_u + (double)this.field_70131_O + 0.1D, this.field_70161_v, 0.0D, 0.01D, 0.0D, 1.0F, 1.0F, 0.1F, 0.5F, false, 14, 1, 1, 20, 0, 2.0F, 0.0F, 1);
      } else if (par1 == 9) {
         for(int a = 0; a < 5; ++a) {
            FXDispatcher.INSTANCE.drawGenericParticles(this.field_70165_t, this.field_70163_u + (double)this.field_70131_O, this.field_70161_v, this.field_70146_Z.nextGaussian() * 0.009999999776482582D, (double)this.field_70146_Z.nextFloat() * 0.02D, this.field_70146_Z.nextGaussian() * 0.009999999776482582D, 1.0F, 1.0F, 1.0F, 0.5F, false, 13, 1, 1, 20 + this.field_70146_Z.nextInt(20), 0, 0.3F + this.field_70146_Z.nextFloat() * 0.4F, 0.0F, 1);
         }
      } else {
         super.func_70103_a(par1);
      }

   }

   public float getGolemMoveSpeed() {
      return 1.0F + (float)this.getProperties().getRank() * 0.025F + (this.getProperties().hasTrait(EnumGolemTrait.LIGHT) ? 0.2F : 0.0F) + (this.getProperties().hasTrait(EnumGolemTrait.HEAVY) ? -0.175F : 0.0F) + (this.getProperties().hasTrait(EnumGolemTrait.FLYER) ? -0.33F : 0.0F) + (this.getProperties().hasTrait(EnumGolemTrait.WHEELED) ? 0.25F : 0.0F);
   }

   public PathNavigate func_70661_as() {
      return (PathNavigate)(this.getProperties().hasTrait(EnumGolemTrait.FLYER) ? new PathNavigateGolemAir(this, this.field_70170_p) : (this.getProperties().hasTrait(EnumGolemTrait.CLIMBER) ? new PathNavigateClimber(this, this.field_70170_p) : new PathNavigateGround(this, this.field_70170_p)));
   }

   public void func_70612_e(float f1, float f2) {
      if (this.func_70613_aW()) {
         if (this.getProperties().hasTrait(EnumGolemTrait.FLYER)) {
            this.func_70060_a(f1, f2, 0.1F);
            this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.field_70159_w *= 0.8999999761581421D;
            this.field_70181_x *= 0.8999999761581421D;
            this.field_70179_y *= 0.8999999761581421D;
            if (this.func_70638_az() == null) {
               this.field_70181_x -= 0.005D;
            }
         } else {
            super.func_70612_e(f1, f2);
         }
      } else {
         super.func_70612_e(f1, f2);
      }

   }

   protected boolean func_70041_e_() {
      return this.getProperties().hasTrait(EnumGolemTrait.HEAVY) && !this.getProperties().hasTrait(EnumGolemTrait.FLYER);
   }

   public void func_180430_e(float distance, float damageMultiplier) {
      if (!this.getProperties().hasTrait(EnumGolemTrait.FLYER) && !this.getProperties().hasTrait(EnumGolemTrait.CLIMBER)) {
         super.func_180430_e(distance, damageMultiplier);
      }

   }

   public void func_70037_a(NBTTagCompound nbt) {
      super.func_70037_a(nbt);
      this.setProperties(GolemProperties.fromLong(nbt.func_74763_f("props")));
      this.func_175449_a(BlockPos.func_177969_a(nbt.func_74763_f("homepos")), 16);
      this.setFlags(Byte.valueOf(nbt.func_74771_c("gflags")));
      this.rankXp = nbt.func_74762_e("rankXP");
      this.setGolemColor(nbt.func_74771_c("color"));
      this.updateEntityAttributes();
   }

   public void func_70014_b(NBTTagCompound nbt) {
      super.func_70014_b(nbt);
      nbt.func_74772_a("props", this.getProperties().toLong());
      nbt.func_74772_a("homepos", this.func_180486_cf().func_177986_g());
      nbt.func_74774_a("gflags", this.getFlags());
      nbt.func_74768_a("rankXP", this.rankXp);
      nbt.func_74774_a("color", this.getGolemColor());
   }

   protected void func_70665_d(DamageSource ds, float damage) {
      if (!ds.func_76347_k() || !this.getProperties().hasTrait(EnumGolemTrait.FIREPROOF)) {
         if (ds.func_94541_c() && this.getProperties().hasTrait(EnumGolemTrait.BLASTPROOF)) {
            damage = Math.min(this.func_110138_aP() / 2.0F, damage * 0.3F);
         }

         if (ds != DamageSource.field_76367_g) {
            if (ds == DamageSource.field_76368_d || ds == DamageSource.field_76380_i) {
               this.func_70012_b((double)this.func_180486_cf().func_177958_n() + 0.5D, (double)this.func_180486_cf().func_177956_o() + 0.5D, (double)this.func_180486_cf().func_177952_p() + 0.5D, 0.0F, 0.0F);
            }

            super.func_70665_d(ds, damage);
         }
      }
   }

   protected boolean func_184645_a(EntityPlayer player, EnumHand hand, ItemStack stack) {
      if (player.func_70093_af() || stack != null && stack.func_77973_b() instanceof ItemNameTag) {
         return false;
      } else if (!this.field_70170_p.field_72995_K && this.isOwner(player) && !this.field_70128_L) {
         if (player.func_70093_af()) {
            this.func_184185_a(SoundsTC.zap, 1.0F, 1.0F);
            if (this.task != null) {
               this.task.setReserved(false);
            }

            this.dropCarried();
            ItemStack placer = new ItemStack(ItemsTC.golemPlacer);
            placer.func_77983_a("props", new NBTTagLong(this.getProperties().toLong()));
            placer.func_77983_a("xp", new NBTTagInt(this.rankXp));
            this.func_70099_a(placer, 0.5F);
            this.func_70106_y();
            player.func_184609_a(hand);
         } else if (stack != null && stack.func_77973_b() instanceof ItemGolemBell && ThaumcraftCapabilities.getKnowledge(player).isResearchKnown("GOLEMDIRECT")) {
            if (this.task != null) {
               this.task.setReserved(false);
            }

            this.func_184185_a(SoundsTC.scan, 1.0F, 1.0F);
            this.setFollowingOwner(!this.isFollowingOwner());
            if (this.isFollowingOwner()) {
               player.func_145747_a(new TextComponentTranslation("golem.follow", new Object[]{""}));
               if (Config.showGolemEmotes) {
                  this.field_70170_p.func_72960_a(this, (byte)5);
               }
            } else {
               player.func_145747_a(new TextComponentTranslation("golem.stay", new Object[]{""}));
               if (Config.showGolemEmotes) {
                  this.field_70170_p.func_72960_a(this, (byte)8);
               }

               this.func_175449_a(this.func_180425_c(), 16);
            }

            this.updateEntityAttributes();
            player.func_184609_a(hand);
         } else if (stack != null) {
            int[] ids = OreDictionary.getOreIDs(stack);
            if (ids != null && ids.length > 0) {
               int[] var5 = ids;
               int var6 = ids.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  int id = var5[var7];
                  String s = OreDictionary.getOreName(id);
                  if (s.startsWith("dye")) {
                     for(int a = 0; a < ConfigAspects.dyes.length; ++a) {
                        if (s.equals(ConfigAspects.dyes[a])) {
                           this.func_184185_a(SoundsTC.zap, 1.0F, 1.0F);
                           this.setGolemColor((byte)(16 - a));
                           --stack.field_77994_a;
                           player.func_184609_a(hand);
                           return true;
                        }
                     }
                  }
               }
            }
         }

         return true;
      } else {
         return super.func_184645_a(player, hand, stack);
      }
   }

   public void func_70645_a(DamageSource cause) {
      if (this.task != null) {
         this.task.setReserved(false);
      }

      super.func_70645_a(cause);
      if (!this.field_70170_p.field_72995_K) {
         this.dropCarried();
      }

   }

   protected void dropCarried() {
      ItemStack[] var1 = this.getCarrying();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ItemStack s = var1[var3];
         if (s != null) {
            this.func_70099_a(s, 0.25F);
         }
      }

   }

   protected void func_70628_a(boolean p_70628_1_, int p_70628_2_) {
      float b = (float)p_70628_2_ * 0.15F;
      ItemStack[] var4 = this.getProperties().generateComponents();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ItemStack stack = var4[var6];
         ItemStack s = stack.func_77946_l();
         if (this.field_70146_Z.nextFloat() < 0.3F + b) {
            if (s.field_77994_a > 0) {
               s.field_77994_a -= this.field_70146_Z.nextInt(s.field_77994_a);
            }

            this.func_70099_a(s, 0.25F);
         }
      }

   }

   public boolean isBesideClimbableBlock() {
      return Utils.getBit(this.getFlags(), 0);
   }

   public void setBesideClimbableBlock(boolean p_70839_1_) {
      byte b0 = this.getFlags();
      if (p_70839_1_) {
         b0 = (byte)Utils.setBit(b0, 0);
      } else {
         b0 = (byte)Utils.clearBit(b0, 0);
      }

      this.setFlags(Byte.valueOf(b0));
   }

   public boolean isFollowingOwner() {
      return Utils.getBit(this.getFlags(), 1);
   }

   public void setFollowingOwner(boolean par1) {
      byte var2 = this.getFlags();
      if (par1) {
         this.setFlags(Byte.valueOf((byte)Utils.setBit(var2, 1)));
      } else {
         this.setFlags(Byte.valueOf((byte)Utils.clearBit(var2, 1)));
      }

   }

   public void func_70624_b(EntityLivingBase entitylivingbaseIn) {
      super.func_70624_b(entitylivingbaseIn);
      this.setInCombat(this.func_70638_az() != null);
   }

   public boolean isInCombat() {
      return Utils.getBit(this.getFlags(), 3);
   }

   public void setInCombat(boolean par1) {
      byte var2 = this.getFlags();
      if (par1) {
         this.setFlags(Byte.valueOf((byte)Utils.setBit(var2, 3)));
      } else {
         this.setFlags(Byte.valueOf((byte)Utils.clearBit(var2, 3)));
      }

   }

   public boolean func_70652_k(Entity ent) {
      float dmg = (float)this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e();
      int kb = 0;
      if (ent instanceof EntityLivingBase) {
         dmg += EnchantmentHelper.func_152377_a(this.func_184614_ca(), ((EntityLivingBase)ent).func_70668_bt());
         kb += EnchantmentHelper.func_77501_a(this);
      }

      boolean flag = ent.func_70097_a(DamageSource.func_76358_a(this), dmg);
      if (flag) {
         if (ent instanceof EntityLivingBase && this.getProperties().hasTrait(EnumGolemTrait.DEFT)) {
            ((EntityLivingBase)ent).field_70718_bc = 100;
         }

         if (kb > 0) {
            ent.func_70024_g((double)(-MathHelper.func_76126_a(this.field_70177_z * 3.1415927F / 180.0F) * (float)kb * 0.5F), 0.1D, (double)(MathHelper.func_76134_b(this.field_70177_z * 3.1415927F / 180.0F) * (float)kb * 0.5F));
            this.field_70159_w *= 0.6D;
            this.field_70179_y *= 0.6D;
         }

         int j = EnchantmentHelper.func_90036_a(this);
         if (j > 0) {
            ent.func_70015_d(j * 4);
         }

         this.func_174815_a(this, ent);
         if (this.getProperties().getArms().function != null) {
            this.getProperties().getArms().function.onMeleeAttack(this, ent);
         }

         if (ent instanceof EntityLiving && !((EntityLiving)ent).func_70089_S()) {
            this.addRankXp(8);
         }
      }

      return flag;
   }

   public Task getTask() {
      if (this.task == null && this.taskID != Integer.MAX_VALUE) {
         this.task = TaskHandler.getTask(this.field_70170_p.field_73011_w.getDimension(), this.taskID);
         this.taskID = Integer.MAX_VALUE;
      }

      return this.task;
   }

   public void setTask(Task task) {
      this.task = task;
   }

   public void addRankXp(int xp) {
      if (this.getProperties().hasTrait(EnumGolemTrait.SMART) && !this.field_70170_p.field_72995_K) {
         int rank = this.getProperties().getRank();
         if (rank < 10) {
            this.rankXp += xp;
            int xn = (rank + 1) * (rank + 1) * 1000;
            if (this.rankXp >= xn) {
               this.rankXp -= xn;
               IGolemProperties props = this.getProperties();
               props.setRank(rank + 1);
               this.setProperties(props);
               if (Config.showGolemEmotes) {
                  this.field_70170_p.func_72960_a(this, (byte)9);
                  this.func_184185_a(SoundEvents.field_187802_ec, 0.25F, 1.0F);
               }
            }
         }

      }
   }

   public ItemStack holdItem(ItemStack stack) {
      if (stack != null && stack.field_77994_a > 0) {
         for(int a = 0; a < (this.getProperties().hasTrait(EnumGolemTrait.HAULER) ? 2 : 1); ++a) {
            if (this.func_184582_a(EntityEquipmentSlot.values()[a]) == null) {
               this.func_184201_a(EntityEquipmentSlot.values()[a], stack);
               return null;
            }

            if (this.func_184582_a(EntityEquipmentSlot.values()[a]).field_77994_a < this.func_184582_a(EntityEquipmentSlot.values()[a]).func_77976_d() && ItemStack.func_179545_c(this.func_184582_a(EntityEquipmentSlot.values()[a]), stack) && ItemStack.func_77970_a(this.func_184582_a(EntityEquipmentSlot.values()[a]), stack)) {
               int d = Math.min(stack.field_77994_a, this.func_184582_a(EntityEquipmentSlot.values()[a]).func_77976_d() - this.func_184582_a(EntityEquipmentSlot.values()[a]).field_77994_a);
               stack.field_77994_a -= d;
               ItemStack var10000 = this.func_184582_a(EntityEquipmentSlot.values()[a]);
               var10000.field_77994_a += d;
               if (stack.field_77994_a <= 0) {
                  stack = null;
               }
            }
         }

         return stack;
      } else {
         return null;
      }
   }

   public ItemStack dropItem(ItemStack stack) {
      ItemStack out = null;

      for(int a = 0; a < (this.getProperties().hasTrait(EnumGolemTrait.HAULER) ? 2 : 1); ++a) {
         if (this.func_184582_a(EntityEquipmentSlot.values()[a]) != null) {
            if (stack == null) {
               out = this.func_184582_a(EntityEquipmentSlot.values()[a]).func_77946_l();
               this.func_184201_a(EntityEquipmentSlot.values()[a], (ItemStack)null);
            } else if (ItemStack.func_179545_c(this.func_184582_a(EntityEquipmentSlot.values()[a]), stack) && ItemStack.func_77970_a(this.func_184582_a(EntityEquipmentSlot.values()[a]), stack)) {
               out = this.func_184582_a(EntityEquipmentSlot.values()[a]).func_77946_l();
               out.field_77994_a = Math.min(stack.field_77994_a, out.field_77994_a);
               ItemStack var10000 = this.func_184582_a(EntityEquipmentSlot.values()[a]);
               var10000.field_77994_a -= stack.field_77994_a;
               if (this.func_184582_a(EntityEquipmentSlot.values()[a]).field_77994_a <= 0) {
                  this.func_184201_a(EntityEquipmentSlot.values()[a], (ItemStack)null);
               }
            }

            if (out != null) {
               break;
            }
         }
      }

      if (this.getProperties().hasTrait(EnumGolemTrait.HAULER) && this.func_184582_a(EntityEquipmentSlot.values()[0]) == null && this.func_184582_a(EntityEquipmentSlot.values()[1]) != null) {
         this.func_184201_a(EntityEquipmentSlot.values()[0], this.func_184582_a(EntityEquipmentSlot.values()[1]).func_77946_l());
         this.func_184201_a(EntityEquipmentSlot.values()[1], (ItemStack)null);
      }

      return out;
   }

   public boolean canCarry(ItemStack stack, boolean partial) {
      for(int a = 0; a < (this.getProperties().hasTrait(EnumGolemTrait.HAULER) ? 2 : 1); ++a) {
         if (this.func_184582_a(EntityEquipmentSlot.values()[a]) == null) {
            return true;
         }

         if (this.func_184582_a(EntityEquipmentSlot.values()[a]).field_77994_a < this.func_184582_a(EntityEquipmentSlot.values()[a]).func_77976_d() && ItemStack.func_179545_c(this.func_184582_a(EntityEquipmentSlot.values()[a]), stack) && ItemStack.func_77970_a(this.func_184582_a(EntityEquipmentSlot.values()[a]), stack)) {
            return true;
         }
      }

      return false;
   }

   public boolean isCarrying(ItemStack stack) {
      if (stack == null) {
         return false;
      } else {
         for(int a = 0; a < (this.getProperties().hasTrait(EnumGolemTrait.HAULER) ? 2 : 1); ++a) {
            if (this.func_184582_a(EntityEquipmentSlot.values()[a]) != null && this.func_184582_a(EntityEquipmentSlot.values()[a]).field_77994_a > 0 && ItemStack.func_179545_c(this.func_184582_a(EntityEquipmentSlot.values()[a]), stack) && ItemStack.func_77970_a(this.func_184582_a(EntityEquipmentSlot.values()[a]), stack)) {
               return true;
            }
         }

         return false;
      }
   }

   public ItemStack[] getCarrying() {
      return this.getProperties().hasTrait(EnumGolemTrait.HAULER) ? new ItemStack[]{this.func_184582_a(EntityEquipmentSlot.values()[0]), this.func_184582_a(EntityEquipmentSlot.values()[1])} : new ItemStack[]{this.func_184582_a(EntityEquipmentSlot.values()[0])};
   }

   public EntityLivingBase getGolemEntity() {
      return this;
   }

   public World getGolemWorld() {
      return this.func_130014_f_();
   }

   public void swingArm() {
      if (!this.field_82175_bq || this.field_110158_av >= 3 || this.field_110158_av < 0) {
         this.field_110158_av = -1;
         this.field_82175_bq = true;
         if (this.field_70170_p instanceof WorldServer) {
            ((WorldServer)this.field_70170_p).func_73039_n().func_151248_b(this, new SPacketAnimation(this, 0));
         }
      }

   }

   public void func_82196_d(EntityLivingBase target, float range) {
      if (this.getProperties().getArms().function != null) {
         this.getProperties().getArms().function.onRangedAttack(this, target, range);
      }

   }

   static {
      PROPS1 = EntityDataManager.func_187226_a(EntityThaumcraftGolem.class, DataSerializers.field_187192_b);
      PROPS2 = EntityDataManager.func_187226_a(EntityThaumcraftGolem.class, DataSerializers.field_187192_b);
      PROPS3 = EntityDataManager.func_187226_a(EntityThaumcraftGolem.class, DataSerializers.field_187192_b);
   }
}
