package thaumcraft.common.lib.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.common.entities.EntityFollowingItem;

public class InventoryUtils {
   public static ItemStack setSize(ItemStack in, int size) {
      ItemStack is = in.func_77946_l();
      is.field_77994_a = size;
      return is;
   }

   public static EntityEquipmentSlot isHoldingItem(EntityPlayer player, Item item) {
      if (player != null && item != null) {
         if (player.func_184614_ca() != null && player.func_184614_ca().func_77973_b() == item) {
            return EntityEquipmentSlot.MAINHAND;
         } else {
            return player.func_184592_cb() != null && player.func_184592_cb().func_77973_b() == item ? EntityEquipmentSlot.OFFHAND : null;
         }
      } else {
         return null;
      }
   }

   public static EntityEquipmentSlot isHoldingItem(EntityPlayer player, Class item) {
      if (player != null && item != null) {
         if (player.func_184614_ca() != null && item.isAssignableFrom(player.func_184614_ca().func_77973_b().getClass())) {
            return EntityEquipmentSlot.MAINHAND;
         } else {
            return player.func_184592_cb() != null && item.isAssignableFrom(player.func_184592_cb().func_77973_b().getClass()) ? EntityEquipmentSlot.OFFHAND : null;
         }
      } else {
         return null;
      }
   }

   public static boolean hasRoomFor(ItemStack stack, IInventory inventory, EnumFacing side) {
      if (stack != null) {
         ItemStack is = placeItemStackIntoInventory(stack, inventory, side, false);
         return !ItemStack.func_77989_b(stack, is);
      } else {
         return false;
      }
   }

   public static ItemStack placeItemStackIntoInventory(ItemStack stack, IInventory inventory, EnumFacing side, boolean doit) {
      ItemStack itemstack = stack.func_77946_l();
      ItemStack itemstack1 = insertStack(inventory, itemstack, side, doit);
      if (itemstack1 != null && itemstack1.field_77994_a != 0) {
         return itemstack1.func_77946_l();
      } else {
         if (doit) {
            inventory.func_70296_d();
         }

         return null;
      }
   }

   public static ItemStack insertStack(IInventory inventory, ItemStack stack1, EnumFacing side, boolean doit) {
      int k2;
      if (inventory instanceof ISidedInventory && side != null) {
         ISidedInventory isidedinventory = (ISidedInventory)inventory;
         int[] aint = isidedinventory.func_180463_a(side);
         if (aint != null) {
            for(k2 = 0; k2 < aint.length && stack1 != null && stack1.field_77994_a > 0; ++k2) {
               if (inventory.func_70301_a(aint[k2]) != null && inventory.func_70301_a(aint[k2]).func_77969_a(stack1)) {
                  stack1 = attemptInsertion(inventory, stack1, aint[k2], side, doit);
               }

               if (stack1 == null || stack1.field_77994_a == 0) {
                  break;
               }
            }
         }

         if (aint != null && stack1 != null && stack1.field_77994_a > 0) {
            for(k2 = 0; k2 < aint.length && stack1 != null && stack1.field_77994_a > 0; ++k2) {
               stack1 = attemptInsertion(inventory, stack1, aint[k2], side, doit);
               if (stack1 == null || stack1.field_77994_a == 0) {
                  break;
               }
            }
         }
      } else {
         int k = inventory.func_70302_i_();

         for(int l = 0; l < k && stack1 != null && stack1.field_77994_a > 0; ++l) {
            if (inventory.func_70301_a(l) != null && inventory.func_70301_a(l).func_77969_a(stack1)) {
               stack1 = attemptInsertion(inventory, stack1, l, side, doit);
            }

            if (stack1 == null || stack1.field_77994_a == 0) {
               break;
            }
         }

         if (stack1 != null && stack1.field_77994_a > 0) {
            TileEntityChest dc = null;
            int l;
            if (inventory instanceof TileEntity) {
               dc = getDoubleChest((TileEntity)inventory);
               if (dc != null) {
                  k2 = dc.func_70302_i_();

                  for(l = 0; l < k2 && stack1 != null && stack1.field_77994_a > 0; ++l) {
                     if (dc.func_70301_a(l) != null && dc.func_70301_a(l).func_77969_a(stack1)) {
                        stack1 = attemptInsertion(dc, stack1, l, side, doit);
                     }

                     if (stack1 == null || stack1.field_77994_a == 0) {
                        break;
                     }
                  }
               }
            }

            if (stack1 != null && stack1.field_77994_a > 0) {
               for(k2 = 0; k2 < k && stack1 != null && stack1.field_77994_a > 0; ++k2) {
                  stack1 = attemptInsertion(inventory, stack1, k2, side, doit);
                  if (stack1 == null || stack1.field_77994_a == 0) {
                     break;
                  }
               }

               if (stack1 != null && stack1.field_77994_a > 0 && dc != null) {
                  k2 = dc.func_70302_i_();

                  for(l = 0; l < k2 && stack1 != null && stack1.field_77994_a > 0; ++l) {
                     stack1 = attemptInsertion(dc, stack1, l, side, doit);
                     if (stack1 == null || stack1.field_77994_a == 0) {
                        break;
                     }
                  }
               }
            }
         }
      }

      if (stack1 != null && stack1.field_77994_a == 0) {
         stack1 = null;
      }

      return stack1;
   }

