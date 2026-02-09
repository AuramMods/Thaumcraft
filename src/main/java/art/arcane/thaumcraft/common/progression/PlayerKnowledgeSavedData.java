package art.arcane.thaumcraft.common.progression;

import art.arcane.thaumcraft.common.aspect.AspectList;
import art.arcane.thaumcraft.common.aspect.AspectType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.level.saveddata.SavedData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class PlayerKnowledgeSavedData extends SavedData {
    // TODO(port): Expand this baseline data model to legacy IPlayerKnowledge parity:
    // TODO(port): expand persisted research stage/flag/category-knowledge scaffolding into full research graph completion rules.
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
    private static final String RESEARCH_STAGES_TAG = "research_stages";
    private static final String RESEARCH_FLAGS_TAG = "research_flags";
    private static final String RESEARCH_KNOWLEDGE_TAG = "research_knowledge";
    private static final String RESEARCH_STAGE_VALUE_TAG = "stage";
    private static final String RESEARCH_FLAG_VALUES_TAG = "flags";
    private static final String RESEARCH_KNOWLEDGE_TYPE_TAG = "type";
    private static final String RESEARCH_KNOWLEDGE_CATEGORY_TAG = "category";
    private static final String RESEARCH_KNOWLEDGE_AMOUNT_TAG = "amount";
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

    public boolean removeResearch(UUID playerId, String researchKey) {
        String normalized = normalizeResearchKey(researchKey);
        if (normalized == null) {
            return false;
        }

        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        boolean removed = entry.researchKeys.remove(normalized);
        if (!removed) {
            return false;
        }

        entry.researchStages.remove(normalized);
        entry.researchFlags.remove(normalized);
        setDirty();
        return true;
    }

    public Set<String> getResearchKeys(UUID playerId) {
        return Set.copyOf(getOrCreate(playerId).researchKeys);
    }

    public int getResearchStage(UUID playerId, String researchKey) {
        String normalized = normalizeResearchKey(researchKey);
        if (normalized == null) {
            return -1;
        }

        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        if (!entry.researchKeys.contains(normalized)) {
            return -1;
        }

        return entry.researchStages.getOrDefault(normalized, 0);
    }

    public boolean setResearchStage(UUID playerId, String researchKey, int stage) {
        String normalized = normalizeResearchKey(researchKey);
        if (normalized == null || stage < 0) {
            return false;
        }

        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        if (!entry.researchKeys.contains(normalized)) {
            return false;
        }

        Integer previous = entry.researchStages.get(normalized);
        if (stage == 0) {
            if (previous == null) {
                return false;
            }
            entry.researchStages.remove(normalized);
            setDirty();
            return true;
        }

        if (previous != null && previous == stage) {
            return false;
        }

        entry.researchStages.put(normalized, stage);
        setDirty();
        return true;
    }

    public boolean hasResearchFlag(UUID playerId, String researchKey, PlayerKnowledgeManager.ResearchFlag flag) {
        if (flag == null) {
            return false;
        }

        String normalized = normalizeResearchKey(researchKey);
        if (normalized == null) {
            return false;
        }

        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        Set<PlayerKnowledgeManager.ResearchFlag> flags = entry.researchFlags.get(normalized);
        return flags != null && flags.contains(flag);
    }

    public boolean setResearchFlag(UUID playerId, String researchKey, PlayerKnowledgeManager.ResearchFlag flag) {
        if (flag == null) {
            return false;
        }

        String normalized = normalizeResearchKey(researchKey);
        if (normalized == null) {
            return false;
        }

        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        if (!entry.researchKeys.contains(normalized)) {
            return false;
        }

        Set<PlayerKnowledgeManager.ResearchFlag> flags = entry.researchFlags.computeIfAbsent(normalized, key -> new HashSet<>());
        boolean added = flags.add(flag);
        if (added) {
            setDirty();
        }
        return added;
    }

    public boolean clearResearchFlag(UUID playerId, String researchKey, PlayerKnowledgeManager.ResearchFlag flag) {
        if (flag == null) {
            return false;
        }

        String normalized = normalizeResearchKey(researchKey);
        if (normalized == null) {
            return false;
        }

        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        Set<PlayerKnowledgeManager.ResearchFlag> flags = entry.researchFlags.get(normalized);
        if (flags == null || !flags.remove(flag)) {
            return false;
        }

        if (flags.isEmpty()) {
            entry.researchFlags.remove(normalized);
        }
        setDirty();
        return true;
    }

    public Set<PlayerKnowledgeManager.ResearchFlag> getResearchFlags(UUID playerId, String researchKey) {
        String normalized = normalizeResearchKey(researchKey);
        if (normalized == null) {
            return Set.of();
        }

        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        Set<PlayerKnowledgeManager.ResearchFlag> flags = entry.researchFlags.get(normalized);
        if (flags == null || flags.isEmpty()) {
            return Set.of();
        }

        return Set.copyOf(flags);
    }

    public int getResearchKnowledgeRaw(UUID playerId, PlayerKnowledgeManager.ResearchKnowledgeType type, String categoryKey) {
        if (type == null) {
            return 0;
        }

        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        String storageKey = createResearchKnowledgeStorageKey(type, categoryKey);
        return entry.researchKnowledgeRaw.getOrDefault(storageKey, 0);
    }

    public int getResearchKnowledgePoints(UUID playerId, PlayerKnowledgeManager.ResearchKnowledgeType type, String categoryKey) {
        if (type == null) {
            return 0;
        }

        int raw = getResearchKnowledgeRaw(playerId, type, categoryKey);
        if (raw <= 0) {
            return 0;
        }

        return raw / type.getProgression();
    }

    public boolean addResearchKnowledge(UUID playerId, PlayerKnowledgeManager.ResearchKnowledgeType type, String categoryKey, int rawAmountDelta) {
        if (type == null) {
            return false;
        }

        if (rawAmountDelta == 0) {
            return true;
        }

        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        String storageKey = createResearchKnowledgeStorageKey(type, categoryKey);
        int current = entry.researchKnowledgeRaw.getOrDefault(storageKey, 0);
        long updatedLong = (long) current + rawAmountDelta;
        if (updatedLong < 0) {
            return false;
        }

        int updated = (int) Math.min(Integer.MAX_VALUE, updatedLong);
        if (updated == current) {
            return true;
        }

        if (updated <= 0) {
            entry.researchKnowledgeRaw.remove(storageKey);
        } else {
            entry.researchKnowledgeRaw.put(storageKey, updated);
        }

        setDirty();
        return true;
    }

    public boolean setResearchKnowledgeRaw(UUID playerId, PlayerKnowledgeManager.ResearchKnowledgeType type, String categoryKey, int rawAmount) {
        if (type == null || rawAmount < 0) {
            return false;
        }

        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        String storageKey = createResearchKnowledgeStorageKey(type, categoryKey);
        int current = entry.researchKnowledgeRaw.getOrDefault(storageKey, 0);
        if (current == rawAmount) {
            return false;
        }

        if (rawAmount <= 0) {
            entry.researchKnowledgeRaw.remove(storageKey);
        } else {
            entry.researchKnowledgeRaw.put(storageKey, rawAmount);
        }
        setDirty();
        return true;
    }

    public Set<PlayerKnowledgeManager.ResearchKnowledgeEntry> getResearchKnowledgeEntries(UUID playerId) {
        PlayerKnowledgeEntry entry = getOrCreate(playerId);
        if (entry.researchKnowledgeRaw.isEmpty()) {
            return Set.of();
        }

        Set<PlayerKnowledgeManager.ResearchKnowledgeEntry> result = new HashSet<>();
        for (Map.Entry<String, Integer> rawEntry : entry.researchKnowledgeRaw.entrySet()) {
            ResearchKnowledgeKey key = parseResearchKnowledgeStorageKey(rawEntry.getKey());
            if (key == null) {
                continue;
            }
            int rawAmount = rawEntry.getValue() == null ? 0 : rawEntry.getValue();
            if (rawAmount <= 0) {
                continue;
            }
            result.add(new PlayerKnowledgeManager.ResearchKnowledgeEntry(key.type(), key.categoryKey(), rawAmount));
        }

        if (result.isEmpty()) {
            return Set.of();
        }
        return Set.copyOf(result);
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

    private static String normalizeResearchCategoryKey(String categoryKey) {
        if (categoryKey == null) {
            return "";
        }

        String normalized = categoryKey.trim().toUpperCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            return "";
        }
        return normalized;
    }

    private static String createResearchKnowledgeStorageKey(PlayerKnowledgeManager.ResearchKnowledgeType type, String categoryKey) {
        String normalizedCategory = type.isCategoryScoped() ? normalizeResearchCategoryKey(categoryKey) : "";
        return type.getAbbreviation() + "|" + normalizedCategory;
    }

    private static ResearchKnowledgeKey parseResearchKnowledgeStorageKey(String storageKey) {
        if (storageKey == null || storageKey.isBlank()) {
            return null;
        }

        String[] split = storageKey.split("\\|", 2);
        if (split.length == 0) {
            return null;
        }

        PlayerKnowledgeManager.ResearchKnowledgeType type = PlayerKnowledgeManager.ResearchKnowledgeType.parse(split[0]);
        if (type == null) {
            return null;
        }

        String categoryKey = "";
        if (split.length > 1 && type.isCategoryScoped()) {
            categoryKey = normalizeResearchCategoryKey(split[1]);
        }

        return new ResearchKnowledgeKey(type, categoryKey);
    }

    private static PlayerKnowledgeManager.ResearchFlag parseResearchFlag(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return PlayerKnowledgeManager.ResearchFlag.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private record ResearchKnowledgeKey(PlayerKnowledgeManager.ResearchKnowledgeType type, String categoryKey) {
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
        private final Map<String, Integer> researchStages = new HashMap<>();
        private final Map<String, Set<PlayerKnowledgeManager.ResearchFlag>> researchFlags = new HashMap<>();
        private final Map<String, Integer> researchKnowledgeRaw = new HashMap<>();

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

            ListTag researchStagesTag = new ListTag();
            for (Map.Entry<String, Integer> stageEntry : this.researchStages.entrySet()) {
                if (stageEntry.getValue() == null || stageEntry.getValue() <= 0) {
                    continue;
                }
                CompoundTag single = new CompoundTag();
                single.putString("id", stageEntry.getKey());
                single.putInt(RESEARCH_STAGE_VALUE_TAG, stageEntry.getValue());
                researchStagesTag.add(single);
            }
            tag.put(RESEARCH_STAGES_TAG, researchStagesTag);

            ListTag researchFlagsTag = new ListTag();
            for (Map.Entry<String, Set<PlayerKnowledgeManager.ResearchFlag>> flagsEntry : this.researchFlags.entrySet()) {
                Set<PlayerKnowledgeManager.ResearchFlag> flags = flagsEntry.getValue();
                if (flags == null || flags.isEmpty()) {
                    continue;
                }
                CompoundTag single = new CompoundTag();
                single.putString("id", flagsEntry.getKey());

                ListTag values = new ListTag();
                for (PlayerKnowledgeManager.ResearchFlag flag : flags) {
                    values.add(StringTag.valueOf(flag.name()));
                }

                single.put(RESEARCH_FLAG_VALUES_TAG, values);
                researchFlagsTag.add(single);
            }
            tag.put(RESEARCH_FLAGS_TAG, researchFlagsTag);

            ListTag researchKnowledgeTag = new ListTag();
            for (Map.Entry<String, Integer> knowledgeEntry : this.researchKnowledgeRaw.entrySet()) {
                int amount = knowledgeEntry.getValue() == null ? 0 : knowledgeEntry.getValue();
                if (amount <= 0) {
                    continue;
                }

                ResearchKnowledgeKey key = parseResearchKnowledgeStorageKey(knowledgeEntry.getKey());
                if (key == null) {
                    continue;
                }

                CompoundTag single = new CompoundTag();
                single.putString(RESEARCH_KNOWLEDGE_TYPE_TAG, key.type().getAbbreviation());
                single.putString(RESEARCH_KNOWLEDGE_CATEGORY_TAG, key.categoryKey());
                single.putInt(RESEARCH_KNOWLEDGE_AMOUNT_TAG, amount);
                researchKnowledgeTag.add(single);
            }
            tag.put(RESEARCH_KNOWLEDGE_TAG, researchKnowledgeTag);

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

            ListTag researchStagesTag = tag.getList(RESEARCH_STAGES_TAG, Tag.TAG_COMPOUND);
            for (int i = 0; i < researchStagesTag.size(); i++) {
                CompoundTag stageTag = researchStagesTag.getCompound(i);
                String researchKey = normalizeResearchKey(stageTag.getString("id"));
                if (researchKey == null || !entry.researchKeys.contains(researchKey)) {
                    continue;
                }

                int stage = stageTag.getInt(RESEARCH_STAGE_VALUE_TAG);
                if (stage > 0) {
                    entry.researchStages.put(researchKey, stage);
                }
            }

            ListTag researchFlagsTag = tag.getList(RESEARCH_FLAGS_TAG, Tag.TAG_COMPOUND);
            for (int i = 0; i < researchFlagsTag.size(); i++) {
                CompoundTag flagsTag = researchFlagsTag.getCompound(i);
                String researchKey = normalizeResearchKey(flagsTag.getString("id"));
                if (researchKey == null || !entry.researchKeys.contains(researchKey)) {
                    continue;
                }

                ListTag values = flagsTag.getList(RESEARCH_FLAG_VALUES_TAG, Tag.TAG_STRING);
                Set<PlayerKnowledgeManager.ResearchFlag> parsed = new HashSet<>();
                for (int j = 0; j < values.size(); j++) {
                    PlayerKnowledgeManager.ResearchFlag flag = parseResearchFlag(values.getString(j));
                    if (flag != null) {
                        parsed.add(flag);
                    }
                }
                if (!parsed.isEmpty()) {
                    entry.researchFlags.put(researchKey, parsed);
                }
            }

            ListTag researchKnowledgeTag = tag.getList(RESEARCH_KNOWLEDGE_TAG, Tag.TAG_COMPOUND);
            for (int i = 0; i < researchKnowledgeTag.size(); i++) {
                CompoundTag knowledgeTag = researchKnowledgeTag.getCompound(i);
                PlayerKnowledgeManager.ResearchKnowledgeType type = PlayerKnowledgeManager.ResearchKnowledgeType.parse(
                        knowledgeTag.getString(RESEARCH_KNOWLEDGE_TYPE_TAG)
                );
                if (type == null) {
                    continue;
                }

                int amount = knowledgeTag.getInt(RESEARCH_KNOWLEDGE_AMOUNT_TAG);
                if (amount <= 0) {
                    continue;
                }

                String category = type.isCategoryScoped()
                        ? normalizeResearchCategoryKey(knowledgeTag.getString(RESEARCH_KNOWLEDGE_CATEGORY_TAG))
                        : "";

                String storageKey = createResearchKnowledgeStorageKey(type, category);
                entry.researchKnowledgeRaw.put(storageKey, amount);
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
