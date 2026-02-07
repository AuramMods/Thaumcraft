package art.arcane.thaumcraft.common.aspect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public final class AspectRegistry {

    private AspectRegistry() {
    }

    public static AspectList getAspects(ItemStack stack) {
        if (stack.isEmpty()) {
            return new AspectList();
        }

        AspectList aspects = AspectObjectRegistry.getInstance().resolveAspects(stack);
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId != null && aspects.isEmpty()) {
            applyLegacyPathHeuristics(aspects, itemId.getPath());
        }

        applyStackTraits(aspects, stack);
        if (aspects.isEmpty()) {
            aspects.add(AspectType.PERDITIO, 1);
        }

        return aspects;
    }

    public static int getTotalAspectValue(ItemStack stack) {
        return getAspects(stack).totalAmount();
    }

    private static void applyLegacyPathHeuristics(AspectList list, String path) {
        if (path.contains("crystal_aer")) {
            list.add(AspectType.AER, 6);
        }
        if (path.contains("crystal_terra")) {
            list.add(AspectType.TERRA, 6);
        }
        if (path.contains("crystal_ignis")) {
            list.add(AspectType.IGNIS, 6);
        }
        if (path.contains("crystal_aqua")) {
            list.add(AspectType.AQUA, 6);
        }
        if (path.contains("crystal_ordo")) {
            list.add(AspectType.ORDO, 6);
        }
        if (path.contains("crystal_perditio")) {
            list.add(AspectType.PERDITIO, 6);
        }
        if (path.contains("crystal_vitium")) {
            list.add(AspectType.VITIUM, 6);
        }

        if (path.contains("ore") || path.contains("stone") || path.contains("pillar") || path.contains("slab")) {
            list.add(AspectType.TERRA, 2);
        }
        if (path.contains("ingot") || path.contains("nugget") || path.contains("plate") || path.contains("metal")) {
            list.add(AspectType.METALLUM, 2);
            list.add(AspectType.ORDO, 1);
        }
        if (path.contains("plank") || path.contains("log") || path.contains("leaf") || path.contains("sapling") || path.contains("vishroom") || path.contains("shimmerleaf") || path.contains("cinderpearl")) {
            list.add(AspectType.HERBA, 2);
        }
        if (path.contains("flux") || path.contains("taint") || path.contains("void") || path.contains("vitium")) {
            list.add(AspectType.VITIUM, 3);
            list.add(AspectType.PERDITIO, 1);
        }
        if (path.contains("alumentum") || path.contains("fire") || path.contains("lava")) {
            list.add(AspectType.IGNIS, 2);
        }
        if (path.contains("bucket") || path.contains("aqua") || path.contains("water")) {
            list.add(AspectType.AQUA, 2);
        }
        if (path.contains("thaumometer") || path.contains("thaumonomicon") || path.contains("mind") || path.contains("research")) {
            list.add(AspectType.COGNITIO, 3);
            list.add(AspectType.PRAECANTATIO, 1);
        }
        if (path.contains("salis_mundus") || path.contains("focus") || path.contains("wand") || path.contains("vis")) {
            list.add(AspectType.PRAECANTATIO, 2);
        }
        if (path.contains("tube") || path.contains("relay") || path.contains("lever") || path.contains("matrix") || path.contains("golem")) {
            list.add(AspectType.MOTUS, 1);
        }
    }

    private static void applyStackTraits(AspectList list, ItemStack stack) {
        if (stack.getItem().isEdible()) {
            list.add(AspectType.VICTUS, 2);
            list.add(AspectType.AQUA, 1);
        }
        if (stack.isEnchanted()) {
            list.add(AspectType.PRAECANTATIO, 2);
        }
        if (stack.isDamageableItem()) {
            list.add(AspectType.MOTUS, 1);
            list.add(AspectType.METALLUM, 1);
        }
    }
}
