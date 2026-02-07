package thaumcraft.api.items;

import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IArchitect {
   ArrayList<BlockPos> getArchitectBlocks(ItemStack var1, World var2, BlockPos var3, EnumFacing var4, EntityPlayer var5);

   boolean showAxis(ItemStack var1, World var2, EntityPlayer var3, EnumFacing var4, IArchitect.EnumAxis var5);

   public static enum EnumAxis {
      X,
      Y,
      Z;
   }
}
