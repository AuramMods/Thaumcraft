package art.arcane.thaumcraft.common.world.aura;

import art.arcane.thaumcraft.Thaumcraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public final class AuraManager {
    // TODO(port): Reintroduce legacy aura simulation behavior (regen/decay, vis-flux balancing, neighboring chunk interactions, and flux event triggers).
    // TODO(port): add preserve/drain policy hooks used by legacy mechanics that conditionally protect low-vis chunks from depletion.

    public static final int AURA_CEILING = 500;
    public static final int DEFAULT_BASE = 200;

    private static final String AURA_DATA_ID = Thaumcraft.MODID + "_aura";

    private AuraManager() {
    }

    public static int getBase(Level level, BlockPos pos) {
        AuraChunkData chunkData = getChunkData(level, pos);
        return chunkData == null ? 0 : chunkData.getBase();
    }

    public static float getVis(Level level, BlockPos pos) {
        AuraChunkData chunkData = getChunkData(level, pos);
        return chunkData == null ? 0.0F : chunkData.getVis();
    }

    public static float getFlux(Level level, BlockPos pos) {
        AuraChunkData chunkData = getChunkData(level, pos);
        return chunkData == null ? 0.0F : chunkData.getFlux();
    }

    public static float getTotalAura(Level level, BlockPos pos) {
        AuraChunkData chunkData = getChunkData(level, pos);
        return chunkData == null ? 0.0F : chunkData.getVis() + chunkData.getFlux();
    }

    public static float getFluxSaturation(Level level, BlockPos pos) {
        AuraChunkData chunkData = getChunkData(level, pos);
        if (chunkData == null || chunkData.getBase() <= 0) {
            return 0.0F;
        }

        return chunkData.getFlux() / (float) chunkData.getBase();
    }

    public static void addVis(Level level, BlockPos pos, float amount) {
        if (amount <= 0.0F) {
            return;
        }

        AuraSavedData data = getAuraData(level);
        if (data == null) {
            return;
        }

        AuraChunkData chunkData = getOrCreateChunkData(data, pos.getX() >> 4, pos.getZ() >> 4);
        chunkData.addVis(amount);
        data.setDirty();
    }

    public static void addFlux(Level level, BlockPos pos, float amount) {
        if (amount <= 0.0F) {
            return;
        }

        AuraSavedData data = getAuraData(level);
        if (data == null) {
            return;
        }

        AuraChunkData chunkData = getOrCreateChunkData(data, pos.getX() >> 4, pos.getZ() >> 4);
        chunkData.addFlux(amount);
        data.setDirty();
    }

    public static float drainVis(Level level, BlockPos pos, float amount, boolean simulate) {
        if (amount <= 0.0F) {
            return 0.0F;
        }

        AuraSavedData data = getAuraData(level);
        if (data == null) {
            return 0.0F;
        }

        AuraChunkData chunkData = getOrCreateChunkData(data, pos.getX() >> 4, pos.getZ() >> 4);
        float drained = Math.min(amount, chunkData.getVis());
        if (!simulate && drained > 0.0F) {
            chunkData.addVis(-drained);
            data.setDirty();
        }

        return drained;
    }

    public static float drainFlux(Level level, BlockPos pos, float amount, boolean simulate) {
        if (amount <= 0.0F) {
            return 0.0F;
        }

        AuraSavedData data = getAuraData(level);
        if (data == null) {
            return 0.0F;
        }

        AuraChunkData chunkData = getOrCreateChunkData(data, pos.getX() >> 4, pos.getZ() >> 4);
        float drained = Math.min(amount, chunkData.getFlux());
        if (!simulate && drained > 0.0F) {
            chunkData.addFlux(-drained);
            data.setDirty();
        }

        return drained;
    }

    public static void setBase(Level level, BlockPos pos, int base) {
        AuraSavedData data = getAuraData(level);
        if (data == null) {
            return;
        }

        AuraChunkData chunkData = getOrCreateChunkData(data, pos.getX() >> 4, pos.getZ() >> 4);
        chunkData.setBase(Mth.clamp(base, 0, AURA_CEILING));
        data.setDirty();
    }

    private static AuraChunkData getChunkData(Level level, BlockPos pos) {
        AuraSavedData data = getAuraData(level);
        if (data == null) {
            return null;
        }

        return getOrCreateChunkData(data, pos.getX() >> 4, pos.getZ() >> 4);
    }

    private static AuraChunkData getOrCreateChunkData(AuraSavedData data, int chunkX, int chunkZ) {
        return data.getOrCreate(chunkX, chunkZ, DEFAULT_BASE);
    }

    private static AuraSavedData getAuraData(Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return null;
        }

        return serverLevel.getDataStorage().computeIfAbsent(AuraSavedData::load, AuraSavedData::new, AURA_DATA_ID);
    }
}
