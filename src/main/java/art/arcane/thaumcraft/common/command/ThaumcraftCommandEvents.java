package art.arcane.thaumcraft.common.command;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.aspect.AspectList;
import art.arcane.thaumcraft.common.aspect.AspectRegistry;
import art.arcane.thaumcraft.common.aspect.AspectType;
import art.arcane.thaumcraft.common.progression.WarpGearManager;
import art.arcane.thaumcraft.common.progression.PlayerKnowledgeManager;
import art.arcane.thaumcraft.common.progression.VisDiscountManager;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ThaumcraftCommandEvents {
    // TODO(port): Expand /thaumcraft debug with subcommands for targeted item ids, block states, and entity scans once scanner parity is complete.
    // TODO(port): Add pagination/filtering for large recipe trees and optional JSON dump output for tooling.

    private static final int MAX_TRACE_LINES = 120;
    private static final int MAX_WARP_VALUE = 100_000;
    private static final int MAX_COUNTER_VALUE = 1_000_000;
    private static final int MAX_RESEARCH_STAGE = 256;
    private static final int DEFAULT_RESEARCH_SUMMARY_LINES = 12;

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
                                .then(createWarpDebugCommand())
                                .then(createResearchDebugCommand()))
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

    private static LiteralArgumentBuilder<CommandSourceStack> createResearchDebugCommand() {
        return Commands.literal("research")
                .requires(source -> source.getEntity() instanceof ServerPlayer)
                .executes(context -> runDebugResearchSummary(context.getSource(), false))
                .then(Commands.literal("list")
                        .executes(context -> runDebugResearchSummary(context.getSource(), true)))
                .then(Commands.literal("unlock")
                        .then(Commands.argument("key", StringArgumentType.word())
                                .executes(context -> runResearchUnlock(
                                        context.getSource(),
                                        StringArgumentType.getString(context, "key")
                                ))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("key", StringArgumentType.word())
                                .executes(context -> runResearchRemove(
                                        context.getSource(),
                                        StringArgumentType.getString(context, "key")
                                ))))
                .then(Commands.literal("stage")
                        .then(Commands.argument("key", StringArgumentType.word())
                                .executes(context -> runResearchStageGet(
                                        context.getSource(),
                                        StringArgumentType.getString(context, "key")
                                ))
                                .then(Commands.argument("value", IntegerArgumentType.integer(0, MAX_RESEARCH_STAGE))
                                        .executes(context -> runResearchStageSet(
                                                context.getSource(),
                                                StringArgumentType.getString(context, "key"),
                                                IntegerArgumentType.getInteger(context, "value")
                                        )))))
                .then(Commands.literal("flag")
                        .then(Commands.literal("set")
                                .then(Commands.argument("key", StringArgumentType.word())
                                        .then(Commands.argument("flag", StringArgumentType.word())
                                                .executes(context -> runResearchFlagSet(
                                                        context.getSource(),
                                                        StringArgumentType.getString(context, "key"),
                                                        StringArgumentType.getString(context, "flag")
                                                )))))
                        .then(Commands.literal("clear")
                                .then(Commands.argument("key", StringArgumentType.word())
                                        .then(Commands.argument("flag", StringArgumentType.word())
                                                .executes(context -> runResearchFlagClear(
                                                        context.getSource(),
                                                        StringArgumentType.getString(context, "key"),
                                                        StringArgumentType.getString(context, "flag")
                                                )))))
                        .then(Commands.literal("check")
                                .then(Commands.argument("key", StringArgumentType.word())
                                        .then(Commands.argument("flag", StringArgumentType.word())
                                                .executes(context -> runResearchFlagCheck(
                                                        context.getSource(),
                                                        StringArgumentType.getString(context, "key"),
                                                        StringArgumentType.getString(context, "flag")
                                                ))))));
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

    private static int runDebugResearchSummary(CommandSourceStack source, boolean verbose) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Set<String> keys = PlayerKnowledgeManager.getResearchKeys(player);
        List<String> sorted = new ArrayList<>(keys);
        sorted.sort(String::compareTo);

        source.sendSuccess(() -> Component.literal("Thaumcraft Debug Research"), false);
        source.sendSuccess(() -> Component.literal("known_keys=" + sorted.size()), false);

        if (sorted.isEmpty()) {
            return 1;
        }

        int maxLines = verbose ? sorted.size() : Math.min(DEFAULT_RESEARCH_SUMMARY_LINES, sorted.size());
        for (int i = 0; i < maxLines; i++) {
            String key = sorted.get(i);
            int stage = PlayerKnowledgeManager.getResearchStage(player, key);
            Set<PlayerKnowledgeManager.ResearchFlag> flags = PlayerKnowledgeManager.getResearchFlags(player, key);
            source.sendSuccess(() -> Component.literal(formatResearchEntry(key, stage, flags)), false);
        }

        if (!verbose && sorted.size() > maxLines) {
            int remaining = sorted.size() - maxLines;
            source.sendSuccess(() -> Component.literal("... " + remaining + " more key(s). Use /thaumcraft debug research list"), false);
        }
        return 1;
    }

    private static int runResearchUnlock(CommandSourceStack source, String key) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        boolean changed = PlayerKnowledgeManager.unlockResearch(player, key);
        source.sendSuccess(() -> Component.literal("research unlock " + key + " -> " + (changed ? "added" : "unchanged")), false);
        return runDebugResearchSummary(source, false);
    }

    private static int runResearchRemove(CommandSourceStack source, String key) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        boolean changed = PlayerKnowledgeManager.removeResearch(player, key);
        source.sendSuccess(() -> Component.literal("research remove " + key + " -> " + (changed ? "removed" : "not_found")), false);
        return runDebugResearchSummary(source, false);
    }

    private static int runResearchStageGet(CommandSourceStack source, String key) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        int stage = PlayerKnowledgeManager.getResearchStage(player, key);
        source.sendSuccess(() -> Component.literal("research stage " + key + " = " + stage), false);
        return 1;
    }

    private static int runResearchStageSet(CommandSourceStack source, String key, int stage) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        boolean changed = PlayerKnowledgeManager.setResearchStage(player, key, stage);
        source.sendSuccess(() -> Component.literal("research stage set " + key + " -> " + stage + " (" + (changed ? "updated" : "unchanged_or_unknown") + ")"), false);
        return runResearchStageGet(source, key);
    }

    private static int runResearchFlagSet(CommandSourceStack source, String key, String rawFlag) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        PlayerKnowledgeManager.ResearchFlag flag = parseResearchFlag(rawFlag);
        if (flag == null) {
            source.sendFailure(Component.literal("Unknown research flag: " + rawFlag + " (valid: page, research, popup)"));
            return 0;
        }

        boolean changed = PlayerKnowledgeManager.setResearchFlag(player, key, flag);
        source.sendSuccess(() -> Component.literal("research flag set " + key + "." + flag.name().toLowerCase(Locale.ROOT)
                + " -> " + (changed ? "added" : "unchanged_or_unknown")), false);
        return 1;
    }

    private static int runResearchFlagClear(CommandSourceStack source, String key, String rawFlag) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        PlayerKnowledgeManager.ResearchFlag flag = parseResearchFlag(rawFlag);
        if (flag == null) {
            source.sendFailure(Component.literal("Unknown research flag: " + rawFlag + " (valid: page, research, popup)"));
            return 0;
        }

        boolean changed = PlayerKnowledgeManager.clearResearchFlag(player, key, flag);
        source.sendSuccess(() -> Component.literal("research flag clear " + key + "." + flag.name().toLowerCase(Locale.ROOT)
                + " -> " + (changed ? "removed" : "unchanged_or_unknown")), false);
        return 1;
    }

    private static int runResearchFlagCheck(CommandSourceStack source, String key, String rawFlag) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        PlayerKnowledgeManager.ResearchFlag flag = parseResearchFlag(rawFlag);
        if (flag == null) {
            source.sendFailure(Component.literal("Unknown research flag: " + rawFlag + " (valid: page, research, popup)"));
            return 0;
        }

        boolean hasFlag = PlayerKnowledgeManager.hasResearchFlag(player, key, flag);
        source.sendSuccess(() -> Component.literal("research flag check " + key + "." + flag.name().toLowerCase(Locale.ROOT)
                + " = " + hasFlag), false);
        return 1;
    }

    private static void sendWarpSummary(CommandSourceStack source, ServerPlayer player) {
        PlayerKnowledgeManager.WarpSnapshot warp = PlayerKnowledgeManager.getWarpSnapshot(player);
        int counter = PlayerKnowledgeManager.getWarpEventCounter(player);
        int gearWarp = WarpGearManager.getWarpFromGear(player);
        int totalWithGear = warp.total() + gearWarp;
        boolean bathSalts = PlayerKnowledgeManager.hasResearch(player, "BATHSALTS");
        boolean bathSaltsHint = PlayerKnowledgeManager.hasResearch(player, PlayerKnowledgeManager.RESEARCH_BATH_SALTS_HINT);
        boolean eldritchMinor = PlayerKnowledgeManager.hasResearch(player, PlayerKnowledgeManager.RESEARCH_ELDRITCH_MINOR);
        boolean eldritchMajor = PlayerKnowledgeManager.hasResearch(player, PlayerKnowledgeManager.RESEARCH_ELDRITCH_MAJOR);

        source.sendSuccess(() -> Component.literal("Thaumcraft Debug Warp"), false);
        source.sendSuccess(() -> Component.literal("permanent=" + warp.permanent()
                + ", normal=" + warp.normal()
                + ", temporary=" + warp.temporary()
                + ", total=" + warp.total()), false);
        source.sendSuccess(() -> Component.literal("gear=" + gearWarp + ", total_with_gear=" + totalWithGear), false);
        source.sendSuccess(() -> Component.literal("event_counter=" + counter), false);
        source.sendSuccess(() -> Component.literal("research: BATHSALTS=" + bathSalts
                + ", !BATHSALTS=" + bathSaltsHint
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

    private static String formatResearchEntry(String key, int stage, Set<PlayerKnowledgeManager.ResearchFlag> flags) {
        return key + " (stage=" + stage + ", flags=" + formatResearchFlags(flags) + ")";
    }

    private static String formatResearchFlags(Set<PlayerKnowledgeManager.ResearchFlag> flags) {
        if (flags == null || flags.isEmpty()) {
            return "-";
        }

        List<String> names = new ArrayList<>();
        for (PlayerKnowledgeManager.ResearchFlag flag : flags) {
            names.add(flag.name().toLowerCase(Locale.ROOT));
        }
        names.sort(String::compareTo);

        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String name : names) {
            if (!first) {
                builder.append(",");
            }
            first = false;
            builder.append(name);
        }
        return builder.toString();
    }

    private static PlayerKnowledgeManager.ResearchFlag parseResearchFlag(String rawFlag) {
        if (rawFlag == null || rawFlag.isBlank()) {
            return null;
        }

        try {
            return PlayerKnowledgeManager.ResearchFlag.valueOf(rawFlag.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            return null;
        }
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
