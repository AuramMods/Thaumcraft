package art.arcane.thaumcraft.common.menu;

import art.arcane.thaumcraft.common.block.entity.CrucibleBlockEntity;
import art.arcane.thaumcraft.common.registry.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;

public class CrucibleMenu extends AbstractStationMenu {
    // TODO(port): either remove this temporary container path or align it with final crucible UX once in-world-only interactions are finalized.

    private static final int SLOT_COUNT = 1;

    public CrucibleMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, playerInventory.player.level(), extraData.readBlockPos(), new SimpleContainer(SLOT_COUNT), new SimpleContainerData(2));
    }

    public CrucibleMenu(int containerId, Inventory playerInventory, CrucibleBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity, blockEntity.getMenuData());
    }

    private CrucibleMenu(int containerId, Inventory playerInventory, Level level, BlockPos blockPos, Container container, ContainerData data) {
        super(ModMenus.CRUCIBLE.get(), containerId, playerInventory, level, blockPos, container, data, SLOT_COUNT);
    }

    @Override
    protected void addStationSlots(Container container) {
        this.addSlot(new Slot(container, 0, 80, 20) {
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
    }
}
