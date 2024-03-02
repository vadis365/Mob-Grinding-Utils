package mob_grinding_utils.recipe;


import com.mojang.serialization.Codec;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class ChickenFeedRecipe extends ShapelessRecipe {
    public static final String NAME = "chicken_feed";
    public ChickenFeedRecipe(ShapelessRecipe recipe) {
        super(recipe.getGroup(), recipe.category(), recipe.getResultItem(RegistryAccess.EMPTY), recipe.getIngredients());
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull CraftingContainer inv, RegistryAccess lol) {
        ItemStack result = super.assemble(inv, lol);

        ItemStack swabItem = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() == ModItems.MOB_SWAB_USED.get())
                    swabItem = stack;
            }
        }


        if(swabItem.hasTag() && swabItem.getTag().contains("mguMobName"))
            result.getOrCreateTag().putString("mguMobName", swabItem.getTag().getString("mguMobName"));
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
        public static final Codec<ChickenFeedRecipe> CODEC = ShapelessRecipe.Serializer.CODEC.xmap(ChickenFeedRecipe::new, recipe -> recipe);

        @Nonnull
        @Override
        public Codec<ChickenFeedRecipe> codec() {
            return CODEC;
        }

        @Nullable
        @Override
        public ChickenFeedRecipe fromNetwork(@Nonnull FriendlyByteBuf buffer) {
            return new ChickenFeedRecipe(RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(buffer));
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull ChickenFeedRecipe recipe) {
            try {
                RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buffer, recipe);
            }
            catch (Exception exception) {
                MobGrindingUtils.LOGGER.info("Error writing "+ NAME +" Recipe to packet: ", exception);
                throw exception;
            }
        }
    }
}
