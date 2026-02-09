package art.arcane.thaumcraft.client;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.item.SanityCheckerItem;
import art.arcane.thaumcraft.common.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ThaumcraftHudEvents {
    // TODO(port): Replace this text/bar sanity HUD with legacy texture-based UI + animated layers.
    // TODO(port): consume server-synced warp capability values directly instead of stack NBT bridge.

    private static final int HUD_X = 8;
    private static final int HUD_Y = 8;
    private static final int HUD_WIDTH = 110;
    private static final int HUD_HEIGHT = 58;
    private static final int BAR_WIDTH = 74;

    private ThaumcraftHudEvents() {
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (!event.getOverlay().id().equals(VanillaGuiOverlay.HOTBAR.id())) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) {
            return;
        }

        Item sanityChecker = ModItems.ITEMS_BY_ID.get("sanity_checker").get();
        ItemStack held = ItemStack.EMPTY;
        if (mc.player.getMainHandItem().is(sanityChecker)) {
            held = mc.player.getMainHandItem();
        } else if (mc.player.getOffhandItem().is(sanityChecker)) {
            held = mc.player.getOffhandItem();
        }

        if (held.isEmpty()) {
            return;
        }

        int permanent = held.getOrCreateTag().getInt(SanityCheckerItem.TAG_WARP_PERMANENT);
        int normal = held.getOrCreateTag().getInt(SanityCheckerItem.TAG_WARP_NORMAL);
        int temporary = held.getOrCreateTag().getInt(SanityCheckerItem.TAG_WARP_TEMPORARY);
        int total = held.getOrCreateTag().getInt(SanityCheckerItem.TAG_WARP_TOTAL);
        int max = Math.max(100, total);

        GuiGraphics gui = event.getGuiGraphics();
        Font font = mc.font;

        gui.fill(HUD_X, HUD_Y, HUD_X + HUD_WIDTH, HUD_Y + HUD_HEIGHT, 0xA0101010);
        gui.fill(HUD_X, HUD_Y, HUD_X + HUD_WIDTH, HUD_Y + 10, 0xA020102A);
        gui.drawString(font, "Sanity", HUD_X + 4, HUD_Y + 2, 0xE5D8C8, false);

        drawWarpRow(gui, font, HUD_X + 4, HUD_Y + 14, "Permanent", permanent, max, 0x7A2A8A);
        drawWarpRow(gui, font, HUD_X + 4, HUD_Y + 27, "Normal", normal, max, 0xA03AB2);
        drawWarpRow(gui, font, HUD_X + 4, HUD_Y + 40, "Temporary", temporary, max, 0xCF67D9);
    }

    private static void drawWarpRow(GuiGraphics gui, Font font, int x, int y, String label, int value, int max, int color) {
        gui.drawString(font, label, x, y, 0xD2C9BE, false);
        gui.fill(x + 34, y + 2, x + 34 + BAR_WIDTH, y + 9, 0x90202020);
        int filled = Math.min(BAR_WIDTH, Math.max(0, (int)Math.round((value / (double) max) * BAR_WIDTH)));
        if (filled > 0) {
            gui.fill(x + 34, y + 2, x + 34 + filled, y + 9, 0xFF000000 | color);
        }
        gui.drawString(font, Integer.toString(value), x + 34 + BAR_WIDTH + 4, y, 0xD2C9BE, false);
    }
}
