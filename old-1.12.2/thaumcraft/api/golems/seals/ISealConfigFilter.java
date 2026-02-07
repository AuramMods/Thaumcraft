package thaumcraft.api.golems.seals;

import net.minecraft.item.ItemStack;

public interface ISealConfigFilter {
   ItemStack[] getInv();

   int getFilterSize();

   ItemStack getFilterSlot(int var1);

   void setFilterSlot(int var1, ItemStack var2);

   boolean isBlacklist();

   void setBlacklist(boolean var1);

   boolean hasStacksizeLimiters();
}
