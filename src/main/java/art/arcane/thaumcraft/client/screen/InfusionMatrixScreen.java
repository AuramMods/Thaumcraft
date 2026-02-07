package art.arcane.thaumcraft.client.screen;

import art.arcane.thaumcraft.common.menu.InfusionMatrixMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class InfusionMatrixScreen extends StationScreen<InfusionMatrixMenu> {

    public InfusionMatrixScreen(InfusionMatrixMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
}
