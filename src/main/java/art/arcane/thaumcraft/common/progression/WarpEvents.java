package art.arcane.thaumcraft.common.progression;

import art.arcane.thaumcraft.Thaumcraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class WarpEvents {
    // TODO(port): Replace this baseline ticker with full legacy warp event table/progression gating/event categories.
    // TODO(port): Add client FX/audio hallucination events and proper sync packet flow.

    private static final int COUNTER_THRESHOLD = 1000;

    private WarpEvents() {
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) {
            return;
        }
        if (!(event.player instanceof ServerPlayer player)) {
            return;
        }
        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        int totalWarp = PlayerKnowledgeManager.getTotalWarp(player);
        if (totalWarp <= 0) {
            return;
        }

        if ((player.tickCount % 20) != 0) {
            return;
        }

        int counter = PlayerKnowledgeManager.getWarpEventCounter(player);
        counter += totalWarp;
        if (counter < COUNTER_THRESHOLD) {
            PlayerKnowledgeManager.setWarpEventCounter(player, counter);
            return;
        }

        counter -= COUNTER_THRESHOLD;
        PlayerKnowledgeManager.setWarpEventCounter(player, counter);
        triggerWarpEvent(player, totalWarp);
    }

    private static void triggerWarpEvent(ServerPlayer player, int totalWarp) {
        if (totalWarp >= 75) {
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 0, true, true));
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 120, 0, true, true));
            player.displayClientMessage(Component.literal("Dark visions overwhelm your mind."), true);
            return;
        }

        if (totalWarp >= 50) {
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 180, 0, true, true));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 180, 0, true, true));
            player.displayClientMessage(Component.literal("Your thoughts twist with eldritch static."), true);
            return;
        }

        if (totalWarp >= 25) {
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 160, 0, true, true));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 160, 0, true, true));
            player.displayClientMessage(Component.literal("A creeping unease settles in."), true);
            return;
        }

        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0, true, true));
        player.displayClientMessage(Component.literal("You feel briefly unsettled."), true);
    }
}
