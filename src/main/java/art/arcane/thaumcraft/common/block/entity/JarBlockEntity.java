package art.arcane.thaumcraft.common.block.entity;

import art.arcane.thaumcraft.common.aspect.AspectList;
import art.arcane.thaumcraft.common.aspect.AspectType;
import art.arcane.thaumcraft.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class JarBlockEntity extends BlockEntity {

    public static final int MAX_ESSENTIA = 250;

    private AspectList aspects = new AspectList();

    public JarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.JAR.get(), pos, state);
    }

    public AspectList getAspects() {
        return this.aspects.copy();
    }

    public int getStoredAmount() {
        return this.aspects.totalAmount();
    }

    public int addAspect(AspectType aspect, int amount) {
        if (aspect == null || amount <= 0) {
            return 0;
        }

        int current = getStoredAmount();
        int accepted = Math.min(amount, Math.max(0, MAX_ESSENTIA - current));
        if (accepted <= 0) {
            return 0;
        }

        this.aspects.add(aspect, accepted);
        setChanged();
        return accepted;
    }

    public int addAspects(AspectList incoming) {
        int added = 0;
        for (Map.Entry<AspectType, Integer> entry : incoming.asMap().entrySet()) {
            added += addAspect(entry.getKey(), entry.getValue());
        }
        return added;
    }

    public int removeAspect(AspectType aspect, int amount) {
        int removed = this.aspects.remove(aspect, amount);
        if (removed > 0) {
            setChanged();
        }
        return removed;
    }

    public @Nullable Map.Entry<AspectType, Integer> getDominantAspect() {
        Map.Entry<AspectType, Integer> best = null;
        for (Map.Entry<AspectType, Integer> entry : this.aspects.asMap().entrySet()) {
            if (best == null || entry.getValue() > best.getValue()) {
                best = entry;
            }
        }
        return best;
    }

    public void syncToClient() {
        setChanged();
        if (this.level != null) {
            BlockState state = getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.aspects.saveToTag(tag, "jar_aspects");
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.aspects = AspectList.loadFromTag(tag, "jar_aspects");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        this.aspects.saveToTag(tag, "jar_aspects");
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        this.aspects = AspectList.loadFromTag(tag, "jar_aspects");
    }

    @Override
    public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
