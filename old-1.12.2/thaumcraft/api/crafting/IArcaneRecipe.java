package thaumcraft.api.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public interface IArcaneRecipe extends IRecipe, ITCRecipe {
   boolean func_77569_a(InventoryCrafting var1, World var2);

   boolean matches(InventoryCrafting var1, World var2, EntityPlayer var3);

   ItemStack func_77572_b(InventoryCrafting var1);

   int func_77570_a();

   ItemStack func_77571_b();

   ItemStack[] func_179532_b(InventoryCrafting var1);

   int getVis();

   int getVis(InventoryCrafting var1);

   String getResearch();

   ItemStack[] getCrystals();
}
