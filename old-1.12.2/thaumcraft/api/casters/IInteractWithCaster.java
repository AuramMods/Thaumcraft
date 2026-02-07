package thaumcraft.api.casters;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IInteractWithCaster {
   boolean onCasterRightClick(World var1, ItemStack var2, EntityPlayer var3, BlockPos var4, EnumFacing var5, EnumHand var6);
}
