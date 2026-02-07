package thaumcraft.common.entities.construct.golem.seals;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealConfigArea;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.ISealGui;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.entities.construct.golem.gui.SealBaseContainer;
import thaumcraft.common.entities.construct.golem.gui.SealBaseGUI;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;

public class SealGuard implements ISeal, ISealGui, ISealConfigArea {
   int delay = (new Random(System.nanoTime())).nextInt(22);
   protected ISealConfigToggles.SealToggle[] props = new ISealConfigToggles.SealToggle[]{new ISealConfigToggles.SealToggle(true, "pmob", "golem.prop.mob"), new ISealConfigToggles.SealToggle(false, "panimal", "golem.prop.animal"), new ISealConfigToggles.SealToggle(false, "pplayer", "golem.prop.player")};
   ResourceLocation icon = new ResourceLocation("thaumcraft", "items/seals/seal_guard");

   public String getKey() {
      return "thaumcraft:guard";
   }

   public void tickSeal(World world, ISealEntity seal) {
      if (this.delay++ % 20 == 0) {
         AxisAlignedBB area = GolemHelper.getBoundsForArea(seal);
         List list = world.func_72872_a(EntityLivingBase.class, area);
         if (list.size() > 0) {
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
               Object e = var5.next();
               EntityLivingBase target = (EntityLivingBase)e;
               if (this.isValidTarget(target)) {
                  Task task = new Task(seal.getSealPos(), target);
                  task.setPriority(seal.getPriority());
                  task.setLifespan((short)10);
                  TaskHandler.addTask(world.field_73011_w.getDimension(), task);
               }
            }
         }

      }
   }

   private boolean isValidTarget(EntityLivingBase target) {
      boolean valid = false;
      if (this.props[0].value && (target instanceof IMob || target instanceof EntityMob)) {
         valid = true;
      }

      if (this.props[1].value && (target instanceof EntityAnimal || target instanceof IAnimals)) {
         valid = true;
      }

      if (this.props[2].value && FMLCommonHandler.instance().getMinecraftServerInstance().func_71219_W() && target instanceof EntityPlayer) {
         valid = true;
      }

      return valid;
   }

   public void onTaskStarted(World world, IGolemAPI golem, Task task) {
      if (task.getEntity() != null && task.getEntity() instanceof EntityLivingBase && this.isValidTarget((EntityLivingBase)task.getEntity())) {
         ((EntityLiving)golem).func_70624_b((EntityLivingBase)task.getEntity());
         golem.addRankXp(1);
      }

      task.setSuspended(true);
   }

   public boolean onTaskCompletion(World world, IGolemAPI golem, Task task) {
      task.setSuspended(true);
      return true;
   }

   public boolean canGolemPerformTask(IGolemAPI golem, Task task) {
      return !golem.getGolemEntity().func_184191_r((EntityLivingBase)task.getEntity());
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
      return new EnumGolemTrait[]{EnumGolemTrait.FIGHTER};
   }

   public EnumGolemTrait[] getForbiddenTags() {
      return null;
   }

   public void onTaskSuspension(World world, Task task) {
   }

   public void readCustomNBT(NBTTagCompound nbt) {
   }

   public void writeCustomNBT(NBTTagCompound nbt) {
   }

   public void onRemoval(World world, BlockPos pos, EnumFacing side) {
   }

   public Object returnContainer(World world, EntityPlayer player, BlockPos pos, EnumFacing side, ISealEntity seal) {
      return new SealBaseContainer(player.field_71071_by, world, seal);
   }

   @SideOnly(Side.CLIENT)
   public Object returnGui(World world, EntityPlayer player, BlockPos pos, EnumFacing side, ISealEntity seal) {
      return new SealBaseGUI(player.field_71071_by, world, seal);
   }
}
