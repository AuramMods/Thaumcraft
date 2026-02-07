package art.arcane.thaumcraft.client;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.aspect.AspectList;
import art.arcane.thaumcraft.common.aspect.AspectRegistry;
import art.arcane.thaumcraft.common.aspect.AspectType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ThaumcraftTooltipEvents {
    // TODO(port): Replace text-only tooltip rendering with icon glyphs and layout parity from legacy client draw helpers.
    // TODO(port): Gate full tooltip visibility behind thaumometer/goggles research progress once the knowledge UI loop is ported.

    private static final int HEADER_COLOR = 0x8F7F68;
    private static final int PREFIX_COLOR = 0x9A9A9A;
    private static final int AMOUNT_COLOR = 0xD0D0D0;

    private ThaumcraftTooltipEvents() {
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        AspectList aspects = minecraft.level != null
                ? AspectRegistry.getAspects(minecraft.level, stack)
                : AspectRegistry.getAspects(stack);
        if (aspects.isEmpty()) {
            return;
        }

        event.getToolTip().add(Component.empty());
        event.getToolTip().add(Component.literal("Aspects").withStyle(style -> style.withColor(HEADER_COLOR)));
        for (Map.Entry<AspectType, Integer> entry : aspects.asMap().entrySet()) {
            event.getToolTip().add(formatAspectLine(entry.getKey(), entry.getValue()));
        }
    }

    private static Component formatAspectLine(AspectType aspect, int amount) {
        return Component.literal(" - ").withStyle(style -> style.withColor(PREFIX_COLOR))
                .append(Component.literal(Integer.toString(amount)).withStyle(style -> style.withColor(AMOUNT_COLOR)))
                .append(Component.literal(" "))
                .append(Component.literal(formatAspectName(aspect)).withStyle(style -> style.withColor(aspect.getColor())));
    }

    private static String formatAspectName(AspectType aspect) {
        String tag = aspect.getTag();
        if (tag.isEmpty()) {
            return aspect.name().toLowerCase();
        }
        return Character.toUpperCase(tag.charAt(0)) + tag.substring(1);
    }
}
