package mob_grinding_utils;

import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.DataPackRegistries;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class ServerResourceReloader implements IResourceManagerReloadListener {
    private final DataPackRegistries dataPackRegistries;
    public ServerResourceReloader(DataPackRegistries dataPackRegistries) {
        this.dataPackRegistries = dataPackRegistries;
    }
    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        RecipeManager recipeManager = this.dataPackRegistries.getRecipeManager();
        MobGrindingUtils.SOLIDIFIER_RECIPES.clear();
        MobGrindingUtils.SOLIDIFIER_RECIPES.addAll(recipeManager.getRecipesForType(MobGrindingUtils.SOLIDIFIER_TYPE));
    }
}
