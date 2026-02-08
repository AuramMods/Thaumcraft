package art.arcane.thaumcraft.common.registry;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.item.ThaumcraftFluidBucketItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ModItems {
    // TODO(port): replace generic placeholder Item registrations with functional item classes (thaumometer, thaumonomicon, phials, scribing tools, foci, etc.).
    // TODO(port): migrate legacy sub-item/meta behavior into explicit item/data-component models for 1.20.1 parity.
    // TODO(port): split legacy variant containers (`nuggets`, `ingots`, `plate`, `gear`, etc.) into explicit subtype/data-component handling so quartz sliver (`nuggetQuartz`) and other recipe-critical variants are addressable.
    // TODO(port): replace placeholder armor ids with real ArmorItem implementations and legacy behaviors (vis discounts, warping values, void self-repair, traveller boots movement/recharge, fortress mask set bonuses, goggles reveal support).
    // TODO(port): replace placeholder tool ids with real tiered tool implementations (thaumium/void/elemental/crimson/primal) and legacy effects (void self-repair + warp, elemental active abilities, crimson/primal special logic).
    // TODO(port): replace placeholder consumable/system items for insanity loop (`sanity_checker`, `sanity_soap`, `bath_salts`, `bucket_pure`, `bucket_death`) once warp + fluid systems are in place.

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
            ITEMS_BY_ID.put(id, ITEMS.register(id, () -> createItem(id)));
        }

        // Transitional parity item for legacy nuggetQuartz catalyst behavior.
        ITEMS_BY_ID.put("quartz_sliver", ITEMS.register("quartz_sliver", () -> new Item(new Item.Properties())));
    }

    private static Item createItem(String id) {
        return switch (id) {
            case "bucket_pure" -> new ThaumcraftFluidBucketItem(() -> ModBlocks.get("purifying_fluid").get());
            case "bucket_death" -> new ThaumcraftFluidBucketItem(() -> ModBlocks.get("liquid_death").get());
            default -> new Item(new Item.Properties());
        };
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
