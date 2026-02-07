package thaumcraft.api.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

public class ShapelessArcaneRecipe implements IArcaneRecipe {
   private ItemStack output = null;
   private ArrayList input = new ArrayList();
   public ItemStack[] inputCrystals = null;
   public int vis = 0;
   public String research;
   private String name;

   public ShapelessArcaneRecipe(String research, ItemStack result, int vis, ItemStack[] crystals, Object... recipe) {
      this.output = result.func_77946_l();
      this.inputCrystals = crystals;
      this.research = research;
      this.vis = vis;
      this.name = "";
      Object[] var6 = recipe;
      int var7 = recipe.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Object in = var6[var8];
         if (in instanceof ItemStack) {
            this.input.add(((ItemStack)in).func_77946_l());
         } else if (in instanceof Item) {
            this.input.add(new ItemStack((Item)in));
         } else if (in instanceof Block) {
            this.input.add(new ItemStack((Block)in));
         } else {
            if (!(in instanceof String)) {
               String ret = "Invalid shapeless ore recipe: ";
               Object[] var11 = recipe;
               int var12 = recipe.length;

               for(int var13 = 0; var13 < var12; ++var13) {
                  Object tmp = var11[var13];
                  ret = ret + tmp + ", ";
               }

               ret = ret + this.output;
               throw new RuntimeException(ret);
            }

            this.input.add(OreDictionary.getOres((String)in, false));
         }
      }

   }

   public int func_77570_a() {
      return this.input.size();
   }

   public ItemStack func_77571_b() {
      return this.output;
   }

   public ItemStack func_77572_b(InventoryCrafting var1) {
      return this.output.func_77946_l();
   }

   public boolean func_77569_a(InventoryCrafting inv, World world) {
      return inv instanceof IArcaneWorkbench && this.matches(inv, world, (EntityPlayer)null);
   }

   public boolean matches(InventoryCrafting var1, World world, EntityPlayer player) {
      if (player != null && !ThaumcraftCapabilities.getKnowledge(player).isResearchKnown(this.research)) {
         return false;
      } else {
         int x;
         if (this.inputCrystals != null && this.inputCrystals.length > 0) {
            ItemStack[] var4 = this.inputCrystals;
            x = var4.length;

            label85:
            for(int var6 = 0; var6 < x; ++var6) {
               ItemStack crystal = var4[var6];

               for(int q = 0; q < 6; ++q) {
                  ItemStack is = var1.func_70301_a(q + 9);
                  if (this.checkItemEquals(crystal, is) && is.field_77994_a >= crystal.field_77994_a) {
                     continue label85;
                  }
               }

               return false;
            }
         }

         ArrayList required = new ArrayList(this.input);

         for(x = 0; x < 9; ++x) {
            ItemStack slot = var1.func_70301_a(x);
            if (slot != null) {
               boolean inRecipe = false;
               Iterator req = required.iterator();

               while(req.hasNext()) {
                  boolean match = false;
                  Object next = req.next();
                  if (next instanceof ItemStack) {
                     match = this.checkItemEquals((ItemStack)next, slot);
                  } else {
                     ItemStack item;
                     if (next instanceof List) {
                        for(Iterator var11 = ((List)next).iterator(); var11.hasNext(); match = match || this.checkItemEquals(item, slot)) {
                           item = (ItemStack)var11.next();
                        }
                     }
                  }

                  if (match) {
                     inRecipe = true;
                     required.remove(next);
                     break;
                  }
               }

               if (!inRecipe) {
                  return false;
               }
            }
         }

         return required.isEmpty();
      }
   }

   private boolean checkItemEquals(ItemStack target, ItemStack input) {
      if ((input != null || target == null) && (input == null || target != null)) {
         return target.func_77973_b() == input.func_77973_b() && (!target.func_77942_o() || ThaumcraftApiHelper.areItemStackTagsEqualForCrafting(input, target)) && (target.func_77952_i() == 32767 || target.func_77952_i() == input.func_77952_i());
      } else {
         return false;
      }
   }

   public ArrayList getInput() {
      return this.input;
   }

   public int getVis() {
      return this.vis;
   }

   public int getVis(InventoryCrafting inv) {
      return this.vis;
   }

   public String getResearch() {
      return this.research;
   }

   public ItemStack[] func_179532_b(InventoryCrafting p_179532_1_) {
      ItemStack[] aitemstack = new ItemStack[p_179532_1_.func_70302_i_()];

      for(int i = 0; i < Math.min(9, aitemstack.length); ++i) {
         ItemStack itemstack = p_179532_1_.func_70301_a(i);
         aitemstack[i] = ForgeHooks.getContainerItem(itemstack);
      }

      return aitemstack;
   }

   public ItemStack[] getCrystals() {
      return this.inputCrystals;
   }

   public String getRecipeName() {
      return this.name;
   }

   public void setRecipeName(String name) {
      this.name = name;
   }
}
