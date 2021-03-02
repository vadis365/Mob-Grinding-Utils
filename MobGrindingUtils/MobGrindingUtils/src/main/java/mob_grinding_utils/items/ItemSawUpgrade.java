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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSawUpgrade extends Item implements ISubItemsItem {

	public ItemSawUpgrade(Properties properties) {
		super(properties);
//		setHasSubtypes(true);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
		if (stack.getDamage() == EnumSawUpgradeType.SHARPNESS.ordinal())
			list.add(new TranslationTextComponent("tooltip.sawupgrade_sharpness").mergeStyle(TextFormatting.YELLOW));
		if (stack.getDamage() == EnumSawUpgradeType.LOOTING.ordinal())
			list.add(new TranslationTextComponent("tooltip.sawupgrade_looting").mergeStyle(TextFormatting.YELLOW));
		if (stack.getDamage() == EnumSawUpgradeType.FIRE.ordinal())
			list.add(new TranslationTextComponent("tooltip.sawupgrade_fire").mergeStyle(TextFormatting.YELLOW));
		if (stack.getDamage() == EnumSawUpgradeType.SMITE.ordinal())
			list.add(new TranslationTextComponent("tooltip.sawupgrade_smite").mergeStyle(TextFormatting.YELLOW));
		if (stack.getDamage() == EnumSawUpgradeType.ARTHROPOD.ordinal())
			list.add(new TranslationTextComponent("tooltip.sawupgrade_arthropods").mergeStyle(TextFormatting.YELLOW));
		if (stack.getDamage() == EnumSawUpgradeType.BEHEADING.ordinal())
			list.add(new TranslationTextComponent("tooltip.sawupgrade_beheading").mergeStyle(TextFormatting.YELLOW));
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