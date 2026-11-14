package mob_grinding_utils.client.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.recipe.SolidifyRecipe;
import mob_grinding_utils.util.RL;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class SolidifierCategory extends AbstractRecipeCategory<SolidifyRecipe> {
    private final IDrawableStatic background;

    public SolidifierCategory(IGuiHelper guiHelper) {
        super(JEIPlugin.SOLIDIFY_TYPE, Component.translatable("mob_grinding_utils.jei.solidifier"), null, 91, 26);
        background = guiHelper.drawableBuilder(RL.mgu("textures/gui/solidifier_jei.png"), 0, 0, 91, 26).setTextureSize(91, 26).build();
    }

    @Override
    public void draw(SolidifyRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SolidifyRecipe recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 5,5)
            .add(recipe.mould());

        builder.addSlot(RecipeIngredientRole.INPUT, 37, 5)
            .addIngredients(NeoForgeTypes.FLUID_STACK, List.of(new FluidStack(ModBlocks.FLUID_XP.get(), recipe.fluidAmount())))
            .addRichTooltipCallback((recipeSlot, tooltip) -> {
                var ingredient = recipeSlot.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK);
                ingredient.ifPresent(fluidStack -> {
                    tooltip.add(Component.literal(fluidStack.getAmount() + " mB"));
                    tooltip.add(Component.translatable("mob_grinding_utils.jei.any_experience").withStyle(ChatFormatting.GRAY));
                });
            });
        builder.addSlot(RecipeIngredientRole.OUTPUT, 70, 5)
            .add(recipe.getResultItem().copy());
    }
}
