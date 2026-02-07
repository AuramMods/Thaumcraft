package art.arcane.thaumcraft.common.command;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.aspect.AspectList;
import art.arcane.thaumcraft.common.aspect.AspectRegistry;
import art.arcane.thaumcraft.common.aspect.AspectType;
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

    private ThaumcraftCommandEvents() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("thaumcraft")
                        .then(Commands.literal("debug")
                                .then(Commands.literal("hand")
                                        .requires(source -> source.getEntity() instanceof ServerPlayer)
                                        .executes(context -> runDebugHand(context.getSource()))))
        );
    }

    private static int runDebugHand(CommandSourceStack source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
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
}
