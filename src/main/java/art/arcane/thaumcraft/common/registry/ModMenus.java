package art.arcane.thaumcraft.common.registry;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.menu.ArcaneWorkbenchMenu;
import art.arcane.thaumcraft.common.menu.CrucibleMenu;
import art.arcane.thaumcraft.common.menu.InfusionMatrixMenu;
import art.arcane.thaumcraft.common.menu.ResearchTableMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModMenus {
    // TODO(port): add dedicated menu types for newly functional stations (thaumatorium, golem builder, etc.) as their block entities are ported.
    // TODO(port): keep menu ids aligned with legacy naming where possible to ease recipe/research/UI migration.

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Thaumcraft.MODID);
    public static final RegistryObject<MenuType<ArcaneWorkbenchMenu>> ARCANE_WORKBENCH = MENUS.register("arcane_workbench",
            () -> IForgeMenuType.create(ArcaneWorkbenchMenu::new));
    public static final RegistryObject<MenuType<ResearchTableMenu>> RESEARCH_TABLE = MENUS.register("research_table",
            () -> IForgeMenuType.create(ResearchTableMenu::new));
    public static final RegistryObject<MenuType<CrucibleMenu>> CRUCIBLE = MENUS.register("crucible",
            () -> IForgeMenuType.create(CrucibleMenu::new));
    public static final RegistryObject<MenuType<InfusionMatrixMenu>> INFUSION_MATRIX = MENUS.register("infusion_matrix",
            () -> IForgeMenuType.create(InfusionMatrixMenu::new));

    private ModMenus() {
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
