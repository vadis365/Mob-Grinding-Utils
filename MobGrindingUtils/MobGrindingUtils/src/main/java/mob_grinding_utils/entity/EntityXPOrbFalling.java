package mob_grinding_utils.entity;

import java.util.Map.Entry;

import mob_grinding_utils.tile.TileEntitySinkTank;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityXPOrbFalling extends ExperienceOrbEntity {

	public EntityXPOrbFalling(World worldIn, double x, double y, double z, int expValue) {
		super(EntityType.EXPERIENCE_ORB, worldIn);
		setPosition(x, y, z);
		rotationYaw = (float) (Math.random() * 360.0D);
		setMotion(0D ,0D ,0D);
		xpValue = expValue;
	}

	@Override
	   public void tick() {
		super.tick();

		if (delayBeforeCanPickup > 0)
			--delayBeforeCanPickup;

		prevPosX = getPosX();
		prevPosY = getPosY();
		prevPosZ = getPosZ();
		setMotion(this.getMotion().add(0.0D, -0.03D, 0.0D));

	      if (!this.world.hasNoCollisions(this.getBoundingBox()))
	          this.pushOutOfBlocks(this.getPosX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getPosZ());

		this.move(MoverType.SELF, this.getMotion());

	     if (this.onGround)
	         this.setMotion(this.getMotion().mul(1.0D, -0.9D, 1.0D));

		++xpColor;
		++xpOrbAge;

		if (xpOrbAge >= 6000)
			setDead();
	}

	@Override
	public void onCollideWithPlayer(PlayerEntity player) {
		if (!world.isRemote) {
			if (delayBeforeCanPickup == 0 && player.xpCooldown == 0) {
				if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerXpEvent.PickupXp(player, this)))
					return;
				player.xpCooldown = 2;
				player.onItemPickup(this, 1);
				 Entry<EquipmentSlotType, ItemStack> entry = EnchantmentHelper.getRandomEquippedWithEnchantment(Enchantments.MENDING, player, ItemStack::isDamaged);

		            if (entry != null) {
		                ItemStack itemstack = entry.getValue();
		                if (!itemstack.isEmpty() && itemstack.isDamaged()) {
		                   int i = Math.min((int)(this.xpValue * itemstack.getXpRepairRatio()), itemstack.getDamage());
		                   this.xpValue -= durabilityToXp(i);
		                   itemstack.setDamage(itemstack.getDamage() - i);
		                }
		             }

				if (xpValue > 0)
					TileEntitySinkTank.addPlayerXP(player, xpValue);

				remove();
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
