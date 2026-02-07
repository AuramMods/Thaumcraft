package thaumcraft.common.lib.network.misc;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.common.items.casters.CasterManager;
import thaumcraft.common.items.casters.ICaster;

public class PacketFocusChangeToServer implements IMessage, IMessageHandler<PacketFocusChangeToServer, IMessage> {
   private String focus;

   public PacketFocusChangeToServer() {
   }

   public PacketFocusChangeToServer(String focus) {
      this.focus = focus;
   }

   public void toBytes(ByteBuf buffer) {
      ByteBufUtils.writeUTF8String(buffer, this.focus);
   }

   public void fromBytes(ByteBuf buffer) {
      this.focus = ByteBufUtils.readUTF8String(buffer);
   }

   public IMessage onMessage(final PacketFocusChangeToServer message, final MessageContext ctx) {
      IThreadListener mainThread = ctx.getServerHandler().field_147369_b.func_71121_q();
      mainThread.func_152344_a(new Runnable() {
         public void run() {
            World world = ctx.getServerHandler().field_147369_b.func_71121_q();
            if (world != null) {
               Entity player = ctx.getServerHandler().field_147369_b;
               if (player != null && player instanceof EntityPlayer && ((EntityPlayer)player).func_184614_ca() != null && ((EntityPlayer)player).func_184614_ca().func_77973_b() instanceof ICaster) {
                  CasterManager.changeFocus(((EntityPlayer)player).func_184614_ca(), world, (EntityPlayer)player, message.focus);
               } else if (player != null && player instanceof EntityPlayer && ((EntityPlayer)player).func_184592_cb() != null && ((EntityPlayer)player).func_184592_cb().func_77973_b() instanceof ICaster) {
                  CasterManager.changeFocus(((EntityPlayer)player).func_184592_cb(), world, (EntityPlayer)player, message.focus);
               }

            }
         }
      });
      return null;
   }
}
