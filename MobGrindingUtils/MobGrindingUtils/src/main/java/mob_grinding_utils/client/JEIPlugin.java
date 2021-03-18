package mob_grinding_utils.client;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "jei_plugin");
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        for (BlockItem block : ModBlocks.ITEM_BLOCKS) {
            String key = block.getTranslationKey()+".jei.info";
            if (I18n.hasKey(key)) {
                String langEntry = TextFormatting.getTextWithoutFormattingCodes(I18n.format(key));
                if (langEntry == null)
                    continue;
                registration.addIngredientInfo(new ItemStack(block), VanillaTypes.ITEM, StringUtils.splitByWholeSeparator(langEntry, "//n"));
            }
        }
        for (Item item : ModItems.ITEMS) {
            String key = item.getTranslationKey()+".jei.info";
            if (I18n.hasKey(key)) {
                String langEntry = TextFormatting.getTextWithoutFormattingCodes(I18n.format(key));
                if (langEntry == null)
                    continue;
                registration.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM, StringUtils.splitByWholeSeparator(langEntry, "//n"));
            }
        }
    }
}
