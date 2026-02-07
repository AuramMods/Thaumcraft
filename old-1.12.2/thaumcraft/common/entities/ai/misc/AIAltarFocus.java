package thaumcraft.common.entities.ai.misc;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.entities.monster.cult.EntityCultistCleric;

public class AIAltarFocus extends EntityAIBase {
   private EntityCultistCleric entity;
   private World world;
   int field_48399_a = 0;

   public AIAltarFocus(EntityCultistCleric par1EntityLiving) {
      this.entity = par1EntityLiving;
      this.world = par1EntityLiving.field_70170_p;
      this.func_75248_a(7);
   }

   public boolean func_75250_a() {
      return this.entity.getIsRitualist() && this.entity.func_180486_cf() != null;
   }

   public void func_75249_e() {
   }

   public void func_75251_c() {
   }

   public boolean func_75253_b() {
      return this.entity.getIsRitualist() && this.entity.func_180486_cf() != null;
   }

   public void func_75246_d() {
      if (this.entity.func_180486_cf() != null && this.entity.field_70173_aa % 40 == 0 && (this.entity.func_180486_cf().func_177954_c(this.entity.field_70165_t, this.entity.field_70163_u, this.entity.field_70161_v) > 16.0D || this.world.func_180495_p(this.entity.func_180486_cf()).func_177230_c() != BlocksTC.eldritch)) {
         this.entity.setIsRitualist(false);
      }

   }
}
