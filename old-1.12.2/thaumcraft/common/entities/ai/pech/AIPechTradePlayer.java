package thaumcraft.common.entities.ai.pech;

import net.minecraft.entity.ai.EntityAIBase;
import thaumcraft.common.entities.monster.EntityPech;

public class AIPechTradePlayer extends EntityAIBase {
   private EntityPech villager;

   public AIPechTradePlayer(EntityPech par1EntityVillager) {
      this.villager = par1EntityVillager;
      this.func_75248_a(5);
   }

   public boolean func_75250_a() {
      if (!this.villager.func_70089_S()) {
         return false;
      } else if (this.villager.func_70090_H()) {
         return false;
      } else if (!this.villager.isTamed()) {
         return false;
      } else if (!this.villager.field_70122_E) {
         return false;
      } else {
         return this.villager.field_70133_I ? false : this.villager.trading;
      }
   }

   public void func_75249_e() {
      this.villager.func_70661_as().func_75499_g();
   }

   public void func_75251_c() {
      this.villager.trading = false;
   }
}
