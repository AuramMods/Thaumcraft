package thaumcraft.common.entities.construct.golem.seals;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.items.ItemsTC;

public class SealGuardAdvanced extends SealGuard implements ISealConfigToggles {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "items/seals/seal_guard_advanced");
   private static final ItemStack item;

   public String getKey() {
      return "thaumcraft:guard_advanced";
   }

   public ResourceLocation getSealIcon() {
      return this.icon;
   }

   public ISealConfigToggles.SealToggle[] getToggles() {
      return this.props;
   }

   public void setToggle(int indx, boolean value) {
      this.props[indx].setValue(value);
   }

   public int[] getGuiCategories() {
      return new int[]{2, 3, 0, 4};
   }

   public EnumGolemTrait[] getRequiredTags() {
      return new EnumGolemTrait[]{EnumGolemTrait.FIGHTER, EnumGolemTrait.SMART};
   }

   static {
      item = new ItemStack(ItemsTC.seals, 1, 10);
   }
}
