package mob_grinding_utils.items;

import java.util.List;

import javax.annotation.Nullable;

import mob_grinding_utils.MobGrindingUtils;
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

public class ItemFanUpgrade extends Item {

	public ItemFanUpgrade() {
		super();
		setMaxStackSize(64);
		setHasSubtypes(true);
		setCreativeTab(MobGrindingUtils.TAB);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
		if(stack.getItemDamage() == 0)
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fanupgrade_width").getFormattedText());
		if(stack.getItemDamage() == 1)
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fanupgrade_height").getFormattedText());
		if(stack.getItemDamage() == 2 )
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.fanupgrade_distance").getFormattedText());
	}

	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (tab == MobGrindingUtils.TAB) {
			list.add(new ItemStack(this, 1, 0));
			list.add(new ItemStack(this, 1, 1));
			list.add(new ItemStack(this, 1, 2));
		}
	}
}