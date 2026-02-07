package thaumcraft.common.entities.construct.golem.seals;

import com.mojang.authlib.GameProfile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
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
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.Utils;

public class SealLumber implements ISeal, ISealGui, ISealConfigArea {
   int delay = (new Random(System.nanoTime())).nextInt(33);
   HashMap<Integer, Long> cache = new HashMap();
   ResourceLocation icon = new ResourceLocation("thaumcraft", "items/seals/seal_lumber");

   public String getKey() {
      return "thaumcraft:lumber";
   }

   public void tickSeal(World world, ISealEntity seal) {
      Task t;
      if (this.delay % 100 == 0) {
         Iterator it = this.cache.keySet().iterator();

         while(it.hasNext()) {
            t = TaskHandler.getTask(world.field_73011_w.getDimension(), (Integer)it.next());
            if (t == null) {
               it.remove();
            }
         }
      }

      ++this.delay;
      BlockPos p = GolemHelper.getPosInArea(seal, this.delay);
      if (!this.cache.containsValue(p.func_177986_g()) && Utils.isWoodLog(world, p)) {
         t = new Task(seal.getSealPos(), p);
         t.setPriority(seal.getPriority());
         TaskHandler.addTask(world.field_73011_w.getDimension(), t);
         this.cache.put(t.getId(), p.func_177986_g());
      }

   }

   public boolean onTaskCompletion(World world, IGolemAPI golem, Task task) {
      if (this.cache.containsKey(task.getId()) && Utils.isWoodLog(world, task.getPos())) {
         FakePlayer fp = FakePlayerFactory.get((WorldServer)world, new GameProfile((UUID)null, "FakeThaumcraftGolem"));
         fp.func_70107_b(golem.getGolemEntity().field_70165_t, golem.getGolemEntity().field_70163_u, golem.getGolemEntity().field_70161_v);
         IBlockState bs = world.func_180495_p(task.getPos());
         golem.swingArm();
         if (BlockUtils.breakFurthestBlock(world, task.getPos(), bs, fp)) {
            task.setLifespan((short)((int)Math.max(task.getLifespan(), 10L)));
            golem.addRankXp(1);
            return false;
         }

         this.cache.remove(task.getId());
      }

      task.setSuspended(true);
      return true;
   }

   public boolean canGolemPerformTask(IGolemAPI golem, Task task) {
      if (this.cache.containsKey(task.getId()) && Utils.isWoodLog(golem.getGolemWorld(), task.getPos())) {
         return true;
      } else {
         task.setSuspended(true);
         return false;
      }
   }

   public void onTaskSuspension(World world, Task task) {
      this.cache.remove(task.getId());
   }

   public void readCustomNBT(NBTTagCompound nbt) {
   }

   public void writeCustomNBT(NBTTagCompound nbt) {
   }

   public boolean canPlaceAt(World world, BlockPos pos, EnumFacing side) {
      return !world.func_175623_d(pos);
   }

   public ResourceLocation getSealIcon() {
      return this.icon;
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

   public int[] getGuiCategories() {
      return new int[]{2, 0, 4};
   }

   public EnumGolemTrait[] getRequiredTags() {
      return new EnumGolemTrait[]{EnumGolemTrait.BREAKER, EnumGolemTrait.SMART};
   }

   public EnumGolemTrait[] getForbiddenTags() {
      return null;
   }

   public void onTaskStarted(World world, IGolemAPI golem, Task task) {
   }
}
