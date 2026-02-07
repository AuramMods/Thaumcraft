package thaumcraft.common.tiles.essentia;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.essentia.BlockSmelter;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.TileThaumcraftInventory;
import thaumcraft.common.tiles.devices.TileBellows;

public class TileSmelter extends TileThaumcraftInventory implements ITickable {
   private static final int[] slots_bottom = new int[]{1};
   private static final int[] slots_top = new int[0];
   private static final int[] slots_sides = new int[]{0};
   public AspectList aspects = new AspectList();
   public int vis;
   private int maxVis = 256;
   public int smeltTime = 100;
   boolean speedBoost = false;
   public int furnaceBurnTime;
   public int currentItemBurnTime;
   public int furnaceCookTime;
   int count = 0;
   int bellows = -1;

   public TileSmelter() {
      this.itemStacks = new ItemStack[2];
   }

   public void readSyncNBT(NBTTagCompound nbttagcompound) {
      this.furnaceBurnTime = nbttagcompound.func_74765_d("BurnTime");
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.func_74777_a("BurnTime", (short)this.furnaceBurnTime);
      return nbttagcompound;
   }

   public void func_145839_a(NBTTagCompound nbtCompound) {
      super.func_145839_a(nbtCompound);
      this.speedBoost = nbtCompound.func_74767_n("speedBoost");
      this.furnaceCookTime = nbtCompound.func_74765_d("CookTime");
      this.currentItemBurnTime = TileEntityFurnace.func_145952_a(this.itemStacks[1]);
      this.aspects.readFromNBT(nbtCompound);
      this.vis = this.aspects.visSize();
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbtCompound) {
      nbtCompound = super.func_189515_b(nbtCompound);
      nbtCompound.func_74757_a("speedBoost", this.speedBoost);
      nbtCompound.func_74777_a("CookTime", (short)this.furnaceCookTime);
      this.aspects.writeToNBT(nbtCompound);
      return nbtCompound;
   }

   public void func_73660_a() {
      boolean flag = this.furnaceBurnTime > 0;
      boolean flag1 = false;
      ++this.count;
      if (this.furnaceBurnTime > 0) {
         --this.furnaceBurnTime;
      }

      if (this.field_145850_b != null && !this.field_145850_b.field_72995_K) {
         if (this.bellows < 0) {
            this.checkNeighbours();
         }

         int speed = this.getSpeed();
         if (this.speedBoost) {
            speed = (int)((double)speed * 0.8D);
         }

         if (this.count % speed == 0 && this.aspects.size() > 0) {
            Aspect[] var4 = this.aspects.getAspects();
            int var5 = var4.length;

            int var6;
            for(var6 = 0; var6 < var5; ++var6) {
               Aspect aspect = var4[var6];
               if (this.aspects.getAmount(aspect) > 0 && TileAlembic.processAlembics(this.func_145831_w(), this.func_174877_v(), aspect)) {
                  this.takeFromContainer(aspect, 1);
                  break;
               }
            }

            EnumFacing[] var13 = EnumFacing.field_176754_o;
            var5 = var13.length;

            for(var6 = 0; var6 < var5; ++var6) {
               EnumFacing face = var13[var6];
               IBlockState aux = this.field_145850_b.func_180495_p(this.func_174877_v().func_177972_a(face));
               if (aux.func_177230_c() == BlocksTC.smelterAux && BlockStateUtils.getFacing(aux) == face.func_176734_d()) {
                  Aspect[] var9 = this.aspects.getAspects();
                  int var10 = var9.length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     Aspect aspect = var9[var11];
                     if (this.aspects.getAmount(aspect) > 0 && TileAlembic.processAlembics(this.func_145831_w(), this.func_174877_v().func_177972_a(face), aspect)) {
                        this.takeFromContainer(aspect, 1);
                        break;
                     }
                  }
               }
            }
         }

         if (this.furnaceBurnTime == 0) {
            if (this.canSmelt()) {
               this.currentItemBurnTime = this.furnaceBurnTime = TileEntityFurnace.func_145952_a(this.itemStacks[1]);
               if (this.furnaceBurnTime > 0) {
                  BlockSmelter.setFurnaceState(this.field_145850_b, this.func_174877_v(), true);
                  flag1 = true;
                  this.speedBoost = false;
                  if (this.itemStacks[1] != null) {
                     if (this.itemStacks[1].func_77969_a(new ItemStack(ItemsTC.alumentum))) {
                        this.speedBoost = true;
                     }

                     --this.itemStacks[1].field_77994_a;
                     if (this.itemStacks[1].field_77994_a == 0) {
                        this.itemStacks[1] = this.itemStacks[1].func_77973_b().getContainerItem(this.itemStacks[1]);
                     }
                  }
               } else {
                  BlockSmelter.setFurnaceState(this.field_145850_b, this.func_174877_v(), false);
               }
            } else {
               BlockSmelter.setFurnaceState(this.field_145850_b, this.func_174877_v(), false);
            }
         }

         if (BlockStateUtils.isEnabled(this.func_145832_p()) && this.canSmelt()) {
            ++this.furnaceCookTime;
            if (this.furnaceCookTime >= this.smeltTime) {
               this.furnaceCookTime = 0;
               this.smeltItem();
               flag1 = true;
            }
         } else {
            this.furnaceCookTime = 0;
         }

         if (flag != this.furnaceBurnTime > 0) {
            flag1 = true;
         }
      }

