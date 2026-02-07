package thaumcraft.api.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface ILabelable {
   boolean applyLabel(EntityPlayer var1, BlockPos var2, EnumFacing var3, ItemStack var4);
}
