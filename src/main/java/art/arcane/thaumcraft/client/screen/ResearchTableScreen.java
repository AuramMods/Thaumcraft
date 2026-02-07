package art.arcane.thaumcraft.client.screen;

import art.arcane.thaumcraft.common.menu.ResearchTableMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ResearchTableScreen extends StationScreen<ResearchTableMenu> {

    public ResearchTableScreen(ResearchTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
}
