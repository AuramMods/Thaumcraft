package art.arcane.thaumcraft.common.world.aura;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

public final class AuraSavedData extends SavedData {
    // TODO(port): persist additional aura runtime state needed for simulation parity (dirty chunk queues, taint trigger markers, and any per-dimension control flags).
    // TODO(port): add migration/version tags for future schema updates as aura threading/simulation state is expanded.

    private static final String CHUNKS_TAG = "chunks";
    private static final String CHUNK_X_TAG = "x";
    private static final String CHUNK_Z_TAG = "z";
    private static final String BASE_TAG = "base";
    private static final String VIS_TAG = "vis";
    private static final String FLUX_TAG = "flux";

    private final Map<Long, AuraChunkData> chunks = new HashMap<>();

    public static AuraSavedData load(CompoundTag tag) {
        AuraSavedData data = new AuraSavedData();

        ListTag chunkList = tag.getList(CHUNKS_TAG, Tag.TAG_COMPOUND);
        for (int i = 0; i < chunkList.size(); i++) {
            CompoundTag chunkTag = chunkList.getCompound(i);
            int chunkX = chunkTag.getInt(CHUNK_X_TAG);
            int chunkZ = chunkTag.getInt(CHUNK_Z_TAG);
            int base = chunkTag.getInt(BASE_TAG);
            float vis = chunkTag.getFloat(VIS_TAG);
            float flux = chunkTag.getFloat(FLUX_TAG);
            data.chunks.put(ChunkPos.asLong(chunkX, chunkZ), new AuraChunkData(base, vis, flux));
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag chunkList = new ListTag();

        for (Map.Entry<Long, AuraChunkData> entry : this.chunks.entrySet()) {
            ChunkPos chunkPos = new ChunkPos(entry.getKey());
            AuraChunkData chunkData = entry.getValue();

            CompoundTag chunkTag = new CompoundTag();
            chunkTag.putInt(CHUNK_X_TAG, chunkPos.x);
            chunkTag.putInt(CHUNK_Z_TAG, chunkPos.z);
            chunkTag.putInt(BASE_TAG, chunkData.getBase());
            chunkTag.putFloat(VIS_TAG, chunkData.getVis());
            chunkTag.putFloat(FLUX_TAG, chunkData.getFlux());
            chunkList.add(chunkTag);
        }

        tag.put(CHUNKS_TAG, chunkList);
        return tag;
    }

    public AuraChunkData getOrCreate(int chunkX, int chunkZ, int defaultBase) {
        return this.chunks.computeIfAbsent(ChunkPos.asLong(chunkX, chunkZ), key -> {
            setDirty();
            return AuraChunkData.withDefaults(defaultBase);
        });
    }

    public void remove(int chunkX, int chunkZ) {
        if (this.chunks.remove(ChunkPos.asLong(chunkX, chunkZ)) != null) {
            setDirty();
        }
    }
}
