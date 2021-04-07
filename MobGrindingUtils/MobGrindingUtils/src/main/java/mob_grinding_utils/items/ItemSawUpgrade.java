package mob_grinding_utils.items;

import java.util.List;

import javax.annotation.Nonnull;
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
	public String upgradeType;

	public ItemSawUpgrade(Properties properties, String type) {
		super(properties);
		upgradeType = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> list, @Nonnull ITooltipFlag flag) {
		if (upgradeType.equals("sharpness"))
			list.add(new TranslationTextComponent("tooltip.sawupgrade_sharpness").mergeStyle(TextFormatting.YELLOW));
		if (upgradeType.equals("looting"))
			list.add(new TranslationTextComponent("tooltip.sawupgrade_looting").mergeStyle(TextFormatting.YELLOW));
		if (upgradeType.equals("fire"))
			list.add(new TranslationTextComponent("tooltip.sawupgrade_fire").mergeStyle(TextFormatting.YELLOW));
		if (upgradeType.equals("smite"))
			list.add(new TranslationTextComponent("tooltip.sawupgrade_smite").mergeStyle(TextFormatting.YELLOW));
		if (upgradeType.equals("arthropod"))
			list.add(new TranslationTextComponent("tooltip.sawupgrade_arthropods").mergeStyle(TextFormatting.YELLOW));
		if (upgradeType.equals("beheading"))
			list.add(new TranslationTextComponent("tooltip.sawupgrade_beheading").mergeStyle(TextFormatting.YELLOW));
	}

}