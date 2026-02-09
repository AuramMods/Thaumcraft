package art.arcane.thaumcraft.common.menu;

import art.arcane.thaumcraft.common.block.entity.ResearchTableBlockEntity;
import art.arcane.thaumcraft.common.registry.ModMenus;
import art.arcane.thaumcraft.common.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ResearchTableMenu extends AbstractStationMenu {
    // TODO(port): replace this simplified draft/complete action bridge with full legacy theorycraft card actions and detailed sync state.

    private static final int SLOT_COUNT = 2;
    public static final int BUTTON_DRAFT_THEORY = 1;
    public static final int BUTTON_COMPLETE_THEORY = 2;
    private final ResearchTableBlockEntity blockEntity;

    public ResearchTableMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, playerInventory.player.level(), extraData.readBlockPos(), new SimpleContainer(SLOT_COUNT), new SimpleContainerData(2));
    }

    public ResearchTableMenu(int containerId, Inventory playerInventory, ResearchTableBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity, blockEntity.getMenuData());
    }

    private ResearchTableMenu(int containerId, Inventory playerInventory, Level level, BlockPos blockPos, Container container, ContainerData data) {
        super(ModMenus.RESEARCH_TABLE.get(), containerId, playerInventory, level, blockPos, container, data, SLOT_COUNT);
        this.blockEntity = container instanceof ResearchTableBlockEntity researchTable ? researchTable : null;
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

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return false;
        }
        if (this.blockEntity == null) {
            return false;
        }
        return switch (id) {
            case BUTTON_DRAFT_THEORY -> this.blockEntity.draftTheory(serverPlayer);
            case BUTTON_COMPLETE_THEORY -> this.blockEntity.completeTheory(serverPlayer);
            default -> false;
        };
    }
}
