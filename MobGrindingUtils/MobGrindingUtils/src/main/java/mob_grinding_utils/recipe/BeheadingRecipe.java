package mob_grinding_utils.recipe;

import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.Optional;

public class BeheadingRecipe implements Recipe<Container>{
    public static final String NAME = "beheading";
    private final EntityType<?> entityType;
    private final ItemStack result;

    public BeheadingRecipe(EntityType<?> type, ItemStack output) {
        this.entityType = type;
        this.result = output;
    }

    @Override
    public boolean matches(@Nonnull Container container, @Nonnull Level level) {
        return false;
    }

    public boolean matches(EntityType<?> typeIn) {
        return typeIn == entityType;
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull Container container, RegistryAccess pointless) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem(RegistryAccess pointless) {
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
        public static final Codec<BeheadingRecipe> CODEC = RecordCodecBuilder.create((p_300958_) -> p_300958_
                .group(BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity")
                                .forGetter((p_300960_) -> p_300960_.entityType),
                        ItemStack.CODEC.fieldOf("result")
                                .forGetter((p_300962_) -> p_300962_.result))
                .apply(p_300958_, BeheadingRecipe::new));

        @Nonnull
        @Override
        public BeheadingRecipe fromNetwork(FriendlyByteBuf buf) {
            ResourceLocation entityRes = new ResourceLocation(buf.readUtf());
            Optional<EntityType<?>> type = BuiltInRegistries.ENTITY_TYPE.getOptional(entityRes);
            if (type.isEmpty())
                throw new JsonParseException("unknown entity type");
            ItemStack result = buf.readItem();

            return new BeheadingRecipe(type.get(), result);
        }

        @Override
        public Codec<BeheadingRecipe> codec() {
            return CODEC;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BeheadingRecipe recipe) {
            buf.writeUtf(BuiltInRegistries.ENTITY_TYPE.getKey(recipe.entityType).toString());
            buf.writeItem(recipe.result);
        }
    }
}
