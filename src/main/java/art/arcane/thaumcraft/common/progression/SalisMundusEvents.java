package art.arcane.thaumcraft.common.progression;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.registry.ModBlocks;
import art.arcane.thaumcraft.common.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SalisMundusEvents {
    // TODO(port): Finish legacy dust-trigger parity from ConfigRecipes/DustTriggerMultiblock:
    // TODO(port): add infusion altar ancient/eldritch material variants and placeholder-to-target conversion semantics where needed.
    // TODO(port): move trigger gating from temporary Salis-unlock checks to exact research-key requirements matching legacy progression keys.

    private static final String SALIS_MUNDUS_ID = "salis_mundus";
    private static final List<Vec3i> ALTAR_CORNER_OFFSETS = List.of(
            new Vec3i(-1, 0, -1),
            new Vec3i(-1, 0, 1),
            new Vec3i(1, 0, -1),
            new Vec3i(1, 0, 1)
    );

    private SalisMundusEvents() {
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide || event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        RegistryObject<Item> salisObject = ModItems.ITEMS_BY_ID.get(SALIS_MUNDUS_ID);
        if (salisObject == null) {
            return;
        }

        ItemStack heldStack = event.getItemStack();
        if (!heldStack.is(salisObject.get())) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState targetState = level.getBlockState(pos);
        ServerPlayer serverPlayer = event.getEntity() instanceof ServerPlayer sp ? sp : null;
        if (targetState.is(Blocks.BOOKSHELF)) {
            handleBookshelfUse(event, heldStack, serverPlayer);
            return;
        }
        if (targetState.is(Blocks.CRAFTING_TABLE)) {
            handleWorkbenchConversion(event, heldStack, targetState, serverPlayer);
            return;
        }
        if (targetState.is(Blocks.CAULDRON)) {
            handleCrucibleConversion(event, heldStack, targetState, serverPlayer);
            return;
        }
        if (targetState.is(ModBlocks.get("infusion_matrix").get())) {
            handleInfusionAltarAssembly(event, heldStack, serverPlayer);
            return;
        }
        if (targetState.is(ModBlocks.get("crucible").get()) || targetState.is(ModBlocks.get("metal").get())) {
            handleThaumatoriumAssembly(event, heldStack, serverPlayer);
            if (event.isCanceled()) {
                return;
            }
        }
        if (isInfernalFurnaceSourceBlock(targetState)) {
            handleInfernalFurnaceAssembly(event, heldStack, serverPlayer);
            if (event.isCanceled()) {
                return;
            }
        }
        if (isGolemPressSourceBlock(targetState)) {
            handleGolemPressAssembly(event, heldStack, serverPlayer);
        }
    }

    private static void handleBookshelfUse(PlayerInteractEvent.RightClickBlock event, ItemStack heldStack, ServerPlayer player) {
        if (player == null) {
            return;
        }
        if (PlayerKnowledgeManager.hasSalisMundusUnlocked(player)) {
            player.displayClientMessage(Component.literal("You have already unlocked this insight."), true);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        RegistryObject<Item> thaumonomiconObject = ModItems.ITEMS_BY_ID.get("thaumonomicon");
        if (thaumonomiconObject == null) {
            return;
        }

        consumeSalisIfNeeded(heldStack, player);
        ItemStack reward = new ItemStack(thaumonomiconObject.get());
        if (!player.getInventory().add(reward)) {
            player.drop(reward, false);
        }
        PlayerKnowledgeManager.unlockSalisMundus(player);
        player.displayClientMessage(Component.literal("Arcane insight stirs within you."), true);
        event.getLevel().levelEvent(2005, event.getPos(), 0);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    private static void handleWorkbenchConversion(PlayerInteractEvent.RightClickBlock event, ItemStack heldStack, BlockState targetState, ServerPlayer player) {
        if (player != null && !PlayerKnowledgeManager.hasSalisMundusUnlocked(player)) {
            player.displayClientMessage(Component.literal("Use Salis Mundus on a bookshelf to awaken your first insight."), true);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        BlockState replacement = ModBlocks.get("arcane_workbench").get().defaultBlockState();
        Level level = event.getLevel();
        level.setBlockAndUpdate(event.getPos(), replacement);
        if (player != null) {
            consumeSalisIfNeeded(heldStack, player);
            PlayerKnowledgeManager.unlockSalisMundus(player);
            player.displayClientMessage(Component.literal("The workbench hums with latent vis."), true);
        }
        level.levelEvent(2001, event.getPos(), Block.getId(targetState));
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    private static void handleCrucibleConversion(PlayerInteractEvent.RightClickBlock event, ItemStack heldStack, BlockState targetState, ServerPlayer player) {
        if (player != null && !PlayerKnowledgeManager.hasSalisMundusUnlocked(player)) {
            player.displayClientMessage(Component.literal("You are missing the knowledge to shape this structure."), true);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        BlockState replacement = ModBlocks.get("crucible").get().defaultBlockState();
        Level level = event.getLevel();
        level.setBlockAndUpdate(event.getPos(), replacement);
        if (player != null) {
            consumeSalisIfNeeded(heldStack, player);
            player.displayClientMessage(Component.literal("The cauldron reshapes into a crucible."), true);
        }
        level.levelEvent(2001, event.getPos(), Block.getId(targetState));
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    private static void handleInfusionAltarAssembly(PlayerInteractEvent.RightClickBlock event, ItemStack heldStack, ServerPlayer player) {
        if (player != null && !PlayerKnowledgeManager.hasSalisMundusUnlocked(player)) {
            player.displayClientMessage(Component.literal("You are missing the knowledge to shape this structure."), true);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        Level level = event.getLevel();
        BlockPos matrixPos = event.getPos();
        Block stoneBlock = ModBlocks.get("stone").get();
        Block pillarBlock = ModBlocks.get("pillar").get();
        Block pedestalBlock = ModBlocks.get("pedestal").get();

        BlockPos upperRingY = matrixPos.below();
        BlockPos lowerRingY = matrixPos.below(2);
        if (!isRingFilledWith(level, upperRingY, stoneBlock) || !isRingFilledWith(level, lowerRingY, stoneBlock)) {
            return;
        }
        if (!level.getBlockState(lowerRingY).isAir()) {
            return;
        }

        for (Vec3i offset : ALTAR_CORNER_OFFSETS) {
            BlockPos upperPos = upperRingY.offset(offset);
            BlockPos lowerPos = lowerRingY.offset(offset);
            level.setBlockAndUpdate(upperPos, Blocks.AIR.defaultBlockState());
            level.setBlockAndUpdate(lowerPos, pillarBlock.defaultBlockState());
        }
        level.setBlockAndUpdate(lowerRingY, pedestalBlock.defaultBlockState());
        if (player != null) {
            consumeSalisIfNeeded(heldStack, player);
            player.displayClientMessage(Component.literal("The infusion altar takes shape."), true);
        }
        level.levelEvent(2005, matrixPos, 0);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    private static void handleThaumatoriumAssembly(PlayerInteractEvent.RightClickBlock event, ItemStack heldStack, ServerPlayer player) {
        if (player != null && !PlayerKnowledgeManager.hasSalisMundusUnlocked(player)) {
            player.displayClientMessage(Component.literal("You are missing the knowledge to shape this structure."), true);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        Level level = event.getLevel();
        Block crucibleBlock = ModBlocks.get("crucible").get();
        Block metalBlock = ModBlocks.get("metal").get();

        BlockPos clicked = event.getPos();
        BlockPos base = null;
        for (int dy = 0; dy <= 2; dy++) {
            BlockPos candidate = clicked.below(dy);
            if (level.getBlockState(candidate).is(crucibleBlock)
                    && level.getBlockState(candidate.above()).is(metalBlock)
                    && level.getBlockState(candidate.above(2)).is(metalBlock)) {
                base = candidate;
                break;
            }
        }
        if (base == null) {
            return;
        }

        level.setBlockAndUpdate(base.above(), ModBlocks.get("thaumatorium").get().defaultBlockState());
        level.setBlockAndUpdate(base.above(2), ModBlocks.get("thaumatorium_top").get().defaultBlockState());
        if (player != null) {
            consumeSalisIfNeeded(heldStack, player);
            player.displayClientMessage(Component.literal("The thaumatorium assembles itself."), true);
        }
        level.levelEvent(2005, base.above(), 0);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    private static void handleInfernalFurnaceAssembly(PlayerInteractEvent.RightClickBlock event, ItemStack heldStack, ServerPlayer player) {
        Level level = event.getLevel();
        BlockPos center = findInfernalFurnaceCenter(level, event.getPos());
        if (center == null) {
            return;
        }
        if (player != null && !PlayerKnowledgeManager.hasSalisMundusUnlocked(player)) {
            player.displayClientMessage(Component.literal("You are missing the knowledge to shape this structure."), true);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        level.setBlockAndUpdate(center, ModBlocks.get("infernal_furnace").get().defaultBlockState());
        BlockPos barsPos = findInfernalFurnaceBars(level, center);
        if (barsPos != null) {
            level.setBlockAndUpdate(barsPos, Blocks.AIR.defaultBlockState());
        }
        if (player != null) {
            consumeSalisIfNeeded(heldStack, player);
            player.displayClientMessage(Component.literal("Infernal forces seize the furnace core."), true);
        }
        level.levelEvent(2005, center, 0);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    private static void handleGolemPressAssembly(PlayerInteractEvent.RightClickBlock event, ItemStack heldStack, ServerPlayer player) {
        Level level = event.getLevel();
        Block tableStoneBlock = ModBlocks.get("table_stone").get();
        BlockPos builderPos = findGolemBuilderBase(level, event.getPos(), tableStoneBlock);
        if (builderPos == null) {
            return;
        }
        if (player != null && !PlayerKnowledgeManager.hasSalisMundusUnlocked(player)) {
            player.displayClientMessage(Component.literal("You are missing the knowledge to shape this structure."), true);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        level.setBlockAndUpdate(builderPos, ModBlocks.get("golem_builder").get().defaultBlockState());
        level.setBlockAndUpdate(builderPos.above(), Blocks.IRON_BARS.defaultBlockState());
        if (player != null) {
            consumeSalisIfNeeded(heldStack, player);
            player.displayClientMessage(Component.literal("The golem press locks into place."), true);
        }
        level.levelEvent(2005, builderPos, 0);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    private static boolean isRingFilledWith(Level level, BlockPos center, Block requiredBlock) {
        for (Vec3i offset : ALTAR_CORNER_OFFSETS) {
            if (!level.getBlockState(center.offset(offset)).is(requiredBlock)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isInfernalFurnaceSourceBlock(BlockState state) {
        return state.is(Blocks.LAVA)
                || state.is(Blocks.NETHER_BRICKS)
                || state.is(Blocks.OBSIDIAN)
                || state.is(Blocks.IRON_BARS);
    }

    private static BlockPos findInfernalFurnaceCenter(Level level, BlockPos clicked) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos candidate = clicked.offset(dx, dy, dz);
                    if (matchesInfernalFurnace(level, candidate)) {
                        return candidate;
                    }
                }
            }
        }
        return null;
    }

    private static boolean matchesInfernalFurnace(Level level, BlockPos center) {
        if (!level.getBlockState(center).is(Blocks.LAVA)) {
            return false;
        }
        if (!level.getBlockState(center.below()).is(Blocks.OBSIDIAN)) {
            return false;
        }

        for (Vec3i corner : ALTAR_CORNER_OFFSETS) {
            if (!level.getBlockState(center.offset(corner.getX(), 1, corner.getZ())).is(Blocks.NETHER_BRICKS)) {
                return false;
            }
            if (!level.getBlockState(center.offset(corner.getX(), 0, corner.getZ())).is(Blocks.NETHER_BRICKS)) {
                return false;
            }
            if (!level.getBlockState(center.offset(corner.getX(), -1, corner.getZ())).is(Blocks.NETHER_BRICKS)) {
                return false;
            }
        }

        int barsCount = 0;
        int obsidianCount = 0;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos edgeTop = center.relative(direction).above();
            BlockPos edgeMid = center.relative(direction);
            BlockPos edgeBottom = center.relative(direction).below();
            if (!level.getBlockState(edgeTop).is(Blocks.OBSIDIAN) || !level.getBlockState(edgeBottom).is(Blocks.OBSIDIAN)) {
                return false;
            }

            BlockState midState = level.getBlockState(edgeMid);
            if (midState.is(Blocks.OBSIDIAN)) {
                obsidianCount++;
            } else if (midState.is(Blocks.IRON_BARS)) {
                barsCount++;
            } else {
                return false;
            }
        }
        return barsCount == 1 && obsidianCount == 3;
    }

    private static BlockPos findInfernalFurnaceBars(Level level, BlockPos center) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos pos = center.relative(direction);
            if (level.getBlockState(pos).is(Blocks.IRON_BARS)) {
                return pos;
            }
        }
        return null;
    }

    private static boolean isGolemPressSourceBlock(BlockState state) {
        return state.is(Blocks.CAULDRON)
                || state.is(Blocks.PISTON)
                || state.getBlock() instanceof AnvilBlock
                || state.is(ModBlocks.get("table_stone").get());
    }

    private static BlockPos findGolemBuilderBase(Level level, BlockPos clicked, Block tableStoneBlock) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos candidate = clicked.offset(dx, dy, dz);
                    if (matchesGolemPress(level, candidate, tableStoneBlock)) {
                        return candidate;
                    }
                }
            }
        }
        return null;
    }

    private static boolean matchesGolemPress(Level level, BlockPos builderPos, Block tableStoneBlock) {
        BlockState pistonState = level.getBlockState(builderPos);
        if (!pistonState.is(Blocks.PISTON)) {
            return false;
        }
        if (!pistonState.hasProperty(PistonBaseBlock.FACING) || pistonState.getValue(PistonBaseBlock.FACING) != Direction.UP) {
            return false;
        }
        if (!level.getBlockState(builderPos.above()).isAir()) {
            return false;
        }

        for (int rotation = 0; rotation < 4; rotation++) {
            Vec3i cauldronOffset = rotateHorizontalOffset(-1, 0, rotation);
            Vec3i anvilOffset = rotateHorizontalOffset(-1, 1, rotation);
            Vec3i tableOffset = rotateHorizontalOffset(0, 1, rotation);
            BlockPos cauldronPos = builderPos.offset(cauldronOffset.getX(), 0, cauldronOffset.getZ());
            BlockPos anvilPos = builderPos.offset(anvilOffset.getX(), 0, anvilOffset.getZ());
            BlockPos tablePos = builderPos.offset(tableOffset.getX(), 0, tableOffset.getZ());
            if (level.getBlockState(cauldronPos).is(Blocks.CAULDRON)
                    && level.getBlockState(anvilPos).getBlock() instanceof AnvilBlock
                    && level.getBlockState(tablePos).is(tableStoneBlock)) {
                return true;
            }
        }
        return false;
    }

    private static Vec3i rotateHorizontalOffset(int x, int z, int rotation) {
        return switch (rotation & 3) {
            case 1 -> new Vec3i(-z, 0, x);
            case 2 -> new Vec3i(-x, 0, -z);
            case 3 -> new Vec3i(z, 0, -x);
            default -> new Vec3i(x, 0, z);
        };
    }

    private static void consumeSalisIfNeeded(ItemStack heldStack, ServerPlayer player) {
        if (!player.getAbilities().instabuild) {
            heldStack.shrink(1);
        }
    }
}
