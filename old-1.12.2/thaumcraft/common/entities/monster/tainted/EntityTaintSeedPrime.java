package thaumcraft.common.entities.monster.tainted;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;
import thaumcraft.common.config.ConfigItems;

public class EntityTaintSeedPrime extends EntityTaintSeed {
   public EntityTaintSeedPrime(World par1World) {
      super(par1World);
      this.func_70105_a(3.0F, 2.0F);
      this.field_70728_aV = 12;
   }

   protected int getArea() {
      return 2;
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(150.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(7.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.0D);
   }

   protected void func_70628_a(boolean flag, int i) {
      this.func_70099_a(ConfigItems.FLUX_CRYSTAL.func_77946_l(), this.field_70131_O / 2.0F);
      if (this.field_70146_Z.nextBoolean()) {
         this.func_70099_a(ConfigItems.FLUX_CRYSTAL.func_77946_l(), this.field_70131_O / 2.0F);
      }

      if (this.field_70146_Z.nextBoolean()) {
         this.func_70099_a(ConfigItems.FLUX_CRYSTAL.func_77946_l(), this.field_70131_O / 2.0F);
      }

   }
}
