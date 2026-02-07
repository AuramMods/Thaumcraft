package thaumcraft.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.Part;
import thaumcraft.api.crafting.RecipeMisc;
import thaumcraft.api.internal.CommonInternals;
import thaumcraft.api.internal.DummyInternalMethodHandler;
import thaumcraft.api.internal.IInternalMethodHandler;
import thaumcraft.api.internal.WeightedRandomLoot;

public class ThaumcraftApi {
   public static IInternalMethodHandler internalMethods = new DummyInternalMethodHandler();

   public static void registerResearchLocation(ResourceLocation loc) {
      if (!CommonInternals.jsonLocs.containsKey(loc.toString())) {
         CommonInternals.jsonLocs.put(loc.toString(), loc);
      }

   }

   public static void addSmeltingBonus(ItemStack in, ItemStack out) {
      CommonInternals.smeltingBonus.put(Arrays.asList(in.func_77973_b(), in.func_77952_i()), new ItemStack(out.func_77973_b(), 0, out.func_77952_i()));
   }

   public static void addSmeltingBonus(String in, ItemStack out) {
      CommonInternals.smeltingBonus.put(in, new ItemStack(out.func_77973_b(), 0, out.func_77952_i()));
   }

   public static ItemStack getSmeltingBonus(ItemStack in) {
      ItemStack out = (ItemStack)CommonInternals.smeltingBonus.get(Arrays.asList(in.func_77973_b(), in.func_77952_i()));
      if (out == null) {
         out = (ItemStack)CommonInternals.smeltingBonus.get(Arrays.asList(in.func_77973_b(), 32767));
      }

      if (out == null) {
         int[] var2 = OreDictionary.getOreIDs(in);
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            int id = var2[var4];
            String od = OreDictionary.getOreName(id);
            out = (ItemStack)CommonInternals.smeltingBonus.get(od);
            if (out != null) {
               break;
            }
         }
      }

