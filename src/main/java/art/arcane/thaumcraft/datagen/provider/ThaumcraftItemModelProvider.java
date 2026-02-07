package art.arcane.thaumcraft.datagen.provider;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Map;

public class ThaumcraftItemModelProvider extends ItemModelProvider {

    private final ExistingFileHelper existingFileHelper;
    private static final Map<String, String> ITEM_TEXTURE_ALIASES = Map.ofEntries(
            Map.entry("baubles", "item/ring_mundane"),
            Map.entry("caster_basic", "item/creative_caster"),
            Map.entry("charm_verdant", "item/verdant_charm"),
            Map.entry("chunks", "item/chunk_beef"),
            Map.entry("clusters", "item/cluster_iron"),
            Map.entry("creative_flux_sponge", "item/flux_sponge"),
            Map.entry("creative_placer", "item/creative_obelisk"),
            Map.entry("curio", "item/curio_arcane"),
            Map.entry("focus", "item/focus_blank"),
            Map.entry("gear", "item/gear_brass"),
            Map.entry("golem_placer", "item/golem"),
            Map.entry("ingots", "item/ingot_thaumium"),
            Map.entry("loot_bag", "item/loot_bag_common"),
            Map.entry("mind", "item/mind_clockwork"),
            Map.entry("modules", "item/module_vision"),
            Map.entry("nuggets", "item/nugget_thaumium"),
            Map.entry("plate", "item/plate_brass"),
            Map.entry("seals", "item/seals/seal_blank"),
            Map.entry("thaumometer", "item/scanscreen"),
            Map.entry("triple_meat_treat", "item/tripletreat"),
            Map.entry("turret_placer", "item/turret_basic")
    );

    public ThaumcraftItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Thaumcraft.MODID, existingFileHelper);
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    protected void registerModels() {
        for (String id : ModItems.ITEMS_BY_ID.keySet()) {
            ResourceLocation texture = resolveItemTexture(id);
            withExistingParent(id, mcLoc("item/generated"))
                    .texture("layer0", texture);
        }
    }

    private ResourceLocation resolveItemTexture(String id) {
        ResourceLocation direct = resolveTexture("item/" + id);
        if (direct != null) {
            return direct;
        }

        String aliasPath = ITEM_TEXTURE_ALIASES.get(id);
        if (aliasPath != null) {
            ResourceLocation aliased = resolveTexture(aliasPath);
            if (aliased != null) {
                return aliased;
            }
        }

        return mcLoc("item/paper");
    }

    private ResourceLocation resolveTexture(String path) {
        ResourceLocation candidate = modLoc(path);
        return existingFileHelper.exists(candidate, PackType.CLIENT_RESOURCES, ".png", "textures") ? candidate : null;
    }
}
