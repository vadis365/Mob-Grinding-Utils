package mob_grinding_utils.recipe;


import com.mojang.serialization.MapCodec;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.components.MGUComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import javax.annotation.Nonnull;


public class ChickenFeedRecipe implements CraftingRecipe {
    public static final String NAME = "chicken_feed";
    private final ShapelessRecipe recipe;
    public ChickenFeedRecipe(ShapelessRecipe recipe) { this.recipe = recipe; }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull CraftingInput inv) {
        ItemStack result = recipe.assemble(inv);

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
    public RecipeSerializer<ChickenFeedRecipe> getSerializer() {
        return MobGrindingUtils.CHICKEN_FEED.get();
    }

    @Override public boolean matches(CraftingInput input, net.minecraft.world.level.Level level) { return recipe.matches(input, level); }
    @Override public net.minecraft.world.item.crafting.CraftingBookCategory category() { return recipe.category(); }
    @Override public String group() { return recipe.group(); }
    @Override public boolean showNotification() { return recipe.showNotification(); }
    @Override public net.minecraft.world.item.crafting.PlacementInfo placementInfo() { return recipe.placementInfo(); }
    @Override public net.minecraft.world.item.crafting.RecipeBookCategory recipeBookCategory() { return recipe.recipeBookCategory(); }

    public static final class Serializer {
        public static final MapCodec<ChickenFeedRecipe> CODEC = ShapelessRecipe.MAP_CODEC.xmap(ChickenFeedRecipe::new, recipe -> recipe.recipe);
        public static final StreamCodec<RegistryFriendlyByteBuf, ChickenFeedRecipe> STREAM_CODEC = StreamCodec.of(
            ChickenFeedRecipe.Serializer::toNetwork, ChickenFeedRecipe.Serializer::fromNetwork
        );
        public static final RecipeSerializer<ChickenFeedRecipe> INSTANCE = new RecipeSerializer<>(CODEC, STREAM_CODEC);

        @Nonnull
        public static ChickenFeedRecipe fromNetwork(@Nonnull RegistryFriendlyByteBuf buffer) {
            return new ChickenFeedRecipe(ShapelessRecipe.STREAM_CODEC.decode(buffer));
        }

        public static void toNetwork(@Nonnull RegistryFriendlyByteBuf buffer, @Nonnull ChickenFeedRecipe recipe) {
            try {
                ShapelessRecipe.STREAM_CODEC.encode(buffer, recipe.recipe);
            }
            catch (Exception exception) {
                MobGrindingUtils.LOGGER.info("Error writing "+ NAME +" Recipe to packet: ", exception);
                throw exception;
            }
        }
    }
}
