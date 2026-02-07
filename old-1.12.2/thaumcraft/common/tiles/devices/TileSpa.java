package thaumcraft.common.tiles.devices;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.items.consumables.ItemBathSalts;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileSpa extends TileThaumcraft implements ISidedInventory, IFluidHandler, ITickable {
   private ItemStack[] itemStacks = new ItemStack[1];
   private boolean mix = true;
   private String customName;
   private int counter = 0;
   public FluidTank tank = new FluidTank(5000);

   public void toggleMix() {
      this.mix = !this.mix;
      this.syncTile(false);
      this.func_70296_d();
   }

   public boolean getMix() {
      return this.mix;
   }

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      this.mix = nbttagcompound.func_74767_n("mix");
      this.tank.setFluid(FluidStack.loadFluidStackFromNBT(nbttagcompound));
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.func_74757_a("mix", this.mix);
      if (this.tank.getFluid() != null) {
         this.tank.getFluid().writeToNBT(nbttagcompound);
      }

      return nbttagcompound;
   }

   public void func_145839_a(NBTTagCompound nbttagcompound) {
      super.func_145839_a(nbttagcompound);
      NBTTagList nbttaglist = nbttagcompound.func_150295_c("Items", 10);
      this.itemStacks = new ItemStack[this.func_70302_i_()];

      for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
         NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
         byte b0 = nbttagcompound1.func_74771_c("Slot");
         if (b0 >= 0 && b0 < this.itemStacks.length) {
            this.itemStacks[b0] = ItemStack.func_77949_a(nbttagcompound1);
         }
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
      super.func_189515_b(nbttagcompound);
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.itemStacks.length; ++i) {
         if (this.itemStacks[i] != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.func_74774_a("Slot", (byte)i);
            this.itemStacks[i].func_77955_b(nbttagcompound1);
            nbttaglist.func_74742_a(nbttagcompound1);
         }
      }

      nbttagcompound.func_74782_a("Items", nbttaglist);
      return nbttagcompound;
   }

   public int func_70302_i_() {
      return 1;
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
            return itemstack;
         } else {
            itemstack = this.itemStacks[par1].func_77979_a(par2);
            if (this.itemStacks[par1].field_77994_a == 0) {
               this.itemStacks[par1] = null;
            }

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

   }

   public int func_70297_j_() {
      return 64;
   }

   public boolean func_70300_a(EntityPlayer par1EntityPlayer) {
      return this.field_145850_b.func_175625_s(this.func_174877_v()) != this ? false : par1EntityPlayer.func_174831_c(this.func_174877_v()) <= 64.0D;
   }

   public boolean func_94041_b(int par1, ItemStack par2ItemStack) {
      return par2ItemStack != null && par2ItemStack.func_77973_b() instanceof ItemBathSalts;
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
      return "thaumcraft.spa";
   }

   public boolean func_145818_k_() {
      return false;
   }

   public ITextComponent func_145748_c_() {
      return null;
   }

   public int[] func_180463_a(EnumFacing side) {
      return side != EnumFacing.UP ? new int[]{0} : new int[0];
   }

   public boolean func_180462_a(int index, ItemStack itemStackIn, EnumFacing side) {
      return side != EnumFacing.UP;
   }

   public boolean func_180461_b(int index, ItemStack stack, EnumFacing side) {
      return side != EnumFacing.UP;
   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K && this.counter++ % 40 == 0 && !this.field_145850_b.func_175640_z(this.field_174879_c) && this.hasIngredients()) {
         Block b = this.field_145850_b.func_180495_p(this.field_174879_c.func_177984_a()).func_177230_c();
         int m = b.func_176201_c(this.field_145850_b.func_180495_p(this.field_174879_c.func_177984_a()));
         Block tb = null;
         if (this.mix) {
            tb = BlocksTC.purifyingFluid;
         } else {
            tb = this.tank.getFluid().getFluid().getBlock();
         }

         if (b == tb && m == 0) {
            for(int xx = -2; xx <= 2; ++xx) {
               for(int zz = -2; zz <= 2; ++zz) {
                  BlockPos p = this.func_174877_v().func_177982_a(xx, 1, zz);
                  if (this.isValidLocation(p, true, tb)) {
                     this.consumeIngredients();
                     this.field_145850_b.func_175656_a(p, tb.func_176223_P());
                     this.checkQuanta(p);
                     return;
                  }
               }
            }
         } else if (this.isValidLocation(this.field_174879_c.func_177984_a(), false, tb)) {
            this.consumeIngredients();
            this.field_145850_b.func_175656_a(this.field_174879_c.func_177984_a(), tb.func_176223_P());
            this.checkQuanta(this.field_174879_c.func_177984_a());
         }
      }

   }

   private void checkQuanta(BlockPos pos) {
      Block b = this.field_145850_b.func_180495_p(pos).func_177230_c();
      if (b instanceof BlockFluidBase) {
         float p = ((BlockFluidBase)b).getQuantaPercentage(this.field_145850_b, pos);
         if (p < 1.0F) {
            int md = (int)(1.0F / p) - 1;
            if (md >= 0 && md < 16) {
               this.field_145850_b.func_175656_a(pos, b.func_176203_a(md));
            }
         }
      }

   }

   private boolean hasIngredients() {
      if (this.mix) {
         if (this.tank.getInfo().fluid == null || !this.tank.getInfo().fluid.containsFluid(new FluidStack(FluidRegistry.WATER, 1000))) {
            return false;
         }

         if (this.itemStacks[0] == null || !(this.itemStacks[0].func_77973_b() instanceof ItemBathSalts)) {
            return false;
         }
      } else if (this.tank.getInfo().fluid == null || !this.tank.getFluid().getFluid().canBePlacedInWorld() || this.tank.getFluidAmount() < 1000) {
         return false;
      }

      return true;
   }

   private void consumeIngredients() {
      if (this.mix) {
         this.func_70298_a(0, 1);
      }

      this.drain(EnumFacing.DOWN, 1000, true);
   }

   private boolean isValidLocation(BlockPos pos, boolean mustBeAdjacent, Block target) {
      if ((target == Blocks.field_150355_j || target == Blocks.field_150358_i) && this.field_145850_b.field_73011_w.func_177500_n()) {
         return false;
      } else {
         Block b = this.field_145850_b.func_180495_p(pos).func_177230_c();
         IBlockState bb = this.field_145850_b.func_180495_p(pos.func_177977_b());
         int m = b.func_176201_c(this.field_145850_b.func_180495_p(pos));
         if (!bb.isSideSolid(this.field_145850_b, pos.func_177977_b(), EnumFacing.UP) || !b.func_176200_f(this.field_145850_b, pos) || b == target && m == 0) {
            return false;
         } else {
            return !mustBeAdjacent ? true : BlockUtils.isBlockTouching(this.field_145850_b, pos, (IBlockState)target.func_176203_a(0));
         }
      }
   }

   public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
      int df = this.tank.fill(resource, doFill);
      if (df > 0 && doFill) {
         this.syncTile(false);
         this.func_70296_d();
      }

      return df;
   }

   public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
      return resource != null && resource.isFluidEqual(this.tank.getFluid()) ? this.tank.drain(resource.amount, doDrain) : null;
   }

   public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
      FluidStack fs = this.tank.drain(maxDrain, doDrain);
      if (fs != null && doDrain) {
         this.syncTile(false);
         this.func_70296_d();
      }

      return fs;
   }

   public boolean canFill(EnumFacing from, Fluid fluid) {
      return true;
   }

   public boolean canDrain(EnumFacing from, Fluid fluid) {
      return from != EnumFacing.UP;
   }

   public FluidTankInfo[] getTankInfo(EnumFacing from) {
      return new FluidTankInfo[]{this.tank.getInfo()};
   }
}
