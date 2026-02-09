package art.arcane.thaumcraft.common.command;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.aspect.AspectList;
import art.arcane.thaumcraft.common.aspect.AspectRegistry;
import art.arcane.thaumcraft.common.aspect.AspectType;
import art.arcane.thaumcraft.common.progression.WarpGearManager;
import art.arcane.thaumcraft.common.progression.PlayerKnowledgeManager;
import art.arcane.thaumcraft.common.progression.VisDiscountManager;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ThaumcraftCommandEvents {
    // TODO(port): Expand /thaumcraft debug with subcommands for targeted item ids, block states, and entity scans once scanner parity is complete.
    // TODO(port): Add pagination/filtering for large recipe trees and optional JSON dump output for tooling.

    private static final int MAX_TRACE_LINES = 120;
    private static final int MAX_WARP_VALUE = 100_000;
    private static final int MAX_COUNTER_VALUE = 1_000_000;

    private ThaumcraftCommandEvents() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("thaumcraft")
                        .then(Commands.literal("debug")
                                .then(Commands.literal("hand")
                                        .requires(source -> source.getEntity() instanceof ServerPlayer)
                                        .executes(context -> runDebugHand(context.getSource())))
                                .then(createVisDebugCommand())
                                .then(createWarpDebugCommand()))
        );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createVisDebugCommand() {
        return Commands.literal("vis")
                .requires(source -> source.getEntity() instanceof ServerPlayer)
                .then(Commands.literal("discount")
                        .executes(context -> runDebugVisDiscount(context.getSource())));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createWarpDebugCommand() {
        return Commands.literal("warp")
                .requires(source -> source.getEntity() instanceof ServerPlayer)
                .executes(context -> runDebugWarp(context.getSource()))
                .then(Commands.literal("add")
                        .then(createWarpAmountLiteral("permanent", PlayerKnowledgeManager.WarpType.PERMANENT, -MAX_WARP_VALUE, MAX_WARP_VALUE, ThaumcraftCommandEvents::runWarpAdd))
                        .then(createWarpAmountLiteral("normal", PlayerKnowledgeManager.WarpType.NORMAL, -MAX_WARP_VALUE, MAX_WARP_VALUE, ThaumcraftCommandEvents::runWarpAdd))
                        .then(createWarpAmountLiteral("temporary", PlayerKnowledgeManager.WarpType.TEMPORARY, -MAX_WARP_VALUE, MAX_WARP_VALUE, ThaumcraftCommandEvents::runWarpAdd)))
                .then(Commands.literal("set")
                        .then(createWarpAmountLiteral("permanent", PlayerKnowledgeManager.WarpType.PERMANENT, 0, MAX_WARP_VALUE, ThaumcraftCommandEvents::runWarpSet))
                        .then(createWarpAmountLiteral("normal", PlayerKnowledgeManager.WarpType.NORMAL, 0, MAX_WARP_VALUE, ThaumcraftCommandEvents::runWarpSet))
                        .then(createWarpAmountLiteral("temporary", PlayerKnowledgeManager.WarpType.TEMPORARY, 0, MAX_WARP_VALUE, ThaumcraftCommandEvents::runWarpSet)))
                .then(Commands.literal("clear")
                        .executes(context -> runWarpClearAll(context.getSource()))
                        .then(createWarpLiteral("permanent", PlayerKnowledgeManager.WarpType.PERMANENT, ThaumcraftCommandEvents::runWarpClear))
                        .then(createWarpLiteral("normal", PlayerKnowledgeManager.WarpType.NORMAL, ThaumcraftCommandEvents::runWarpClear))
                        .then(createWarpLiteral("temporary", PlayerKnowledgeManager.WarpType.TEMPORARY, ThaumcraftCommandEvents::runWarpClear)))
                .then(Commands.literal("gear")
                        .executes(context -> runDebugWarpGear(context.getSource())))
                .then(Commands.literal("counter")
                        .executes(context -> runDebugWarp(context.getSource()))
                        .then(Commands.literal("set")
                                .then(Commands.argument("value", IntegerArgumentType.integer(0, MAX_COUNTER_VALUE))
                                        .executes(context -> runWarpCounterSet(context.getSource(), IntegerArgumentType.getInteger(context, "value")))))
                        .then(Commands.literal("reset")
                                .executes(context -> runWarpCounterSet(context.getSource(), 0))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createWarpAmountLiteral(
            String literalName,
            PlayerKnowledgeManager.WarpType type,
            int minValue,
            int maxValue,
            WarpTypeAmountExecutor executor
    ) {
        return Commands.literal(literalName)
                .then(Commands.argument("amount", IntegerArgumentType.integer(minValue, maxValue))
                        .executes(context -> executor.run(
                                context.getSource(),
                                type,
                                IntegerArgumentType.getInteger(context, "amount")
                        )));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createWarpLiteral(
            String literalName,
            PlayerKnowledgeManager.WarpType type,
            WarpTypeExecutor executor
    ) {
        return Commands.literal(literalName)
                .executes(context -> executor.run(context.getSource(), type));
    }

    private static int runDebugHand(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) {
            source.sendFailure(Component.literal("Main hand is empty."));
            return 0;
        }

        AspectRegistry.AspectDebugResult debugResult = AspectRegistry.debugAspects(player.serverLevel(), stack);
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        String itemLabel = itemId == null ? "unknown" : itemId.toString();

        source.sendSuccess(() -> Component.literal("Thaumcraft Debug: " + itemLabel + " x" + stack.getCount()), false);
        source.sendSuccess(() -> Component.literal("Aspects: " + formatAspectSummary(debugResult.aspects())), false);

        int emitted = 0;
        for (String line : debugResult.traceLines()) {
            if (emitted >= MAX_TRACE_LINES) {
                int hidden = Math.max(0, debugResult.traceLines().size() - MAX_TRACE_LINES);
                source.sendSuccess(() -> Component.literal("... truncated " + hidden + " additional line(s)."), false);
                break;
            }
            String safeLine = line.length() > 300 ? line.substring(0, 300) + "..." : line;
            source.sendSuccess(() -> Component.literal(safeLine), false);
            emitted++;
        }

        return 1;
    }

    private static int runDebugWarp(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        sendWarpSummary(source, player);
        return 1;
    }

    private static int runWarpAdd(CommandSourceStack source, PlayerKnowledgeManager.WarpType type, int amount) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        int value = PlayerKnowledgeManager.addWarp(player, type, amount);
        source.sendSuccess(() -> Component.literal("warp." + warpTypeName(type) + " += " + amount + " -> " + value), false);
        sendWarpSummary(source, player);
        return 1;
    }

    private static int runWarpSet(CommandSourceStack source, PlayerKnowledgeManager.WarpType type, int amount) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        int value = PlayerKnowledgeManager.setWarp(player, type, amount);
        source.sendSuccess(() -> Component.literal("warp." + warpTypeName(type) + " = " + value), false);
        sendWarpSummary(source, player);
        return 1;
    }

    private static int runWarpClear(CommandSourceStack source, PlayerKnowledgeManager.WarpType type) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        PlayerKnowledgeManager.setWarp(player, type, 0);
        source.sendSuccess(() -> Component.literal("warp." + warpTypeName(type) + " cleared."), false);
        sendWarpSummary(source, player);
        return 1;
    }

    private static int runWarpClearAll(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        PlayerKnowledgeManager.setWarp(player, PlayerKnowledgeManager.WarpType.PERMANENT, 0);
        PlayerKnowledgeManager.setWarp(player, PlayerKnowledgeManager.WarpType.NORMAL, 0);
        PlayerKnowledgeManager.setWarp(player, PlayerKnowledgeManager.WarpType.TEMPORARY, 0);
        PlayerKnowledgeManager.setWarpEventCounter(player, 0);
        source.sendSuccess(() -> Component.literal("All warp pools and warp event counter cleared."), false);
        sendWarpSummary(source, player);
        return 1;
    }

    private static int runWarpCounterSet(CommandSourceStack source, int value) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        PlayerKnowledgeManager.setWarpEventCounter(player, value);
        source.sendSuccess(() -> Component.literal("warp.event_counter = " + value), false);
        sendWarpSummary(source, player);
        return 1;
    }

    private static int runDebugVisDiscount(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        int totalDiscount = VisDiscountManager.getTotalVisDiscountPercent(player);

        source.sendSuccess(() -> Component.literal("Thaumcraft Debug Vis Discount"), false);
        source.sendSuccess(() -> Component.literal("total_discount_percent=" + totalDiscount), false);

        String[] armorSlotNames = {"boots", "leggings", "chestplate", "helmet"};
        for (int i = 0; i < player.getInventory().armor.size(); i++) {
            ItemStack armorStack = player.getInventory().armor.get(i);
            int discount = VisDiscountManager.getVisDiscountPercent(armorStack, player);
            String slotName = i < armorSlotNames.length ? armorSlotNames[i] : ("armor_" + i);
            source.sendSuccess(() -> Component.literal(slotName + ": " + formatStackLabel(armorStack) + " -> " + discount + "%"), false);
        }

        return 1;
    }

    private static int runDebugWarpGear(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        ItemStack mainHand = player.getMainHandItem();
        int mainHandWarp = WarpGearManager.getWarpFromStack(mainHand, player);

        source.sendSuccess(() -> Component.literal("Thaumcraft Debug Warp Gear"), false);
        source.sendSuccess(() -> Component.literal("main_hand: " + formatStackLabel(mainHand) + " -> " + mainHandWarp), false);

        String[] armorSlotNames = {"boots", "leggings", "chestplate", "helmet"};
        int totalArmorWarp = 0;
        for (int i = 0; i < player.getInventory().armor.size(); i++) {
            ItemStack armorStack = player.getInventory().armor.get(i);
            int armorWarp = WarpGearManager.getWarpFromStack(armorStack, player);
            totalArmorWarp += armorWarp;

            String slotName = i < armorSlotNames.length ? armorSlotNames[i] : ("armor_" + i);
            source.sendSuccess(() -> Component.literal(slotName + ": " + formatStackLabel(armorStack) + " -> " + armorWarp), false);
        }

        int totalGearWarp = mainHandWarp + totalArmorWarp;
        source.sendSuccess(() -> Component.literal("gear_total=" + totalGearWarp), false);
        return 1;
    }

    private static void sendWarpSummary(CommandSourceStack source, ServerPlayer player) {
        PlayerKnowledgeManager.WarpSnapshot warp = PlayerKnowledgeManager.getWarpSnapshot(player);
        int counter = PlayerKnowledgeManager.getWarpEventCounter(player);
        int gearWarp = WarpGearManager.getWarpFromGear(player);
        int totalWithGear = warp.total() + gearWarp;
        boolean bathSaltsHint = PlayerKnowledgeManager.hasWarpMilestone(player, PlayerKnowledgeManager.WarpMilestone.BATH_SALTS_HINT);
        boolean eldritchMinor = PlayerKnowledgeManager.hasWarpMilestone(player, PlayerKnowledgeManager.WarpMilestone.ELDRITCH_MINOR);
        boolean eldritchMajor = PlayerKnowledgeManager.hasWarpMilestone(player, PlayerKnowledgeManager.WarpMilestone.ELDRITCH_MAJOR);

        source.sendSuccess(() -> Component.literal("Thaumcraft Debug Warp"), false);
        source.sendSuccess(() -> Component.literal("permanent=" + warp.permanent()
                + ", normal=" + warp.normal()
                + ", temporary=" + warp.temporary()
                + ", total=" + warp.total()), false);
        source.sendSuccess(() -> Component.literal("gear=" + gearWarp + ", total_with_gear=" + totalWithGear), false);
        source.sendSuccess(() -> Component.literal("event_counter=" + counter), false);
        source.sendSuccess(() -> Component.literal("milestones: bath_salts_hint=" + bathSaltsHint
                + ", eldritch_minor=" + eldritchMinor
                + ", eldritch_major=" + eldritchMajor), false);
    }

    private static String warpTypeName(PlayerKnowledgeManager.WarpType type) {
        return switch (type) {
            case PERMANENT -> "permanent";
            case NORMAL -> "normal";
            case TEMPORARY -> "temporary";
        };
    }

    private static String formatAspectSummary(AspectList aspects) {
        if (aspects == null || aspects.isEmpty()) {
            return "none";
        }

        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Map.Entry<AspectType, Integer> entry : aspects.asMap().entrySet()) {
            if (!first) {
                builder.append(", ");
            }
            first = false;
            builder.append(entry.getKey().getTag()).append("=").append(entry.getValue());
        }
        return builder.toString();
    }

    private static String formatStackLabel(ItemStack stack) {
        if (stack.isEmpty()) {
            return "empty";
        }

        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId == null) {
            return "unknown";
        }

        return itemId.toString();
    }

    @FunctionalInterface
    private interface WarpTypeAmountExecutor {
        int run(CommandSourceStack source, PlayerKnowledgeManager.WarpType type, int value) throws CommandSyntaxException;
    }

    @FunctionalInterface
    private interface WarpTypeExecutor {
        int run(CommandSourceStack source, PlayerKnowledgeManager.WarpType type) throws CommandSyntaxException;
    }
}
