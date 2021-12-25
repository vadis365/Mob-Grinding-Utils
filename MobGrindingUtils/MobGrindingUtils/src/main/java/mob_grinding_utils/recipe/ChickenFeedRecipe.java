package mob_grinding_utils.recipe;


import com.google.gson.JsonObject;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class ChickenFeedRecipe extends ShapelessRecipe {
    public static final String NAME = "chicken_feed";
    public ChickenFeedRecipe(ShapelessRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.getRecipeOutput(), recipe.getIngredients());
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
        ItemStack result = super.getCraftingResult(inv);

        ItemStack swabItem = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
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
    public IRecipeSerializer<?> getSerializer() {
        return MobGrindingUtils.CHICKEN_FEED.get();
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ChickenFeedRecipe> {
        @Nullable
        @Override
        public ChickenFeedRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
            return new ChickenFeedRecipe(IRecipeSerializer.CRAFTING_SHAPELESS.read(recipeId, buffer));
        }

        @Nonnull
        @Override
        public ChickenFeedRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            try {
                return new ChickenFeedRecipe(IRecipeSerializer.CRAFTING_SHAPELESS.read(recipeId, json));
            }
            catch (Exception exception) {
                LogManager.getLogger().info("Error reading "+ NAME +" Recipe from packet: ", exception);
                throw exception;
            }
        }

        @Override
        public void write(@Nonnull PacketBuffer buffer, @Nonnull ChickenFeedRecipe recipe) {
            try {
                IRecipeSerializer.CRAFTING_SHAPELESS.write(buffer, recipe);
            }
            catch (Exception exception) {
                LogManager.getLogger().info("Error writing "+ NAME +" Recipe to packet: ", exception);
                throw exception;
            }
        }
    }
}
