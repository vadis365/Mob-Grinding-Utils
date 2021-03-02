package mob_grinding_utils.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.SwordItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemImaginaryInvisibleNotReallyThereSword extends SwordItem {
	public ItemImaginaryInvisibleNotReallyThereSword(Properties properties) {
		super(ItemTier.DIAMOND, 3, -2.4F, properties);
	}

	@Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		target.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) attacker), getAttackDamage() + EnchantmentHelper.getModifierForCreature(stack, ((LivingEntity)target).getCreatureAttribute()));
		return true;
    }

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
		list.add(new TranslationTextComponent("Nothing to see here - Move along."));
	}
}