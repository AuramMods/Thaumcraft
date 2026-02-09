package art.arcane.thaumcraft.common.progression;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.registry.ModMobEffects;
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
    // TODO(port): Add gear warp modifiers and research-unlock milestone hooks to match legacy checkWarpEvent behavior.

    private static final int TEMPORARY_WARP_DECAY_INTERVAL_TICKS = 2000;
    private static final int MIN_COUNTER_DECAY_ON_EVENT = 5;

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

        // Legacy parity baseline: warp events are suppressed while Warp Ward is active.
        if (player.hasEffect(ModMobEffects.WARP_WARD.get())) {
            return;
        }

        if ((player.tickCount % TEMPORARY_WARP_DECAY_INTERVAL_TICKS) != 0) {
            return;
        }

        // Legacy-shaped baseline: temporary warp passively decays on each periodic warp check.
        int temporary = PlayerKnowledgeManager.getWarp(player, PlayerKnowledgeManager.WarpType.TEMPORARY);
        if (temporary > 0) {
            PlayerKnowledgeManager.addWarp(player, PlayerKnowledgeManager.WarpType.TEMPORARY, -1);
        }

        PlayerKnowledgeManager.WarpSnapshot warp = PlayerKnowledgeManager.getWarpSnapshot(player);
        int totalWarp = warp.total();
        int counter = PlayerKnowledgeManager.getWarpEventCounter(player);

        if (counter <= 0 || totalWarp <= 0) {
            return;
        }

        int roll = player.getRandom().nextInt(100);
        double threshold = Math.sqrt(counter);
        if (roll > threshold) {
            return;
        }

        int effectiveWarp = Math.min(100, (totalWarp + totalWarp + counter) / 3);
        int counterDecay = Math.max(MIN_COUNTER_DECAY_ON_EVENT, (int) (Math.sqrt(counter) * 2.0D));
        counter = Math.max(0, counter - counterDecay);
        PlayerKnowledgeManager.setWarpEventCounter(player, counter);

        triggerWarpEvent(player, effectiveWarp);
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
