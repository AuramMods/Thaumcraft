package thaumcraft.common.lib.crafting;

import baubles.api.IBauble;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.items.IRechargable;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

public class InfusionEnchantmentRecipe extends InfusionRecipe {
   EnumInfusionEnchantment enchantment;

   public InfusionEnchantmentRecipe(EnumInfusionEnchantment ench, AspectList as, Object[] components) {
      super(ench.research, (Object)null, 4, as, (Object)null, components);
      this.enchantment = ench;
   }

   public InfusionEnchantmentRecipe(InfusionEnchantmentRecipe recipe, ItemStack in) {
      super(recipe.enchantment.research, (Object)null, recipe.instability, recipe.aspects, in, recipe.components);
      this.enchantment = recipe.enchantment;
   }

   public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, EntityPlayer player) {
      if (central != null && ThaumcraftCapabilities.knowsResearch(player, this.research)) {
         if (EnumInfusionEnchantment.getInfusionEnchantmentLevel(central, this.enchantment) >= this.enchantment.maxLevel) {
            return false;
         } else {
            Multimap itemMods;
            if (!this.enchantment.toolClasses.contains("all")) {
               itemMods = central.func_111283_C(EntityEquipmentSlot.MAINHAND);
               boolean cool = false;
               if (itemMods != null && itemMods.containsKey(SharedMonsterAttributes.field_111264_e.func_111108_a()) && this.enchantment.toolClasses.contains("weapon")) {
                  cool = true;
               }

               if (!cool && central.func_77973_b() instanceof ItemTool) {
                  Set<String> tcs = ((ItemTool)central.func_77973_b()).getToolClasses(central);
                  Iterator var8 = tcs.iterator();

                  while(var8.hasNext()) {
                     String tc = (String)var8.next();
                     if (this.enchantment.toolClasses.contains(tc)) {
                        cool = true;
                        break;
                     }
                  }
               }

               String at;
               if (!cool && central.func_77973_b() instanceof ItemArmor) {
                  at = "none";
                  switch(((ItemArmor)central.func_77973_b()).field_77881_a) {
                  case HEAD:
                     at = "helm";
                     break;
                  case CHEST:
                     at = "chest";
                     break;
                  case LEGS:
                     at = "legs";
                     break;
                  case FEET:
                     at = "boots";
                  }

                  if (this.enchantment.toolClasses.contains("armor") || this.enchantment.toolClasses.contains(at)) {
                     cool = true;
                  }
               }

               if (!cool && central.func_77973_b() instanceof IBauble) {
                  at = "none";
                  switch(((IBauble)central.func_77973_b()).getBaubleType(central)) {
                  case AMULET:
                     at = "amulet";
                     break;
                  case BELT:
                     at = "belt";
                     break;
                  case RING:
                     at = "ring";
                  }

                  if (this.enchantment.toolClasses.contains("bauble") || this.enchantment.toolClasses.contains(at)) {
                     cool = true;
                  }
               }

               if (!cool && central.func_77973_b() instanceof IRechargable && this.enchantment.toolClasses.contains("chargable")) {
                  cool = true;
               }

               if (!cool) {
                  return false;
               }
            }

            itemMods = null;
            ArrayList<ItemStack> ii = new ArrayList();
            Iterator var19 = input.iterator();

            while(var19.hasNext()) {
               ItemStack is = (ItemStack)var19.next();
               ii.add(is.func_77946_l());
            }

            Object[] var20 = this.components;
            int var16 = var20.length;

            for(int var17 = 0; var17 < var16; ++var17) {
               Object comp = var20[var17];
               boolean b = false;

               for(int a = 0; a < ii.size(); ++a) {
                  ItemStack i2 = ((ItemStack)ii.get(a)).func_77946_l();
                  if (ThaumcraftApiHelper.areItemStacksEqualForCrafting(i2, comp)) {
                     ii.remove(a);
                     b = true;
                     break;
                  }
               }

               if (!b) {
                  return false;
               }
            }

            return ii.size() == 0;
         }
      } else {
         return false;
      }
   }

   public Object getRecipeOutput(EntityPlayer player, ItemStack input, ArrayList<ItemStack> comps) {
      if (input == null) {
         return null;
      } else {
         ItemStack out = input.func_77946_l();
         int cl = EnumInfusionEnchantment.getInfusionEnchantmentLevel(out, this.enchantment);
         if (cl >= this.enchantment.maxLevel) {
            return null;
         } else {
            List<EnumInfusionEnchantment> el = EnumInfusionEnchantment.getInfusionEnchantments(input);
            Random rand = new Random(System.nanoTime());
            if (rand.nextInt(10) < el.size()) {
               int base = 1;
               if (input.func_77942_o()) {
                  base += input.func_77978_p().func_74771_c("TC.WARP");
               }

               out.func_77983_a("TC.WARP", new NBTTagByte((byte)base));
            }

            EnumInfusionEnchantment.addInfusionEnchantment(out, this.enchantment, cl + 1);
            return out;
         }
      }
   }

   public AspectList getAspects(EntityPlayer player, ItemStack input, ArrayList<ItemStack> comps) {
      AspectList out = new AspectList();
      if (input == null) {
         return out;
      } else {
         int cl = EnumInfusionEnchantment.getInfusionEnchantmentLevel(input, this.enchantment) + 1;
         if (cl > this.enchantment.maxLevel) {
            return out;
         } else {
            List<EnumInfusionEnchantment> el = EnumInfusionEnchantment.getInfusionEnchantments(input);
            int otherEnchantments = el.size();
            if (el.contains(this.enchantment)) {
               --otherEnchantments;
            }

            float modifier = (float)cl + (float)otherEnchantments * 0.33F;
            Aspect[] var9 = this.getAspects().getAspects();
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               Aspect a = var9[var11];
               out.add(a, (int)((float)this.getAspects().getAmount(a) * modifier));
            }

            return out;
         }
      }
   }
}
