package thaumcraft.common.entities.construct.golem.ai;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.construct.golem.EntityThaumcraftGolem;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;

public class AIGotoBlock extends AIGoto {
   public AIGotoBlock(EntityThaumcraftGolem g) {
      super(g, (byte)0);
   }

   public void func_75246_d() {
      super.func_75246_d();
      if (this.golem.func_70671_ap() != null) {
         this.golem.func_70671_ap().func_75650_a((double)this.golem.getTask().getPos().func_177958_n() + 0.5D, (double)this.golem.getTask().getPos().func_177956_o() + 0.5D, (double)this.golem.getTask().getPos().func_177952_p() + 0.5D, 10.0F, (float)this.golem.func_70646_bf());
      }

   }

   protected void moveTo() {
      if (this.targetBlock != null) {
         this.golem.func_70661_as().func_75492_a((double)this.targetBlock.func_177958_n() + 0.5D, (double)this.targetBlock.func_177956_o() + 0.5D, (double)this.targetBlock.func_177952_p() + 0.5D, (double)this.golem.getGolemMoveSpeed());
      } else {
         this.golem.func_70661_as().func_75492_a((double)this.golem.getTask().getPos().func_177958_n() + 0.5D, (double)this.golem.getTask().getPos().func_177956_o() + 0.5D, (double)this.golem.getTask().getPos().func_177952_p() + 0.5D, (double)this.golem.getGolemMoveSpeed());
      }

   }

   protected boolean findDestination() {
      ArrayList<Task> list = TaskHandler.getBlockTasksSorted(this.golem.field_70170_p.field_73011_w.getDimension(), this.golem.func_110124_au(), this.golem);
      Iterator var2 = list.iterator();

      Task ticket;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         ticket = (Task)var2.next();
      } while(!this.areGolemTagsValidForTask(ticket) || !ticket.canGolemPerformTask(this.golem) || !this.golem.func_180485_d(ticket.getPos()) || !this.isValidDestination(this.golem.field_70170_p, ticket.getPos()) || !this.canEasilyReach(ticket.getPos()));

      this.targetBlock = this.getAdjacentSpace(ticket.getPos());
      this.golem.setTask(ticket);
      this.golem.getTask().setReserved(true);
      if (Config.showGolemEmotes) {
         this.golem.field_70170_p.func_72960_a(this.golem, (byte)5);
      }

      return true;
   }

   private BlockPos getAdjacentSpace(BlockPos pos) {
      double d = Double.MAX_VALUE;
      BlockPos closest = null;
      EnumFacing[] var5 = EnumFacing.field_176754_o;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         EnumFacing face = var5[var7];
         IBlockState block = this.golem.field_70170_p.func_180495_p(pos.func_177972_a(face));
         if (!block.func_185904_a().func_76230_c()) {
            double dist = pos.func_177972_a(face).func_177957_d(this.golem.field_70165_t, this.golem.field_70163_u, this.golem.field_70161_v);
            if (dist < d) {
               closest = pos.func_177972_a(face);
               d = dist;
            }
         }
      }

      return closest;
   }

   private boolean canEasilyReach(BlockPos pos) {
      if (this.golem.func_174831_c(pos) < this.minDist) {
         return true;
      } else {
         Path pathentity = this.golem.func_70661_as().func_75488_a((double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + 0.5D, (double)pos.func_177952_p() + 0.5D);
         if (pathentity == null) {
            return false;
         } else {
            PathPoint pathpoint = pathentity.func_75870_c();
            if (pathpoint == null) {
               return false;
            } else {
               int i = pathpoint.field_75839_a - MathHelper.func_76141_d((float)pos.func_177958_n());
               int j = pathpoint.field_75838_c - MathHelper.func_76141_d((float)pos.func_177952_p());
               int k = pathpoint.field_75837_b - MathHelper.func_76141_d((float)pos.func_177956_o());
               return (double)(i * i + j * j + k * k) < 2.25D;
            }
         }
      }
   }
}
