package thaumcraft.common.entities.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.init.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityGiantBrainyZombie extends EntityBrainyZombie {
   private static final DataParameter<Float> ANGER;

   public EntityGiantBrainyZombie(World world) {
      super(world);
      this.field_70728_aV = 15;
      this.field_70714_bg.func_75776_a(2, new EntityAILeapAtTarget(this, 0.4F));
   }

   public void func_70636_d() {
      super.func_70636_d();
      if (this.getAnger() > 1.0F) {
         this.setAnger(this.getAnger() - 0.002F);
         this.func_70105_a(0.6F + this.getAnger() * 0.6F, 1.95F + this.getAnger() * 1.95F);
         this.func_146069_a(1.0F);
      }

      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a((double)(7.0F + (this.getAnger() - 1.0F) * 5.0F));
   }

   public float func_70047_e() {
      float f = 1.74F + this.getAnger() * 1.74F;
      if (this.func_70631_g_()) {
         f = (float)((double)f - 0.81D);
      }

      return f;
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.func_184212_Q().func_187214_a(ANGER, 0.0F);
   }

   public float getAnger() {
      return (Float)this.func_184212_Q().func_187225_a(ANGER);
   }

   public void setAnger(float par1) {
      this.func_184212_Q().func_187227_b(ANGER, par1);
   }

   public boolean func_70097_a(DamageSource par1DamageSource, float par2) {
      this.setAnger(Math.min(2.0F, this.getAnger() + 0.1F));
      return super.func_70097_a(par1DamageSource, par2);
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(60.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(7.0D);
   }

   protected void func_70628_a(boolean flag, int i) {
      int a;
      for(a = 0; a < 6; ++a) {
         if (this.field_70170_p.field_73012_v.nextBoolean()) {
            this.func_145779_a(Items.field_151078_bh, 2);
         }
      }

      for(a = 0; a < 6; ++a) {
         if (this.field_70170_p.field_73012_v.nextBoolean()) {
            this.func_145779_a(Items.field_151078_bh, 2);
         }
      }

   }

   static {
      ANGER = EntityDataManager.func_187226_a(EntityGiantBrainyZombie.class, DataSerializers.field_187193_c);
   }
}
