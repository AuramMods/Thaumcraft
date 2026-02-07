package thaumcraft.api.golems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IGolemAPI {
   EntityLivingBase getGolemEntity();

   IGolemProperties getProperties();

   void setProperties(IGolemProperties var1);

   World getGolemWorld();

   ItemStack holdItem(ItemStack var1);

   ItemStack dropItem(ItemStack var1);

   boolean canCarry(ItemStack var1, boolean var2);

   boolean isCarrying(ItemStack var1);

   ItemStack[] getCarrying();

   void addRankXp(int var1);

   byte getGolemColor();

   void swingArm();

   boolean isInCombat();
}
