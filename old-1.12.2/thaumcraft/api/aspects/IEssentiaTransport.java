package thaumcraft.api.aspects;

import net.minecraft.util.EnumFacing;

public interface IEssentiaTransport {
   boolean isConnectable(EnumFacing var1);

   boolean canInputFrom(EnumFacing var1);

   boolean canOutputTo(EnumFacing var1);

   void setSuction(Aspect var1, int var2);

   Aspect getSuctionType(EnumFacing var1);

   int getSuctionAmount(EnumFacing var1);

   int takeEssentia(Aspect var1, int var2, EnumFacing var3);

   int addEssentia(Aspect var1, int var2, EnumFacing var3);

   Aspect getEssentiaType(EnumFacing var1);

   int getEssentiaAmount(EnumFacing var1);

   int getMinimumSuction();
}
