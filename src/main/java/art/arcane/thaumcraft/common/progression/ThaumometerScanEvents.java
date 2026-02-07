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
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ThaumometerScanEvents {
    // TODO(port): Replace baseline scan counters with legacy-style ScanningManager behavior:
    // TODO(port): scans should unlock research keys, grant category-specific observation knowledge, and distinguish unknown vs known targets by research state.
    // TODO(port): expand scan surface parity (inventory contents, special block/entity handlers, and richer client feedback/UI hooks).

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

        Item thaumometer = getThaumometerItem();
        if (thaumometer == null) {
            return;
        }

        ItemStack held = event.getItemStack();
        if (!held.is(thaumometer)) {
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
        AspectList aspects = AspectRegistry.getAspects(event.getLevel(), scanStack);

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

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide || event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        Item thaumometer = getThaumometerItem();
        if (thaumometer == null || !event.getItemStack().is(thaumometer)) {
            return;
        }

        if (!PlayerKnowledgeManager.hasSalisMundusUnlocked(player)) {
            player.displayClientMessage(Component.literal("You cannot decipher these readings yet."), true);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(event.getTarget().getType());
        if (entityId == null) {
            return;
        }

        ItemStack scanStack = event.getTarget().getPickResult();
        if (scanStack.isEmpty()) {
            SpawnEggItem spawnEgg = SpawnEggItem.byId(event.getTarget().getType());
            if (spawnEgg != null) {
                scanStack = new ItemStack(spawnEgg);
            }
        }

        AspectList aspects = scanStack.isEmpty() ? new AspectList() : AspectRegistry.getAspects(event.getLevel(), scanStack);
        if (aspects.isEmpty()) {
            aspects.add(AspectType.COGNITIO, 4);
        }

        PlayerKnowledgeManager.ScanResult result = PlayerKnowledgeManager.recordEntityScan(player, entityId, aspects);
        String aspectSummary = formatAspectSummary(aspects);
        if (result.firstScan()) {
            player.displayClientMessage(
                    Component.literal("Scanned entity " + entityId + " | Aspects: " + aspectSummary + " | Knowledge " + result.totalScans() + " | Discovered Aspects " + result.totalAspects()),
                    true
            );
        } else {
            player.displayClientMessage(
                    Component.literal("Known entity: " + entityId + " | Aspects: " + aspectSummary + " | Knowledge " + result.totalScans()),
                    true
            );
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getLevel().isClientSide || event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        Item thaumometer = getThaumometerItem();
        if (thaumometer == null || !event.getItemStack().is(thaumometer)) {
            return;
        }

        if (!PlayerKnowledgeManager.hasSalisMundusUnlocked(player)) {
            player.displayClientMessage(Component.literal("You cannot decipher these readings yet."), true);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            return;
        }

        ItemStack target = player.getItemInHand(InteractionHand.OFF_HAND);
        if (target.isEmpty() || target.is(thaumometer)) {
            return;
        }

        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(target.getItem());
        if (itemId == null) {
            return;
        }

        AspectList aspects = AspectRegistry.getAspects(event.getLevel(), target);
        PlayerKnowledgeManager.ScanResult result = PlayerKnowledgeManager.recordItemScan(player, itemId, aspects);
        String aspectSummary = formatAspectSummary(aspects);
        if (result.firstScan()) {
            player.displayClientMessage(
                    Component.literal("Scanned item " + itemId + " | Aspects: " + aspectSummary + " | Knowledge " + result.totalScans() + " | Discovered Aspects " + result.totalAspects()),
                    true
            );
        } else {
            player.displayClientMessage(
                    Component.literal("Known item: " + itemId + " | Aspects: " + aspectSummary + " | Knowledge " + result.totalScans()),
                    true
            );
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    private static Item getThaumometerItem() {
        RegistryObject<Item> thaumometerObject = ModItems.ITEMS_BY_ID.get(THAUMOMETER_ID);
        if (thaumometerObject == null) {
            return null;
        }
        return thaumometerObject.get();
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
