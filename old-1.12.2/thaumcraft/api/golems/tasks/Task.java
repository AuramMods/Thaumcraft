package thaumcraft.api.golems.tasks;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.SealPos;

public class Task {
   private UUID golemUUID;
   private int id;
   private byte type;
   private SealPos sealPos;
   private BlockPos pos;
   private Entity entity;
   private boolean reserved;
   private boolean suspended;
   private boolean completed;
   private int data;
   private short lifespan;
   private byte priority = 0;

   private Task() {
   }

   public Task(SealPos sealPos, BlockPos pos) {
      this.sealPos = sealPos;
      this.pos = pos;
      if (sealPos == null) {
         this.id = (System.currentTimeMillis() + "/BNPOS/" + pos.toString()).hashCode();
      } else {
         this.id = (System.currentTimeMillis() + "/B/" + sealPos.face.toString() + "/" + sealPos.pos.toString() + "/" + pos.toString()).hashCode();
      }

      this.type = 0;
      this.lifespan = 300;
   }

   public Task(SealPos sealPos, Entity entity) {
      this.sealPos = sealPos;
      this.entity = entity;
      if (sealPos == null) {
         this.id = (System.currentTimeMillis() + "/ENPOS/" + this.pos.toString()).hashCode();
      } else {
         this.id = (System.currentTimeMillis() + "/E/" + sealPos.face.toString() + "/" + sealPos.pos.toString() + "/" + entity.func_145782_y()).hashCode();
      }

      this.type = 1;
      this.lifespan = 300;
   }

   public byte getPriority() {
      return this.priority;
   }

   public void setPriority(byte priority) {
      this.priority = priority;
   }

   public boolean isCompleted() {
      return this.completed;
   }

   public void setCompletion(boolean fulfilled) {
      this.completed = fulfilled;
      ++this.lifespan;
   }

   public UUID getGolemUUID() {
      return this.golemUUID;
   }

   public void setGolemUUID(UUID golemUUID) {
      this.golemUUID = golemUUID;
   }

   public BlockPos getPos() {
      return this.type == 1 ? this.entity.func_180425_c() : this.pos;
   }

   public byte getType() {
      return this.type;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public int getId() {
      return this.id;
   }

   public boolean isReserved() {
      return this.reserved;
   }

   public void setReserved(boolean res) {
      this.reserved = res;
      this.lifespan = (short)(this.lifespan + 120);
   }

   public boolean isSuspended() {
      return this.suspended;
   }

   public void setSuspended(boolean suspended) {
      this.suspended = suspended;
   }

   public SealPos getSealPos() {
      return this.sealPos;
   }

   public boolean equals(Object o) {
      if (!(o instanceof Task)) {
         return false;
      } else {
         Task t = (Task)o;
         return t.id == this.id;
      }
   }

   public long getLifespan() {
      return (long)this.lifespan;
   }

   public void setLifespan(short ls) {
      this.lifespan = ls;
   }

   public boolean canGolemPerformTask(IGolemAPI golem) {
      ISealEntity se = GolemHelper.getSealEntity(golem.getGolemWorld().field_73011_w.getDimension(), this.sealPos);
      if (se != null) {
         return golem.getGolemColor() > 0 && se.getColor() > 0 && golem.getGolemColor() != se.getColor() ? false : se.getSeal().canGolemPerformTask(golem, this);
      } else {
         return true;
      }
   }

   public int getData() {
      return this.data;
   }

   public void setData(int data) {
      this.data = data;
   }
}
