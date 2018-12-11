package mob_grinding_utils.items;

import java.util.List;

import javax.annotation.Nullable;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGMChickenFeed extends Item {

	public ItemGMChickenFeed() {
		super();
		setMaxStackSize(1);
		setCreativeTab(MobGrindingUtils.TAB);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
		list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.chickenfeed_1").getFormattedText());
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("mguMobName")) {
			list.add(TextFormatting.YELLOW + new TextComponentTranslation("tooltip.chickenfeed_2").getFormattedText());
			if(stack.getTagCompound().hasKey("chickenType"))
				list.add(TextFormatting.GREEN + new TextComponentTranslation("tooltip..chickenfeed_3").getFormattedText()  + " " + stack.getTagCompound().getTag("chickenType") + TextFormatting.GREEN + " 'DNA'.");
			else
				list.add(TextFormatting.GREEN + new TextComponentTranslation("tooltip.chickenfeed_3").getFormattedText()  + " " + stack.getTagCompound().getTag("mguMobName") + TextFormatting.GREEN + " 'DNA'.");
		}
	}

}