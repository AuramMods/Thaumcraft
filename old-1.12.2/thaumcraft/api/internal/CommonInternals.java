package thaumcraft.api.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;

public class CommonInternals {
   public static HashMap<String, ResourceLocation> jsonLocs = new HashMap();
   public static ArrayList<ThaumcraftApi.EntityTags> scanEntities = new ArrayList();
   public static HashMap<String, Object> craftingRecipeCatalog = new HashMap();
   public static ArrayList<String> craftingRecipesCatalogFake = new ArrayList();
   public static ArrayList<String> craftingRecipesUnlinked = new ArrayList();
   public static HashMap<Object, ItemStack> smeltingBonus = new HashMap();
   public static ConcurrentHashMap<String, AspectList> objectTags = new ConcurrentHashMap();
   public static ConcurrentHashMap<String, int[]> groupedObjectTags = new ConcurrentHashMap();
   public static HashMap<Object, Integer> warpMap = new HashMap();
   public static HashMap<String, ItemStack> seedList = new HashMap();

   public static Object getCatalogRecipe(String key) {
      return craftingRecipeCatalog.get(key);
   }
}
