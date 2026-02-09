package art.arcane.thaumcraft.common.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public interface VisDiscountGearItem {
    int getVisDiscountPercent(ItemStack stack, ServerPlayer player);
}
