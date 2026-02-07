package art.arcane.thaumcraft.common.registry;

import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.common.recipe.CrucibleRecipe;
import art.arcane.thaumcraft.common.recipe.CrucibleRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Thaumcraft.MODID);

    public static final RegistryObject<RecipeSerializer<CrucibleRecipe>> CRUCIBLE =
            RECIPE_SERIALIZERS.register("crucible", CrucibleRecipeSerializer::new);

    public static final RecipeType<CrucibleRecipe> CRUCIBLE_TYPE = new RecipeType<>() {
        @Override
        public String toString() {
            return Thaumcraft.MODID + ":crucible";
        }
    };

    private ModRecipes() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
