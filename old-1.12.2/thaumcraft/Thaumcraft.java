package thaumcraft;

import java.io.File;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thaumcraft.common.lib.CommandThaumcraft;
import thaumcraft.proxies.IProxy;

@Mod(
   modid = "thaumcraft",
   name = "Thaumcraft",
   version = "6.0.ALPHA15",
   guiFactory = "thaumcraft.client.gui.ThaumcraftGuiFactory",
   dependencies = "required-after:Forge@[12.18.1.2049,);required-after:Baubles@[1.3.4,)",
   acceptedMinecraftVersions = "[1.10.2]"
)
public class Thaumcraft {
   public static final String MODID = "thaumcraft";
   public static final String MODNAME = "Thaumcraft";
   public static final String VERSION = "6.0.ALPHA15";
   @SidedProxy(
      clientSide = "thaumcraft.proxies.ClientProxy",
      serverSide = "thaumcraft.proxies.ServerProxy"
   )
   public static IProxy proxy;
   @Instance("thaumcraft")
   public static Thaumcraft instance;
   public File modDir;
   public static final Logger log = LogManager.getLogger("thaumcraft".toUpperCase());

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      proxy.preInit(event);
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      proxy.init(event);
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent event) {
      proxy.postInit(event);
   }

   @EventHandler
   public void serverLoad(FMLServerStartingEvent event) {
      event.registerServerCommand(new CommandThaumcraft());
   }
}
