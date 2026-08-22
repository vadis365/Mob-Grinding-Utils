package mob_grinding_utils.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;

import javax.annotation.Nonnull;
import java.util.function.Function;
@SuppressWarnings("unchecked")
public class RecipeInjector<T extends Recipe<?>> implements RecipeOutput {
    private final RecipeOutput inner;
    private final Function<T, ? extends T> constructor;
    public RecipeInjector(RecipeOutput output, Function<T, ? extends T> constructorIn) {
        inner = output;
        this.constructor = constructorIn;
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
        inner.accept(key, constructor.apply((T) recipe), advancement, conditions);
    }
}