   private static ItemStack attemptInsertion(IInventory inventory, ItemStack stack, int slot, EnumFacing side, boolean doit) {
      ItemStack slotStack = inventory.func_70301_a(slot);
      if (canInsertItemToInventory(inventory, stack, slot, side)) {
         boolean flag = false;
         if (slotStack == null) {
            if (inventory.func_70297_j_() < stack.field_77994_a) {
               ItemStack in = stack.func_77979_a(inventory.func_70297_j_());
               if (doit) {
                  inventory.func_70299_a(slot, in);
               }
            } else {
               if (doit) {
                  inventory.func_70299_a(slot, stack);
               }

               stack = null;
            }

            flag = true;
         } else if (areItemStacksEqualStrict(slotStack, stack)) {
            int k = Math.min(inventory.func_70297_j_() - slotStack.field_77994_a, stack.func_77976_d() - slotStack.field_77994_a);
            int l = Math.min(stack.field_77994_a, k);
            stack.field_77994_a -= l;
            if (doit) {
               slotStack.field_77994_a += l;
            }

            flag = l > 0;
         }

         if (flag && doit) {
            if (inventory instanceof TileEntityHopper) {
               ((TileEntityHopper)inventory).func_145896_c(8);
               inventory.func_70296_d();
            }

            inventory.func_70296_d();
         }
      }

      return stack;
   }

   public static int countItemsInWorld(World world, BlockPos pos, ItemStack stack, double range, boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod) {
      int count = 0;
      List<EntityItem> l = EntityUtils.getEntitiesInRange(world, pos, (Entity)null, EntityItem.class, range);
      Iterator var11 = l.iterator();

      while(var11.hasNext()) {
         EntityItem ei = (EntityItem)var11.next();
         if (ei.func_92059_d() != null && areItemStacksEqual(stack, ei.func_92059_d(), ignoreDamage, ignoreNBT, useOre, useMod)) {
            count += ei.func_92059_d().field_77994_a;
         }
      }

      return count;
   }

   public static int inventoryContainsAmount(IInventory inventory, ItemStack stack, EnumFacing side, boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod) {
      int count = 0;
      if (inventory instanceof ISidedInventory && side != null) {
         ISidedInventory isidedinventory = (ISidedInventory)inventory;
         int[] aint = isidedinventory.func_180463_a(side);

         for(int j = 0; j < aint.length && stack != null && stack.field_77994_a > 0; ++j) {
            ItemStack ts = attemptExtraction(inventory, stack, aint[j], side, ignoreDamage, ignoreNBT, useOre, useMod, false);
            if (ts != null) {
               count += ts.field_77994_a;
            }
         }
      } else {
         int k = inventory.func_70302_i_();

         for(int l = 0; l < k && stack != null && stack.field_77994_a > 0; ++l) {
            ItemStack ts = attemptExtraction(inventory, stack, l, side, ignoreDamage, ignoreNBT, useOre, useMod, false);
            if (ts != null) {
               count += ts.field_77994_a;
            }
         }
      }

      return count;
   }

