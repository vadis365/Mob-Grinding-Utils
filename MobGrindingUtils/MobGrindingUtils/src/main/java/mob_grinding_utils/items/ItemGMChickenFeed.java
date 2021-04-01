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

public class ItemGMChickenFeed extends Item {
	public String type;
	public ItemGMChickenFeed(Properties properties, String typeIn) {
		super(properties);
		this.type = typeIn;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
		list.add(new TranslationTextComponent("tooltip.chickenfeed_1").mergeStyle(TextFormatting.YELLOW));
		list.add(new TranslationTextComponent("tooltip.chickenfeed_2").mergeStyle(TextFormatting.YELLOW));

		if (type == "mob")
			if (stack.hasTag() && stack.getTag().contains("mguMobName"))
				list.add(new TranslationTextComponent("tooltip.chickenfeed_3").mergeStyle(TextFormatting.GREEN).appendString(" " + stack.getTag().get("mguMobName") + " 'DNA'."));

		if (type == "cursed")
			list.add(new TranslationTextComponent("tooltip.chickenfeed_4").mergeStyle(TextFormatting.YELLOW));

		if (type == "nutritious") 
			list.add(new TranslationTextComponent("tooltip.chickenfeed_5").mergeStyle(TextFormatting.YELLOW));
	}

}