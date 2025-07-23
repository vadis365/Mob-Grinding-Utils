package mob_grinding_utils.items;

import mob_grinding_utils.ModItems;
import mob_grinding_utils.util.RL;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemMonocle extends ArmorItem {

	public ItemMonocle(Holder<ArmorMaterial> material, ArmorItem.Type slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
		tooltip.add(Component.translatable("tooltip.monocle").withStyle(ChatFormatting.YELLOW));
	}

	@Override
	public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		if (stack.getItem() == ModItems.MONOCLE.get())
			return RL.mgu("textures/item/monocle_armour.png");
		else
			return super.getArmorTexture(stack, entity, slot, layer, innerModel);
	}

	@Override
	public boolean isValidRepairItem(@Nonnull ItemStack armour, ItemStack material) {
		return material.getItem() == Items.IRON_INGOT;
	}
}