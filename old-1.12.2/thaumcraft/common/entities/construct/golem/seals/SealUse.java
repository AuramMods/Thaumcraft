package thaumcraft.common.entities.construct.golem.seals;

import com.mojang.authlib.GameProfile;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
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
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.entities.construct.golem.gui.SealBaseContainer;
import thaumcraft.common.entities.construct.golem.gui.SealBaseGUI;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;
import thaumcraft.common.lib.network.FakeNetHandlerPlayServer;
import thaumcraft.common.lib.utils.InventoryUtils;

public class SealUse extends SealFiltered implements ISealConfigToggles {
   int delay = (new Random(System.nanoTime())).nextInt(49);
   FakePlayer fp;
   int watchedTask = Integer.MIN_VALUE;
   ResourceLocation icon = new ResourceLocation("thaumcraft", "items/seals/seal_use");
   protected ISealConfigToggles.SealToggle[] props = new ISealConfigToggles.SealToggle[]{new ISealConfigToggles.SealToggle(true, "pmeta", "golem.prop.meta"), new ISealConfigToggles.SealToggle(true, "pnbt", "golem.prop.nbt"), new ISealConfigToggles.SealToggle(false, "pore", "golem.prop.ore"), new ISealConfigToggles.SealToggle(false, "pmod", "golem.prop.mod"), new ISealConfigToggles.SealToggle(false, "pleft", "golem.prop.left"), new ISealConfigToggles.SealToggle(false, "pempty", "golem.prop.empty"), new ISealConfigToggles.SealToggle(false, "pemptyhand", "golem.prop.emptyhand"), new ISealConfigToggles.SealToggle(false, "psneak", "golem.prop.sneak"), new ISealConfigToggles.SealToggle(false, "ppro", "golem.prop.provision.wl")};

   public String getKey() {
      return "thaumcraft:use";
   }

   public void tickSeal(World world, ISealEntity seal) {
      if (this.delay++ % 5 == 0) {
         Task oldTask = TaskHandler.getTask(world.field_73011_w.getDimension(), this.watchedTask);
         if (oldTask == null || oldTask.isSuspended() || oldTask.isCompleted()) {
            if (this.getToggles()[5].value != world.func_175623_d(seal.getSealPos().pos)) {
               return;
            }

            Task task = new Task(seal.getSealPos(), seal.getSealPos().pos);
            task.setPriority(seal.getPriority());
            TaskHandler.addTask(world.field_73011_w.getDimension(), task);
            this.watchedTask = task.getId();
         }

      }
   }

   public void onTaskStarted(World world, IGolemAPI golem, Task task) {
   }

   public boolean canBlockBePlaced(World world, Block blockIn, BlockPos pos, EnumFacing side) {
      world.func_180495_p(pos);
      AxisAlignedBB axisalignedbb = blockIn.func_185496_a(blockIn.func_176223_P(), world, pos);
      return axisalignedbb == null || world.func_72917_a(axisalignedbb, (Entity)null);
   }

   public boolean onTaskCompletion(World world, IGolemAPI golem, Task task) {
      if (this.getToggles()[5].value == world.func_175623_d(task.getPos())) {
         if (this.fp == null) {
            this.fp = FakePlayerFactory.get((WorldServer)world, new GameProfile((UUID)null, "FakeThaumcraftGolem"));
            this.fp.field_71135_a = new FakeNetHandlerPlayServer(this.fp.field_71133_b, new NetworkManager(EnumPacketDirection.CLIENTBOUND), this.fp);
         }

         this.fp.func_70080_a(golem.getGolemEntity().field_70165_t, golem.getGolemEntity().field_70163_u, golem.getGolemEntity().field_70161_v, golem.getGolemEntity().field_70177_z, golem.getGolemEntity().field_70125_A);
         world.func_180495_p(task.getPos());
         ItemStack clickStack = golem.getCarrying()[0];
         if (this.filter[0] != null) {
            clickStack = InventoryUtils.findFirstMatchFromFilter(this.filter, this.blacklist, golem.getCarrying(), !this.props[0].value, !this.props[1].value, this.props[2].value, this.props[3].value);
         }

         if (clickStack != null || this.props[6].value) {
            ItemStack ss = null;
            if (clickStack != null) {
               ss = clickStack.func_77946_l();
               golem.dropItem(clickStack.func_77946_l());
            }

            this.fp.func_184611_a(EnumHand.MAIN_HAND, ss);
            this.fp.func_70095_a(this.props[6].value);
            if (this.getToggles()[4].value) {
               this.fp.field_71134_c.func_180784_a(task.getPos(), task.getSealPos().face);
            } else {
               if (this.fp.func_184614_ca() != null && this.fp.func_184614_ca().func_77973_b() instanceof ItemBlock && !this.canBlockBePlaced(world, ((ItemBlock)this.fp.func_184614_ca().func_77973_b()).field_150939_a, task.getPos(), task.getSealPos().face)) {
                  golem.getGolemEntity().func_70107_b(golem.getGolemEntity().field_70165_t + (double)task.getSealPos().face.func_82601_c(), golem.getGolemEntity().field_70163_u + (double)task.getSealPos().face.func_96559_d(), golem.getGolemEntity().field_70161_v + (double)task.getSealPos().face.func_82599_e());
               }

               this.fp.field_71134_c.func_187251_a(this.fp, world, this.fp.func_184614_ca(), EnumHand.MAIN_HAND, task.getPos(), task.getSealPos().face, 0.5F, 0.5F, 0.5F);
            }

            golem.addRankXp(1);
            if (this.fp.func_184614_ca() != null && this.fp.func_184614_ca().field_77994_a <= 0) {
               this.fp.func_184611_a(EnumHand.MAIN_HAND, (ItemStack)null);
            }

            this.dropSomeItems(this.fp, golem);
            golem.swingArm();
         }
      }

      task.setSuspended(true);
      return true;
   }

