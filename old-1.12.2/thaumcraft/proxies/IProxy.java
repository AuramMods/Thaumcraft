package thaumcraft.proxies;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {
   void preInit(FMLPreInitializationEvent var1);

   void init(FMLInitializationEvent var1);

   void postInit(FMLPostInitializationEvent var1);

   World getClientWorld();

   boolean isShiftKeyDown();
}
