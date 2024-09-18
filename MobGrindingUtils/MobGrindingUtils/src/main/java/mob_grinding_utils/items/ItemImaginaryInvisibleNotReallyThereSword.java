package mob_grinding_utils.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemImaginaryInvisibleNotReallyThereSword extends SwordItem {
	public ItemImaginaryInvisibleNotReallyThereSword(Properties properties) {
		//super(Tiers.DIAMOND, 3, -2.4F, properties);
		super(Tiers.DIAMOND, properties.attributes(createAttributes()));
	}

	private static ItemAttributeModifiers createAttributes() {
		ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
		builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID,3.0f, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
		builder.add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.4f, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
		return builder.build();
	}

/*	@Override
	public boolean hurtEnemy(@Nonnull ItemStack stack, LivingEntity target, @Nonnull LivingEntity attacker) {
		target.hurt(target.level().damageSources().playerAttack((Player) attacker), getDamage(stack) + EnchantmentHelper.getDamageBonus(stack, target.getMobType()));
		return true;
	}*/ //TODO is this needed anymore?

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nonnull TooltipContext context, List<Component> list, TooltipFlag flag) {
		list.add(Component.literal("Nothing to see here - Move along."));
	}
}