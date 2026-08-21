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

public class ItemAbsorptionUpgrade extends Item {

	public ItemAbsorptionUpgrade(Properties properties) {
		super(properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, @Nonnull TooltipFlag flag) {
		tooltip.accept(Component.translatable("tooltip.hopperupgrade").withStyle(ChatFormatting.YELLOW));
	}
}
