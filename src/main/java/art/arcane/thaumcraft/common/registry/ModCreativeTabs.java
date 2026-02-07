package art.arcane.thaumcraft.common.registry;

import art.arcane.thaumcraft.Thaumcraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Thaumcraft.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thaumcraft.main"))
            .icon(() -> new ItemStack(ModItems.ITEMS_BY_ID.get("thaumonomicon").get()))
            .displayItems((params, output) -> {
                for (Map.Entry<String, RegistryObject<net.minecraft.world.item.Item>> entry : ModItems.BLOCK_ITEMS_BY_ID.entrySet()) {
                    output.accept(entry.getValue().get());
                }
                for (Map.Entry<String, RegistryObject<net.minecraft.world.item.Item>> entry : ModItems.ITEMS_BY_ID.entrySet()) {
                    output.accept(entry.getValue().get());
                }
            })
            .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}
