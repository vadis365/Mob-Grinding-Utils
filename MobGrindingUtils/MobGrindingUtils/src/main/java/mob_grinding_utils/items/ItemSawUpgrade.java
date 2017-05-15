package mob_grinding_utils.items;

import java.util.List;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSawUpgrade extends Item {

	public ItemSawUpgrade() {
		super();
		setMaxStackSize(64);
		setHasSubtypes(true);
		setCreativeTab(MobGrindingUtils.TAB);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		if (stack.getItemDamage() == 0)
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.sawupgrade_sharpness").getFormattedText());
		if (stack.getItemDamage() == 1)
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.sawupgrade_looting").getFormattedText());
		if (stack.getItemDamage() == 2)
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.sawupgrade_fire").getFormattedText());
		if (stack.getItemDamage() == 3)
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.sawupgrade_smite").getFormattedText());
		if (stack.getItemDamage() == 4)
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.sawupgrade_arthropods").getFormattedText());
		if (stack.getItemDamage() == 5)
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.sawupgrade_beheading").getFormattedText());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
		list.add(new ItemStack((item), 1, 0));
		list.add(new ItemStack((item), 1, 1));
		list.add(new ItemStack((item), 1, 2));
		list.add(new ItemStack((item), 1, 3));
		list.add(new ItemStack((item), 1, 4));
		list.add(new ItemStack((item), 1, 5));
	}
}