      if (flag1) {
         this.func_70296_d();
      }

   }

   private boolean canSmelt() {
      if (this.itemStacks[0] == null) {
         return false;
      } else {
         AspectList al = ThaumcraftCraftingManager.getObjectTags(this.itemStacks[0]);
         if (al != null && al.size() != 0) {
            int vs = al.visSize();
            if (vs > this.maxVis - this.vis) {
               return false;
            } else {
               this.smeltTime = (int)((float)(vs * 2) * (1.0F - 0.125F * (float)this.bellows));
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public void checkNeighbours() {
      EnumFacing[] faces = EnumFacing.field_176754_o;

      try {
         if (BlockStateUtils.getFacing(this.func_145832_p()) == EnumFacing.NORTH) {
            faces = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST};
         }

         if (BlockStateUtils.getFacing(this.func_145832_p()) == EnumFacing.SOUTH) {
            faces = new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
         }

         if (BlockStateUtils.getFacing(this.func_145832_p()) == EnumFacing.EAST) {
            faces = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.WEST};
         }

         if (BlockStateUtils.getFacing(this.func_145832_p()) == EnumFacing.WEST) {
            faces = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.NORTH};
         }
      } catch (Exception var3) {
      }

      this.bellows = TileBellows.getBellows(this.field_145850_b, this.field_174879_c, faces);
   }

   private int getType() {
      return this.func_145838_q() == BlocksTC.smelterBasic ? 0 : (this.func_145838_q() == BlocksTC.smelterThaumium ? 1 : 2);
   }

   private float getEfficiency() {
      float efficiency = 0.8F;
      if (this.getType() == 1) {
         efficiency = 0.85F;
      }

      if (this.getType() == 2) {
         efficiency = 0.95F;
      }

      return efficiency;
   }

   private int getSpeed() {
      int speed = 20 - (this.getType() == 1 ? 10 : 5);
      return speed;
   }

   public void smeltItem() {
      if (this.canSmelt()) {
         int flux = 0;
         AspectList al = ThaumcraftCraftingManager.getObjectTags(this.itemStacks[0]);
         Aspect[] var3 = al.getAspects();
         int c = var3.length;

         int qq;
         for(int var5 = 0; var5 < c; ++var5) {
            Aspect a = var3[var5];
            if (this.getEfficiency() < 1.0F) {
               qq = al.getAmount(a);

               for(int q = 0; q < qq; ++q) {
                  if (this.field_145850_b.field_73012_v.nextFloat() > (a == Aspect.FLUX ? this.getEfficiency() * 0.66F : this.getEfficiency())) {
                     al.reduce(a, 1);
                     ++flux;
                  }
               }
            }

            this.aspects.add(a, al.getAmount(a));
         }

         if (flux > 0) {
            int pp = 0;
            c = 0;

            while(true) {
               if (c >= flux) {
                  AuraHelper.polluteAura(this.func_145831_w(), this.func_174877_v(), (float)pp, true);
                  break;
               }

               EnumFacing[] var11 = EnumFacing.field_176754_o;
               int var12 = var11.length;
               qq = 0;

               while(true) {
                  if (qq >= var12) {
                     ++pp;
                     break;
                  }

                  EnumFacing face = var11[qq];
                  IBlockState vent = this.field_145850_b.func_180495_p(this.func_174877_v().func_177972_a(face));
                  if (vent.func_177230_c() == BlocksTC.smelterVent && BlockStateUtils.getFacing(vent) == face.func_176734_d() && (double)this.field_145850_b.field_73012_v.nextFloat() < 0.333D) {
                     this.field_145850_b.func_175641_c(this.func_174877_v(), this.func_145838_q(), 1, face.func_176734_d().ordinal());
                     break;
                  }

                  ++qq;
               }

               ++c;
            }
         }

         this.vis = this.aspects.visSize();
         --this.itemStacks[0].field_77994_a;
         if (this.itemStacks[0].field_77994_a <= 0) {
            this.itemStacks[0] = null;
         }
      }

   }

   public static boolean isItemFuel(ItemStack par0ItemStack) {
      return TileEntityFurnace.func_145952_a(par0ItemStack) > 0;
   }

   public boolean func_94041_b(int par1, ItemStack par2ItemStack) {
      if (par1 == 0) {
         AspectList al = ThaumcraftCraftingManager.getObjectTags(par2ItemStack);
         if (al != null && al.size() > 0) {
            return true;
         }
      }

      return par1 == 1 ? isItemFuel(par2ItemStack) : false;
   }

   public int[] func_180463_a(EnumFacing par1) {
      return par1 == EnumFacing.DOWN ? slots_bottom : (par1 == EnumFacing.UP ? slots_top : slots_sides);
   }

   public boolean func_180462_a(int par1, ItemStack par2ItemStack, EnumFacing par3) {
      return par3 == EnumFacing.UP ? false : this.func_94041_b(par1, par2ItemStack);
   }

   public boolean func_180461_b(int par1, ItemStack par2ItemStack, EnumFacing par3) {
      return par3 != EnumFacing.UP || par1 != 1 || par2ItemStack.func_77973_b() == Items.field_151133_ar;
   }

   public boolean takeFromContainer(Aspect tag, int amount) {
      if (this.aspects != null && this.aspects.getAmount(tag) >= amount) {
         this.aspects.remove(tag, amount);
         this.vis = this.aspects.visSize();
         this.func_70296_d();
         return true;
      } else {
         return false;
      }
   }

   @SideOnly(Side.CLIENT)
   public int getCookProgressScaled(int par1) {
      if (this.smeltTime <= 0) {
         this.smeltTime = 1;
      }

      return this.furnaceCookTime * par1 / this.smeltTime;
   }

   @SideOnly(Side.CLIENT)
   public int getVisScaled(int par1) {
      return this.vis * par1 / this.maxVis;
   }

   @SideOnly(Side.CLIENT)
   public int getBurnTimeRemainingScaled(int par1) {
      if (this.currentItemBurnTime == 0) {
         this.currentItemBurnTime = 200;
      }

      return this.furnaceBurnTime * par1 / this.currentItemBurnTime;
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      if (this.field_145850_b != null) {
         this.field_145850_b.func_180500_c(EnumSkyBlock.BLOCK, this.field_174879_c);
      }

   }

   public boolean func_145842_c(int i, int j) {
      if (i != 1) {
         return super.func_145842_c(i, j);
      } else {
         if (this.field_145850_b.field_72995_K) {
            EnumFacing d = EnumFacing.field_82609_l[j];
            this.field_145850_b.func_184134_a((double)this.func_174877_v().func_177958_n() + 0.5D + (double)d.func_176734_d().func_82601_c(), (double)this.func_174877_v().func_177956_o() + 0.5D, (double)this.func_174877_v().func_177952_p() + 0.5D + (double)d.func_176734_d().func_82599_e(), SoundEvents.field_187659_cY, SoundCategory.BLOCKS, 0.25F, 2.6F + (this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.8F, true);

            for(int a = 0; a < 4; ++a) {
               float fx = 0.1F - this.field_145850_b.field_73012_v.nextFloat() * 0.2F;
               float fz = 0.1F - this.field_145850_b.field_73012_v.nextFloat() * 0.2F;
               float fy = 0.1F - this.field_145850_b.field_73012_v.nextFloat() * 0.2F;
               float fx2 = 0.1F - this.field_145850_b.field_73012_v.nextFloat() * 0.2F;
               float fz2 = 0.1F - this.field_145850_b.field_73012_v.nextFloat() * 0.2F;
               float fy2 = 0.1F - this.field_145850_b.field_73012_v.nextFloat() * 0.2F;
               int color = 11184810;
               FXDispatcher.INSTANCE.drawVentParticles((double)((float)this.func_174877_v().func_177958_n() + 0.5F + fx + (float)d.func_176734_d().func_82601_c()), (double)((float)this.func_174877_v().func_177956_o() + 0.5F + fy), (double)((float)this.func_174877_v().func_177952_p() + 0.5F + fz + (float)d.func_176734_d().func_82599_e()), (double)((float)d.func_176734_d().func_82601_c() / 4.0F + fx2), (double)((float)d.func_176734_d().func_96559_d() / 4.0F + fy2), (double)((float)d.func_176734_d().func_82599_e() / 4.0F + fz2), color);
            }
         }

         return true;
      }
   }
}
