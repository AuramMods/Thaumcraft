package art.arcane.thaumcraft.common.registry;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.block.entity.ArcaneWorkbenchBlockEntity;
import art.arcane.thaumcraft.common.block.entity.CrucibleBlockEntity;
import art.arcane.thaumcraft.common.block.entity.InfusionMatrixBlockEntity;
import art.arcane.thaumcraft.common.block.entity.JarBlockEntity;
import art.arcane.thaumcraft.common.block.entity.PedestalBlockEntity;
import art.arcane.thaumcraft.common.block.entity.ResearchTableBlockEntity;
import art.arcane.thaumcraft.common.block.entity.SpaBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlockEntities {
    // TODO(port): register additional block entities as placeholder blocks are promoted (thaumatorium, golem builder, infernal furnace, essentia transport line, etc.).
    // TODO(port): split shared jar registration if variant-specific behavior/state requires distinct block entity types for parity.

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Thaumcraft.MODID);
    public static final RegistryObject<BlockEntityType<ArcaneWorkbenchBlockEntity>> ARCANE_WORKBENCH = BLOCK_ENTITIES.register("arcane_workbench",
            () -> BlockEntityType.Builder.of(ArcaneWorkbenchBlockEntity::new, ModBlocks.get("arcane_workbench").get()).build(null));
    public static final RegistryObject<BlockEntityType<ResearchTableBlockEntity>> RESEARCH_TABLE = BLOCK_ENTITIES.register("research_table",
            () -> BlockEntityType.Builder.of(ResearchTableBlockEntity::new, ModBlocks.get("research_table").get()).build(null));
    public static final RegistryObject<BlockEntityType<CrucibleBlockEntity>> CRUCIBLE = BLOCK_ENTITIES.register("crucible",
            () -> BlockEntityType.Builder.of(CrucibleBlockEntity::new, ModBlocks.get("crucible").get()).build(null));
    public static final RegistryObject<BlockEntityType<InfusionMatrixBlockEntity>> INFUSION_MATRIX = BLOCK_ENTITIES.register("infusion_matrix",
            () -> BlockEntityType.Builder.of(InfusionMatrixBlockEntity::new, ModBlocks.get("infusion_matrix").get()).build(null));
    public static final RegistryObject<BlockEntityType<JarBlockEntity>> JAR = BLOCK_ENTITIES.register("jar",
            () -> BlockEntityType.Builder.of(JarBlockEntity::new,
                    ModBlocks.get("jar_normal").get(),
                    ModBlocks.get("jar_void").get(),
                    ModBlocks.get("jar_brain").get()).build(null));
    public static final RegistryObject<BlockEntityType<PedestalBlockEntity>> PEDESTAL = BLOCK_ENTITIES.register("pedestal",
            () -> BlockEntityType.Builder.of(PedestalBlockEntity::new, ModBlocks.get("pedestal").get()).build(null));
    public static final RegistryObject<BlockEntityType<SpaBlockEntity>> SPA = BLOCK_ENTITIES.register("spa",
            () -> BlockEntityType.Builder.of(SpaBlockEntity::new, ModBlocks.get("spa").get()).build(null));

    private ModBlockEntities() {
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
