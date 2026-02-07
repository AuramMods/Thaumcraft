package thaumcraft.common.tiles.devices;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.tiles.TileThaumcraftInventory;

public class TileInfernalFurnace extends TileThaumcraftInventory implements ITickable {
   public int furnaceCookTime = 0;
   public int furnaceMaxCookTime = 0;
   public int speedyTime = 0;
   public int facingX = -5;
   public int facingZ = -5;

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return new AxisAlignedBB((double)this.func_174877_v().func_177958_n() - 1.3D, (double)this.func_174877_v().func_177956_o() - 1.3D, (double)this.func_174877_v().func_177952_p() - 1.3D, (double)this.func_174877_v().func_177958_n() + 2.3D, (double)this.func_174877_v().func_177956_o() + 2.3D, (double)this.func_174877_v().func_177952_p() + 2.3D);
   }

   public TileInfernalFurnace() {
      this.itemStacks = new ItemStack[32];
   }

   public int[] func_180463_a(EnumFacing par1) {
      return par1 == EnumFacing.UP ? new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31} : new int[0];
   }

   public boolean func_180461_b(int par1, ItemStack par2ItemStack, EnumFacing par3) {
      return false;
   }

   public void func_145839_a(NBTTagCompound nbttagcompound) {
      super.func_145839_a(nbttagcompound);
      this.furnaceCookTime = nbttagcompound.func_74765_d("CookTime");
      this.speedyTime = nbttagcompound.func_74765_d("SpeedyTime");
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
      super.func_189515_b(nbttagcompound);
      nbttagcompound.func_74777_a("CookTime", (short)this.furnaceCookTime);
      nbttagcompound.func_74777_a("SpeedyTime", (short)this.speedyTime);
      return nbttagcompound;
   }

   public void func_73660_a() {
      if (this.facingX == -5) {
         this.setFacing();
      }

      if (!this.field_145850_b.field_72995_K) {
         boolean cookedflag = false;
         if (this.furnaceCookTime > 0) {
            --this.furnaceCookTime;
            cookedflag = true;
         }

         if (this.furnaceMaxCookTime == 0) {
            this.furnaceMaxCookTime = this.calcCookTime();
         }

         if (this.furnaceCookTime > this.furnaceMaxCookTime) {
            this.furnaceCookTime = this.furnaceMaxCookTime;
         }

         int a;
         if (this.furnaceCookTime == 0 && cookedflag) {
            for(a = 0; a < this.func_70302_i_(); ++a) {
               if (this.itemStacks[a] != null) {
                  ItemStack itemstack = FurnaceRecipes.func_77602_a().func_151395_a(this.itemStacks[a]);
                  if (itemstack != null) {
                     if (this.speedyTime > 0) {
                        --this.speedyTime;
                     }

                     this.ejectItem(itemstack.func_77946_l(), this.itemStacks[a]);
                     this.field_145850_b.func_175641_c(this.func_174877_v(), BlocksTC.infernalFurnace, 3, 0);
                     if (this.func_145831_w().field_73012_v.nextInt(20) == 0) {
                        AuraHelper.polluteAura(this.func_145831_w(), this.func_174877_v().func_177972_a(this.getFacing().func_176734_d()), 1.0F, true);
                     }

                     --this.itemStacks[a].field_77994_a;
                     if (this.itemStacks[a].field_77994_a <= 0) {
                        this.itemStacks[a] = null;
                     }
                     break;
                  }
               }
            }
         }

         if (this.speedyTime <= 0) {
            this.speedyTime = (int)AuraHelper.drainVis(this.func_145831_w(), this.func_174877_v(), 20.0F, false);
         }

         if (this.furnaceCookTime == 0 && !cookedflag) {
            for(a = 0; a < this.func_70302_i_(); ++a) {
               if (this.itemStacks[a] != null && this.canSmelt(a)) {
                  this.furnaceMaxCookTime = this.calcCookTime();
                  this.furnaceCookTime = this.furnaceMaxCookTime;
                  break;
               }

               if (this.itemStacks[a] != null && !this.canSmelt(a)) {
                  this.destroyItem(a);
                  this.func_70296_d();
                  break;
               }
            }
         }
      }

   }

   private int getBellows() {
      int bellows = 0;
      EnumFacing[] var2 = EnumFacing.field_82609_l;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumFacing dir = var2[var4];
         if (dir != EnumFacing.UP) {
            BlockPos p2 = this.field_174879_c.func_177967_a(dir, 2);
            TileEntity tile = this.field_145850_b.func_175625_s(p2);
            if (tile != null && tile instanceof TileBellows && BlockStateUtils.getFacing(this.field_145850_b.func_180495_p(p2)) == dir.func_176734_d() && this.field_145850_b.func_175687_A(p2) == 0) {
               ++bellows;
            }
         }
      }

      return Math.min(3, bellows);
   }

   private int calcCookTime() {
      return (this.speedyTime > 0 ? 80 : 140) - 20 * this.getBellows();
   }

   public boolean addItemsToInventory(ItemStack items) {
      for(int a = 0; a < this.func_70302_i_(); ++a) {
         if (this.itemStacks[a] != null && this.itemStacks[a].func_77969_a(items) && this.itemStacks[a].field_77994_a + items.field_77994_a <= items.func_77976_d()) {
            ItemStack var10000 = this.itemStacks[a];
            var10000.field_77994_a += items.field_77994_a;
            if (!this.canSmelt(a)) {
               this.destroyItem(a);
            }

            this.func_70296_d();
            return true;
         }

         if (this.itemStacks[a] == null) {
            this.func_70299_a(a, items);
            if (!this.canSmelt(a)) {
               this.destroyItem(a);
            }

            this.func_70296_d();
            return true;
         }
      }

      return false;
   }

   private void destroyItem(int slot) {
      this.itemStacks[slot] = null;
      this.field_145850_b.func_184134_a((double)((float)this.field_174879_c.func_177958_n() + 0.5F), (double)((float)this.field_174879_c.func_177956_o() + 0.5F), (double)((float)this.field_174879_c.func_177952_p() + 0.5F), SoundEvents.field_187659_cY, SoundCategory.BLOCKS, 0.3F, 2.6F + (this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.8F, false);
      double var21 = (double)((float)this.field_174879_c.func_177958_n() + this.field_145850_b.field_73012_v.nextFloat());
      double var22 = (double)(this.field_174879_c.func_177956_o() + 1);
      double var23 = (double)((float)this.field_174879_c.func_177952_p() + this.field_145850_b.field_73012_v.nextFloat());
      this.field_145850_b.func_175688_a(EnumParticleTypes.LAVA, var21, var22, var23, 0.0D, 0.0D, 0.0D, new int[0]);
   }

   public void ejectItem(ItemStack items, ItemStack furnaceItemStack) {
      if (items != null) {
         ItemStack bit = items.func_77946_l();
         int bellows = this.getBellows();
         float lx = 0.5F;
         lx += (float)this.facingX * 1.2F;
         float lz = 0.5F;
         lz += (float)this.facingZ * 1.2F;
         float mx = this.facingX == 0 ? (this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.03F : (float)this.facingX * 0.13F;
         float mz = this.facingZ == 0 ? (this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.03F : (float)this.facingZ * 0.13F;
         EntityItem entityitem = new EntityItem(this.field_145850_b, (double)((float)this.field_174879_c.func_177958_n() + lx), (double)((float)this.field_174879_c.func_177956_o() + 0.4F), (double)((float)this.field_174879_c.func_177952_p() + lz), items);
         entityitem.field_70159_w = (double)mx;
         entityitem.field_70179_y = (double)mz;
         entityitem.field_70181_x = 0.0D;
         this.field_145850_b.func_72838_d(entityitem);
         if (ThaumcraftApi.getSmeltingBonus(furnaceItemStack) != null) {
            ItemStack bonus = ThaumcraftApi.getSmeltingBonus(furnaceItemStack).func_77946_l();
            if (bonus != null) {
               if (bellows == 0) {
                  if (this.field_145850_b.field_73012_v.nextInt(4) == 0) {
                     ++bonus.field_77994_a;
                  }
               } else {
                  for(int a = 0; a < bellows; ++a) {
                     if (this.field_145850_b.field_73012_v.nextFloat() < 0.44F) {
                        ++bonus.field_77994_a;
                     }
                  }
               }
            }

            if (bonus != null && bonus.field_77994_a > 0) {
               mx = this.facingX == 0 ? (this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.03F : (float)this.facingX * 0.13F;
               mz = this.facingZ == 0 ? (this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.03F : (float)this.facingZ * 0.13F;
               EntityItem entityitem2 = new EntityItem(this.field_145850_b, (double)((float)this.field_174879_c.func_177958_n() + lx), (double)((float)this.field_174879_c.func_177956_o() + 0.4F), (double)((float)this.field_174879_c.func_177952_p() + lz), bonus);
               entityitem2.field_70159_w = (double)mx;
               entityitem2.field_70179_y = (double)mz;
               entityitem2.field_70181_x = 0.0D;
               this.field_145850_b.func_72838_d(entityitem2);
            }
         }

         int var2 = items.field_77994_a;
         float var3 = FurnaceRecipes.func_77602_a().func_151398_b(bit);
         int var4;
         if (var3 == 0.0F) {
            var2 = 0;
         } else if (var3 < 1.0F) {
            var4 = MathHelper.func_76141_d((float)var2 * var3);
            if (var4 < MathHelper.func_76123_f((float)var2 * var3) && (float)Math.random() < (float)var2 * var3 - (float)var4) {
               ++var4;
            }

            var2 = var4;
         }

         while(var2 > 0) {
            var4 = EntityXPOrb.func_70527_a(var2);
            var2 -= var4;
            EntityXPOrb xp = new EntityXPOrb(this.field_145850_b, (double)((float)this.field_174879_c.func_177958_n() + lx), (double)((float)this.field_174879_c.func_177956_o() + 0.4F), (double)((float)this.field_174879_c.func_177952_p() + lz), var4);
            mx = this.facingX == 0 ? (this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.025F : (float)this.facingX * 0.13F;
            mz = this.facingZ == 0 ? (this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.025F : (float)this.facingZ * 0.13F;
            xp.field_70159_w = (double)mx;
            xp.field_70179_y = (double)mz;
            xp.field_70181_x = 0.0D;
            this.field_145850_b.func_72838_d(xp);
         }

      }
   }

   private boolean canSmelt(int slotIn) {
      if (this.itemStacks[slotIn] == null) {
         return false;
      } else {
         ItemStack itemstack = FurnaceRecipes.func_77602_a().func_151395_a(this.itemStacks[slotIn]);
         return itemstack != null;
      }
   }

   private void setFacing() {
      this.facingX = 0;
      this.facingZ = 0;
      EnumFacing face = this.getFacing().func_176734_d();
      this.facingX = face.func_82601_c();
      this.facingZ = face.func_82599_e();
   }

   public boolean func_145842_c(int i, int j) {
      if (i != 3) {
         return super.func_145842_c(i, j);
      } else {
         if (this.field_145850_b.field_72995_K) {
            for(int a = 0; a < 5; ++a) {
               FXDispatcher.INSTANCE.furnaceLavaFx(this.field_174879_c.func_177958_n(), this.field_174879_c.func_177956_o(), this.field_174879_c.func_177952_p(), this.facingX, this.facingZ);
               this.field_145850_b.func_184134_a((double)((float)this.field_174879_c.func_177958_n() + 0.5F), (double)((float)this.field_174879_c.func_177956_o() + 0.5F), (double)((float)this.field_174879_c.func_177952_p() + 0.5F), SoundEvents.field_187662_cZ, SoundCategory.BLOCKS, 0.1F + this.field_145850_b.field_73012_v.nextFloat() * 0.1F, 0.9F + this.field_145850_b.field_73012_v.nextFloat() * 0.15F, false);
            }
         }

         return true;
      }
   }
}
