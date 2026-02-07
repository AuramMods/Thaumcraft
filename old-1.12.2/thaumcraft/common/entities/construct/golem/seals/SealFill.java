package thaumcraft.common.entities.construct.golem.seals;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;
import thaumcraft.common.lib.utils.InventoryUtils;

public class SealFill extends SealFiltered {
   int delay = (new Random(System.nanoTime())).nextInt(50);
   int watchedTask = Integer.MIN_VALUE;
   protected ISealConfigToggles.SealToggle[] props = new ISealConfigToggles.SealToggle[]{new ISealConfigToggles.SealToggle(true, "pmeta", "golem.prop.meta"), new ISealConfigToggles.SealToggle(true, "pnbt", "golem.prop.nbt"), new ISealConfigToggles.SealToggle(false, "pore", "golem.prop.ore"), new ISealConfigToggles.SealToggle(false, "pmod", "golem.prop.mod"), new ISealConfigToggles.SealToggle(false, "pexist", "golem.prop.exist")};
   ResourceLocation icon = new ResourceLocation("thaumcraft", "items/seals/seal_fill");

   public String getKey() {
      return "thaumcraft:fill";
   }

   public void tickSeal(World world, ISealEntity seal) {
      if (this.delay++ % 20 == 0) {
         Task oldTask = TaskHandler.getTask(world.field_73011_w.getDimension(), this.watchedTask);
         if (oldTask == null || oldTask.isReserved() || oldTask.isSuspended() || oldTask.isCompleted()) {
            Task task = new Task(seal.getSealPos(), seal.getSealPos().pos);
            task.setPriority(seal.getPriority());
            TaskHandler.addTask(world.field_73011_w.getDimension(), task);
            this.watchedTask = task.getId();
         }

      }
   }

   public void onTaskStarted(World world, IGolemAPI golem, Task task) {
      ISealEntity se = SealHandler.getSealEntity(world.field_73011_w.getDimension(), task.getSealPos());
      if (se != null && !se.isStoppedByRedstone(world)) {
         Task newTask = new Task(task.getSealPos(), task.getSealPos().pos);
         newTask.setPriority(se.getPriority());
         TaskHandler.addTask(world.field_73011_w.getDimension(), newTask);
         this.watchedTask = newTask.getId();
      }

   }

