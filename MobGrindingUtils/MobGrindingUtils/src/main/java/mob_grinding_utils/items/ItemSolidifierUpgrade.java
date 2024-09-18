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

public class ItemSolidifierUpgrade extends Item {

	public ItemSolidifierUpgrade(Properties properties) {
		super(properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nonnull TooltipContext context, List<Component> list, TooltipFlag flag) {
		list.add(Component.translatable("tooltip.solidifier_upgrade").withStyle(ChatFormatting.YELLOW));
	}
}