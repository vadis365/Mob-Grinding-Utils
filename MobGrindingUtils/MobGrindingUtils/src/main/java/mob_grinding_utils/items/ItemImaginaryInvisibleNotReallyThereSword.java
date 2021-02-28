package mob_grinding_utils.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.SwordItem;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemImaginaryInvisibleNotReallyThereSword extends SwordItem {
	public ItemImaginaryInvisibleNotReallyThereSword() {
		super(ToolMaterial.DIAMOND);
	}

	@Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), getAttackDamage() + EnchantmentHelper.getModifierForCreature(stack, ((EntityLivingBase)target).getCreatureAttribute()));
		return true;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> list, ITooltipFlag flag) {
		list.add("Nothing to see here - Move along.");
	}
}