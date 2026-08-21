package mob_grinding_utils.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public record SolidifyRecipe(Ingredient mould, ItemStackTemplate resultTemplate, int fluidAmount) implements Recipe<RecipeInput> {
    public static final String NAME = "solidify";

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
        return resultTemplate.create();
    }

    public ItemStack result() {
        return resultTemplate.create();
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
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Nonnull
    @Override
    public RecipeSerializer<SolidifyRecipe> getSerializer() {
        return MobGrindingUtils.SOLIDIFIER_RECIPE.get();
    }

    @Nonnull
    @Override
    public RecipeType<SolidifyRecipe> getType() {
        return MobGrindingUtils.SOLIDIFIER_TYPE.get();
    }

    public static final class Serializer {
        public static final MapCodec<SolidifyRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
                .group(Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.mould),
                        ItemStackTemplate.CODEC.fieldOf("result").forGetter(SolidifyRecipe::resultTemplate),
                        Codec.INT.fieldOf("fluidAmount").forGetter(recipe -> recipe.fluidAmount))
                .apply(instance, SolidifyRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SolidifyRecipe> STREAM_CODEC = StreamCodec.of(
                SolidifyRecipe.Serializer::toNetwork, SolidifyRecipe.Serializer::fromNetwork
        );
        public static final RecipeSerializer<SolidifyRecipe> INSTANCE = new RecipeSerializer<>(CODEC, STREAM_CODEC);

        @Nonnull
        public static SolidifyRecipe fromNetwork(@Nonnull RegistryFriendlyByteBuf buffer) {
            Ingredient mould = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStackTemplate result = ItemStackTemplate.STREAM_CODEC.decode(buffer);
            int fluidAmount = buffer.readInt();
            return new SolidifyRecipe(mould, result, fluidAmount);
        }

        public static void toNetwork(@Nonnull RegistryFriendlyByteBuf buffer, SolidifyRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.mould);
            ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.resultTemplate);
            buffer.writeInt(recipe.fluidAmount);
        }
    }
}
