package thaumcraft.common.lib.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessNBTOreRecipe extends ShapelessOreRecipe {
   private ItemStack output;
   private ArrayList input;

   public ShapelessNBTOreRecipe(Block result, Object... recipe) {
      this(new ItemStack(result), recipe);
   }

   public ShapelessNBTOreRecipe(Item result, Object... recipe) {
      this(new ItemStack(result), recipe);
   }

   public ShapelessNBTOreRecipe(ItemStack result, Object... recipe) {
      super(result, recipe);
      this.output = null;
      this.input = new ArrayList();
      this.output = result.func_77946_l();
      Object[] var3 = recipe;
      int var4 = recipe.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object in = var3[var5];
         if (in instanceof ItemStack) {
            this.input.add(((ItemStack)in).func_77946_l());
         } else if (in instanceof Item) {
            this.input.add(new ItemStack((Item)in));
         } else if (in instanceof Block) {
            this.input.add(new ItemStack((Block)in));
         } else {
            if (!(in instanceof String)) {
               String ret = "Invalid shapeless ore recipe: ";
               Object[] var8 = recipe;
               int var9 = recipe.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  Object tmp = var8[var10];
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

   public boolean func_77569_a(InventoryCrafting var1, World world) {
      ArrayList required = new ArrayList(this.input);

      for(int x = 0; x < var1.func_70302_i_(); ++x) {
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
                  if (next instanceof ArrayList) {
                     for(Iterator var10 = ((ArrayList)next).iterator(); var10.hasNext(); match = match || this.checkItemEquals(item, slot)) {
                        item = (ItemStack)var10.next();
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

   private boolean checkItemEquals(ItemStack target, ItemStack input) {
      return target.func_77973_b() == input.func_77973_b() && ItemStack.func_77970_a(target, input) && (target.func_77952_i() == 32767 || target.func_77952_i() == input.func_77952_i());
   }

   public ArrayList getInput() {
      return this.input;
   }
}
