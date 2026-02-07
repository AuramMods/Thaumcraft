package art.arcane.thaumcraft.common.aspect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public final class AspectRegistry {
    // TODO(port): Remove broad fallback heuristics after full data migration from legacy object tags to avoid aspect drift from canonical values.
    // TODO(port): add explicit metadata/NBT handling entry points so callers can request exact legacy parity for special stacks.
    // TODO(port): Extend recipe-derived aspect generation to include crucible/infusion/arcane recipe families once those systems are fully data-driven.
    // TODO(port): Add packet-backed server authoritative tooltip syncing for edge cases where client recipe data differs from server packs.

    private static final int MAX_RECIPE_DEPTH = 16;
    private static final int MAX_INGREDIENT_OPTIONS = 64;
    private static final int MAX_DEBUG_RECIPES = 128;

    private static final Map<RecipeManager, CachedRecipeData> CRAFTING_CACHE_BY_MANAGER =
            Collections.synchronizedMap(new WeakHashMap<>());

    private AspectRegistry() {
    }

    public static AspectList getAspects(ItemStack stack) {
        return getAspects(null, stack);
    }

    public static AspectList getAspects(Level level, ItemStack stack) {
        if (stack.isEmpty()) {
            return new AspectList();
        }

        ItemStack single = stack.copy();
        single.setCount(1);
        CachedRecipeData recipeData = level == null ? null : getOrBuildRecipeData(level);
        ResolutionContext context = new ResolutionContext(level, recipeData, new HashMap<>(), new HashSet<>());
        AspectList aspects = resolveBaseAspects(single, context, 0);
        applyStackTraits(aspects, stack);
        if (aspects.isEmpty()) {
            aspects.add(AspectType.PERDITIO, 1);
        }

        return aspects;
    }

    public static int getTotalAspectValue(ItemStack stack) {
        return getTotalAspectValue(null, stack);
    }

    public static int getTotalAspectValue(Level level, ItemStack stack) {
        return getAspects(level, stack).totalAmount();
    }

    public static AspectDebugResult debugAspects(Level level, ItemStack stack) {
        if (stack.isEmpty()) {
            return new AspectDebugResult(new AspectList(), List.of("Stack is empty."));
        }

        ItemStack single = stack.copy();
        single.setCount(1);
        CachedRecipeData recipeData = level == null ? null : getOrBuildRecipeData(level);
        ResolutionContext scoreContext = new ResolutionContext(level, recipeData, new HashMap<>(), new HashSet<>());

        List<String> trace = new ArrayList<>();
        trace.add("resolve " + describeStack(single));

        AspectList baseAspects = debugResolveBaseAspects(
                single,
                scoreContext,
                0,
                trace,
                1,
                new HashSet<>(),
                false
        );
        trace.add("base: " + formatAspectList(baseAspects));

        AspectList traitAspects = new AspectList();
        applyStackTraits(traitAspects, stack);

        AspectList total = baseAspects.copy();
        if (!traitAspects.isEmpty()) {
            total.addAll(traitAspects);
            trace.add("stack_traits: " + formatAspectList(traitAspects));
        } else {
            trace.add("stack_traits: none");
        }
        trace.add("total: " + formatAspectList(total));

        return new AspectDebugResult(total, List.copyOf(trace));
    }

    public static void invalidateCaches() {
        synchronized (CRAFTING_CACHE_BY_MANAGER) {
            CRAFTING_CACHE_BY_MANAGER.clear();
        }
    }

    private static AspectList resolveBaseAspects(ItemStack stack, ResolutionContext context, int depth) {
        if (stack.isEmpty()) {
            return new AspectList();
        }

        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId != null) {
            AspectList localCached = context.localCache().get(itemId);
            if (localCached != null) {
                return localCached.copy();
            }

            if (context.recipeData() != null) {
                AspectList sharedCached = context.recipeData().resolvedBaseAspects().get(itemId);
                if (sharedCached != null) {
                    AspectList copy = sharedCached.copy();
                    context.localCache().put(itemId, copy.copy());
                    return copy;
                }
            }

            if (context.resolutionStack().contains(itemId)) {
                return new AspectList();
            }

            context.resolutionStack().add(itemId);
        }

        try {
            AspectList aspects = AspectObjectRegistry.getInstance().resolveAspects(stack);
            if (aspects.isEmpty() && context.level() != null && context.recipeData() != null && depth < MAX_RECIPE_DEPTH) {
                aspects.addAll(resolveFromCraftingRecipes(itemId, stack, context, depth + 1));
            }

            if (itemId != null && aspects.isEmpty()) {
                applyLegacyPathHeuristics(aspects, itemId.getPath());
            }
            if (aspects.isEmpty()) {
                aspects.add(AspectType.PERDITIO, 1);
            }

            AspectList resolved = aspects.copy();
            if (itemId != null) {
                context.localCache().put(itemId, resolved.copy());
                if (context.recipeData() != null) {
                    context.recipeData().resolvedBaseAspects().putIfAbsent(itemId, resolved.copy());
                }
            }
            return resolved;
        } finally {
            if (itemId != null) {
                context.resolutionStack().remove(itemId);
            }
        }
    }

    private static AspectList resolveFromCraftingRecipes(ResourceLocation itemId, ItemStack target, ResolutionContext context, int depth) {
        if (itemId == null || context.level() == null || context.recipeData() == null) {
            return new AspectList();
        }

        List<CraftingRecipe> recipes = context.recipeData().recipesByOutput().get(itemId);
        if (recipes == null || recipes.isEmpty()) {
            return new AspectList();
        }

        AspectList best = new AspectList();
        int bestScore = Integer.MAX_VALUE;
        for (CraftingRecipe recipe : recipes) {
            ItemStack recipeOutput = recipe.getResultItem(context.level().registryAccess());
            if (recipeOutput.isEmpty() || recipeOutput.getItem() != target.getItem()) {
                continue;
            }

            AspectList candidate = resolveCraftingRecipeAspects(recipe, recipeOutput, context, depth + 1);
            int candidateScore = candidate.totalAmount();
            if (candidateScore > 0 && candidateScore < bestScore) {
                best = candidate;
                bestScore = candidateScore;
            }
        }

        return best;
    }

    private static AspectList resolveCraftingRecipeAspects(CraftingRecipe recipe, ItemStack recipeOutput, ResolutionContext context, int depth) {
        AspectList ingredientTotals = new AspectList();
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.isEmpty()) {
                continue;
            }

            AspectList ingredientAspects = resolveIngredientAspects(ingredient, context, depth + 1);
            if (ingredientAspects.isEmpty()) {
                return new AspectList();
            }
            ingredientTotals.addAll(ingredientAspects);
        }

        if (ingredientTotals.isEmpty()) {
            return new AspectList();
        }

        return scaleCraftedAspects(ingredientTotals, Math.max(1, recipeOutput.getCount()));
    }

    private static AspectList resolveIngredientAspects(Ingredient ingredient, ResolutionContext context, int depth) {
        ItemStack[] options = ingredient.getItems();
        if (options.length == 0) {
            return new AspectList();
        }

        AspectList best = new AspectList();
        int bestScore = Integer.MAX_VALUE;
        int limit = Math.min(options.length, MAX_INGREDIENT_OPTIONS);
        for (int i = 0; i < limit; i++) {
            ItemStack option = options[i];
            if (option.isEmpty()) {
                continue;
            }

            ItemStack single = option.copy();
            single.setCount(1);
            AspectList candidate = resolveBaseAspects(single, context, depth + 1);
            int candidateScore = candidate.totalAmount();
            if (candidateScore > 0 && candidateScore < bestScore) {
                best = candidate;
                bestScore = candidateScore;
            }
        }

        return best;
    }

    private static AspectList scaleCraftedAspects(AspectList ingredientTotals, int outputCount) {
        AspectList out = new AspectList();
        for (Map.Entry<AspectType, Integer> entry : ingredientTotals.asMap().entrySet()) {
            float scaled = (entry.getValue() * 0.75F) / (float) outputCount;
            int amount = (int) scaled;
            if (scaled < 1.0F && scaled > 0.75F) {
                amount = 1;
            }
            if (amount > 0) {
                out.add(entry.getKey(), amount);
            }
        }
        return out;
    }

    private static CachedRecipeData getOrBuildRecipeData(Level level) {
        RecipeManager recipeManager = level.getRecipeManager();
        List<CraftingRecipe> craftingRecipes = recipeManager.getAllRecipesFor(RecipeType.CRAFTING);
        int recipeCount = craftingRecipes.size();
        int recipeHash = craftingRecipes.hashCode();

        synchronized (CRAFTING_CACHE_BY_MANAGER) {
            CachedRecipeData existing = CRAFTING_CACHE_BY_MANAGER.get(recipeManager);
            if (existing != null
                    && existing.craftingRecipeCount() == recipeCount
                    && existing.craftingRecipeHash() == recipeHash) {
                return existing;
            }

            Map<ResourceLocation, List<CraftingRecipe>> grouped = new HashMap<>();
            for (CraftingRecipe recipe : craftingRecipes) {
                ItemStack output = recipe.getResultItem(level.registryAccess());
                if (output.isEmpty()) {
                    continue;
                }

                ResourceLocation outputId = ForgeRegistries.ITEMS.getKey(output.getItem());
                if (outputId == null) {
                    continue;
                }
                grouped.computeIfAbsent(outputId, key -> new ArrayList<>()).add(recipe);
            }

            Map<ResourceLocation, List<CraftingRecipe>> immutableGrouped = new HashMap<>();
            for (Map.Entry<ResourceLocation, List<CraftingRecipe>> entry : grouped.entrySet()) {
                immutableGrouped.put(entry.getKey(), List.copyOf(entry.getValue()));
            }

            CachedRecipeData rebuilt = new CachedRecipeData(
                    recipeCount,
                    recipeHash,
                    Map.copyOf(immutableGrouped),
                    new ConcurrentHashMap<>()
            );
            CRAFTING_CACHE_BY_MANAGER.put(recipeManager, rebuilt);
            return rebuilt;
        }
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

    private static AspectList debugResolveBaseAspects(ItemStack stack,
                                                      ResolutionContext scoreContext,
                                                      int depth,
                                                      List<String> trace,
                                                      int indent,
                                                      Set<ResourceLocation> debugStack,
                                                      boolean preferCaches) {
        if (stack.isEmpty()) {
            addDebugLine(trace, indent, "empty stack");
            return new AspectList();
        }

        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId == null) {
            addDebugLine(trace, indent, "no item id; fallback to perditio=1");
            AspectList fallback = new AspectList();
            fallback.add(AspectType.PERDITIO, 1);
            return fallback;
        }

        if (preferCaches) {
            AspectList localCached = scoreContext.localCache().get(itemId);
            if (localCached != null) {
                addDebugLine(trace, indent, "cache(local) " + itemId + " -> " + formatAspectList(localCached));
                return localCached.copy();
            }
            if (scoreContext.recipeData() != null) {
                AspectList sharedCached = scoreContext.recipeData().resolvedBaseAspects().get(itemId);
                if (sharedCached != null) {
                    addDebugLine(trace, indent, "cache(shared) " + itemId + " -> " + formatAspectList(sharedCached));
                    return sharedCached.copy();
                }
            }
        }

        if (debugStack.contains(itemId)) {
            addDebugLine(trace, indent, "cycle detected at " + itemId + ", branch ignored");
            return new AspectList();
        }
        debugStack.add(itemId);

        try {
            addDebugLine(trace, indent, "item " + itemId);
            AspectList aspects = AspectObjectRegistry.getInstance().resolveAspects(stack);
            if (!aspects.isEmpty()) {
                addDebugLine(trace, indent + 1, "object_data -> " + formatAspectList(aspects));
                return aspects.copy();
            }

            addDebugLine(trace, indent + 1, "object_data -> none");
            if (scoreContext.level() != null && scoreContext.recipeData() != null && depth < MAX_RECIPE_DEPTH) {
                AspectList recipeAspects = debugResolveFromCraftingRecipes(
                        itemId,
                        stack,
                        scoreContext,
                        depth + 1,
                        trace,
                        indent + 1,
                        debugStack
                );
                if (!recipeAspects.isEmpty()) {
                    return recipeAspects.copy();
                }
            } else if (scoreContext.level() == null) {
                addDebugLine(trace, indent + 1, "recipes skipped: no level context");
            } else if (depth >= MAX_RECIPE_DEPTH) {
                addDebugLine(trace, indent + 1, "recipes skipped: max depth reached");
            }

            AspectList heuristics = new AspectList();
            applyLegacyPathHeuristics(heuristics, itemId.getPath());
            if (!heuristics.isEmpty()) {
                addDebugLine(trace, indent + 1, "legacy_heuristics -> " + formatAspectList(heuristics));
                return heuristics;
            }

            AspectList fallback = new AspectList();
            fallback.add(AspectType.PERDITIO, 1);
            addDebugLine(trace, indent + 1, "fallback -> perditio=1");
            return fallback;
        } finally {
            debugStack.remove(itemId);
        }
    }

    private static AspectList debugResolveFromCraftingRecipes(ResourceLocation itemId,
                                                              ItemStack target,
                                                              ResolutionContext scoreContext,
                                                              int depth,
                                                              List<String> trace,
                                                              int indent,
                                                              Set<ResourceLocation> debugStack) {
        List<CraftingRecipe> recipes = scoreContext.recipeData().recipesByOutput().get(itemId);
        if (recipes == null || recipes.isEmpty()) {
            addDebugLine(trace, indent, "crafting_recipes -> none");
            return new AspectList();
        }

        AspectList bestAspects = new AspectList();
        CraftingRecipe bestRecipe = null;
        ItemStack bestOutput = ItemStack.EMPTY;
        int bestScore = Integer.MAX_VALUE;
        int scanned = 0;

        addDebugLine(trace, indent, "crafting_recipes -> " + recipes.size());
        for (CraftingRecipe recipe : recipes) {
            if (scanned >= MAX_DEBUG_RECIPES) {
                addDebugLine(trace, indent + 1, "recipe scan truncated at " + MAX_DEBUG_RECIPES);
                break;
            }
            scanned++;

            ItemStack recipeOutput = recipe.getResultItem(scoreContext.level().registryAccess());
            if (recipeOutput.isEmpty() || recipeOutput.getItem() != target.getItem()) {
                continue;
            }

            AspectList candidate = resolveCraftingRecipeAspects(recipe, recipeOutput, scoreContext, depth + 1);
            int score = candidate.totalAmount();
            addDebugLine(trace, indent + 1, "recipe " + recipe.getId() + " -> " + formatAspectList(candidate));
            if (score > 0 && score < bestScore) {
                bestScore = score;
                bestAspects = candidate.copy();
                bestRecipe = recipe;
                bestOutput = recipeOutput.copy();
            }
        }

        if (bestRecipe == null || bestAspects.isEmpty()) {
            addDebugLine(trace, indent, "no valid crafting recipe aspects");
            return new AspectList();
        }

        addDebugLine(trace, indent, "selected_recipe " + bestRecipe.getId() + " -> " + formatAspectList(bestAspects));
        debugExplainCraftingRecipe(bestRecipe, bestOutput, scoreContext, depth + 1, trace, indent + 1, debugStack);
        return bestAspects;
    }

    private static void debugExplainCraftingRecipe(CraftingRecipe recipe,
                                                   ItemStack recipeOutput,
                                                   ResolutionContext scoreContext,
                                                   int depth,
                                                   List<String> trace,
                                                   int indent,
                                                   Set<ResourceLocation> debugStack) {
        int ingredientIndex = 0;
        AspectList ingredientTotals = new AspectList();

        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.isEmpty()) {
                continue;
            }

            ingredientIndex++;
            AspectList selected = debugResolveSelectedIngredient(
                    ingredient,
                    ingredientIndex,
                    scoreContext,
                    depth + 1,
                    trace,
                    indent + 1,
                    debugStack
            );
            ingredientTotals.addAll(selected);
        }

        if (ingredientTotals.isEmpty()) {
            addDebugLine(trace, indent, "selected recipe has no resolvable ingredients");
            return;
        }

        AspectList scaled = scaleCraftedAspects(ingredientTotals, Math.max(1, recipeOutput.getCount()));
        addDebugLine(trace, indent, "ingredient_total -> " + formatAspectList(ingredientTotals));
        addDebugLine(trace, indent, "scaled_output -> " + formatAspectList(scaled));
    }

    private static AspectList debugResolveSelectedIngredient(Ingredient ingredient,
                                                             int ingredientIndex,
                                                             ResolutionContext scoreContext,
                                                             int depth,
                                                             List<String> trace,
                                                             int indent,
                                                             Set<ResourceLocation> debugStack) {
        ItemStack[] options = ingredient.getItems();
        if (options.length == 0) {
            addDebugLine(trace, indent, "ingredient[" + ingredientIndex + "] has no options");
            return new AspectList();
        }

        int limit = Math.min(options.length, MAX_INGREDIENT_OPTIONS);
        ItemStack bestOption = ItemStack.EMPTY;
        AspectList bestAspects = new AspectList();
        int bestScore = Integer.MAX_VALUE;

        for (int i = 0; i < limit; i++) {
            ItemStack option = options[i];
            if (option.isEmpty()) {
                continue;
            }

            ItemStack single = option.copy();
            single.setCount(1);
            AspectList candidate = resolveBaseAspects(single, scoreContext, depth + 1);
            int score = candidate.totalAmount();
            if (score > 0 && score < bestScore) {
                bestScore = score;
                bestOption = single;
                bestAspects = candidate.copy();
            }
        }

        if (bestOption.isEmpty() || bestAspects.isEmpty()) {
            addDebugLine(trace, indent, "ingredient[" + ingredientIndex + "] unresolved");
            return new AspectList();
        }

        addDebugLine(trace, indent, "ingredient[" + ingredientIndex + "] selected " + describeStack(bestOption) + " -> " + formatAspectList(bestAspects));
        ResolutionContext explainContext = new ResolutionContext(
                scoreContext.level(),
                scoreContext.recipeData(),
                new HashMap<>(),
                new HashSet<>()
        );
        debugResolveBaseAspects(bestOption, explainContext, depth + 1, trace, indent + 1, debugStack, false);
        return bestAspects;
    }

    private static void addDebugLine(List<String> trace, int indent, String message) {
        trace.add("  ".repeat(Math.max(0, indent)) + message);
    }

    private static String describeStack(ItemStack stack) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId == null) {
            return "unknown";
        }
        return itemId + " x" + stack.getCount();
    }

    private static String formatAspectList(AspectList aspects) {
        if (aspects == null || aspects.isEmpty()) {
            return "none";
        }

        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Map.Entry<AspectType, Integer> entry : aspects.asMap().entrySet()) {
            if (!first) {
                builder.append(", ");
            }
            first = false;
            builder.append(entry.getKey().getTag()).append("=").append(entry.getValue());
        }
        return builder.toString();
    }

    private record ResolutionContext(Level level,
                                     CachedRecipeData recipeData,
                                     Map<ResourceLocation, AspectList> localCache,
                                     Set<ResourceLocation> resolutionStack) {
    }

    private record CachedRecipeData(int craftingRecipeCount,
                                    int craftingRecipeHash,
                                    Map<ResourceLocation, List<CraftingRecipe>> recipesByOutput,
                                    Map<ResourceLocation, AspectList> resolvedBaseAspects) {
    }

    public record AspectDebugResult(AspectList aspects, List<String> traceLines) {
    }
}
