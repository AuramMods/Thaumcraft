package thaumcraft.common.entities.construct.golem.seals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.seals.ISealConfigArea;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;
import thaumcraft.common.lib.utils.InventoryUtils;

public class SealPickup extends SealFiltered implements ISealConfigArea {
   int delay = (new Random(System.nanoTime())).nextInt(100);
   static HashMap<Integer, HashMap<Integer, Integer>> itemEntities = new HashMap();
   ResourceLocation icon = new ResourceLocation("thaumcraft", "items/seals/seal_pickup");
   protected ISealConfigToggles.SealToggle[] props = new ISealConfigToggles.SealToggle[]{new ISealConfigToggles.SealToggle(true, "pmeta", "golem.prop.meta"), new ISealConfigToggles.SealToggle(true, "pnbt", "golem.prop.nbt"), new ISealConfigToggles.SealToggle(false, "pore", "golem.prop.ore"), new ISealConfigToggles.SealToggle(false, "pmod", "golem.prop.mod")};

   public String getKey() {
      return "thaumcraft:pickup";
   }

   public void tickSeal(World world, ISealEntity seal) {
      if (this.delay++ % 20 == 0) {
         if (!itemEntities.containsKey(world.field_73011_w.getDimension())) {
            itemEntities.put(world.field_73011_w.getDimension(), new HashMap());
         }

         AxisAlignedBB area = GolemHelper.getBoundsForArea(seal);
         List list = world.func_72872_a(EntityItem.class, area);
         if (list.size() > 0) {
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
               Object e = var5.next();
               EntityItem ent = (EntityItem)e;
               if (ent != null && ent.field_70122_E && !ent.func_174874_s() && ent.func_92059_d() != null && !((HashMap)itemEntities.get(world.field_73011_w.getDimension())).containsValue(ent.func_145782_y())) {
                  ItemStack stack = InventoryUtils.findFirstMatchFromFilter(this.filter, this.isBlacklist(), new ItemStack[]{ent.func_92059_d()}, !this.props[0].value, !this.props[1].value, this.props[2].value, this.props[3].value);
                  if (stack != null) {
                     Task task = new Task(seal.getSealPos(), ent);
                     task.setPriority(seal.getPriority());
                     ((HashMap)itemEntities.get(world.field_73011_w.getDimension())).put(task.getId(), ent.func_145782_y());
                     TaskHandler.addTask(world.field_73011_w.getDimension(), task);
                     break;
                  }
               }
            }
         }

         if (this.delay % 100 != 0) {
            HashMap<Integer, Integer> hm = (HashMap)itemEntities.get(world.field_73011_w.getDimension());
            Iterator it = hm.values().iterator();

            while(true) {
               Entity e;
               do {
                  if (!it.hasNext()) {
                     return;
                  }

                  e = world.func_73045_a((Integer)it.next());
               } while(e != null && !e.field_70128_L);

               try {
                  it.remove();
               } catch (Exception var10) {
               }
            }
         }
      }
   }

   public boolean onTaskCompletion(World world, IGolemAPI golem, Task task) {
      if (!itemEntities.containsKey(world.field_73011_w.getDimension())) {
         itemEntities.put(world.field_73011_w.getDimension(), new HashMap());
      }

      EntityItem ei = this.getItemEntity(world, task);
      if (ei != null && ei.func_92059_d() != null) {
         ItemStack stack = InventoryUtils.findFirstMatchFromFilter(this.filter, this.isBlacklist(), new ItemStack[]{ei.func_92059_d()}, !this.props[0].value, !this.props[1].value, this.props[2].value, this.props[3].value);
         if (stack != null) {
            ItemStack is = golem.holdItem(ei.func_92059_d());
            if (is != null && is.field_77994_a > 0) {
               ei.func_92058_a(is);
            }

            if (is == null || is.field_77994_a <= 0) {
               ei.func_70106_y();
            }

            ((Entity)golem).func_184185_a(SoundEvents.field_187638_cR, 0.125F, ((world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            golem.swingArm();
         }
      }

      task.setSuspended(true);
      ((HashMap)itemEntities.get(world.field_73011_w.getDimension())).remove(task.getId());
      return true;
   }

   protected EntityItem getItemEntity(World world, Task task) {
      Integer ei = (Integer)((HashMap)itemEntities.get(world.field_73011_w.getDimension())).get(task.getId());
      if (ei != null) {
         Entity ent = world.func_73045_a(ei);
         if (ent != null && ent instanceof EntityItem) {
            return (EntityItem)ent;
         }
      }

      return null;
   }

   public boolean canGolemPerformTask(IGolemAPI golem, Task task) {
      EntityItem ei = this.getItemEntity(golem.getGolemWorld(), task);
      if (ei != null && ei.func_92059_d() != null) {
         if (ei.field_70128_L) {
            task.setSuspended(true);
            return false;
         } else {
            return golem.canCarry(ei.func_92059_d(), true);
         }
      } else {
         return false;
      }
   }

   public boolean canPlaceAt(World world, BlockPos pos, EnumFacing side) {
      return !world.func_175623_d(pos);
   }

   public ResourceLocation getSealIcon() {
      return this.icon;
   }

   public int[] getGuiCategories() {
      return new int[]{2, 1, 0, 4};
   }

   public EnumGolemTrait[] getRequiredTags() {
      return null;
   }

   public EnumGolemTrait[] getForbiddenTags() {
      return new EnumGolemTrait[]{EnumGolemTrait.CLUMSY};
   }

   public void onTaskStarted(World world, IGolemAPI golem, Task task) {
   }

   public void onTaskSuspension(World world, Task task) {
   }

   public void onRemoval(World world, BlockPos pos, EnumFacing side) {
   }
}
