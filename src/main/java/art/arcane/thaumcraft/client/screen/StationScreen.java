package art.arcane.thaumcraft.client.screen;

import art.arcane.thaumcraft.common.menu.AbstractStationMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class StationScreen<T extends AbstractStationMenu> extends AbstractContainerScreen<T> {

    public StationScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int left = this.leftPos;
        int top = this.topPos;

        guiGraphics.fill(left, top, left + this.imageWidth, top + this.imageHeight, 0xCC191919);
        guiGraphics.fill(left + 7, top + 17, left + 169, top + 71, 0xCC2A2A2A);
        guiGraphics.fill(left + 79, top + 20, left + 97, top + 38, 0xFF575757);

        int progressPixels = Math.min(104, this.menu.getActivity() / 2);
        guiGraphics.fill(left + 36, top + 56, left + 36 + progressPixels, top + 62, 0xFF68C26D);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 8, 6, 0xFFFFFF, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 0xD0D0D0, false);
        guiGraphics.drawString(this.font, Component.literal("Ticks: " + this.menu.getServerTicks()), 8, 20, 0xC9C9C9, false);
        guiGraphics.drawString(this.font, Component.literal("Activity: " + this.menu.getActivity()), 8, 32, 0xC9C9C9, false);
    }
}
