package mob_grinding_utils.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

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
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {
		switch (mouldType) {
			case BLANK -> list.add(Component.translatable("tooltip.solid_xp_mould_blank").withStyle(ChatFormatting.YELLOW));
			case BABY -> list.add(Component.translatable("tooltip.solid_xp_mould").withStyle(ChatFormatting.YELLOW));
		}
	}
}