package mob_grinding_utils.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemFanUpgrade extends Item {
	
	public String upgradeType;

	public ItemFanUpgrade(Properties properties, String type) {
		super(properties);
		upgradeType = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level world, @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {
		if (upgradeType.equals("width"))
			list.add(Component.translatable("tooltip.fanupgrade_width").withStyle(ChatFormatting.YELLOW));
		if (upgradeType.equals("height"))
			list.add(Component.translatable("tooltip.fanupgrade_height").withStyle(ChatFormatting.YELLOW));
		if (upgradeType.equals("speed"))
			list.add(Component.translatable("tooltip.fanupgrade_distance").withStyle(ChatFormatting.YELLOW));
	}

}