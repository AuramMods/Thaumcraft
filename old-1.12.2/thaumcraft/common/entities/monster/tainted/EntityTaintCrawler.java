package thaumcraft.common.entities.monster.tainted;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.api.potions.PotionFluxTaint;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.world.taint.BlockTaintFibre;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.utils.BlockUtils;

public class EntityTaintCrawler extends EntityMob implements ITaintedMob {
   BlockPos lastPos = new BlockPos(0, 0, 0);

   public EntityTaintCrawler(World par1World) {
      super(par1World);
      this.func_70105_a(0.4F, 0.3F);
      this.field_70728_aV = 3;
   }

   protected void func_184651_r() {
      this.field_70714_bg.func_75776_a(1, new EntityAISwimming(this));
      this.field_70714_bg.func_75776_a(2, new EntityAIAttackMelee(this, 1.0D, false));
      this.field_70714_bg.func_75776_a(3, new EntityAIWander(this, 1.0D));
      this.field_70714_bg.func_75776_a(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.field_70714_bg.func_75776_a(8, new EntityAILookIdle(this));
      this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, true, new Class[0]));
      this.field_70715_bh.func_75776_a(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
   }

   public boolean func_70686_a(Class clazz) {
      return !ITaintedMob.class.isAssignableFrom(clazz);
   }

   public boolean func_184191_r(Entity otherEntity) {
      return otherEntity instanceof ITaintedMob || super.func_184191_r(otherEntity);
   }

   public float func_70047_e() {
      return 0.1F;
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(8.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.275D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(2.0D);
   }

   protected float func_70647_i() {
      return 0.7F;
   }

   protected SoundEvent func_184639_G() {
      return SoundEvents.field_187793_eY;
   }

   protected SoundEvent func_184601_bQ() {
      return SoundEvents.field_187850_fa;
   }

   protected SoundEvent func_184615_bR() {
      return SoundEvents.field_187795_eZ;
   }

   protected void func_180429_a(BlockPos p_180429_1_, Block p_180429_2_) {
      this.func_184185_a(SoundEvents.field_187852_fb, 0.15F, 1.0F);
   }

   protected boolean func_70041_e_() {
      return false;
   }

   public void func_70071_h_() {
      if (this.field_70170_p.field_72995_K && this.field_70146_Z.nextFloat() < 0.05F) {
         FXDispatcher.INSTANCE.drawPollutionParticles(this.func_180425_c());
      }

      if (!this.field_70170_p.field_72995_K && this.field_70173_aa % 5 == 0 && this.lastPos != this.func_180425_c()) {
         this.lastPos = this.func_180425_c();
         IBlockState bs = this.field_70170_p.func_180495_p(this.func_180425_c());
         Material bm = bs.func_185904_a();
         if (!bs.func_177230_c().isLeaves(bs, this.field_70170_p, this.func_180425_c()) && !bm.func_76224_d() && bm != ThaumcraftMaterials.MATERIAL_TAINT && (this.field_70170_p.func_175623_d(this.func_180425_c()) || bs.func_177230_c().func_176200_f(this.field_70170_p, this.func_180425_c()) || bs.func_177230_c() instanceof BlockFlower || bs.func_177230_c() instanceof IPlantable) && BlockUtils.isAdjacentToSolidBlock(this.field_70170_p, this.func_180425_c()) && !BlockTaintFibre.isOnlyAdjacentToTaint(this.field_70170_p, this.func_180425_c())) {
            this.field_70170_p.func_175656_a(this.func_180425_c(), BlocksTC.taintFibre.func_176223_P());
         }
      }

      super.func_70071_h_();
   }

   protected boolean func_70814_o() {
      return true;
   }

   public EnumCreatureAttribute func_70668_bt() {
      return EnumCreatureAttribute.ARTHROPOD;
   }

   protected Item func_146068_u() {
      return Item.func_150899_d(0);
   }

   protected void func_70628_a(boolean flag, int i) {
      if (this.field_70170_p.field_73012_v.nextInt(8) == 0) {
         this.func_70099_a(ConfigItems.FLUX_CRYSTAL.func_77946_l(), this.field_70131_O / 2.0F);
      }

   }

   public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_) {
      return p_180482_2_;
   }

   public boolean func_70652_k(Entity victim) {
      if (super.func_70652_k(victim)) {
         if (victim instanceof EntityLivingBase) {
            byte b0 = 0;
            if (this.field_70170_p.func_175659_aa() == EnumDifficulty.NORMAL) {
               b0 = 3;
            } else if (this.field_70170_p.func_175659_aa() == EnumDifficulty.HARD) {
               b0 = 6;
            }

            if (b0 > 0 && this.field_70146_Z.nextInt(b0 + 1) > 2) {
               ((EntityLivingBase)victim).func_70690_d(new PotionEffect(PotionFluxTaint.instance, b0 * 20, 0));
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
