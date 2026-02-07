package thaumcraft.common.lib.network.misc;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.common.tiles.crafting.TileGolemBuilder;

public class PacketStartGolemCraftToServer implements IMessage, IMessageHandler<PacketStartGolemCraftToServer, IMessage> {
   private long pos;
   private long golem;

   public PacketStartGolemCraftToServer() {
   }

   public PacketStartGolemCraftToServer(EntityPlayer player, BlockPos pos, long golem) {
      this.pos = pos.func_177986_g();
      this.golem = golem;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeLong(this.pos);
      buffer.writeLong(this.golem);
   }

   public void fromBytes(ByteBuf buffer) {
      this.pos = buffer.readLong();
      this.golem = buffer.readLong();
   }

   public IMessage onMessage(final PacketStartGolemCraftToServer message, final MessageContext ctx) {
      IThreadListener mainThread = ctx.getServerHandler().field_147369_b.func_71121_q();
      mainThread.func_152344_a(new Runnable() {
         public void run() {
            World world = ctx.getServerHandler().field_147369_b.func_71121_q();
            Entity player = ctx.getServerHandler().field_147369_b;
            BlockPos bp = BlockPos.func_177969_a(message.pos);
            if (world != null && player != null && player instanceof EntityPlayer && bp != null) {
               TileEntity te = world.func_175625_s(bp);
               if (te != null && te instanceof TileGolemBuilder) {
                  ((TileGolemBuilder)te).startCraft(message.golem, (EntityPlayer)player);
               }
            }

         }
      });
      return null;
   }
}
