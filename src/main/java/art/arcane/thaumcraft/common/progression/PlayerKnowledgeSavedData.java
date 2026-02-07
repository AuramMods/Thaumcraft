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
    // TODO(port): persist research entries with stage/flags, category-based observation/theory totals, and warp data (permanent/normal/temporary).
    // TODO(port): keep backward compatibility migration paths for current simplified scan/salis fields when introducing the richer schema.

    private static final String PLAYERS_TAG = "players";
    private static final String UUID_TAG = "uuid";
    private static final String SALIS_UNLOCKED_TAG = "salis_unlocked";
    private static final String SCAN_COUNT_TAG = "scan_count";
    private static final String SCANNED_BLOCKS_TAG = "scanned_blocks";
    private static final String SCANNED_ITEMS_TAG = "scanned_items";
    private static final String SCANNED_ENTITIES_TAG = "scanned_entities";
    private static final String DISCOVERED_ASPECTS_TAG = "discovered_aspects";

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

    private static final class PlayerKnowledgeEntry {
        private boolean salisUnlocked;
        private int scanCount;
        private final Set<String> scannedBlocks = new HashSet<>();
        private final Set<String> scannedItems = new HashSet<>();
        private final Set<String> scannedEntities = new HashSet<>();
        private final Set<String> discoveredAspects = new HashSet<>();

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

            return entry;
        }
    }
}
