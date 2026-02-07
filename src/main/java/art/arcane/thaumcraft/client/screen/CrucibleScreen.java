package art.arcane.thaumcraft.client.screen;

import art.arcane.thaumcraft.common.menu.CrucibleMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class CrucibleScreen extends StationScreen<CrucibleMenu> {

    public CrucibleScreen(CrucibleMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
}
