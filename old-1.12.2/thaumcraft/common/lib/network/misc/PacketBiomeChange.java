package thaumcraft.common.lib.network.misc;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.Thaumcraft;
import thaumcraft.common.lib.utils.Utils;

public class PacketBiomeChange implements IMessage, IMessageHandler<PacketBiomeChange, IMessage> {
   private int x;
   private int z;
   private short biome;

   public PacketBiomeChange() {
   }

   public PacketBiomeChange(int x, int z, short biome) {
      this.x = x;
      this.z = z;
      this.biome = biome;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.x);
      buffer.writeInt(this.z);
      buffer.writeShort(this.biome);
   }

   public void fromBytes(ByteBuf buffer) {
      this.x = buffer.readInt();
      this.z = buffer.readInt();
      this.biome = buffer.readShort();
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(final PacketBiomeChange message, MessageContext ctx) {
      Minecraft.func_71410_x().func_152344_a(new Runnable() {
         public void run() {
            Utils.setBiomeAt(Thaumcraft.proxy.getClientWorld(), new BlockPos(message.x, 0, message.z), Biome.func_150568_d(message.biome));
         }
      });
      return null;
   }
}
