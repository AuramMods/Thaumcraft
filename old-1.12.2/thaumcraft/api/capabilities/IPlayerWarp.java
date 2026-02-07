package thaumcraft.api.capabilities;

import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerWarp extends INBTSerializable<NBTTagCompound> {
   void clear();

   int get(@Nonnull IPlayerWarp.EnumWarpType var1);

   void set(@Nonnull IPlayerWarp.EnumWarpType var1, int var2);

   int add(@Nonnull IPlayerWarp.EnumWarpType var1, int var2);

   int reduce(@Nonnull IPlayerWarp.EnumWarpType var1, int var2);

   void sync(EntityPlayerMP var1);

   int getCounter();

   void setCounter(int var1);

   public static enum EnumWarpType {
      PERMANENT,
      NORMAL,
      TEMPORARY;
   }
}
