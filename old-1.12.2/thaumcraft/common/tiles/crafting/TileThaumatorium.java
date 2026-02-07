package thaumcraft.common.tiles.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.IBlockFacing;
import thaumcraft.common.container.InventoryFake;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileThaumatorium extends TileThaumcraft implements IAspectContainer, IEssentiaTransport, ISidedInventory, ITickable {
   public ItemStack inputStack = null;
   public AspectList essentia = new AspectList();
   public ArrayList<Integer> recipeHash = new ArrayList();
   public ArrayList<AspectList> recipeEssentia = new ArrayList();
   public ArrayList<String> recipePlayer = new ArrayList();
   public int currentCraft = -1;
   public int maxRecipes = 1;
   public Aspect currentSuction = null;
   int venting = 0;
   int counter = 0;
   boolean heated = false;
   CrucibleRecipe currentRecipe = null;
   public Container eventHandler;

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return new AxisAlignedBB((double)this.func_174877_v().func_177958_n() - 0.1D, (double)this.func_174877_v().func_177956_o() - 0.1D, (double)this.func_174877_v().func_177952_p() - 0.1D, (double)this.func_174877_v().func_177958_n() + 1.1D, (double)this.func_174877_v().func_177956_o() + 2.1D, (double)this.func_174877_v().func_177952_p() + 1.1D);
   }

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      this.essentia.readFromNBT(nbttagcompound);
      this.maxRecipes = nbttagcompound.func_74771_c("maxrec");
      this.recipeEssentia = new ArrayList();
      this.recipeHash = new ArrayList();
      this.recipePlayer = new ArrayList();
      int[] hashes = nbttagcompound.func_74759_k("recipes");
      if (hashes != null) {
         int[] var3 = hashes;
         int var4 = hashes.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            int hash = var3[var5];
            CrucibleRecipe recipe = ThaumcraftApi.getCrucibleRecipeFromHash(hash);
            if (recipe != null) {
               this.recipeEssentia.add(recipe.aspects.copy());
               this.recipePlayer.add("");
               this.recipeHash.add(hash);
            }
         }
      }

   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.func_74774_a("maxrec", (byte)this.maxRecipes);
      this.essentia.writeToNBT(nbttagcompound);
      int[] hashes = new int[this.recipeHash.size()];
      int a = 0;

      for(Iterator var4 = this.recipeHash.iterator(); var4.hasNext(); ++a) {
         Integer i = (Integer)var4.next();
         hashes[a] = i;
      }

      nbttagcompound.func_74783_a("recipes", hashes);
      return nbttagcompound;
   }

   public void func_145839_a(NBTTagCompound nbtCompound) {
      super.func_145839_a(nbtCompound);
      NBTTagList nbttaglist = nbtCompound.func_150295_c("Items", 10);
      if (nbttaglist.func_74745_c() > 0) {
         this.inputStack = ItemStack.func_77949_a(nbttaglist.func_150305_b(0));
      }

      NBTTagList nbttaglist2 = nbtCompound.func_150295_c("OutputPlayer", 8);

      for(int a = 0; a < nbttaglist2.func_74745_c(); ++a) {
         if (this.recipePlayer.size() > a) {
            this.recipePlayer.set(a, nbttaglist2.func_150307_f(a));
         }
      }

   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbtCompound) {
      super.func_189515_b(nbtCompound);
      NBTTagList nbttaglist = new NBTTagList();
      if (this.inputStack != null) {
         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
         nbttagcompound1.func_74774_a("Slot", (byte)0);
         this.inputStack.func_77955_b(nbttagcompound1);
         nbttaglist.func_74742_a(nbttagcompound1);
      }

      nbtCompound.func_74782_a("Items", nbttaglist);
      NBTTagList nbttaglist2 = new NBTTagList();
      if (this.recipePlayer.size() > 0) {
         for(int a = 0; a < this.recipePlayer.size(); ++a) {
            if (this.recipePlayer.get(a) != null) {
               NBTTagString nbttagcompound1 = new NBTTagString((String)this.recipePlayer.get(a));
               nbttaglist2.func_74742_a(nbttagcompound1);
            }
         }
      }

      nbtCompound.func_74782_a("OutputPlayer", nbttaglist2);
      return nbtCompound;
   }

   boolean checkHeat() {
      Material mat = this.field_145850_b.func_180495_p(this.field_174879_c.func_177979_c(2)).func_185904_a();
      Block bi = this.field_145850_b.func_180495_p(this.field_174879_c.func_177979_c(2)).func_177230_c();
      return mat == Material.field_151587_i || mat == Material.field_151581_o || bi == BlocksTC.nitor;
   }

   public ItemStack getCurrentOutputRecipe() {
      ItemStack out = null;
      if (this.currentCraft >= 0 && this.recipeHash != null && this.recipeHash.size() > 0) {
         CrucibleRecipe recipe = ThaumcraftApi.getCrucibleRecipeFromHash((Integer)this.recipeHash.get(this.currentCraft));
         if (recipe != null) {
            out = recipe.getRecipeOutput().func_77946_l();
         }
      }

      return out;
   }

   public void func_73660_a() {
      if (!this.field_145850_b.field_72995_K) {
         if (this.counter == 0 || this.counter % 40 == 0) {
            this.heated = this.checkHeat();
            this.getUpgrades();
         }

         ++this.counter;
         if (this.heated && !this.gettingPower() && this.counter % 5 == 0 && this.recipeHash != null && this.recipeHash.size() > 0) {
            if (this.inputStack == null) {
               this.currentSuction = null;
               return;
            }

            if (this.currentCraft < 0 || this.currentCraft >= this.recipeHash.size() || this.currentRecipe == null || !this.currentRecipe.catalystMatches(this.inputStack)) {
               for(int a = 0; a < this.recipeHash.size(); ++a) {
                  CrucibleRecipe recipe = ThaumcraftApi.getCrucibleRecipeFromHash((Integer)this.recipeHash.get(a));
                  if (recipe.catalystMatches(this.inputStack)) {
                     this.currentCraft = a;
                     this.currentRecipe = recipe;
                     break;
                  }
               }
            }

            if (this.currentCraft < 0 || this.currentCraft >= this.recipeHash.size()) {
               return;
            }

            TileEntity inventory = this.field_145850_b.func_175625_s(this.field_174879_c.func_177972_a(BlockStateUtils.getFacing(this.func_145832_p())));
            if (inventory != null && inventory instanceof IInventory) {
               ItemStack dropped = this.getCurrentOutputRecipe();
               dropped = InventoryUtils.placeItemStackIntoInventory(dropped, (IInventory)inventory, BlockStateUtils.getFacing(this.func_145832_p()).func_176734_d(), false);
               if (dropped != null) {
                  return;
               }
            }

            boolean done = true;
            this.currentSuction = null;
            Aspect[] var3 = ((AspectList)this.recipeEssentia.get(this.currentCraft)).getAspectsSortedByName();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Aspect aspect = var3[var5];
               if (this.essentia.getAmount(aspect) < ((AspectList)this.recipeEssentia.get(this.currentCraft)).getAmount(aspect)) {
                  this.currentSuction = aspect;
                  done = false;
                  break;
               }
            }

            if (done) {
               this.completeRecipe();
            } else if (this.currentSuction != null) {
               this.fill();
            }
         }
      } else if (this.venting > 0) {
         --this.venting;
         float fx = 0.1F - this.field_145850_b.field_73012_v.nextFloat() * 0.2F;
         float fz = 0.1F - this.field_145850_b.field_73012_v.nextFloat() * 0.2F;
         float fy = 0.1F - this.field_145850_b.field_73012_v.nextFloat() * 0.2F;
         float fx2 = 0.1F - this.field_145850_b.field_73012_v.nextFloat() * 0.2F;
         float fz2 = 0.1F - this.field_145850_b.field_73012_v.nextFloat() * 0.2F;
         float fy2 = 0.1F - this.field_145850_b.field_73012_v.nextFloat() * 0.2F;
         int color = 16777215;
         EnumFacing facing = BlockStateUtils.getFacing(this.func_145832_p());
         FXDispatcher.INSTANCE.drawVentParticles((double)((float)this.field_174879_c.func_177958_n() + 0.5F + fx + (float)facing.func_82601_c() / 2.0F), (double)((float)this.field_174879_c.func_177956_o() + 0.5F + fy), (double)((float)this.field_174879_c.func_177952_p() + 0.5F + fz + (float)facing.func_82599_e() / 2.0F), (double)((float)facing.func_82601_c() / 4.0F + fx2), (double)fy2, (double)((float)facing.func_82599_e() / 4.0F + fz2), color);
      }

   }

   private void completeRecipe() {
      if (this.currentRecipe != null && this.currentCraft < this.recipeHash.size() && this.currentRecipe.matches(this.essentia, this.inputStack) && this.func_70298_a(0, 1) != null) {
         this.essentia = new AspectList();
         ItemStack dropped = this.getCurrentOutputRecipe();
         EntityPlayer p = this.field_145850_b.func_72924_a((String)this.recipePlayer.get(this.currentCraft));
         if (p != null) {
            FMLCommonHandler.instance().firePlayerCraftingEvent(p, dropped, new InventoryFake(new ItemStack[]{this.inputStack}));
         }

         EnumFacing facing = BlockStateUtils.getFacing(this.func_145832_p());
         TileEntity inventory = this.field_145850_b.func_175625_s(this.field_174879_c.func_177972_a(facing));
         if (inventory != null && inventory instanceof IInventory) {
            dropped = InventoryUtils.placeItemStackIntoInventory(dropped, (IInventory)inventory, facing.func_176734_d(), true);
         }

         if (dropped != null) {
            EntityItem ei = new EntityItem(this.field_145850_b, (double)this.field_174879_c.func_177958_n() + 0.5D + (double)facing.func_82601_c() * 0.66D, (double)this.field_174879_c.func_177956_o() + 0.33D + (double)facing.func_176734_d().func_96559_d(), (double)this.field_174879_c.func_177952_p() + 0.5D + (double)facing.func_82599_e() * 0.66D, dropped.func_77946_l());
            ei.field_70159_w = (double)(0.075F * (float)facing.func_82601_c());
            ei.field_70181_x = 0.02500000037252903D;
            ei.field_70179_y = (double)(0.075F * (float)facing.func_82599_e());
            this.field_145850_b.func_72838_d(ei);
            this.field_145850_b.func_175641_c(this.field_174879_c, this.func_145838_q(), 0, 0);
         }

         this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundEvents.field_187659_cY, SoundCategory.BLOCKS, 0.25F, 2.6F + (this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.8F);
         this.currentCraft = -1;
         this.syncTile(false);
         this.func_70296_d();
      }

   }

   void fill() {
      EnumFacing facing = BlockStateUtils.getFacing(this.func_145832_p());
      TileEntity te = null;
      IEssentiaTransport ic = null;

      for(int y = 0; y <= 1; ++y) {
         EnumFacing[] var5 = EnumFacing.field_82609_l;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            EnumFacing dir = var5[var7];
            if (dir != facing && dir != EnumFacing.DOWN && (y != 0 || dir != EnumFacing.UP)) {
               te = ThaumcraftApiHelper.getConnectableTile(this.field_145850_b, this.field_174879_c.func_177981_b(y), dir);
               if (te != null) {
                  ic = (IEssentiaTransport)te;
                  if (ic.getEssentiaAmount(dir.func_176734_d()) > 0 && ic.getSuctionAmount(dir.func_176734_d()) < this.getSuctionAmount((EnumFacing)null) && this.getSuctionAmount((EnumFacing)null) >= ic.getMinimumSuction()) {
                     int ess = ic.takeEssentia(this.currentSuction, 1, dir.func_176734_d());
                     if (ess > 0) {
                        this.addToContainer(this.currentSuction, ess);
                        return;
                     }
                  }
               }
            }
         }
      }

   }

   public int addToContainer(Aspect tt, int am) {
      int ce = this.currentRecipe.aspects.getAmount(tt) - this.essentia.getAmount(tt);
      if (this.currentRecipe != null && ce > 0) {
         int add = Math.min(ce, am);
         this.essentia.add(tt, add);
         this.syncTile(false);
         this.func_70296_d();
         return am - add;
      } else {
         return am;
      }
   }

   public boolean takeFromContainer(Aspect tt, int am) {
      if (this.essentia.getAmount(tt) >= am) {
         this.essentia.remove(tt, am);
         this.syncTile(false);
         this.func_70296_d();
         return true;
      } else {
         return false;
      }
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean doesContainerContain(AspectList ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tt, int am) {
      return this.essentia.getAmount(tt) >= am;
   }

   public int containerContains(Aspect tt) {
      return this.essentia.getAmount(tt);
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }

   public boolean func_145842_c(int i, int j) {
      if (i >= 0) {
         if (this.field_145850_b.field_72995_K) {
            this.venting = 7;
         }

         return true;
      } else {
         return super.func_145842_c(i, j);
      }
   }

   public boolean isConnectable(EnumFacing face) {
      return face != BlockStateUtils.getFacing(this.func_145832_p());
   }

   public boolean canInputFrom(EnumFacing face) {
      return face != BlockStateUtils.getFacing(this.func_145832_p());
   }

   public boolean canOutputTo(EnumFacing face) {
      return false;
   }

   public void setSuction(Aspect aspect, int amount) {
      this.currentSuction = aspect;
   }

   public Aspect getSuctionType(EnumFacing loc) {
      return this.currentSuction;
   }

   public int getSuctionAmount(EnumFacing loc) {
      return this.currentSuction != null ? 128 : 0;
   }

   public Aspect getEssentiaType(EnumFacing loc) {
      return null;
   }

   public int getEssentiaAmount(EnumFacing loc) {
      return 0;
   }

   public int takeEssentia(Aspect aspect, int amount, EnumFacing face) {
      return this.canOutputTo(face) && this.takeFromContainer(aspect, amount) ? amount : 0;
   }

   public int addEssentia(Aspect aspect, int amount, EnumFacing face) {
      return this.canInputFrom(face) ? amount - this.addToContainer(aspect, amount) : 0;
   }

   public int getMinimumSuction() {
      return 0;
   }

   public AspectList getAspects() {
      return this.essentia;
   }

   public void setAspects(AspectList aspects) {
      this.essentia = aspects;
   }

   public int func_70302_i_() {
      return 1;
   }

   public ItemStack func_70301_a(int par1) {
      return this.inputStack;
   }

   public ItemStack func_70298_a(int par1, int par2) {
      if (this.inputStack != null) {
         ItemStack itemstack;
         if (this.inputStack.field_77994_a <= par2) {
            itemstack = this.inputStack;
            this.inputStack = null;
            if (this.eventHandler != null) {
               this.eventHandler.func_75130_a(this);
            }

            return itemstack;
         } else {
            itemstack = this.inputStack.func_77979_a(par2);
            if (this.inputStack.field_77994_a == 0) {
               this.inputStack = null;
            }

            if (this.eventHandler != null) {
               this.eventHandler.func_75130_a(this);
            }

            return itemstack;
         }
      } else {
         return null;
      }
   }

   public ItemStack func_70304_b(int par1) {
      if (this.inputStack != null) {
         ItemStack itemstack = this.inputStack;
         this.inputStack = null;
         return itemstack;
      } else {
         return null;
      }
   }

   public void func_70299_a(int par1, ItemStack par2ItemStack) {
      this.inputStack = par2ItemStack;
      if (par2ItemStack != null && par2ItemStack.field_77994_a > this.func_70297_j_()) {
         par2ItemStack.field_77994_a = this.func_70297_j_();
      }

      if (this.eventHandler != null) {
         this.eventHandler.func_75130_a(this);
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

   public boolean gettingPower() {
      return this.field_145850_b.func_175687_A(this.field_174879_c) > 0 || this.field_145850_b.func_175687_A(this.field_174879_c.func_177977_b()) > 0 || this.field_145850_b.func_175687_A(this.field_174879_c.func_177984_a()) > 0;
   }

   public void getUpgrades() {
      EnumFacing facing = BlockStateUtils.getFacing(this.func_145832_p());
      int mr = 1;

      for(int yy = 0; yy <= 1; ++yy) {
         EnumFacing[] var4 = EnumFacing.field_82609_l;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            EnumFacing dir = var4[var6];
            if (dir != EnumFacing.DOWN && dir != facing) {
               int xx = this.field_174879_c.func_177958_n() + dir.func_82601_c();
               int zz = this.field_174879_c.func_177952_p() + dir.func_82599_e();
               BlockPos bp = new BlockPos(xx, this.field_174879_c.func_177956_o() + yy + dir.func_96559_d(), zz);
               IBlockState bs = this.field_145850_b.func_180495_p(bp);
               if (bs == BlocksTC.brainBox.func_176223_P().func_177226_a(IBlockFacing.FACING, dir.func_176734_d())) {
                  mr += 2;
               }
            }
         }
      }

      if (mr != this.maxRecipes) {
         this.maxRecipes = mr;

         while(this.recipeHash.size() > this.maxRecipes) {
            this.recipeHash.remove(this.recipeHash.size() - 1);
         }

         this.syncTile(false);
         this.func_70296_d();
      }

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
      return "container.alchemyfurnace";
   }

   public boolean func_145818_k_() {
      return false;
   }

   public ITextComponent func_145748_c_() {
      return null;
   }

   public boolean func_180462_a(int index, ItemStack itemStackIn, EnumFacing direction) {
      return true;
   }

   public boolean func_180461_b(int index, ItemStack stack, EnumFacing direction) {
      return true;
   }
}
