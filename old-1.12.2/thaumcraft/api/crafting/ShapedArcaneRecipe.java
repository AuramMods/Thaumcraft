package thaumcraft.api.crafting;

import java.util.HashMap;
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

public class ShapedArcaneRecipe implements IArcaneRecipe {
   private static final int MAX_CRAFT_GRID_WIDTH = 3;
   private static final int MAX_CRAFT_GRID_HEIGHT = 3;
   public ItemStack output = null;
   public Object[] input = null;
   public ItemStack[] inputCrystals = null;
   public int vis = 0;
   public String research;
   public int width = 0;
   public int height = 0;
   private boolean mirrored = true;
   private String name;

   public ShapedArcaneRecipe(String research, ItemStack result, int vis, ItemStack[] crystals, Object... recipe) {
      this.inputCrystals = crystals;
      this.output = result.func_77946_l();
      this.research = research;
      this.vis = vis;
      String shape = "";
      this.name = "";
      int idx = 0;
      if (recipe[idx] instanceof Boolean) {
         this.mirrored = (Boolean)recipe[idx];
         if (recipe[idx + 1] instanceof Object[]) {
            recipe = (Object[])((Object[])recipe[idx + 1]);
         } else {
            idx = 1;
         }
      }

      String ret;
      int var10;
      int var11;
      if (recipe[idx] instanceof String[]) {
         String[] parts = (String[])((String[])recipe[idx++]);
         String[] var9 = parts;
         var10 = parts.length;

         for(var11 = 0; var11 < var10; ++var11) {
            String s = var9[var11];
            this.width = s.length();
            shape = shape + s;
         }

         this.height = parts.length;
      } else {
         while(recipe[idx] instanceof String) {
            ret = (String)recipe[idx++];
            shape = shape + ret;
            this.width = ret.length();
            ++this.height;
         }
      }

      if (this.width * this.height != shape.length()) {
         ret = "Invalid shaped ore recipe: ";
         Object[] var20 = recipe;
         var10 = recipe.length;

         for(var11 = 0; var11 < var10; ++var11) {
            Object tmp = var20[var11];
            ret = ret + tmp + ", ";
         }

         ret = ret + this.output;
         throw new RuntimeException(ret);
      } else {
         HashMap itemMap;
         for(itemMap = new HashMap(); idx < recipe.length; idx += 2) {
            Character chr = (Character)recipe[idx];
            Object in = recipe[idx + 1];
            if (in instanceof ItemStack) {
               itemMap.put(chr, ((ItemStack)in).func_77946_l());
            } else if (in instanceof Item) {
               itemMap.put(chr, new ItemStack((Item)in));
            } else if (in instanceof Block) {
               itemMap.put(chr, new ItemStack((Block)in, 1, 32767));
            } else {
               if (!(in instanceof String)) {
                  String ret = "Invalid shaped ore recipe: ";
                  Object[] var24 = recipe;
                  int var13 = recipe.length;

                  for(int var14 = 0; var14 < var13; ++var14) {
                     Object tmp = var24[var14];
                     ret = ret + tmp + ", ";
                  }

                  ret = ret + this.output;
                  throw new RuntimeException(ret);
               }

               itemMap.put(chr, OreDictionary.getOres((String)in, false));
            }
         }

         this.input = new Object[this.width * this.height];
         int x = 0;
         char[] var22 = shape.toCharArray();
         var11 = var22.length;

         for(int var25 = 0; var25 < var11; ++var25) {
            char chr = var22[var25];
            this.input[x++] = itemMap.get(chr);
         }

      }
   }

   public ItemStack func_77572_b(InventoryCrafting var1) {
      return this.output.func_77946_l();
   }

   public int func_77570_a() {
      return this.input.length;
   }

   public ItemStack func_77571_b() {
      return this.output;
   }

   public boolean func_77569_a(InventoryCrafting inv, World world) {
      return inv instanceof IArcaneWorkbench && this.matches(inv, world, (EntityPlayer)null);
   }

   public boolean matches(InventoryCrafting inv, World world, EntityPlayer player) {
      if (player != null && !ThaumcraftCapabilities.knowsResearch(player, this.research)) {
         return false;
      } else {
         int y;
         if (this.inputCrystals != null && this.inputCrystals.length > 0) {
            ItemStack[] var4 = this.inputCrystals;
            y = var4.length;

            label60:
            for(int var6 = 0; var6 < y; ++var6) {
               ItemStack crystal = var4[var6];

               for(int q = 0; q < 6; ++q) {
                  ItemStack is = inv.func_70301_a(q + 9);
                  if (this.checkItemEquals(crystal, is) && is.field_77994_a >= crystal.field_77994_a) {
                     continue label60;
                  }
               }

               return false;
            }
         }

         for(int x = 0; x <= 3 - this.width; ++x) {
            for(y = 0; y <= 3 - this.height; ++y) {
               if (this.checkMatch(inv, x, y, false)) {
                  return true;
               }

               if (this.mirrored && this.checkMatch(inv, x, y, true)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
      for(int x = 0; x < 3; ++x) {
         for(int y = 0; y < 3; ++y) {
            int subX = x - startX;
            int subY = y - startY;
            Object target = null;
            if (subX >= 0 && subY >= 0 && subX < this.width && subY < this.height) {
               if (mirror) {
                  target = this.input[this.width - subX - 1 + subY * this.width];
               } else {
                  target = this.input[subX + subY * this.width];
               }
            }

            ItemStack slot = inv.func_70463_b(x, y);
            if (target instanceof ItemStack) {
               if (!this.checkItemEquals((ItemStack)target, slot)) {
                  return false;
               }
            } else if (!(target instanceof List)) {
               if (target == null && slot != null) {
                  return false;
               }
            } else {
               boolean matched = false;

               ItemStack item;
               for(Iterator var12 = ((List)target).iterator(); var12.hasNext(); matched = matched || this.checkItemEquals(item, slot)) {
                  item = (ItemStack)var12.next();
               }

               if (!matched) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   private boolean checkItemEquals(ItemStack target, ItemStack input) {
      if ((input != null || target == null) && (input == null || target != null)) {
         return target.func_77973_b() == input.func_77973_b() && (!target.func_77942_o() || ThaumcraftApiHelper.areItemStackTagsEqualForCrafting(input, target)) && (target.func_77952_i() == 32767 || target.func_77952_i() == input.func_77952_i());
      } else {
         return false;
      }
   }

   public ShapedArcaneRecipe setMirrored(boolean mirror) {
      this.mirrored = mirror;
      return this;
   }

   public Object[] getInput() {
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
