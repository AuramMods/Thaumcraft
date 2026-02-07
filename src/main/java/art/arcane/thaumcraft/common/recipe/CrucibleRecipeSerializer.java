package art.arcane.thaumcraft.common.recipe;

import art.arcane.thaumcraft.common.aspect.AspectList;
import art.arcane.thaumcraft.common.aspect.AspectType;
import com.google.gson.JsonElement;
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
import java.util.Map;

public class CrucibleRecipeSerializer implements RecipeSerializer<CrucibleRecipe> {

    private static final int DEFAULT_WATER_COST = 50;

    @Override
    public CrucibleRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        if (!json.has("catalyst")) {
            throw new JsonSyntaxException("Missing crucible recipe catalyst");
        }

        Ingredient catalyst = Ingredient.fromJson(json.get("catalyst"));
        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        AspectList requiredAspects = readAspectsFromJson(GsonHelper.getAsJsonObject(json, "aspects"));
        int waterCost = Math.max(1, GsonHelper.getAsInt(json, "water", DEFAULT_WATER_COST));

        return new CrucibleRecipe(recipeId, catalyst, result, requiredAspects, waterCost);
    }

    @Override
    public @Nullable CrucibleRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        Ingredient catalyst = Ingredient.fromNetwork(buffer);
        ItemStack result = buffer.readItem();
        AspectList requiredAspects = readAspectsFromNetwork(buffer);
        int waterCost = Math.max(1, buffer.readVarInt());

        return new CrucibleRecipe(recipeId, catalyst, result, requiredAspects, waterCost);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, CrucibleRecipe recipe) {
        recipe.getCatalyst().toNetwork(buffer);
        buffer.writeItem(recipe.getResult());
        writeAspectsToNetwork(buffer, recipe.getRequiredAspects());
        buffer.writeVarInt(recipe.getWaterCost());
    }

    private static AspectList readAspectsFromJson(JsonObject object) {
        AspectList list = new AspectList();

        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            AspectType aspectType = AspectType.byTag(entry.getKey());
            if (aspectType == null) {
                throw new JsonSyntaxException("Unknown aspect tag in crucible recipe: " + entry.getKey());
            }

            int amount = GsonHelper.convertToInt(entry.getValue(), entry.getKey());
            if (amount > 0) {
                list.add(aspectType, amount);
            }
        }

        if (list.isEmpty()) {
            throw new JsonSyntaxException("Crucible recipe must define at least one aspect requirement");
        }

        return list;
    }

    private static void writeAspectsToNetwork(FriendlyByteBuf buffer, AspectList aspects) {
        Map<AspectType, Integer> map = aspects.asMap();
        buffer.writeVarInt(map.size());

        for (Map.Entry<AspectType, Integer> entry : map.entrySet()) {
            buffer.writeUtf(entry.getKey().getTag());
            buffer.writeVarInt(entry.getValue());
        }
    }

    private static AspectList readAspectsFromNetwork(FriendlyByteBuf buffer) {
        int size = buffer.readVarInt();
        AspectList list = new AspectList();

        for (int i = 0; i < size; i++) {
            String tag = buffer.readUtf();
            int amount = buffer.readVarInt();

            AspectType aspectType = AspectType.byTag(tag);
            if (aspectType != null && amount > 0) {
                list.add(aspectType, amount);
            }
        }

        return list;
    }
}
