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

public class ItemSolidXPMould extends Item {
	public enum Mould {
		BLANK,
		BABY
	}
	public Mould mouldType;
	
	public ItemSolidXPMould(Properties properties, Mould type) {
		super(properties);
		this.mouldType = type;
	}

	@Override
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull TooltipDisplay display, @Nonnull Consumer<Component> builder, @Nonnull TooltipFlag tooltipFlag) {
		switch (mouldType) {
			case BLANK -> builder.accept(Component.translatable("tooltip.solid_xp_mould_blank").withStyle(ChatFormatting.YELLOW));
			case BABY -> builder.accept(Component.translatable("tooltip.solid_xp_mould").withStyle(ChatFormatting.YELLOW));
		}
	}
}