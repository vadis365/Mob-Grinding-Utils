package mob_grinding_utils.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.item.Item.Properties;

public class ItemImaginaryInvisibleNotReallyThereSword extends SwordItem {
	public ItemImaginaryInvisibleNotReallyThereSword(Properties properties) {
		super(Tiers.DIAMOND, 3, -2.4F, properties);
	}

	@Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		target.hurt(DamageSource.playerAttack((Player) attacker), getDamage() + EnchantmentHelper.getDamageBonus(stack, ((LivingEntity)target).getMobType()));
		return true;
    }

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
		list.add(new TranslatableComponent("Nothing to see here - Move along."));
	}
}