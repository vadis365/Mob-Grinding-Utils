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

public class ItemMonocle extends Item {

	public ItemMonocle(Properties properties) {
		super(properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull Item.TooltipContext context, @Nonnull TooltipDisplay display, @Nonnull Consumer<Component> builder, @Nonnull TooltipFlag tooltipFlag) {
		builder.accept(Component.translatable("tooltip.monocle").withStyle(ChatFormatting.YELLOW));
	}

/*	@Override
	public @Nullable Identifier getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		if (stack.getItem() == ModItems.MONOCLE.get())
			return RL.mgu("textures/item/monocle_armour.png");
		else
			return super.getArmorTexture(stack, entity, slot, layer, innerModel);
	}

	@Override
	public boolean isValidRepairItem(@Nonnull ItemStack armour, ItemStack material) {
		return material.getItem() == Items.IRON_INGOT;
	}*/ //TODO are these needed anymore?
}