package thaumcraft.common.lib.research;

import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import thaumcraft.api.research.IScanThing;
import thaumcraft.api.research.ScanningManager;

public class ScanPotion implements IScanThing {
   Potion potion;

   public ScanPotion(Potion potion) {
      this.potion = potion;
   }

   public boolean checkThing(EntityPlayer player, Object obj) {
      return this.getPotionEffect(player, obj) != null;
   }

   private PotionEffect getPotionEffect(EntityPlayer player, Object obj) {
      if (obj == null) {
         return null;
      } else {
         Iterator var4;
         PotionEffect potioneffect;
         if (obj instanceof EntityLivingBase) {
            EntityLivingBase e = (EntityLivingBase)obj;
            var4 = e.func_70651_bq().iterator();

            while(var4.hasNext()) {
               potioneffect = (PotionEffect)var4.next();
               if (potioneffect.func_188419_a() == this.potion) {
                  return potioneffect;
               }
            }
         } else {
            ItemStack is = ScanningManager.getItemFromParms(player, obj);
            if (is != null) {
               var4 = PotionUtils.func_185189_a(is).iterator();

               while(var4.hasNext()) {
                  potioneffect = (PotionEffect)var4.next();
                  if (potioneffect.func_188419_a() == this.potion) {
                     return potioneffect;
                  }
               }
            }
         }

         return null;
      }
   }

   public String getResearchKey(EntityPlayer player, Object obj) {
      return "!" + this.potion.func_76393_a();
   }
}
