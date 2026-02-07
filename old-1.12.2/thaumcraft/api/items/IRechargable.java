package thaumcraft.api.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IRechargable {
   int getMaxCharge(ItemStack var1, EntityLivingBase var2);

   IRechargable.EnumChargeDisplay showInHud(ItemStack var1, EntityLivingBase var2);

   public static enum EnumChargeDisplay {
      NEVER,
      NORMAL,
      PERIODIC;
   }
}
