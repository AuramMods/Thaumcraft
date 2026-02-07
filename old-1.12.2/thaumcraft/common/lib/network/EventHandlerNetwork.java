package thaumcraft.common.lib.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import thaumcraft.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.network.misc.PacketConfig;
import thaumcraft.common.lib.network.playerdata.PacketSyncKnowledge;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarp;

public class EventHandlerNetwork {
   @SubscribeEvent
   public void playerLoggedInEvent(PlayerLoggedInEvent event) {
      Side side = FMLCommonHandler.instance().getEffectiveSide();
      if (side == Side.SERVER) {
         EntityPlayer p = event.player;
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(p), (EntityPlayerMP)p);
         PacketHandler.INSTANCE.sendTo(new PacketSyncKnowledge(p), (EntityPlayerMP)p);
         PacketHandler.INSTANCE.sendTo(new PacketConfig(), (EntityPlayerMP)p);
      }

   }

   @SubscribeEvent
   public void clientLogsOut(ClientDisconnectionFromServerEvent event) {
      if (Thaumcraft.proxy.getClientWorld() != null) {
         Config.allowCheatSheet = Config.CallowCheatSheet;
         Config.wuss = Config.Cwuss;
         Config.researchDifficulty = Config.CresearchDifficulty;
         Thaumcraft.log.info("Restoring client configs.");
      }

   }
}
