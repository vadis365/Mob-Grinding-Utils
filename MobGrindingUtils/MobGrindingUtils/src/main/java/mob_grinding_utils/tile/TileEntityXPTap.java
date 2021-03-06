package mob_grinding_utils.tile;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.blocks.BlockXPTap;
import mob_grinding_utils.entity.EntityXPOrbFalling;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class TileEntityXPTap extends TileEntity implements ITickableTileEntity {
	
	public TileEntityXPTap() {
		super(ModBlocks.XP_TAP_TILE);
	}

	public boolean active;
/*
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
*/
	@Override
	public void tick() {
		if (!getWorld().isRemote && active) {
			TileEntity tileentity = getWorld().getTileEntity(pos.offset(getWorld().getBlockState(pos).get(BlockXPTap.FACING).getOpposite()));
			if (tileentity instanceof TileEntityTank) {
				if (((TileEntityTank) tileentity).tank.getFluidAmount() > 0 && ((TileEntityTank) tileentity).tank.getFluid().getFluid().equals(ModBlocks.FLUID_XP) && getWorld().getGameTime() % 3 == 0) {
					int xpAmount = EntityXPOrbFalling.getXPSplit(Math.min(20, ((TileEntityTank) tileentity).tank.getFluidAmount() / 20));
					((TileEntityTank) tileentity).tank.drain(xpAmount * 20, FluidAction.EXECUTE);
					spawnXP(getWorld(), pos, xpAmount, (TileEntityTank) tileentity);
					//MobGrindingUtils.NETWORK_WRAPPER.sendToAll(new MessageTapParticle(getPos())); //todo
				}
			}
		}
	}

	public void spawnXP(World world, BlockPos pos, int xp, TileEntityTank tankTile) {
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
		read(null, packet.getNbtCompound());
	}
}
