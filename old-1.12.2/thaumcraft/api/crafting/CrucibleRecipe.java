package thaumcraft.api.crafting;

import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class CrucibleRecipe implements ITCRecipe {
   private ItemStack recipeOutput;
   public Object catalyst;
   public AspectList aspects;
   public String[] research;
   private String name;
   public int hash;

   public CrucibleRecipe(String researchKey, ItemStack result, Object cat, AspectList tags) {
      this(new String[]{researchKey}, result, cat, tags);
   }

   public CrucibleRecipe(String[] researchKey, ItemStack result, Object cat, AspectList tags) {
      this.recipeOutput = result;
      this.name = "";
      this.aspects = tags;
      this.research = researchKey;
      this.catalyst = cat;
      if (cat instanceof String) {
         this.catalyst = OreDictionary.getOres((String)cat, false);
      }

      String hc = "";
      String[] var6 = this.research;
      int var7 = var6.length;

      int var8;
      for(var8 = 0; var8 < var7; ++var8) {
         String ss = var6[var8];
         hc = hc + ss;
      }

      hc = hc + result.toString();
      Aspect[] var10 = tags.getAspects();
      var7 = var10.length;

      for(var8 = 0; var8 < var7; ++var8) {
         Aspect tag = var10[var8];
         hc = hc + tag.getTag() + tags.getAmount(tag);
      }

      if (cat instanceof ItemStack) {
         hc = hc + ((ItemStack)cat).toString();
      } else {
         ItemStack is;
         if (cat instanceof List && ((List)this.catalyst).size() > 0) {
            for(Iterator var11 = ((List)this.catalyst).iterator(); var11.hasNext(); hc = hc + is.toString()) {
               is = (ItemStack)var11.next();
            }
         }
      }

      this.hash = hc.hashCode();
   }

   public boolean matches(AspectList itags, ItemStack cat) {
      if (this.catalyst instanceof ItemStack && !OreDictionary.itemMatches((ItemStack)this.catalyst, cat, false)) {
         return false;
      } else if (this.catalyst instanceof List && ((List)this.catalyst).size() > 0 && !ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{cat}, (List)this.catalyst)) {
         return false;
      } else if (itags == null) {
         return false;
      } else {
         Aspect[] var3 = this.aspects.getAspects();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Aspect tag = var3[var5];
            if (itags.getAmount(tag) < this.aspects.getAmount(tag)) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean catalystMatches(ItemStack cat) {
      if (this.catalyst instanceof ItemStack && OreDictionary.itemMatches((ItemStack)this.catalyst, cat, false)) {
         return true;
      } else {
         return this.catalyst instanceof List && ((List)this.catalyst).size() > 0 && ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{cat}, (List)this.catalyst);
      }
   }

   public AspectList removeMatching(AspectList itags) {
      AspectList temptags = new AspectList();
      temptags.aspects.putAll(itags.aspects);
      Aspect[] var3 = this.aspects.getAspects();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Aspect tag = var3[var5];
         temptags.remove(tag, this.aspects.getAmount(tag));
      }

      return temptags;
   }

   public ItemStack getRecipeOutput() {
      return this.recipeOutput;
   }

   public String getRecipeName() {
      return this.name;
   }

   public void setRecipeName(String name) {
      this.name = name;
   }
}
