package art.arcane.thaumcraft.common.item;

import art.arcane.thaumcraft.common.progression.PlayerKnowledgeManager;
import art.arcane.thaumcraft.common.registry.ModBlocks;
import art.arcane.thaumcraft.common.registry.ModMobEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SanitySoapItem extends Item {
    // TODO(port): Port legacy particle/sound timing and full warp-ward interactions from ItemSanitySoap.

    private static final int USE_DURATION_TICKS = 200;

    public SanitySoapItem() {
        super(new Properties().stacksTo(16));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return USE_DURATION_TICKS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, net.minecraft.world.entity.player.Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, net.minecraft.world.entity.LivingEntity livingEntity) {
        if (!(livingEntity instanceof ServerPlayer player)) {
            return stack;
        }

        float chance = 0.33F;
        if (player.hasEffect(ModMobEffects.WARP_WARD.get())) {
            chance += 0.25F;
        }

        BlockState standingIn = level.getBlockState(player.blockPosition());
        if (standingIn.is(ModBlocks.get("purifying_fluid").get())) {
            chance += 0.25F;
        }

        int temporary = PlayerKnowledgeManager.getWarp(player, PlayerKnowledgeManager.WarpType.TEMPORARY);
        if (temporary > 0) {
            PlayerKnowledgeManager.addWarp(player, PlayerKnowledgeManager.WarpType.TEMPORARY, -temporary);
        }

        int normal = PlayerKnowledgeManager.getWarp(player, PlayerKnowledgeManager.WarpType.NORMAL);
        if (normal > 0 && level.getRandom().nextFloat() < chance) {
            PlayerKnowledgeManager.addWarp(player, PlayerKnowledgeManager.WarpType.NORMAL, -1);
        }

        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 0.35F, 1.0F);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return stack;
    }
}
