package art.arcane.thaumcraft.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.function.Supplier;

public class ThaumcraftFluidBucketItem extends Item {
    // TODO(port): Replace this custom block-placement bucket path with true FluidType/ForgeFlowingFluid bucket behavior.
    // TODO(port): preserve source-level semantics and flowing behavior once `purifying_fluid` / `liquid_death` are real fluids.

    private final Supplier<Block> placedBlock;

    public ThaumcraftFluidBucketItem(Supplier<Block> placedBlock) {
        super(new Item.Properties().stacksTo(1));
        this.placedBlock = placedBlock;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, net.minecraft.world.entity.player.Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        HitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (!(hitResult instanceof BlockHitResult blockHitResult)) {
            return InteractionResultHolder.pass(held);
        }

        BlockPos hitPos = blockHitResult.getBlockPos();
        Direction face = blockHitResult.getDirection();
        BlockState hitState = level.getBlockState(hitPos);
        BlockPos placePos = hitState.canBeReplaced(new BlockPlaceContext(player, hand, held, blockHitResult))
                ? hitPos
                : hitPos.relative(face);

        if (!level.mayInteract(player, hitPos) || !player.mayUseItemAt(placePos, face, held)) {
            return InteractionResultHolder.fail(held);
        }

        if (!placeContainedBlock(level, placePos)) {
            return InteractionResultHolder.fail(held);
        }

        level.playSound(player, placePos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
        player.awardStat(Stats.ITEM_USED.get(this));

        if (player.getAbilities().instabuild) {
            return InteractionResultHolder.sidedSuccess(held, level.isClientSide());
        }
        return InteractionResultHolder.sidedSuccess(new ItemStack(Items.BUCKET), level.isClientSide());
    }

    private boolean placeContainedBlock(Level level, BlockPos pos) {
        BlockState existing = level.getBlockState(pos);
        if (!level.isEmptyBlock(pos) && !existing.canBeReplaced()) {
            return false;
        }

        if (!level.isClientSide() && !existing.isAir() && existing.canBeReplaced()) {
            level.destroyBlock(pos, true);
        }

        return level.setBlock(pos, this.placedBlock.get().defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
    }
}
