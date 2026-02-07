package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.entities.monster.EntityWisp;

public class PacketFXWispZap implements IMessage, IMessageHandler<PacketFXWispZap, IMessage> {
   private int source;
   private int target;

   public PacketFXWispZap() {
   }

   public PacketFXWispZap(int source, int target) {
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

   public IMessage onMessage(PacketFXWispZap message, MessageContext ctx) {
      Minecraft mc = FMLClientHandler.instance().getClient();
      WorldClient world = mc.field_71441_e;
      Entity var2 = this.getEntityByID(message.source, mc, world);
      Entity var3 = this.getEntityByID(message.target, mc, world);
      if (var2 != null && var3 != null) {
         float r = 1.0F;
         float g = 1.0F;
         float b = 1.0F;
         if (var2 instanceof EntityWisp) {
            Color c = new Color(Aspect.getAspect(((EntityWisp)var2).getType()).getColor());
            r = (float)c.getRed() / 255.0F;
            g = (float)c.getGreen() / 255.0F;
            b = (float)c.getBlue() / 255.0F;
         }

         FXDispatcher.INSTANCE.arcBolt(var2.field_70165_t, var2.field_70163_u, var2.field_70161_v, var3.field_70165_t, var3.field_70163_u, var3.field_70161_v, r, g, b, 1.0F);
      }

      return null;
   }

   @SideOnly(Side.CLIENT)
   private Entity getEntityByID(int par1, Minecraft mc, WorldClient world) {
      return (Entity)(par1 == mc.field_71439_g.func_145782_y() ? mc.field_71439_g : world.func_73045_a(par1));
   }
}
