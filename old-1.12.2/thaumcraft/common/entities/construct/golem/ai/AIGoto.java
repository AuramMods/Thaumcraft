package thaumcraft.common.entities.construct.golem.ai;

import java.util.Arrays;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.construct.golem.EntityThaumcraftGolem;
import thaumcraft.common.entities.construct.golem.seals.SealHandler;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;

public abstract class AIGoto extends EntityAIBase {
   protected EntityThaumcraftGolem golem;
   protected int taskCounter = -1;
   protected byte type = 0;
   protected int cooldown;
   protected double minDist = 4.0D;
   private BlockPos prevRamble;
   protected BlockPos targetBlock;
   int pause = 0;

   public AIGoto(EntityThaumcraftGolem g, byte type) {
      this.golem = g;
      this.type = type;
      this.func_75248_a(5);
   }

   public boolean func_75250_a() {
      if (this.cooldown > 0) {
         --this.cooldown;
         return false;
      } else {
         this.cooldown = 5;
         if (this.golem.getTask() != null && !this.golem.getTask().isSuspended()) {
            return false;
         } else {
            this.targetBlock = null;
            boolean start = this.findDestination();
            if (start && this.golem.getTask() != null && this.golem.getTask().getSealPos() != null) {
               ISealEntity se = GolemHelper.getSealEntity(this.golem.field_70170_p.field_73011_w.getDimension(), this.golem.getTask().getSealPos());
               if (se != null) {
                  se.getSeal().onTaskStarted(this.golem.field_70170_p, this.golem, this.golem.getTask());
               }
            }

            return start;
         }
      }
   }

   public void func_75249_e() {
      this.moveTo();
      this.taskCounter = 0;
   }

   protected abstract void moveTo();

   public boolean func_75253_b() {
      return this.taskCounter >= 0 && this.taskCounter <= 1000 && this.golem.getTask() != null && !this.golem.getTask().isSuspended() && this.isValidDestination(this.golem.field_70170_p, this.golem.getTask().getPos());
   }

   public void func_75246_d() {
      if (this.golem.getTask() != null) {
         if (this.pause-- <= 0) {
            double dist = this.golem.getTask().getType() == 0 ? this.golem.func_174831_c(this.targetBlock == null ? this.golem.getTask().getPos() : this.targetBlock) : this.golem.func_70068_e(this.golem.getTask().getEntity());
            if (dist > this.minDist) {
               this.golem.getTask().setCompletion(false);
               ++this.taskCounter;
               if (this.taskCounter % 40 == 0) {
                  if (this.prevRamble != null && this.prevRamble.equals(this.golem.func_180425_c())) {
                     Vec3d vec3 = RandomPositionGenerator.func_75464_a(this.golem, 6, 4, new Vec3d((double)this.golem.getTask().getPos().func_177958_n(), (double)this.golem.getTask().getPos().func_177956_o(), (double)this.golem.getTask().getPos().func_177952_p()));
                     if (vec3 != null) {
                        this.golem.func_70661_as().func_75492_a(vec3.field_72450_a + 0.5D, vec3.field_72448_b + 0.5D, vec3.field_72449_c + 0.5D, (double)this.golem.getGolemMoveSpeed());
                     }
                  } else {
                     this.moveTo();
                  }

                  this.prevRamble = this.golem.func_180425_c();
               }
            } else {
               TaskHandler.completeTask(this.golem.getTask(), this.golem);
               if (this.golem.getTask() != null && this.golem.getTask().isCompleted()) {
                  if (this.taskCounter >= 0) {
                     this.taskCounter = 0;
                  }

                  this.pause = 0;
               } else {
                  this.pause = 10;
                  ++this.taskCounter;
               }

               --this.taskCounter;
            }
         }

      }
   }

   public void func_75251_c() {
      if (this.golem.getTask() != null) {
         if (!this.golem.getTask().isCompleted() && this.golem.getTask().isReserved() && Config.showGolemEmotes) {
            this.golem.field_70170_p.func_72960_a(this.golem, (byte)6);
         }

         if (this.golem.getTask().isCompleted() && !this.golem.getTask().isSuspended()) {
            this.golem.getTask().setSuspended(true);
         }

         this.golem.getTask().setReserved(false);
      }

   }

   protected abstract boolean findDestination();

   protected boolean isValidDestination(World world, BlockPos pos) {
      return true;
   }

   protected boolean areGolemTagsValidForTask(Task ticket) {
      ISealEntity se = SealHandler.getSealEntity(this.golem.field_70170_p.field_73011_w.getDimension(), ticket.getSealPos());
      if (se != null && se.getSeal() != null) {
         if (se.isLocked() && !this.golem.func_184753_b().equals(se.getOwner())) {
            return false;
         } else if (se.getSeal().getRequiredTags() != null && !this.golem.getProperties().getTraits().containsAll(Arrays.asList(se.getSeal().getRequiredTags()))) {
            return false;
         } else {
            if (se.getSeal().getForbiddenTags() != null) {
               EnumGolemTrait[] var3 = se.getSeal().getForbiddenTags();
               int var4 = var3.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  EnumGolemTrait tag = var3[var5];
                  if (this.golem.getProperties().getTraits().contains(tag)) {
                     return false;
                  }
               }
            }

            return true;
         }
      } else {
         return true;
      }
   }
}
