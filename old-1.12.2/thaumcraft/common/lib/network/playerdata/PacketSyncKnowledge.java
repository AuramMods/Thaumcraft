package thaumcraft.common.lib.network.playerdata;

import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchEntry;
import thaumcraft.client.lib.events.RenderEventHandler;
import thaumcraft.common.lib.utils.Utils;

public class PacketSyncKnowledge implements IMessage, IMessageHandler<PacketSyncKnowledge, IMessage> {
   protected NBTTagCompound data;

   public PacketSyncKnowledge() {
   }

   public PacketSyncKnowledge(EntityPlayer player) {
      IPlayerKnowledge pk = ThaumcraftCapabilities.getKnowledge(player);
      this.data = (NBTTagCompound)pk.serializeNBT();
      Iterator var3 = pk.getResearchList().iterator();

      while(var3.hasNext()) {
         String key = (String)var3.next();
         pk.clearResearchFlag(key, IPlayerKnowledge.EnumResearchFlag.POPUP);
      }

   }

   public void toBytes(ByteBuf buffer) {
      Utils.writeNBTTagCompoundToBuffer(buffer, this.data);
   }

   public void fromBytes(ByteBuf buffer) {
      this.data = Utils.readNBTTagCompoundFromBuffer(buffer);
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(final PacketSyncKnowledge message, MessageContext ctx) {
      Minecraft.func_71410_x().func_152344_a(new Runnable() {
         public void run() {
            EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
            IPlayerKnowledge pk = ThaumcraftCapabilities.getKnowledge(player);
            pk.deserializeNBT(message.data);

            String key;
            for(Iterator var3 = pk.getResearchList().iterator(); var3.hasNext(); pk.clearResearchFlag(key, IPlayerKnowledge.EnumResearchFlag.POPUP)) {
               key = (String)var3.next();
               if (pk.hasResearchFlag(key, IPlayerKnowledge.EnumResearchFlag.POPUP)) {
                  ResearchEntry ri = ResearchCategories.getResearch(key);
                  if (ri != null) {
                     RenderEventHandler var10000 = RenderEventHandler.INSTANCE;
                     RenderEventHandler.researchPopup.queueResearchInformation(ri);
                  }
               }
            }

         }
      });
      return null;
   }
}
