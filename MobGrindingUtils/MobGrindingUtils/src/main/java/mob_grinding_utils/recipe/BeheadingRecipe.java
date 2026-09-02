package mob_grinding_utils.recipe;

import com.google.gson.JsonParseException;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.Optional;

public class BeheadingRecipe implements Recipe<EmptyInput>{
    public static final String NAME = "beheading";
    private final EntityType<?> entityType;
    private final ItemStack result;

    public static final MapCodec<BeheadingRecipe> CODEC = RecordCodecBuilder.mapCodec((p_300958_) -> p_300958_
            .group(BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity")
                            .forGetter((p_300960_) -> p_300960_.entityType),
                    ItemStack.CODEC.fieldOf("result")
                            .forGetter((p_300962_) -> p_300962_.result))
            .apply(p_300958_, BeheadingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BeheadingRecipe> STREAM_CODEC = StreamCodec.of(
            BeheadingRecipe::toNetwork,
            BeheadingRecipe::fromNetwork);

    public static final RecipeSerializer<BeheadingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    public BeheadingRecipe(EntityType<?> type, ItemStack output) {
        this.entityType = type;
        this.result = output;
    }

    @Override
    public boolean matches(@Nonnull EmptyInput container, @Nonnull Level level) {
        return false;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    public boolean matches(EntityType<?> typeIn) {
        return typeIn == entityType;
    }

    @Nonnull
    @Override
    public ItemStack assemble(EmptyInput input) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    public ItemStack getResultItem() {
        return result.copy();
    }

    @Nonnull
    @Override
    public RecipeSerializer<? extends Recipe<EmptyInput>> getSerializer() {
        return MobGrindingUtils.BEHEADING_RECIPE.get();
    }

    @Nonnull
    @Override
    public RecipeType<? extends Recipe<EmptyInput>> getType() {
        return MobGrindingUtils.BEHEADING_TYPE.get();
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
    public static BeheadingRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
        Identifier entityRes = Identifier.parse(buf.readUtf());
        Optional<EntityType<?>> type = BuiltInRegistries.ENTITY_TYPE.getOptional(entityRes);
        if (type.isEmpty())
            throw new JsonParseException("unknown entity type");
        ItemStack result = ItemStack.STREAM_CODEC.decode(buf);

        return new BeheadingRecipe(type.get(), result);
    }

    public static void toNetwork(RegistryFriendlyByteBuf buf, BeheadingRecipe recipe) {
        buf.writeUtf(BuiltInRegistries.ENTITY_TYPE.getKey(recipe.entityType).toString());
        ItemStack.STREAM_CODEC.encode(buf, recipe.result);
    }
}
