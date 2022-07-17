package mob_grinding_utils.recipe;

import com.google.gson.JsonObject;
import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class SolidifyRecipe implements Recipe<Container> {
    private final Ingredient mould;
    private final ItemStack result;
    private final int fluidAmount;
    private final ResourceLocation id;
    public static final String NAME = "solidify";

    public SolidifyRecipe(ResourceLocation id, Ingredient mould, ItemStack result, int fluidAmount) {
        this.id = id;
        this.mould = mould;
        this.result = result;
        this.fluidAmount = fluidAmount;
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return false;
    }

    public boolean matches(ItemStack input) {
        return this.mould.test(input);
    }

    public int getFluidAmount() {
        return this.fluidAmount;
    }

    public Ingredient getMould() {
        return this.mould;
    }

    @Nonnull
    @Override
    public ItemStack assemble(Container inv) {
        return result.copy();
    }

    public ItemStack getResult() {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem() {
        return result;
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return MobGrindingUtils.SOLIDIFIER_RECIPE.get();
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return MobGrindingUtils.SOLIDIFIER_TYPE.get();
    }

    public static class DataRecipe implements FinishedRecipe {
        private final Ingredient mould;
        private final ItemStack result;
        private final int fluidAmount;
        private final ResourceLocation id;

        public DataRecipe(ResourceLocation id, Ingredient mould, ItemStack result, int fluidAmount) {
            this.mould = mould;
            this.result = result;
            this.fluidAmount = fluidAmount;
            this.id = id;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("ingredient", this.mould.toJson());
            json.addProperty("fluidAmount", this.fluidAmount);
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.result.getItem())).toString());
            json.add("result", resultJson);
        }

        @Nonnull
        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Nonnull
        @Override
        public RecipeSerializer<?> getType() {
            return MobGrindingUtils.SOLIDIFIER_RECIPE.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }

    public static class Serializer implements RecipeSerializer<SolidifyRecipe> {

        @Nonnull
        @Override
        public SolidifyRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            ItemStack result = new ItemStack(GsonHelper.getAsItem(json.get("result").getAsJsonObject(), "item"));
            int fluidAmount = json.get("fluidAmount").getAsInt();
            return new SolidifyRecipe(recipeId, ingredient, result, fluidAmount);
        }

        @Override
        public SolidifyRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            Ingredient mould = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int fluidAmount = buffer.readInt();
            return new SolidifyRecipe(recipeId, mould, result, fluidAmount);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, SolidifyRecipe recipe) {
            recipe.mould.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.fluidAmount);
        }
    }
}
