package thaumcraft.common.entities.monster.tainted;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thaumcraft.api.entities.ITaintedMob;

public class EntityTaintacleSmall extends EntityTaintacle implements ITaintedMob {
   int lifetime = 200;

   public EntityTaintacleSmall(World par1World) {
      super(par1World);
      this.func_70105_a(0.22F, 1.0F);
      this.field_70728_aV = 0;
   }

   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(5.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(2.0D);
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (this.lifetime-- <= 0) {
         this.func_70665_d(DamageSource.field_76376_m, 10.0F);
      }

   }

   public boolean func_70601_bi() {
      return false;
   }

   protected Item func_146068_u() {
      return Item.func_150899_d(0);
   }

   protected void func_70628_a(boolean flag, int i) {
   }
}
