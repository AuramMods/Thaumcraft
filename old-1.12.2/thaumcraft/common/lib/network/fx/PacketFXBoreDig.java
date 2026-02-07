package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.lib.events.ServerEvents;

public class PacketFXBoreDig implements IMessage, IMessageHandler<PacketFXBoreDig, IMessage> {
   private int x;
   private int y;
   private int z;
   private int bore;
   private int delay;

   public PacketFXBoreDig() {
   }

   public PacketFXBoreDig(BlockPos pos, Entity bore, int delay) {
      this.x = pos.func_177958_n();
      this.y = pos.func_177956_o();
      this.z = pos.func_177952_p();
      this.bore = bore.func_145782_y();
      this.delay = delay;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.x);
      buffer.writeInt(this.y);
      buffer.writeInt(this.z);
      buffer.writeInt(this.bore);
      buffer.writeInt(this.delay);
   }

   public void fromBytes(ByteBuf buffer) {
      this.x = buffer.readInt();
      this.y = buffer.readInt();
      this.z = buffer.readInt();
      this.bore = buffer.readInt();
      this.delay = buffer.readInt();
   }

   public IMessage onMessage(final PacketFXBoreDig message, MessageContext ctx) {
      Minecraft.func_71410_x().func_152344_a(new Runnable() {
         public void run() {
            PacketFXBoreDig.this.processMessage(message);
         }
      });
      return null;
   }

   @SideOnly(Side.CLIENT)
   void processMessage(final PacketFXBoreDig message) {
      try {
         World world = Minecraft.func_71410_x().field_71441_e;
         final BlockPos pos = new BlockPos(message.x, message.y, message.z);
         final Entity entity = world.func_73045_a(message.bore);
         if (entity == null) {
            return;
         }

         final IBlockState ts = world.func_180495_p(pos);
         if (ts.func_177230_c() == Blocks.field_150350_a) {
            return;
         }

         for(int a = 0; a < message.delay; ++a) {
            ServerEvents.addRunnableClient(world, new Runnable() {
               public void run() {
                  FXDispatcher.INSTANCE.boreDigFx(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), entity, ts, ts.func_177230_c().func_176201_c(ts) >> 12 & 255, message.delay);
               }
            }, a);
         }
      } catch (Exception var7) {
      }

   }
}
