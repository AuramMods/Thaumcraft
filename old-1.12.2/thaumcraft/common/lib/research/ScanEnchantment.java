package thaumcraft.common.lib.research;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.research.IScanThing;
import thaumcraft.api.research.ScanningManager;

public class ScanEnchantment implements IScanThing {
   Enchantment enchantment;

   public ScanEnchantment(Enchantment ench) {
      this.enchantment = ench;
   }

   public boolean checkThing(EntityPlayer player, Object obj) {
      return this.getEnchantment(player, obj) != null;
   }

   private Enchantment getEnchantment(EntityPlayer player, Object obj) {
      if (obj == null) {
         return null;
      } else {
         ItemStack is = ScanningManager.getItemFromParms(player, obj);
         if (is != null) {
            Map<Enchantment, Integer> e = EnchantmentHelper.func_82781_a(is);
            Iterator var5 = e.keySet().iterator();

            while(var5.hasNext()) {
               Enchantment ench = (Enchantment)var5.next();
               if (ench == this.enchantment) {
                  return ench;
               }
            }
         }

         return null;
      }
   }

   public String getResearchKey(EntityPlayer player, Object obj) {
      return "!" + this.enchantment.func_77320_a();
   }
}
