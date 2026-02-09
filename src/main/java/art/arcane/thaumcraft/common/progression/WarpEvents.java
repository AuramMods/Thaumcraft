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
    // TODO(port): Expand baseline research unlock flow to full research-stage/state parity once thaumonomicon progression is ported.

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
        int actualWarp = warp.permanent() + warp.normal();
        int gearWarp = WarpGearManager.getWarpFromGear(player);
        int totalWarp = warp.total() + gearWarp;
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
        int counterDecay = Math.max(MIN_COUNTER_DECAY_ON_EVENT, (int) (Math.sqrt(counter) * 2.0D - (gearWarp * 2.0D)));
        counter = Math.max(0, counter - counterDecay);
        PlayerKnowledgeManager.setWarpEventCounter(player, counter);

        triggerWarpEvent(player, effectiveWarp);
        applyWarpResearchUnlocks(player, actualWarp);
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

    private static void applyWarpResearchUnlocks(ServerPlayer player, int actualWarp) {
        boolean knowsBathSalts = PlayerKnowledgeManager.hasResearch(player, "BATHSALTS")
                || PlayerKnowledgeManager.hasResearch(player, PlayerKnowledgeManager.RESEARCH_BATH_SALTS_HINT);

        if (actualWarp > 10 && !knowsBathSalts
                && PlayerKnowledgeManager.unlockResearch(player, PlayerKnowledgeManager.RESEARCH_BATH_SALTS_HINT)) {
            player.displayClientMessage(Component.literal("A cleansing thought surfaces: perhaps bath salts could calm this corruption."), false);
        }
        if (actualWarp > 25) {
            PlayerKnowledgeManager.unlockResearch(player, PlayerKnowledgeManager.RESEARCH_ELDRITCH_MINOR);
        }
        if (actualWarp > 50) {
            PlayerKnowledgeManager.unlockResearch(player, PlayerKnowledgeManager.RESEARCH_ELDRITCH_MAJOR);
        }
    }
}
