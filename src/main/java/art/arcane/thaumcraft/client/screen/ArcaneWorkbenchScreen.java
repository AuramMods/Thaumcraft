package art.arcane.thaumcraft.client.screen;

import art.arcane.thaumcraft.common.menu.ArcaneWorkbenchMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ArcaneWorkbenchScreen extends StationScreen<ArcaneWorkbenchMenu> {

    public ArcaneWorkbenchScreen(ArcaneWorkbenchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
}
