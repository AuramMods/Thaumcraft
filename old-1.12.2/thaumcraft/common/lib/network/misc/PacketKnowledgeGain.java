package thaumcraft.common.lib.network.misc;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.client.lib.events.HudHandler;
import thaumcraft.client.lib.events.RenderEventHandler;
import thaumcraft.common.lib.SoundsTC;

public class PacketKnowledgeGain implements IMessage, IMessageHandler<PacketKnowledgeGain, IMessage> {
   private byte type;
   private String cat;

   public PacketKnowledgeGain() {
   }

   public PacketKnowledgeGain(byte type, String value) {
      this.type = type;
      this.cat = value == null ? "" : value;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeByte(this.type);
      ByteBufUtils.writeUTF8String(buffer, this.cat);
   }

   public void fromBytes(ByteBuf buffer) {
      this.type = buffer.readByte();
      this.cat = ByteBufUtils.readUTF8String(buffer);
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(final PacketKnowledgeGain message, MessageContext ctx) {
      Minecraft.func_71410_x().func_152344_a(new Runnable() {
         public void run() {
            PacketKnowledgeGain.this.processMessage(message);
         }
      });
      return null;
   }

   @SideOnly(Side.CLIENT)
   void processMessage(PacketKnowledgeGain message) {
      EntityPlayer p = Minecraft.func_71410_x().field_71439_g;
      IPlayerKnowledge.EnumKnowledgeType type = IPlayerKnowledge.EnumKnowledgeType.values()[message.type];
      ResearchCategory cat = message.cat.length() > 0 ? ResearchCategories.getResearchCategory(message.cat) : null;
      RenderEventHandler.INSTANCE.hudHandler.knowledgeGainTrackers.add(new HudHandler.KnowledgeGainTracker(type, cat, 40 + p.field_70170_p.field_73012_v.nextInt(20), p.field_70170_p.field_73012_v.nextLong()));
      p.field_70170_p.func_184134_a(p.field_70165_t, p.field_70163_u, p.field_70161_v, SoundsTC.learn, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
   }
}
