package art.arcane.thaumcraft.common.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class ThaumcraftItemBehaviors {
    private ThaumcraftItemBehaviors() {
    }

    public static void selfRepair(ItemStack stack, Level level, Entity entity, int intervalTicks) {
        if (level.isClientSide()) {
            return;
        }
        if (intervalTicks <= 0 || entity.tickCount % intervalTicks != 0) {
            return;
        }
        if (!stack.isDamaged()) {
            return;
        }

        stack.setDamageValue(Math.max(0, stack.getDamageValue() - 1));
    }

    public static void applyWither(LivingEntity target, int durationTicks) {
        if (durationTicks <= 0 || target.level().isClientSide()) {
            return;
        }
        target.addEffect(new MobEffectInstance(MobEffects.WITHER, durationTicks, 0));
    }

    public static void applyWeakness(LivingEntity target, int durationTicks) {
        if (durationTicks <= 0 || target.level().isClientSide()) {
            return;
        }
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, durationTicks, 0));
    }
}
