package art.arcane.thaumcraft.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class LiquidDeathBlock extends Block {
    // TODO(port): Move to true fluid with depth-based damage scaling (legacy used source quanta/meta to scale damage).
    // TODO(port): reintroduce legacy dissolve damage source and visuals once custom damage/effects layers are ported.

    private static final int DAMAGE_INTERVAL_TICKS = 10;
    private static final float DAMAGE_PER_TICK = 4.0F;

    public LiquidDeathBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!(level instanceof ServerLevel serverLevel) || !(entity instanceof LivingEntity living)) {
            return;
        }
        if ((living.tickCount % DAMAGE_INTERVAL_TICKS) != 0) {
            return;
        }

        living.hurt(serverLevel.damageSources().magic(), DAMAGE_PER_TICK);
    }
}
