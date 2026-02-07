package art.arcane.thaumcraft.common.block.entity;

import art.arcane.thaumcraft.common.menu.ResearchTableMenu;
import art.arcane.thaumcraft.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ResearchTableBlockEntity extends StationBlockEntity {

    public ResearchTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RESEARCH_TABLE.get(), pos, state, "container.thaumcraft.research_table", 2);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ResearchTableMenu(containerId, playerInventory, this);
    }
}
