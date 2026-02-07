package thaumcraft.common.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import thaumcraft.api.items.IVisDiscountGear;
import thaumcraft.common.items.ItemTCBase;

public class ItemBaubles extends ItemTCBase implements IBauble, IVisDiscountGear {
   public ItemBaubles() {
      super("baubles", "amulet_mundane", "ring_mundane", "girdle_mundane", "ring_apprentice", "amulet_fancy", "ring_fancy", "girdle_fancy");
      this.field_77777_bU = 1;
      this.func_77656_e(0);
   }

   public BaubleType getBaubleType(ItemStack itemstack) {
      switch(itemstack.func_77952_i()) {
      case 1:
      case 3:
      case 5:
         return BaubleType.RING;
      case 2:
      case 6:
         return BaubleType.BELT;
      case 4:
      default:
         return BaubleType.AMULET;
      }
   }

   public EnumRarity func_77613_e(ItemStack stack) {
      return stack.func_77952_i() >= 3 ? EnumRarity.UNCOMMON : super.func_77613_e(stack);
   }

   public int getVisDiscount(ItemStack stack, EntityPlayer player) {
      return stack.func_77952_i() == 3 ? 5 : 0;
   }
}
