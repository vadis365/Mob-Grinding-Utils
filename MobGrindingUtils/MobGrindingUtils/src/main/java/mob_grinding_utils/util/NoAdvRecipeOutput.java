package mob_grinding_utils.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;

import javax.annotation.Nonnull;

public class NoAdvRecipeOutput implements RecipeOutput {
    private final RecipeOutput inner;
    public NoAdvRecipeOutput(RecipeOutput output) {
        inner = output;
    }

    @Nonnull
    @Override
    public Advancement.Builder advancement() {
        return inner.advancement();
    }

    @Override
    public void includeRootAdvancement() {

    }

    @Override
    public void accept(ResourceKey<Recipe<?>> key, Recipe<?> recipe, @org.jspecify.annotations.Nullable AdvancementHolder advancement, ICondition... conditions) {
        inner.accept(key, recipe, null, conditions);
    }
}
