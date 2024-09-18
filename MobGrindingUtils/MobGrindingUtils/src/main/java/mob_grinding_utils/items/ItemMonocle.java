package mob_grinding_utils.items;

import mob_grinding_utils.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemMonocle extends ArmorItem {

	public ItemMonocle(Holder<ArmorMaterial> material, ArmorItem.Type slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
		tooltip.add(Component.translatable("tooltip.monocle").withStyle(ChatFormatting.YELLOW));//applyTextStyle
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public String getArmorTexture(ItemStack is, Entity entity, EquipmentSlot slot, String type) {
		if (is.getItem() == ModItems.MONOCLE.get())
			return "mob_grinding_utils:textures/items/monocle_armour.png";
		else
			return null;
	}

	@Override
	public boolean isValidRepairItem(@Nonnull ItemStack armour, ItemStack material) {
		return material.getItem() == Items.IRON_INGOT;
	}
}