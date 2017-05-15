package mob_grinding_utils.entity;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.world.World;

public class EntityXPOrbFalling extends EntityXPOrb {

	public EntityXPOrbFalling(World worldIn, double x, double y, double z, int expValue) {
		super(worldIn);
		this.setSize(0.125F, 0.125F);
		this.setPosition(x, y, z);
		this.rotationYaw = (float) (Math.random() * 360.0D);
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		this.xpValue = expValue;
	}

	@Override
	public void onUpdate() {
		super.onEntityUpdate();

		if (this.delayBeforeCanPickup > 0)
			--this.delayBeforeCanPickup;

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= 0.029999999329447746D;

		pushOutOfBlocks(this.posX, (getEntityBoundingBox().minY + getEntityBoundingBox().maxY) / 2.0D, this.posZ);
		move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

		if (this.onGround)
			this.motionY *= -0.8999999761581421D;

		++this.xpColor;
		++this.xpOrbAge;

		if (this.xpOrbAge >= 6000)
			setDead();
	}

}
