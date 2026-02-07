package art.arcane.thaumcraft.datagen.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.function.Consumer;

public class ThaumcraftRecipeProvider extends RecipeProvider {

    public ThaumcraftRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeWriter) {
        // Phase 1+ will add migrated recipes.
    }
}
