package thaumcraft.common.entities.construct.golem.seals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.ProvisionRequest;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.entities.construct.golem.EntityThaumcraftGolem;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;
import thaumcraft.common.lib.utils.InventoryUtils;

public class SealProvide extends SealFiltered implements ISealConfigToggles {
   int delay = (new Random(System.nanoTime())).nextInt(88);
   HashMap<Integer, ProvisionRequest> cache = new HashMap();
   ResourceLocation icon = new ResourceLocation("thaumcraft", "items/seals/seal_provider");
   protected ISealConfigToggles.SealToggle[] props = new ISealConfigToggles.SealToggle[]{new ISealConfigToggles.SealToggle(true, "pmeta", "golem.prop.meta"), new ISealConfigToggles.SealToggle(true, "pnbt", "golem.prop.nbt"), new ISealConfigToggles.SealToggle(false, "pore", "golem.prop.ore"), new ISealConfigToggles.SealToggle(false, "pmod", "golem.prop.mod"), new ISealConfigToggles.SealToggle(false, "psing", "golem.prop.single"), new ISealConfigToggles.SealToggle(false, "pleave", "golem.prop.leave")};

   public String getKey() {
      return "thaumcraft:provider";
   }

   public int getFilterSize() {
      return 9;
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
         if (te != null && te instanceof IInventory && GolemHelper.provisionRequests.containsKey(world.field_73011_w.getDimension())) {
            ListIterator it = ((ArrayList)GolemHelper.provisionRequests.get(world.field_73011_w.getDimension())).listIterator();

            while(it.hasNext()) {
               ProvisionRequest pr = (ProvisionRequest)it.next();
               if (pr.getSeal().getSealPos().pos.func_177951_i(seal.getSealPos().pos) < 4096.0D && InventoryUtils.findFirstMatchFromFilter(this.getInv(), this.blacklist, new ItemStack[]{pr.getStack()}, !this.props[0].value, !this.props[1].value, this.props[2].value, this.props[3].value) != null && InventoryUtils.inventoryContainsAmount((IInventory)te, pr.getStack(), seal.getSealPos().face, false, false, false, false) > (this.props[5].value ? 1 : 0)) {
                  Task task = new Task(seal.getSealPos(), seal.getSealPos().pos);
                  task.setPriority(pr.getSeal().getPriority());
                  task.setLifespan((short)5);
                  TaskHandler.addTask(world.field_73011_w.getDimension(), task);
                  this.cache.put(task.getId(), pr);
                  it.remove();
                  break;
               }
            }
         }

      }
   }

   public boolean onTaskCompletion(World world, IGolemAPI golem, Task task) {
      TileEntity te = world.func_175625_s(task.getSealPos().pos);
      if (te != null && te instanceof IInventory) {
         ItemStack stack = null;

         try {
            stack = ((ProvisionRequest)this.cache.get(task.getId())).getStack().func_77946_l();
         } catch (Exception var9) {
         }

         if (stack != null && this.props[4].value) {
            stack.field_77994_a = 1;
         }

         int sa = false;
         int sa;
         if (stack != null && this.props[5].value && (sa = InventoryUtils.inventoryContainsAmount((IInventory)te, stack, task.getSealPos().face, false, false, false, false)) <= stack.field_77994_a) {
            stack.field_77994_a = sa - 1;
         }

         if (stack != null && golem.canCarry(stack, true)) {
            ItemStack s = golem.holdItem(InventoryUtils.extractStack((IInventory)te, stack.func_77946_l(), task.getSealPos().face, false, false, false, false, true));
            if (s != null) {
               ItemStack q = InventoryUtils.placeItemStackIntoInventory(s, (IInventory)te, task.getSealPos().face, true);
               if (q != null) {
                  ((Entity)golem).func_70099_a(q, 0.25F);
               }
            }

            ((Entity)golem).func_184185_a(SoundEvents.field_187638_cR, 0.125F, ((world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            golem.addRankXp(1);
            golem.swingArm();
         }

         this.cache.remove(task.getId());
      }

      task.setSuspended(true);
      return true;
   }

   public boolean canGolemPerformTask(IGolemAPI golem, Task task) {
      ProvisionRequest pr = (ProvisionRequest)this.cache.get(task.getId());
      return pr != null && ((EntityThaumcraftGolem)golem).func_180485_d(pr.getSeal().getSealPos().pos) && this.areGolemTagsValidForTask(pr.getSeal(), golem) && pr.getStack() != null && !golem.isCarrying(pr.getStack()) && golem.canCarry(pr.getStack(), true);
   }

   private boolean areGolemTagsValidForTask(ISealEntity se, IGolemAPI golem) {
      if (se == null) {
         return true;
      } else if (se.isLocked() && !((IEntityOwnable)golem).func_184753_b().equals(se.getOwner())) {
         return false;
      } else if (se.getSeal().getRequiredTags() != null && !golem.getProperties().getTraits().containsAll(Arrays.asList(se.getSeal().getRequiredTags()))) {
         return false;
      } else {
         if (se.getSeal().getForbiddenTags() != null) {
            EnumGolemTrait[] var3 = se.getSeal().getForbiddenTags();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               EnumGolemTrait tag = var3[var5];
               if (golem.getProperties().getTraits().contains(tag)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public void onTaskSuspension(World world, Task task) {
      this.cache.remove(task.getId());
   }

   public boolean canPlaceAt(World world, BlockPos pos, EnumFacing side) {
      TileEntity te = world.func_175625_s(pos);
      return te != null && te instanceof IInventory;
   }

   public ResourceLocation getSealIcon() {
      return this.icon;
   }

   public int[] getGuiCategories() {
      return new int[]{1, 3, 0, 4};
   }

   public EnumGolemTrait[] getRequiredTags() {
      return new EnumGolemTrait[]{EnumGolemTrait.SMART};
   }

   public EnumGolemTrait[] getForbiddenTags() {
      return new EnumGolemTrait[]{EnumGolemTrait.CLUMSY};
   }

   public void onTaskStarted(World world, IGolemAPI golem, Task task) {
   }

   public void onRemoval(World world, BlockPos pos, EnumFacing side) {
   }

   public ISealConfigToggles.SealToggle[] getToggles() {
      return this.props;
   }

   public void setToggle(int indx, boolean value) {
      this.props[indx].setValue(value);
   }
}