   public static boolean inventoryContains(IInventory inventory, ItemStack stack, EnumFacing side, boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod) {
      ItemStack s = extractStack(inventory, stack, side, ignoreDamage, ignoreNBT, useOre, useMod, false);
      return s != null && s.field_77994_a > 0;
   }

   public static ItemStack extractStack(IInventory inventory, ItemStack stack1, EnumFacing side, boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod, boolean doit) {
      ItemStack outStack = null;
      if (inventory instanceof ISidedInventory && side != null) {
         ISidedInventory isidedinventory = (ISidedInventory)inventory;
         int[] aint = isidedinventory.func_180463_a(side);

         for(int j = 0; j < aint.length && stack1 != null && stack1.field_77994_a > 0 && outStack == null; ++j) {
            outStack = attemptExtraction(inventory, stack1, aint[j], side, ignoreDamage, ignoreNBT, useOre, useMod, doit);
         }
      } else {
         int k = inventory.func_70302_i_();

         for(int l = 0; l < k && stack1 != null && stack1.field_77994_a > 0 && outStack == null; ++l) {
            outStack = attemptExtraction(inventory, stack1, l, side, ignoreDamage, ignoreNBT, useOre, useMod, doit);
         }
      }

      return outStack != null && outStack.field_77994_a != 0 ? outStack.func_77946_l() : null;
   }

