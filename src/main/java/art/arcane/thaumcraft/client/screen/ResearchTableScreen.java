package art.arcane.thaumcraft.client.screen;

import art.arcane.thaumcraft.common.menu.ResearchTableMenu;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ResearchTableScreen extends StationScreen<ResearchTableMenu> {
    private Button draftTheoryButton;
    private Button completeTheoryButton;

    public ResearchTableScreen(ResearchTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.draftTheoryButton = this.addRenderableWidget(
                Button.builder(Component.literal("Draft"), button -> {
                            if (this.minecraft == null || this.minecraft.gameMode == null) {
                                return;
                            }
                            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, ResearchTableMenu.BUTTON_DRAFT_THEORY);
                        })
                        .pos(this.leftPos + 116, this.topPos + 48)
                        .size(50, 16)
                        .tooltip(Tooltip.create(Component.literal("Consume paper + observation to gain theory")))
                        .build()
        );
        this.completeTheoryButton = this.addRenderableWidget(
                Button.builder(Component.literal("Complete"), button -> {
                            if (this.minecraft == null || this.minecraft.gameMode == null) {
                                return;
                            }
                            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, ResearchTableMenu.BUTTON_COMPLETE_THEORY);
                        })
                        .pos(this.leftPos + 116, this.topPos + 66)
                        .size(50, 16)
                        .tooltip(Tooltip.create(Component.literal("Finish current theory once inspiration is spent")))
                        .build()
        );
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (this.draftTheoryButton != null) {
            boolean hasScribing = this.menu.getSlot(0).hasItem();
            boolean hasPaper = this.menu.getSlot(1).hasItem();
            this.draftTheoryButton.active = hasScribing && hasPaper;
        }
        if (this.completeTheoryButton != null) {
            this.completeTheoryButton.active = true;
        }
    }
}
