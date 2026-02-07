package thaumcraft.common.lib.network.playerdata;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.common.lib.utils.Utils;

public class PacketSyncResearchFlagsToServer implements IMessage, IMessageHandler<PacketSyncResearchFlagsToServer, IMessage> {
   String key;
   byte flags;

   public PacketSyncResearchFlagsToServer() {
   }

   public PacketSyncResearchFlagsToServer(EntityPlayer player, String key) {
      this.key = key;
      this.flags = Utils.pack(ThaumcraftCapabilities.getKnowledge(player).hasResearchFlag(key, IPlayerKnowledge.EnumResearchFlag.PAGE), ThaumcraftCapabilities.getKnowledge(player).hasResearchFlag(key, IPlayerKnowledge.EnumResearchFlag.POPUP), ThaumcraftCapabilities.getKnowledge(player).hasResearchFlag(key, IPlayerKnowledge.EnumResearchFlag.RESEARCH));
   }

   public void toBytes(ByteBuf buffer) {
      ByteBufUtils.writeUTF8String(buffer, this.key);
      buffer.writeByte(this.flags);
   }

   public void fromBytes(ByteBuf buffer) {
      this.key = ByteBufUtils.readUTF8String(buffer);
      this.flags = buffer.readByte();
   }

   public IMessage onMessage(final PacketSyncResearchFlagsToServer message, final MessageContext ctx) {
      IThreadListener mainThread = ctx.getServerHandler().field_147369_b.func_71121_q();
      mainThread.func_152344_a(new Runnable() {
         public void run() {
            boolean[] b = Utils.unpack(message.flags);
            if (ctx.getServerHandler().field_147369_b != null) {
               EntityPlayer player = ctx.getServerHandler().field_147369_b;
               if (b[0]) {
                  ThaumcraftCapabilities.getKnowledge(player).setResearchFlag(message.key, IPlayerKnowledge.EnumResearchFlag.PAGE);
               } else {
                  ThaumcraftCapabilities.getKnowledge(player).clearResearchFlag(message.key, IPlayerKnowledge.EnumResearchFlag.PAGE);
               }

               if (b[1]) {
                  ThaumcraftCapabilities.getKnowledge(player).setResearchFlag(message.key, IPlayerKnowledge.EnumResearchFlag.POPUP);
               } else {
                  ThaumcraftCapabilities.getKnowledge(player).clearResearchFlag(message.key, IPlayerKnowledge.EnumResearchFlag.POPUP);
               }

               if (b[2]) {
                  ThaumcraftCapabilities.getKnowledge(player).setResearchFlag(message.key, IPlayerKnowledge.EnumResearchFlag.RESEARCH);
               } else {
                  ThaumcraftCapabilities.getKnowledge(player).clearResearchFlag(message.key, IPlayerKnowledge.EnumResearchFlag.RESEARCH);
               }
            }

         }
      });
      return null;
   }
}
