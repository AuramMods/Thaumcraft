package art.arcane.thaumcraft.common.registry;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.block.JarBlock;
import art.arcane.thaumcraft.common.block.PedestalBlock;
import art.arcane.thaumcraft.common.block.StationBlock;
import art.arcane.thaumcraft.common.block.CrucibleBlock;
import art.arcane.thaumcraft.common.block.entity.ArcaneWorkbenchBlockEntity;
import art.arcane.thaumcraft.common.block.entity.CrucibleBlockEntity;
import art.arcane.thaumcraft.common.block.entity.InfusionMatrixBlockEntity;
import art.arcane.thaumcraft.common.block.entity.ResearchTableBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ModBlocks {
    // TODO(port): Replace remaining placeholder `new Block(...)` registrations with dedicated block classes as each legacy system is implemented.
    // TODO(port): split variant-heavy legacy ids (e.g., metadata-driven stone/pillar families) into explicit 1.20.1 blockstate/property models where parity needs it.
    // TODO(port): replace placeholder fluid blocks (`purifying_fluid`, `liquid_death`) with proper fluid block/fluid type stacks so custom bucket pickup/place behavior and warp interactions can be restored.

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Thaumcraft.MODID);
    public static final Map<String, RegistryObject<Block>> BLOCKS_BY_ID = new LinkedHashMap<>();

    static {
        registerLegacyBlocks();
    }

    private ModBlocks() {
    }

    private static void registerLegacyBlocks() {
        for (String id : LegacyContentRegistryIds.BLOCK_IDS) {
            BLOCKS_BY_ID.put(id, BLOCKS.register(id, () -> createBlock(id)));
        }
    }

    private static Block createBlock(String id) {
        return switch (id) {
            case "arcane_workbench" -> new StationBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE), ArcaneWorkbenchBlockEntity::new);
            case "research_table" -> new StationBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE), ResearchTableBlockEntity::new);
            case "crucible" -> new CrucibleBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON), CrucibleBlockEntity::new);
            case "infusion_matrix" -> new StationBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion(), InfusionMatrixBlockEntity::new);
            case "jar_normal", "jar_void", "jar_brain" -> new JarBlock(jarProperties());
            case "pedestal" -> new PedestalBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(3.0F).noOcclusion());
            default -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE));
        };
    }

    private static BlockBehaviour.Properties jarProperties() {
        return BlockBehaviour.Properties.copy(Blocks.GLASS)
                .strength(0.3F)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .isSuffocating((state, level, pos) -> false)
                .isViewBlocking((state, level, pos) -> false);
    }

    public static RegistryObject<Block> get(String id) {
        RegistryObject<Block> block = BLOCKS_BY_ID.get(id);
        if (block == null) {
            throw new IllegalArgumentException("Unknown block id: " + id);
        }
        return block;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
