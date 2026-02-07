package thaumcraft.common.lib.network.misc;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.lib.events.RenderEventHandler;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.SoundsTC;

public class PacketMiscEvent implements IMessage, IMessageHandler<PacketMiscEvent, IMessage> {
   private byte type;
   private int value = 0;
   public static final byte WARP_EVENT = 0;
   public static final byte MIST_EVENT = 1;
   public static final byte MIST_EVENT_SHORT = 2;

   public PacketMiscEvent() {
   }

   public PacketMiscEvent(byte type) {
      this.type = type;
   }

   public PacketMiscEvent(byte type, int value) {
      this.type = type;
      this.value = value;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeByte(this.type);
      if (this.value != 0) {
         buffer.writeInt(this.value);
      }

   }

   public void fromBytes(ByteBuf buffer) {
      this.type = buffer.readByte();
      if (buffer.isReadable()) {
         this.value = buffer.readInt();
      }

   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(final PacketMiscEvent message, MessageContext ctx) {
      Minecraft.func_71410_x().func_152344_a(new Runnable() {
         public void run() {
            PacketMiscEvent.this.processMessage(message);
         }
      });
      return null;
   }

   @SideOnly(Side.CLIENT)
   void processMessage(PacketMiscEvent message) {
      EntityPlayer p = Minecraft.func_71410_x().field_71439_g;
      switch(message.type) {
      case 0:
         if (!Config.nostress) {
            p.field_70170_p.func_184134_a(p.field_70165_t, p.field_70163_u, p.field_70161_v, SoundsTC.heartbeat, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
         }
         break;
      case 1:
         RenderEventHandler.fogFiddled = true;
         RenderEventHandler.fogDuration = 2400;
         break;
      case 2:
         RenderEventHandler.fogFiddled = true;
         if (RenderEventHandler.fogDuration < 200) {
            RenderEventHandler.fogDuration = 200;
         }
      }

   }
}
