package art.arcane.thaumcraft.common.progression;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.aspect.AspectList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class PlayerKnowledgeManager {
    // TODO(port): Expand baseline research key/stage/flag/knowledge support to legacy IPlayerKnowledge parity (full research entry flow and auto-unlock rules).
    // TODO(port): reintroduce player sync packet flow once research UI/thaumonomicon and server-authoritative progression are ported.
    // TODO(port): expose research completion helpers that also trigger popup/page flags once thaumonomicon UI exists.

    private static final String DATA_ID = Thaumcraft.MODID + "_player_knowledge";
    public static final String RESEARCH_BATH_SALTS_HINT = "!BATHSALTS";
    public static final String RESEARCH_ELDRITCH_MINOR = "ELDRITCHMINOR";
    public static final String RESEARCH_ELDRITCH_MAJOR = "ELDRITCHMAJOR";
    public static final String RESEARCH_CATEGORY_BASICS = "BASICS";
    public static final String RESEARCH_CATEGORY_THAUMATURGY = "THAUMATURGY";
    public static final String RESEARCH_CATEGORY_ALCHEMY = "ALCHEMY";
    public static final String RESEARCH_CATEGORY_ARTIFICE = "ARTIFICE";
    public static final String RESEARCH_CATEGORY_GOLEMANCY = "GOLEMANCY";
    public static final String RESEARCH_CATEGORY_ELDRITCH = "ELDRITCH";

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

    public static int getResearchKnowledgeRaw(ServerPlayer player, ResearchKnowledgeType type, String categoryKey) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.getResearchKnowledgeRaw(player.getUUID(), type, categoryKey);
    }

    public static int getResearchKnowledgePoints(ServerPlayer player, ResearchKnowledgeType type, String categoryKey) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.getResearchKnowledgePoints(player.getUUID(), type, categoryKey);
    }

    public static boolean addResearchKnowledge(ServerPlayer player, ResearchKnowledgeType type, String categoryKey, int rawAmountDelta) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.addResearchKnowledge(player.getUUID(), type, categoryKey, rawAmountDelta);
    }

    public static boolean setResearchKnowledgeRaw(ServerPlayer player, ResearchKnowledgeType type, String categoryKey, int rawAmount) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.setResearchKnowledgeRaw(player.getUUID(), type, categoryKey, rawAmount);
    }

    public static Set<ResearchKnowledgeEntry> getResearchKnowledgeEntries(ServerPlayer player) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());
        return data.getResearchKnowledgeEntries(player.getUUID());
    }

    public static ScanResult recordBlockScan(ServerPlayer player, ResourceLocation blockId, AspectList aspects) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());

        boolean firstScan = data.markBlockScanned(player.getUUID(), blockId);
        int newAspects = data.discoverAspects(player.getUUID(), aspects);
        ScanKnowledgeGain knowledgeGain = firstScan ? applyScanKnowledgeGain(data, player, aspects, newAspects) : ScanKnowledgeGain.empty();

        return new ScanResult(
                firstScan,
                data.getScanCount(player.getUUID()),
                newAspects,
                data.getDiscoveredAspectCount(player.getUUID()),
                knowledgeGain.observationRawByCategory(),
                knowledgeGain.epiphanyRaw()
        );
    }

    public static ScanResult recordItemScan(ServerPlayer player, ResourceLocation itemId, AspectList aspects) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());

        boolean firstScan = data.markItemScanned(player.getUUID(), itemId);
        int newAspects = data.discoverAspects(player.getUUID(), aspects);
        ScanKnowledgeGain knowledgeGain = firstScan ? applyScanKnowledgeGain(data, player, aspects, newAspects) : ScanKnowledgeGain.empty();

        return new ScanResult(
                firstScan,
                data.getScanCount(player.getUUID()),
                newAspects,
                data.getDiscoveredAspectCount(player.getUUID()),
                knowledgeGain.observationRawByCategory(),
                knowledgeGain.epiphanyRaw()
        );
    }

    public static ScanResult recordEntityScan(ServerPlayer player, ResourceLocation entityId, AspectList aspects) {
        PlayerKnowledgeSavedData data = getData(player.serverLevel());

        boolean firstScan = data.markEntityScanned(player.getUUID(), entityId);
        int newAspects = data.discoverAspects(player.getUUID(), aspects);
        ScanKnowledgeGain knowledgeGain = firstScan ? applyScanKnowledgeGain(data, player, aspects, newAspects) : ScanKnowledgeGain.empty();

        return new ScanResult(
                firstScan,
                data.getScanCount(player.getUUID()),
                newAspects,
                data.getDiscoveredAspectCount(player.getUUID()),
                knowledgeGain.observationRawByCategory(),
                knowledgeGain.epiphanyRaw()
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

    private static ScanKnowledgeGain applyScanKnowledgeGain(
            PlayerKnowledgeSavedData data,
            ServerPlayer player,
            AspectList aspects,
            int newAspects
    ) {
        Map<String, Integer> observationRawByCategory = computeObservationKnowledgeGains(aspects);
        for (Map.Entry<String, Integer> entry : observationRawByCategory.entrySet()) {
            data.addResearchKnowledge(
                    player.getUUID(),
                    ResearchKnowledgeType.OBSERVATION,
                    entry.getKey(),
                    entry.getValue()
            );
        }

        int epiphanyRaw = newAspects > 0 ? 1 : 0;
        if (epiphanyRaw > 0) {
            data.addResearchKnowledge(
                    player.getUUID(),
                    ResearchKnowledgeType.EPIPHANY,
                    "",
                    epiphanyRaw
            );
        }

        return new ScanKnowledgeGain(Map.copyOf(observationRawByCategory), epiphanyRaw);
    }

    private static Map<String, Integer> computeObservationKnowledgeGains(AspectList aspects) {
        int totalAspects = Math.max(1, aspects == null ? 0 : aspects.totalAmount());
        double scale = Math.sqrt(totalAspects);

        LinkedHashMap<String, Integer> gains = new LinkedHashMap<>();
        gains.put(RESEARCH_CATEGORY_BASICS, scaledKnowledgeGain(scale, 1.00));
        gains.put(RESEARCH_CATEGORY_THAUMATURGY, scaledKnowledgeGain(scale, 0.85));
        gains.put(RESEARCH_CATEGORY_ALCHEMY, scaledKnowledgeGain(scale, 0.70));
        gains.put(RESEARCH_CATEGORY_ARTIFICE, scaledKnowledgeGain(scale, 0.55));
        gains.put(RESEARCH_CATEGORY_GOLEMANCY, scaledKnowledgeGain(scale, 0.45));
        gains.put(RESEARCH_CATEGORY_ELDRITCH, scaledKnowledgeGain(scale, 0.35));
        return gains;
    }

    private static int scaledKnowledgeGain(double scale, double weight) {
        int gain = (int) Math.round(scale * weight);
        return Math.max(1, gain);
    }

    private record ScanKnowledgeGain(Map<String, Integer> observationRawByCategory, int epiphanyRaw) {
        private static ScanKnowledgeGain empty() {
            return new ScanKnowledgeGain(Map.of(), 0);
        }
    }

    public record ScanResult(
            boolean firstScan,
            int totalScans,
            int newAspects,
            int totalAspects,
            Map<String, Integer> observationRawByCategory,
            int epiphanyRaw
    ) {
        public int observationRawTotal() {
            int total = 0;
            for (int value : this.observationRawByCategory.values()) {
                total += value;
            }
            return total;
        }
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

    public enum ResearchKnowledgeType {
        THEORY(32, true, "T"),
        OBSERVATION(16, true, "O"),
        EPIPHANY(256, false, "E");

        private final int progression;
        private final boolean categoryScoped;
        private final String abbreviation;

        ResearchKnowledgeType(int progression, boolean categoryScoped, String abbreviation) {
            this.progression = progression;
            this.categoryScoped = categoryScoped;
            this.abbreviation = abbreviation;
        }

        public int getProgression() {
            return this.progression;
        }

        public boolean isCategoryScoped() {
            return this.categoryScoped;
        }

        public String getAbbreviation() {
            return this.abbreviation;
        }

        public static ResearchKnowledgeType parse(String value) {
            if (value == null || value.isBlank()) {
                return null;
            }

            String normalized = value.trim().toUpperCase(Locale.ROOT);
            for (ResearchKnowledgeType type : values()) {
                if (type.name().equals(normalized) || type.abbreviation.equals(normalized)) {
                    return type;
                }
            }
            return null;
        }
    }

    public record ResearchKnowledgeEntry(ResearchKnowledgeType type, String categoryKey, int rawAmount) {
        public int points() {
            if (this.rawAmount <= 0) {
                return 0;
            }
            return this.rawAmount / this.type.getProgression();
        }
    }

    public record WarpSnapshot(int permanent, int normal, int temporary) {
        public int total() {
            return this.permanent + this.normal + this.temporary;
        }
    }
}
