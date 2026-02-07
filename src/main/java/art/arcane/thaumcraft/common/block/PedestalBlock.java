package art.arcane.thaumcraft.common.block;

import art.arcane.thaumcraft.common.block.entity.PedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class PedestalBlock extends BaseEntityBlock {
    // TODO(port): Match legacy BlockPedestal behavior:
    // TODO(port): support infusion-triggered event signaling (client FX + server events) and non-player insertion/removal paths.
    // TODO(port): add richer synchronization hooks for matrix-driven transforms when infusion parity work advances.

    public PedestalBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PedestalBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof PedestalBlockEntity pedestal)) {
            return InteractionResult.PASS;
        }

        ItemStack held = player.getItemInHand(hand);
        ItemStack pedestalStack = pedestal.getItem(0);

        if (pedestalStack.isEmpty()) {
            if (held.isEmpty()) {
                return InteractionResult.PASS;
            }

            ItemStack placed = held.copy();
            placed.setCount(1);
            pedestal.setItem(0, placed);
            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }
            return InteractionResult.CONSUME;
        }

        ItemStack toGive = pedestal.getItem(0).copy();
        pedestal.setItem(0, ItemStack.EMPTY);

        if (held.isEmpty()) {
            player.setItemInHand(hand, toGive);
        } else if (!player.getInventory().add(toGive)) {
            Containers.dropItemStack(level, player.getX(), player.getY() + 0.5D, player.getZ(), toGive);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof Container container) {
                Containers.dropContents(level, pos, container);
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }
}