   public static ItemStack attemptExtraction(IInventory inventory, ItemStack stack, int slot, EnumFacing side, boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod, boolean doit) {
      ItemStack slotStack = inventory.func_70301_a(slot);
      ItemStack outStack = stack.func_77946_l();
      if (canExtractItemFromInventory(inventory, slotStack, slot, side)) {
         boolean flag = false;
         if (areItemStacksEqual(slotStack, stack, ignoreDamage, ignoreNBT, useOre, useMod)) {
            outStack = slotStack.func_77946_l();
            outStack.field_77994_a = stack.field_77994_a;
            int k = stack.field_77994_a - slotStack.field_77994_a;
            if (k >= 0) {
               outStack.field_77994_a -= k;
               if (doit) {
                  slotStack = null;
                  inventory.func_70299_a(slot, (ItemStack)null);
               }
            } else if (doit) {
               slotStack.field_77994_a -= outStack.field_77994_a;
               inventory.func_70299_a(slot, slotStack);
            }

            flag = true;
            if (flag && doit) {
               inventory.func_70296_d();
            }

            return outStack;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static boolean canInsertItemToInventory(IInventory inventory, ItemStack stack1, int par2, EnumFacing side) {
      return stack1 != null && inventory.func_94041_b(par2, stack1) && (!(inventory instanceof ISidedInventory) || ((ISidedInventory)inventory).func_180462_a(par2, stack1, side));
   }

   public static boolean canExtractItemFromInventory(IInventory inventory, ItemStack stack1, int par2, EnumFacing side) {
      return stack1 != null && (!(inventory instanceof ISidedInventory) || ((ISidedInventory)inventory).func_180461_b(par2, stack1, side));
   }

   public static boolean compareMultipleItems(ItemStack c1, ItemStack[] c2) {
      if (c1 != null && c1.field_77994_a > 0) {
         ItemStack[] var2 = c2;
         int var3 = c2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ItemStack is = var2[var4];
            if (is != null && c1.func_77969_a(is) && ItemStack.func_77970_a(c1, is)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean areItemStacksEqualStrict(ItemStack stack0, ItemStack stack1) {
      return areItemStacksEqual(stack0, stack1, false, false, false, false);
   }

   public static ItemStack findFirstMatchFromFilter(ItemStack[] filters, boolean blacklist, IInventory inv, EnumFacing face, boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod) {
      return findFirstMatchFromFilter(filters, blacklist, inv, face, ignoreDamage, ignoreNBT, useOre, useMod, false);
   }

   public static ItemStack findFirstMatchFromFilter(ItemStack[] filters, boolean blacklist, IInventory inv, EnumFacing face, boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod, boolean leaveOne) {
      label53:
      for(int a = 0; a < inv.func_70302_i_(); ++a) {
         ItemStack is = inv.func_70301_a(a);
         if (is != null && is.field_77994_a > 0 && (!leaveOne || inventoryContainsAmount(inv, is, face, ignoreDamage, ignoreNBT, useOre, useMod) >= 2)) {
            boolean allow = false;
            boolean allEmpty = true;
            ItemStack[] var13 = filters;
            int var14 = filters.length;

            for(int var15 = 0; var15 < var14; ++var15) {
               ItemStack fs = var13[var15];
               if (fs != null) {
                  allEmpty = false;
                  boolean r = areItemStacksEqual(fs.func_77946_l(), is.func_77946_l(), ignoreDamage, ignoreNBT, useOre, useMod);
                  if (blacklist) {
                     if (r) {
                        continue label53;
                     }

                     allow = true;
                  } else if (r) {
                     return is;
                  }
               }
            }

            if (blacklist && (allow || allEmpty)) {
               return is;
            }
         }
      }

      return null;
   }

   public static ItemStack findFirstMatchFromFilter(ItemStack[] filters, boolean blacklist, ItemStack[] itemStacks, boolean value, boolean ignoreDamage, boolean ignoreNBT, boolean useOre) {
      return (ItemStack)findFirstMatchFromFilterTuple(filters, blacklist, itemStacks, value, ignoreDamage, ignoreNBT, useOre).func_76341_a();
   }

   public static Tuple<ItemStack, ItemStack> findFirstMatchFromFilterTuple(ItemStack[] filters, boolean blacklist, ItemStack[] stacks, boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod) {
      ItemStack[] var7 = stacks;
      int var8 = stacks.length;

      label46:
      for(int var9 = 0; var9 < var8; ++var9) {
         ItemStack is = var7[var9];
         if (is != null && is.field_77994_a > 0) {
            boolean allow = false;
            boolean allEmpty = true;
            ItemStack[] var13 = filters;
            int var14 = filters.length;

            for(int var15 = 0; var15 < var14; ++var15) {
               ItemStack fs = var13[var15];
               if (fs != null) {
                  allEmpty = false;
                  boolean r = areItemStacksEqual(fs.func_77946_l(), is.func_77946_l(), ignoreDamage, ignoreNBT, useOre, useMod);
                  if (blacklist) {
                     if (r) {
                        continue label46;
                     }

                     allow = true;
                  } else if (r) {
                     return new Tuple(is, fs);
                  }
               }
            }

            if (blacklist && (allow || allEmpty)) {
               return new Tuple(is, (Object)null);
            }
         }
      }

      return new Tuple((Object)null, (Object)null);
   }

   public static boolean areItemStacksEqual(ItemStack stack0, ItemStack stack1, boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod) {
      if (stack0 == null && stack1 != null) {
         return false;
      } else if (stack0 != null && stack1 == null) {
         return false;
      } else if (stack0 == null && stack1 == null) {
         return true;
      } else if (useMod) {
         String m1 = "A";
         String m2 = "B";
         String a = stack0.func_77973_b().getRegistryName().func_110624_b();
         if (a != null) {
            m1 = a;
         }

         String b = stack1.func_77973_b().getRegistryName().func_110624_b();
         if (b != null) {
            m2 = b;
         }

         return m1.equals(m2);
      } else {
         if (useOre) {
            int[] od = OreDictionary.getOreIDs(stack0);
            int[] var7 = od;
            int var8 = od.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               int i = var7[var9];
               if (ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{stack1}, OreDictionary.getOres(OreDictionary.getOreName(i), false))) {
                  return true;
               }
            }
         }

         boolean t1 = true;
         if (!ignoreNBT) {
            t1 = ItemStack.func_77970_a(stack0, stack1);
         }

         if (stack0.func_77952_i() == 32767 || stack1.func_77952_i() == 32767) {
            ignoreDamage = true;
         }

         boolean t2 = !ignoreDamage && stack0.func_77952_i() != stack1.func_77952_i();
         return stack0.func_77973_b() != stack1.func_77973_b() ? false : (t2 ? false : t1);
      }
   }

   public static boolean consumeInventoryItem(EntityPlayer player, ItemStack item, boolean nocheck, boolean ore) {
      if (!nocheck && !isPlayerCarryingAmount(player, item, ore)) {
         return false;
      } else {
         int count = item.field_77994_a;

         for(int var2 = 0; var2 < player.field_71071_by.field_70462_a.length; ++var2) {
            if (player.field_71071_by.field_70462_a[var2] != null && player.field_71071_by.field_70462_a[var2].func_77969_a(item) && areItemStacksEqual(player.field_71071_by.field_70462_a[var2], item, false, false, ore, false)) {
               if (player.field_71071_by.field_70462_a[var2].field_77994_a > count) {
                  ItemStack var10000 = player.field_71071_by.field_70462_a[var2];
                  var10000.field_77994_a -= count;
                  count = 0;
               } else {
                  count -= player.field_71071_by.field_70462_a[var2].field_77994_a;
                  player.field_71071_by.field_70462_a[var2] = null;
               }

               if (count <= 0) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static boolean consumeInventoryItem(EntityPlayer player, Item item, int md, int amt) {
      if (!isPlayerCarryingAmount(player, new ItemStack(item, amt, md), false)) {
         return false;
      } else {
         int count = amt;

         for(int var2 = 0; var2 < player.field_71071_by.field_70462_a.length; ++var2) {
            if (player.field_71071_by.field_70462_a[var2] != null && player.field_71071_by.field_70462_a[var2].func_77973_b() == item && player.field_71071_by.field_70462_a[var2].func_77952_i() == md) {
               if (player.field_71071_by.field_70462_a[var2].field_77994_a > count) {
                  ItemStack var10000 = player.field_71071_by.field_70462_a[var2];
                  var10000.field_77994_a -= count;
                  count = 0;
               } else {
                  count -= player.field_71071_by.field_70462_a[var2].field_77994_a;
                  player.field_71071_by.field_70462_a[var2] = null;
               }

               if (count <= 0) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static boolean consumeInventoryItem(EntityPlayer player, Item item, int md) {
      for(int var2 = 0; var2 < player.field_71071_by.field_70462_a.length; ++var2) {
         if (player.field_71071_by.field_70462_a[var2] != null && player.field_71071_by.field_70462_a[var2].func_77973_b() == item && player.field_71071_by.field_70462_a[var2].func_77952_i() == md) {
            if (--player.field_71071_by.field_70462_a[var2].field_77994_a <= 0) {
               player.field_71071_by.field_70462_a[var2] = null;
            }

            return true;
         }
      }

      return false;
   }

   public static void dropItems(World world, BlockPos pos) {
      TileEntity tileEntity = world.func_175625_s(pos);
      if (tileEntity instanceof IInventory) {
         IInventory inventory = (IInventory)tileEntity;

         for(int i = 0; i < inventory.func_70302_i_(); ++i) {
            ItemStack item = inventory.func_70301_a(i);
            if (item != null && item.field_77994_a > 0) {
               float rx = world.field_73012_v.nextFloat() * 0.8F + 0.1F;
               float ry = world.field_73012_v.nextFloat() * 0.8F + 0.1F;
               float rz = world.field_73012_v.nextFloat() * 0.8F + 0.1F;
               EntityItem entityItem = new EntityItem(world, (double)((float)pos.func_177958_n() + rx), (double)((float)pos.func_177956_o() + ry), (double)((float)pos.func_177952_p() + rz), item.func_77946_l());
               float factor = 0.05F;
               entityItem.field_70159_w = world.field_73012_v.nextGaussian() * (double)factor;
               entityItem.field_70181_x = world.field_73012_v.nextGaussian() * (double)factor + 0.20000000298023224D;
               entityItem.field_70179_y = world.field_73012_v.nextGaussian() * (double)factor;
               world.func_72838_d(entityItem);
               inventory.func_70299_a(i, (ItemStack)null);
            }
         }

      }
   }

   public static void dropHarvestsAtPos(World worldIn, BlockPos pos, List<ItemStack> list) {
      dropHarvestsAtPos(worldIn, pos, list, false, 0, (Entity)null);
   }

   public static void dropHarvestsAtPos(World worldIn, BlockPos pos, List<ItemStack> list, boolean followItem, int color, Entity target) {
      Iterator var6 = list.iterator();

      while(var6.hasNext()) {
         ItemStack item = (ItemStack)var6.next();
         if (!worldIn.field_72995_K && worldIn.func_82736_K().func_82766_b("doTileDrops") && !worldIn.restoringBlockSnapshots) {
            float f = 0.5F;
            double d0 = (double)(worldIn.field_73012_v.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(worldIn.field_73012_v.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(worldIn.field_73012_v.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = null;
            if (followItem) {
               entityitem = new EntityFollowingItem(worldIn, (double)pos.func_177958_n() + d0, (double)pos.func_177956_o() + d1, (double)pos.func_177952_p() + d2, item, target, color);
            } else {
               entityitem = new EntityItem(worldIn, (double)pos.func_177958_n() + d0, (double)pos.func_177956_o() + d1, (double)pos.func_177952_p() + d2, item);
            }

            ((EntityItem)entityitem).func_174869_p();
            worldIn.func_72838_d((Entity)entityitem);
         }
      }

   }

   public static void dropItemAtPos(World world, ItemStack item, BlockPos pos) {
      if (!world.field_72995_K && item != null && item.field_77994_a > 0) {
         EntityItem entityItem = new EntityItem(world, (double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + 0.5D, (double)pos.func_177952_p() + 0.5D, item.func_77946_l());
         world.func_72838_d(entityItem);
      }

   }

   public static void dropItemAtEntity(World world, ItemStack item, Entity entity) {
      if (!world.field_72995_K && item != null && item.field_77994_a > 0) {
         EntityItem entityItem = new EntityItem(world, entity.field_70165_t, entity.field_70163_u + (double)(entity.func_70047_e() / 2.0F), entity.field_70161_v, item.func_77946_l());
         world.func_72838_d(entityItem);
      }

   }

   public static void dropItemsAtEntity(World world, BlockPos pos, Entity entity) {
      TileEntity tileEntity = world.func_175625_s(pos);
      if (tileEntity instanceof IInventory && !world.field_72995_K) {
         IInventory inventory = (IInventory)tileEntity;

         for(int i = 0; i < inventory.func_70302_i_(); ++i) {
            ItemStack item = inventory.func_70301_a(i);
            if (item != null && item.field_77994_a > 0) {
               EntityItem entityItem = new EntityItem(world, entity.field_70165_t, entity.field_70163_u + (double)(entity.func_70047_e() / 2.0F), entity.field_70161_v, item.func_77946_l());
               world.func_72838_d(entityItem);
               inventory.func_70299_a(i, (ItemStack)null);
            }
         }

      }
   }

   public static boolean isPlayerCarryingAmount(EntityPlayer player, ItemStack stack, boolean ore) {
      if (stack == null) {
         return false;
      } else {
         int count = stack.field_77994_a;

         for(int var2 = 0; var2 < player.field_71071_by.field_70462_a.length; ++var2) {
            if (player.field_71071_by.field_70462_a[var2] != null && player.field_71071_by.field_70462_a[var2].func_77969_a(stack) && areItemStacksEqual(player.field_71071_by.field_70462_a[var2], stack, false, false, ore, false)) {
               count -= player.field_71071_by.field_70462_a[var2].field_77994_a;
               if (count <= 0) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static int isPlayerCarrying(EntityPlayer player, ItemStack stack) {
      for(int var2 = 0; var2 < player.field_71071_by.field_70462_a.length; ++var2) {
         if (player.field_71071_by.field_70462_a[var2] != null && player.field_71071_by.field_70462_a[var2].func_77969_a(stack)) {
            return var2;
         }
      }

      return -1;
   }

   public static ItemStack damageItem(int par1, ItemStack stack, World world) {
      if (stack.func_77984_f()) {
         if (par1 > 0) {
            int var3 = EnchantmentHelper.func_77506_a(Enchantments.field_185307_s, stack);
            int var4 = 0;

            for(int var5 = 0; var3 > 0 && var5 < par1; ++var5) {
               if (EnchantmentDurability.func_92097_a(stack, var3, world.field_73012_v)) {
                  ++var4;
               }
            }

            par1 -= var4;
            if (par1 <= 0) {
               return stack;
            }
         }

         stack.func_77964_b(stack.func_77952_i() + par1);
         if (stack.func_77952_i() > stack.func_77958_k()) {
            --stack.field_77994_a;
            if (stack.field_77994_a < 0) {
               stack.field_77994_a = 0;
            }

            stack.func_77964_b(0);
         }
      }

      return stack;
   }

   public static void dropItemsWithChance(World world, int x, int y, int z, float chance, int fortune, ArrayList<ItemStack> items) {
      Iterator var7 = items.iterator();

      while(var7.hasNext()) {
         ItemStack item = (ItemStack)var7.next();
         if (world.field_73012_v.nextFloat() <= chance && item.field_77994_a > 0 && !world.field_72995_K && world.func_82736_K().func_82766_b("doTileDrops")) {
            float var6 = 0.7F;
            double var7 = (double)(world.field_73012_v.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
            double var9 = (double)(world.field_73012_v.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
            double var11 = (double)(world.field_73012_v.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
            EntityItem var13 = new EntityItem(world, (double)x + var7, (double)y + var9, (double)z + var11, item);
            var13.func_174867_a(10);
            world.func_72838_d(var13);
         }
      }

   }

   public static TileEntityChest getDoubleChest(TileEntity tile) {
      if (tile != null && tile instanceof TileEntityChest) {
         if (((TileEntityChest)tile).field_145991_k != null) {
            return ((TileEntityChest)tile).field_145991_k;
         }

         if (((TileEntityChest)tile).field_145990_j != null) {
            return ((TileEntityChest)tile).field_145990_j;
         }

         if (((TileEntityChest)tile).field_145992_i != null) {
            return ((TileEntityChest)tile).field_145992_i;
         }

         if (((TileEntityChest)tile).field_145988_l != null) {
            return ((TileEntityChest)tile).field_145988_l;
         }
      }

      return null;
   }

   public static ItemStack cycleItemStack(Object input) {
      return cycleItemStack(input, 0);
   }

   public static ItemStack cycleItemStack(Object input, int counter) {
      ItemStack it = null;
      int idx;
      if (input instanceof ItemStack[]) {
         ItemStack[] q = (ItemStack[])((ItemStack[])input);
         if (q != null && q.length > 0) {
            idx = (int)(((long)counter + System.currentTimeMillis() / 1000L) % (long)q.length);
            it = cycleItemStack(q[idx], counter++);
         }
      } else if (input instanceof ItemStack) {
         it = (ItemStack)input;
         if (it != null && it.func_77973_b() != null && it.func_77952_i() == 32767) {
            List<ItemStack> q = new ArrayList();
            it.func_77973_b().func_150895_a(it.func_77973_b(), it.func_77973_b().func_77640_w(), q);
            if (q != null && q.size() > 0) {
               idx = (int)(((long)counter + System.currentTimeMillis() / 1000L) % (long)q.size());
               it = (ItemStack)q.get(idx);
            }
         } else if (it != null && it.func_77973_b() != null && it.func_77952_i() == 32767 && it.func_77984_f()) {
            int md = (int)(((long)counter + System.currentTimeMillis() / 10L) % (long)it.func_77958_k());
            ItemStack it2 = new ItemStack(it.func_77973_b(), 1, md);
            it2.func_77982_d(it.func_77978_p());
            it = it2;
         }
      } else {
         List q;
         if (input instanceof List) {
            q = (List)input;
            if (q != null && q.size() > 0) {
               idx = (int)(((long)counter + System.currentTimeMillis() / 1000L) % (long)q.size());
               it = cycleItemStack(q.get(idx), counter++);
            }
         } else if (input instanceof String) {
            q = OreDictionary.getOres((String)input, false);
            if (q != null && q.size() > 0) {
               idx = (int)(((long)counter + System.currentTimeMillis() / 1000L) % (long)q.size());
               it = cycleItemStack(q.get(idx), counter++);
            }
         }
      }

      return it;
   }

   public static boolean consumeAmount(IItemHandler inventory, ItemStack stack, int amount, boolean ignoreDamage, boolean ignoreNBT, boolean useOre, boolean useMod, boolean simulate) {
      for(int a = 0; a < inventory.getSlots(); ++a) {
         if (areItemStacksEqual(stack, inventory.getStackInSlot(a), ignoreDamage, ignoreNBT, useOre, useMod)) {
            int s = Math.min(amount, inventory.getStackInSlot(a).field_77994_a);
            ItemStack es = inventory.extractItem(a, s, simulate);
            if (es != null) {
               amount -= es.field_77994_a;
            }
         }

         if (amount <= 0) {
            return true;
         }
      }

      return false;
   }
}
