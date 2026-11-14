package mob_grinding_utils.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public record SolidifyRecipe(Ingredient mould, ItemStack result, int fluidAmount) implements Recipe<RecipeInput> {
    public static final String NAME = "solidify";

    public static final MapCodec<SolidifyRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.mould),
                    ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                    Codec.INT.fieldOf("fluidAmount").forGetter(recipe -> recipe.fluidAmount))
            .apply(instance, SolidifyRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SolidifyRecipe> STREAM_CODEC = StreamCodec.of(
            SolidifyRecipe::toNetwork, SolidifyRecipe::fromNetwork
    );
    public static final RecipeSerializer<SolidifyRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(@Nonnull RecipeInput inv, @Nonnull Level worldIn) {
        return false;
    }

    public boolean matches(ItemStack input) {
        return this.mould.test(input);
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull RecipeInput inv) {
        return result.copy();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public ItemStack result() {
        return result.copy();
    }


    @Nonnull
    public ItemStack getResultItem() {
        return result;
    }

    @Nonnull
    @Override
    public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
        return MobGrindingUtils.SOLIDIFIER_RECIPE.get();
    }

    @Nonnull
    @Override
    public RecipeType<? extends Recipe<RecipeInput>> getType() {
        return MobGrindingUtils.SOLIDIFIER_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Nonnull
    public static SolidifyRecipe fromNetwork(@Nonnull RegistryFriendlyByteBuf buffer) {
        Ingredient mould = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
        int fluidAmount = buffer.readInt();
        return new SolidifyRecipe(mould, result, fluidAmount);
    }

    public static void toNetwork(@Nonnull RegistryFriendlyByteBuf buffer, SolidifyRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.mould);
        ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        buffer.writeInt(recipe.fluidAmount);
    }
}
