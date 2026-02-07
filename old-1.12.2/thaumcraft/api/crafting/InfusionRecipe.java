package thaumcraft.api.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

public class InfusionRecipe implements ITCRecipe {
   public AspectList aspects;
   public String research;
   private String name = "";
   public Object[] components;
   public Object recipeInput;
   public Object recipeOutput;
   public int instability;

   public InfusionRecipe(String research, Object output, int inst, AspectList aspects2, Object input, Object[] recipe) {
      this.research = research;
      this.recipeOutput = output;
      this.recipeInput = input;
      this.aspects = aspects2;
      this.components = recipe;
      this.instability = inst;
   }

   public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, EntityPlayer player) {
      if (this.getRecipeInput() == null) {
         return false;
      } else if (!ThaumcraftCapabilities.getKnowledge(player).isResearchKnown(this.research)) {
         return false;
      } else {
         ItemStack i2 = central.func_77946_l();
         if (this.getRecipeInput() instanceof ItemStack && ((ItemStack)this.getRecipeInput()).func_77952_i() == 32767) {
            i2.func_77964_b(32767);
         }

         if (!ThaumcraftApiHelper.areItemStacksEqualForCrafting(i2, this.getRecipeInput())) {
            return false;
         } else {
            ArrayList<ItemStack> ii = new ArrayList();
            Iterator var7 = input.iterator();

            while(var7.hasNext()) {
               ItemStack is = (ItemStack)var7.next();
               ii.add(is.func_77946_l());
            }

            Object[] var13 = this.getComponents();
            int var14 = var13.length;

            for(int var9 = 0; var9 < var14; ++var9) {
               Object comp = var13[var9];
               boolean b = false;

               for(int a = 0; a < ii.size(); ++a) {
                  i2 = ((ItemStack)ii.get(a)).func_77946_l();
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
      }
   }

   public String getResearch() {
      return this.research;
   }

   public Object getRecipeInput() {
      return this.recipeInput;
   }

   public Object[] getComponents() {
      return this.components;
   }

   public Object getRecipeOutput() {
      return this.recipeOutput;
   }

   public AspectList getAspects() {
      return this.aspects;
   }

   public Object getRecipeOutput(EntityPlayer player, ItemStack input, ArrayList<ItemStack> comps) {
      return this.recipeOutput;
   }

   public AspectList getAspects(EntityPlayer player, ItemStack input, ArrayList<ItemStack> comps) {
      return this.aspects;
   }

   public int getInstability(EntityPlayer player, ItemStack input, ArrayList<ItemStack> comps) {
      return this.instability;
   }

   public String getRecipeName() {
      return this.name;
   }

   public void setRecipeName(String name) {
      this.name = name;
   }
}
