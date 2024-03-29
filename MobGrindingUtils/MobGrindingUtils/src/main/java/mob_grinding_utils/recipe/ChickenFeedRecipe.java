package mob_grinding_utils.recipe;


import com.google.gson.JsonObject;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class ChickenFeedRecipe extends ShapelessRecipe {
    public static final String NAME = "chicken_feed";
    public ChickenFeedRecipe(ShapelessRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.getResultItem(), recipe.getIngredients());
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull CraftingContainer inv) {
        ItemStack result = super.assemble(inv);

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

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ChickenFeedRecipe> {
        @Nullable
        @Override
        public ChickenFeedRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            return new ChickenFeedRecipe(RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(recipeId, buffer));
        }

        @Nonnull
        @Override
        public ChickenFeedRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            try {
                return new ChickenFeedRecipe(RecipeSerializer.SHAPELESS_RECIPE.fromJson(recipeId, json));
            }
            catch (Exception exception) {
                LogManager.getLogger().info("Error reading "+ NAME +" Recipe from packet: ", exception);
                throw exception;
            }
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull ChickenFeedRecipe recipe) {
            try {
                RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buffer, recipe);
            }
            catch (Exception exception) {
                LogManager.getLogger().info("Error writing "+ NAME +" Recipe to packet: ", exception);
                throw exception;
            }
        }
    }
}
