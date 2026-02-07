package art.arcane.thaumcraft.common.menu;

import art.arcane.thaumcraft.common.block.entity.ResearchTableBlockEntity;
import art.arcane.thaumcraft.common.registry.ModMenus;
import art.arcane.thaumcraft.common.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ResearchTableMenu extends AbstractStationMenu {

    private static final int SLOT_COUNT = 2;

    public ResearchTableMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, playerInventory.player.level(), extraData.readBlockPos(), new SimpleContainer(SLOT_COUNT), new SimpleContainerData(2));
    }

    public ResearchTableMenu(int containerId, Inventory playerInventory, ResearchTableBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity, blockEntity.getMenuData());
    }

    private ResearchTableMenu(int containerId, Inventory playerInventory, Level level, BlockPos blockPos, Container container, ContainerData data) {
        super(ModMenus.RESEARCH_TABLE.get(), containerId, playerInventory, level, blockPos, container, data, SLOT_COUNT);
    }

    @Override
    protected void addStationSlots(Container container) {
        this.addSlot(new Slot(container, 0, 53, 20) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isScribingTools(stack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        this.addSlot(new Slot(container, 1, 107, 20) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.PAPER);
            }
        });
    }

    private static boolean isScribingTools(ItemStack stack) {
        if (!ModItems.ITEMS_BY_ID.containsKey("scribing_tools")) {
            return false;
        }

        return stack.is(ModItems.ITEMS_BY_ID.get("scribing_tools").get());
    }
}
