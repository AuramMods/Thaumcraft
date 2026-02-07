package thaumcraft.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class TileThaumcraftInventory extends TileThaumcraft implements ISidedInventory {
   protected ItemStack[] itemStacks = new ItemStack[1];
   protected String customName;
   protected int[] syncedSlots = new int[0];

   public int func_70302_i_() {
      return this.itemStacks.length;
   }

   public ItemStack func_70301_a(int par1) {
      return this.itemStacks[par1];
   }

   public ItemStack func_70298_a(int par1, int par2) {
      if (this.itemStacks[par1] != null) {
         ItemStack itemstack;
         if (this.itemStacks[par1].field_77994_a <= par2) {
            itemstack = this.itemStacks[par1];
            this.itemStacks[par1] = null;
            this.func_70296_d();
            return itemstack;
         } else {
            itemstack = this.itemStacks[par1].func_77979_a(par2);
            if (this.itemStacks[par1].field_77994_a == 0) {
               this.itemStacks[par1] = null;
            }

            this.func_70296_d();
            return itemstack;
         }
      } else {
         return null;
      }
   }

   public ItemStack func_70304_b(int par1) {
      if (this.itemStacks[par1] != null) {
         ItemStack itemstack = this.itemStacks[par1];
         this.itemStacks[par1] = null;
         this.func_70296_d();
         return itemstack;
      } else {
         return null;
      }
   }

   public void func_70299_a(int par1, ItemStack par2ItemStack) {
      this.itemStacks[par1] = par2ItemStack;
      if (par2ItemStack != null && par2ItemStack.field_77994_a > this.func_70297_j_()) {
         par2ItemStack.field_77994_a = this.func_70297_j_();
      }

      this.func_70296_d();
   }

   public String func_70005_c_() {
      return this.func_145818_k_() ? this.customName : "container.thaumcraft";
   }

   public boolean func_145818_k_() {
      return this.customName != null && this.customName.length() > 0;
   }

   public ITextComponent func_145748_c_() {
      return this.func_145818_k_() ? new TextComponentTranslation(this.func_70005_c_(), new Object[0]) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]);
   }

   private boolean isSyncedSlot(int slot) {
      int[] var2 = this.syncedSlots;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int s = var2[var4];
         if (s == slot) {
            return true;
         }
      }

      return false;
   }

   public void readSyncNBT(NBTTagCompound nbtCompound) {
      NBTTagList nbttaglist = nbtCompound.func_150295_c("ItemsSynced", 10);
      this.itemStacks = new ItemStack[this.func_70302_i_()];

      for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
         if (this.isSyncedSlot(i)) {
            NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
            byte b0 = nbttagcompound1.func_74771_c("Slot");
            if (b0 >= 0 && b0 < this.itemStacks.length) {
               this.itemStacks[b0] = ItemStack.func_77949_a(nbttagcompound1);
            }
         }
      }

   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbtCompound) {
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.itemStacks.length; ++i) {
         if (this.itemStacks[i] != null && this.isSyncedSlot(i)) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.func_74774_a("Slot", (byte)i);
            this.itemStacks[i].func_77955_b(nbttagcompound1);
            nbttaglist.func_74742_a(nbttagcompound1);
         }
      }

      nbtCompound.func_74782_a("ItemsSynced", nbttaglist);
      return nbtCompound;
   }

   public void func_145839_a(NBTTagCompound nbtCompound) {
      super.func_145839_a(nbtCompound);
      if (nbtCompound.func_74764_b("CustomName")) {
         this.customName = nbtCompound.func_74779_i("CustomName");
      }

      NBTTagList nbttaglist = nbtCompound.func_150295_c("Items", 10);

      for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
         if (!this.isSyncedSlot(i)) {
            NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
            byte b0 = nbttagcompound1.func_74771_c("Slot");
            if (b0 >= 0 && b0 < this.itemStacks.length) {
               this.itemStacks[b0] = ItemStack.func_77949_a(nbttagcompound1);
            }
         }
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbtCompound) {
      super.func_189515_b(nbtCompound);
      if (this.func_145818_k_()) {
         nbtCompound.func_74778_a("CustomName", this.customName);
      }

      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.itemStacks.length; ++i) {
         if (this.itemStacks[i] != null && !this.isSyncedSlot(i)) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.func_74774_a("Slot", (byte)i);
            this.itemStacks[i].func_77955_b(nbttagcompound1);
            nbttaglist.func_74742_a(nbttagcompound1);
         }
      }

      nbtCompound.func_74782_a("Items", nbttaglist);
      return nbtCompound;
   }

   public int func_70297_j_() {
      return 64;
   }

   public boolean func_70300_a(EntityPlayer par1EntityPlayer) {
      return this.field_145850_b.func_175625_s(this.func_174877_v()) != this ? false : par1EntityPlayer.func_174831_c(this.func_174877_v()) <= 64.0D;
   }

   public boolean func_94041_b(int par1, ItemStack par2ItemStack) {
      return true;
   }

   public int[] func_180463_a(EnumFacing par1) {
      return new int[]{0};
   }

   public void func_174889_b(EntityPlayer player) {
   }

   public void func_174886_c(EntityPlayer player) {
   }

   public int func_174887_a_(int id) {
      return 0;
   }

   public void func_174885_b(int id, int value) {
   }

   public int func_174890_g() {
      return 0;
   }

   public void func_174888_l() {
   }

   public boolean func_180462_a(int par1, ItemStack par2ItemStack, EnumFacing par3) {
      return this.func_94041_b(par1, par2ItemStack);
   }

   public boolean func_180461_b(int par1, ItemStack par2ItemStack, EnumFacing par3) {
      return true;
   }
}
