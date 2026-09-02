package mob_grinding_utils.recipe;


import com.mojang.serialization.MapCodec;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.components.MGUComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;


public class ChickenFeedRecipe implements CraftingRecipe {
    public static final String NAME = "chicken_feed";
    private final ShapelessRecipe inner;
    public static final MapCodec<ChickenFeedRecipe> CODEC = ShapelessRecipe.MAP_CODEC.xmap(ChickenFeedRecipe::new, recipe -> recipe.inner);
    public static final StreamCodec<RegistryFriendlyByteBuf, ChickenFeedRecipe> STREAM_CODEC = StreamCodec.of(
            ChickenFeedRecipe::toNetwork, ChickenFeedRecipe::fromNetwork
    );

    public static final RecipeSerializer<ChickenFeedRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);


    public ChickenFeedRecipe(ShapelessRecipe recipe) {
        this.inner = recipe;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return inner.matches(input, level);
    }

    @Nonnull
    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack result = inner.assemble(input);

        ItemStack swabItem = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
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


    public static ChickenFeedRecipe fromNetwork(@Nonnull RegistryFriendlyByteBuf buffer) {
        return new ChickenFeedRecipe(ShapelessRecipe.STREAM_CODEC.decode(buffer));
    }

    public static void toNetwork(@Nonnull RegistryFriendlyByteBuf buffer, @Nonnull ChickenFeedRecipe recipe) {
        try {
            ShapelessRecipe.STREAM_CODEC.encode(buffer, recipe.inner);
        }
        catch (Exception exception) {
            MobGrindingUtils.LOGGER.info("Error writing "+ NAME +" Recipe to packet: ", exception);
            throw exception;
        }
    }

    @Override
    public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return MobGrindingUtils.CHICKEN_FEED.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return inner.placementInfo();
    }

    @Override
    public CraftingBookCategory category() {
        return inner.category();
    }

    @Override
    public boolean showNotification() {
        return inner.showNotification();
    }

    @Override
    public String group() {
        return inner.group();
    }

}
