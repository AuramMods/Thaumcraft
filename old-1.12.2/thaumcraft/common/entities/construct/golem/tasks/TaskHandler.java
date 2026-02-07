package thaumcraft.common.entities.construct.golem.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.entities.construct.golem.EntityThaumcraftGolem;
import thaumcraft.common.entities.construct.golem.seals.SealHandler;

public class TaskHandler {
   static final int TASK_LIMIT = 1000;
   public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Task>> tasks = new ConcurrentHashMap();

   public static void addTask(int dim, Task ticket) {
      if (!tasks.containsKey(dim)) {
         tasks.put(dim, new ConcurrentHashMap());
      }

      ConcurrentHashMap<Integer, Task> dc = (ConcurrentHashMap)tasks.get(dim);
      if (dc.size() > 1000) {
         try {
            Iterator<Task> i = dc.values().iterator();
            if (i.hasNext()) {
               i.next();
               i.remove();
            }
         } catch (Exception var4) {
         }
      }

      dc.put(ticket.getId(), ticket);
   }

   public static Task getTask(int dim, int id) {
      return (Task)getTasks(dim).get(id);
   }

   public static ConcurrentHashMap<Integer, Task> getTasks(int dim) {
      if (!tasks.containsKey(dim)) {
         tasks.put(dim, new ConcurrentHashMap());
      }

      return (ConcurrentHashMap)tasks.get(dim);
   }

   public static ArrayList<Task> getBlockTasksSorted(int dim, UUID uuid, Entity golem) {
      ConcurrentHashMap<Integer, Task> tickets = getTasks(dim);
      ArrayList<Task> out = new ArrayList();
      Iterator var5 = tickets.values().iterator();

      while(true) {
         label50:
         while(true) {
            Task ticket;
            do {
               do {
                  do {
                     if (!var5.hasNext()) {
                        return out;
                     }

                     ticket = (Task)var5.next();
                  } while(ticket.isReserved());
               } while(ticket.getType() != 0);
            } while(uuid != null && ticket.getGolemUUID() != null && !uuid.equals(ticket.getGolemUUID()));

            if (out.size() == 0) {
               out.add(ticket);
            } else {
               double d = ticket.getPos().func_177957_d(golem.field_70165_t, golem.field_70163_u, golem.field_70161_v);
               d -= (double)(ticket.getPriority() * 256);

               for(int a = 0; a < out.size(); ++a) {
                  double d1 = ((Task)out.get(a)).getPos().func_177957_d(golem.field_70165_t, golem.field_70163_u, golem.field_70161_v);
                  d1 -= (double)(((Task)out.get(a)).getPriority() * 256);
                  if (d < d1) {
                     out.add(a, ticket);
                     continue label50;
                  }
               }

               out.add(ticket);
            }
         }
      }
   }

   public static ArrayList<Task> getEntityTasksSorted(int dim, UUID uuid, Entity golem) {
      ConcurrentHashMap<Integer, Task> tickets = getTasks(dim);
      ArrayList<Task> out = new ArrayList();
      Iterator var5 = tickets.values().iterator();

      while(true) {
         label50:
         while(true) {
            Task ticket;
            do {
               do {
                  do {
                     if (!var5.hasNext()) {
                        return out;
                     }

                     ticket = (Task)var5.next();
                  } while(ticket.isReserved());
               } while(ticket.getType() != 1);
            } while(uuid != null && ticket.getGolemUUID() != null && !uuid.equals(ticket.getGolemUUID()));

            if (out.size() == 0) {
               out.add(ticket);
            } else {
               double d = ticket.getPos().func_177957_d(golem.field_70165_t, golem.field_70163_u, golem.field_70161_v);
               d -= (double)(ticket.getPriority() * 256);

               for(int a = 0; a < out.size(); ++a) {
                  double d1 = ((Task)out.get(a)).getPos().func_177957_d(golem.field_70165_t, golem.field_70163_u, golem.field_70161_v);
                  d1 -= (double)(((Task)out.get(a)).getPriority() * 256);
                  if (d < d1) {
                     out.add(a, ticket);
                     continue label50;
                  }
               }

               out.add(ticket);
            }
         }
      }
   }

   public static void completeTask(Task task, EntityThaumcraftGolem golem) {
      if (!task.isCompleted() && !task.isSuspended()) {
         ISealEntity se = SealHandler.getSealEntity(golem.field_70170_p.field_73011_w.getDimension(), task.getSealPos());
         if (se != null) {
            task.setCompletion(se.getSeal().onTaskCompletion(golem.field_70170_p, golem, task));
         } else {
            task.setCompletion(true);
         }

      }
   }

   public static void clearSuspendedOrExpiredTasks(World world) {
      ConcurrentHashMap<Integer, Task> tickets = getTasks(world.field_73011_w.getDimension());
      ConcurrentHashMap<Integer, Task> temp = new ConcurrentHashMap();
      Iterator var3 = tickets.values().iterator();

      while(true) {
         while(var3.hasNext()) {
            Task ticket = (Task)var3.next();
            if (!ticket.isSuspended() && ticket.getLifespan() > 0L) {
               ticket.setLifespan((short)((int)(ticket.getLifespan() - 1L)));
               temp.put(ticket.getId(), ticket);
            } else {
               ISealEntity sEnt = SealHandler.getSealEntity(world.field_73011_w.getDimension(), ticket.getSealPos());
               if (sEnt != null) {
                  sEnt.getSeal().onTaskSuspension(world, ticket);
               }
            }
         }

         tasks.put(world.field_73011_w.getDimension(), temp);
         return;
      }
   }
}
