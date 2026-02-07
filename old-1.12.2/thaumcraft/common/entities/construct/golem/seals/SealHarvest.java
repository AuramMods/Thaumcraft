package thaumcraft.common.entities.construct.golem.seals;

import com.mojang.authlib.GameProfile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApi;
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
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.CropUtils;

public class SealHarvest implements ISeal, ISealGui, ISealConfigArea, ISealConfigToggles {
   int delay = (new Random(System.nanoTime())).nextInt(33);
   int count = 0;
   HashMap<Long, SealHarvest.ReplantInfo> replantTasks = new HashMap();
   ResourceLocation icon = new ResourceLocation("thaumcraft", "items/seals/seal_harvest");
   protected ISealConfigToggles.SealToggle[] props = new ISealConfigToggles.SealToggle[]{new ISealConfigToggles.SealToggle(true, "prep", "golem.prop.replant"), new ISealConfigToggles.SealToggle(false, "ppro", "golem.prop.provision")};

   public String getKey() {
      return "thaumcraft:harvest";
   }

   public void tickSeal(World world, ISealEntity seal) {
      if (this.delay % 100 == 0) {
         AxisAlignedBB area = GolemHelper.getBoundsForArea(seal);
         Iterator rt = this.replantTasks.keySet().iterator();

         while(rt.hasNext()) {
            BlockPos pp = BlockPos.func_177969_a((Long)rt.next());
            if (!area.func_72318_a(new Vec3d((double)pp.func_177958_n() + 0.5D, (double)pp.func_177956_o() + 0.5D, (double)pp.func_177952_p() + 0.5D))) {
               if (this.replantTasks.get(rt) != null) {
                  Task tt = TaskHandler.getTask(world.field_73011_w.getDimension(), ((SealHarvest.ReplantInfo)this.replantTasks.get(rt)).taskid);
                  if (tt != null) {
                     tt.setSuspended(true);
                  }
               }

               rt.remove();
            }
         }
      }

      if (this.delay++ % 5 == 0) {
         BlockPos p = GolemHelper.getPosInArea(seal, this.count++);
         Task t;
         if (CropUtils.isGrownCrop(world, p)) {
            t = new Task(seal.getSealPos(), p);
            t.setPriority(seal.getPriority());
            TaskHandler.addTask(world.field_73011_w.getDimension(), t);
         } else if (this.getToggles()[0].value && this.replantTasks.containsKey(p.func_177986_g()) && world.func_175623_d(p)) {
            t = TaskHandler.getTask(world.field_73011_w.getDimension(), ((SealHarvest.ReplantInfo)this.replantTasks.get(p.func_177986_g())).taskid);
            if (t == null) {
               Task tt = new Task(seal.getSealPos(), ((SealHarvest.ReplantInfo)this.replantTasks.get(p.func_177986_g())).pos);
               tt.setPriority(seal.getPriority());
               TaskHandler.addTask(world.field_73011_w.getDimension(), tt);
               ((SealHarvest.ReplantInfo)this.replantTasks.get(p.func_177986_g())).taskid = tt.getId();
            }
         }

      }
   }

   public boolean onTaskCompletion(World world, IGolemAPI golem, Task task) {
      FakePlayer fp;
      ItemStack seed;
      if (CropUtils.isGrownCrop(world, task.getPos())) {
         fp = FakePlayerFactory.get((WorldServer)world, new GameProfile((UUID)null, "FakeThaumcraftGolem"));
         fp.func_70107_b(golem.getGolemEntity().field_70165_t, golem.getGolemEntity().field_70163_u, golem.getGolemEntity().field_70161_v);
         EnumFacing face = BlockPistonBase.func_185647_a(task.getPos(), golem.getGolemEntity());
         IBlockState bs = world.func_180495_p(task.getPos());
         if (CropUtils.clickableCrops.contains(bs.func_177230_c().func_149739_a() + bs.func_177230_c().func_176201_c(bs))) {
            bs.func_177230_c().func_180639_a(world, task.getPos(), bs, fp, EnumHand.MAIN_HAND, (ItemStack)null, face, 0.0F, 0.0F, 0.0F);
            golem.addRankXp(1);
            golem.swingArm();
         } else {
            BlockUtils.harvestBlock(world, fp, task.getPos(), true, false, 0);
            golem.addRankXp(1);
            golem.swingArm();
            if (this.getToggles()[0].value) {
               seed = ThaumcraftApi.getSeed(bs.func_177230_c());
               if (seed != null) {
                  IBlockState bb = world.func_180495_p(task.getPos().func_177977_b());
                  EnumFacing rf = null;
                  if (seed.func_77973_b() instanceof IPlantable && bb.func_177230_c().canSustainPlant(bb, world, task.getPos().func_177977_b(), EnumFacing.UP, (IPlantable)seed.func_77973_b())) {
                     rf = EnumFacing.DOWN;
                  } else if (!(seed.func_77973_b() instanceof IPlantable) && bs.func_177230_c() instanceof BlockDirectional) {
                     rf = (EnumFacing)bs.func_177229_b(BlockDirectional.field_176387_N);
                  }

                  if (rf != null) {
                     Task tt = new Task(task.getSealPos(), task.getPos());
                     tt.setPriority(task.getPriority());
                     tt.setLifespan((short)300);
                     this.replantTasks.put(tt.getPos().func_177986_g(), new SealHarvest.ReplantInfo(tt.getPos(), rf, tt.getId(), seed.func_77946_l(), bb.func_177230_c() instanceof BlockFarmland));
                     TaskHandler.addTask(world.field_73011_w.getDimension(), tt);
                  }
               }
            }
         }
      } else if (this.replantTasks.containsKey(task.getPos().func_177986_g()) && ((SealHarvest.ReplantInfo)this.replantTasks.get(task.getPos().func_177986_g())).taskid == task.getId() && world.func_175623_d(task.getPos()) && golem.isCarrying(((SealHarvest.ReplantInfo)this.replantTasks.get(task.getPos().func_177986_g())).stack)) {
         fp = FakePlayerFactory.get((WorldServer)world, new GameProfile((UUID)null, "FakeThaumcraftGolem"));
         fp.func_70107_b(golem.getGolemEntity().field_70165_t, golem.getGolemEntity().field_70163_u, golem.getGolemEntity().field_70161_v);
         IBlockState bb = world.func_180495_p(task.getPos().func_177977_b());
         SealHarvest.ReplantInfo ri = (SealHarvest.ReplantInfo)this.replantTasks.get(task.getPos().func_177986_g());
         if ((bb.func_177230_c() instanceof BlockDirt || bb.func_177230_c() instanceof BlockGrass) && ri.farmland) {
            Items.field_151012_L.func_180614_a(new ItemStack(Items.field_151012_L), fp, world, task.getPos().func_177977_b(), EnumHand.MAIN_HAND, EnumFacing.UP, 0.5F, 0.5F, 0.5F);
         }

         seed = ri.stack.func_77946_l();
         seed.field_77994_a = 1;
         if (seed.func_77973_b().func_180614_a(seed.func_77946_l(), fp, world, task.getPos().func_177972_a(ri.face), EnumHand.MAIN_HAND, ri.face.func_176734_d(), 0.5F, 0.5F, 0.5F) == EnumActionResult.SUCCESS) {
            world.func_175669_a(2001, task.getPos(), Block.func_176210_f(world.func_180495_p(task.getPos())));
            golem.dropItem(seed);
            golem.addRankXp(1);
            golem.swingArm();
         }
      }

      task.setSuspended(true);
      return true;
   }

