package art.arcane.thaumcraft.common.progression;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.item.WarpingGearItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public final class WarpGearManager {
    private static final String LEGACY_WARP_NBT_TAG = "TC.WARP";

    // Baseline fallback for warping items that are still placeholder classes in the 1.20.1 port.
    private static final Map<String, Integer> FALLBACK_ITEM_WARP = Map.of(
            "crimson_boots", 1,
            "crimson_robe_chest", 1,
            "crimson_robe_helm", 1,
            "crimson_robe_legs", 1
    );

    private WarpGearManager() {
    }

    public static int getWarpFromGear(ServerPlayer player) {
        int warp = getWarpFromStack(player.getMainHandItem(), player);
        for (ItemStack armor : player.getInventory().armor) {
            warp += getWarpFromStack(armor, player);
        }
        // TODO(port): Add curios/baubles-equivalent slots once accessory support is ported.
        return warp;
    }

    public static int getWarpFromStack(ItemStack stack, ServerPlayer player) {
        if (stack.isEmpty()) {
            return 0;
        }

        int warp = 0;
        if (stack.getItem() instanceof WarpingGearItem warpingGearItem) {
            warp += warpingGearItem.getWarp(stack, player);
        } else {
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
            if (itemId != null && Thaumcraft.MODID.equals(itemId.getNamespace())) {
                warp += FALLBACK_ITEM_WARP.getOrDefault(itemId.getPath(), 0);
            }
        }

        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(LEGACY_WARP_NBT_TAG, Tag.TAG_ANY_NUMERIC)) {
            warp += tag.getInt(LEGACY_WARP_NBT_TAG);
        }

        return warp;
    }
}
