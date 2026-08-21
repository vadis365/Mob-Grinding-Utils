package mob_grinding_utils.client.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import mob_grinding_utils.recipe.SolidifyRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import javax.annotation.Nonnull;

public class SolidifierCategory implements IRecipeCategory<SolidifyRecipe> {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(Reference.MOD_ID, "solidifier_jei");

    private final IDrawableStatic background;

    public SolidifierCategory(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/solidifier_jei.png"), 0, 0, 91, 26).setTextureSize(91, 26).build();
    }

    @Nonnull
    @Override
    public IRecipeType<SolidifyRecipe> getRecipeType() {
        return JEIPlugin.SOLIDIFY_TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable("mob_grinding_utiles.jei.solidifier");
    }

    @Nonnull
    @Override
	public int getWidth() {
		return 91;
	}

	@Override
	public int getHeight() {
		return 26;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SolidifyRecipe recipe, @Nonnull IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 5,5)
            .add(recipe.mould());

        builder.addSlot(RecipeIngredientRole.INPUT, 37, 5)
			.add(ModBlocks.FLUID_XP.get(), recipe.fluidAmount())
			.addRichTooltipCallback((recipeSlot, tooltip) -> {
				tooltip.add(Component.literal(recipe.fluidAmount() + " mB"));
				tooltip.add(Component.translatable("mob_grinding_utils.jei.any_experience").withStyle(ChatFormatting.GRAY));
            });
        builder.addSlot(RecipeIngredientRole.OUTPUT, 70, 5)
			.add(recipe.result());
    }
}
