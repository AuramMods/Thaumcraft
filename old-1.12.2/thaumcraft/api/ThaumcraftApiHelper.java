package thaumcraft.api;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.items.ItemGenericEssentiaContainer;
import thaumcraft.api.items.ItemsTC;

public class ThaumcraftApiHelper {
   public static boolean areItemsEqual(ItemStack s1, ItemStack s2) {
      if (s1.func_77984_f() && s2.func_77984_f()) {
         return s1.func_77973_b() == s2.func_77973_b();
      } else {
         return s1.func_77973_b() == s2.func_77973_b() && s1.func_77952_i() == s2.func_77952_i();
      }
   }

   public static boolean containsMatch(boolean strict, ItemStack[] inputs, List<ItemStack> targets) {
      ItemStack[] var3 = inputs;
      int var4 = inputs.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ItemStack input = var3[var5];
         Iterator var7 = targets.iterator();

         while(var7.hasNext()) {
            ItemStack target = (ItemStack)var7.next();
            if (OreDictionary.itemMatches(target, input, strict)) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean areItemStacksEqualForCrafting(ItemStack stack0, Object in) {
      if (stack0 == null && in != null) {
         return false;
      } else if (stack0 != null && in == null) {
         return false;
      } else if (stack0 == null && in == null) {
         return true;
      } else if (in instanceof Object[]) {
         return true;
      } else if (in instanceof String) {
         List<ItemStack> l = OreDictionary.getOres((String)in, false);
         return containsMatch(false, new ItemStack[]{stack0}, l);
      } else if (in instanceof ItemStack) {
         boolean t1 = areItemStackTagsEqualForCrafting(stack0, (ItemStack)in);
         return !t1 ? false : OreDictionary.itemMatches((ItemStack)in, stack0, false);
      } else {
         return false;
      }
   }

   public static boolean areItemStackTagsEqualForCrafting(ItemStack slotItem, ItemStack recipeItem) {
      if (recipeItem != null && slotItem != null) {
         if (recipeItem.func_77978_p() != null && slotItem.func_77978_p() == null) {
            return false;
         } else if (recipeItem.func_77978_p() == null) {
            return true;
         } else {
            Iterator iterator = recipeItem.func_77978_p().func_150296_c().iterator();

            String s;
            do {
               if (!iterator.hasNext()) {
                  return true;
               }

               s = (String)iterator.next();
               if (!slotItem.func_77978_p().func_74764_b(s)) {
                  return false;
               }
            } while(slotItem.func_77978_p().func_74781_a(s).toString().equals(recipeItem.func_77978_p().func_74781_a(s).toString()));

            return false;
         }
      } else {
         return false;
      }
   }

   public static TileEntity getConnectableTile(World world, BlockPos pos, EnumFacing face) {
      TileEntity te = world.func_175625_s(pos.func_177972_a(face));
      return te instanceof IEssentiaTransport && ((IEssentiaTransport)te).isConnectable(face.func_176734_d()) ? te : null;
   }

   public static TileEntity getConnectableTile(IBlockAccess world, BlockPos pos, EnumFacing face) {
      TileEntity te = world.func_175625_s(pos.func_177972_a(face));
      return te instanceof IEssentiaTransport && ((IEssentiaTransport)te).isConnectable(face.func_176734_d()) ? te : null;
   }

   public static RayTraceResult rayTraceIgnoringSource(World world, Vec3d v1, Vec3d v2, boolean bool1, boolean bool2, boolean bool3) {
      if (!Double.isNaN(v1.field_72450_a) && !Double.isNaN(v1.field_72448_b) && !Double.isNaN(v1.field_72449_c)) {
         if (!Double.isNaN(v2.field_72450_a) && !Double.isNaN(v2.field_72448_b) && !Double.isNaN(v2.field_72449_c)) {
            int i = MathHelper.func_76128_c(v2.field_72450_a);
            int j = MathHelper.func_76128_c(v2.field_72448_b);
            int k = MathHelper.func_76128_c(v2.field_72449_c);
            int l = MathHelper.func_76128_c(v1.field_72450_a);
            int i1 = MathHelper.func_76128_c(v1.field_72448_b);
            int j1 = MathHelper.func_76128_c(v1.field_72449_c);
            world.func_180495_p(new BlockPos(l, i1, j1));
            RayTraceResult rayTraceResult2 = null;
            int var14 = 200;

            while(var14-- >= 0) {
               if (Double.isNaN(v1.field_72450_a) || Double.isNaN(v1.field_72448_b) || Double.isNaN(v1.field_72449_c)) {
                  return null;
               }

               if (l != i || i1 != j || j1 != k) {
                  boolean flag6 = true;
                  boolean flag3 = true;
                  boolean flag4 = true;
                  double d0 = 999.0D;
                  double d1 = 999.0D;
                  double d2 = 999.0D;
                  if (i > l) {
                     d0 = (double)l + 1.0D;
                  } else if (i < l) {
                     d0 = (double)l + 0.0D;
                  } else {
                     flag6 = false;
                  }

                  if (j > i1) {
                     d1 = (double)i1 + 1.0D;
                  } else if (j < i1) {
                     d1 = (double)i1 + 0.0D;
                  } else {
                     flag3 = false;
                  }

                  if (k > j1) {
                     d2 = (double)j1 + 1.0D;
                  } else if (k < j1) {
                     d2 = (double)j1 + 0.0D;
                  } else {
                     flag4 = false;
                  }

                  double d3 = 999.0D;
                  double d4 = 999.0D;
                  double d5 = 999.0D;
                  double d6 = v2.field_72450_a - v1.field_72450_a;
                  double d7 = v2.field_72448_b - v1.field_72448_b;
                  double d8 = v2.field_72449_c - v1.field_72449_c;
                  if (flag6) {
                     d3 = (d0 - v1.field_72450_a) / d6;
                  }

                  if (flag3) {
                     d4 = (d1 - v1.field_72448_b) / d7;
                  }

                  if (flag4) {
                     d5 = (d2 - v1.field_72449_c) / d8;
                  }

                  if (d3 == -0.0D) {
                     d3 = -1.0E-4D;
                  }

                  if (d4 == -0.0D) {
                     d4 = -1.0E-4D;
                  }

                  if (d5 == -0.0D) {
                     d5 = -1.0E-4D;
                  }

                  EnumFacing enumfacing;
                  if (d3 < d4 && d3 < d5) {
                     enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                     v1 = new Vec3d(d0, v1.field_72448_b + d7 * d3, v1.field_72449_c + d8 * d3);
                  } else if (d4 < d5) {
                     enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                     v1 = new Vec3d(v1.field_72450_a + d6 * d4, d1, v1.field_72449_c + d8 * d4);
                  } else {
                     enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                     v1 = new Vec3d(v1.field_72450_a + d6 * d5, v1.field_72448_b + d7 * d5, d2);
                  }

                  l = MathHelper.func_76128_c(v1.field_72450_a) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                  i1 = MathHelper.func_76128_c(v1.field_72448_b) - (enumfacing == EnumFacing.UP ? 1 : 0);
                  j1 = MathHelper.func_76128_c(v1.field_72449_c) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                  IBlockState block1 = world.func_180495_p(new BlockPos(l, i1, j1));
                  if (!bool2 || block1.func_185890_d(world, new BlockPos(l, i1, j1)) != null) {
                     if (block1.func_177230_c().func_176209_a(block1, bool1)) {
                        RayTraceResult rayTraceResult1 = block1.func_185910_a(world, new BlockPos(l, i1, j1), v1, v2);
                        if (rayTraceResult1 != null) {
                           return rayTraceResult1;
                        }
                     } else {
                        rayTraceResult2 = new RayTraceResult(Type.MISS, v1, enumfacing, new BlockPos(l, i1, j1));
                     }
                  }
               }
            }

            return bool3 ? rayTraceResult2 : null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static Object getNBTDataFromId(NBTTagCompound nbt, byte id, String key) {
      switch(id) {
      case 1:
         return nbt.func_74771_c(key);
      case 2:
         return nbt.func_74765_d(key);
      case 3:
         return nbt.func_74762_e(key);
      case 4:
         return nbt.func_74763_f(key);
      case 5:
         return nbt.func_74760_g(key);
      case 6:
         return nbt.func_74769_h(key);
      case 7:
         return nbt.func_74770_j(key);
      case 8:
         return nbt.func_74779_i(key);
      case 9:
         return nbt.func_150295_c(key, 10);
      case 10:
         return nbt.func_74781_a(key);
      case 11:
         return nbt.func_74759_k(key);
      default:
         return null;
      }
   }

   public static int setByteInInt(int data, byte b, int index) {
      ByteBuffer bb = ByteBuffer.allocate(4);
      bb.putInt(0, data);
      bb.put(index, b);
      return bb.getInt(0);
   }

   public static byte getByteInInt(int data, int index) {
      ByteBuffer bb = ByteBuffer.allocate(4);
      bb.putInt(0, data);
      return bb.get(index);
   }

   public static long setByteInLong(long data, byte b, int index) {
      ByteBuffer bb = ByteBuffer.allocate(8);
      bb.putLong(0, data);
      bb.put(index, b);
      return bb.getLong(0);
   }

   public static byte getByteInLong(long data, int index) {
      ByteBuffer bb = ByteBuffer.allocate(8);
      bb.putLong(0, data);
      return bb.get(index);
   }

   public static int setNibbleInInt(int data, int nibble, int nibbleIndex) {
      int shift = nibbleIndex * 4;
      return data & ~(15 << shift) | nibble << shift;
   }

   public static int getNibbleInInt(int data, int nibbleIndex) {
      return data >> (nibbleIndex << 2) & 15;
   }

   public static ItemStack makeCrystal(Aspect aspect, int stackSize) {
      if (aspect == null) {
         return null;
      } else {
         ItemStack is = new ItemStack(ItemsTC.crystalEssence, stackSize, 0);
         ((ItemGenericEssentiaContainer)ItemsTC.crystalEssence).setAspects(is, (new AspectList()).add(aspect, 1));
         return is;
      }
   }

   public static ItemStack makeCrystal(Aspect aspect) {
      return makeCrystal(aspect, 1);
   }

   public static List<ItemStack> getOresWithWildCards(String oreDict) {
      if (oreDict.trim().endsWith("*")) {
         ArrayList<ItemStack> ores = new ArrayList();
         String[] names = OreDictionary.getOreNames();
         String m = oreDict.trim().replaceAll("\\*", "");
         String[] var4 = names;
         int var5 = names.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String name = var4[var6];
            if (name.startsWith(m)) {
               ores.addAll(OreDictionary.getOres(name, false));
            }
         }

         return ores;
      } else {
         return OreDictionary.getOres(oreDict, false);
      }
   }
}
