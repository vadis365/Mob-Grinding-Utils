package mob_grinding_utils.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModItems;
import mob_grinding_utils.ModItems.ISubItemsItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemFanUpgrade extends Item implements ISubItemsItem {

	public ItemFanUpgrade(Properties properties) {
		super(properties);
		//setHasSubtypes(true);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
		if (stack.getDamage() == EnumFanUpgradeType.WIDTH.ordinal())
			list.add(new TranslationTextComponent("tooltip.fanupgrade_width").mergeStyle(TextFormatting.YELLOW));
		if (stack.getDamage() == EnumFanUpgradeType.HEIGHT.ordinal())
			list.add(new TranslationTextComponent("tooltip.fanupgrade_height").mergeStyle(TextFormatting.YELLOW));
		if (stack.getDamage() == EnumFanUpgradeType.SPEED.ordinal())
			list.add(new TranslationTextComponent("tooltip.fanupgrade_distance").mergeStyle(TextFormatting.YELLOW));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (tab == MobGrindingUtils.TAB)
			for (EnumFanUpgradeType type : EnumFanUpgradeType.values())
				list.add(type.createStack(1));
	}

	@Override
	public List<String> getModels() {
		List<String> models = new ArrayList<String>();
		for (EnumFanUpgradeType type : EnumFanUpgradeType.values())
			models.add("fan_upgrade_" + type.getName());
		return models;
	}

	public enum EnumFanUpgradeType implements IMGUItemEnum {
		WIDTH,
		HEIGHT,
		SPEED;

		@Override
		public ItemStack createStack(int size) {
			return new ItemStack(ModItems.FAN_UPGRADE, size, ordinal());
		}

		@Override
		public String getString() {
			 return name().toLowerCase(Locale.ENGLISH);
		}
	}
}