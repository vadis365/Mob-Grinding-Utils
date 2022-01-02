 package mob_grinding_utils.client.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;


 @JeiPlugin
public class JEIPlugin implements IModPlugin {
    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "jei_plugin");
    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        ModItems.ITEMS.getEntries().forEach((item) -> {
            String key = item.get().getTranslationKey()+".jei.info";
            if (I18n.hasKey(key)) {
                registration.addIngredientInfo(new ItemStack(item.get()), VanillaTypes.ITEM, new TranslationTextComponent(key).getString());
            }
        });
        World level = Minecraft.getInstance().world;
        registration.addRecipes(level.getRecipeManager().getRecipesForType(MobGrindingUtils.SOLIDIFIER_TYPE), SolidifierCategory.ID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SolidifierCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.XPSOLIDIFIER.getItem()), SolidifierCategory.ID);
    }
 }
