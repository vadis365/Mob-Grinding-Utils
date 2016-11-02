package mob_grinding_utils.items;

import java.util.List;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAbsorptionUpgrade extends Item {

	public ItemAbsorptionUpgrade() {
		super();
		setMaxStackSize(64);
		setHasSubtypes(true);
		setCreativeTab(MobGrindingUtils.TAB);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		list.add("Radius Modifier: +1 Max of 6");
	}
}