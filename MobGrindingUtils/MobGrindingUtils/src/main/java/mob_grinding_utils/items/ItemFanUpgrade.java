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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFanUpgrade extends Item implements ISubItemsItem {

	public ItemFanUpgrade() {
		super();
		setMaxStackSize(64);
		setHasSubtypes(true);
		setCreativeTab(MobGrindingUtils.TAB);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
		if (stack.getItemDamage() == EnumFanUpgradeType.WIDTH.ordinal())
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fanupgrade_width").getFormattedText());
		if (stack.getItemDamage() == EnumFanUpgradeType.HEIGHT.ordinal())
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fanupgrade_height").getFormattedText());
		if (stack.getItemDamage() == EnumFanUpgradeType.SPEED.ordinal())
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fanupgrade_distance").getFormattedText());
	}

	@Override
	@SideOnly(Side.CLIENT)
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
		public String getName() {
			return name().toLowerCase(Locale.ENGLISH);
		}

		@Override
		public ItemStack createStack(int size) {
			return new ItemStack(ModItems.FAN_UPGRADE, size, ordinal());
		}
	}
}