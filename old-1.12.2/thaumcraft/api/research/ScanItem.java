package thaumcraft.api.research;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApiHelper;

public class ScanItem implements IScanThing {
   String research;
   ItemStack stack;

   public ScanItem(String research, ItemStack stack) {
      this.research = research;
      this.stack = stack;
   }

   public boolean checkThing(EntityPlayer player, Object obj) {
      if (obj == null) {
         return false;
      } else {
         ItemStack is = null;
         if (obj instanceof ItemStack) {
            is = (ItemStack)obj;
         }

         if (obj instanceof EntityItem && ((EntityItem)obj).func_92059_d() != null) {
            is = ((EntityItem)obj).func_92059_d();
         }

         return is != null && ThaumcraftApiHelper.areItemStacksEqualForCrafting(is, this.stack);
      }
   }

   public String getResearchKey(EntityPlayer player, Object object) {
      return this.research;
   }
}
