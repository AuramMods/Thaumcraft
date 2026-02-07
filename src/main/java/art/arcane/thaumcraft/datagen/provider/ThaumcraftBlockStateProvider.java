package art.arcane.thaumcraft.datagen.provider;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.registry.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Map;
import java.util.Set;

public class ThaumcraftBlockStateProvider extends BlockStateProvider {

    private final ExistingFileHelper existingFileHelper;
    private static final Set<String> CUSTOM_MODEL_BLOCKS = Set.of(
            "pedestal",
            "arcane_workbench",
            "crucible",
            "infusion_matrix",
            "jar_normal",
            "jar_void",
            "jar_brain"
    );
    private static final Map<String, String> BLOCK_TEXTURE_ALIASES = Map.ofEntries(
            Map.entry("activator_rail", "block/rail_activator"),
            Map.entry("alembic", "block/alembic_normal"),
            Map.entry("ancient_stairs", "block/ancient_stone_1"),
            Map.entry("arcane_brick_stairs", "block/arcane_brick_stone"),
            Map.entry("arcane_ear", "block/arcane_ear_off"),
            Map.entry("arcane_stairs", "block/arcane_stone_1"),
            Map.entry("arcane_workbench", "block/arcane_workbench_side"),
            Map.entry("banner", "block/base_wood"),
            Map.entry("barrier", "block/paving_stone_barrier"),
            Map.entry("crucible", "block/crucible_side"),
            Map.entry("crystal_aer", "block/crystal"),
            Map.entry("crystal_aqua", "block/crystal"),
            Map.entry("crystal_ignis", "block/crystal"),
            Map.entry("crystal_ordo", "block/crystal"),
            Map.entry("crystal_perditio", "block/crystal"),
            Map.entry("crystal_terra", "block/crystal"),
            Map.entry("crystal_vitium", "block/crystal"),
            Map.entry("dioptra", "block/dioptra_side_off"),
            Map.entry("effect", "block/empty"),
            Map.entry("essentia_output", "block/essentia_input"),
            Map.entry("flesh_block", "block/flesh"),
            Map.entry("golem_builder", "block/golembuilder"),
            Map.entry("grass_ambient", "block/greatwood_leaves"),
            Map.entry("greatwood_stairs", "block/greatwood_plank"),
            Map.entry("hole", "block/empty"),
            Map.entry("hungry_chest", "block/base_wood"),
            Map.entry("infusion_matrix", "block/infuser_normal"),
            Map.entry("jar_brain", "block/jar_side"),
            Map.entry("jar_normal", "block/jar_side"),
            Map.entry("jar_void", "block/jar_side_void"),
            Map.entry("lamp_arcane", "block/lamp_off"),
            Map.entry("lamp_fertility", "block/lamp_fert_off"),
            Map.entry("lamp_growth", "block/lamp_grow_off"),
            Map.entry("leaf", "block/greatwood_leaves"),
            Map.entry("levitator", "block/levitator_side_off"),
            Map.entry("liquid_death", "block/flux_goo"),
            Map.entry("log", "block/log_greatwood"),
            Map.entry("loot_crate", "block/loot_crate_side_0"),
            Map.entry("loot_urn", "block/loot_urn_side_0"),
            Map.entry("metal", "block/thaumium_metal"),
            Map.entry("mirror", "block/mirrorpane"),
            Map.entry("mirror_essentia", "block/mirrorpanetrans"),
            Map.entry("paving_stone", "block/paving_stone_travel"),
            Map.entry("pedestal", "block/pedestal_side"),
            Map.entry("pillar", "block/pillar_normal"),
            Map.entry("placeholder", "block/empty"),
            Map.entry("plank", "block/greatwood_plank"),
            Map.entry("purifying_fluid", "block/flux_goo"),
            Map.entry("recharge_pedestal", "block/recharge_pedestal_side"),
            Map.entry("redstone_relay", "block/redstone_relay_off"),
            Map.entry("research_table", "block/research_table_side"),
            Map.entry("sapling", "block/greatwood_sapling"),
            Map.entry("silverwood_stairs", "block/silverwood_plank"),
            Map.entry("slab_stone", "block/base_stone"),
            Map.entry("slab_stone_double", "block/base_stone"),
            Map.entry("slab_wood", "block/base_wood"),
            Map.entry("slab_wood_double", "block/base_wood"),
            Map.entry("smelter_basic", "block/smelter_basic_side"),
            Map.entry("smelter_thaumium", "block/smelter_thaumium_side"),
            Map.entry("smelter_void", "block/smelter_void_side"),
            Map.entry("spa", "block/spa_side"),
            Map.entry("stone", "block/base_stone"),
            Map.entry("table_stone", "block/table_stone_top"),
            Map.entry("table_wood", "block/table_wood_top"),
            Map.entry("taint", "block/taint_rock"),
            Map.entry("taint_feature", "block/taint_geyser_side"),
            Map.entry("taint_fibre", "block/taint_fibres"),
            Map.entry("taint_log", "block/log_taintwood_0"),
            Map.entry("thaumatorium_top", "block/thaumatorium"),
            Map.entry("translucent", "block/animatedglow"),
            Map.entry("tube_buffer", "block/tube"),
            Map.entry("tube_filter", "block/tube"),
            Map.entry("tube_oneway", "block/tube"),
            Map.entry("tube_restrict", "block/tube"),
            Map.entry("tube_valve", "block/tube"),
            Map.entry("wand_workbench", "block/wand_workbench_side")
    );

    public ThaumcraftBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Thaumcraft.MODID, exFileHelper);
        this.existingFileHelper = exFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        for (String id : ModBlocks.BLOCKS_BY_ID.keySet()) {
            if (CUSTOM_MODEL_BLOCKS.contains(id)) {
                continue;
            }
            Block block = ModBlocks.BLOCKS_BY_ID.get(id).get();
            ResourceLocation texture = resolveBlockTexture(id);
            ModelFile model = models().cubeAll(id, texture);
            simpleBlockWithItem(block, model);
        }
    }

    private ResourceLocation resolveBlockTexture(String id) {
        ResourceLocation direct = resolveTexture("block/" + id);
        if (direct != null) {
            return direct;
        }

        String aliasPath = BLOCK_TEXTURE_ALIASES.get(id);
        if (aliasPath != null) {
            ResourceLocation aliased = resolveTexture(aliasPath);
            if (aliased != null) {
                return aliased;
            }
        }

        return mcLoc("block/stone");
    }

    private ResourceLocation resolveTexture(String path) {
        ResourceLocation candidate = modLoc(path);
        return existingFileHelper.exists(candidate, PackType.CLIENT_RESOURCES, ".png", "textures") ? candidate : null;
    }
}
