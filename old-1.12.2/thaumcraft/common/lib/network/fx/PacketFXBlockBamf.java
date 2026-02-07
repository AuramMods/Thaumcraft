package thaumcraft.common.lib.network.fx;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.lib.utils.Utils;

public class PacketFXBlockBamf implements IMessage, IMessageHandler<PacketFXBlockBamf, IMessage> {
   private double x;
   private double y;
   private double z;
   private int color;
   private byte flags;
   private byte face;

   public PacketFXBlockBamf() {
   }

   public PacketFXBlockBamf(double x, double y, double z, int color, boolean sound, boolean flair, EnumFacing side) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.color = color;
      int f = 0;
      if (sound) {
         f = Utils.setBit(f, 0);
      }

      if (flair) {
         f = Utils.setBit(f, 1);
      }

      if (side != null) {
         this.face = (byte)side.ordinal();
      } else {
         this.face = -1;
      }

      this.flags = (byte)f;
   }

   public PacketFXBlockBamf(BlockPos pos, int color, boolean sound, boolean flair, EnumFacing side) {
      this.x = (double)pos.func_177958_n() + 0.5D;
      this.y = (double)pos.func_177956_o() + 0.5D;
      this.z = (double)pos.func_177952_p() + 0.5D;
      this.color = color;
      int f = 0;
      if (sound) {
         f = Utils.setBit(f, 0);
      }

      if (flair) {
         f = Utils.setBit(f, 1);
      }

      if (side != null) {
         this.face = (byte)side.ordinal();
      } else {
         this.face = -1;
      }

      this.flags = (byte)f;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeDouble(this.x);
      buffer.writeDouble(this.y);
      buffer.writeDouble(this.z);
      buffer.writeInt(this.color);
      buffer.writeByte(this.flags);
      buffer.writeByte(this.face);
   }

   public void fromBytes(ByteBuf buffer) {
      this.x = buffer.readDouble();
      this.y = buffer.readDouble();
      this.z = buffer.readDouble();
      this.color = buffer.readInt();
      this.flags = buffer.readByte();
      this.face = buffer.readByte();
   }

   public IMessage onMessage(final PacketFXBlockBamf message, MessageContext ctx) {
      Minecraft.func_71410_x().func_152344_a(new Runnable() {
         public void run() {
            PacketFXBlockBamf.this.processMessage(message);
         }
      });
      return null;
   }

   @SideOnly(Side.CLIENT)
   void processMessage(PacketFXBlockBamf message) {
      EnumFacing side = null;
      if (message.face >= 0) {
         side = EnumFacing.func_82600_a(message.face);
      }

      if (message.color != -9999) {
         FXDispatcher.INSTANCE.drawBamf(message.x, message.y, message.z, message.color, Utils.getBit(message.flags, 0), Utils.getBit(message.flags, 1), side);
      } else {
         FXDispatcher.INSTANCE.drawBamf(message.x, message.y, message.z, Utils.getBit(message.flags, 0), Utils.getBit(message.flags, 1), side);
      }

   }
}
