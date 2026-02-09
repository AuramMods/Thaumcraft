package art.arcane.thaumcraft.datagen.provider;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.registry.ModBlocks;
import art.arcane.thaumcraft.common.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ThaumcraftLanguageProvider extends LanguageProvider {

    public ThaumcraftLanguageProvider(PackOutput output, String locale) {
        super(output, Thaumcraft.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.thaumcraft.main", "Thaumcraft");
        add("container.thaumcraft.arcane_workbench", "Arcane Workbench");
        add("container.thaumcraft.research_table", "Research Table");
        add("container.thaumcraft.crucible", "Crucible");
        add("container.thaumcraft.infusion_matrix", "Infusion Matrix");
        add("enchantment.special.sapgreat", "Greatwood Sap");

        for (String id : ModBlocks.BLOCKS_BY_ID.keySet()) {
            add("block.thaumcraft." + id, toTitle(id));
        }

        for (String id : ModItems.ITEMS_BY_ID.keySet()) {
            add("item.thaumcraft." + id, getItemTitle(id));
        }
    }

    private static String getItemTitle(String id) {
        return switch (id) {
            case "nugget_quartz" -> "Quartz Sliver";
            case "quartz_sliver" -> "Quartz Sliver (Legacy)";
            default -> toTitle(id);
        };
    }

    private static String toTitle(String value) {
        String[] parts = value.split("_");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                builder.append(part.substring(1));
            }
        }
        return builder.toString();
    }
}
