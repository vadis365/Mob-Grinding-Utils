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
import java.util.Objects;

public class ItemGMChickenFeed extends Item {
	public String type;
	public ItemGMChickenFeed(Properties properties, String typeIn) {
		super(properties);
		this.type = typeIn;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level world, List<Component> list, @Nonnull TooltipFlag flag) {
		list.add(Component.translatable("tooltip.chickenfeed_1").withStyle(ChatFormatting.YELLOW));
		list.add(Component.translatable("tooltip.chickenfeed_2").withStyle(ChatFormatting.YELLOW));

		if (type.equals("mob"))
			if (stack.hasTag() && Objects.requireNonNull(stack.getTag()).contains("mguMobName"))
				list.add(Component.translatable("tooltip.chickenfeed_3").withStyle(ChatFormatting.GREEN).append(" " + stack.getTag().get("mguMobName") + " 'DNA'."));

		if (type.equals("cursed"))
			list.add(Component.translatable("tooltip.chickenfeed_4").withStyle(ChatFormatting.YELLOW));

		if (type.equals("nutritious"))
			list.add(Component.translatable("tooltip.chickenfeed_5").withStyle(ChatFormatting.YELLOW));
	}

}