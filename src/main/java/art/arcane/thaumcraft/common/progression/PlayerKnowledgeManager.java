package art.arcane.thaumcraft.common.progression;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.aspect.AspectList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Set;

public final class PlayerKnowledgeManager {
    // TODO(port): Expand baseline research key/stage/flag support to legacy IPlayerKnowledge parity (category knowledge points and full research entry flow).
    // TODO(port): reintroduce player sync packet flow once research UI/thaumonomicon and server-authoritative progression are ported.
    // TODO(port): expose research completion helpers that also trigger popup/page flags once thaumonomicon UI exists.

    private static final String DATA_ID = Thaumcraft.MODID + "_player_knowledge";
    public static final String RESEARCH_BATH_SALTS_HINT = "!BATHSALTS";
    public static final String RESEARCH_ELDRITCH_MINOR = "ELDRITCHMINOR";
    public static final String RESEARCH_ELDRITCH_MAJOR = "ELDRITCHMAJOR";

    private PlayerKnowledgeManager() {
    }

    public static void unlockSalisMundus(ServerPlayer player) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        data.unlockSalisMundus(player.getUUID());
    }

    public static boolean hasSalisMundusUnlocked(ServerPlayer player) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.hasSalisMundusUnlocked(player.getUUID());
    }

    public static boolean hasResearch(ServerPlayer player, String researchKey) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.hasResearch(player.getUUID(), researchKey);
    }

    public static boolean unlockResearch(ServerPlayer player, String researchKey) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.unlockResearch(player.getUUID(), researchKey);
    }

    public static boolean removeResearch(ServerPlayer player, String researchKey) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.removeResearch(player.getUUID(), researchKey);
    }

    public static Set<String> getResearchKeys(ServerPlayer player) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.getResearchKeys(player.getUUID());
    }

    public static int getResearchStage(ServerPlayer player, String researchKey) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.getResearchStage(player.getUUID(), researchKey);
    }

    public static boolean setResearchStage(ServerPlayer player, String researchKey, int stage) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.setResearchStage(player.getUUID(), researchKey, stage);
    }

    public static boolean hasResearchFlag(ServerPlayer player, String researchKey, ResearchFlag flag) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.hasResearchFlag(player.getUUID(), researchKey, flag);
    }

    public static boolean setResearchFlag(ServerPlayer player, String researchKey, ResearchFlag flag) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.setResearchFlag(player.getUUID(), researchKey, flag);
    }

    public static boolean clearResearchFlag(ServerPlayer player, String researchKey, ResearchFlag flag) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.clearResearchFlag(player.getUUID(), researchKey, flag);
    }

    public static Set<ResearchFlag> getResearchFlags(ServerPlayer player, String researchKey) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.getResearchFlags(player.getUUID(), researchKey);
    }

    public static ScanResult recordBlockScan(ServerPlayer player, ResourceLocation blockId, AspectList aspects) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());

        boolean firstScan = data.markBlockScanned(player.getUUID(), blockId);
        int newAspects = data.discoverAspects(player.getUUID(), aspects);

        return new ScanResult(
                firstScan,
                data.getScanCount(player.getUUID()),
                newAspects,
                data.getDiscoveredAspectCount(player.getUUID())
        );
    }

    public static ScanResult recordItemScan(ServerPlayer player, ResourceLocation itemId, AspectList aspects) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());

        boolean firstScan = data.markItemScanned(player.getUUID(), itemId);
        int newAspects = data.discoverAspects(player.getUUID(), aspects);

        return new ScanResult(
                firstScan,
                data.getScanCount(player.getUUID()),
                newAspects,
                data.getDiscoveredAspectCount(player.getUUID())
        );
    }

    public static ScanResult recordEntityScan(ServerPlayer player, ResourceLocation entityId, AspectList aspects) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());

        boolean firstScan = data.markEntityScanned(player.getUUID(), entityId);
        int newAspects = data.discoverAspects(player.getUUID(), aspects);

        return new ScanResult(
                firstScan,
                data.getScanCount(player.getUUID()),
                newAspects,
                data.getDiscoveredAspectCount(player.getUUID())
        );
    }

    public static int getWarp(ServerPlayer player, WarpType type) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.getWarp(player.getUUID(), type);
    }

    public static int addWarp(ServerPlayer player, WarpType type, int delta) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.addWarp(player.getUUID(), type, delta);
    }

    public static int setWarp(ServerPlayer player, WarpType type, int value) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.setWarp(player.getUUID(), type, value);
    }

    public static void clearTemporaryWarp(ServerPlayer player) {
        setWarp(player, WarpType.TEMPORARY, 0);
    }

    public static int getTotalWarp(ServerPlayer player) {
        return getWarp(player, WarpType.PERMANENT) + getWarp(player, WarpType.NORMAL) + getWarp(player, WarpType.TEMPORARY);
    }

    public static WarpSnapshot getWarpSnapshot(ServerPlayer player) {
        return new WarpSnapshot(
                getWarp(player, WarpType.PERMANENT),
                getWarp(player, WarpType.NORMAL),
                getWarp(player, WarpType.TEMPORARY)
        );
    }

    public static int getWarpEventCounter(ServerPlayer player) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.getWarpEventCounter(player.getUUID());
    }

    public static void setWarpEventCounter(ServerPlayer player, int value) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        data.setWarpEventCounter(player.getUUID(), value);
    }

    private static PlayerKnowledgeSavedData getData(ServerLevel level) {
        ServerLevel overworld = level.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(PlayerKnowledgeSavedData::load, PlayerKnowledgeSavedData::new, DATA_ID);
    }

    public record ScanResult(boolean firstScan, int totalScans, int newAspects, int totalAspects) {
    }

    public enum WarpType {
        PERMANENT,
        NORMAL,
        TEMPORARY
    }

    public enum ResearchFlag {
        PAGE,
        RESEARCH,
        POPUP
    }

    public record WarpSnapshot(int permanent, int normal, int temporary) {
        public int total() {
            return this.permanent + this.normal + this.temporary;
        }
    }
}
