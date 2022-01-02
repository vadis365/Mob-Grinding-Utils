package mob_grinding_utils.client.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.ITooltipCallback;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.Reference;
import mob_grinding_utils.recipe.SolidifyRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;

public class SolidifierCategory implements IRecipeCategory<SolidifyRecipe> {
    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "solidifier_jei");

    private final IDrawableStatic background;

    public SolidifierCategory(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(new ResourceLocation(Reference.MOD_ID, "textures/gui/solidifier_jei.png"), 0, 0, 91, 26).setTextureSize(91, 26).build();
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends SolidifyRecipe> getRecipeClass() {
        return SolidifyRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TextComponent("Solidifier Recipe");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setIngredients(SolidifyRecipe recipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(recipe.getMould().getItems()));
        ingredients.setInputs(VanillaTypes.FLUID, Arrays.asList(new FluidStack(ModBlocks.FLUID_XP.get(), recipe.getFluidAmount())));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SolidifyRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 4, 4);
        recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        recipeLayout.getFluidStacks().init(2, true, 37, 5);
        recipeLayout.getFluidStacks().set(2, ingredients.getInputs(VanillaTypes.FLUID).get(0));
        recipeLayout.getFluidStacks().addTooltipCallback(new ITooltipCallback<FluidStack>() {
            @Override
            public void onTooltip(int slotIndex, boolean input, FluidStack ingredient, List<Component> tooltip) {
                tooltip.add(new TextComponent(recipe.getFluidAmount() + " mB"));
                tooltip.add(new TranslatableComponent("mob_grinding_utils.jei.any_experience").withStyle(ChatFormatting.GRAY));
            }
        });
        recipeLayout.getItemStacks().init(3, false, 69, 4);
        recipeLayout.getItemStacks().set(3, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }
}
