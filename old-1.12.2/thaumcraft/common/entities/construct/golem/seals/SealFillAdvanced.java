package thaumcraft.common.entities.construct.golem.seals;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.items.ItemsTC;

public class SealFillAdvanced extends SealFill implements ISealConfigToggles {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "items/seals/seal_fill_advanced");
   private static final ItemStack item;

   public String getKey() {
      return "thaumcraft:fill_advanced";
   }

   public int getFilterSize() {
      return 9;
   }

   public ResourceLocation getSealIcon() {
      return this.icon;
   }

   public int[] getGuiCategories() {
      return new int[]{1, 3, 0, 4};
   }

   public ISealConfigToggles.SealToggle[] getToggles() {
      return this.props;
   }

   public void setToggle(int indx, boolean value) {
      this.props[indx].setValue(value);
   }

   public EnumGolemTrait[] getRequiredTags() {
      return new EnumGolemTrait[]{EnumGolemTrait.SMART};
   }

   static {
      item = new ItemStack(ItemsTC.seals, 1, 3);
   }
}
