package art.arcane.thaumcraft.common.menu;

import art.arcane.thaumcraft.common.block.entity.InfusionMatrixBlockEntity;
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

public class InfusionMatrixMenu extends AbstractStationMenu {
    // TODO(port): extend menu with infusion state visibility (selected recipe, instability, essentia demands, progress events) for debugging and player UX parity.

    private static final int SLOT_COUNT = 1;

    public InfusionMatrixMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, playerInventory.player.level(), extraData.readBlockPos(), new SimpleContainer(SLOT_COUNT), new SimpleContainerData(2));
    }

    public InfusionMatrixMenu(int containerId, Inventory playerInventory, InfusionMatrixBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity, blockEntity.getMenuData());
    }

    private InfusionMatrixMenu(int containerId, Inventory playerInventory, Level level, BlockPos blockPos, Container container, ContainerData data) {
        super(ModMenus.INFUSION_MATRIX.get(), containerId, playerInventory, level, blockPos, container, data, SLOT_COUNT);
    }

    @Override
    protected void addStationSlots(Container container) {
        this.addSlot(new Slot(container, InfusionMatrixBlockEntity.MATRIX_SLOT, 80, 20));
    }
}
