package art.arcane.thaumcraft.common.menu;

import art.arcane.thaumcraft.common.block.entity.ArcaneWorkbenchBlockEntity;
import art.arcane.thaumcraft.common.registry.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ArcaneWorkbenchMenu extends AbstractStationMenu {
    // TODO(port): wire menu-side hints/validation for final arcane recipe parity (research lock messaging and precise vis/crystal requirements per recipe type).

    private static final int SLOT_COUNT = ArcaneWorkbenchBlockEntity.TOTAL_SLOTS;

    public ArcaneWorkbenchMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, playerInventory.player.level(), extraData.readBlockPos(), new SimpleContainer(SLOT_COUNT), new SimpleContainerData(2));
    }

    public ArcaneWorkbenchMenu(int containerId, Inventory playerInventory, ArcaneWorkbenchBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity, blockEntity.getMenuData());
    }

    private ArcaneWorkbenchMenu(int containerId, Inventory playerInventory, Level level, BlockPos blockPos, Container container, ContainerData data) {
        super(ModMenus.ARCANE_WORKBENCH.get(), containerId, playerInventory, level, blockPos, container, data, SLOT_COUNT);
    }

    @Override
    protected void addStationSlots(Container container) {
        ArcaneWorkbenchBlockEntity blockEntity = container instanceof ArcaneWorkbenchBlockEntity workbench ? workbench : null;

        this.addSlot(new Slot(container, ArcaneWorkbenchBlockEntity.OUTPUT_SLOT, 124, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public boolean mayPickup(Player player) {
                return blockEntity == null || blockEntity.canTakeCurrentResult(player);
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                if (blockEntity != null) {
                    blockEntity.onResultTaken(player);
                }
            }
        });

        this.addSlot(new Slot(container, ArcaneWorkbenchBlockEntity.CRYSTAL_SLOT, 8, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ArcaneWorkbenchBlockEntity.isCrystalItem(stack);
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int slot = ArcaneWorkbenchBlockEntity.INPUT_SLOT_START + col + row * 3;
                this.addSlot(new Slot(container, slot, 30 + col * 18, 17 + row * 18));
            }
        }
    }

    @Override
    protected boolean moveFromPlayerToStation(ItemStack stack) {
        if (ArcaneWorkbenchBlockEntity.isCrystalItem(stack) && this.moveItemStackTo(
                stack,
                ArcaneWorkbenchBlockEntity.CRYSTAL_SLOT,
                ArcaneWorkbenchBlockEntity.CRYSTAL_SLOT + 1,
                false
        )) {
            return true;
        }

        return this.moveItemStackTo(
                stack,
                ArcaneWorkbenchBlockEntity.INPUT_SLOT_START,
                ArcaneWorkbenchBlockEntity.INPUT_SLOT_START + ArcaneWorkbenchBlockEntity.INPUT_SLOT_COUNT,
                false
        );
    }
}
