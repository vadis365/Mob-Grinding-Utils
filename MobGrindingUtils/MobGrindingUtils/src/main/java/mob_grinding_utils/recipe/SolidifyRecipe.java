package mob_grinding_utils.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public record SolidifyRecipe(Ingredient mould, ItemStack result, int fluidAmount) implements Recipe<RecipeInput> {
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
    public ItemStack assemble(@Nonnull RecipeInput inv, @Nonnull HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public ItemStack result() {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem(@Nonnull HolderLookup.Provider registries) {
        return result;
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

    public static class Serializer implements RecipeSerializer<SolidifyRecipe> {
        public static final MapCodec<SolidifyRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
                .group(Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.mould),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.INT.fieldOf("fluidAmount").forGetter(recipe -> recipe.fluidAmount))
                .apply(instance, SolidifyRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SolidifyRecipe> STREAM_CODEC = StreamCodec.of(
                SolidifyRecipe.Serializer::toNetwork, SolidifyRecipe.Serializer::fromNetwork
        );

        @Nonnull
        @Override
        public MapCodec<SolidifyRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SolidifyRecipe> streamCodec() {
            return STREAM_CODEC;
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
}
