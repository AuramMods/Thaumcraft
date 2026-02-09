package art.arcane.thaumcraft.common.block.entity;

import art.arcane.thaumcraft.common.registry.ModBlockEntities;
import art.arcane.thaumcraft.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SpaBlockEntity extends net.minecraft.world.level.block.entity.BlockEntity implements TickingStationBlockEntity {
    // TODO(port): Replace this baseline counters model with full legacy spa tank/inventory/menu parity.
    // TODO(port): Port full container compatibility and fluid capability behavior once real fluid stacks are in place.

    public static final int MAX_FLUID = 5000;
    public static final int MAX_WATER = MAX_FLUID;
    public static final int MAX_BATH_SALTS = 64;
    public static final int FLUID_COST_PER_OPERATION = 1000;
    public static final int OPERATION_INTERVAL_TICKS = 40;

    private static final String TAG_MIX = "spa_mix";
    private static final String TAG_FLUID_AMOUNT = "spa_fluid_amount";
    private static final String TAG_FLUID_MODE = "spa_fluid_mode";
    private static final String TAG_BATH_SALTS = "spa_bath_salts";

    private int fluidAmount;
    private FluidMode fluidMode;
    private boolean mix = true;
    private int bathSaltsCount;

    public SpaBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SPA.get(), pos, state);
    }

    @Override
    public void serverTick() {
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (serverLevel.hasNeighborSignal(this.worldPosition)) {
            return;
        }
        if ((serverLevel.getGameTime() + this.worldPosition.asLong()) % OPERATION_INTERVAL_TICKS != 0) {
            return;
        }
        if (!hasIngredients()) {
            return;
        }

        Block targetBlock = resolveTargetBlock(serverLevel);
        if (targetBlock == null) {
            return;
        }

        if (tryPlaceTargetFluid(serverLevel, targetBlock)) {
            consumeIngredients();
            setChanged();
        }
    }

    public int addFluid(FluidMode mode, int amount) {
        if (mode == null || amount <= 0) {
            return 0;
        }
        if (this.fluidAmount > 0 && this.fluidMode != mode) {
            return 0;
        }

        int added = Math.min(amount, MAX_FLUID - this.fluidAmount);
        if (added > 0) {
            this.fluidAmount += added;
            this.fluidMode = mode;
            setChanged();
        }
        return added;
    }

    public int addWater(int amount) {
        return addFluid(FluidMode.WATER, amount);
    }

    public FluidMode extractBucketFluid() {
        if (this.fluidMode == null || this.fluidAmount < FLUID_COST_PER_OPERATION) {
            return null;
        }
        FluidMode extracted = this.fluidMode;
        this.fluidAmount -= FLUID_COST_PER_OPERATION;
        if (this.fluidAmount <= 0) {
            this.fluidAmount = 0;
            this.fluidMode = null;
        }
        setChanged();
        return extracted;
    }

    public void toggleMixMode() {
        this.mix = !this.mix;
        setChanged();
    }

    public int getFluidAmount() {
        return this.fluidAmount;
    }

    public String getFluidModeId() {
        return this.fluidMode == null ? "empty" : this.fluidMode.id;
    }

    public FluidMode getFluidMode() {
        return this.fluidMode;
    }

    public boolean isMixMode() {
        return this.mix;
    }

    public int addBathSalts(int amount) {
        if (amount <= 0) {
            return 0;
        }
        int added = Math.min(amount, MAX_BATH_SALTS - this.bathSaltsCount);
        if (added > 0) {
            this.bathSaltsCount += added;
            setChanged();
        }
        return added;
    }

    public int getWaterAmount() {
        return this.fluidMode == FluidMode.WATER ? this.fluidAmount : 0;
    }

    public int getBathSaltsCount() {
        return this.bathSaltsCount;
    }

    public boolean hasIngredients() {
        if (this.fluidAmount < FLUID_COST_PER_OPERATION || this.fluidMode == null) {
            return false;
        }
        if (!this.mix) {
            return true;
        }
        return this.fluidMode == FluidMode.WATER && this.bathSaltsCount > 0;
    }

    private Block resolveTargetBlock(ServerLevel level) {
        if (this.mix) {
            return ModBlocks.get("purifying_fluid").get();
        }
        if (this.fluidMode == null) {
            return null;
        }
        Block block = this.fluidMode.targetBlock();
        if (block == Blocks.WATER && level.dimensionType().ultraWarm()) {
            return null;
        }
        return block;
    }

    private void consumeIngredients() {
        this.fluidAmount = Math.max(0, this.fluidAmount - FLUID_COST_PER_OPERATION);
        if (this.fluidAmount == 0) {
            this.fluidMode = null;
        }
        if (this.mix) {
            this.bathSaltsCount = Math.max(0, this.bathSaltsCount - 1);
        }
    }

    private boolean tryPlaceTargetFluid(ServerLevel level, Block targetBlock) {
        BlockPos above = this.worldPosition.above();
        BlockState aboveState = level.getBlockState(above);

        if (aboveState.is(targetBlock)) {
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    BlockPos candidate = this.worldPosition.offset(dx, 1, dz);
                    if (canPlaceAt(level, candidate, targetBlock, true)) {
                        level.setBlockAndUpdate(candidate, targetBlock.defaultBlockState());
                        return true;
                    }
                }
            }
        }

        if (canPlaceAt(level, above, targetBlock, false)) {
            level.setBlockAndUpdate(above, targetBlock.defaultBlockState());
            return true;
        }

        return false;
    }

    private static boolean canPlaceAt(ServerLevel level, BlockPos pos, Block targetBlock, boolean requireAdjacentTarget) {
        BlockState targetState = level.getBlockState(pos);
        boolean sourceWater = targetState.getFluidState().is(FluidTags.WATER) && targetState.getFluidState().isSource();
        boolean replaceable = targetState.canBeReplaced() || (sourceWater && targetBlock != Blocks.WATER);

        if (targetState.is(targetBlock) || !replaceable) {
            return false;
        }

        BlockState below = level.getBlockState(pos.below());
        if (!below.isFaceSturdy(level, pos.below(), Direction.UP)) {
            return false;
        }

        if (!requireAdjacentTarget) {
            return true;
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (level.getBlockState(pos.relative(direction)).is(targetBlock)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean(TAG_MIX, this.mix);
        tag.putInt(TAG_FLUID_AMOUNT, this.fluidAmount);
        tag.putString(TAG_FLUID_MODE, this.fluidMode == null ? "" : this.fluidMode.id);
        tag.putInt(TAG_BATH_SALTS, this.bathSaltsCount);

        // Compatibility fallback for older snapshots that read only this key.
        tag.putInt("spa_water", this.fluidAmount);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.mix = !tag.contains(TAG_MIX) || tag.getBoolean(TAG_MIX);
        this.bathSaltsCount = Math.max(0, Math.min(MAX_BATH_SALTS, tag.getInt(TAG_BATH_SALTS)));

        int storedAmount = tag.contains(TAG_FLUID_AMOUNT) ? tag.getInt(TAG_FLUID_AMOUNT) : tag.getInt("spa_water");
        this.fluidAmount = Math.max(0, Math.min(MAX_FLUID, storedAmount));

        String modeId = tag.getString(TAG_FLUID_MODE);
        this.fluidMode = FluidMode.byId(modeId);
        if (this.fluidMode == null && this.fluidAmount > 0) {
            this.fluidMode = FluidMode.WATER;
        }
        if (this.fluidAmount <= 0) {
            this.fluidMode = null;
        }
    }

    public enum FluidMode {
        WATER("water"),
        PURIFYING_FLUID("purifying_fluid"),
        LIQUID_DEATH("liquid_death");

        private final String id;

        FluidMode(String id) {
            this.id = id;
        }

        private Block targetBlock() {
            return switch (this) {
                case WATER -> Blocks.WATER;
                case PURIFYING_FLUID -> ModBlocks.get("purifying_fluid").get();
                case LIQUID_DEATH -> ModBlocks.get("liquid_death").get();
            };
        }

        private static FluidMode byId(String id) {
            for (FluidMode mode : values()) {
                if (mode.id.equals(id)) {
                    return mode;
                }
            }
            return null;
        }
    }
}
