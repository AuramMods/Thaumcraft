package thaumcraft.common.entities.monster.boss;

import java.util.List;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.entities.monster.tainted.EntityTaintacle;
import thaumcraft.common.lib.utils.EntityUtils;

public class EntityTaintacleGiant extends EntityTaintacle implements ITaintedMob, IEldritchMob {
   protected final BossInfoServer bossInfo;
   private static final DataParameter<Integer> AGGRO;

   public EntityTaintacleGiant(World par1World) {
      super(par1World);
      this.bossInfo = (BossInfoServer)(new BossInfoServer(this.func_145748_c_(), Color.PURPLE, Overlay.PROGRESS)).func_186741_a(true);
      this.func_70105_a(1.1F, 6.0F);
      this.field_70728_aV = 20;
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(175.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(9.0D);
   }

   public IEntityLivingData func_180482_a(DifficultyInstance diff, IEntityLivingData data) {
      EntityUtils.makeChampion(this, true);
      return data;
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.getAnger() > 0) {
         this.setAnger(this.getAnger() - 1);
      }

      if (this.field_70170_p.field_72995_K && this.field_70146_Z.nextInt(15) == 0 && this.getAnger() > 0) {
         double d0 = this.field_70146_Z.nextGaussian() * 0.02D;
         double d1 = this.field_70146_Z.nextGaussian() * 0.02D;
         double d2 = this.field_70146_Z.nextGaussian() * 0.02D;
         this.field_70170_p.func_175688_a(EnumParticleTypes.VILLAGER_ANGRY, this.field_70165_t + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N) - (double)this.field_70130_N / 2.0D, this.func_174813_aQ().field_72338_b + (double)this.field_70131_O + (double)this.field_70146_Z.nextFloat() * 0.5D, this.field_70161_v + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N) - (double)this.field_70130_N / 2.0D, d0, d1, d2, new int[0]);
      }

      if (!this.field_70170_p.field_72995_K && this.field_70173_aa % 30 == 0) {
         this.func_70691_i(1.0F);
      }

   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.func_184212_Q().func_187214_a(AGGRO, 0);
   }

   public int getAnger() {
      return (Integer)this.func_184212_Q().func_187225_a(AGGRO);
   }

   public void setAnger(int par1) {
      this.func_184212_Q().func_187227_b(AGGRO, par1);
   }

   public boolean func_70601_bi() {
      return false;
   }

   protected void func_70628_a(boolean flag, int i) {
      List<EntityTaintacleGiant> ents = EntityUtils.getEntitiesInRange(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, this, EntityTaintacleGiant.class, 48.0D);
      if (ents == null || ents.size() <= 0) {
         EntityUtils.entityDropSpecialItem(this, new ItemStack(ItemsTC.primordialPearl, 1 + this.field_70146_Z.nextInt(2)), this.field_70131_O / 2.0F);
      }

   }

   protected boolean func_70692_ba() {
      return false;
   }

   public boolean func_70648_aU() {
      return true;
   }

   protected int func_70682_h(int air) {
      return air;
   }

   public boolean func_70097_a(DamageSource source, float damage) {
      if (!this.field_70170_p.field_72995_K && damage > 35.0F) {
         if (this.getAnger() == 0) {
            try {
               this.func_70690_d(new PotionEffect(MobEffects.field_76428_l, 200, (int)(damage / 15.0F)));
               this.func_70690_d(new PotionEffect(MobEffects.field_76420_g, 200, (int)(damage / 10.0F)));
               this.func_70690_d(new PotionEffect(MobEffects.field_76422_e, 200, (int)(damage / 40.0F)));
               this.setAnger(200);
            } catch (Exception var4) {
            }

            if (source.func_76346_g() != null && source.func_76346_g() instanceof EntityPlayer) {
               ((EntityPlayer)source.func_76346_g()).func_145747_a(new TextComponentTranslation(this.func_70005_c_() + " " + I18n.func_74838_a("tc.boss.enrage"), new Object[0]));
            }
         }

         damage = 35.0F;
      }

      return super.func_70097_a(source, damage);
   }

   protected void func_70619_bc() {
      super.func_70619_bc();
      this.bossInfo.func_186735_a(this.func_110143_aJ() / this.func_110138_aP());
   }

   public void func_184203_c(EntityPlayerMP player) {
      super.func_184203_c(player);
      this.bossInfo.func_186760_a(player);
   }

   public void func_184178_b(EntityPlayerMP player) {
      super.func_184178_b(player);
      this.bossInfo.func_186761_b(player);
   }

   public boolean func_184222_aU() {
      return false;
   }

   static {
      AGGRO = EntityDataManager.func_187226_a(EntityThaumcraftBoss.class, DataSerializers.field_187192_b);
   }
}
