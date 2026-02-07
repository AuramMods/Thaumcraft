package art.arcane.thaumcraft.client;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.client.screen.ArcaneWorkbenchScreen;
import art.arcane.thaumcraft.client.screen.CrucibleScreen;
import art.arcane.thaumcraft.client.screen.InfusionMatrixScreen;
import art.arcane.thaumcraft.client.screen.ResearchTableScreen;
import art.arcane.thaumcraft.common.registry.ModMenus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.gui.screens.MenuScreens;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ThaumcraftClientEvents {

    private ThaumcraftClientEvents() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.ARCANE_WORKBENCH.get(), ArcaneWorkbenchScreen::new);
            MenuScreens.register(ModMenus.RESEARCH_TABLE.get(), ResearchTableScreen::new);
            MenuScreens.register(ModMenus.CRUCIBLE.get(), CrucibleScreen::new);
            MenuScreens.register(ModMenus.INFUSION_MATRIX.get(), InfusionMatrixScreen::new);
        });
    }
}
