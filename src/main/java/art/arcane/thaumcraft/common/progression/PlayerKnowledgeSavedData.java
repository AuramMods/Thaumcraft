package art.arcane.thaumcraft.common.progression;

import art.arcane.thaumcraft.common.aspect.AspectList;
import art.arcane.thaumcraft.common.aspect.AspectType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.level.saveddata.SavedData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class PlayerKnowledgeSavedData extends SavedData {
    // TODO(port): Expand this baseline data model to legacy IPlayerKnowledge parity:
    // TODO(port): persist research entries with stage/flags and category-based observation/theory totals.
    // TODO(port): warp pools are now present as baseline (permanent/normal/temporary + event counter); expand toward full legacy event parity.
    // TODO(port): keep backward compatibility migration paths for current simplified scan/salis fields when introducing the richer schema.

    private static final String PLAYERS_TAG = "players";
    private static final String UUID_TAG = "uuid";
    private static final String SALIS_UNLOCKED_TAG = "salis_unlocked";
    private static final String SCAN_COUNT_TAG = "scan_count";
    private static final String SCANNED_BLOCKS_TAG = "scanned_blocks";
    private static final String SCANNED_ITEMS_TAG = "scanned_items";
    private static final String SCANNED_ENTITIES_TAG = "scanned_entities";
    private static final String DISCOVERED_ASPECTS_TAG = "discovered_aspects";
    private static final String WARP_PERMANENT_TAG = "warp_permanent";
    private static final String WARP_NORMAL_TAG = "warp_normal";
    private static final String WARP_TEMPORARY_TAG = "warp_temporary";
    private static final String WARP_EVENT_COUNTER_TAG = "warp_event_counter";
    private static final String RESEARCH_KEYS_TAG = "research_keys";
    private static final String WARP_MILESTONES_TAG = "warp_milestones";
    private static final String LEGACY_BATH_SALTS_MILESTONE = "bath_salts_hint";
    private static final String LEGACY_ELDRITCH_MINOR_MILESTONE = "eldritch_minor";
    private static final String LEGACY_ELDRITCH_MAJOR_MILESTONE = "eldritch_major";

    private final Map<UUID, PlayerKnowledgeEntry> players = new HashMap<>();

    public static PlayerKnowledgeSavedData load(CompoundTag tag) {
        PlayerKnowledgeSavedData data = new PlayerKnowledgeSavedData();
        ListTag playersTag = tag.getList(PLAYERS_TAG, Tag.TAG_COMPOUND);

        for (int i = 0; i < playersTag.size(); i++) {
            CompoundTag playerTag = playersTag.getCompound(i);
            String uuidString = playerTag.getString(UUID_TAG);
            if (uuidString.isEmpty()) {
                continue;
            }

            UUID uuid;
            try {
                uuid = UUID.fromString(uuidString);
            } catch (IllegalArgumentException ignored) {
                continue;
            }

            PlayerKnowledgeEntry entry = PlayerKnowledgeEntry.fromTag(playerTag);
            data.players.put(uuid, entry);
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag playersTag = new ListTag();

        for (Map.Entry<UUID, PlayerKnowledgeEntry> entry : this.players.entrySet()) {
            CompoundTag playerTag = entry.getValue().toTag();
            playerTag.putString(UUID_TAG, entry.getKey().toString());
            playersTag.add(playerTag);
        }

        tag.put(PLAYERS_TAG, playersTag);
        return tag;
    }

    public void unlockSalisMundus(UUID playerId) {
        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        if (!entry.salisUnlocked) {
            entry.salisUnlocked = true;
            setDirty();
        }
    }

    public boolean hasSalisMundusUnlocked(UUID playerId) {
        return getOrCreate(playerId).salisUnlocked;
    }

    public boolean markBlockScanned(UUID playerId, ResourceLocation blockId) {
        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        return markScanned(entry, entry.scannedBlocks, blockId.toString());
    }

    public boolean markItemScanned(UUID playerId, ResourceLocation itemId) {
        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        return markScanned(entry, entry.scannedItems, itemId.toString());
    }

    public boolean markEntityScanned(UUID playerId, ResourceLocation entityId) {
        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        return markScanned(entry, entry.scannedEntities, entityId.toString());
    }

    public int discoverAspects(UUID playerId, AspectList aspects) {
        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        int discovered = 0;

        for (AspectType aspect : aspects.asMap().keySet()) {
            if (entry.discoveredAspects.add(aspect.getTag())) {
                discovered++;
            }
        }

        if (discovered > 0) {
            setDirty();
        }

        return discovered;
    }

    public int getScanCount(UUID playerId) {
        return getOrCreate(playerId).scanCount;
    }

    public int getDiscoveredAspectCount(UUID playerId) {
        return getOrCreate(playerId).discoveredAspects.size();
    }

    public int getWarp(UUID playerId, PlayerKnowledgeManager.WarpType type) {
        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        return switch (type) {
            case PERMANENT -> entry.warpPermanent;
            case NORMAL -> entry.warpNormal;
            case TEMPORARY -> entry.warpTemporary;
        };
    }

    public int addWarp(UUID playerId, PlayerKnowledgeManager.WarpType type, int delta) {
        if (delta == 0) {
            return getWarp(playerId, type);
        }

        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        int updated = switch (type) {
            case PERMANENT -> entry.warpPermanent = clampWarp(entry.warpPermanent + delta);
            case NORMAL -> entry.warpNormal = clampWarp(entry.warpNormal + delta);
            case TEMPORARY -> entry.warpTemporary = clampWarp(entry.warpTemporary + delta);
        };

        if (delta > 0) {
            entry.warpEventCounter = Math.max(0, entry.warpEventCounter + delta);
        }
        setDirty();
        return updated;
    }

    public int setWarp(UUID playerId, PlayerKnowledgeManager.WarpType type, int value) {
        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        int clamped = clampWarp(value);

        int previous = getWarp(playerId, type);
        if (previous == clamped) {
            return clamped;
        }

        switch (type) {
            case PERMANENT -> entry.warpPermanent = clamped;
            case NORMAL -> entry.warpNormal = clamped;
            case TEMPORARY -> entry.warpTemporary = clamped;
        }

        setDirty();
        return clamped;
    }

    public int getWarpEventCounter(UUID playerId) {
        return getOrCreate(playerId).warpEventCounter;
    }

    public void setWarpEventCounter(UUID playerId, int value) {
        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        int clamped = Math.max(0, value);
        if (entry.warpEventCounter != clamped) {
            entry.warpEventCounter = clamped;
            setDirty();
        }
    }

    public boolean hasResearch(UUID playerId, String researchKey) {
        String normalized = normalizeResearchKey(researchKey);
        if (normalized == null) {
            return false;
        }
        return getOrCreate(playerId).researchKeys.contains(normalized);
    }

    public boolean unlockResearch(UUID playerId, String researchKey) {
        String normalized = normalizeResearchKey(researchKey);
        if (normalized == null) {
            return false;
        }

        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        boolean added = entry.researchKeys.add(normalized);
        if (added) {
            setDirty();
        }
        return added;
    }

    public Set<String> getResearchKeys(UUID playerId) {
        return Set.copyOf(getOrCreate(playerId).researchKeys);
    }

    private PlayerKnowledgeEntry getOrCreate(UUID playerId) {
        return this.players.computeIfAbsent(playerId, id -> new PlayerKnowledgeEntry());
    }

    private boolean markScanned(PlayerKnowledgeEntry entry, Set<String> targetSet, String id) {
        boolean added = targetSet.add(id);
        if (added) {
            entry.scanCount++;
            setDirty();
        }
        return added;
    }

    private static int clampWarp(int value) {
        return Math.max(0, Math.min(100_000, value));
    }

    private static String normalizeResearchKey(String researchKey) {
        if (researchKey == null) {
            return null;
        }

        String normalized = researchKey.trim();
        if (normalized.isEmpty()) {
            return null;
        }
        return normalized;
    }

    private static final class PlayerKnowledgeEntry {
        private boolean salisUnlocked;
        private int scanCount;
        private final Set<String> scannedBlocks = new HashSet<>();
        private final Set<String> scannedItems = new HashSet<>();
        private final Set<String> scannedEntities = new HashSet<>();
        private final Set<String> discoveredAspects = new HashSet<>();
        private int warpPermanent;
        private int warpNormal;
        private int warpTemporary;
        private int warpEventCounter;
        private final Set<String> researchKeys = new HashSet<>();

        private CompoundTag toTag() {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean(SALIS_UNLOCKED_TAG, this.salisUnlocked);
            tag.putInt(SCAN_COUNT_TAG, this.scanCount);

            ListTag scannedTag = new ListTag();
            for (String blockId : this.scannedBlocks) {
                CompoundTag blockTag = new CompoundTag();
                blockTag.putString("id", blockId);
                scannedTag.add(blockTag);
            }
            tag.put(SCANNED_BLOCKS_TAG, scannedTag);

            ListTag scannedItemsTag = new ListTag();
            for (String itemId : this.scannedItems) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putString("id", itemId);
                scannedItemsTag.add(itemTag);
            }
            tag.put(SCANNED_ITEMS_TAG, scannedItemsTag);

            ListTag scannedEntitiesTag = new ListTag();
            for (String entityId : this.scannedEntities) {
                CompoundTag entityTag = new CompoundTag();
                entityTag.putString("id", entityId);
                scannedEntitiesTag.add(entityTag);
            }
            tag.put(SCANNED_ENTITIES_TAG, scannedEntitiesTag);

            ListTag aspectTag = new ListTag();
            for (String aspect : this.discoveredAspects) {
                CompoundTag single = new CompoundTag();
                single.putString("id", aspect);
                aspectTag.add(single);
            }
            tag.put(DISCOVERED_ASPECTS_TAG, aspectTag);

            tag.putInt(WARP_PERMANENT_TAG, this.warpPermanent);
            tag.putInt(WARP_NORMAL_TAG, this.warpNormal);
            tag.putInt(WARP_TEMPORARY_TAG, this.warpTemporary);
            tag.putInt(WARP_EVENT_COUNTER_TAG, this.warpEventCounter);

            ListTag researchKeysTag = new ListTag();
            for (String researchKey : this.researchKeys) {
                CompoundTag single = new CompoundTag();
                single.putString("id", researchKey);
                researchKeysTag.add(single);
            }
            tag.put(RESEARCH_KEYS_TAG, researchKeysTag);

            return tag;
        }

        private static PlayerKnowledgeEntry fromTag(CompoundTag tag) {
            PlayerKnowledgeEntry entry = new PlayerKnowledgeEntry();
            entry.salisUnlocked = tag.getBoolean(SALIS_UNLOCKED_TAG);
            entry.scanCount = tag.getInt(SCAN_COUNT_TAG);

            ListTag scannedTag = tag.getList(SCANNED_BLOCKS_TAG, Tag.TAG_COMPOUND);
            for (int i = 0; i < scannedTag.size(); i++) {
                String blockId = scannedTag.getCompound(i).getString("id");
                if (!blockId.isEmpty()) {
                    entry.scannedBlocks.add(blockId);
                }
            }

            ListTag scannedItemsTag = tag.getList(SCANNED_ITEMS_TAG, Tag.TAG_COMPOUND);
            for (int i = 0; i < scannedItemsTag.size(); i++) {
                String itemId = scannedItemsTag.getCompound(i).getString("id");
                if (!itemId.isEmpty()) {
                    entry.scannedItems.add(itemId);
                }
            }

            ListTag scannedEntitiesTag = tag.getList(SCANNED_ENTITIES_TAG, Tag.TAG_COMPOUND);
            for (int i = 0; i < scannedEntitiesTag.size(); i++) {
                String entityId = scannedEntitiesTag.getCompound(i).getString("id");
                if (!entityId.isEmpty()) {
                    entry.scannedEntities.add(entityId);
                }
            }

            ListTag aspectTag = tag.getList(DISCOVERED_ASPECTS_TAG, Tag.TAG_COMPOUND);
            for (int i = 0; i < aspectTag.size(); i++) {
                String aspect = aspectTag.getCompound(i).getString("id");
                if (!aspect.isEmpty()) {
                    entry.discoveredAspects.add(aspect);
                }
            }

            entry.warpPermanent = clampWarp(tag.getInt(WARP_PERMANENT_TAG));
            entry.warpNormal = clampWarp(tag.getInt(WARP_NORMAL_TAG));
            entry.warpTemporary = clampWarp(tag.getInt(WARP_TEMPORARY_TAG));
            entry.warpEventCounter = Math.max(0, tag.getInt(WARP_EVENT_COUNTER_TAG));

            ListTag researchKeysTag = tag.getList(RESEARCH_KEYS_TAG, Tag.TAG_COMPOUND);
            for (int i = 0; i < researchKeysTag.size(); i++) {
                String researchKey = normalizeResearchKey(researchKeysTag.getCompound(i).getString("id"));
                if (researchKey != null) {
                    entry.researchKeys.add(researchKey);
                }
            }

            ListTag warpMilestonesTag = tag.getList(WARP_MILESTONES_TAG, Tag.TAG_COMPOUND);
            for (int i = 0; i < warpMilestonesTag.size(); i++) {
                String milestoneId = warpMilestonesTag.getCompound(i).getString("id");
                if (!milestoneId.isEmpty()) {
                    migrateLegacyWarpMilestone(entry, milestoneId);
                }
            }

            return entry;
        }

        private static void migrateLegacyWarpMilestone(PlayerKnowledgeEntry entry, String milestoneId) {
            if (LEGACY_BATH_SALTS_MILESTONE.equals(milestoneId)) {
                entry.researchKeys.add(PlayerKnowledgeManager.RESEARCH_BATH_SALTS_HINT);
                return;
            }
            if (LEGACY_ELDRITCH_MINOR_MILESTONE.equals(milestoneId)) {
                entry.researchKeys.add(PlayerKnowledgeManager.RESEARCH_ELDRITCH_MINOR);
                return;
            }
            if (LEGACY_ELDRITCH_MAJOR_MILESTONE.equals(milestoneId)) {
                entry.researchKeys.add(PlayerKnowledgeManager.RESEARCH_ELDRITCH_MAJOR);
            }
        }
    }
}
