package thaumcraft.api.golems.seals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.tasks.Task;

public interface ISeal {
   String getKey();

   boolean canPlaceAt(World var1, BlockPos var2, EnumFacing var3);

   void tickSeal(World var1, ISealEntity var2);

   void onTaskStarted(World var1, IGolemAPI var2, Task var3);

   boolean onTaskCompletion(World var1, IGolemAPI var2, Task var3);

   void onTaskSuspension(World var1, Task var2);

   boolean canGolemPerformTask(IGolemAPI var1, Task var2);

   void readCustomNBT(NBTTagCompound var1);

   void writeCustomNBT(NBTTagCompound var1);

   ResourceLocation getSealIcon();

   void onRemoval(World var1, BlockPos var2, EnumFacing var3);

   Object returnContainer(World var1, EntityPlayer var2, BlockPos var3, EnumFacing var4, ISealEntity var5);

   @SideOnly(Side.CLIENT)
   Object returnGui(World var1, EntityPlayer var2, BlockPos var3, EnumFacing var4, ISealEntity var5);

   EnumGolemTrait[] getRequiredTags();

   EnumGolemTrait[] getForbiddenTags();
}
