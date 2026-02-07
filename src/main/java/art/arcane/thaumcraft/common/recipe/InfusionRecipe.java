package art.arcane.thaumcraft.common.recipe;

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

import java.util.List;

public class InfusionRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final Ingredient catalyst;
    private final List<Ingredient> components;
    private final ItemStack result;
    private final int instability;
    private final float visCost;

    public InfusionRecipe(
            ResourceLocation id,
            Ingredient catalyst,
            List<Ingredient> components,
            ItemStack result,
            int instability,
            float visCost
    ) {
        this.id = id;
        this.catalyst = catalyst;
        this.components = List.copyOf(components);
        this.result = result;
        this.instability = instability;
        this.visCost = visCost;
    }

    public Ingredient getCatalyst() {
        return this.catalyst;
    }

    public List<Ingredient> getComponents() {
        return this.components;
    }

    public ItemStack getResult() {
        return this.result.copy();
    }

    public int getInstability() {
        return this.instability;
    }

    public float getVisCost() {
        return this.visCost;
    }

    public int getComponentCount() {
        return this.components.size();
    }

    public boolean matchesCatalyst(ItemStack stack) {
        return this.catalyst.test(stack);
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
        return ModRecipes.INFUSION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.INFUSION_TYPE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(this.components.size() + 1, Ingredient.EMPTY);
        ingredients.set(0, this.catalyst);
        for (int i = 0; i < this.components.size(); i++) {
            ingredients.set(i + 1, this.components.get(i));
        }
        return ingredients;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
