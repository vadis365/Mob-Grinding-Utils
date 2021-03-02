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

public class ItemFanUpgrade extends Item {
	
	public String upgradeType = "width";

	public ItemFanUpgrade(Properties properties, String type) {
		super(properties);
		upgradeType = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
		if (upgradeType.matches("width"))
			list.add(new TranslationTextComponent("tooltip.fanupgrade_width").mergeStyle(TextFormatting.YELLOW));
		if (upgradeType.matches("height"))
			list.add(new TranslationTextComponent("tooltip.fanupgrade_height").mergeStyle(TextFormatting.YELLOW));
		if (upgradeType.matches("speed"))
			list.add(new TranslationTextComponent("tooltip.fanupgrade_distance").mergeStyle(TextFormatting.YELLOW));
	}

}