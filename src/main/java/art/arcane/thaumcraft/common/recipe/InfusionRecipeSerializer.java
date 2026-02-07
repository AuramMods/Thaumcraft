package art.arcane.thaumcraft.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class InfusionRecipeSerializer implements RecipeSerializer<InfusionRecipe> {

    private static final int DEFAULT_INSTABILITY = 1;
    private static final float DEFAULT_VIS_COST = 50.0F;

    @Override
    public InfusionRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        if (!json.has("catalyst")) {
            throw new JsonSyntaxException("Missing infusion recipe catalyst");
        }
        if (!json.has("components")) {
            throw new JsonSyntaxException("Missing infusion recipe components");
        }

        Ingredient catalyst = Ingredient.fromJson(json.get("catalyst"));
        List<Ingredient> components = readComponentsFromJson(GsonHelper.getAsJsonArray(json, "components"));
        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        int instability = Math.max(0, GsonHelper.getAsInt(json, "instability", DEFAULT_INSTABILITY));
        float visCost = Math.max(1.0F, GsonHelper.getAsFloat(json, "vis", DEFAULT_VIS_COST));

        return new InfusionRecipe(recipeId, catalyst, components, result, instability, visCost);
    }

    @Override
    public @Nullable InfusionRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient catalyst = Ingredient.fromNetwork(buffer);

        int componentCount = buffer.readVarInt();
        List<Ingredient> components = new ArrayList<>(componentCount);
        for (int i = 0; i < componentCount; i++) {
            components.add(Ingredient.fromNetwork(buffer));
        }

        ItemStack result = buffer.readItem();
        int instability = Math.max(0, buffer.readVarInt());
        float visCost = Math.max(1.0F, buffer.readFloat());

        return new InfusionRecipe(recipeId, catalyst, components, result, instability, visCost);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, InfusionRecipe recipe) {
        recipe.getCatalyst().toNetwork(buffer);

        List<Ingredient> components = recipe.getComponents();
        buffer.writeVarInt(components.size());
        for (Ingredient component : components) {
            component.toNetwork(buffer);
        }

        buffer.writeItem(recipe.getResult());
        buffer.writeVarInt(recipe.getInstability());
        buffer.writeFloat(recipe.getVisCost());
    }

    private static List<Ingredient> readComponentsFromJson(JsonArray array) {
        List<Ingredient> components = new ArrayList<>(array.size());
        for (int i = 0; i < array.size(); i++) {
            components.add(Ingredient.fromJson(array.get(i)));
        }

        if (components.isEmpty()) {
            throw new JsonSyntaxException("Infusion recipe must define at least one component");
        }

        return components;
    }
}
