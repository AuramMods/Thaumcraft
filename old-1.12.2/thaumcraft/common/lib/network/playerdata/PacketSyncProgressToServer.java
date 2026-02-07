package thaumcraft.common.lib.network.playerdata;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchEntry;
import thaumcraft.api.research.ResearchStage;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.utils.InventoryUtils;

public class PacketSyncProgressToServer implements IMessage, IMessageHandler<PacketSyncProgressToServer, IMessage> {
   private String key;
   private boolean first;
   private boolean checks;
   private boolean noFlags;

   public PacketSyncProgressToServer() {
   }

   public PacketSyncProgressToServer(String key, boolean first, boolean checks, boolean noFlags) {
      this.key = key;
      this.first = first;
      this.checks = checks;
      this.noFlags = noFlags;
   }

   public PacketSyncProgressToServer(String key, boolean first) {
      this(key, first, false, true);
   }

   public void toBytes(ByteBuf buffer) {
      ByteBufUtils.writeUTF8String(buffer, this.key);
      buffer.writeBoolean(this.first);
      buffer.writeBoolean(this.checks);
      buffer.writeBoolean(this.noFlags);
   }

   public void fromBytes(ByteBuf buffer) {
      this.key = ByteBufUtils.readUTF8String(buffer);
      this.first = buffer.readBoolean();
      this.checks = buffer.readBoolean();
      this.noFlags = buffer.readBoolean();
   }

   public IMessage onMessage(final PacketSyncProgressToServer message, final MessageContext ctx) {
      IThreadListener mainThread = ctx.getServerHandler().field_147369_b.func_71121_q();
      mainThread.func_152344_a(new Runnable() {
         public void run() {
            EntityPlayer player = ctx.getServerHandler().field_147369_b;
            if (player != null && message.first != ThaumcraftCapabilities.knowsResearch(player, message.key)) {
               if (message.checks && !PacketSyncProgressToServer.this.checkRequisites(player, message.key)) {
                  return;
               }

               if (message.noFlags) {
                  ResearchManager.noFlags = true;
               }

               ResearchManager.progressResearch(player, message.key);
            }

         }
      });
      return null;
   }

   private boolean checkRequisites(EntityPlayer player, String key) {
      ResearchEntry research = ResearchCategories.getResearch(key);
      if (research.getStages() != null) {
         int currentStage = ThaumcraftCapabilities.getKnowledge(player).getResearchStage(key) - 1;
         if (currentStage < 0) {
            return false;
         }

         if (currentStage >= research.getStages().length) {
            return true;
         }

         ResearchStage stage = research.getStages()[currentStage];
         ItemStack[] o = stage.getObtain();
         if (o != null) {
            int a;
            for(a = 0; a < o.length; ++a) {
               if (!InventoryUtils.isPlayerCarryingAmount(player, o[a], false)) {
                  return false;
               }
            }

            for(a = 0; a < o.length; ++a) {
               InventoryUtils.consumeInventoryItem(player, o[a], true, false);
            }
         }

         ItemStack[] c = stage.getCraft();
         if (c != null) {
            for(int a = 0; a < c.length; ++a) {
               if (!ThaumcraftCapabilities.getKnowledge(player).isResearchKnown("[#]" + stage.getCraftReference()[a])) {
                  return false;
               }
            }
         }

         String[] r = stage.getResearch();
         if (r != null) {
            for(int a = 0; a < r.length; ++a) {
               if (!ThaumcraftCapabilities.knowsResearchStrict(player, r[a])) {
                  return false;
               }
            }
         }

         ResearchStage.Knowledge[] k = stage.getKnow();
         if (k != null) {
            int a;
            for(a = 0; a < k.length; ++a) {
               int pk = ThaumcraftCapabilities.getKnowledge(player).getKnowledge(k[a].type, k[a].category);
               if (pk < k[a].amount) {
                  return false;
               }
            }

            for(a = 0; a < k.length; ++a) {
               ResearchManager.addKnowledge(player, k[a].type, k[a].category, -k[a].amount);
            }
         }
      }

      return true;
   }
}
