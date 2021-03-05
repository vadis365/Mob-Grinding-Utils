package mob_grinding_utils.tile;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class TileEntitySinkTank extends TileEntityTank implements ITickableTileEntity {

	public TileEntitySinkTank() {
		super();
	}

	@Override
	public void tick() {
		if (!getWorld().isRemote)
			if (tank.getFluid() == null || tank.getFluid().containsFluid(new FluidStack(FluidRegistry.getFluid("xpjuice"), 0)))
				captureDroppedXP();
	}

	public boolean captureDroppedXP() {
		for (PlayerEntity player : getCaptureXP(getWorld(), getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D)) {
			int xpAmount = getPlayerXP(player);
			if (xpAmount <= 0)
				return false;
			if (tank.getFluidAmount() < tank.getCapacity()) {
				tank.fill(new FluidStack(FluidRegistry.getFluid("xpjuice"), 20), FluidAction.EXECUTE);
				addPlayerXP(player, -1);
				getWorld().playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL , 0.1F, 0.5F * ((getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 0.7F + 1.8F));
				// MobGrindingUtils.NETWORK_WRAPPER.sendToAll(new MessageTapParticle(getPos().up())); //todo
			}
			return true;
		}
		return false;
	}

	public List<PlayerEntity> getCaptureXP(World world, double x, double y, double z) {
        return world.<PlayerEntity>getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(x - 0.45D, y - 0.5D, z - 0.45D, x + 0.45D, y + 1.03D, z + 0.45D), EntitySelectors.IS_ALIVE);
    }

	public static void addPlayerXP(PlayerEntity player, int amount) {
		int experience = getPlayerXP(player) + amount;
		player.experienceTotal = experience;
		player.experienceLevel = getLevelForExperience(experience);
		int expForLevel = getExperienceForLevel(player.experienceLevel);
		player.experience = (float)(experience - expForLevel) / (float)player.xpBarCap();
	}

	public static int getPlayerXP(PlayerEntity player) {
		return (int)(getExperienceForLevel(player.experienceLevel) + (player.experience * player.xpBarCap()));
	}

	public static int getLevelForExperience(int experience) {
		int i = 0;
		while (getExperienceForLevel(i) <= experience) {
			i++;
		}
		return i - 1;
	}

	public static int getExperienceForLevel(int level) {
		if (level == 0)
			return 0;
		if (level > 0 && level < 17)
			return (int) (Math.pow(level, 2) + 6 * level);
		else if (level > 16 && level < 32)
			return (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360);
		else
			return (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
	}
}
