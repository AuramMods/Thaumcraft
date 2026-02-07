package thaumcraft.common.lib.network.misc;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.common.config.Config;

public class PacketConfig implements IMessage, IMessageHandler<PacketConfig, IMessage> {
   boolean b1;
   boolean b5;
   byte by1;

   public void toBytes(ByteBuf dos) {
      dos.writeBoolean(Config.allowCheatSheet);
      dos.writeBoolean(Config.wuss);
      dos.writeByte(Config.researchDifficulty);
   }

   public void fromBytes(ByteBuf dat) {
      this.b1 = dat.readBoolean();
      this.b5 = dat.readBoolean();
      this.by1 = dat.readByte();
   }

   public IMessage onMessage(PacketConfig message, MessageContext ctx) {
      Config.allowCheatSheet = message.b1;
      Config.wuss = message.b5;
      Config.researchDifficulty = message.by1;
      return null;
   }
}
