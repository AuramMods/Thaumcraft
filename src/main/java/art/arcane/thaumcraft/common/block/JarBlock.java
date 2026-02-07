package art.arcane.thaumcraft.common.block;

import art.arcane.thaumcraft.common.aspect.AspectType;
import art.arcane.thaumcraft.common.aspect.AspectList;
import art.arcane.thaumcraft.common.block.entity.JarBlockEntity;
import art.arcane.thaumcraft.common.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;

public class JarBlock extends BaseEntityBlock {

    private static final String PHIAL_ITEM_ID = "phial";
    private static final String PHIAL_ASPECT_TAG = "Aspect";
    private static final String PHIAL_AMOUNT_TAG = "Amount";
    private static final int PHIAL_TRANSFER_AMOUNT = 8;

    private static final VoxelShape JAR_SHAPE = Shapes.or(
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 12.0D, 13.0D),
            Block.box(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D)
    );

    public JarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new JarBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return JAR_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return JAR_SHAPE;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return false;
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
        if (!(blockEntity instanceof JarBlockEntity jar)) {
            return InteractionResult.PASS;
        }

        ItemStack held = player.getItemInHand(hand);
        if (tryInsertPhial(level, player, hand, held, jar)) {
            return InteractionResult.CONSUME;
        }
        if (tryExtractToPhial(level, player, hand, held, jar)) {
            return InteractionResult.CONSUME;
        }
        if (held.isEmpty()) {
            player.displayClientMessage(Component.literal(describeContents(jar)), true);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    private static boolean tryInsertPhial(Level level, Player player, InteractionHand hand, ItemStack held, JarBlockEntity jar) {
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

        int inserted = jar.addAspect(aspect, amount);
        if (inserted <= 0) {
            player.displayClientMessage(Component.literal("Jar is full."), true);
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

        jar.syncToClient();
        return true;
    }

    private static boolean tryExtractToPhial(Level level, Player player, InteractionHand hand, ItemStack held, JarBlockEntity jar) {
        if (!held.is(Items.GLASS_BOTTLE)) {
            return false;
        }

        Item phialItem = getPhialItem();
        if (phialItem == null) {
            return false;
        }

        Map.Entry<AspectType, Integer> dominant = jar.getDominantAspect();
        if (dominant == null) {
            player.displayClientMessage(Component.literal("Jar is empty."), true);
            return true;
        }

        int drained = jar.removeAspect(dominant.getKey(), Math.min(PHIAL_TRANSFER_AMOUNT, dominant.getValue()));
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
        jar.syncToClient();
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

    private static String describeContents(JarBlockEntity jar) {
        if (jar.getStoredAmount() <= 0) {
            return "Jar: empty";
        }

        Map.Entry<AspectType, Integer> dominant = jar.getDominantAspect();
        String dominantText = dominant == null ? "none" : dominant.getKey().getTag() + "=" + dominant.getValue();
        return "Jar: " + jar.getStoredAmount() + "/" + JarBlockEntity.MAX_ESSENTIA
                + " | dominant " + dominantText
                + " | " + formatAspectSummary(jar.getAspects());
    }

    private static String formatAspectSummary(AspectList list) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Map.Entry<AspectType, Integer> entry : list.asMap().entrySet()) {
            if (!first) {
                builder.append(", ");
            }
            first = false;
            builder.append(entry.getKey().getTag().toLowerCase(Locale.ROOT)).append("=").append(entry.getValue());
        }

        return builder.toString();
    }
}
