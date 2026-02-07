package thaumcraft.common.entities.monster.tainted;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.world.biomes.BiomeHandler;

public class EntityTaintacle extends EntityMob implements ITaintedMob {
   public float flailIntensity = 1.0F;

   public EntityTaintacle(World par1World) {
      super(par1World);
      this.func_70105_a(0.8F, 3.0F);
      this.field_70728_aV = 8;
   }

   protected void func_184651_r() {
      this.field_70714_bg.func_75776_a(1, new EntityAIAttackMelee(this, 1.0D, false));
      this.field_70714_bg.func_75776_a(2, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.field_70714_bg.func_75776_a(3, new EntityAILookIdle(this));
      this.field_70715_bh.func_75776_a(0, new EntityAIHurtByTarget(this, false, new Class[0]));
      this.field_70715_bh.func_75776_a(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
   }

   public boolean func_70686_a(Class clazz) {
      return !ITaintedMob.class.isAssignableFrom(clazz);
   }

   public boolean func_184191_r(Entity otherEntity) {
      return otherEntity instanceof ITaintedMob || super.func_184191_r(otherEntity);
   }

   public boolean func_70601_bi() {
      boolean onTaint = this.field_70170_p.func_180495_p(this.func_180425_c()).func_185904_a() == ThaumcraftMaterials.MATERIAL_TAINT || this.field_70170_p.func_180495_p(this.func_180425_c().func_177977_b()).func_185904_a() == ThaumcraftMaterials.MATERIAL_TAINT;
      return onTaint && this.field_70170_p.func_175659_aa() != EnumDifficulty.PEACEFUL;
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(50.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(7.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.0D);
   }

   public void func_70091_d(double par1, double par3, double par5) {
      par1 = 0.0D;
      par5 = 0.0D;
      if (par3 > 0.0D) {
         par3 = 0.0D;
      }

      super.func_70091_d(par1, par3, par5);
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.field_70170_p.field_72995_K && this.field_70173_aa % 20 == 0) {
         boolean onTaint = this.field_70170_p.func_180495_p(this.func_180425_c()).func_185904_a() == ThaumcraftMaterials.MATERIAL_TAINT || this.field_70170_p.func_180495_p(this.func_180425_c().func_177977_b()).func_185904_a() == ThaumcraftMaterials.MATERIAL_TAINT;
         if (!onTaint) {
            this.func_70665_d(DamageSource.field_76366_f, 1.0F);
         }

         if (!(this instanceof EntityTaintacleSmall) && this.field_70173_aa % 40 == 0 && this.func_70638_az() != null && this.func_70068_e(this.func_70638_az()) > 16.0D && this.func_70068_e(this.func_70638_az()) < 256.0D && this.func_70635_at().func_75522_a(this.func_70638_az())) {
            this.spawnTentacles(this.func_70638_az());
         }
      }

      if (this.field_70170_p.field_72995_K) {
         if (this.flailIntensity > 1.0F) {
            this.flailIntensity -= 0.01F;
         }

         if ((float)this.field_70173_aa < this.field_70131_O * 10.0F && this.field_70122_E) {
            FXDispatcher.INSTANCE.tentacleAriseFX(this);
         }
      }

   }

   protected void spawnTentacles(Entity entity) {
      if (this.field_70170_p.func_180494_b(entity.func_180425_c()) == BiomeHandler.ELDRITCH || this.field_70170_p.func_180495_p(entity.func_180425_c()).func_185904_a() == ThaumcraftMaterials.MATERIAL_TAINT || this.field_70170_p.func_180495_p(entity.func_180425_c().func_177977_b()).func_185904_a() == ThaumcraftMaterials.MATERIAL_TAINT) {
         EntityTaintacleSmall taintlet = new EntityTaintacleSmall(this.field_70170_p);
         taintlet.func_70012_b(entity.field_70165_t + (double)this.field_70170_p.field_73012_v.nextFloat() - (double)this.field_70170_p.field_73012_v.nextFloat(), entity.field_70163_u, entity.field_70161_v + (double)this.field_70170_p.field_73012_v.nextFloat() - (double)this.field_70170_p.field_73012_v.nextFloat(), 0.0F, 0.0F);
         this.field_70170_p.func_72838_d(taintlet);
         this.func_184185_a(SoundsTC.tentacle, this.func_70599_aP(), this.func_70647_i());
         if (this.field_70170_p.func_180494_b(entity.func_180425_c()) == BiomeHandler.ELDRITCH && this.field_70170_p.func_175623_d(entity.func_180425_c()) && BlockUtils.isAdjacentToSolidBlock(this.field_70170_p, entity.func_180425_c())) {
            this.field_70170_p.func_175656_a(entity.func_180425_c(), BlocksTC.taintFibre.func_176223_P());
         }
      }

   }

   public void faceEntity(Entity par1Entity, float par2) {
      double d0 = par1Entity.field_70165_t - this.field_70165_t;
      double d1 = par1Entity.field_70161_v - this.field_70161_v;
      float f2 = (float)(Math.atan2(d1, d0) * 180.0D / 3.141592653589793D) - 90.0F;
      this.field_70177_z = this.func_70663_b(this.field_70177_z, f2, par2);
   }

   protected float func_70663_b(float par1, float par2, float par3) {
      float f3 = MathHelper.func_76142_g(par2 - par1);
      if (f3 > par3) {
         f3 = par3;
      }

      if (f3 < -par3) {
         f3 = -par3;
      }

      return par1 + f3;
   }

   public int func_70627_aG() {
      return 200;
   }

   protected SoundEvent func_184639_G() {
      return SoundsTC.roots;
   }

   protected float func_70647_i() {
      return 1.3F - this.field_70131_O / 10.0F;
   }

   protected float func_70599_aP() {
      return this.field_70131_O / 8.0F;
   }

   protected SoundEvent func_184601_bQ() {
      return SoundsTC.tentacle;
   }

   protected SoundEvent func_184615_bR() {
      return SoundsTC.tentacle;
   }

   protected Item func_146068_u() {
      return Item.func_150899_d(0);
   }

   protected void func_70628_a(boolean flag, int i) {
      this.func_70099_a(ConfigItems.FLUX_CRYSTAL.func_77946_l(), this.field_70131_O / 2.0F);
   }

   @SideOnly(Side.CLIENT)
   public void func_70103_a(byte par1) {
      if (par1 == 16) {
         this.flailIntensity = 3.0F;
      } else {
         super.func_70103_a(par1);
      }

   }

   public boolean func_70652_k(Entity p_70652_1_) {
      this.field_70170_p.func_72960_a(this, (byte)16);
      this.func_184185_a(SoundsTC.tentacle, this.func_70599_aP(), this.func_70647_i());
      return super.func_70652_k(p_70652_1_);
   }
}
