package thaumcraft.common.tiles.crafting;

import java.util.List;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.codechicken.lib.raytracer.IndexedCuboid6;
import thaumcraft.codechicken.lib.vec.Cuboid6;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.BlockStateUtils;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TileThaumcraft;

public class TilePatternCrafter extends TileThaumcraft implements ITickable {
   public byte type = 0;
   public int count = (new Random(System.currentTimeMillis())).nextInt(20);
   private final InventoryCrafting craftMatrix = new InventoryCrafting(new Container() {
      public boolean func_75145_c(EntityPlayer playerIn) {
         return false;
      }
   }, 3, 3);
   float power = 0.0F;
   public float rot;
   public float rp;
   public int rotTicks = 0;
   ItemStack outStack = null;

   public void readSyncNBT(NBTTagCompound nbt) {
      this.type = nbt.func_74771_c("type");
   }

   public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
      nbt.func_74774_a("type", this.type);
      return nbt;
   }

   public void func_145839_a(NBTTagCompound nbt) {
      this.power = nbt.func_74760_g("power");
      super.func_145839_a(nbt);
   }

   public NBTTagCompound func_189515_b(NBTTagCompound nbt) {
      nbt.func_74776_a("power", this.power);
      return super.func_189515_b(nbt);
   }

   public void func_73660_a() {
      if (this.field_145850_b.field_72995_K) {
         if (this.rotTicks > 0) {
            --this.rotTicks;
            if (this.rotTicks % 5 == 0) {
               this.field_145850_b.func_184134_a((double)this.field_174879_c.func_177958_n() + 0.5D, (double)this.field_174879_c.func_177956_o() + 0.5D, (double)this.field_174879_c.func_177952_p() + 0.5D, SoundsTC.clack, SoundCategory.BLOCKS, 0.4F, 1.7F, false);
            }

            ++this.rp;
         } else {
            this.rp *= 0.8F;
         }

         this.rot += this.rp;
      }

      if (!this.field_145850_b.field_72995_K && this.count++ % 20 == 0 && BlockStateUtils.isEnabled(this.func_145832_p())) {
         if (this.power <= 0.0F) {
            this.power += AuraHelper.drainVis(this.func_145831_w(), this.func_174877_v(), 5.0F, false);
         }

         TileEntity above = this.field_145850_b.func_175625_s(this.func_174877_v().func_177984_a());
         TileEntity below = this.field_145850_b.func_175625_s(this.func_174877_v().func_177977_b());
         if (above != null && above.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN) && below != null && below.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)) {
            IItemHandler ca = (IItemHandler)above.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
            IItemHandler cb = (IItemHandler)below.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

            for(int a = 0; a < ca.getSlots(); ++a) {
               ItemStack as = ca.getStackInSlot(a);
               int amt = 9;
               switch(this.type) {
               case 0:
                  amt = 9;
                  break;
               case 1:
                  amt = 1;
                  break;
               case 2:
               case 3:
                  amt = 2;
                  break;
               case 4:
                  amt = 4;
                  break;
               case 5:
               case 6:
                  amt = 3;
                  break;
               case 7:
               case 8:
                  amt = 6;
                  break;
               case 9:
                  amt = 8;
               }

               if (as != null && InventoryUtils.consumeAmount(ca, as, amt, false, false, true, false, true) && this.craft(as) && this.power >= 1.0F && ItemHandlerHelper.insertItem(cb, this.outStack.func_77946_l(), true) == null) {
                  boolean b = true;

                  int i;
                  for(i = 0; i < 9; ++i) {
                     if (this.craftMatrix.func_70301_a(i) != null && ItemHandlerHelper.insertItem(cb, this.craftMatrix.func_70301_a(i).func_77946_l(), true) != null) {
                        b = false;
                        break;
                     }
                  }

                  if (b) {
                     ItemHandlerHelper.insertItem(cb, this.outStack.func_77946_l(), false);

                     for(i = 0; i < 9; ++i) {
                        if (this.craftMatrix.func_70301_a(i) != null) {
                           ItemHandlerHelper.insertItem(cb, this.craftMatrix.func_70301_a(i).func_77946_l(), false);
                        }
                     }

                     InventoryUtils.consumeAmount(ca, as, amt, false, false, true, false, false);
                     this.field_145850_b.func_175641_c(this.func_174877_v(), this.func_145838_q(), 1, 0);
                     --this.power;
                     break;
                  }
               }
            }
         }
      }

   }

   private boolean craft(ItemStack inStack) {
      int i;
      this.outStack = null;
      this.craftMatrix.func_174888_l();
      int a;
      label131:
      switch(this.type) {
      case 0:
         a = 0;

         while(true) {
            if (a >= 9) {
               break label131;
            }

            this.craftMatrix.func_70299_a(a, ItemHandlerHelper.copyStackWithSize(inStack, 1));
            ++a;
         }
      case 1:
         this.craftMatrix.func_70299_a(0, ItemHandlerHelper.copyStackWithSize(inStack, 1));
         break;
      case 2:
         a = 0;

         while(true) {
            if (a >= 2) {
               break label131;
            }

            this.craftMatrix.func_70299_a(a, ItemHandlerHelper.copyStackWithSize(inStack, 1));
            ++a;
         }
      case 3:
         a = 0;

         while(true) {
            if (a >= 2) {
               break label131;
            }

            this.craftMatrix.func_70299_a(a * 3, ItemHandlerHelper.copyStackWithSize(inStack, 1));
            ++a;
         }
      case 4:
         a = 0;

         while(true) {
            if (a >= 2) {
               break label131;
            }

            for(i = 0; i < 2; ++i) {
               this.craftMatrix.func_70299_a(a + i * 3, ItemHandlerHelper.copyStackWithSize(inStack, 1));
            }

            ++a;
         }
      case 5:
         a = 0;

         while(true) {
            if (a >= 3) {
               break label131;
            }

            this.craftMatrix.func_70299_a(a, ItemHandlerHelper.copyStackWithSize(inStack, 1));
            ++a;
         }
      case 6:
         a = 0;

         while(true) {
            if (a >= 3) {
               break label131;
            }

            this.craftMatrix.func_70299_a(a * 3, ItemHandlerHelper.copyStackWithSize(inStack, 1));
            ++a;
         }
      case 7:
         a = 0;

         while(true) {
            if (a >= 6) {
               break label131;
            }

            this.craftMatrix.func_70299_a(a, ItemHandlerHelper.copyStackWithSize(inStack, 1));
            ++a;
         }
      case 8:
         a = 0;

         while(true) {
            if (a >= 2) {
               break label131;
            }

            for(i = 0; i < 3; ++i) {
               this.craftMatrix.func_70299_a(a + i * 3, ItemHandlerHelper.copyStackWithSize(inStack, 1));
            }

            ++a;
         }
      case 9:
         for(a = 0; a < 9; ++a) {
            if (a != 4) {
               this.craftMatrix.func_70299_a(a, ItemHandlerHelper.copyStackWithSize(inStack, 1));
            }
         }
      }

      this.outStack = CraftingManager.func_77594_a().func_82787_a(this.craftMatrix, this.field_145850_b);
      ItemStack[] aitemstack = CraftingManager.func_77594_a().func_180303_b(this.craftMatrix, this.field_145850_b);

      for(i = 0; i < Math.min(9, aitemstack.length); ++i) {
         ItemStack itemstack1 = this.craftMatrix.func_70301_a(i);
         ItemStack itemstack2 = aitemstack[i];
         if (itemstack1 != null) {
            this.craftMatrix.func_70299_a(i, (ItemStack)null);
         }

         if (itemstack2 != null && this.craftMatrix.func_70301_a(i) == null) {
            this.craftMatrix.func_70299_a(i, itemstack2);
         }
      }

      return this.outStack != null;
   }

   public void cycle() {
      ++this.type;
      if (this.type > 9) {
         this.type = 0;
      }

      this.func_70296_d();
   }

   public boolean func_145842_c(int i, int j) {
      if (i == 1) {
         if (this.field_145850_b.field_72995_K) {
            this.rotTicks = 10;
         }

         return true;
      } else {
         return super.func_145842_c(i, j);
      }
   }

   public RayTraceResult rayTrace(World world, Vec3d vec3d, Vec3d vec3d1, RayTraceResult fullblock) {
      return fullblock;
   }

   public void addTraceableCuboids(List<IndexedCuboid6> cuboids) {
      EnumFacing facing = BlockStateUtils.getFacing(this.func_145832_p());
      cuboids.add(new IndexedCuboid6(0, this.getCuboidByFacing(facing)));
   }

   public Cuboid6 getCuboidByFacing(EnumFacing facing) {
      switch(facing) {
      case EAST:
         return new Cuboid6((double)this.func_174877_v().func_177958_n() + 0.125D, (double)this.func_174877_v().func_177956_o() + 0.125D, (double)this.func_174877_v().func_177952_p() + 0.375D, (double)this.func_174877_v().func_177958_n() + 0.25D, (double)this.func_174877_v().func_177956_o() + 0.375D, (double)this.func_174877_v().func_177952_p() + 0.625D);
      case NORTH:
         return new Cuboid6((double)this.func_174877_v().func_177958_n() + 0.375D, (double)this.func_174877_v().func_177956_o() + 0.125D, (double)this.func_174877_v().func_177952_p() + 0.75D, (double)this.func_174877_v().func_177958_n() + 0.625D, (double)this.func_174877_v().func_177956_o() + 0.375D, (double)this.func_174877_v().func_177952_p() + 0.875D);
      case SOUTH:
         return new Cuboid6((double)this.func_174877_v().func_177958_n() + 0.375D, (double)this.func_174877_v().func_177956_o() + 0.125D, (double)this.func_174877_v().func_177952_p() + 0.125D, (double)this.func_174877_v().func_177958_n() + 0.625D, (double)this.func_174877_v().func_177956_o() + 0.375D, (double)this.func_174877_v().func_177952_p() + 0.25D);
      default:
         return new Cuboid6((double)this.func_174877_v().func_177958_n() + 0.75D, (double)this.func_174877_v().func_177956_o() + 0.125D, (double)this.func_174877_v().func_177952_p() + 0.375D, (double)this.func_174877_v().func_177958_n() + 0.875D, (double)this.func_174877_v().func_177956_o() + 0.375D, (double)this.func_174877_v().func_177952_p() + 0.625D);
      }
   }
}
