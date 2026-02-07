package thaumcraft.common.items.tools;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import thaumcraft.common.items.ItemTCBase;

public class ItemSanityChecker extends ItemTCBase {
   public ItemSanityChecker() {
      super("sanity_checker");
      this.func_77625_d(1);
   }

   public EnumRarity func_77613_e(ItemStack itemstack) {
      return EnumRarity.UNCOMMON;
   }
}
