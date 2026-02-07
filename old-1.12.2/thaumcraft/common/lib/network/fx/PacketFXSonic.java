package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.Thaumcraft;
import thaumcraft.client.fx.other.FXSonic;

public class PacketFXSonic implements IMessage, IMessageHandler<PacketFXSonic, IMessage> {
   private int source;

   public PacketFXSonic() {
   }

   public PacketFXSonic(int source) {
      this.source = source;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.source);
   }

   public void fromBytes(ByteBuf buffer) {
      this.source = buffer.readInt();
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketFXSonic message, MessageContext ctx) {
      Entity p = Thaumcraft.proxy.getClientWorld().func_73045_a(message.source);
      if (p != null) {
         FXSonic fb = new FXSonic(Thaumcraft.proxy.getClientWorld(), p.field_70165_t, p.field_70163_u, p.field_70161_v, p, 10);
         FMLClientHandler.instance().getClient().field_71452_i.func_78873_a(fb);
      }

      return null;
   }
}
