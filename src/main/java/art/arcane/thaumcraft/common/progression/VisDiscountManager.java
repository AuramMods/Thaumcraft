package art.arcane.thaumcraft.common.progression;

import art.arcane.thaumcraft.common.item.VisDiscountGearItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public final class VisDiscountManager {
    private static final int MAX_TOTAL_DISCOUNT_PERCENT = 90;

    private VisDiscountManager() {
    }

    public static int getTotalVisDiscountPercent(ServerPlayer player) {
        int total = 0;
        for (ItemStack armor : player.getInventory().armor) {
            total += getVisDiscountPercent(armor, player);
        }
        // TODO(port): Add curios/baubles-equivalent vis discount sources once accessory support is ported.
        return Math.max(0, total);
    }

    public static int getVisDiscountPercent(ItemStack stack, ServerPlayer player) {
        if (stack.isEmpty()) {
            return 0;
        }
        if (stack.getItem() instanceof VisDiscountGearItem visDiscountGearItem) {
            return Math.max(0, visDiscountGearItem.getVisDiscountPercent(stack, player));
        }
        return 0;
    }

    public static int applyDiscountToCost(int baseCost, ServerPlayer player) {
        if (baseCost <= 0) {
            return 0;
        }

        int discountPercent = Mth.clamp(getTotalVisDiscountPercent(player), 0, MAX_TOTAL_DISCOUNT_PERCENT);
        double multiplier = (100.0D - discountPercent) / 100.0D;
        return Math.max(1, Mth.ceil(baseCost * multiplier));
    }
}
