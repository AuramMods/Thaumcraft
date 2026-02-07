package thaumcraft.common.lib.network.playerdata;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.common.lib.utils.Utils;

public class PacketSyncWarp implements IMessage, IMessageHandler<PacketSyncWarp, IMessage> {
   protected NBTTagCompound data;

   public PacketSyncWarp() {
   }

   public PacketSyncWarp(EntityPlayer player) {
      IPlayerWarp pk = ThaumcraftCapabilities.getWarp(player);
      this.data = (NBTTagCompound)pk.serializeNBT();
   }

   public void toBytes(ByteBuf buffer) {
      Utils.writeNBTTagCompoundToBuffer(buffer, this.data);
   }

   public void fromBytes(ByteBuf buffer) {
      this.data = Utils.readNBTTagCompoundFromBuffer(buffer);
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(final PacketSyncWarp message, MessageContext ctx) {
      Minecraft.func_71410_x().func_152344_a(new Runnable() {
         public void run() {
            EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
            IPlayerWarp pk = ThaumcraftCapabilities.getWarp(player);
            pk.deserializeNBT(message.data);
         }
      });
      return null;
   }
}
