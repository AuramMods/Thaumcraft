package art.arcane.thaumcraft.common.aspect;

import art.arcane.thaumcraft.Thaumcraft;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thaumcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class AspectReloadEvents {

    private AspectReloadEvents() {
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(AspectObjectRegistry.getInstance());
    }
}
