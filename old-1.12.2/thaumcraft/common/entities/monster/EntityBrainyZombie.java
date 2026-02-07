package thaumcraft.common.entities.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.items.ItemsTC;

public class EntityBrainyZombie extends EntityZombie {
   protected void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(25.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(5.0D);
      this.func_110148_a(field_110186_bp).func_111128_a(0.0D);
   }

   public EntityBrainyZombie(World world) {
      super(world);
      this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, false, new Class[0]));
   }

   public int func_70658_aO() {
      int var1 = super.func_70658_aO() + 3;
      if (var1 > 20) {
         var1 = 20;
      }

      return var1;
   }

   protected void func_70628_a(boolean flag, int i) {
      if (this.field_70170_p.field_73012_v.nextInt(10) - i <= 4) {
         this.func_70099_a(new ItemStack(ItemsTC.brain), 1.5F);
      }

      super.func_70628_a(flag, i);
   }
}