   private void dropSomeItems(FakePlayer fp2, IGolemAPI golem) {
      int i;
      for(i = 0; i < fp2.field_71071_by.field_70462_a.length; ++i) {
         if (fp2.field_71071_by.field_70462_a[i] != null) {
            if (golem.canCarry(fp2.field_71071_by.field_70462_a[i], true)) {
               fp2.field_71071_by.field_70462_a[i] = golem.holdItem(fp2.field_71071_by.field_70462_a[i]);
            }

            if (fp2.field_71071_by.field_70462_a[i] != null && fp2.field_71071_by.field_70462_a[i].field_77994_a > 0) {
               InventoryUtils.dropItemAtEntity(golem.getGolemWorld(), fp2.field_71071_by.field_70462_a[i], golem.getGolemEntity());
            }

            fp2.field_71071_by.field_70462_a[i] = null;
         }
      }

      for(i = 0; i < fp2.field_71071_by.field_70460_b.length; ++i) {
         if (fp2.field_71071_by.field_70460_b[i] != null) {
            if (golem.canCarry(fp2.field_71071_by.field_70460_b[i], true)) {
               fp2.field_71071_by.field_70460_b[i] = golem.holdItem(fp2.field_71071_by.field_70460_b[i]);
            }

            if (fp2.field_71071_by.field_70462_a[i] != null && fp2.field_71071_by.field_70460_b[i].field_77994_a > 0) {
               InventoryUtils.dropItemAtEntity(golem.getGolemWorld(), fp2.field_71071_by.field_70460_b[i], golem.getGolemEntity());
            }

            fp2.field_71071_by.field_70460_b[i] = null;
         }
      }

   }

   public boolean canGolemPerformTask(IGolemAPI golem, Task task) {
      if (!this.props[6].value) {
         boolean found = InventoryUtils.findFirstMatchFromFilter(this.filter, this.blacklist, golem.getCarrying(), !this.props[0].value, !this.props[1].value, this.props[2].value, this.props[3].value) != null;
         if (!found && this.getToggles()[8].value && !this.blacklist && this.getInv()[0] != null) {
            ISealEntity se = SealHandler.getSealEntity(golem.getGolemWorld().field_73011_w.getDimension(), task.getSealPos());
            if (se != null) {
               ItemStack stack = this.getInv()[0].func_77946_l();
               if (!this.props[0].value) {
                  stack.func_77964_b(32767);
               }

               GolemHelper.requestProvisioning(golem.getGolemWorld(), se, stack);
            }
         }

         return found;
      } else {
         return true;
      }
   }

   public void onTaskSuspension(World world, Task task) {
   }

   public boolean canPlaceAt(World world, BlockPos pos, EnumFacing side) {
      return true;
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
      return new int[]{1, 3, 0, 4};
   }

   public EnumGolemTrait[] getRequiredTags() {
      return new EnumGolemTrait[]{EnumGolemTrait.DEFT, EnumGolemTrait.SMART};
   }

   public EnumGolemTrait[] getForbiddenTags() {
      return null;
   }

   public ISealConfigToggles.SealToggle[] getToggles() {
      return this.props;
   }

   public void setToggle(int indx, boolean value) {
      this.props[indx].setValue(value);
   }
}
