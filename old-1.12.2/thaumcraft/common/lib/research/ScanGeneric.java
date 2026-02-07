package thaumcraft.common.lib.research;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.research.IScanThing;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ScanningManager;

public class ScanGeneric implements IScanThing {
   public boolean checkThing(EntityPlayer player, Object obj) {
      if (obj == null) {
         return false;
      } else {
         AspectList al = null;
         if (obj instanceof Entity && !(obj instanceof EntityItem)) {
            al = AspectHelper.getEntityAspects((Entity)obj);
         } else {
            ItemStack is = ScanningManager.getItemFromParms(player, obj);
            if (is != null) {
               al = AspectHelper.getObjectAspects(is);
            }
         }

         return al != null && al.size() > 0;
      }
   }

   public void onSuccess(EntityPlayer player, Object obj) {
      if (obj != null) {
         AspectList al = null;
         if (obj instanceof Entity && !(obj instanceof EntityItem)) {
            al = AspectHelper.getEntityAspects((Entity)obj);
         } else {
            ItemStack is = ScanningManager.getItemFromParms(player, obj);
            if (is != null) {
               al = AspectHelper.getObjectAspects(is);
            }
         }

         if (al != null) {
            Iterator var6 = ResearchCategories.researchCategories.values().iterator();

            while(var6.hasNext()) {
               ResearchCategory category = (ResearchCategory)var6.next();
               ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, category, category.applyFormula(al));
            }
         }

      }
   }

   public String getResearchKey(EntityPlayer player, Object obj) {
      if (obj instanceof Entity && !(obj instanceof EntityItem)) {
         String s = EntityList.func_75621_b((Entity)obj);
         return "!" + s;
      } else {
         ItemStack is = ScanningManager.getItemFromParms(player, obj);
         if (is != null) {
            String s = "!" + is.func_77973_b().getRegistryName();
            if (!is.func_77984_f()) {
               s = s + is.func_77952_i();
            }

            return s;
         } else {
            return null;
         }
      }
   }
}
