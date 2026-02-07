package thaumcraft.api.research;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.capabilities.IPlayerKnowledge;

public class ScanAspect implements IScanThing {
   String research;
   Aspect aspect;

   public ScanAspect(String research, Aspect aspect) {
      this.research = research;
      this.aspect = aspect;
   }

   public boolean checkThing(EntityPlayer player, Object obj) {
      if (obj == null) {
         return false;
      } else {
         AspectList al = null;
         if (obj instanceof Entity && !(obj instanceof EntityItem)) {
            al = AspectHelper.getEntityAspects((Entity)obj);
         } else {
            ItemStack is = null;
            if (obj instanceof ItemStack) {
               is = (ItemStack)obj;
            }

            if (obj instanceof EntityItem && ((EntityItem)obj).func_92059_d() != null) {
               is = ((EntityItem)obj).func_92059_d();
            }

            if (obj instanceof BlockPos) {
               Block b = player.field_70170_p.func_180495_p((BlockPos)obj).func_177230_c();
               is = new ItemStack(b, 1, b.func_176201_c(player.field_70170_p.func_180495_p((BlockPos)obj)));
            }

            if (is != null) {
               al = AspectHelper.getObjectAspects(is);
            }
         }

         return al != null && al.getAmount(this.aspect) > 0;
      }
   }

   public void onSuccess(EntityPlayer player, Object obj) {
      ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, ResearchCategories.getResearchCategory("THAUMATURGY"), 1);
      ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, ResearchCategories.getResearchCategory("BASICS"), 1);
      ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.OBSERVATION, ResearchCategories.getResearchCategory("ALCHEMY"), 1);
      ThaumcraftApi.internalMethods.addKnowledge(player, IPlayerKnowledge.EnumKnowledgeType.EPIPHANY, (ResearchCategory)null, 1);
   }

   public String getResearchKey(EntityPlayer player, Object object) {
      return this.research;
   }
}
