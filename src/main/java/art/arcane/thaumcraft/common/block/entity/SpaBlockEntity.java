package art.arcane.thaumcraft.common.block.entity;

import art.arcane.thaumcraft.common.registry.ModBlockEntities;
import art.arcane.thaumcraft.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SpaBlockEntity extends net.minecraft.world.level.block.entity.BlockEntity implements TickingStationBlockEntity {
    // TODO(port): Replace this baseline internal counters model with full legacy spa tank + inventory/menu parity.
    // TODO(port): Add configurable non-mix mode and fluid-source selection parity once proper fluids and spa UI exist.

    public static final int MAX_WATER = 5000;
    public static final int MAX_BATH_SALTS = 64;
    public static final int WATER_COST_PER_OPERATION = 1000;
    public static final int OPERATION_INTERVAL_TICKS = 40;

    private int waterAmount;
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

        if (tryPlacePurifyingFluid(serverLevel)) {
            this.bathSaltsCount = Math.max(0, this.bathSaltsCount - 1);
            this.waterAmount = Math.max(0, this.waterAmount - WATER_COST_PER_OPERATION);
            setChanged();
        }
    }

    public int addWater(int amount) {
        if (amount <= 0) {
            return 0;
        }
        int added = Math.min(amount, MAX_WATER - this.waterAmount);
        if (added > 0) {
            this.waterAmount += added;
            setChanged();
        }
        return added;
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
        return this.waterAmount;
    }

    public int getBathSaltsCount() {
        return this.bathSaltsCount;
    }

    public boolean hasIngredients() {
        return this.bathSaltsCount > 0 && this.waterAmount >= WATER_COST_PER_OPERATION;
    }

    private boolean tryPlacePurifyingFluid(ServerLevel level) {
        Block purifyingFluid = ModBlocks.get("purifying_fluid").get();
        BlockPos above = this.worldPosition.above();
        BlockState aboveState = level.getBlockState(above);

        if (aboveState.is(purifyingFluid)) {
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    BlockPos candidate = this.worldPosition.offset(dx, 1, dz);
                    if (canPlaceAt(level, candidate, purifyingFluid, true)) {
                        level.setBlockAndUpdate(candidate, purifyingFluid.defaultBlockState());
                        return true;
                    }
                }
            }
        }

        if (canPlaceAt(level, above, purifyingFluid, false)) {
            level.setBlockAndUpdate(above, purifyingFluid.defaultBlockState());
            return true;
        }

        return false;
    }

    private static boolean canPlaceAt(ServerLevel level, BlockPos pos, Block purifyingFluid, boolean requireAdjacentPurifyingFluid) {
        BlockState targetState = level.getBlockState(pos);
        boolean sourceWater = targetState.getFluidState().is(FluidTags.WATER) && targetState.getFluidState().isSource();
        if (targetState.is(purifyingFluid) || (!targetState.canBeReplaced() && !sourceWater)) {
            return false;
        }

        BlockState below = level.getBlockState(pos.below());
        if (!below.isFaceSturdy(level, pos.below(), Direction.UP)) {
            return false;
        }

        if (!requireAdjacentPurifyingFluid) {
            return true;
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (level.getBlockState(pos.relative(direction)).is(purifyingFluid)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("spa_water", this.waterAmount);
        tag.putInt("spa_bath_salts", this.bathSaltsCount);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.waterAmount = Math.max(0, Math.min(MAX_WATER, tag.getInt("spa_water")));
        this.bathSaltsCount = Math.max(0, Math.min(MAX_BATH_SALTS, tag.getInt("spa_bath_salts")));
    }
}
