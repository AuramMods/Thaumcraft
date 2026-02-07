package thaumcraft.api.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public interface IArchitectExtended extends IArchitect {
   RayTraceResult getArchitectMOP(ItemStack var1, World var2, EntityLivingBase var3);

   boolean useBlockHighlight(ItemStack var1);
}
