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

public class ItemSpawnerUpgrade extends Item {
	
	public String upgradeType = "width";

	public ItemSpawnerUpgrade(Properties properties, String type) {
		super(properties);
		upgradeType = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
		if (upgradeType.equals("width"))
			list.add(new TranslationTextComponent("tooltip.spawner_upgrade_width").mergeStyle(TextFormatting.YELLOW));
		if (upgradeType.equals("height"))
			list.add(new TranslationTextComponent("tooltip.spawner_upgrade_height").mergeStyle(TextFormatting.YELLOW));
	}

}