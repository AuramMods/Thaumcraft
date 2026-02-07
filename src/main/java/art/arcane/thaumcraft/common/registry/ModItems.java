package art.arcane.thaumcraft.common.registry;

import art.arcane.thaumcraft.Thaumcraft;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Thaumcraft.MODID);
    public static final Map<String, RegistryObject<Item>> BLOCK_ITEMS_BY_ID = new LinkedHashMap<>();
    public static final Map<String, RegistryObject<Item>> ITEMS_BY_ID = new LinkedHashMap<>();

    static {
        registerLegacyBlockItems();
        registerLegacyItems();
    }

    private ModItems() {
    }

    private static void registerLegacyBlockItems() {
        for (Map.Entry<String, RegistryObject<net.minecraft.world.level.block.Block>> entry : ModBlocks.BLOCKS_BY_ID.entrySet()) {
            BLOCK_ITEMS_BY_ID.put(entry.getKey(), ITEMS.register(entry.getKey(), () -> new BlockItem(entry.getValue().get(), new Item.Properties())));
        }
    }

    private static void registerLegacyItems() {
        for (String id : LegacyContentRegistryIds.ITEM_IDS) {
            ITEMS_BY_ID.put(id, ITEMS.register(id, () -> new Item(new Item.Properties())));
        }
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
