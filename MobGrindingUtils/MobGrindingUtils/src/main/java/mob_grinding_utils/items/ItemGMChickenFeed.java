package mob_grinding_utils.items;

import java.util.List;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		list.add(TextFormatting.YELLOW + "Feed to Chickens.");
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("mguMobName")) {
			list.add(TextFormatting.YELLOW + "Single use for the Chicken...");
			list.add(TextFormatting.GREEN + "Contains: " + stack.getTagCompound().getTag("mguMobName") + " 'DNA'.");
		}
	}

}