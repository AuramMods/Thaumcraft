package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.common.lib.events.EssentiaHandler;

public class PacketFXEssentiaSource implements IMessage, IMessageHandler<PacketFXEssentiaSource, IMessage> {
   private int x;
   private int y;
   private int z;
   private byte dx;
   private byte dy;
   private byte dz;
   private int color;
   private int ext;

   public PacketFXEssentiaSource() {
   }

   public PacketFXEssentiaSource(BlockPos p1, byte dx, byte dy, byte dz, int color, int e) {
      this.x = p1.func_177958_n();
      this.y = p1.func_177956_o();
      this.z = p1.func_177952_p();
      this.dx = dx;
      this.dy = dy;
      this.dz = dz;
      this.color = color;
      this.ext = e;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.x);
      buffer.writeInt(this.y);
      buffer.writeInt(this.z);
      buffer.writeInt(this.color);
      buffer.writeByte(this.dx);
      buffer.writeByte(this.dy);
      buffer.writeByte(this.dz);
      buffer.writeShort(this.ext);
   }

   public void fromBytes(ByteBuf buffer) {
      this.x = buffer.readInt();
      this.y = buffer.readInt();
      this.z = buffer.readInt();
      this.color = buffer.readInt();
      this.dx = buffer.readByte();
      this.dy = buffer.readByte();
      this.dz = buffer.readByte();
      this.ext = buffer.readShort();
   }

   public IMessage onMessage(PacketFXEssentiaSource message, MessageContext ctx) {
      int tx = message.x - message.dx;
      int ty = message.y - message.dy;
      int tz = message.z - message.dz;
      String key = message.x + ":" + message.y + ":" + message.z + ":" + tx + ":" + ty + ":" + tz + ":" + message.color;
      if (EssentiaHandler.sourceFX.containsKey(key)) {
         EssentiaHandler.EssentiaSourceFX sf = (EssentiaHandler.EssentiaSourceFX)EssentiaHandler.sourceFX.get(key);
         EssentiaHandler.sourceFX.remove(key);
         EssentiaHandler.sourceFX.put(key, sf);
      } else {
         EssentiaHandler.sourceFX.put(key, new EssentiaHandler.EssentiaSourceFX(new BlockPos(message.x, message.y, message.z), new BlockPos(tx, ty, tz), message.color, message.ext));
      }

      return null;
   }
}
