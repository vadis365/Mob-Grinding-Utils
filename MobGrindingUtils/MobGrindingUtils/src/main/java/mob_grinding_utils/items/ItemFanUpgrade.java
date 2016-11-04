package mob_grinding_utils.items;

import java.util.List;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFanUpgrade extends Item {

	public ItemFanUpgrade() {
		super();
		setMaxStackSize(64);
		setHasSubtypes(true);
		setCreativeTab(MobGrindingUtils.TAB);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		if(stack.getItemDamage() == 0)
			list.add(TextFormatting.YELLOW + "Width Modifier: +1. Max of 3.");
		if(stack.getItemDamage() == 1)
			list.add(TextFormatting.YELLOW + "Height Modifier: +1. Max of 3.");
		if(stack.getItemDamage() == 2 )
			list.add(TextFormatting.YELLOW + "Distance Modifier: +1. Max of 10.");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
		list.add(new ItemStack((item), 1, 0));
		list.add(new ItemStack((item), 1, 1));
		list.add(new ItemStack((item), 1, 2));
	}
}