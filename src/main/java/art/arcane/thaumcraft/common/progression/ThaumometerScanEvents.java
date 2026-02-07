package art.arcane.thaumcraft.common.progression;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.aspect.AspectList;
import art.arcane.thaumcraft.common.aspect.AspectRegistry;
import art.arcane.thaumcraft.common.aspect.AspectType;
import art.arcane.thaumcraft.common.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ThaumometerScanEvents {

    private static final String THAUMOMETER_ID = "thaumometer";

    private ThaumometerScanEvents() {
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide || event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        RegistryObject<Item> thaumometerObject = ModItems.ITEMS_BY_ID.get(THAUMOMETER_ID);
        if (thaumometerObject == null) {
            return;
        }

        ItemStack held = event.getItemStack();
        if (!held.is(thaumometerObject.get())) {
            return;
        }

        if (!PlayerKnowledgeManager.hasSalisMundusUnlocked(player)) {
            player.displayClientMessage(Component.literal("You cannot decipher these readings yet."), true);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        BlockState state = event.getLevel().getBlockState(event.getPos());
        ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(state.getBlock());
        if (blockId == null) {
            return;
        }

        ItemStack scanStack = new ItemStack(state.getBlock().asItem());
        AspectList aspects = AspectRegistry.getAspects(scanStack);

        PlayerKnowledgeManager.ScanResult result = PlayerKnowledgeManager.recordBlockScan(player, blockId, aspects);
        String aspectSummary = formatAspectSummary(aspects);

        if (result.firstScan()) {
            player.displayClientMessage(
                    Component.literal("Scanned " + blockId + " | Aspects: " + aspectSummary + " | Knowledge " + result.totalScans() + " | Discovered Aspects " + result.totalAspects()),
                    true
            );
        } else {
            player.displayClientMessage(
                    Component.literal("Known object: " + blockId + " | Aspects: " + aspectSummary + " | Knowledge " + result.totalScans()),
                    true
            );
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    private static String formatAspectSummary(AspectList aspects) {
        if (aspects.isEmpty()) {
            return "none";
        }

        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (java.util.Map.Entry<AspectType, Integer> entry : aspects.asMap().entrySet()) {
            if (!first) {
                builder.append(", ");
            }
            first = false;
            builder.append(entry.getKey().getTag()).append("=").append(entry.getValue());
        }

        return builder.toString();
    }
}
