package thaumcraft.common.entities.monster.tainted;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
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
import thaumcraft.api.potions.PotionFluxTaint;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.world.taint.TaintHelper;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.world.biomes.BiomeHandler;

public class EntityTaintSeed extends EntityMob implements ITaintedMob {
   boolean firstRun = false;
   public float attackAnim = 0.0F;

   public EntityTaintSeed(World par1World) {
      super(par1World);
      this.func_70105_a(2.0F, 1.25F);
      this.field_70728_aV = 8;
   }

   protected int getArea() {
      return 1;
   }

   protected void func_184651_r() {
      this.field_70714_bg.func_75776_a(1, new EntityAIAttackMelee(this, 1.0D, false));
      this.field_70715_bh.func_75776_a(0, new EntityAIHurtByTarget(this, false, new Class[0]));
      this.field_70715_bh.func_75776_a(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
   }

   public boolean func_70652_k(Entity p_70652_1_) {
      this.field_70170_p.func_72960_a(this, (byte)16);
      this.func_184185_a(SoundsTC.tentacle, this.func_70599_aP(), this.func_70647_i());
      return super.func_70652_k(p_70652_1_);
   }

   @SideOnly(Side.CLIENT)
   public void func_70103_a(byte par1) {
      if (par1 == 16) {
         this.attackAnim = 0.5F;
      } else {
         super.func_70103_a(par1);
      }

   }

   public boolean func_70686_a(Class clazz) {
      return !ITaintedMob.class.isAssignableFrom(clazz);
   }

   public boolean func_184191_r(Entity otherEntity) {
      return otherEntity instanceof ITaintedMob || super.func_184191_r(otherEntity);
   }

   public boolean func_70601_bi() {
      return this.field_70170_p.func_175659_aa() != EnumDifficulty.PEACEFUL && this.func_70058_J() && EntityUtils.getEntitiesInRange(this.func_130014_f_(), this.func_180425_c(), (Entity)null, EntityTaintSeed.class, (double)Config.taintSpreadArea * 0.9D).size() <= 0;
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(75.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(4.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.0D);
   }

   public void func_70645_a(DamageSource cause) {
      TaintHelper.removeTaintSeed(this.func_130014_f_(), this.func_180425_c());
      super.func_70645_a(cause);
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.field_70170_p.field_72995_K) {
         if (!this.firstRun) {
            TaintHelper.addTaintSeed(this.func_130014_f_(), this.func_180425_c());
            this.firstRun = true;
         }

         if (this.field_70173_aa % 20 == 0) {
            TaintHelper.spreadFibres(this.field_70170_p, this.func_180425_c().func_177982_a(MathHelper.func_76136_a(this.func_70681_au(), -this.getArea() * 3, this.getArea() * 3), MathHelper.func_76136_a(this.func_70681_au(), -this.getArea(), this.getArea()), MathHelper.func_76136_a(this.func_70681_au(), -this.getArea() * 3, this.getArea() * 3)), true);
            if (this.func_70638_az() != null && this.func_70068_e(this.func_70638_az()) < (double)(this.getArea() * 256) && this.func_70635_at().func_75522_a(this.func_70638_az())) {
               this.spawnTentacles(this.func_70638_az());
            }

            List<EntityLivingBase> list = EntityUtils.getEntitiesInRange(this.func_130014_f_(), this.func_180425_c(), this, EntityLivingBase.class, (double)(this.getArea() * 4));
            Iterator var2 = list.iterator();

            while(var2.hasNext()) {
               EntityLivingBase elb = (EntityLivingBase)var2.next();
               elb.func_70690_d(new PotionEffect(PotionFluxTaint.instance, 200, this.getArea(), false, true));
            }
         }
      } else {
         if (this.attackAnim > 0.0F) {
            this.attackAnim *= 0.75F;
         }

         if ((double)this.attackAnim < 0.001D) {
            this.attackAnim = 0.0F;
         }

         for(int i = 0; i <= FXDispatcher.INSTANCE.particleCount(2) - 1; ++i) {
            FXDispatcher.INSTANCE.drawTaintParticles((float)this.field_70165_t, (float)this.field_70163_u + this.field_70131_O, (float)this.field_70161_v, (float)this.field_70146_Z.nextGaussian() * 0.05F, 0.025F * this.field_70146_Z.nextFloat(), (float)this.field_70146_Z.nextGaussian() * 0.05F, 4.0F);
         }

         if ((double)this.field_70146_Z.nextFloat() < 0.033D) {
            FXDispatcher.INSTANCE.drawLightningFlash((double)((float)this.field_70165_t), (double)((float)this.field_70163_u + this.field_70131_O), (double)((float)this.field_70161_v), 0.7F, 0.1F, 0.9F, 0.5F, 1.5F + this.field_70146_Z.nextFloat());
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

   public boolean func_70104_M() {
      return false;
   }

   public boolean func_70067_L() {
      return true;
   }

   public void func_70060_a(float strafe, float forward, float friction) {
   }

   public void func_70091_d(double par1, double par3, double par5) {
      par1 = 0.0D;
      par5 = 0.0D;
      if (par3 > 0.0D) {
         par3 = 0.0D;
      }

      super.func_70091_d(par1, par3, par5);
   }

   protected int func_70682_h(int air) {
      return air;
   }

   public boolean func_70648_aU() {
      return true;
   }

   protected boolean func_70692_ba() {
      return false;
   }
}
