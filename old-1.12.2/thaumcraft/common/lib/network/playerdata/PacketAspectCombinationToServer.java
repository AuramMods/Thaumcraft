package thaumcraft.common.lib.network.playerdata;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.crafting.TileResearchTable;

public class PacketAspectCombinationToServer implements IMessage, IMessageHandler<PacketAspectCombinationToServer, IMessage> {
   private long loc;
   Aspect aspect1;
   Aspect aspect2;

   public PacketAspectCombinationToServer() {
   }

   public PacketAspectCombinationToServer(BlockPos pos, Aspect aspect1, Aspect aspect2) {
      this.loc = pos.func_177986_g();
      this.aspect1 = aspect1;
      this.aspect2 = aspect2;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeLong(this.loc);
      ByteBufUtils.writeUTF8String(buffer, this.aspect1.getTag());
      ByteBufUtils.writeUTF8String(buffer, this.aspect2.getTag());
   }

   public void fromBytes(ByteBuf buffer) {
      this.loc = buffer.readLong();
      this.aspect1 = Aspect.getAspect(ByteBufUtils.readUTF8String(buffer));
      this.aspect2 = Aspect.getAspect(ByteBufUtils.readUTF8String(buffer));
   }

   public IMessage onMessage(final PacketAspectCombinationToServer message, final MessageContext ctx) {
      IThreadListener mainThread = ctx.getServerHandler().field_147369_b.func_71121_q();
      mainThread.func_152344_a(new Runnable() {
         public void run() {
            if (ctx.getServerHandler().field_147369_b != null) {
               World world = ctx.getServerHandler().field_147369_b.field_70170_p;
               BlockPos pos = BlockPos.func_177969_a(message.loc);
               if (message.aspect1 != null && message.aspect2 != null) {
                  Aspect combo = ResearchManager.getCombinationResult(message.aspect1, message.aspect2);
                  if (combo != null) {
                     TileEntity rt = world.func_175625_s(pos);
                     if (rt != null && rt instanceof TileResearchTable) {
                        TileResearchTable table = (TileResearchTable)rt;
                        if (table.data == null) {
                           return;
                        }
                     }
                  }
               }

            }
         }
      });
      return null;
   }
}
