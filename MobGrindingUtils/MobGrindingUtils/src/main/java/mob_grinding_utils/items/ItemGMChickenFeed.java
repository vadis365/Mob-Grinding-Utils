package mob_grinding_utils.items;

import mob_grinding_utils.components.MGUComponents;
import mob_grinding_utils.util.RL;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemGMChickenFeed extends Item {
	public enum FeedType {
		MOB, CURSED, NUTRITIOUS
	}
	public FeedType type;
	public ItemGMChickenFeed(Properties properties, FeedType typeIn) {
		super(properties);
		this.type = typeIn;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, List<Component> list, @Nonnull TooltipFlag flag) {
		list.add(Component.translatable("tooltip.chickenfeed_1").withStyle(ChatFormatting.YELLOW));
		list.add(Component.translatable("tooltip.chickenfeed_2").withStyle(ChatFormatting.YELLOW));

		if (type == FeedType.MOB)
			if (stack.has(MGUComponents.MOB_DNA))
				list.add(Component.translatable("tooltip.chickenfeed_3").withStyle(ChatFormatting.GREEN).append(stack.getOrDefault(MGUComponents.MOB_DNA, RL.mc("nobody")) + " 'DNA'."));

		if (type == FeedType.CURSED)
			list.add(Component.translatable("tooltip.chickenfeed_4").withStyle(ChatFormatting.YELLOW));

		if (type == FeedType.NUTRITIOUS)
			list.add(Component.translatable("tooltip.chickenfeed_5").withStyle(ChatFormatting.YELLOW));
	}

}