package thaumcraft.common.entities.construct.golem.seals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;
import thaumcraft.common.lib.utils.InventoryUtils;

public class SealEmpty extends SealFiltered {
   int delay = (new Random(System.nanoTime())).nextInt(30);
   int filterInc = 0;
   HashMap<Integer, ItemStack> cache = new HashMap();
   ResourceLocation icon = new ResourceLocation("thaumcraft", "items/seals/seal_empty");
   protected ISealConfigToggles.SealToggle[] props = new ISealConfigToggles.SealToggle[]{new ISealConfigToggles.SealToggle(true, "pmeta", "golem.prop.meta"), new ISealConfigToggles.SealToggle(true, "pnbt", "golem.prop.nbt"), new ISealConfigToggles.SealToggle(false, "pore", "golem.prop.ore"), new ISealConfigToggles.SealToggle(false, "pmod", "golem.prop.mod"), new ISealConfigToggles.SealToggle(false, "pcycle", "golem.prop.cycle"), new ISealConfigToggles.SealToggle(false, "pleave", "golem.prop.leave")};

   public String getKey() {
      return "thaumcraft:empty";
   }

   public void tickSeal(World world, ISealEntity seal) {
      if (this.delay % 100 == 0) {
         Iterator it = this.cache.keySet().iterator();

         while(it.hasNext()) {
            Task t = TaskHandler.getTask(world.field_73011_w.getDimension(), (Integer)it.next());
            if (t == null) {
               it.remove();
            }
         }
      }

      if (this.delay++ % 20 == 0) {
         TileEntity te = world.func_175625_s(seal.getSealPos().pos);
         if (te != null && te instanceof IInventory) {
            ItemStack[] var10000 = this.getInv(this.filterInc);
            boolean var10001 = this.isBlacklist();
            IInventory var10002 = (IInventory)te;
            boolean var10004 = !this.props[0].value;
            boolean var10005 = !this.props[1].value;
            ItemStack stack = InventoryUtils.findFirstMatchFromFilter(var10000, var10001, var10002, seal.getSealPos().face, var10004, var10005, this.props[2].value, this.props[3].value, this.props[5].value);
            if (stack != null) {
               Task task = new Task(seal.getSealPos(), seal.getSealPos().pos);
               task.setPriority(seal.getPriority());
               task.setLifespan((short)5);
               TaskHandler.addTask(world.field_73011_w.getDimension(), task);
               this.cache.put(task.getId(), stack);
            }
         }

      }
   }

   public boolean onTaskCompletion(World world, IGolemAPI golem, Task task) {
      TileEntity te = world.func_175625_s(task.getSealPos().pos);
      if (te != null && te instanceof IInventory) {
         ItemStack stack = (ItemStack)this.cache.get(task.getId());
         int sa = false;
         boolean var10004;
         if (stack != null && this.props[5].value) {
            IInventory var10000 = (IInventory)te;
            boolean var10003 = !this.props[0].value;
            var10004 = !this.props[1].value;
            int sa;
            if ((sa = InventoryUtils.inventoryContainsAmount(var10000, stack, task.getSealPos().face, var10003, var10004, this.props[2].value, this.props[3].value)) <= stack.field_77994_a) {
               stack = stack.func_77946_l();
               stack.field_77994_a = sa - 1;
            }
         }

         if (stack != null && golem.canCarry(stack, true)) {
            IInventory var10001 = (IInventory)te;
            ItemStack var10002 = stack.func_77946_l();
            var10004 = !this.props[0].value;
            boolean var10005 = !this.props[1].value;
            ItemStack s = golem.holdItem(InventoryUtils.extractStack(var10001, var10002, task.getSealPos().face, var10004, var10005, this.props[2].value, this.props[3].value, true));
            if (s != null) {
               ItemStack q = InventoryUtils.placeItemStackIntoInventory(s, (IInventory)te, task.getSealPos().face, true);
               if (q != null) {
                  ((Entity)golem).func_70099_a(q, 0.25F);
               }
            }

            ((Entity)golem).func_184185_a(SoundEvents.field_187638_cR, 0.125F, ((world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            golem.swingArm();
         }

         this.cache.remove(task.getId());
         ++this.filterInc;
      }

      task.setSuspended(true);
      return true;
   }

   public boolean canGolemPerformTask(IGolemAPI golem, Task task) {
      ItemStack stack = (ItemStack)this.cache.get(task.getId());
      return stack != null && golem.canCarry(stack, true);
   }

   public boolean canPlaceAt(World world, BlockPos pos, EnumFacing side) {
      TileEntity te = world.func_175625_s(pos);
      return te != null && te instanceof IInventory;
   }

   public ItemStack[] getInv(int c) {
      return super.getInv();
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

   public void onTaskStarted(World world, IGolemAPI golem, Task task) {
   }

   public void onTaskSuspension(World world, Task task) {
      this.cache.remove(task.getId());
   }

   public void onRemoval(World world, BlockPos pos, EnumFacing side) {
   }
}
