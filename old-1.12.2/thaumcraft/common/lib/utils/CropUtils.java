package thaumcraft.common.lib.utils;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockStem;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CropUtils {
   public static ArrayList<String> clickableCrops = new ArrayList();
   public static ArrayList<String> standardCrops = new ArrayList();
   public static ArrayList<String> stackedCrops = new ArrayList();
   public static ArrayList<String> lampBlacklist = new ArrayList();

   public static void addStandardCrop(ItemStack stack, int grownMeta) {
      Block block = Block.func_149634_a(stack.func_77973_b());
      if (block != null) {
         addStandardCrop(block, grownMeta);
      }
   }

   public static void addStandardCrop(Block block, int grownMeta) {
      if (grownMeta == 32767) {
         for(int a = 0; a < 16; ++a) {
            standardCrops.add(block.func_149739_a() + a);
         }
      } else {
         standardCrops.add(block.func_149739_a() + grownMeta);
      }

      if (block instanceof BlockCrops && grownMeta != 7) {
         standardCrops.add(block.func_149739_a() + "7");
      }

   }

   public static void addClickableCrop(ItemStack stack, int grownMeta) {
      if (Block.func_149634_a(stack.func_77973_b()) != null) {
         if (grownMeta == 32767) {
            for(int a = 0; a < 16; ++a) {
               clickableCrops.add(Block.func_149634_a(stack.func_77973_b()).func_149739_a() + a);
            }
         } else {
            clickableCrops.add(Block.func_149634_a(stack.func_77973_b()).func_149739_a() + grownMeta);
         }

         if (Block.func_149634_a(stack.func_77973_b()) instanceof BlockCrops && grownMeta != 7) {
            clickableCrops.add(Block.func_149634_a(stack.func_77973_b()).func_149739_a() + "7");
         }

      }
   }

   public static void addStackedCrop(ItemStack stack, int grownMeta) {
      if (Block.func_149634_a(stack.func_77973_b()) != null) {
         addStackedCrop(Block.func_149634_a(stack.func_77973_b()), grownMeta);
      }
   }

   public static void addStackedCrop(Block block, int grownMeta) {
      if (grownMeta == 32767) {
         for(int a = 0; a < 16; ++a) {
            stackedCrops.add(block.func_149739_a() + a);
         }
      } else {
         stackedCrops.add(block.func_149739_a() + grownMeta);
      }

      if (block instanceof BlockCrops && grownMeta != 7) {
         stackedCrops.add(block.func_149739_a() + "7");
      }

   }

   public static boolean isGrownCrop(World world, BlockPos pos) {
      if (world.func_175623_d(pos)) {
         return false;
      } else {
         boolean found = false;
         IBlockState bs = world.func_180495_p(pos);
         Block bi = bs.func_177230_c();
         int md = bi.func_176201_c(bs);
         if (standardCrops.contains(bi.func_149739_a() + md) || clickableCrops.contains(bi.func_149739_a() + md) || stackedCrops.contains(bi.func_149739_a() + md)) {
            found = true;
         }

         Block biB = world.func_180495_p(pos.func_177977_b()).func_177230_c();
         return bi instanceof IGrowable && !((IGrowable)bi).func_176473_a(world, pos, world.func_180495_p(pos), world.field_72995_K) && !(bi instanceof BlockStem) || bi instanceof BlockCrops && md == 7 && !found || standardCrops.contains(bi.func_149739_a() + md) || clickableCrops.contains(bi.func_149739_a() + md) || stackedCrops.contains(bi.func_149739_a() + md) && biB == bi;
      }
   }

   public static void blacklistLamp(ItemStack stack, int meta) {
      if (Block.func_149634_a(stack.func_77973_b()) != null) {
         if (meta == 32767) {
            for(int a = 0; a < 16; ++a) {
               lampBlacklist.add(Block.func_149634_a(stack.func_77973_b()).func_149739_a() + a);
            }
         } else {
            lampBlacklist.add(Block.func_149634_a(stack.func_77973_b()).func_149739_a() + meta);
         }

      }
   }

   public static boolean doesLampGrow(World world, BlockPos pos) {
      Block bi = world.func_180495_p(pos).func_177230_c();
      int md = bi.func_176201_c(world.func_180495_p(pos));
      return !lampBlacklist.contains(bi.func_149739_a() + md);
   }
}
