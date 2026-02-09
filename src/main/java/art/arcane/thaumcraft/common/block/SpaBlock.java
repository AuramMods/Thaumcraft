package art.arcane.thaumcraft.common.block;

import art.arcane.thaumcraft.common.block.entity.SpaBlockEntity;
import art.arcane.thaumcraft.common.item.BathSaltsItem;
import art.arcane.thaumcraft.common.registry.ModBlockEntities;
import art.arcane.thaumcraft.common.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
    // TODO(port): Add dedicated spa UI/container parity (legacy mix toggle button + slot/tank rendering).
    // TODO(port): Replace this bucket-based bridge with real fluid-container compatibility once fluid types are ported.

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
        if (player.isShiftKeyDown() && held.isEmpty()) {
            spa.toggleMixMode();
            player.displayClientMessage(Component.literal("Spa mix mode: " + (spa.isMixMode() ? "enabled" : "disabled")), true);
            showStatus(player, spa);
            return InteractionResult.CONSUME;
        }

        Item bucketPure = getRegisteredItem("bucket_pure");
        Item bucketDeath = getRegisteredItem("bucket_death");

        if (held.is(Items.WATER_BUCKET)) {
            int added = spa.addFluid(SpaBlockEntity.FluidMode.WATER, 1000);
            if (added > 0) {
                replaceHeldWithBucket(player, hand);
            }
            showStatus(player, spa);
            return InteractionResult.CONSUME;
        }

        if (bucketPure != null && held.is(bucketPure)) {
            int added = spa.addFluid(SpaBlockEntity.FluidMode.PURIFYING_FLUID, 1000);
            if (added > 0) {
                replaceHeldWithBucket(player, hand);
            }
            showStatus(player, spa);
            return InteractionResult.CONSUME;
        }

        if (bucketDeath != null && held.is(bucketDeath)) {
            int added = spa.addFluid(SpaBlockEntity.FluidMode.LIQUID_DEATH, 1000);
            if (added > 0) {
                replaceHeldWithBucket(player, hand);
            }
            showStatus(player, spa);
            return InteractionResult.CONSUME;
        }

        if (held.is(Items.BUCKET)) {
            if (tryExtractToBucket(player, hand, spa)) {
                return InteractionResult.CONSUME;
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

    private static boolean tryExtractToBucket(Player player, InteractionHand hand, SpaBlockEntity spa) {
        SpaBlockEntity.FluidMode mode = spa.getFluidMode();
        if (mode == null || spa.getFluidAmount() < SpaBlockEntity.FLUID_COST_PER_OPERATION) {
            return false;
        }

        ItemStack filled = toBucketStack(mode);
        if (filled.isEmpty()) {
            return false;
        }

        spa.extractBucketFluid();
        if (!player.getAbilities().instabuild) {
            player.getItemInHand(hand).shrink(1);
        }
        giveOrDrop(player, filled);
        showStatus(player, spa);
        return true;
    }

    private static ItemStack toBucketStack(SpaBlockEntity.FluidMode mode) {
        return switch (mode) {
            case WATER -> new ItemStack(Items.WATER_BUCKET);
            case PURIFYING_FLUID -> toRegisteredBucket("bucket_pure");
            case LIQUID_DEATH -> toRegisteredBucket("bucket_death");
        };
    }

    private static ItemStack toRegisteredBucket(String id) {
        Item item = getRegisteredItem(id);
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }

    private static void replaceHeldWithBucket(Player player, InteractionHand hand) {
        if (!player.getAbilities().instabuild) {
            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
        }
    }

    private static void giveOrDrop(Player player, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    private static Item getRegisteredItem(String id) {
        var object = ModItems.ITEMS_BY_ID.get(id);
        return object == null ? null : object.get();
    }

    private static void showStatus(Player player, SpaBlockEntity spa) {
        player.displayClientMessage(Component.literal("Spa: mix=" + spa.isMixMode()
                + ", fluid=" + spa.getFluidModeId()
                + ", amount=" + spa.getFluidAmount()
                + "/" + SpaBlockEntity.MAX_FLUID
                + ", bath_salts=" + spa.getBathSaltsCount()
                + ", ready=" + spa.hasIngredients()), true);
    }
}
