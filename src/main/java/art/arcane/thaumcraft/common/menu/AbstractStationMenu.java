package art.arcane.thaumcraft.common.menu;

import art.arcane.thaumcraft.common.block.StationBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class AbstractStationMenu extends AbstractContainerMenu {

    private static final int PLAYER_INV_SLOT_COUNT = 27;
    private static final int HOTBAR_SLOT_COUNT = 9;

    private final Level level;
    private final BlockPos blockPos;
    private final Container container;
    private final ContainerData data;
    private final int stationSlotCount;

    protected AbstractStationMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, Level level, BlockPos blockPos,
                                  Container container, ContainerData data, int stationSlotCount) {
        super(menuType, containerId);
        checkContainerSize(container, stationSlotCount);
        checkContainerDataCount(data, 2);

        this.level = level;
        this.blockPos = blockPos;
        this.container = container;
        this.data = data;
        this.stationSlotCount = stationSlotCount;

        addStationSlots(container);
        addPlayerSlots(playerInventory, getPlayerInventoryStartY());
        addDataSlots(data);
        this.container.startOpen(playerInventory.player);
    }

    protected abstract void addStationSlots(Container container);

    protected int getPlayerInventoryStartY() {
        return 84;
    }

    private void addPlayerSlots(Inventory playerInventory, int startY) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, startY + row * 18));
            }
        }

        int hotbarY = startY + 58;
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, hotbarY));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level == null || !(this.level.getBlockState(this.blockPos).getBlock() instanceof StationBlock)) {
            return false;
        }

        return player.distanceToSqr(this.blockPos.getX() + 0.5D, this.blockPos.getY() + 0.5D, this.blockPos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot.hasItem()) {
            ItemStack source = slot.getItem();
            result = source.copy();

            int playerInventoryStart = this.stationSlotCount;
            int playerInventoryEnd = this.stationSlotCount + PLAYER_INV_SLOT_COUNT + HOTBAR_SLOT_COUNT;

            if (slotIndex < this.stationSlotCount) {
                if (!this.moveItemStackTo(source, playerInventoryStart, playerInventoryEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveFromPlayerToStation(source)) {
                return ItemStack.EMPTY;
            }

            if (source.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (source.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, source);
        }

        return result;
    }

    protected boolean moveFromPlayerToStation(ItemStack stack) {
        return this.moveItemStackTo(stack, 0, this.stationSlotCount, false);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    public int getServerTicks() {
        return this.data.get(0);
    }

    public int getActivity() {
        return this.data.get(1);
    }
}