   public boolean canGolemPerformTask(IGolemAPI golem, Task task) {
      if (this.replantTasks.containsKey(task.getPos().func_177986_g()) && ((SealHarvest.ReplantInfo)this.replantTasks.get(task.getPos().func_177986_g())).taskid == task.getId()) {
         boolean carry = golem.isCarrying(((SealHarvest.ReplantInfo)this.replantTasks.get(task.getPos().func_177986_g())).stack);
         if (!carry && this.getToggles()[1].value) {
            ISealEntity se = SealHandler.getSealEntity(golem.getGolemWorld().field_73011_w.getDimension(), task.getSealPos());
            if (se != null) {
               GolemHelper.requestProvisioning(golem.getGolemWorld(), se, ((SealHarvest.ReplantInfo)this.replantTasks.get(task.getPos().func_177986_g())).stack);
            }
         }

         return carry;
      } else {
         return true;
      }
   }

   public void onTaskSuspension(World world, Task task) {
   }

   public void readCustomNBT(NBTTagCompound nbt) {
      NBTTagList nbttaglist = nbt.func_150295_c("replant", 10);

      for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
         NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
         long loc = nbttagcompound1.func_74763_f("taskloc");
         byte face = nbttagcompound1.func_74771_c("taskface");
         boolean farmland = nbttagcompound1.func_74767_n("farmland");
         ItemStack stack = ItemStack.func_77949_a(nbttagcompound1);
         this.replantTasks.put(loc, new SealHarvest.ReplantInfo(BlockPos.func_177969_a(loc), EnumFacing.field_82609_l[face], 0, stack, farmland));
      }

   }

   public void writeCustomNBT(NBTTagCompound nbt) {
      if (this.getToggles()[0].value) {
         NBTTagList nbttaglist = new NBTTagList();
         Iterator var3 = this.replantTasks.keySet().iterator();

         while(var3.hasNext()) {
            Long key = (Long)var3.next();
            SealHarvest.ReplantInfo info = (SealHarvest.ReplantInfo)this.replantTasks.get(key);
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.func_74772_a("taskloc", info.pos.func_177986_g());
            nbttagcompound1.func_74774_a("taskface", (byte)info.face.ordinal());
            nbttagcompound1.func_74757_a("farmland", info.farmland);
            info.stack.func_77955_b(nbttagcompound1);
            nbttaglist.func_74742_a(nbttagcompound1);
         }

         nbt.func_74782_a("replant", nbttaglist);
      }

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
      return new int[]{2, 3, 0, 4};
   }

   public ISealConfigToggles.SealToggle[] getToggles() {
      return this.props;
   }

   public void setToggle(int indx, boolean value) {
      this.props[indx].setValue(value);
   }

   public EnumGolemTrait[] getRequiredTags() {
      return new EnumGolemTrait[]{EnumGolemTrait.DEFT, EnumGolemTrait.SMART};
   }

   public EnumGolemTrait[] getForbiddenTags() {
      return null;
   }

   public void onTaskStarted(World world, IGolemAPI golem, Task task) {
   }

   private class ReplantInfo {
      EnumFacing face;
      BlockPos pos;
      int taskid;
      ItemStack stack;
      boolean farmland;

      public ReplantInfo(BlockPos pos, EnumFacing face, int taskid, ItemStack stack, boolean farmland) {
         this.pos = pos;
         this.face = face;
         this.taskid = taskid;
         this.stack = stack;
         this.farmland = farmland;
      }
   }
}
