package mob_grinding_utils.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ItemFanUpgrade extends Item {
	public enum UpgradeType {
		WIDTH, HEIGHT, SPEED
	}
	public final UpgradeType upgradeType;

	public ItemFanUpgrade(Properties properties, UpgradeType type) {
		super(properties);
		upgradeType = type;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, @Nonnull TooltipFlag flag) {
		if (upgradeType == UpgradeType.WIDTH)
			tooltip.accept(Component.translatable("tooltip.fanupgrade_width").withStyle(ChatFormatting.YELLOW));
		if (upgradeType == UpgradeType.HEIGHT)
			tooltip.accept(Component.translatable("tooltip.fanupgrade_height").withStyle(ChatFormatting.YELLOW));
		if (upgradeType == UpgradeType.SPEED)
			tooltip.accept(Component.translatable("tooltip.fanupgrade_distance").withStyle(ChatFormatting.YELLOW));
	}

}
