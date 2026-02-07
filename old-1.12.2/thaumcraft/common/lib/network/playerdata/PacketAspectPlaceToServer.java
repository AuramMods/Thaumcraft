package thaumcraft.common.lib.network.playerdata;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.crafting.TileResearchTable;

public class PacketAspectPlaceToServer implements IMessage, IMessageHandler<PacketAspectPlaceToServer, IMessage> {
   private long loc;
   Aspect aspect;
   byte q;
   byte r;

   public PacketAspectPlaceToServer() {
   }

   public PacketAspectPlaceToServer(byte q, byte r, BlockPos pos, Aspect aspect) {
      this.loc = pos.func_177986_g();
      this.aspect = aspect;
      this.q = q;
      this.r = r;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeLong(this.loc);
      ByteBufUtils.writeUTF8String(buffer, this.aspect == null ? "null" : this.aspect.getTag());
      buffer.writeByte(this.q);
      buffer.writeByte(this.r);
   }

   public void fromBytes(ByteBuf buffer) {
      this.loc = buffer.readLong();
      this.aspect = Aspect.getAspect(ByteBufUtils.readUTF8String(buffer));
      this.q = buffer.readByte();
      this.r = buffer.readByte();
   }

   public IMessage onMessage(final PacketAspectPlaceToServer message, final MessageContext ctx) {
      IThreadListener mainThread = ctx.getServerHandler().field_147369_b.func_71121_q();
      mainThread.func_152344_a(new Runnable() {
         public void run() {
            if (ctx.getServerHandler().field_147369_b != null) {
               BlockPos pos = BlockPos.func_177969_a(message.loc);
               TileEntity rt = ctx.getServerHandler().field_147369_b.field_70170_p.func_175625_s(pos);
               if (rt != null && rt instanceof TileResearchTable) {
               }

            }
         }
      });
      return null;
   }
}
