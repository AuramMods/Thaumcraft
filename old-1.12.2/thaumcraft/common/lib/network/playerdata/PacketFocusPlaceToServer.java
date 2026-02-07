package thaumcraft.common.lib.network.playerdata;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.common.tiles.crafting.TileFocalManipulator;

public class PacketFocusPlaceToServer implements IMessage, IMessageHandler<PacketFocusPlaceToServer, IMessage> {
   private long loc;
   String part;
   byte q;
   byte r;

   public PacketFocusPlaceToServer() {
   }

   public PacketFocusPlaceToServer(byte q, byte r, BlockPos pos, String part) {
      this.loc = pos.func_177986_g();
      this.part = part;
      this.q = q;
      this.r = r;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeLong(this.loc);
      ByteBufUtils.writeUTF8String(buffer, this.part);
      buffer.writeByte(this.q);
      buffer.writeByte(this.r);
   }

   public void fromBytes(ByteBuf buffer) {
      this.loc = buffer.readLong();
      this.part = ByteBufUtils.readUTF8String(buffer);
      this.q = buffer.readByte();
      this.r = buffer.readByte();
   }

   public IMessage onMessage(final PacketFocusPlaceToServer message, final MessageContext ctx) {
      IThreadListener mainThread = ctx.getServerHandler().field_147369_b.func_71121_q();
      mainThread.func_152344_a(new Runnable() {
         public void run() {
            if (ctx.getServerHandler().field_147369_b != null) {
               BlockPos pos = BlockPos.func_177969_a(message.loc);
               TileEntity rt = ctx.getServerHandler().field_147369_b.field_70170_p.func_175625_s(pos);
               if (rt != null && rt instanceof TileFocalManipulator) {
                  ((TileFocalManipulator)rt).placePart(message.q, message.r, message.part, ctx.getServerHandler().field_147369_b);
               }

            }
         }
      });
      return null;
   }
}
