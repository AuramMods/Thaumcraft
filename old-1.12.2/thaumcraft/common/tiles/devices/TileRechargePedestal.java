package thaumcraft.common.tiles.devices;

import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.items.IRechargable;
import thaumcraft.api.items.RechargeHelper;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileRechargePedestal extends TileThaumcraft implements ISidedInventory, IAspectContainer, ITickable {
   private static final int[] slots = new int[]{0};
   private ItemStack[] inventory = new ItemStack[1];
   private String customName;
   int counter = 0;

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return (new AxisAlignedBB((double)this.func_174877_v().func_177958_n(), (double)this.func_174877_v().func_177956_o(), (double)this.func_174877_v().func_177952_p(), (double)(this.func_174877_v().func_177958_n() + 1), (double)(this.func_174877_v().func_177956_o() + 1), (double)(this.func_174877_v().func_177952_p() + 1))).func_72314_b(2.0D, 2.0D, 2.0D);
   }

   public int func_70302_i_() {
      return 1;
   }

   public ItemStack func_70301_a(int par1) {
      return this.inventory[par1];
   }

   public ItemStack func_70298_a(int par1, int par2) {
      if (this.inventory[par1] != null) {
         this.syncTile(false);
         ItemStack itemstack;
         if (this.inventory[par1].field_77994_a <= par2) {
            itemstack = this.inventory[par1];
            this.inventory[par1] = null;
            this.func_70296_d();
            return itemstack;
         } else {
            itemstack = this.inventory[par1].func_77979_a(par2);
            if (this.inventory[par1].field_77994_a == 0) {
               this.inventory[par1] = null;
            }

            this.func_70296_d();
            return itemstack;
         }
      } else {
         return null;
      }
   }

   public ItemStack func_70304_b(int par1) {
      if (this.inventory[par1] != null) {
         ItemStack itemstack = this.inventory[par1];
         this.inventory[par1] = null;
         this.func_70296_d();
         return itemstack;
      } else {
         return null;
      }
   }

   public void func_70299_a(int par1, ItemStack par2ItemStack) {
      this.inventory[par1] = par2ItemStack;
      if (par2ItemStack != null && par2ItemStack.field_77994_a > this.func_70297_j_()) {
         par2ItemStack.field_77994_a = this.func_70297_j_();
      }

      this.func_70296_d();
      this.syncTile(false);
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

   public String func_70005_c_() {
      return this.func_145818_k_() ? this.customName : "container.rechargepedestal";
   }

   public boolean func_145818_k_() {
      return this.customName != null && this.customName.length() > 0;
   }

   public ITextComponent func_145748_c_() {
      return (ITextComponent)(this.func_145818_k_() ? new TextComponentString(this.func_70005_c_()) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]));
   }

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      NBTTagList nbttaglist = nbttagcompound.func_150295_c("Items", 10);
      this.inventory = new ItemStack[this.func_70302_i_()];

      for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
         NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
         byte b0 = nbttagcompound1.func_74771_c("Slot");
         if (b0 >= 0 && b0 < this.inventory.length) {
            this.inventory[b0] = ItemStack.func_77949_a(nbttagcompound1);
         }
      }

   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.inventory.length; ++i) {
         if (this.inventory[i] != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.func_74774_a("Slot", (byte)i);
            this.inventory[i].func_77955_b(nbttagcompound1);
            nbttaglist.func_74742_a(nbttagcompound1);
         }
      }

      nbttagcompound.func_74782_a("Items", nbttaglist);
      return nbttagcompound;
   }

   public void func_145839_a(NBTTagCompound nbtCompound) {
      super.func_145839_a(nbtCompound);
      if (nbtCompound.func_74764_b("CustomName")) {
         this.customName = nbtCompound.func_74779_i("CustomName");
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbtCompound) {
      nbtCompound = super.func_189515_b(nbtCompound);
      if (this.func_145818_k_()) {
         nbtCompound.func_74778_a("CustomName", this.customName);
      }

      return nbtCompound;
   }

   public int func_70297_j_() {
      return 1;
   }

   public void func_73660_a() {
      if (!this.func_145831_w().field_72995_K && this.counter++ % 10 == 0 && this.func_70301_a(0) != null && RechargeHelper.rechargeItem(this.func_145831_w(), this.func_70301_a(0), this.field_174879_c, (EntityPlayer)null, 5) > 0.0F) {
         this.syncTile(false);
         this.func_70296_d();
         ArrayList<Aspect> al = Aspect.getPrimalAspects();
         this.field_145850_b.func_175641_c(this.field_174879_c, this.func_145838_q(), 5, ((Aspect)al.get(this.func_145831_w().field_73012_v.nextInt(al.size()))).getColor());
      }

   }

   public boolean func_70300_a(EntityPlayer par1EntityPlayer) {
      return this.field_145850_b.func_175625_s(this.field_174879_c) != this ? false : par1EntityPlayer.func_174831_c(this.field_174879_c) <= 64.0D;
   }

   public boolean func_94041_b(int par1, ItemStack stack) {
      return stack != null && stack.func_77973_b() instanceof IRechargable;
   }

   public int[] func_180463_a(EnumFacing side) {
      return slots;
   }

   public boolean func_180462_a(int par1, ItemStack stack, EnumFacing par3) {
      return this.func_70301_a(par1) == null && stack.func_77973_b() instanceof IRechargable;
   }

   public boolean func_180461_b(int par1, ItemStack par2ItemStack, EnumFacing par3) {
      return true;
   }

   public AspectList getAspects() {
      if (this.func_70301_a(0) != null && this.func_70301_a(0).func_77973_b() instanceof IRechargable) {
         float c = (float)RechargeHelper.getCharge(this.func_70301_a(0));
         return (new AspectList()).add(Aspect.ENERGY, Math.round(c));
      } else {
         return null;
      }
   }

   public void setAspects(AspectList aspects) {
   }

   public int addToContainer(Aspect tag, int amount) {
      return 0;
   }

   public boolean takeFromContainer(Aspect tag, int amount) {
      return false;
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amount) {
      return false;
   }

   public boolean doesContainerContain(AspectList ot) {
      return false;
   }

   public int containerContains(Aspect tag) {
      return 0;
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }

   public boolean func_145842_c(int i, int j) {
      if (i == 5) {
         if (this.field_145850_b.field_72995_K) {
            FXDispatcher.INSTANCE.visSparkle(this.field_174879_c.func_177958_n() + this.func_145831_w().field_73012_v.nextInt(3) - this.func_145831_w().field_73012_v.nextInt(3), this.field_174879_c.func_177984_a().func_177956_o() + this.func_145831_w().field_73012_v.nextInt(3), this.field_174879_c.func_177952_p() + this.func_145831_w().field_73012_v.nextInt(3) - this.func_145831_w().field_73012_v.nextInt(3), this.field_174879_c.func_177958_n(), this.field_174879_c.func_177984_a().func_177956_o(), this.field_174879_c.func_177952_p(), j);
         }

         return true;
      } else {
         return super.func_145842_c(i, j);
      }
   }
}
