package thaumcraft.common.tiles.crafting;

import java.awt.Color;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.container.InventoryFake;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.tiles.TileThaumcraft;

public class TileCrucible extends TileThaumcraft implements ITickable, IFluidHandler, IAspectContainer {
   public short heat;
   public AspectList aspects = new AspectList();
   public final int maxTags = 1000;
   int bellows = -1;
   private int delay = 0;
   public FluidTank tank;
   private long counter;
   int prevcolor;
   int prevx;
   int prevy;

   public TileCrucible() {
      this.tank = new FluidTank(FluidRegistry.WATER, 0, 1000);
      this.counter = -100L;
      this.prevcolor = 0;
      this.prevx = 0;
      this.prevy = 0;
      this.heat = 0;
   }

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      this.heat = nbttagcompound.func_74765_d("Heat");
      this.tank.readFromNBT(nbttagcompound);
      if (nbttagcompound.func_74764_b("Empty")) {
         this.tank.setFluid((FluidStack)null);
      }

      this.aspects.readFromNBT(nbttagcompound);
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.func_74777_a("Heat", this.heat);
      this.tank.writeToNBT(nbttagcompound);
      this.aspects.writeToNBT(nbttagcompound);
      return nbttagcompound;
   }

   public void func_73660_a() {
      ++this.counter;
      int prevheat = this.heat;
      if (!this.field_145850_b.field_72995_K) {
         if (this.tank.getFluidAmount() > 0) {
            IBlockState block = this.field_145850_b.func_180495_p(this.func_174877_v().func_177977_b());
            if (block.func_185904_a() != Material.field_151587_i && block.func_185904_a() != Material.field_151581_o && block.func_177230_c() != BlocksTC.nitor) {
               if (this.heat > 0) {
                  --this.heat;
                  if (this.heat == 149) {
                     this.func_70296_d();
                     this.syncTile(false);
                  }
               }
            } else if (this.heat < 200) {
               ++this.heat;
               if (prevheat < 151 && this.heat >= 151) {
                  this.func_70296_d();
                  this.syncTile(false);
               }
            }
         } else if (this.heat > 0) {
            --this.heat;
         }

         if (this.aspects.visSize() > 1000) {
            this.spillRandom();
         }

         if (this.counter >= 100L && this.heat > 150) {
            this.spillRandom();
            this.counter = 0L;
         }
      } else if (this.tank.getFluidAmount() > 0) {
         this.drawEffects();
      }

      if (this.field_145850_b.field_72995_K && prevheat < 151 && this.heat >= 151) {
         ++this.heat;
      }

   }

   private void drawEffects() {
      int a;
      if (this.heat > 150) {
         FXDispatcher.INSTANCE.crucibleFroth((float)this.field_174879_c.func_177958_n() + 0.2F + this.field_145850_b.field_73012_v.nextFloat() * 0.6F, (float)this.field_174879_c.func_177956_o() + this.getFluidHeight(), (float)this.field_174879_c.func_177952_p() + 0.2F + this.field_145850_b.field_73012_v.nextFloat() * 0.6F);
         if (this.aspects.visSize() > 1000) {
            for(a = 0; a < 2; ++a) {
               FXDispatcher.INSTANCE.crucibleFrothDown((float)this.field_174879_c.func_177958_n(), (float)(this.field_174879_c.func_177956_o() + 1), (float)this.field_174879_c.func_177952_p() + this.field_145850_b.field_73012_v.nextFloat());
               FXDispatcher.INSTANCE.crucibleFrothDown((float)(this.field_174879_c.func_177958_n() + 1), (float)(this.field_174879_c.func_177956_o() + 1), (float)this.field_174879_c.func_177952_p() + this.field_145850_b.field_73012_v.nextFloat());
               FXDispatcher.INSTANCE.crucibleFrothDown((float)this.field_174879_c.func_177958_n() + this.field_145850_b.field_73012_v.nextFloat(), (float)(this.field_174879_c.func_177956_o() + 1), (float)this.field_174879_c.func_177952_p());
               FXDispatcher.INSTANCE.crucibleFrothDown((float)this.field_174879_c.func_177958_n() + this.field_145850_b.field_73012_v.nextFloat(), (float)(this.field_174879_c.func_177956_o() + 1), (float)(this.field_174879_c.func_177952_p() + 1));
            }
         }
      }

      if (this.field_145850_b.field_73012_v.nextInt(6) == 0 && this.aspects.size() > 0) {
         a = this.aspects.getAspects()[this.field_145850_b.field_73012_v.nextInt(this.aspects.size())].getColor() + -16777216;
         int x = 5 + this.field_145850_b.field_73012_v.nextInt(22);
         int y = 5 + this.field_145850_b.field_73012_v.nextInt(22);
         this.delay = this.field_145850_b.field_73012_v.nextInt(10);
         this.prevcolor = a;
         this.prevx = x;
         this.prevy = y;
         Color c = new Color(a);
         float r = (float)c.getRed() / 255.0F;
         float g = (float)c.getGreen() / 255.0F;
         float b = (float)c.getBlue() / 255.0F;
         FXDispatcher.INSTANCE.crucibleBubble((float)this.field_174879_c.func_177958_n() + (float)x / 32.0F + 0.015625F, (float)this.field_174879_c.func_177956_o() + 0.05F + this.getFluidHeight(), (float)this.field_174879_c.func_177952_p() + (float)y / 32.0F + 0.015625F, r, g, b);
      }

   }

   public void ejectItem(ItemStack items) {
      boolean first = true;

      do {
         ItemStack spitout = items.func_77946_l();
         if (spitout.field_77994_a > spitout.func_77976_d()) {
            spitout.field_77994_a = spitout.func_77976_d();
         }

         items.field_77994_a -= spitout.field_77994_a;
         EntitySpecialItem entityitem = new EntitySpecialItem(this.field_145850_b, (double)((float)this.field_174879_c.func_177958_n() + 0.5F), (double)((float)this.field_174879_c.func_177956_o() + 0.71F), (double)((float)this.field_174879_c.func_177952_p() + 0.5F), spitout);
         entityitem.field_70181_x = 0.07500000298023224D;
         entityitem.field_70159_w = first ? 0.0D : (double)((this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.01F);
         entityitem.field_70179_y = first ? 0.0D : (double)((this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.01F);
         this.field_145850_b.func_72838_d(entityitem);
         first = false;
      } while(items.field_77994_a > 0);

   }

   public ItemStack attemptSmelt(ItemStack item, String username) {
      boolean bubble = false;
      boolean craftDone = false;
      int stacksize = item.field_77994_a;
      EntityPlayer player = this.field_145850_b.func_72924_a(username);

      for(int a = 0; a < stacksize; ++a) {
         CrucibleRecipe rc = ThaumcraftCraftingManager.findMatchingCrucibleRecipe(player, this.aspects, item);
         if (rc != null && this.tank.getFluidAmount() > 0) {
            ItemStack out = rc.getRecipeOutput().func_77946_l();
            if (player != null) {
               FMLCommonHandler.instance().firePlayerCraftingEvent(player, out, new InventoryFake(new ItemStack[]{item}));
            }

            this.aspects = rc.removeMatching(this.aspects);
            this.tank.drain(50, true);
            this.ejectItem(out);
            craftDone = true;
            --stacksize;
            this.counter = -250L;
         } else {
            AspectList ot = ThaumcraftCraftingManager.getObjectTags(item);
            if (ot != null && ot.size() != 0) {
               Aspect[] var10 = ot.getAspects();
               int var11 = var10.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  Aspect tag = var10[var12];
                  this.aspects.add(tag, ot.getAmount(tag));
               }

               bubble = true;
               --stacksize;
               this.counter = -150L;
            }
         }
      }

      if (bubble) {
         this.field_145850_b.func_184133_a((EntityPlayer)null, this.field_174879_c, SoundsTC.bubble, SoundCategory.BLOCKS, 0.2F, 1.0F + this.field_145850_b.field_73012_v.nextFloat() * 0.4F);
         this.syncTile(false);
         this.field_145850_b.func_175641_c(this.field_174879_c, BlocksTC.crucible, 2, 1);
      }

      if (craftDone) {
         this.syncTile(false);
         this.field_145850_b.func_175641_c(this.field_174879_c, BlocksTC.crucible, 99, 0);
      }

      this.func_70296_d();
      if (stacksize <= 0) {
         return null;
      } else {
         item.field_77994_a = stacksize;
         return item;
      }
   }

   public void attemptSmelt(EntityItem entity) {
      ItemStack item = entity.func_92059_d();
      NBTTagCompound itemData = entity.getEntityData();
      String username = itemData.func_74779_i("thrower");
      ItemStack res = this.attemptSmelt(item, username);
      if (res != null && res.field_77994_a > 0) {
         item.field_77994_a = res.field_77994_a;
         entity.func_92058_a(item);
      } else {
         entity.func_70106_y();
      }

   }

   public float getFluidHeight() {
      float base = 0.3F + 0.5F * ((float)this.tank.getFluidAmount() / (float)this.tank.getCapacity());
      float out = base + (float)this.aspects.visSize() / 1000.0F * (1.0F - base);
      if (out > 1.0F) {
         out = 1.001F;
      }

      if (out == 1.0F) {
         out = 0.9999F;
      }

      return out;
   }

   public void spillRandom() {
      if (this.aspects.size() > 0) {
         Aspect tag = this.aspects.getAspects()[this.field_145850_b.field_73012_v.nextInt(this.aspects.getAspects().length)];
         this.aspects.remove(tag, 1);
         if (this.field_145850_b.field_73012_v.nextBoolean()) {
            AuraHelper.polluteAura(this.field_145850_b, this.func_174877_v(), 1.0F, true);
         }
      }

      this.func_70296_d();
      this.syncTile(false);
   }

   public void spillRemnants() {
      if (this.tank.getFluidAmount() > 0 || this.aspects.visSize() > 0) {
         this.tank.setFluid((FluidStack)null);
         if (this.aspects.visSize() > 1) {
            AuraHelper.polluteAura(this.field_145850_b, this.func_174877_v(), (float)this.aspects.visSize() * 0.75F, true);
         }

         this.aspects = new AspectList();
         this.field_145850_b.func_175641_c(this.field_174879_c, BlocksTC.crucible, 2, 5);
         this.func_70296_d();
         this.syncTile(false);
      }

   }

   public boolean func_145842_c(int i, int j) {
      if (i == 99) {
         if (this.field_145850_b.field_72995_K) {
            FXDispatcher.INSTANCE.drawBamf((double)this.field_174879_c.func_177958_n() + 0.5D, (double)((float)this.field_174879_c.func_177956_o() + 1.25F), (double)this.field_174879_c.func_177952_p() + 0.5D, true, true, EnumFacing.UP);
            this.field_145850_b.func_184134_a((double)((float)this.field_174879_c.func_177958_n() + 0.5F), (double)((float)this.field_174879_c.func_177956_o() + 0.5F), (double)((float)this.field_174879_c.func_177952_p() + 0.5F), SoundsTC.spill, SoundCategory.BLOCKS, 0.2F, 1.0F, false);
         }

         return true;
      } else if (i == 1) {
         if (this.field_145850_b.field_72995_K) {
            FXDispatcher.INSTANCE.drawBamf(this.field_174879_c.func_177984_a(), true, true, EnumFacing.UP);
         }

         return true;
      } else if (i != 2) {
         return super.func_145842_c(i, j);
      } else {
         this.field_145850_b.func_184134_a((double)((float)this.field_174879_c.func_177958_n() + 0.5F), (double)((float)this.field_174879_c.func_177956_o() + 0.5F), (double)((float)this.field_174879_c.func_177952_p() + 0.5F), SoundsTC.spill, SoundCategory.BLOCKS, 0.2F, 1.0F, false);
         if (this.field_145850_b.field_72995_K) {
            for(int q = 0; q < 10; ++q) {
               FXDispatcher.INSTANCE.crucibleBoil(this.field_174879_c, this, j);
            }
         }

         return true;
      }
   }

   public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
      if (resource != null && !FluidRegistry.getFluidName(resource).equals(FluidRegistry.getFluidName(FluidRegistry.WATER))) {
         return 0;
      } else {
         if (doFill) {
            this.func_70296_d();
            this.syncTile(false);
         }

         return this.tank.fill(resource, doFill);
      }
   }

   public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
      if (resource != null && resource.isFluidEqual(this.tank.getFluid())) {
         if (doDrain) {
            this.func_70296_d();
            this.syncTile(false);
         }

         return this.tank.drain(resource.amount, doDrain);
      } else {
         return null;
      }
   }

   public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
      return this.tank.drain(maxDrain, doDrain);
   }

   public boolean canFill(EnumFacing from, Fluid fluid) {
      return fluid != null && fluid.getUnlocalizedName().equals(FluidRegistry.WATER.getUnlocalizedName());
   }

   public boolean canDrain(EnumFacing from, Fluid fluid) {
      return true;
   }

   public FluidTankInfo[] getTankInfo(EnumFacing from) {
      return new FluidTankInfo[]{this.tank.getInfo()};
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return new AxisAlignedBB((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), (double)(this.field_174879_c.func_177958_n() + 1), (double)(this.field_174879_c.func_177956_o() + 1), (double)(this.field_174879_c.func_177952_p() + 1));
   }

   public AspectList getAspects() {
      return this.aspects;
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
}