   public boolean onTaskCompletion(World world, IGolemAPI golem, Task task) {
      TileEntity te = world.func_175625_s(task.getSealPos().pos);
      Tuple tuple;
      int limit;
      int c;
      ItemStack s;
      ItemStack t;
      if (te != null && te instanceof IInventory) {
         tuple = InventoryUtils.findFirstMatchFromFilterTuple(this.getInv(), this.isBlacklist(), golem.getCarrying(), !this.props[0].value, !this.props[1].value, this.props[2].value, this.props[3].value);
         if (tuple.func_76341_a() != null) {
            limit = ((ItemStack)tuple.func_76341_a()).field_77994_a;
            if (this.hasStacksizeLimiters() && tuple.func_76340_b() != null && ((ItemStack)tuple.func_76340_b()).field_77994_a > 0) {
               IInventory var10000 = (IInventory)te;
               ItemStack var10001 = (ItemStack)tuple.func_76340_b();
               boolean var10003 = !this.props[0].value;
               boolean var10004 = !this.props[1].value;
               c = InventoryUtils.inventoryContainsAmount(var10000, var10001, task.getSealPos().face, var10003, var10004, this.props[2].value, this.props[3].value);
               if (c < ((ItemStack)tuple.func_76340_b()).field_77994_a) {
                  limit = ((ItemStack)tuple.func_76340_b()).field_77994_a - c;
               } else {
                  limit = 0;
               }
            }

            if (limit > 0) {
               t = ((ItemStack)tuple.func_76341_a()).func_77946_l();
               t.field_77994_a = limit;
               s = golem.dropItem(t);
               golem.holdItem(InventoryUtils.placeItemStackIntoInventory(s, (IInventory)te, task.getSealPos().face, true));
               ((Entity)golem).func_184185_a(SoundEvents.field_187638_cR, 0.125F, ((world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.7F + 1.0F) * 1.0F);
               golem.addRankXp(1);
               golem.swingArm();
            }
         }
      } else {
         tuple = InventoryUtils.findFirstMatchFromFilterTuple(this.getInv(), this.isBlacklist(), golem.getCarrying(), !this.props[0].value, !this.props[1].value, this.props[2].value, this.props[3].value);
         if (tuple.func_76341_a() != null) {
            limit = ((ItemStack)tuple.func_76341_a()).field_77994_a;
            if (this.hasStacksizeLimiters() && tuple.func_76340_b() != null && ((ItemStack)tuple.func_76340_b()).field_77994_a > 0) {
               c = InventoryUtils.countItemsInWorld(golem.getGolemWorld(), task.getSealPos().pos, (ItemStack)tuple.func_76340_b(), 1.5D, !this.props[0].value, !this.props[1].value, this.props[2].value, this.props[3].value);
               if (c < ((ItemStack)tuple.func_76340_b()).field_77994_a) {
                  limit = ((ItemStack)tuple.func_76340_b()).field_77994_a - c;
               } else {
                  limit = 0;
               }
            }

            if (limit > 0) {
               t = ((ItemStack)tuple.func_76341_a()).func_77946_l();
               t.field_77994_a = limit;
               s = golem.dropItem(t);
               EntityItem ie = new EntityItem(world, (double)task.getSealPos().pos.func_177958_n() + 0.5D + (double)task.getSealPos().face.func_82601_c(), (double)task.getSealPos().pos.func_177956_o() + 0.5D + (double)task.getSealPos().face.func_96559_d(), (double)task.getSealPos().pos.func_177952_p() + 0.5D + (double)task.getSealPos().face.func_82599_e(), s);
               ie.field_70159_w /= 5.0D;
               ie.field_70181_x /= 2.0D;
               ie.field_70179_y /= 5.0D;
               world.func_72838_d(ie);
               ((Entity)golem).func_184185_a(SoundEvents.field_187638_cR, 0.125F, ((world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.7F + 1.0F) * 1.0F);
               golem.swingArm();
            }
         }
      }

      task.setSuspended(true);
      return true;
   }

   public boolean canGolemPerformTask(IGolemAPI golem, Task task) {
      TileEntity te = golem.getGolemWorld().func_175625_s(task.getSealPos().pos);
      Tuple tuple;
      if (te != null && te instanceof IInventory) {
         tuple = InventoryUtils.findFirstMatchFromFilterTuple(this.getInv(), this.isBlacklist(), golem.getCarrying(), !this.props[0].value, !this.props[1].value, this.props[2].value, this.props[3].value);
         IInventory var10000;
         ItemStack var10001;
         boolean var10003;
         boolean var10004;
         if (tuple.func_76341_a() != null && this.props[4].value) {
            var10000 = (IInventory)te;
            var10001 = (ItemStack)tuple.func_76341_a();
            var10003 = !this.props[0].value;
            var10004 = !this.props[1].value;
            if (InventoryUtils.inventoryContainsAmount(var10000, var10001, task.getSealPos().face, var10003, var10004, this.props[2].value, this.props[3].value) <= 0) {
               return false;
            }
         }

         if (tuple.func_76341_a() != null && InventoryUtils.hasRoomFor((ItemStack)tuple.func_76341_a(), (IInventory)te, task.getSealPos().face)) {
            if (!this.hasStacksizeLimiters() || tuple.func_76340_b() == null || ((ItemStack)tuple.func_76340_b()).field_77994_a <= 0) {
               return true;
            }

            var10000 = (IInventory)te;
            var10001 = (ItemStack)tuple.func_76340_b();
            var10003 = !this.props[0].value;
            var10004 = !this.props[1].value;
            if (InventoryUtils.inventoryContainsAmount(var10000, var10001, task.getSealPos().face, var10003, var10004, this.props[2].value, this.props[3].value) < ((ItemStack)tuple.func_76340_b()).field_77994_a) {
               return true;
            }
         }

         return false;
      } else {
         tuple = InventoryUtils.findFirstMatchFromFilterTuple(this.getInv(), this.isBlacklist(), golem.getCarrying(), !this.props[0].value, !this.props[1].value, this.props[2].value, this.props[3].value);
         if (tuple.func_76341_a() != null) {
            if (this.hasStacksizeLimiters() && tuple.func_76340_b() != null && ((ItemStack)tuple.func_76340_b()).field_77994_a > 0) {
               return InventoryUtils.countItemsInWorld(golem.getGolemWorld(), task.getSealPos().pos, (ItemStack)tuple.func_76340_b(), 1.5D, !this.props[0].value, !this.props[1].value, this.props[2].value, this.props[3].value) < ((ItemStack)tuple.func_76340_b()).field_77994_a;
            } else {
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public boolean canPlaceAt(World world, BlockPos pos, EnumFacing side) {
      return !world.func_175623_d(pos);
   }

   public ResourceLocation getSealIcon() {
      return this.icon;
   }

   public int[] getGuiCategories() {
      return new int[]{1, 0, 4};
   }

   public EnumGolemTrait[] getRequiredTags() {
      return null;
   }

   public EnumGolemTrait[] getForbiddenTags() {
      return new EnumGolemTrait[]{EnumGolemTrait.CLUMSY};
   }

   public void onTaskSuspension(World world, Task task) {
   }

   public void onRemoval(World world, BlockPos pos, EnumFacing side) {
   }

   public boolean hasStacksizeLimiters() {
      return !this.isBlacklist();
   }
}
