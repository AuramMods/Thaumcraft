package art.arcane.thaumcraft.common.block;

import art.arcane.thaumcraft.common.block.entity.SpaBlockEntity;
import art.arcane.thaumcraft.common.item.BathSaltsItem;
import art.arcane.thaumcraft.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SpaBlock extends BaseEntityBlock {
    // TODO(port): Add dedicated spa UI/tank semantics (legacy container and mix toggle path).
    // TODO(port): Expand interaction parity for additional fluid containers once proper fluid systems are ported.

    public SpaBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpaBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide
                ? null
                : createTickerHelper(type, ModBlockEntities.SPA.get(), (tickLevel, tickPos, tickState, spa) -> spa.serverTick());
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
        if (!(blockEntity instanceof SpaBlockEntity spa)) {
            return InteractionResult.PASS;
        }

        ItemStack held = player.getItemInHand(hand);
        if (held.is(Items.WATER_BUCKET)) {
            int added = spa.addWater(1000);
            if (added > 0 && !player.getAbilities().instabuild) {
                player.setItemInHand(hand, new ItemStack(Items.BUCKET));
            }
            showStatus(player, spa);
            return InteractionResult.CONSUME;
        }

        if (!held.isEmpty() && held.getItem() instanceof BathSaltsItem) {
            int added = spa.addBathSalts(1);
            if (added > 0 && !player.getAbilities().instabuild) {
                held.shrink(1);
            }
            showStatus(player, spa);
            return InteractionResult.CONSUME;
        }

        if (held.isEmpty()) {
            showStatus(player, spa);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    private static void showStatus(Player player, SpaBlockEntity spa) {
        player.displayClientMessage(Component.literal("Spa: water=" + spa.getWaterAmount()
                + "/" + SpaBlockEntity.MAX_WATER
                + ", bath_salts=" + spa.getBathSaltsCount()
                + ", ready=" + spa.hasIngredients()), true);
    }
}
