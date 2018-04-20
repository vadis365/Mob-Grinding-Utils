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

public class ItemSawUpgrade extends Item implements ISubItemsItem {

	public ItemSawUpgrade() {
		super();
		setMaxStackSize(64);
		setHasSubtypes(true);
		setCreativeTab(MobGrindingUtils.TAB);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
		if (stack.getItemDamage() == EnumSawUpgradeType.SHARPNESS.ordinal())
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.sawupgrade_sharpness").getFormattedText());
		if (stack.getItemDamage() == EnumSawUpgradeType.LOOTING.ordinal())
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.sawupgrade_looting").getFormattedText());
		if (stack.getItemDamage() == EnumSawUpgradeType.FIRE.ordinal())
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.sawupgrade_fire").getFormattedText());
		if (stack.getItemDamage() == EnumSawUpgradeType.SMITE.ordinal())
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.sawupgrade_smite").getFormattedText());
		if (stack.getItemDamage() == EnumSawUpgradeType.ARTHROPOD.ordinal())
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.sawupgrade_arthropods").getFormattedText());
		if (stack.getItemDamage() == EnumSawUpgradeType.BEHEADING.ordinal())
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.sawupgrade_beheading").getFormattedText());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (tab == MobGrindingUtils.TAB)
			for (EnumSawUpgradeType type : EnumSawUpgradeType.values())
				list.add(type.createStack(1));
	}

	@Override
	public List<String> getModels() {
		List<String> models = new ArrayList<String>();
		for (EnumSawUpgradeType type : EnumSawUpgradeType.values())
			models.add("saw_upgrade_" + type.getName());
		return models;
	}

	public enum EnumSawUpgradeType implements IMGUItemEnum {
		SHARPNESS,
		LOOTING,
		FIRE,
		SMITE,
		ARTHROPOD,
		BEHEADING;

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ENGLISH);
		}

		@Override
		public ItemStack createStack(int size) {
			return new ItemStack(ModItems.SAW_UPGRADE, size, ordinal());
		}
	}
}