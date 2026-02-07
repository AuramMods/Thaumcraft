package thaumcraft.api.golems.seals;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISealEntity {
   void tickSealEntity(World var1);

   ISeal getSeal();

   SealPos getSealPos();

   byte getPriority();

   void setPriority(byte var1);

   void readNBT(NBTTagCompound var1);

   NBTTagCompound writeNBT();

   void syncToClient(World var1);

   BlockPos getArea();

   void setArea(BlockPos var1);

   boolean isLocked();

   void setLocked(boolean var1);

   boolean isRedstoneSensitive();

   void setRedstoneSensitive(boolean var1);

   String getOwner();

   void setOwner(String var1);

   byte getColor();

   void setColor(byte var1);

   boolean isStoppedByRedstone(World var1);
}
