package mob_grinding_utils.client.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.Reference;
import mob_grinding_utils.recipe.SolidifyRecipe;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "jei_plugin");
    public static final IRecipeType<SolidifyRecipe> SOLIDIFY_TYPE = IRecipeType.create(Reference.MOD_ID, "solidify", SolidifyRecipe.class);

    @Nonnull
    @Override
    public Identifier getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        ModItems.ITEMS.getEntries().forEach((item) -> {
            String key = item.get().getDescriptionId()+".jei.info";
            if (I18n.exists(key)) {
                registration.addIngredientInfo(new ItemStack(item.get()), VanillaTypes.ITEM_STACK, Component.translatable(key));
            }
        });
		registration.addRecipes(SOLIDIFY_TYPE, MobGrindingUtils.SOLIDIFIER_RECIPES.stream().map(holder -> holder.value()).toList());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SolidifierCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(SOLIDIFY_TYPE, ModBlocks.XPSOLIDIFIER.getItem());
    }
}
