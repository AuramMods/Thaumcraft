package thaumcraft.common.entities.construct.golem.seals;

import com.mojang.authlib.GameProfile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.seals.ISealConfigArea;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.entities.construct.golem.gui.SealBaseContainer;
import thaumcraft.common.entities.construct.golem.gui.SealBaseGUI;
import thaumcraft.common.entities.construct.golem.tasks.TaskHandler;
import thaumcraft.common.lib.utils.BlockUtils;

public class SealBreaker extends SealFiltered implements ISealConfigArea, ISealConfigToggles {
   int delay = (new Random(System.nanoTime())).nextInt(42);
   HashMap<Integer, Long> cache = new HashMap();
   ResourceLocation icon = new ResourceLocation("thaumcraft", "items/seals/seal_breaker");
   protected ISealConfigToggles.SealToggle[] props = new ISealConfigToggles.SealToggle[]{new ISealConfigToggles.SealToggle(true, "pmeta", "golem.prop.meta")};

   public String getKey() {
      return "thaumcraft:breaker";
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
      if (!this.cache.containsValue(p.func_177986_g()) && this.isValidBlock(world, p)) {
         t = new Task(seal.getSealPos(), p);
         t.setPriority(seal.getPriority());
         t.setData((int)(world.func_180495_p(p).func_185887_b(world, p) / 3.0F));
         TaskHandler.addTask(world.field_73011_w.getDimension(), t);
         this.cache.put(t.getId(), p.func_177986_g());
      }

   }

   private boolean isValidBlock(World world, BlockPos p) {
      IBlockState bs = world.func_180495_p(p);
      if (!world.func_175623_d(p) && bs.func_185887_b(world, p) >= 0.0F) {
         ItemStack ts = this.getFilterSlot(0);
         if (ts != null) {
            ItemStack fs = BlockUtils.getSilkTouchDrop(bs);
            if (fs == null) {
               fs = new ItemStack(bs.func_177230_c(), 1, !this.getToggles()[0].value ? 32767 : bs.func_177230_c().func_176201_c(bs));
            }

            if (!this.getToggles()[0].value) {
               fs.func_77964_b(32767);
            }

            if (this.isBlacklist()) {
               if (OreDictionary.itemMatches(fs, this.getFilterSlot(0), this.getToggles()[0].value)) {
                  return false;
               }
            } else if (!OreDictionary.itemMatches(fs, this.getFilterSlot(0), this.getToggles()[0].value)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean onTaskCompletion(World world, IGolemAPI golem, Task task) {
      IBlockState bs = world.func_180495_p(task.getPos());
      if (this.cache.containsKey(task.getId()) && this.isValidBlock(world, task.getPos())) {
         FakePlayer fp = FakePlayerFactory.get((WorldServer)world, new GameProfile((UUID)null, "FakeThaumcraftGolem"));
         fp.func_70107_b(golem.getGolemEntity().field_70165_t, golem.getGolemEntity().field_70163_u, golem.getGolemEntity().field_70161_v);
         golem.swingArm();
         if (task.getData() > 0) {
            float bh = bs.func_185887_b(world, task.getPos()) / 3.0F;
            world.func_184133_a((EntityPlayer)null, task.getPos(), bs.func_177230_c().func_185467_w().func_185845_c(), SoundCategory.BLOCKS, (bs.func_177230_c().func_185467_w().func_185843_a() + 0.7F) / 8.0F, bs.func_177230_c().func_185467_w().func_185847_b() * 0.5F);
            BlockUtils.destroyBlockPartially(world, golem.getGolemEntity().func_145782_y(), task.getPos(), (int)(9.0F * (1.0F - (float)task.getData() / bh)));
            task.setLifespan((short)((int)Math.max(task.getLifespan(), 10L)));
            task.setData(task.getData() - 1);
            return false;
         }

         BlockUtils.harvestBlock(world, fp, task.getPos(), true, false, 0);
         golem.addRankXp(1);
         this.cache.remove(task.getId());
      }

      task.setSuspended(true);
      return true;
   }

   public boolean canGolemPerformTask(IGolemAPI golem, Task task) {
      if (this.cache.containsKey(task.getId()) && this.isValidBlock(golem.getGolemWorld(), task.getPos())) {
         return true;
      } else {
         task.setSuspended(true);
         return false;
      }
   }

   public void onTaskSuspension(World world, Task task) {
      this.cache.remove(task.getId());
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
      return new int[]{2, 1, 3, 0, 4};
   }

   public EnumGolemTrait[] getRequiredTags() {
      return new EnumGolemTrait[]{EnumGolemTrait.BREAKER};
   }

   public EnumGolemTrait[] getForbiddenTags() {
      return null;
   }

   public void onTaskStarted(World world, IGolemAPI golem, Task task) {
   }

   public ISealConfigToggles.SealToggle[] getToggles() {
      return this.props;
   }

   public void setToggle(int indx, boolean value) {
      this.props[indx].setValue(value);
   }
}
