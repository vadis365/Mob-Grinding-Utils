package mob_grinding_utils.tile;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.blocks.BlockXPTap;
import mob_grinding_utils.datagen.MGUFluidTags;
import mob_grinding_utils.entity.EntityXPOrbFalling;
import mob_grinding_utils.network.MessageTapParticle;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fml.network.PacketDistributor;

public class TileEntityXPTap extends TileEntity implements ITickableTileEntity {
	
	public TileEntityXPTap() {
		super(ModBlocks.XP_TAP_TILE);
	}

	public boolean active;

	@Override
	public void tick() {
		if (!getWorld().isRemote && active) {
			TileEntity tileentity = getWorld().getTileEntity(pos.offset(getWorld().getBlockState(pos).get(BlockXPTap.FACING).getOpposite()));
			if (tileentity != null) {
				LazyOptional<IFluidHandler> fluidHandler = tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getWorld().getBlockState(pos).get(BlockXPTap.FACING));
				fluidHandler.ifPresent((handler) -> {
					if (handler.getFluidInTank(0).getAmount() > 0 && handler.getFluidInTank(0).getFluid().isIn(MGUFluidTags.EXPERIENCE) && getWorld().getGameTime() % 3 == 0) {
						int xpAmount = EntityXPOrbFalling.getXPSplit(Math.min(20, handler.getFluidInTank(0).getAmount() / 20));
						if (!handler.drain(xpAmount * 20, FluidAction.EXECUTE).isEmpty()) {
							spawnXP(getWorld(), pos, xpAmount, tileentity);
							MobGrindingUtils.NETWORK_WRAPPER.send(PacketDistributor.ALL.noArg(), new MessageTapParticle(getPos()));
						}
					}
				});
			}
		}
	}

	public void spawnXP(World world, BlockPos pos, int xp, TileEntity tankTile) {
		tankTile.markDirty();
		EntityXPOrbFalling orb = new EntityXPOrbFalling(world, pos.getX() + 0.5D, pos.getY() - 0.125D, pos.getZ() + 0.5D, xp);
		world.addEntity(orb);
	}

	public void setActive(boolean isActive) {
		active = isActive;
		getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		super.write(nbt);
		nbt.putBoolean("active", active);
		return nbt;
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		active = nbt.getBoolean("active");
	}

	@Override
    public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = new CompoundNBT();
        return write(nbt);
    }

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		write(nbt);
		return new SUpdateTileEntityPacket(getPos(), 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		read(getBlockState(), packet.getNbtCompound());
	}
}
