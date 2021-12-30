package mob_grinding_utils.tile;

import java.util.List;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.network.MessageTapParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.network.PacketDistributor;

public class TileEntitySinkTank extends TileEntityTank {

	public TileEntitySinkTank(BlockPos pos, BlockState state) {
		super(ModBlocks.TANK_SINK.getTileEntityType(), pos, state);
	}

	public static <T extends BlockEntity> void serverTick(Level world, BlockPos worldPosition, BlockState blockState, T t) {
		if (t instanceof TileEntitySinkTank tile) {
			if (tile.tank.getFluid().isEmpty() || tile.tank.getFluid().containsFluid(new FluidStack(ModBlocks.FLUID_XP.get(), 1)))
				tile.captureDroppedXP();
			TileEntityTank.serverTick(world, worldPosition, blockState, t);
		}
	}

	public boolean captureDroppedXP() {
		for (Player player : getCaptureXP(level, getBlockPos().getX() + 0.5D, getBlockPos().getY() + 0.5D, getBlockPos().getZ() + 0.5D)) {
			int xpAmount = getPlayerXP(player);
			if (xpAmount <= 0)
				return false;
			if (tank.getFluidAmount() < tank.getCapacity()) {
				tank.fill(new FluidStack(ModBlocks.FLUID_XP.get(), 20), FluidAction.EXECUTE);
				addPlayerXP(player, -1);
				level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL , 0.1F, 0.5F * ((getLevel().random.nextFloat() - getLevel().random.nextFloat()) * 0.7F + 1.8F));
				MobGrindingUtils.NETWORK_WRAPPER.send(PacketDistributor.ALL.noArg(), new MessageTapParticle(getBlockPos().above()));
			}
			return true;
		}
		return false;
	}

	public List<Player> getCaptureXP(Level world, double x, double y, double z) {
		return world.<Player>getEntitiesOfClass(Player.class, new AABB(x - 0.45D, y - 0.5D, z - 0.45D, x + 0.45D, y + 1.03D, z + 0.45D), EntitySelector.ENTITY_STILL_ALIVE);
	}

	public static void addPlayerXP(Player player, int amount) {
		int experience = getPlayerXP(player) + amount;
		player.totalExperience = experience;
		player.experienceLevel = getLevelForExperience(experience);
		int expForLevel = getExperienceForLevel(player.experienceLevel);
		player.experienceProgress = (float)(experience - expForLevel) / (float)player.getXpNeededForNextLevel();
	}

	public static int getPlayerXP(Player player) {
		return (int)(getExperienceForLevel(player.experienceLevel) + (player.experienceProgress * player.getXpNeededForNextLevel()));
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
