package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.client.fx.FXDispatcher;

public class PacketFXPollute implements IMessage, IMessageHandler<PacketFXPollute, IMessage> {
   private int x;
   private int y;
   private int z;
   private byte amount;

   public PacketFXPollute() {
   }

   public PacketFXPollute(BlockPos pos, float amt) {
      this.x = pos.func_177958_n();
      this.y = pos.func_177956_o();
      this.z = pos.func_177952_p();
      this.amount = (byte)((int)amt);
   }

   public PacketFXPollute(BlockPos pos, float amt, boolean vary) {
      this(pos, amt);
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.x);
      buffer.writeInt(this.y);
      buffer.writeInt(this.z);
      buffer.writeByte(this.amount);
   }

   public void fromBytes(ByteBuf buffer) {
      this.x = buffer.readInt();
      this.y = buffer.readInt();
      this.z = buffer.readInt();
      this.amount = buffer.readByte();
   }

   public IMessage onMessage(PacketFXPollute message, MessageContext ctx) {
      for(int a = 0; a < Math.min(FXDispatcher.INSTANCE.particleCount(20), message.amount); ++a) {
         FXDispatcher.INSTANCE.drawPollutionParticles(new BlockPos(message.x, message.y, message.z));
      }

      return null;
   }
}
