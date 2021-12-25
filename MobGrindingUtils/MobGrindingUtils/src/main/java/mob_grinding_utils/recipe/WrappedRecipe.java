package mob_grinding_utils.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class WrappedRecipe implements IFinishedRecipe {
    IFinishedRecipe inner;
    IRecipeSerializer<?> serializerOverride;

    public WrappedRecipe(IFinishedRecipe innerIn) {
        inner = innerIn;
    }

    public WrappedRecipe(IFinishedRecipe innerIn, IRecipeSerializer<?> serializerOverrideIn) {
        inner = innerIn;
        serializerOverride = serializerOverrideIn;
    }

    public static Consumer<IFinishedRecipe> Inject(Consumer<IFinishedRecipe> consumer, IRecipeSerializer<?> serializer) {
        return iFinishedRecipe -> consumer.accept(new WrappedRecipe(iFinishedRecipe, serializer));
    }

    @Override
    public void serialize(JsonObject json) {
        inner.serialize(json);
    }

    @Override
    public JsonObject getRecipeJson() {
        JsonObject jsonObject = new JsonObject();

        if (serializerOverride != null)
            jsonObject.addProperty("type", serializerOverride.getRegistryName().toString());
        else
            jsonObject.addProperty("type", inner.getSerializer().getRegistryName().toString());
        serialize(jsonObject);
        return jsonObject;
    }

    @Override
    public ResourceLocation getID() {
        return inner.getID();
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return serializerOverride != null? serializerOverride:inner.getSerializer();
    }

    @Nullable
    @Override
    public JsonObject getAdvancementJson() {
        return inner.getAdvancementJson();
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementID() {
        return inner.getAdvancementID();
    }
}
