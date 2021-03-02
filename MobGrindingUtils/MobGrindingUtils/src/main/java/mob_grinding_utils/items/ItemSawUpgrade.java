package mob_grinding_utils.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemSawUpgrade extends Item  {
	public String upgradeType = "sharpness";

	public ItemSawUpgrade(Properties properties, String type) {
		super(properties);
		upgradeType = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
		if (upgradeType.matches("sharpness"))
			list.add(new TranslationTextComponent("tooltip.sawupgrade_sharpness").mergeStyle(TextFormatting.YELLOW));
		if (upgradeType.matches("looting"))
			list.add(new TranslationTextComponent("tooltip.sawupgrade_looting").mergeStyle(TextFormatting.YELLOW));
		if (upgradeType.matches("fire"))
			list.add(new TranslationTextComponent("tooltip.sawupgrade_fire").mergeStyle(TextFormatting.YELLOW));
		if (upgradeType.matches("smite"))
			list.add(new TranslationTextComponent("tooltip.sawupgrade_smite").mergeStyle(TextFormatting.YELLOW));
		if (upgradeType.matches("arthropod"))
			list.add(new TranslationTextComponent("tooltip.sawupgrade_arthropods").mergeStyle(TextFormatting.YELLOW));
		if (upgradeType.matches("beheading"))
			list.add(new TranslationTextComponent("tooltip.sawupgrade_beheading").mergeStyle(TextFormatting.YELLOW));
	}

}