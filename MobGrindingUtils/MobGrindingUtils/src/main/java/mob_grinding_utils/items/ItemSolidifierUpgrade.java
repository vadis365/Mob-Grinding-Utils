package mob_grinding_utils.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemSolidifierUpgrade extends Item {

	public ItemSolidifierUpgrade(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull TooltipDisplay display, @Nonnull Consumer<Component> builder, @Nonnull TooltipFlag tooltipFlag) {
		builder.accept(Component.translatable("tooltip.solidifier_upgrade").withStyle(ChatFormatting.YELLOW));
	}
}