package mob_grinding_utils.recipe;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;

public class ChickenFeedRecipe extends ShapelessRecipe {
    public ChickenFeedRecipe(ResourceLocation idIn, String groupIn, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
        super(idIn, groupIn, recipeOutputIn, recipeItemsIn);
    }

    public ChickenFeedRecipe(ShapelessRecipe recipe) {
        super(recipe.getId(), recipe.getGroup(), recipe.getRecipeOutput(), recipe.getIngredients());
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        return super.getCraftingResult(inv);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
        return super.getRemainingItems(inv);
    }

    public boolean matches(CraftingInventory inv, World worldIn) {
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        //NonNullList<Ingredient> ingredients = new NonNullList<Ingredient>.from(this.getIngredients());
        int i = 0;

        for(int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack = inv.getStackInSlot(j);
            if (!itemstack.isEmpty()) {
                ++i;
                inputs.add(itemstack);
            }
        }

        return i == this.getIngredients().size() && net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.getIngredients()) != null;    }
}
