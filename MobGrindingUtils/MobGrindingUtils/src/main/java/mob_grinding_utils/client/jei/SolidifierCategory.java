package mob_grinding_utils.client.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import mob_grinding_utils.recipe.SolidifyRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class SolidifierCategory implements IRecipeCategory<SolidifyRecipe> {
    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "solidifier_jei");

    private final IDrawableStatic background;

    public SolidifierCategory(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(new ResourceLocation(Reference.MOD_ID, "textures/gui/solidifier_jei.png"), 0, 0, 91, 26).setTextureSize(91, 26).build();
    }

    @Nonnull
    @Override
    public RecipeType<SolidifyRecipe> getRecipeType() {
        return JEIPlugin.SOLIDIFY_TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return new TextComponent("Solidifier Recipe");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Nonnull
    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Nonnull
    @SuppressWarnings("removal")
    @Override
    public Class<? extends SolidifyRecipe> getRecipeClass() {
        return SolidifyRecipe.class;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SolidifyRecipe recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.CATALYST, 5,5)
            .addIngredients(recipe.getMould());

        builder.addSlot(RecipeIngredientRole.INPUT, 37, 5)
            .addIngredients(VanillaTypes.FLUID, List.of(new FluidStack(ModBlocks.FLUID_XP.get(), recipe.getFluidAmount())))
            .addTooltipCallback((recipeSlot, tooltip) -> {
                var ingredient = recipeSlot.getDisplayedIngredient(VanillaTypes.FLUID);
                ingredient.ifPresent(fluidStack -> {
                    tooltip.add(new TextComponent(fluidStack.getAmount() + " mB"));
                    tooltip.add(new TranslatableComponent("mob_grinding_utils.jei.any_experience").withStyle(ChatFormatting.GRAY));
                });
            });
        builder.addSlot(RecipeIngredientRole.OUTPUT, 70, 5)
            .addItemStack(recipe.getResultItem());
    }
}