      return out;
   }

   public static HashMap<String, Object> getCraftingRecipes() {
      return CommonInternals.craftingRecipeCatalog;
   }

   public static void addRecipeUnlinked(String recipeKey) {
      CommonInternals.craftingRecipesUnlinked.add(recipeKey);
   }

   public static void addIRecipeToCatalog(String name, boolean fake, IRecipe... recipes) {
      getCraftingRecipes().put(name, recipes);
      if (fake) {
         CommonInternals.craftingRecipesCatalogFake.add(name);
      }

   }

   public static void addMiscRecipeToCatalog(String name, RecipeMisc... recipes) {
      getCraftingRecipes().put(name, recipes);
      CommonInternals.craftingRecipesCatalogFake.add(name);
   }

   public static void addMultiblockRecipeToCatalog(String name, ThaumcraftApi.BluePrint... recipes) {
      getCraftingRecipes().put(name, recipes);
      CommonInternals.craftingRecipesCatalogFake.add(name);
   }

   public static void addArcaneCraftingRecipe(String name, boolean fake, IArcaneRecipe... recipes) {
      getCraftingRecipes().put(name, recipes);
      IArcaneRecipe[] var3 = recipes;
      int var4 = recipes.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         IArcaneRecipe recipe = var3[var5];
         recipe.setRecipeName(name);
         if (!fake) {
            CraftingManager.func_77594_a().func_180302_a(recipe);
         } else {
            CommonInternals.craftingRecipesCatalogFake.add(name);
         }
      }

   }

   public static void addInfusionCraftingRecipe(String name, boolean fake, InfusionRecipe... recipes) {
      getCraftingRecipes().put(name, recipes);
      if (fake) {
         CommonInternals.craftingRecipesCatalogFake.add(name);
      }

   }

   public static InfusionRecipe getInfusionRecipe(ItemStack res) {
      Iterator var1 = getCraftingRecipes().values().iterator();

      while(true) {
         Object r;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            r = var1.next();
         } while(!(r instanceof InfusionRecipe[]));

         InfusionRecipe[] var3 = (InfusionRecipe[])((InfusionRecipe[])r);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            InfusionRecipe recipe = var3[var5];
            if (recipe.getRecipeOutput() instanceof ItemStack && ((ItemStack)recipe.getRecipeOutput()).func_77969_a(res)) {
               return recipe;
            }
         }
      }
   }

   public static void addCrucibleRecipe(String name, boolean fake, CrucibleRecipe... recipes) {
      getCraftingRecipes().put(name, recipes);
      if (fake) {
         CommonInternals.craftingRecipesCatalogFake.add(name);
      }

   }

   public static CrucibleRecipe getCrucibleRecipe(ItemStack stack) {
      Iterator var1 = getCraftingRecipes().values().iterator();

      while(true) {
         Object r;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            r = var1.next();
         } while(!(r instanceof CrucibleRecipe[]));

         CrucibleRecipe[] var3 = (CrucibleRecipe[])((CrucibleRecipe[])r);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            CrucibleRecipe recipe = var3[var5];
            if (recipe.getRecipeOutput().func_77969_a(stack)) {
               return recipe;
            }
         }
      }
   }

   public static CrucibleRecipe getCrucibleRecipeFromHash(int hash) {
      Iterator var1 = getCraftingRecipes().values().iterator();

      while(true) {
         Object r;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            r = var1.next();
         } while(!(r instanceof CrucibleRecipe[]));

         CrucibleRecipe[] var3 = (CrucibleRecipe[])((CrucibleRecipe[])r);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            CrucibleRecipe recipe = var3[var5];
            if (recipe.hash == hash) {
               return recipe;
            }
         }
      }
   }

   public static boolean exists(ItemStack item) {
      ItemStack stack = item.func_77946_l();
      stack.field_77994_a = 1;
      AspectList tmp = (AspectList)CommonInternals.objectTags.get(stack.serializeNBT().toString());
      if (tmp == null) {
         stack.func_77964_b(32767);
         tmp = (AspectList)CommonInternals.objectTags.get(stack.serializeNBT().toString());
         if (item.func_77952_i() == 32767 && tmp == null) {
            int index = 0;

            do {
               stack.func_77964_b(index);
               tmp = (AspectList)CommonInternals.objectTags.get(stack.serializeNBT().toString());
               ++index;
            } while(index < 16 && tmp == null);
         }

         if (tmp == null) {
            return false;
         }
      }

      return true;
   }

   public static void registerObjectTag(ItemStack item, AspectList aspects) {
      if (aspects == null) {
         aspects = new AspectList();
      }

      try {
         item.field_77994_a = 1;
         NBTTagCompound nbt = new NBTTagCompound();
         aspects.writeToNBT(nbt);
         CommonInternals.objectTags.put(item.serializeNBT().toString(), aspects);
      } catch (Exception var3) {
      }

   }

   public static void registerObjectTag(ItemStack item, int[] meta, AspectList aspects) {
      if (aspects == null) {
         aspects = new AspectList();
      }

      try {
         item.field_77994_a = 1;
         String s = item.serializeNBT().toString();
         CommonInternals.objectTags.put(s, aspects);
         int[] var4 = meta;
         int var5 = meta.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            int m = var4[var6];
            CommonInternals.groupedObjectTags.put(m + ":" + s, meta);
         }
      } catch (Exception var8) {
      }

   }

   public static void registerObjectTag(String oreDict, AspectList aspects) {
      if (aspects == null) {
         aspects = new AspectList();
      }

      List<ItemStack> ores = ThaumcraftApiHelper.getOresWithWildCards(oreDict);
      if (ores != null && ores.size() > 0) {
         Iterator var3 = ores.iterator();

         while(var3.hasNext()) {
            ItemStack ore = (ItemStack)var3.next();

            try {
               ItemStack oc = ore.func_77946_l();
               oc.field_77994_a = 1;
               registerObjectTag(oc, aspects.copy());
            } catch (Exception var6) {
            }
         }
      }

   }

   public static void registerComplexObjectTag(ItemStack item, AspectList aspects) {
      AspectList tmp;
      Aspect[] var3;
      int var4;
      int var5;
      Aspect tag;
      if (!exists(item)) {
         tmp = AspectHelper.generateTags(item);
         if (tmp != null && tmp.size() > 0) {
            var3 = tmp.getAspects();
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
               tag = var3[var5];
               aspects.add(tag, tmp.getAmount(tag));
            }
         }

         registerObjectTag(item, aspects);
      } else {
         tmp = AspectHelper.getObjectAspects(item);
         var3 = aspects.getAspects();
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            tag = var3[var5];
            tmp.merge(tag, tmp.getAmount(tag));
         }

         registerObjectTag(item, tmp);
      }

   }

   public static void registerComplexObjectTag(String oreDict, AspectList aspects) {
      if (aspects == null) {
         aspects = new AspectList();
      }

      List<ItemStack> ores = ThaumcraftApiHelper.getOresWithWildCards(oreDict);
      if (ores != null && ores.size() > 0) {
         Iterator var3 = ores.iterator();

         while(var3.hasNext()) {
            ItemStack ore = (ItemStack)var3.next();

            try {
               ItemStack oc = ore.func_77946_l();
               oc.field_77994_a = 1;
               registerComplexObjectTag(oc, aspects.copy());
            } catch (Exception var6) {
            }
         }
      }

   }

   public static void registerEntityTag(String entityName, AspectList aspects, ThaumcraftApi.EntityTagsNBT... nbt) {
      CommonInternals.scanEntities.add(new ThaumcraftApi.EntityTags(entityName, aspects, nbt));
   }

   public static void addWarpToItem(ItemStack craftresult, int amount) {
      CommonInternals.warpMap.put(Arrays.asList(craftresult.func_77973_b(), craftresult.func_77952_i()), amount);
   }

   public static int getWarp(ItemStack in) {
      if (in == null) {
         return 0;
      } else {
         return in instanceof ItemStack && CommonInternals.warpMap.containsKey(Arrays.asList(in.func_77973_b(), in.func_77952_i())) ? (Integer)CommonInternals.warpMap.get(Arrays.asList(in.func_77973_b(), in.func_77952_i())) : 0;
      }
   }

   public static void addLootBagItem(ItemStack item, int weight, int... bagTypes) {
      if (bagTypes != null && bagTypes.length != 0) {
         int[] var3 = bagTypes;
         int var4 = bagTypes.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            int rarity = var3[var5];
            switch(rarity) {
            case 0:
               WeightedRandomLoot.lootBagCommon.add(new WeightedRandomLoot(item, weight));
               break;
            case 1:
               WeightedRandomLoot.lootBagUncommon.add(new WeightedRandomLoot(item, weight));
               break;
            case 2:
               WeightedRandomLoot.lootBagRare.add(new WeightedRandomLoot(item, weight));
            }
         }
      } else {
         WeightedRandomLoot.lootBagCommon.add(new WeightedRandomLoot(item, weight));
      }

   }

   public static void registerSeed(Block block, ItemStack seed) {
      CommonInternals.seedList.put(block.func_149739_a(), seed);
   }

   public static ItemStack getSeed(Block block) {
      return (ItemStack)CommonInternals.seedList.get(block.func_149739_a());
   }

   public static class EntityTags {
      public String entityName;
      public ThaumcraftApi.EntityTagsNBT[] nbts;
      public AspectList aspects;

      public EntityTags(String entityName, AspectList aspects, ThaumcraftApi.EntityTagsNBT... nbts) {
         this.entityName = entityName;
         this.nbts = nbts;
         this.aspects = aspects;
      }
   }

   public static class EntityTagsNBT {
      public String name;
      public Object value;

      public EntityTagsNBT(String name, Object value) {
         this.name = name;
         this.value = value;
      }
   }

   public static class BluePrint {
      Part[][][] parts;
      String research;
      ItemStack displayStack;
      ItemStack[] ingredientList;

      public BluePrint(String research, Part[][][] parts, ItemStack... ingredientList) {
         this.parts = parts;
         this.research = research;
         this.ingredientList = ingredientList;
      }

      public BluePrint(String research, ItemStack display, Part[][][] parts, ItemStack... ingredientList) {
         this.parts = parts;
         this.research = research;
         this.displayStack = display;
         this.ingredientList = ingredientList;
      }

      public Part[][][] getParts() {
         return this.parts;
      }

      public String getResearch() {
         return this.research;
      }

      public ItemStack[] getIngredientList() {
         return this.ingredientList;
      }

      public ItemStack getDisplayStack() {
         return this.displayStack;
      }
   }
}
