package art.arcane.thaumcraft.common.progression;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.aspect.AspectList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class PlayerKnowledgeManager {
    // TODO(port): Add legacy research/knowledge API surface (progress research, set/clear research flags, category knowledge gain, warp integration).
    // TODO(port): reintroduce player sync packet flow once research UI/thaumonomicon and server-authoritative progression are ported.
    // TODO(port): Add dedicated warp/insanity persistence and APIs (permanent/normal/temporary pools, counters, and event hooks) instead of overloading scan knowledge.
    // TODO(port): Expose warp query/mutation helpers used by sanity-checker HUD, sanity soap cleansing, purifying fluid, and warp event systems.

    private static final String DATA_ID = Thaumcraft.MODID + "_player_knowledge";

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

    public static void clearTemporaryWarp(ServerPlayer player) {
        addWarp(player, WarpType.TEMPORARY, -getWarp(player, WarpType.TEMPORARY));
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

    public record WarpSnapshot(int permanent, int normal, int temporary) {
        public int total() {
            return this.permanent + this.normal + this.temporary;
        }
    }
}
