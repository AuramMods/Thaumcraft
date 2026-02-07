package thaumcraft.common.lib.enchantment;

import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public enum EnumInfusionEnchantment {
   COLLECTOR(ImmutableSet.of("axe", "pickaxe", "shovel", "weapon"), 1, "INFUSIONENCHANTMENT"),
   DESTRUCTIVE(ImmutableSet.of("axe", "pickaxe", "shovel"), 1, "INFUSIONENCHANTMENT"),
   BURROWING(ImmutableSet.of("axe", "pickaxe"), 1, "INFUSIONENCHANTMENT"),
   SOUNDING(ImmutableSet.of("pickaxe"), 4, "INFUSIONENCHANTMENT"),
   REFINING(ImmutableSet.of("pickaxe"), 4, "INFUSIONENCHANTMENT"),
   ARCING(ImmutableSet.of("weapon"), 4, "INFUSIONENCHANTMENT"),
   ESSENCE(ImmutableSet.of("weapon"), 5, "INFUSIONENCHANTMENT"),
   VISBATTERY(ImmutableSet.of("chargable"), 3, "?"),
   VISCHARGE(ImmutableSet.of("chargable"), 1, "?"),
   SWIFT(ImmutableSet.of("boots"), 4, "IEARMOR"),
   AGILE(ImmutableSet.of("legs"), 1, "IEARMOR"),
   INFESTED(ImmutableSet.of("chest"), 1, "IETAINT");

   public Set<String> toolClasses;
   public int maxLevel;
   public String research;

   private EnumInfusionEnchantment(Set<String> toolClasses, int ml, String research) {
      this.toolClasses = toolClasses;
      this.maxLevel = ml;
      this.research = research;
   }

   public static NBTTagList getInfusionEnchantmentTagList(ItemStack stack) {
      return stack != null && stack.func_77978_p() != null ? stack.func_77978_p().func_150295_c("infench", 10) : null;
   }

   public static List<EnumInfusionEnchantment> getInfusionEnchantments(ItemStack stack) {
      NBTTagList nbttaglist = getInfusionEnchantmentTagList(stack);
      List<EnumInfusionEnchantment> list = new ArrayList();
      if (nbttaglist != null) {
         for(int j = 0; j < nbttaglist.func_74745_c(); ++j) {
            int k = nbttaglist.func_150305_b(j).func_74765_d("id");
            int l = nbttaglist.func_150305_b(j).func_74765_d("lvl");
            if (k >= 0 && k < values().length) {
               list.add(values()[k]);
            }
         }
      }

      return list;
   }

   public static int getInfusionEnchantmentLevel(ItemStack stack, EnumInfusionEnchantment enchantment) {
      NBTTagList nbttaglist = getInfusionEnchantmentTagList(stack);
      new ArrayList();
      if (nbttaglist != null) {
         for(int j = 0; j < nbttaglist.func_74745_c(); ++j) {
            int k = nbttaglist.func_150305_b(j).func_74765_d("id");
            int l = nbttaglist.func_150305_b(j).func_74765_d("lvl");
            if (k >= 0 && k < values().length && values()[k] == enchantment) {
               return l;
            }
         }
      }

      return 0;
   }

   public static void addInfusionEnchantment(ItemStack stack, EnumInfusionEnchantment ie, int level) {
      if (stack != null && level <= ie.maxLevel) {
         NBTTagList nbttaglist = getInfusionEnchantmentTagList(stack);
         if (nbttaglist != null) {
            for(int j = 0; j < nbttaglist.func_74745_c(); ++j) {
               int k = nbttaglist.func_150305_b(j).func_74765_d("id");
               int l = nbttaglist.func_150305_b(j).func_74765_d("lvl");
               if (k == ie.ordinal()) {
                  if (level <= l) {
                     return;
                  }

                  nbttaglist.func_150305_b(j).func_74777_a("lvl", (short)level);
                  stack.func_77983_a("infench", nbttaglist);
                  return;
               }
            }
         } else {
            nbttaglist = new NBTTagList();
         }

         NBTTagCompound nbttagcompound = new NBTTagCompound();
         nbttagcompound.func_74777_a("id", (short)ie.ordinal());
         nbttagcompound.func_74777_a("lvl", (short)level);
         nbttaglist.func_74742_a(nbttagcompound);
         if (nbttaglist.func_74745_c() > 0) {
            stack.func_77983_a("infench", nbttaglist);
         }

      }
   }
}
