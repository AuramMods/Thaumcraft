package thaumcraft.proxies;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import thaumcraft.client.ColorHandler;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.ender.ShaderHelper;
import thaumcraft.client.lib.events.RenderEventHandler;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.events.KeyHandler;

public class ClientProxy extends CommonProxy {
   ProxyEntities proxyEntities = new ProxyEntities();
   ProxyTESR proxyTESR = new ProxyTESR();
   ProxyBlock proxyBlocks = new ProxyBlock();
   KeyHandler kh = new KeyHandler();

   public void preInit(FMLPreInitializationEvent event) {
      super.preInit(event);
      OBJLoader.INSTANCE.addDomain("thaumcraft".toLowerCase());
      ConfigItems.initModelsAndVariants();
      ConfigBlocks.initModelsAndVariants();
      this.proxyBlocks.setupBlocks();
      MinecraftForge.EVENT_BUS.register(RenderEventHandler.INSTANCE);
      MinecraftForge.EVENT_BUS.register(ParticleEngine.INSTANCE);
      MinecraftForge.EVENT_BUS.register(ProxyBlock.BakeBlockEventHandler.INSTANCE);
      ShaderHelper.initShaders();
   }

   public void init(FMLInitializationEvent event) {
      super.init(event);
      ColorHandler.registerColourHandlers();
      this.registerKeyBindings();
      this.proxyEntities.setupEntityRenderers();
      this.proxyTESR.setupTESR();
   }

   public void postInit(FMLPostInitializationEvent event) {
      super.postInit(event);
   }

   public void registerKeyBindings() {
      MinecraftForge.EVENT_BUS.register(this.kh);
   }

   public World getClientWorld() {
      return FMLClientHandler.instance().getClient().field_71441_e;
   }

   public boolean isShiftKeyDown() {
      return GuiScreen.func_146272_n();
   }

   public void setOtherBlockRenderers() {
   }
}
