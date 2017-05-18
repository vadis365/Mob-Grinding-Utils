package mob_grinding_utils.entity;

import mob_grinding_utils.tile.TileEntitySinkTank;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityXPOrbFalling extends EntityXPOrb {

	public EntityXPOrbFalling(World worldIn, double x, double y, double z, int expValue) {
		super(worldIn);
		setSize(0.125F, 0.125F);
		setPosition(x, y, z);
		rotationYaw = (float) (Math.random() * 360.0D);
		motionX = 0;
		motionY = 0;
		motionZ = 0;
		xpValue = expValue;
	}

	@Override
	public void onUpdate() {
		super.onEntityUpdate();

		if (delayBeforeCanPickup > 0)
			--delayBeforeCanPickup;

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		motionY -= 0.029999999329447746D;

		pushOutOfBlocks(posX, (getEntityBoundingBox().minY + getEntityBoundingBox().maxY) / 2.0D, posZ);
		move(MoverType.SELF, motionX, motionY, motionZ);

		if (onGround)
			motionY *= -0.8999999761581421D;

		++xpColor;
		++xpOrbAge;

		if (xpOrbAge >= 6000)
			setDead();
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer player) {
		if (!world.isRemote) {
			if (delayBeforeCanPickup == 0 && player.xpCooldown == 0) {
				if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerPickupXpEvent(player, this)))
					return;
				player.xpCooldown = 2;
				player.onItemPickup(this, 1);
				ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, player);

				if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
					int i = Math.min(xpToDurability(xpValue), itemstack.getItemDamage());
					xpValue -= durabilityToXp(i);
					itemstack.setItemDamage(itemstack.getItemDamage() - i);
				}

				if (xpValue > 0)
					TileEntitySinkTank.addPlayerXP(player, xpValue);

				setDead();
			}
		}
	}

    private int durabilityToXp(int durability) {
        return durability / 2;
    }

    private int xpToDurability(int xp) {
        return xp * 2;
    }

}
