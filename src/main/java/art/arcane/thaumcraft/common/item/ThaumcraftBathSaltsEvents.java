package art.arcane.thaumcraft.common.item;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.registry.ModBlocks;
import art.arcane.thaumcraft.common.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ThaumcraftBathSaltsEvents {
    // TODO(port): Legacy uses ItemBathSalts class identity checks; migrate this to dedicated item class once placeholders are replaced.
    // TODO(port): Extend with full Spa mixing/spreading logic once Spa block entity flow is ported.

    private static final int FULL_CAULDRON_LEVEL = 3;

    private ThaumcraftBathSaltsEvents() {
    }

    @SubscribeEvent
    public static void onItemExpire(ItemExpireEvent event) {
        ItemStack stack = event.getEntity().getItem();
        if (stack.isEmpty() || !stack.is(ModItems.ITEMS_BY_ID.get("bath_salts").get())) {
            return;
        }

        Level level = event.getEntity().level();
        if (level.isClientSide()) {
            return;
        }

        BlockPos pos = event.getEntity().blockPosition();
        BlockState state = level.getBlockState(pos);
        if (!state.is(Blocks.WATER_CAULDRON)) {
            return;
        }
        if (!state.hasProperty(LayeredCauldronBlock.LEVEL) || state.getValue(LayeredCauldronBlock.LEVEL) < FULL_CAULDRON_LEVEL) {
            return;
        }

        level.setBlockAndUpdate(pos, ModBlocks.get("purifying_fluid").get().defaultBlockState());
    }
}
