package art.arcane.thaumcraft.common.item;

import art.arcane.thaumcraft.common.progression.PlayerKnowledgeManager;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class SanityCheckerItem extends Item {
    // TODO(port): Replace chat-based debug readout with legacy sanity HUD rendering path tied to held item state.
    // TODO(port): Replace stack-NBT client sync bridge with dedicated capability/network sync once warp packets are in place.

    public static final String TAG_WARP_PERMANENT = "thaumcraft_warp_permanent";
    public static final String TAG_WARP_NORMAL = "thaumcraft_warp_normal";
    public static final String TAG_WARP_TEMPORARY = "thaumcraft_warp_temporary";
    public static final String TAG_WARP_TOTAL = "thaumcraft_warp_total";

    public SanityCheckerItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, net.minecraft.world.entity.player.Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        updateWarpTag(stack, serverPlayer);
        PlayerKnowledgeManager.WarpSnapshot warp = PlayerKnowledgeManager.getWarpSnapshot(serverPlayer);
        serverPlayer.displayClientMessage(
                Component.literal("Warp - Permanent: " + warp.permanent()
                        + ", Normal: " + warp.normal()
                        + ", Temporary: " + warp.temporary()
                        + ", Total: " + warp.total()),
                true
        );
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }

        if ((player.tickCount % 20) != 0) {
            return;
        }

        if (!isHeldByPlayer(player, stack)) {
            return;
        }

        updateWarpTag(stack, player);
    }

    private static boolean isHeldByPlayer(Player player, ItemStack stack) {
        return player.getMainHandItem() == stack || player.getOffhandItem() == stack;
    }

    private static void updateWarpTag(ItemStack stack, ServerPlayer player) {
        PlayerKnowledgeManager.WarpSnapshot warp = PlayerKnowledgeManager.getWarpSnapshot(player);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(TAG_WARP_PERMANENT, warp.permanent());
        tag.putInt(TAG_WARP_NORMAL, warp.normal());
        tag.putInt(TAG_WARP_TEMPORARY, warp.temporary());
        tag.putInt(TAG_WARP_TOTAL, warp.total());
    }
}
