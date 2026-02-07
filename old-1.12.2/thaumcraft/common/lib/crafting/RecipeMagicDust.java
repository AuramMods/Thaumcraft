package thaumcraft.common.lib.crafting;

import java.util.ArrayList;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.items.resources.ItemCrystalEssence;

public class RecipeMagicDust implements IRecipe {
   public boolean func_77569_a(InventoryCrafting inv, World worldIn) {
      boolean bowl = false;
      boolean flint = false;
      boolean redstone = false;
      ArrayList<String> crystals = new ArrayList();

      for(int a = 0; a < 3; ++a) {
         for(int b = 0; b < 3; ++b) {
            if (inv.func_70463_b(a, b) != null) {
               ItemStack stack = inv.func_70463_b(a, b).func_77946_l();
               if (stack.func_77973_b() == Items.field_151054_z && bowl) {
                  return false;
               }

               if (stack.func_77973_b() == Items.field_151054_z && !bowl) {
                  bowl = true;
               } else {
                  if (stack.func_77973_b() == Items.field_151145_ak && flint) {
                     return false;
                  }

                  if (stack.func_77973_b() == Items.field_151145_ak && !flint) {
                     flint = true;
                  } else {
                     if (stack.func_77973_b() == Items.field_151137_ax && redstone) {
                        return false;
                     }

                     if (stack.func_77973_b() == Items.field_151137_ax && !redstone) {
                        redstone = true;
                     } else {
                        if (stack.func_77973_b() != ItemsTC.crystalEssence) {
                           return false;
                        }

                        ItemCrystalEssence ice = (ItemCrystalEssence)stack.func_77973_b();
                        if (crystals.contains(ice.getAspects(stack).getAspects()[0].getTag()) || crystals.size() >= 3) {
                           return false;
                        }

                        crystals.add(ice.getAspects(stack).getAspects()[0].getTag());
                     }
                  }
               }
            }
         }
      }

      return bowl && redstone && flint && crystals.size() == 3;
   }

   public ItemStack func_77572_b(InventoryCrafting inv) {
      return new ItemStack(ItemsTC.salisMundus);
   }

   public int func_77570_a() {
      return 9;
   }

   public ItemStack func_77571_b() {
      return new ItemStack(ItemsTC.salisMundus);
   }

   public ItemStack[] func_179532_b(InventoryCrafting inv) {
      ItemStack[] aitemstack = new ItemStack[inv.func_70302_i_()];

      for(int i = 0; i < aitemstack.length; ++i) {
         ItemStack itemstack = inv.func_70301_a(i);
         aitemstack[i] = ForgeHooks.getContainerItem(itemstack);
         if (itemstack != null && (itemstack.func_77973_b() == Items.field_151145_ak || itemstack.func_77973_b() == Items.field_151054_z)) {
            ItemStack is = itemstack.func_77946_l();
            is.field_77994_a = 1;
            aitemstack[i] = is;
         }
      }

      return aitemstack;
   }
}
