package thaumcraft.common.entities.construct.golem.seals;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealConfigArea;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.ISealGui;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.entities.construct.golem.gui.SealBaseContainer;
import thaumcraft.common.entities.construct.golem.gui.SealBaseGUI;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;

public class SealButcher implements ISeal, ISealGui, ISealConfigArea {
   int delay = (new Random(System.nanoTime())).nextInt(200);
   boolean wait = false;
   ResourceLocation icon = new ResourceLocation("thaumcraft", "items/seals/seal_butcher");

   public String getKey() {
      return "thaumcraft:butcher";
   }

   public void tickSeal(World world, ISealEntity seal) {
      if (this.delay++ % 200 == 0 && !this.wait) {
         AxisAlignedBB area = GolemHelper.getBoundsForArea(seal);
         List list = world.func_72872_a(EntityLivingBase.class, area);
         if (list.size() > 0) {
            Iterator var5 = list.iterator();

            while(true) {
               EntityLivingBase target;
               do {
                  if (!var5.hasNext()) {
                     return;
                  }

                  Object e = var5.next();
                  target = (EntityLivingBase)e;
               } while(!this.isValidTarget(target));

               List var55 = world.func_72872_a(target.getClass(), area);
               Iterator var22 = var55.iterator();
               int count = 0;

               while(var22.hasNext() && count < 3) {
                  EntityLivingBase var33 = (EntityLivingBase)var22.next();
                  if (this.isValidTarget(var33)) {
                     ++count;
                  }
               }

               if (count > 2) {
                  Task task = new Task(seal.getSealPos(), target);
                  task.setPriority(seal.getPriority());
                  task.setLifespan((short)10);
                  TaskHandler.addTask(world.field_73011_w.getDimension(), task);
                  this.wait = true;
                  break;
               }
            }
         }

      }
   }

   private boolean isValidTarget(EntityLivingBase target) {
      if ((target instanceof EntityAnimal || target instanceof IAnimals) && !(target instanceof IMob) && (!(target instanceof EntityTameable) || !((EntityTameable)target).func_70909_n()) && !(target instanceof EntityGolem)) {
         return !(target instanceof EntityAnimal) || !((EntityAnimal)target).func_70631_g_();
      } else {
         return false;
      }
   }

   public void onTaskStarted(World world, IGolemAPI golem, Task task) {
      if (task.getEntity() != null && task.getEntity() instanceof EntityLivingBase && this.isValidTarget((EntityLivingBase)task.getEntity())) {
         ((EntityLiving)golem).func_70624_b((EntityLivingBase)task.getEntity());
         golem.addRankXp(1);
      }

      task.setSuspended(true);
      this.wait = false;
   }

   public boolean onTaskCompletion(World world, IGolemAPI golem, Task task) {
      task.setSuspended(true);
      this.wait = false;
      return true;
   }

   public boolean canGolemPerformTask(IGolemAPI golem, Task task) {
      return true;
   }

   public boolean canPlaceAt(World world, BlockPos pos, EnumFacing side) {
      return !world.func_175623_d(pos);
   }

   public ResourceLocation getSealIcon() {
      return this.icon;
   }

   public int[] getGuiCategories() {
      return new int[]{2, 0, 4};
   }

   public EnumGolemTrait[] getRequiredTags() {
      return new EnumGolemTrait[]{EnumGolemTrait.FIGHTER, EnumGolemTrait.SMART};
   }

   public EnumGolemTrait[] getForbiddenTags() {
      return null;
   }

   public void onTaskSuspension(World world, Task task) {
      this.wait = false;
   }

   public void readCustomNBT(NBTTagCompound nbt) {
   }

   public void writeCustomNBT(NBTTagCompound nbt) {
   }

   public void onRemoval(World world, BlockPos pos, EnumFacing side) {
      this.wait = false;
   }

   public Object returnContainer(World world, EntityPlayer player, BlockPos pos, EnumFacing side, ISealEntity seal) {
      return new SealBaseContainer(player.field_71071_by, world, seal);
   }

   @SideOnly(Side.CLIENT)
   public Object returnGui(World world, EntityPlayer player, BlockPos pos, EnumFacing side, ISealEntity seal) {
      return new SealBaseGUI(player.field_71071_by, world, seal);
   }
}
