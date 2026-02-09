package art.arcane.thaumcraft.common.item;

import art.arcane.thaumcraft.common.progression.PlayerKnowledgeManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class SanityCheckerItem extends Item {
    // TODO(port): Replace chat-based debug readout with legacy sanity HUD rendering path tied to held item state.

    public SanityCheckerItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, net.minecraft.world.entity.player.Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

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
}
