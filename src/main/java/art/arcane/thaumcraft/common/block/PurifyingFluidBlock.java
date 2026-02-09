package art.arcane.thaumcraft.common.block;

import art.arcane.thaumcraft.common.progression.PlayerKnowledgeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class PurifyingFluidBlock extends Block {
    // TODO(port): Replace temporary resistance buff with true Warp Ward effect once warp/insanity capability is ported.
    // TODO(port): convert this placeholder block into a proper fluid implementation with source/flowing states.

    private static final int WARD_DURATION_TICKS = 20 * 30;

    public PurifyingFluidBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!(level instanceof ServerLevel serverLevel) || !(entity instanceof Player player)) {
            return;
        }
        if (player.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
            return;
        }

        // Transitional parity: consume source and grant a temporary ward-like protection effect.
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, WARD_DURATION_TICKS, 0, true, true));
        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            // Transitional parity: purifying fluid trims temporary warp until full warp system/event loop is in place.
            int temporary = PlayerKnowledgeManager.getWarp(serverPlayer, PlayerKnowledgeManager.WarpType.TEMPORARY);
            if (temporary > 0) {
                PlayerKnowledgeManager.addWarp(serverPlayer, PlayerKnowledgeManager.WarpType.TEMPORARY, -1);
            }
        }
        serverLevel.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        serverLevel.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.6F, 1.2F);
    }
}
