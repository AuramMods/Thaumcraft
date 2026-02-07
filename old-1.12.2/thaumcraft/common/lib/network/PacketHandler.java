package thaumcraft.common.lib.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import thaumcraft.common.lib.network.fx.PacketFXBlockArc;
import thaumcraft.common.lib.network.fx.PacketFXBlockBamf;
import thaumcraft.common.lib.network.fx.PacketFXBlockMist;
import thaumcraft.common.lib.network.fx.PacketFXBoreDig;
import thaumcraft.common.lib.network.fx.PacketFXEssentiaSource;
import thaumcraft.common.lib.network.fx.PacketFXFocusPartImpact;
import thaumcraft.common.lib.network.fx.PacketFXInfusionSource;
import thaumcraft.common.lib.network.fx.PacketFXPollute;
import thaumcraft.common.lib.network.fx.PacketFXScanSource;
import thaumcraft.common.lib.network.fx.PacketFXShield;
import thaumcraft.common.lib.network.fx.PacketFXSlash;
import thaumcraft.common.lib.network.fx.PacketFXSonic;
import thaumcraft.common.lib.network.fx.PacketFXWispZap;
import thaumcraft.common.lib.network.fx.PacketFXZap;
import thaumcraft.common.lib.network.misc.PacketAuraToClient;
import thaumcraft.common.lib.network.misc.PacketBiomeChange;
import thaumcraft.common.lib.network.misc.PacketConfig;
import thaumcraft.common.lib.network.misc.PacketFocusChangeToServer;
import thaumcraft.common.lib.network.misc.PacketItemKeyToServer;
import thaumcraft.common.lib.network.misc.PacketKnowledgeGain;
import thaumcraft.common.lib.network.misc.PacketMiscEvent;
import thaumcraft.common.lib.network.misc.PacketNote;
import thaumcraft.common.lib.network.misc.PacketSealToClient;
import thaumcraft.common.lib.network.misc.PacketStartGolemCraftToServer;
import thaumcraft.common.lib.network.misc.PacketStartTheoryToServer;
import thaumcraft.common.lib.network.playerdata.PacketAspectCombinationToServer;
import thaumcraft.common.lib.network.playerdata.PacketAspectPlaceToServer;
import thaumcraft.common.lib.network.playerdata.PacketFocusPlaceToServer;
import thaumcraft.common.lib.network.playerdata.PacketSyncKnowledge;
import thaumcraft.common.lib.network.playerdata.PacketSyncProgressToServer;
import thaumcraft.common.lib.network.playerdata.PacketSyncResearchFlagsToServer;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarp;
import thaumcraft.common.lib.network.playerdata.PacketWarpMessage;

public class PacketHandler {
   public static final SimpleNetworkWrapper INSTANCE;

   public static void preInit() {
      int idx = 0;
      int var1 = idx + 1;
      INSTANCE.registerMessage(PacketBiomeChange.class, PacketBiomeChange.class, idx, Side.CLIENT);
      INSTANCE.registerMessage(PacketConfig.class, PacketConfig.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketMiscEvent.class, PacketMiscEvent.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketKnowledgeGain.class, PacketKnowledgeGain.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketStartGolemCraftToServer.class, PacketStartGolemCraftToServer.class, var1++, Side.SERVER);
      INSTANCE.registerMessage(PacketStartTheoryToServer.class, PacketStartTheoryToServer.class, var1++, Side.SERVER);
      INSTANCE.registerMessage(PacketAspectCombinationToServer.class, PacketAspectCombinationToServer.class, var1++, Side.SERVER);
      INSTANCE.registerMessage(PacketAspectPlaceToServer.class, PacketAspectPlaceToServer.class, var1++, Side.SERVER);
      INSTANCE.registerMessage(PacketFocusPlaceToServer.class, PacketFocusPlaceToServer.class, var1++, Side.SERVER);
      INSTANCE.registerMessage(PacketAuraToClient.class, PacketAuraToClient.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketSealToClient.class, PacketSealToClient.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketNote.class, PacketNote.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketSyncWarp.class, PacketSyncWarp.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketSyncKnowledge.class, PacketSyncKnowledge.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketWarpMessage.class, PacketWarpMessage.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketItemKeyToServer.class, PacketItemKeyToServer.class, var1++, Side.SERVER);
      INSTANCE.registerMessage(PacketFocusChangeToServer.class, PacketFocusChangeToServer.class, var1++, Side.SERVER);
      INSTANCE.registerMessage(PacketSyncProgressToServer.class, PacketSyncProgressToServer.class, var1++, Side.SERVER);
      INSTANCE.registerMessage(PacketSyncResearchFlagsToServer.class, PacketSyncResearchFlagsToServer.class, var1++, Side.SERVER);
      INSTANCE.registerMessage(PacketFXPollute.class, PacketFXPollute.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXBlockBamf.class, PacketFXBlockBamf.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXFocusPartImpact.class, PacketFXFocusPartImpact.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXBlockMist.class, PacketFXBlockMist.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXBlockArc.class, PacketFXBlockArc.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXEssentiaSource.class, PacketFXEssentiaSource.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXInfusionSource.class, PacketFXInfusionSource.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXShield.class, PacketFXShield.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXSonic.class, PacketFXSonic.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXWispZap.class, PacketFXWispZap.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXZap.class, PacketFXZap.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXSlash.class, PacketFXSlash.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXScanSource.class, PacketFXScanSource.class, var1++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXBoreDig.class, PacketFXBoreDig.class, var1++, Side.CLIENT);
   }

   static {
      INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("thaumcraft".toLowerCase());
   }
}
