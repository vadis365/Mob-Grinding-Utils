package mob_grinding_utils.entity;

import mob_grinding_utils.tile.TileEntitySinkTank;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

import java.util.Map.Entry;

public class EntityXPOrbFalling extends ExperienceOrb {
	private int age;
	public int delayBeforeCanPickup;

	public EntityXPOrbFalling(Level level, double x, double y, double z, int expValue) {
		super(EntityType.EXPERIENCE_ORB, level);
		setPos(x, y, z);
		setYRot((float) (Math.random() * 360.0D));
		setDeltaMovement(0D ,0D ,0D);
		value = expValue;
	}

	@Override
	   public void tick() {
		super.tick();

		if (delayBeforeCanPickup > 0)
			--delayBeforeCanPickup;

		xo = getX();
		yo = getY();
		zo = getZ();
		setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));

	      if (!this.level().noCollision(this.getBoundingBox()))
	          this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());

		this.move(MoverType.SELF, this.getDeltaMovement());

	     if (this.onGround())
	         this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, -0.9D, 1.0D));

		++tickCount;
		++age;

		if (age >= 6000)
			removeAfterChangingDimensions();
	}

	@Override
	public void playerTouch(Player player) {
		if (!level().isClientSide) {
			if (delayBeforeCanPickup == 0 && player.takeXpDelay == 0) {
				if (NeoForge.EVENT_BUS.post(new PlayerXpEvent.PickupXp(player, this)).isCanceled()) //TODO maybe?
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

				remove(RemovalReason.DISCARDED);
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
