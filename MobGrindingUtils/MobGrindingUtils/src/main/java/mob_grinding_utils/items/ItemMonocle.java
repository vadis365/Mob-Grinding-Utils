package mob_grinding_utils.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.api.distmarker.Dist;
import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ItemMonocle extends Item {

	public ItemMonocle(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, @Nonnull TooltipFlag flagIn) {
		tooltip.accept(Component.translatable("tooltip.monocle").withStyle(ChatFormatting.YELLOW));
	}
}
