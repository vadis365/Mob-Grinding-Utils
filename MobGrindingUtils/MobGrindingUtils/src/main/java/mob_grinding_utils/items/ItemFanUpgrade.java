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

public class ItemFanUpgrade extends Item {
	
	public String upgradeType;

	public ItemFanUpgrade(Properties properties, String type) {
		super(properties);
		upgradeType = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> list, @Nonnull ITooltipFlag flag) {
		if (upgradeType.equals("width"))
			list.add(new TranslationTextComponent("tooltip.fanupgrade_width").mergeStyle(TextFormatting.YELLOW));
		if (upgradeType.equals("height"))
			list.add(new TranslationTextComponent("tooltip.fanupgrade_height").mergeStyle(TextFormatting.YELLOW));
		if (upgradeType.equals("speed"))
			list.add(new TranslationTextComponent("tooltip.fanupgrade_distance").mergeStyle(TextFormatting.YELLOW));
	}

}