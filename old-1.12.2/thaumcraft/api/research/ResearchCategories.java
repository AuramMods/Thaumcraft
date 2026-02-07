package thaumcraft.api.research;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import thaumcraft.api.aspects.AspectList;

public class ResearchCategories {
   public static LinkedHashMap<String, ResearchCategory> researchCategories = new LinkedHashMap();

   public static ResearchCategory getResearchCategory(String key) {
      return (ResearchCategory)researchCategories.get(key);
   }

   public static String getCategoryName(String key) {
      return I18n.func_74838_a("tc.research_category." + key);
   }

   public static ResearchEntry getResearch(String key) {
      Collection rc = researchCategories.values();
      Iterator var2 = rc.iterator();

      while(var2.hasNext()) {
         Object cat = var2.next();
         Collection rl = ((ResearchCategory)cat).research.values();
         Iterator var5 = rl.iterator();

         while(var5.hasNext()) {
            Object ri = var5.next();
            if (((ResearchEntry)ri).key.equals(key)) {
               return (ResearchEntry)ri;
            }
         }
      }

      return null;
   }

   public static ResearchCategory registerCategory(String key, String researchkey, AspectList formula, ResourceLocation icon, ResourceLocation background) {
      if (getResearchCategory(key) == null) {
         ResearchCategory rl = new ResearchCategory(key, researchkey, formula, icon, background);
         researchCategories.put(key, rl);
         return rl;
      } else {
         return null;
      }
   }

   public static ResearchCategory registerCategory(String key, String researchkey, AspectList formula, ResourceLocation icon, ResourceLocation background, ResourceLocation background2) {
      if (getResearchCategory(key) == null) {
         ResearchCategory rl = new ResearchCategory(key, researchkey, formula, icon, background, background2);
         researchCategories.put(key, rl);
         return rl;
      } else {
         return null;
      }
   }
}
