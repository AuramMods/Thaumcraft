package thaumcraft.common.entities.construct.golem.ai;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.MathHelper;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.construct.golem.EntityThaumcraftGolem;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;

public class AIGotoEntity extends AIGoto {
   public AIGotoEntity(EntityThaumcraftGolem g) {
      super(g, (byte)1);
   }

   public void func_75246_d() {
      super.func_75246_d();
      if (this.golem.func_70671_ap() != null) {
         this.golem.func_70671_ap().func_75651_a(this.golem.getTask().getEntity(), 10.0F, (float)this.golem.func_70646_bf());
      }

   }

   protected void moveTo() {
      this.golem.func_70661_as().func_75497_a(this.golem.getTask().getEntity(), (double)this.golem.getGolemMoveSpeed());
   }

   protected boolean findDestination() {
      ArrayList<Task> list = TaskHandler.getEntityTasksSorted(this.golem.field_70170_p.field_73011_w.getDimension(), this.golem.func_110124_au(), this.golem);
      Iterator var2 = list.iterator();

      Task ticket;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         ticket = (Task)var2.next();
      } while(!this.areGolemTagsValidForTask(ticket) || !ticket.canGolemPerformTask(this.golem) || !this.golem.func_180485_d(ticket.getEntity().func_180425_c()) || !this.isValidDestination(this.golem.field_70170_p, ticket.getEntity().func_180425_c()) || !this.canEasilyReach(ticket.getEntity()));

      this.golem.setTask(ticket);
      this.golem.getTask().setReserved(true);
      this.minDist = 3.5D + (double)(this.golem.getTask().getEntity().field_70130_N / 2.0F * (this.golem.getTask().getEntity().field_70130_N / 2.0F));
      if (Config.showGolemEmotes) {
         this.golem.field_70170_p.func_72960_a(this.golem, (byte)5);
      }

      return true;
   }

   private boolean canEasilyReach(Entity e) {
      if (this.golem.func_70068_e(e) < this.minDist) {
         return true;
      } else {
         Path pathentity = this.golem.func_70661_as().func_75494_a(e);
         if (pathentity == null) {
            return false;
         } else {
            PathPoint pathpoint = pathentity.func_75870_c();
            if (pathpoint == null) {
               return false;
            } else {
               int i = pathpoint.field_75839_a - MathHelper.func_76128_c(e.field_70165_t);
               int j = pathpoint.field_75838_c - MathHelper.func_76128_c(e.field_70161_v);
               return (double)(i * i + j * j) < this.minDist;
            }
         }
      }
   }
}
