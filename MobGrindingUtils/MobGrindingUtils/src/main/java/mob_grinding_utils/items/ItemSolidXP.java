package mob_grinding_utils.items;

import mob_grinding_utils.BlockEntities.BlockEntitySinkTank;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ItemSolidXP extends Item {
	public int xpValue;

	public ItemSolidXP(Properties properties, int value) {
		super(properties);
		xpValue = value;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nonnull TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		builder.accept(Component.translatable("tooltip.solid_xp").append(Integer.toString(xpValue)).withStyle(ChatFormatting.YELLOW));
		if (stack.getCount() > 1)
			builder.accept(Component.translatable("tooltip.solid_xp2").append(Integer.toString(xpValue * stack.getCount())).withStyle(ChatFormatting.YELLOW));
	}

    @Nonnull
	@Override
	public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level world, @Nonnull LivingEntity entity) {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			if (xpValue > 0)
				if (!world.isClientSide()) {
					if (stack.getCount() > 1 && entity.isShiftKeyDown()) {
						BlockEntitySinkTank.addPlayerXP(player, xpValue * stack.getCount());
						stack.shrink(stack.getCount()-1);
					} else
						BlockEntitySinkTank.addPlayerXP(player, xpValue);
					world.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 0.8F + world.getRandom().nextFloat() * 0.4F);
				}
		}
		return super.finishUsingItem(stack, world, entity);
	}
}