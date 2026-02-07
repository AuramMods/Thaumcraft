package thaumcraft.common.items.curios;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.items.ItemTCBase;

public class ItemPrimordialPearl extends ItemTCBase {
   public ItemPrimordialPearl() {
      super("primordial_pearl", "mote", "nodule", "pearl");
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      switch(itemstack.func_77952_i()) {
      case 1:
         return EnumRarity.RARE;
      case 2:
         return EnumRarity.EPIC;
      default:
         return EnumRarity.UNCOMMON;
      }
   }

   public int getItemStackLimit(ItemStack itemstack) {
      switch(itemstack.func_77952_i()) {
      case 2:
         return 16;
      default:
         return 1;
      }
   }

   public double getDurabilityForDisplay(ItemStack stack) {
      return stack.func_77952_i() == 2 ? super.getDurabilityForDisplay(stack) : (double)getCharge(stack) / (double)getMaxCharge(stack);
   }

   public static int getMaxCharge(ItemStack stack) {
      switch(stack.func_77952_i()) {
      case 0:
         return 64;
      case 1:
         return 512;
      default:
         return 0;
      }
   }

   public static int getCharge(ItemStack stack) {
      int a = 0;
      return stack != null && stack.func_77973_b() == ItemsTC.primordialPearl && stack.func_77942_o() ? stack.func_77978_p().func_74762_e("charge") : a;
   }

   public static void addCharge(ItemStack stack) {
      if (stack != null && stack.func_77973_b() == ItemsTC.primordialPearl && stack.func_77952_i() < 2) {
         int cur = 0;
         if (stack.func_77942_o()) {
            cur = stack.func_77978_p().func_74762_e("charge");
         }

         ++cur;
         if (cur >= getMaxCharge(stack)) {
            stack.func_77982_d((NBTTagCompound)null);
            stack.func_77964_b(stack.func_77952_i() + 1);
         } else {
            stack.func_77983_a("charge", new NBTTagInt(cur));
         }
      }

   }
}
