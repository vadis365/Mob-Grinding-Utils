package mob_grinding_utils.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mob_grinding_utils.tile.TileEntitySinkTank;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemSolidXP extends Item {
	public int xpValue;

	public ItemSolidXP(Properties properties, int value) {
		super(properties);
		xpValue = value;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, @Nonnull ITooltipFlag flag) {
		list.add(new TranslationTextComponent("tooltip.solid_xp").appendString(Integer.toString(xpValue)).mergeStyle(TextFormatting.YELLOW));
		if (stack.getCount() > 1)
			list.add(new TranslationTextComponent("tooltip.solid_xp2").appendString(Integer.toString(xpValue * stack.getCount())).mergeStyle(TextFormatting.YELLOW));
	}

	@Nonnull
	@Override
	public ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull LivingEntity entity) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			if (xpValue > 0)
				if (!world.isRemote) {
					if (stack.getCount() > 1 && entity.isSneaking()) {
						TileEntitySinkTank.addPlayerXP(player, xpValue * stack.getCount());
						stack.shrink(stack.getCount()-1);
					} else
						TileEntitySinkTank.addPlayerXP(player, xpValue);
					world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5F, 0.8F + world.rand.nextFloat() * 0.4F);
				}
		}
		return super.onItemUseFinish(stack, world, entity);
	}
}