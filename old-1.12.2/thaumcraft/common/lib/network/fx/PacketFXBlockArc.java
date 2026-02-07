package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.client.fx.FXDispatcher;

public class PacketFXBlockArc implements IMessage, IMessageHandler<PacketFXBlockArc, IMessage> {
   private int x;
   private int y;
   private int z;
   private float tx;
   private float ty;
   private float tz;
   private float r;
   private float g;
   private float b;

   public PacketFXBlockArc() {
   }

   public PacketFXBlockArc(BlockPos pos, Entity source, float r, float g, float b) {
      this.x = pos.func_177958_n();
      this.y = pos.func_177956_o();
      this.z = pos.func_177952_p();
      this.tx = (float)source.field_70165_t;
      this.ty = (float)(source.func_174813_aQ().field_72338_b + (double)(source.field_70131_O / 2.0F));
      this.tz = (float)source.field_70161_v;
      this.r = r;
      this.g = g;
      this.b = b;
   }

   public PacketFXBlockArc(BlockPos pos, BlockPos pos2, float r, float g, float b) {
      this.x = pos.func_177958_n();
      this.y = pos.func_177956_o();
      this.z = pos.func_177952_p();
      this.tx = (float)pos2.func_177958_n() + 0.5F;
      this.ty = (float)pos2.func_177956_o() + 0.5F;
      this.tz = (float)pos2.func_177952_p() + 0.5F;
      this.r = r;
      this.g = g;
      this.b = b;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.x);
      buffer.writeInt(this.y);
      buffer.writeInt(this.z);
      buffer.writeFloat(this.tx);
      buffer.writeFloat(this.ty);
      buffer.writeFloat(this.tz);
      buffer.writeFloat(this.r);
      buffer.writeFloat(this.g);
      buffer.writeFloat(this.b);
   }

   public void fromBytes(ByteBuf buffer) {
      this.x = buffer.readInt();
      this.y = buffer.readInt();
      this.z = buffer.readInt();
      this.tx = buffer.readFloat();
      this.ty = buffer.readFloat();
      this.tz = buffer.readFloat();
      this.r = buffer.readFloat();
      this.g = buffer.readFloat();
      this.b = buffer.readFloat();
   }

   public IMessage onMessage(PacketFXBlockArc message, MessageContext ctx) {
      FXDispatcher.INSTANCE.arcLightning((double)message.tx, (double)message.ty, (double)message.tz, (double)message.x + 0.5D, (double)message.y + 0.5D, (double)message.z + 0.5D, message.r, message.g, message.b, 0.5F);
      return null;
   }
}
