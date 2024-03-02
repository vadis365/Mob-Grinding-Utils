package mob_grinding_utils.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public record SolidifyRecipe(Ingredient mould, ItemStack result, int fluidAmount) implements Recipe<Container> {
    public static final String NAME = "solidify";

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return false;
    }

    public boolean matches(ItemStack input) {
        return this.mould.test(input);
    }

    @Nonnull
    @Override
    public ItemStack assemble(Container inv, RegistryAccess pointless) {
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
    public ItemStack getResultItem(RegistryAccess pointless) {
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
        public static final Codec<SolidifyRecipe> CODEC = RecordCodecBuilder.create(instance -> instance
                .group(Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.mould),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.INT.fieldOf("fluidAmount").forGetter(recipe -> recipe.fluidAmount))
                .apply(instance, SolidifyRecipe::new));

        @Nonnull
        @Override
        public Codec<SolidifyRecipe> codec() {
            return CODEC;
        }

        @Nonnull
        @Override
        public SolidifyRecipe fromNetwork(@Nonnull FriendlyByteBuf buffer) {
            Ingredient mould = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int fluidAmount = buffer.readInt();
            return new SolidifyRecipe(mould, result, fluidAmount);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, SolidifyRecipe recipe) {
            recipe.mould.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.fluidAmount);
        }
    }
}
