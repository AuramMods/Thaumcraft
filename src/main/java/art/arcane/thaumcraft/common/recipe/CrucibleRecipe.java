package art.arcane.thaumcraft.common.recipe;

import art.arcane.thaumcraft.common.aspect.AspectList;
import art.arcane.thaumcraft.common.registry.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CrucibleRecipe implements Recipe<Container> {
    // TODO(port): include legacy research key gating and optional catalyst metadata matching nuances used by old CrucibleRecipe definitions.
    // TODO(port): support richer outputs where needed (e.g., NBT-specialized results) to match legacy alchemy edge cases.

    private final ResourceLocation id;
    private final Ingredient catalyst;
    private final ItemStack result;
    private final AspectList requiredAspects;
    private final int waterCost;

    public CrucibleRecipe(ResourceLocation id, Ingredient catalyst, ItemStack result, AspectList requiredAspects, int waterCost) {
        this.id = id;
        this.catalyst = catalyst;
        this.result = result;
        this.requiredAspects = requiredAspects;
        this.waterCost = waterCost;
    }

    public Ingredient getCatalyst() {
        return this.catalyst;
    }

    public ItemStack getResult() {
        return this.result.copy();
    }

    public AspectList getRequiredAspects() {
        return this.requiredAspects.copy();
    }

    public int getWaterCost() {
        return this.waterCost;
    }

    public boolean matchesCatalyst(ItemStack stack) {
        return this.catalyst.test(stack);
    }

    public boolean matches(AspectList availableAspects, ItemStack catalystStack) {
        return matchesCatalyst(catalystStack) && availableAspects.containsAtLeast(this.requiredAspects);
    }

    @Override
    public boolean matches(Container container, Level level) {
        return this.catalyst.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CRUCIBLE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CRUCIBLE_TYPE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.catalyst);
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
