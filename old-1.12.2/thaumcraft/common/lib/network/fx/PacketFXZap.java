package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;

public class PacketFXZap implements IMessage, IMessageHandler<PacketFXZap, IMessage> {
   private int source;
   private int target;

   public PacketFXZap() {
   }

   public PacketFXZap(int source, int target) {
      this.source = source;
      this.target = target;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.source);
      buffer.writeInt(this.target);
   }

   public void fromBytes(ByteBuf buffer) {
      this.source = buffer.readInt();
      this.target = buffer.readInt();
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketFXZap message, MessageContext ctx) {
      Minecraft mc = FMLClientHandler.instance().getClient();
      WorldClient world = mc.field_71441_e;
      Entity var2 = this.getEntityByID(message.source, mc, world);
      Entity var3 = this.getEntityByID(message.target, mc, world);
      if (var2 != null && var3 != null) {
         FXDispatcher.INSTANCE.arcBolt(var2.field_70165_t, var2.func_174813_aQ().field_72338_b + (double)(var2.field_70131_O / 2.0F), var2.field_70161_v, var3.field_70165_t, var3.func_174813_aQ().field_72338_b + (double)(var3.field_70131_O / 2.0F), var3.field_70161_v, 0.5F, 1.0F, 1.0F, 1.0F);
      }

      return null;
   }

   @SideOnly(Side.CLIENT)
   private Entity getEntityByID(int par1, Minecraft mc, WorldClient world) {
      return (Entity)(par1 == mc.field_71439_g.func_145782_y() ? mc.field_71439_g : world.func_73045_a(par1));
   }
}
