package mob_grinding_utils.recipe;

import com.google.gson.JsonParseException;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.Optional;

public class BeheadingRecipe implements Recipe<EmptyInput>{
    public static final String NAME = "beheading";
    private final EntityType<?> entityType;
    private final ItemStack result;

    public BeheadingRecipe(EntityType<?> type, ItemStack output) {
        this.entityType = type;
        this.result = output;
    }

    @Override
    public boolean matches(@Nonnull EmptyInput container, @Nonnull Level level) {
        return false;
    }

    public boolean matches(EntityType<?> typeIn) {
        return typeIn == entityType;
    }

    @Nonnull
    @Override
    public ItemStack assemble(EmptyInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return MobGrindingUtils.BEHEADING_RECIPE.get();
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return MobGrindingUtils.BEHEADING_TYPE.get();
    }


    public static class Serializer implements RecipeSerializer<BeheadingRecipe> {
        public static final MapCodec<BeheadingRecipe> CODEC = RecordCodecBuilder.mapCodec((p_300958_) -> p_300958_
                .group(BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity")
                                .forGetter((p_300960_) -> p_300960_.entityType),
                        ItemStack.CODEC.fieldOf("result")
                                .forGetter((p_300962_) -> p_300962_.result))
                .apply(p_300958_, BeheadingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, BeheadingRecipe> STREAM_CODEC = StreamCodec.of(
                BeheadingRecipe.Serializer::toNetwork,
                BeheadingRecipe.Serializer::fromNetwork);

        @Nonnull
        public static BeheadingRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            ResourceLocation entityRes = ResourceLocation.parse(buf.readUtf());
            Optional<EntityType<?>> type = BuiltInRegistries.ENTITY_TYPE.getOptional(entityRes);
            if (type.isEmpty())
                throw new JsonParseException("unknown entity type");
            ItemStack result = ItemStack.STREAM_CODEC.decode(buf);

            return new BeheadingRecipe(type.get(), result);
        }

        @Override
        public MapCodec<BeheadingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BeheadingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static void toNetwork(RegistryFriendlyByteBuf buf, BeheadingRecipe recipe) {
            buf.writeUtf(BuiltInRegistries.ENTITY_TYPE.getKey(recipe.entityType).toString());
            ItemStack.STREAM_CODEC.encode(buf, recipe.result);
        }
    }
}
