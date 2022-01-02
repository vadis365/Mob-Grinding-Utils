package mob_grinding_utils.recipe;

import com.google.gson.JsonObject;
import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SolidifyRecipe implements IRecipe<IInventory> {
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
    public boolean matches(IInventory inv, World worldIn) {
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
    public ItemStack getCraftingResult(IInventory inv) {
        return result.copy();
    }

    public ItemStack getResult() {
        return result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return result;
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Nonnull
    @Override
    public IRecipeSerializer<?> getSerializer() {
        return MobGrindingUtils.SOLIDIFIER_RECIPE.get();
    }

    @Nonnull
    @Override
    public IRecipeType<?> getType() {
        return MobGrindingUtils.SOLIDIFIER_TYPE;
    }

    public static class FinishedRecipe implements IFinishedRecipe {
        private final Ingredient mould;
        private final ItemStack result;
        private final int fluidAmount;
        private final ResourceLocation id;

        public FinishedRecipe(ResourceLocation id, Ingredient mould, ItemStack result, int fluidAmount) {
            this.mould = mould;
            this.result = result;
            this.fluidAmount = fluidAmount;
            this.id = id;
        }

        @Override
        public void serialize(JsonObject json) {
            json.add("ingredient", this.mould.serialize());
            json.addProperty("fluidAmount", this.fluidAmount);
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", this.result.getItem().getRegistryName().toString());
            json.add("result", resultJson);
        }

        @Nonnull
        @Override
        public ResourceLocation getID() {
            return id;
        }

        @Nonnull
        @Override
        public IRecipeSerializer<?> getSerializer() {
            return MobGrindingUtils.SOLIDIFIER_RECIPE.get();
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SolidifyRecipe> {

        @Nonnull
        @Override
        public SolidifyRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "ingredient"));
            ItemStack result = new ItemStack(JSONUtils.getItem(json.get("result").getAsJsonObject(), "item"));
            int fluidAmount = json.get("fluidAmount").getAsInt();
            return new SolidifyRecipe(recipeId, ingredient, result, fluidAmount);
        }

        @Override
        public SolidifyRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
            Ingredient mould = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();
            int fluidAmount = buffer.readInt();
            return new SolidifyRecipe(recipeId, mould, result, fluidAmount);
        }

        @Override
        public void write(@Nonnull PacketBuffer buffer, SolidifyRecipe recipe) {
            recipe.mould.write(buffer);
            buffer.writeItemStack(recipe.result);
            buffer.writeInt(recipe.fluidAmount);
        }
    }
}
