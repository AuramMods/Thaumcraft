package art.arcane.thaumcraft.common.registry;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.item.ThaumcraftFluidBucketItem;
import art.arcane.thaumcraft.common.item.BathSaltsItem;
import art.arcane.thaumcraft.common.item.armor.CultistBootsItem;
import art.arcane.thaumcraft.common.item.armor.CultistRobeArmorItem;
import art.arcane.thaumcraft.common.item.armor.GogglesItem;
import art.arcane.thaumcraft.common.item.armor.RobeArmorItem;
import art.arcane.thaumcraft.common.item.armor.TravellerBootsItem;
import art.arcane.thaumcraft.common.item.armor.VoidArmorItem;
import art.arcane.thaumcraft.common.item.SanityCheckerItem;
import art.arcane.thaumcraft.common.item.SanitySoapItem;
import art.arcane.thaumcraft.common.item.tool.CrimsonBladeItem;
import art.arcane.thaumcraft.common.item.tool.PrimalCrusherItem;
import art.arcane.thaumcraft.common.item.tool.VoidAxeItem;
import art.arcane.thaumcraft.common.item.tool.VoidHoeItem;
import art.arcane.thaumcraft.common.item.tool.VoidPickaxeItem;
import art.arcane.thaumcraft.common.item.tool.VoidShovelItem;
import art.arcane.thaumcraft.common.item.tool.VoidSwordItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
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
        Item armorItem = createArmorItem(id);
        if (armorItem != null) {
            return armorItem;
        }

        Item toolItem = createToolItem(id);
        if (toolItem != null) {
            return toolItem;
        }

        return switch (id) {
            case "bucket_pure" -> new ThaumcraftFluidBucketItem(() -> ModBlocks.get("purifying_fluid").get());
            case "bucket_death" -> new ThaumcraftFluidBucketItem(() -> ModBlocks.get("liquid_death").get());
            case "bath_salts" -> new BathSaltsItem();
            case "sanity_checker" -> new SanityCheckerItem();
            case "sanity_soap" -> new SanitySoapItem();
            default -> new Item(new Item.Properties());
        };
    }

    @Nullable
    private static Item createArmorItem(String id) {
        // TODO(port): Replace these baseline vanilla-material mappings with proper Thaumcraft armor classes/materials/textures + behaviors.
        return switch (id) {
            case "cloth_boots" -> new RobeArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, 2, new Item.Properties());
            case "cloth_chest" -> new RobeArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, 3, new Item.Properties());
            case "cloth_legs" -> new RobeArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.LEGGINGS, 3, new Item.Properties());

            case "thaumium_boots" -> new ArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.BOOTS, new Item.Properties());
            case "thaumium_chest" -> new ArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.CHESTPLATE, new Item.Properties());
            case "thaumium_helm" -> new ArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, new Item.Properties());
            case "thaumium_legs" -> new ArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.LEGGINGS, new Item.Properties());

            case "traveller_boots" -> new TravellerBootsItem(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new Item.Properties());

            case "fortress_chest" -> new ArmorItem(ArmorMaterials.NETHERITE, ArmorItem.Type.CHESTPLATE, new Item.Properties());
            case "fortress_helm" -> new ArmorItem(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, new Item.Properties());
            case "fortress_legs" -> new ArmorItem(ArmorMaterials.NETHERITE, ArmorItem.Type.LEGGINGS, new Item.Properties());

            case "void_boots" -> new VoidArmorItem(ArmorMaterials.NETHERITE, ArmorItem.Type.BOOTS, 1, 0, new Item.Properties());
            case "void_chest" -> new VoidArmorItem(ArmorMaterials.NETHERITE, ArmorItem.Type.CHESTPLATE, 1, 0, new Item.Properties());
            case "void_helm" -> new VoidArmorItem(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, 1, 0, new Item.Properties());
            case "void_legs" -> new VoidArmorItem(ArmorMaterials.NETHERITE, ArmorItem.Type.LEGGINGS, 1, 0, new Item.Properties());

            case "void_robe_chest" -> new VoidArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, 3, 5, new Item.Properties());
            case "void_robe_helm" -> new VoidArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, 3, 5, new Item.Properties());
            case "void_robe_legs" -> new VoidArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.LEGGINGS, 3, 5, new Item.Properties());

            case "crimson_boots" -> new CultistBootsItem(ArmorMaterials.IRON, ArmorItem.Type.BOOTS, new Item.Properties());
            case "crimson_plate_chest" -> new ArmorItem(ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE, new Item.Properties());
            case "crimson_plate_helm" -> new ArmorItem(ArmorMaterials.IRON, ArmorItem.Type.HELMET, new Item.Properties());
            case "crimson_plate_legs" -> new ArmorItem(ArmorMaterials.IRON, ArmorItem.Type.LEGGINGS, new Item.Properties());

            case "crimson_praetor_chest" -> new ArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.CHESTPLATE, new Item.Properties());
            case "crimson_praetor_helm" -> new ArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, new Item.Properties());
            case "crimson_praetor_legs" -> new ArmorItem(ArmorMaterials.DIAMOND, ArmorItem.Type.LEGGINGS, new Item.Properties());

            case "crimson_robe_chest" -> new CultistRobeArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, new Item.Properties());
            case "crimson_robe_helm" -> new CultistRobeArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new Item.Properties());
            case "crimson_robe_legs" -> new CultistRobeArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.LEGGINGS, new Item.Properties());
            case "goggles" -> new GogglesItem(ArmorMaterials.CHAIN, ArmorItem.Type.HELMET, new Item.Properties());
            default -> null;
        };
    }

    @Nullable
    private static Item createToolItem(String id) {
        // TODO(port): Replace these baseline vanilla-tier tools with dedicated Thaumcraft tool classes/effects.
        return switch (id) {
            case "thaumium_pick" -> new PickaxeItem(Tiers.DIAMOND, 1, -2.8F, new Item.Properties());
            case "thaumium_axe" -> new AxeItem(Tiers.DIAMOND, 5.0F, -3.0F, new Item.Properties());
            case "thaumium_shovel" -> new ShovelItem(Tiers.DIAMOND, 1.5F, -3.0F, new Item.Properties());
            case "thaumium_sword" -> new SwordItem(Tiers.DIAMOND, 3, -2.4F, new Item.Properties());
            case "thaumium_hoe" -> new HoeItem(Tiers.DIAMOND, -3, 0.0F, new Item.Properties());

            case "void_pick" -> new VoidPickaxeItem(Tiers.NETHERITE, 1, -2.8F, new Item.Properties().fireResistant());
            case "void_axe" -> new VoidAxeItem(Tiers.NETHERITE, 5.0F, -3.0F, new Item.Properties().fireResistant());
            case "void_shovel" -> new VoidShovelItem(Tiers.NETHERITE, 1.5F, -3.0F, new Item.Properties().fireResistant());
            case "void_sword" -> new VoidSwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties().fireResistant());
            case "void_hoe" -> new VoidHoeItem(Tiers.NETHERITE, -4, 0.0F, new Item.Properties().fireResistant());

            case "elemental_pick" -> new PickaxeItem(Tiers.NETHERITE, 1, -2.8F, new Item.Properties().fireResistant());
            case "elemental_axe" -> new AxeItem(Tiers.NETHERITE, 5.0F, -3.0F, new Item.Properties().fireResistant());
            case "elemental_shovel" -> new ShovelItem(Tiers.NETHERITE, 1.5F, -3.0F, new Item.Properties().fireResistant());
            case "elemental_sword" -> new SwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties().fireResistant());
            case "elemental_hoe" -> new HoeItem(Tiers.NETHERITE, -4, 0.0F, new Item.Properties().fireResistant());

            case "crimson_blade" -> new CrimsonBladeItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties().fireResistant());
            case "primal_crusher" -> new PrimalCrusherItem(Tiers.NETHERITE, 2, -2.7F, new Item.Properties().fireResistant());
            default -> null;
        };
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
