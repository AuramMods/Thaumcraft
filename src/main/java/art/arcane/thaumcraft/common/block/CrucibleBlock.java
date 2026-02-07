package art.arcane.thaumcraft.common.block;

import art.arcane.thaumcraft.common.aspect.AspectType;
import art.arcane.thaumcraft.common.block.entity.CrucibleBlockEntity;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Map;

public class CrucibleBlock extends StationBlock {
    // TODO(port): Align interaction feedback/effects with legacy crucible block:
    // TODO(port): add full particle/sound event parity for smelting, spills, and boil state transitions.
    // TODO(port): preserve thrower/context metadata for recipe ownership/research attribution where needed by progression.
    // TODO(port): Restore quartz sliver catalyst parity for crucible workflows:
    // TODO(port): legacy "quartz sliver" is the quartz variant of `thaumcraft:nuggets` (ore key `nuggetQuartz`), not raw `minecraft:quartz`.
    // TODO(port): once nugget subtype/tag parity exists, migrate crucible catalyst checks + user-facing feedback to quartz sliver semantics.

    public static final BooleanProperty WATER = BooleanProperty.create("water");
    public static final BooleanProperty HEATED = BooleanProperty.create("heated");

    private static final String PHIAL_ITEM_ID = "phial";
    private static final String PHIAL_ASPECT_TAG = "Aspect";
    private static final String PHIAL_AMOUNT_TAG = "Amount";
    private static final int PHIAL_TRANSFER_AMOUNT = 8;

    public CrucibleBlock(BlockBehaviour.Properties properties, BlockEntityFactory factory) {
        super(properties, factory);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WATER, false)
                .setValue(HEATED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATER, HEATED);
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
        if (!(blockEntity instanceof CrucibleBlockEntity crucible)) {
            return InteractionResult.PASS;
        }

        ItemStack held = player.getItemInHand(hand);

        if (held.isEmpty() && player.isShiftKeyDown()) {
            if (crucible.dumpContentsToFlux()) {
                player.displayClientMessage(Component.literal("Crucible contents dumped. Flux rises."), true);
            } else {
                player.displayClientMessage(Component.literal("Crucible is already empty."), true);
            }
            return InteractionResult.CONSUME;
        }

        if (tryFillWithWaterBucket(player, hand, held, crucible)) {
            return InteractionResult.CONSUME;
        }
        if (tryInsertPhial(level, player, hand, held, crucible)) {
            return InteractionResult.CONSUME;
        }
        if (tryExtractToPhial(level, player, hand, held, crucible)) {
            return InteractionResult.CONSUME;
        }
        if (tryOfferHeldItem(level, player, hand, held, crucible)) {
            return InteractionResult.CONSUME;
        }

        if (held.isEmpty()) {
            player.displayClientMessage(Component.literal(describeContents(crucible)), true);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    private static boolean tryFillWithWaterBucket(Player player, InteractionHand hand, ItemStack held, CrucibleBlockEntity crucible) {
        if (!held.is(Items.WATER_BUCKET)) {
            return false;
        }

        if (!crucible.fillFromWaterBucket()) {
            player.displayClientMessage(Component.literal("Crucible is already full of water."), true);
            return true;
        }

        if (!player.getAbilities().instabuild) {
            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
        }
        return true;
    }

    private static boolean tryInsertPhial(Level level, Player player, InteractionHand hand, ItemStack held, CrucibleBlockEntity crucible) {
        Item phialItem = getPhialItem();
        if (phialItem == null || !held.is(phialItem) || !held.hasTag()) {
            return false;
        }

        String aspectTag = held.getTag().getString(PHIAL_ASPECT_TAG);
        int amount = held.getTag().getInt(PHIAL_AMOUNT_TAG);
        AspectType aspect = AspectType.byTag(aspectTag);
        if (aspect == null || amount <= 0) {
            return false;
        }

        int inserted = crucible.addEssentia(aspect, amount);
        if (inserted <= 0) {
            player.displayClientMessage(Component.literal("Crucible essentia is full."), true);
            return true;
        }

        if (!player.getAbilities().instabuild) {
            int remainder = amount - inserted;
            if (remainder <= 0) {
                held.shrink(1);
                giveOrDrop(level, player, new ItemStack(Items.GLASS_BOTTLE));
            } else {
                held.getTag().putInt(PHIAL_AMOUNT_TAG, remainder);
                player.setItemInHand(hand, held);
            }
        }

        crucible.syncToClient();
        return true;
    }

    private static boolean tryExtractToPhial(Level level, Player player, InteractionHand hand, ItemStack held, CrucibleBlockEntity crucible) {
        if (!held.is(Items.GLASS_BOTTLE)) {
            return false;
        }

        Item phialItem = getPhialItem();
        if (phialItem == null) {
            return false;
        }

        Map.Entry<AspectType, Integer> dominant = crucible.getDominantAspect();
        if (dominant == null) {
            player.displayClientMessage(Component.literal("Crucible has no essentia."), true);
            return true;
        }

        int drained = crucible.removeEssentia(dominant.getKey(), Math.min(PHIAL_TRANSFER_AMOUNT, dominant.getValue()));
        if (drained <= 0) {
            return false;
        }

        ItemStack filledPhial = new ItemStack(phialItem);
        filledPhial.getOrCreateTag().putString(PHIAL_ASPECT_TAG, dominant.getKey().getTag());
        filledPhial.getOrCreateTag().putInt(PHIAL_AMOUNT_TAG, drained);

        if (!player.getAbilities().instabuild) {
            held.shrink(1);
        }

        giveOrDrop(level, player, filledPhial);
        crucible.syncToClient();
        return true;
    }

    private static boolean tryOfferHeldItem(Level level, Player player, InteractionHand hand, ItemStack held, CrucibleBlockEntity crucible) {
        if (held.isEmpty() || held.is(Items.GLASS_BOTTLE) || held.is(Items.WATER_BUCKET) || held.is(Items.BUCKET)) {
            return false;
        }

        Item phialItem = getPhialItem();
        if (phialItem != null && held.is(phialItem)) {
            return false;
        }

        ItemStack single = held.copy();
        single.setCount(1);
        boolean processed = crucible.processOfferedItem(single);
        if (!processed) {
            player.displayClientMessage(Component.literal("Crucible must be boiling with water to process items."), true);
            return true;
        }

        if (!player.getAbilities().instabuild) {
            held.shrink(1);
            ItemStack remainder = single.getCraftingRemainingItem();
            if (!remainder.isEmpty()) {
                giveOrDrop(level, player, remainder);
            }
            player.setItemInHand(hand, held);
        }
        return true;
    }

    private static Item getPhialItem() {
        var phialObject = ModItems.ITEMS_BY_ID.get(PHIAL_ITEM_ID);
        return phialObject == null ? null : phialObject.get();
    }

    private static void giveOrDrop(Level level, Player player, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    private static String describeContents(CrucibleBlockEntity crucible) {
        Map.Entry<AspectType, Integer> dominant = crucible.getDominantAspect();
        String dominantText = dominant == null ? "none" : dominant.getKey().getTag() + "=" + dominant.getValue();
        return "Crucible: heat=" + crucible.getHeat()
                + ", water=" + crucible.getWaterAmount()
                + ", essentia=" + crucible.getEssentiaUnits()
                + ", boiling=" + crucible.isBoiling()
                + ", dominant=" + dominantText;
    }
}
