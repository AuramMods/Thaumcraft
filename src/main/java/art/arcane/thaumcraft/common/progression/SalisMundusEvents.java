package art.arcane.thaumcraft.common.progression;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.registry.ModBlocks;
import art.arcane.thaumcraft.common.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SalisMundusEvents {

    private static final String SALIS_MUNDUS_ID = "salis_mundus";

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
        if (!targetState.is(Blocks.CRAFTING_TABLE)) {
            return;
        }

        BlockState replacement = ModBlocks.get("arcane_workbench").get().defaultBlockState();
        level.setBlockAndUpdate(pos, replacement);

        if (!event.getEntity().getAbilities().instabuild) {
            heldStack.shrink(1);
        }
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PlayerKnowledgeManager.unlockSalisMundus(serverPlayer);
            serverPlayer.displayClientMessage(Component.literal("Arcane insight stirs within you."), true);
        }

        level.levelEvent(2001, pos, Block.getId(targetState));
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }
}
