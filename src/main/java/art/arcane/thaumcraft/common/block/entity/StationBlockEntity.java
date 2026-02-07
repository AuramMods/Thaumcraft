package art.arcane.thaumcraft.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class StationBlockEntity extends BlockEntity implements Container, MenuProvider, TickingStationBlockEntity {

    private NonNullList<ItemStack> items;
    private final int slotCount;
    private final Component displayName;
    private int serverTicks;
    private int activity;
    private final ContainerData menuData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> StationBlockEntity.this.serverTicks;
                case 1 -> StationBlockEntity.this.activity;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> StationBlockEntity.this.serverTicks = value;
                case 1 -> StationBlockEntity.this.activity = value;
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    protected StationBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, String containerTranslationKey, int slotCount) {
        super(type, pos, state);
        this.slotCount = slotCount;
        this.items = NonNullList.withSize(slotCount, ItemStack.EMPTY);
        this.displayName = Component.translatable(containerTranslationKey);
    }

    @Override
    public void serverTick() {
        incrementServerTicks();
        setActivityValue((getActivityValue() + 1) % 200);
        if ((getServerTicksValue() % 20) == 0) {
            setChanged();
        }
    }

    protected int getServerTicksValue() {
        return this.serverTicks;
    }

    protected void setServerTicksValue(int value) {
        this.serverTicks = value;
    }

    protected void incrementServerTicks() {
        this.serverTicks++;
    }

    protected int getActivityValue() {
        return this.activity;
    }

    protected void setActivityValue(int value) {
        this.activity = value;
    }

    public ContainerData getMenuData() {
        return this.menuData;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putInt("server_ticks", this.serverTicks);
        tag.putInt("activity", this.activity);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.slotCount, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
        this.serverTicks = tag.getInt("server_ticks");
        this.activity = tag.getInt("activity");
    }

    @Override
    public Component getDisplayName() {
        return this.displayName;
    }

    @Override
    public abstract AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player);

    @Override
    public int getContainerSize() {
        return this.slotCount;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.items) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(this.items, slot, amount);
        if (!result.isEmpty()) {
            setChanged();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.items = NonNullList.withSize(this.slotCount, ItemStack.EMPTY);
        setChanged();
    }
}
