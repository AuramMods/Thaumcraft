package thaumcraft.proxies;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigAspects;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigEntities;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.config.ConfigRecipes;
import thaumcraft.common.config.ConfigResearch;
import thaumcraft.common.lib.InternalMethodHandler;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.capabilities.PlayerKnowledge;
import thaumcraft.common.lib.capabilities.PlayerWarp;
import thaumcraft.common.lib.events.ChunkEvents;
import thaumcraft.common.lib.events.CraftingEvents;
import thaumcraft.common.lib.events.EntityEvents;
import thaumcraft.common.lib.events.PlayerEvents;
import thaumcraft.common.lib.events.ServerEvents;
import thaumcraft.common.lib.events.ToolEvents;
import thaumcraft.common.lib.events.WorldEvents;
import thaumcraft.common.lib.network.EventHandlerNetwork;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.CropUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.world.ThaumcraftWorldGenerator;
import thaumcraft.common.world.biomes.BiomeHandler;

public class CommonProxy implements IGuiHandler, IProxy {
   ProxyGUI proxyGUI = new ProxyGUI();

   public void preInit(FMLPreInitializationEvent event) {
      event.getModMetadata().version = "6.0.ALPHA15";
      Thaumcraft.instance.modDir = event.getModConfigurationDirectory();

      try {
         Config.initialize(event.getSuggestedConfigurationFile());
      } catch (Exception var6) {
         Thaumcraft.log.error("Thaumcraft has a problem loading it's configuration");
      } finally {
         if (Config.config != null) {
            Config.save();
         }

      }

      ThaumcraftApi.internalMethods = new InternalMethodHandler();
      PlayerKnowledge.preInit();
      PlayerWarp.preInit();
      PacketHandler.preInit();
      MinecraftForge.EVENT_BUS.register(new EventHandlerNetwork());
      MinecraftForge.EVENT_BUS.register(new ChunkEvents());
      MinecraftForge.EVENT_BUS.register(new ServerEvents());
      MinecraftForge.EVENT_BUS.register(WorldEvents.INSTANCE);
      MinecraftForge.TERRAIN_GEN_BUS.register(WorldEvents.INSTANCE);
      MinecraftForge.EVENT_BUS.register(PlayerEvents.INSTANCE);
      MinecraftForge.EVENT_BUS.register(new EntityEvents());
      MinecraftForge.EVENT_BUS.register(new ToolEvents());
      GameRegistry.registerFuelHandler(new CraftingEvents());
      MinecraftForge.EVENT_BUS.register(new CraftingEvents());
      GameRegistry.registerWorldGenerator(ThaumcraftWorldGenerator.INSTANCE, 0);
      Config.save();
      SoundsTC.registerSounds();
      ConfigItems.preInitSeals();
      ConfigBlocks.preInit();
      ConfigItems.preInit();
      ThaumcraftWorldGenerator.INSTANCE.initialize();
      MinecraftForge.EVENT_BUS.register(Thaumcraft.instance);
      BiomeHandler.registerBiomes();
      ConfigEntities.preInit();
   }

   public void init(FMLInitializationEvent event) {
      ConfigBlocks.init();
      ConfigItems.init();
      NetworkRegistry.INSTANCE.registerGuiHandler(Thaumcraft.instance, this);
      ConfigResearch.init();
   }

   public void postInit(FMLPostInitializationEvent event) {
      Config.postInitPotions();
      ConfigEntities.postInitEntitySpawns();
      Config.postInitModCompatibility();
      ConfigRecipes.postInit();
      ConfigAspects.postInit();
      ConfigRecipes.postAspects();
      Config.postInitLoot();
      Config.postInitMisc();
      ImmutableList<IMCMessage> messages = FMLInterModComms.fetchRuntimeMessages(this);
      UnmodifiableIterator var3 = messages.iterator();

      while(var3.hasNext()) {
         IMCMessage message = (IMCMessage)var3.next();
         if (message.key.equals("portableHoleBlacklist") && message.isStringMessage()) {
            BlockUtils.portableHoleBlackList.add(message.getStringValue());
         }

         ItemStack crop;
         if (message.key.equals("harvestStandardCrop") && message.isItemStackMessage()) {
            crop = message.getItemStackValue();
            CropUtils.addStandardCrop(crop, crop.func_77952_i());
         }

         if (message.key.equals("harvestClickableCrop") && message.isItemStackMessage()) {
            crop = message.getItemStackValue();
            CropUtils.addClickableCrop(crop, crop.func_77952_i());
         }

         if (message.key.equals("harvestStackedCrop") && message.isItemStackMessage()) {
            crop = message.getItemStackValue();
            CropUtils.addStackedCrop(crop, crop.func_77952_i());
         }

         String[] t;
         if (message.key.equals("nativeCluster") && message.isStringMessage()) {
            t = message.getStringValue().split(",");
            if (t != null && t.length == 5) {
               try {
                  ItemStack ore = new ItemStack(Item.func_150899_d(Integer.parseInt(t[0])), 1, Integer.parseInt(t[1]));
                  ItemStack cluster = new ItemStack(Item.func_150899_d(Integer.parseInt(t[2])), 1, Integer.parseInt(t[3]));
                  Utils.addSpecialMiningResult(ore, cluster, Float.parseFloat(t[4]));
               } catch (Exception var8) {
               }
            }
         }

         if (message.key.equals("lampBlacklist") && message.isItemStackMessage()) {
            crop = message.getItemStackValue();
            CropUtils.blacklistLamp(crop, crop.func_77952_i());
         }

         if (message.key.equals("dimensionBlacklist") && message.isStringMessage()) {
            t = message.getStringValue().split(":");
            if (t != null && t.length == 2) {
               try {
                  BiomeHandler.addDimBlacklist(Integer.parseInt(t[0]), Integer.parseInt(t[1]));
               } catch (Exception var9) {
               }
            }
         }

         if (message.key.equals("biomeBlacklist") && message.isStringMessage()) {
            t = message.getStringValue().split(":");
            if (t != null && t.length == 2 && Biome.func_150568_d(Integer.parseInt(t[0])) != null) {
               try {
                  BiomeHandler.addBiomeBlacklist(Integer.parseInt(t[0]), Integer.parseInt(t[1]));
               } catch (Exception var10) {
               }
            }
         }

         if (message.key.equals("championWhiteList") && message.isStringMessage()) {
            try {
               t = message.getStringValue().split(":");
               Class oclass = (Class)EntityList.field_75625_b.get(t[0]);
               if (oclass != null) {
                  ConfigEntities.championModWhitelist.put(oclass, Integer.parseInt(t[1]));
               }
            } catch (Exception var11) {
               Thaumcraft.log.error("Failed to Whitelist [" + message.getStringValue() + "] with [ championWhiteList ] message.");
            }
         }
      }

      ConfigResearch.postInit();
   }

   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      return this.proxyGUI.getClientGuiElement(ID, player, world, x, y, z);
   }

   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      return this.proxyGUI.getServerGuiElement(ID, player, world, x, y, z);
   }

   public boolean isShiftKeyDown() {
      return false;
   }

   public World getClientWorld() {
      return null;
   }
}
