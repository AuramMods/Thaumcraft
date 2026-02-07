package thaumcraft.common.lib.network.misc;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.common.items.casters.CasterManager;
import thaumcraft.common.items.casters.ICaster;
import thaumcraft.common.items.tools.ItemElementalShovel;

public class PacketItemKeyToServer implements IMessage, IMessageHandler<PacketItemKeyToServer, IMessage> {
   private byte key;

   public PacketItemKeyToServer() {
   }

   public PacketItemKeyToServer(int key) {
      this.key = (byte)key;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeByte(this.key);
   }

   public void fromBytes(ByteBuf buffer) {
      this.key = buffer.readByte();
   }

   public IMessage onMessage(final PacketItemKeyToServer message, final MessageContext ctx) {
      IThreadListener mainThread = ctx.getServerHandler().field_147369_b.func_71121_q();
      mainThread.func_152344_a(new Runnable() {
         public void run() {
            World world = ctx.getServerHandler().field_147369_b.func_71121_q();
            if (world != null) {
               Entity player = ctx.getServerHandler().field_147369_b;
               if (player != null && player instanceof EntityPlayer && ((EntityPlayer)player).func_184614_ca() != null) {
                  if (message.key == 1 && ((EntityPlayer)player).func_184614_ca().func_77973_b() instanceof ICaster) {
                     CasterManager.toggleMisc(((EntityPlayer)player).func_184614_ca(), world, (EntityPlayer)player);
                  }

                  if (message.key == 1 && ((EntityPlayer)player).func_184614_ca().func_77973_b() instanceof ItemElementalShovel) {
                     ItemElementalShovel var10000 = (ItemElementalShovel)((EntityPlayer)player).func_184614_ca().func_77973_b();
                     byte b = ItemElementalShovel.getOrientation(((EntityPlayer)player).func_184614_ca());
                     var10000 = (ItemElementalShovel)((EntityPlayer)player).func_184614_ca().func_77973_b();
                     ItemElementalShovel.setOrientation(((EntityPlayer)player).func_184614_ca(), (byte)(b + 1));
                  }
               }

            }
         }
      });
      return null;
   }
}
