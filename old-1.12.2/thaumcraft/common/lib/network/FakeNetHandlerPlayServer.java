package thaumcraft.common.lib.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.server.MinecraftServer;

public class FakeNetHandlerPlayServer extends NetHandlerPlayServer {
   public FakeNetHandlerPlayServer(MinecraftServer server, NetworkManager networkManagerIn, EntityPlayerMP playerIn) {
      super(server, networkManagerIn, playerIn);
   }

   public void func_73660_a() {
   }

   public void func_147360_c(String reason) {
   }

   public void func_147358_a(CPacketInput packetIn) {
   }

   public void func_147347_a(CPacketPlayer packetIn) {
   }

   public void func_147359_a(Packet packetIn) {
   }

   public void func_147340_a(CPacketUseEntity packetIn) {
   }

   public void func_147342_a(CPacketClientStatus packetIn) {
   }

   public void func_147356_a(CPacketCloseWindow packetIn) {
   }

   public void func_147351_a(CPacketClickWindow packetIn) {
   }

   public void func_147338_a(CPacketEnchantItem packetIn) {
   }

   public void func_147344_a(CPacketCreativeInventoryAction packetIn) {
   }

   public void func_147339_a(CPacketConfirmTransaction packetIn) {
   }

   public void func_147343_a(CPacketUpdateSign packetIn) {
   }

   public void func_147353_a(CPacketKeepAlive packetIn) {
   }

   public void func_147341_a(CPacketTabComplete packetIn) {
   }

   public void func_147352_a(CPacketClientSettings packetIn) {
   }

   public void func_147349_a(CPacketCustomPayload packetIn) {
   }
}
