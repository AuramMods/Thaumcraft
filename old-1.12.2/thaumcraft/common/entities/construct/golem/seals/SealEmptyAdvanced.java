package thaumcraft.common.entities.construct.golem.seals;

import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.items.ItemsTC;

public class SealEmptyAdvanced extends SealEmpty implements ISealConfigToggles {
   ResourceLocation icon = new ResourceLocation("thaumcraft", "items/seals/seal_empty_advanced");
   private static final ItemStack item;

   public String getKey() {
      return "thaumcraft:empty_advanced";
   }

   public int getFilterSize() {
      return 9;
   }

   public ResourceLocation getSealIcon() {
      return this.icon;
   }

   public ItemStack[] getInv(int c) {
      if (this.getToggles()[4].value && !this.isBlacklist()) {
         ArrayList<ItemStack> w = new ArrayList();
         ItemStack[] var3 = super.getInv();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ItemStack s = var3[var5];
            if (s != null) {
               w.add(s);
            }
         }

         if (w.size() > 0) {
            int i = Math.abs(c % w.size());
            return new ItemStack[]{(ItemStack)w.get(i)};
         }
      }

      return super.getInv();
   }

   public ItemStack[] getInv() {
      return super.getInv();
   }

   public ISealConfigToggles.SealToggle[] getToggles() {
      return this.props;
   }

   public int[] getGuiCategories() {
      return new int[]{1, 3, 0, 4};
   }

   public void setToggle(int indx, boolean value) {
      this.props[indx].setValue(value);
   }

   public EnumGolemTrait[] getRequiredTags() {
      return new EnumGolemTrait[]{EnumGolemTrait.SMART};
   }

   static {
      item = new ItemStack(ItemsTC.seals, 1, 5);
   }
}
