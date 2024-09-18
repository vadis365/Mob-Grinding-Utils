package mob_grinding_utils.recipe;


import com.mojang.serialization.MapCodec;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.components.MGUComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import javax.annotation.Nonnull;


public class ChickenFeedRecipe extends ShapelessRecipe {
    public static final String NAME = "chicken_feed";
    public ChickenFeedRecipe(ShapelessRecipe recipe) {
        super(recipe.getGroup(), recipe.category(), recipe.getResultItem(RegistryAccess.EMPTY), recipe.getIngredients());
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull CraftingInput inv, @Nonnull HolderLookup.Provider lol) {
        ItemStack result = super.assemble(inv, lol);

        ItemStack swabItem = ItemStack.EMPTY;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() == ModItems.MOB_SWAB_USED.get())
                    swabItem = stack;
            }
        }


        if(swabItem.has(MGUComponents.MOB_DNA))
            result.set(MGUComponents.MOB_DNA, swabItem.get(MGUComponents.MOB_DNA));
        else
            return ItemStack.EMPTY;

        return result;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return MobGrindingUtils.CHICKEN_FEED.get();
    }

    public static class Serializer implements RecipeSerializer<ChickenFeedRecipe> {
        public static final MapCodec<ChickenFeedRecipe> CODEC = ShapelessRecipe.Serializer.CODEC.xmap(ChickenFeedRecipe::new, recipe -> recipe);
        public static final StreamCodec<RegistryFriendlyByteBuf, ChickenFeedRecipe> STREAM_CODEC = StreamCodec.of(
            ChickenFeedRecipe.Serializer::toNetwork, ChickenFeedRecipe.Serializer::fromNetwork
        );

        @Nonnull
        @Override
        public MapCodec<ChickenFeedRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ChickenFeedRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static ChickenFeedRecipe fromNetwork(@Nonnull RegistryFriendlyByteBuf buffer) {
            return new ChickenFeedRecipe(RecipeSerializer.SHAPELESS_RECIPE.streamCodec().decode(buffer));
        }

        public static void toNetwork(@Nonnull RegistryFriendlyByteBuf buffer, @Nonnull ChickenFeedRecipe recipe) {
            try {
                RecipeSerializer.SHAPELESS_RECIPE.streamCodec().encode(buffer, recipe);
            }
            catch (Exception exception) {
                MobGrindingUtils.LOGGER.info("Error writing "+ NAME +" Recipe to packet: ", exception);
                throw exception;
            }
        }
    }
}
