package thaumcraft.common.lib.network.misc;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.client.lib.events.HudHandler;
import thaumcraft.common.world.aura.AuraChunk;

public class PacketAuraToClient implements IMessage, IMessageHandler<PacketAuraToClient, IMessage> {
   short base;
   float vis;
   float flux;

   public PacketAuraToClient() {
   }

   public PacketAuraToClient(AuraChunk ac) {
      this.base = ac.getBase();
      this.vis = ac.getVis();
      this.flux = ac.getFlux();
   }

   public void toBytes(ByteBuf dos) {
      dos.writeShort(this.base);
      dos.writeFloat(this.vis);
      dos.writeFloat(this.flux);
   }

   public void fromBytes(ByteBuf dat) {
      this.base = dat.readShort();
      this.vis = dat.readFloat();
      this.flux = dat.readFloat();
   }

   public IMessage onMessage(final PacketAuraToClient message, MessageContext ctx) {
      Minecraft.func_71410_x().func_152344_a(new Runnable() {
         public void run() {
            HudHandler.currentAura = new AuraChunk((Chunk)null, message.base, message.vis, message.flux);
         }
      });
      return null;
   }
}
