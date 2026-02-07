package thaumcraft.common.entities.construct.golem.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;
import thaumcraft.common.entities.construct.golem.EntityThaumcraftGolem;

public class AIGotoHome extends EntityAIBase {
   protected EntityThaumcraftGolem golem;
   private double movePosX;
   private double movePosY;
   private double movePosZ;
   protected int idleCounter = 10;

   public AIGotoHome(EntityThaumcraftGolem g) {
      this.golem = g;
      this.func_75248_a(5);
   }

   public boolean func_75250_a() {
      if (this.idleCounter > 0) {
         --this.idleCounter;
         return false;
      } else {
         this.idleCounter = 50;
         double dd = this.golem.func_174831_c(this.golem.func_180486_cf());
         if (dd < 9.0D) {
            return false;
         } else if (dd > 256.0D) {
            Vec3d vec3 = RandomPositionGenerator.func_75464_a(this.golem, 16, 7, new Vec3d((double)this.golem.func_180486_cf().func_177958_n(), (double)this.golem.func_180486_cf().func_177956_o(), (double)this.golem.func_180486_cf().func_177952_p()));
            if (vec3 == null) {
               return false;
            } else {
               this.movePosX = vec3.field_72450_a;
               this.movePosY = vec3.field_72448_b;
               this.movePosZ = vec3.field_72449_c;
               return true;
            }
         } else {
            this.movePosX = (double)this.golem.func_180486_cf().func_177958_n();
            this.movePosY = (double)this.golem.func_180486_cf().func_177956_o();
            this.movePosZ = (double)this.golem.func_180486_cf().func_177952_p();
            return true;
         }
      }
   }

   public void func_75249_e() {
      this.golem.func_70661_as().func_75492_a(this.movePosX, this.movePosY, this.movePosZ, (double)this.golem.getGolemMoveSpeed());
   }

   public boolean func_75253_b() {
      return this.golem.getTask() != null && !this.golem.func_70661_as().func_75500_f() && this.golem.func_174831_c(this.golem.func_180486_cf()) > 3.0D;
   }

   public void func_75251_c() {
      this.idleCounter = 50;
   }
}
