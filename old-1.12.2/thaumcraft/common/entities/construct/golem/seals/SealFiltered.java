package thaumcraft.common.entities.construct.golem.seals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealConfigFilter;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.ISealGui;
import thaumcraft.common.entities.construct.golem.gui.SealBaseContainer;
import thaumcraft.common.entities.construct.golem.gui.SealBaseGUI;

public abstract class SealFiltered implements ISeal, ISealGui, ISealConfigFilter {
   ItemStack[] filter = new ItemStack[this.getFilterSize()];
   boolean blacklist = true;

   public void readCustomNBT(NBTTagCompound nbt) {
      NBTTagList nbttaglist = nbt.func_150295_c("Items", 10);
      this.filter = new ItemStack[this.getFilterSize()];

      for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
         NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
         byte b0 = nbttagcompound1.func_74771_c("Slot");
         if (b0 >= 0 && b0 < this.filter.length) {
            this.filter[b0] = ItemStack.func_77949_a(nbttagcompound1);
         }
      }

      this.blacklist = nbt.func_74767_n("bl");
   }

   public void writeCustomNBT(NBTTagCompound nbt) {
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.filter.length; ++i) {
         if (this.filter[i] != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.func_74774_a("Slot", (byte)i);
            this.filter[i].func_77955_b(nbttagcompound1);
            nbttaglist.func_74742_a(nbttagcompound1);
         }
      }

      nbt.func_74782_a("Items", nbttaglist);
      nbt.func_74757_a("bl", this.blacklist);
   }

   public Object returnContainer(World world, EntityPlayer player, BlockPos pos, EnumFacing side, ISealEntity seal) {
      return new SealBaseContainer(player.field_71071_by, world, seal);
   }

   @SideOnly(Side.CLIENT)
   public Object returnGui(World world, EntityPlayer player, BlockPos pos, EnumFacing side, ISealEntity seal) {
      return new SealBaseGUI(player.field_71071_by, world, seal);
   }

   public int[] getGuiCategories() {
      return new int[]{0};
   }

   public int getFilterSize() {
      return 1;
   }

   public ItemStack[] getInv() {
      return this.filter;
   }

   public ItemStack getFilterSlot(int i) {
      return this.filter[i];
   }

   public void setFilterSlot(int i, ItemStack stack) {
      this.filter[i] = stack == null ? null : stack.func_77946_l();
   }

   public boolean isBlacklist() {
      return this.blacklist;
   }

   public void setBlacklist(boolean black) {
      this.blacklist = black;
   }

   public boolean hasStacksizeLimiters() {
      return false;
   }
}
