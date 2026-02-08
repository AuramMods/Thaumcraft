package art.arcane.thaumcraft.common.item;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.registry.ModBlocks;
import art.arcane.thaumcraft.common.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ThaumcraftBucketEvents {
    // TODO(port): Keep this event bridge only until Forge fluid stack migration is complete.
    // TODO(port): when proper fluids land, switch to fluid pickup handling and source-only checks from fluid states.

    private ThaumcraftBucketEvents() {
    }

    @SubscribeEvent
    public static void onFillBucket(FillBucketEvent event) {
        if (!event.getEmptyBucket().is(Items.BUCKET)) {
            return;
        }
        if (!(event.getTarget() instanceof BlockHitResult blockHitResult)) {
            return;
        }

        Level level = event.getLevel();
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);

        if (state.is(ModBlocks.get("purifying_fluid").get())) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            ItemStack bucket = new ItemStack(ModItems.ITEMS_BY_ID.get("bucket_pure").get());
            event.setFilledBucket(bucket);
            event.setResult(Event.Result.ALLOW);
            return;
        }

        if (state.is(ModBlocks.get("liquid_death").get())) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            ItemStack bucket = new ItemStack(ModItems.ITEMS_BY_ID.get("bucket_death").get());
            event.setFilledBucket(bucket);
            event.setResult(Event.Result.ALLOW);
        }
    }
}
