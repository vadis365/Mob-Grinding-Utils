package mob_grinding_utils.entity;

import java.util.Map.Entry;

import mob_grinding_utils.tile.TileEntitySinkTank;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityXPOrbFalling extends ExperienceOrb {

	public EntityXPOrbFalling(Level worldIn, double x, double y, double z, int expValue) {
		super(EntityType.EXPERIENCE_ORB, worldIn);
		setPos(x, y, z);
		yRot = (float) (Math.random() * 360.0D);
		setDeltaMovement(0D ,0D ,0D);
		value = expValue;
	}

	@Override
	   public void tick() {
		super.tick();

		if (throwTime > 0)
			--throwTime;

		xo = getX();
		yo = getY();
		zo = getZ();
		setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));

	      if (!this.level.noCollision(this.getBoundingBox()))
	          this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());

		this.move(MoverType.SELF, this.getDeltaMovement());

	     if (this.onGround)
	         this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, -0.9D, 1.0D));

		++tickCount;
		++age;

		if (age >= 6000)
			removeAfterChangingDimensions();
	}

	@Override
	public void playerTouch(Player player) {
		if (!level.isClientSide) {
			if (throwTime == 0 && player.takeXpDelay == 0) {
				if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerXpEvent.PickupXp(player, this)))
					return;
				player.takeXpDelay = 2;
				player.take(this, 1);
				 Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, player, ItemStack::isDamaged);

		            if (entry != null) {
		                ItemStack itemstack = entry.getValue();
		                if (!itemstack.isEmpty() && itemstack.isDamaged()) {
		                   int i = Math.min((int)(this.value * itemstack.getXpRepairRatio()), itemstack.getDamageValue());
		                   this.value -= durabilityToXp(i);
		                   itemstack.setDamageValue(itemstack.getDamageValue() - i);
		                }
		             }

				if (value > 0)
					TileEntitySinkTank.addPlayerXP(player, value);

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
