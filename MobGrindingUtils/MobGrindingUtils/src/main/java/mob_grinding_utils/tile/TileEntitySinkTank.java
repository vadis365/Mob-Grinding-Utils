package mob_grinding_utils.tile;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileEntitySinkTank extends TileEntityTank implements ITickable {

	public TileEntitySinkTank() {
        this.tank = new FluidTankTile(null, Fluid.BUCKET_VOLUME * 32);
        this.tank.setTileEntity(this);
	}

	@Override
	public void update() {
		if (!worldObj.isRemote && worldObj.getWorldTime() % 1 == 0) {
		if(tank.getFluid() == null || tank.getFluid().containsFluid(new FluidStack(FluidRegistry.getFluid("xpjuice"), 0)))
			captureDroppedXP();
		}
	}

	public boolean captureDroppedXP() {
		for (EntityPlayer player : getCaptureXP(getWorld(), getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D)) {
			int xpAmount = getPlayerXP(player);
			System.out.println("Player XP: " + xpAmount);
			if (xpAmount <= 0)
				return false;
			if (tank.getFluidAmount() < tank.getCapacity()) {
				tank.fill(new FluidStack(FluidRegistry.getFluid("xpjuice"), 20), true);
				addPlayerXP(player, -1);
				worldObj.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL , 0.1F, 0.5F * ((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.7F + 1.8F));
			}
			System.out.println("Fluid Amount: " + tank.getFluidAmount());
			return true;
		}
		return false;
	}

	public List<EntityPlayer> getCaptureXP(World world, double x, double y, double z) {
        return world.<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 1.5D, z + 0.5D), EntitySelectors.IS_ALIVE);
    }

	public static void addPlayerXP(EntityPlayer player, int amount) {
		int experience = getPlayerXP(player) + amount;
		player.experienceTotal = experience;
		player.experienceLevel = getLevelForExperience(experience);
		int expForLevel = getExperienceForLevel(player.experienceLevel);
		player.experience = (float)(experience - expForLevel) / (float)player.xpBarCap();
	}

	public static int getPlayerXP(EntityPlayer player) {
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
		if (level == 0) { return 0; }
		if (level > 0 && level < 16) {
			return level * 17;
		} else if (level > 15 && level < 31) {
			return (int)(1.5 * Math.pow(level, 2) - 29.5 * level + 360);
		} else {
			return (int)(3.5 * Math.pow(level, 2) - 151.5 * level + 2220);
		}
	}
}