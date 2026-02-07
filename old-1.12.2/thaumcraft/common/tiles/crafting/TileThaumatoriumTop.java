package thaumcraft.common.tiles.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileThaumatoriumTop extends TileThaumcraft implements IAspectContainer, IEssentiaTransport, ISidedInventory, ITickable {
   public TileThaumatorium thaumatorium = null;

   public void func_73660_a() {
      if (this.thaumatorium == null) {
         TileEntity tile = this.field_145850_b.func_175625_s(this.field_174879_c.func_177977_b());
         if (tile != null && tile instanceof TileThaumatorium) {
            this.thaumatorium = (TileThaumatorium)tile;
         }
      }

   }

   public int addToContainer(Aspect tt, int am) {
      return this.thaumatorium == null ? am : this.thaumatorium.addToContainer(tt, am);
   }

   public boolean takeFromContainer(Aspect tt, int am) {
      return this.thaumatorium == null ? false : this.thaumatorium.takeFromContainer(tt, am);
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean doesContainerContain(AspectList ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tt, int am) {
      return this.thaumatorium == null ? false : this.thaumatorium.doesContainerContainAmount(tt, am);
   }

   public int containerContains(Aspect tt) {
      return this.thaumatorium == null ? 0 : this.thaumatorium.containerContains(tt);
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }

   public boolean isConnectable(EnumFacing face) {
      return this.thaumatorium == null ? false : this.thaumatorium.isConnectable(face);
   }

   public boolean canInputFrom(EnumFacing face) {
      return this.thaumatorium == null ? false : this.thaumatorium.canInputFrom(face);
   }

   public boolean canOutputTo(EnumFacing face) {
      return false;
   }

   public void setSuction(Aspect aspect, int amount) {
      if (this.thaumatorium != null) {
         this.thaumatorium.setSuction(aspect, amount);
      }
   }

   public Aspect getSuctionType(EnumFacing loc) {
      return this.thaumatorium == null ? null : this.thaumatorium.getSuctionType(loc);
   }

   public int getSuctionAmount(EnumFacing loc) {
      return this.thaumatorium == null ? 0 : this.thaumatorium.getSuctionAmount(loc);
   }

   public Aspect getEssentiaType(EnumFacing loc) {
      return null;
   }

   public int getEssentiaAmount(EnumFacing loc) {
      return 0;
   }

   public int takeEssentia(Aspect aspect, int amount, EnumFacing face) {
      return this.thaumatorium == null ? 0 : this.thaumatorium.takeEssentia(aspect, amount, face);
   }

   public int addEssentia(Aspect aspect, int amount, EnumFacing face) {
      return this.thaumatorium == null ? 0 : this.thaumatorium.addEssentia(aspect, amount, face);
   }

   public int getMinimumSuction() {
      return 0;
   }

   public AspectList getAspects() {
      return this.thaumatorium == null ? null : this.thaumatorium.essentia;
   }

   public void setAspects(AspectList aspects) {
      if (this.thaumatorium != null) {
         this.thaumatorium.setAspects(aspects);
      }
   }

   public int func_70302_i_() {
      return 1;
   }

   public ItemStack func_70301_a(int par1) {
      return this.thaumatorium == null ? null : this.thaumatorium.func_70301_a(par1);
   }

   public ItemStack func_70298_a(int par1, int par2) {
      return this.thaumatorium == null ? null : this.thaumatorium.func_70298_a(par1, par2);
   }

   public ItemStack func_70304_b(int par1) {
      return this.thaumatorium == null ? null : this.thaumatorium.func_70304_b(par1);
   }

   public void func_70299_a(int par1, ItemStack par2ItemStack) {
      if (this.thaumatorium != null) {
         this.thaumatorium.func_70299_a(par1, par2ItemStack);
      }
   }

   public int func_70297_j_() {
      return 64;
   }

   public boolean func_70300_a(EntityPlayer par1EntityPlayer) {
      return this.field_145850_b.func_175625_s(this.field_174879_c) != this ? false : par1EntityPlayer.func_174831_c(this.field_174879_c) <= 64.0D;
   }

   public boolean func_94041_b(int par1, ItemStack par2ItemStack) {
      return true;
   }

   public int[] func_180463_a(EnumFacing side) {
      return new int[]{0};
   }

   public boolean func_180462_a(int index, ItemStack itemStackIn, EnumFacing direction) {
      return true;
   }

   public boolean func_180461_b(int index, ItemStack stack, EnumFacing direction) {
      return true;
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
      return null;
   }

   public boolean func_145818_k_() {
      return false;
   }

   public ITextComponent func_145748_c_() {
      return null;
   }
}
