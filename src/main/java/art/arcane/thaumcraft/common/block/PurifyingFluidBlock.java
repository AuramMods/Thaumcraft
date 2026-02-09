package art.arcane.thaumcraft.common.block;

import art.arcane.thaumcraft.common.progression.PlayerKnowledgeManager;
import art.arcane.thaumcraft.common.registry.ModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class PurifyingFluidBlock extends Block {
    // TODO(port): convert this placeholder block into a proper fluid implementation with source/flowing states.

    private static final int BASE_WARD_DURATION_TICKS = 200_000;
    private static final int MAX_WARD_DURATION_TICKS = 32_000;

    public PurifyingFluidBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!(level instanceof ServerLevel serverLevel) || !(entity instanceof ServerPlayer player)) {
            return;
        }
        if (player.hasEffect(ModMobEffects.WARP_WARD.get())) {
            return;
        }

        int permanentWarp = PlayerKnowledgeManager.getWarp(player, PlayerKnowledgeManager.WarpType.PERMANENT);
        int divisor = permanentWarp <= 0 ? 1 : Math.max(1, (int) Math.sqrt(permanentWarp));
        int duration = Math.min(MAX_WARD_DURATION_TICKS, BASE_WARD_DURATION_TICKS / divisor);

        // Legacy behavior parity baseline: touching a source grants Warp Ward and consumes the source.
        player.addEffect(new MobEffectInstance(ModMobEffects.WARP_WARD.get(), duration, 0, true, true));
        serverLevel.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        serverLevel.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.6F, 1.2F);
    }
}
