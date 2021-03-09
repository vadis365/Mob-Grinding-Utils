package mob_grinding_utils.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mob_grinding_utils.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileEntityTank extends TileEntity {
	public FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME *  32);
    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);
    
	public TileEntityTank() {
		super(ModBlocks.TANK_TILE);

	}

	public TileEntityTank(TileEntityType<TileEntitySinkTank> TANK_SINK_TILE) {
		super(TANK_SINK_TILE);
	}

	/*
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
*/
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		super.onDataPacket(net, packet);
		read(null, packet.getNbtCompound());
		//tank.onContentsChanged();
		return;
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		write(nbt);
		return new SUpdateTileEntityPacket(getPos(), 0, nbt);
	}

	@Override
    public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = new CompoundNBT();
        return write(nbt);
    }

	@Override
	public void read(BlockState state, CompoundNBT tagCompound) {
		super.read(state, tagCompound);
		tank.readFromNBT(tagCompound);
	}

	@Override
	public CompoundNBT write(CompoundNBT tagCompound) {
		super.write(tagCompound);
		tank.writeToNBT(tagCompound);
		return tagCompound;
	}

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return holder.cast();
        return super.getCapability(capability, facing);
    }

    public FluidTank getTank(){
        return this.tank;
    }

	public int getScaledFluid(int scale) {
		return tank.getFluid() != null ? (int) ((float) tank.getFluidAmount() / (float) tank.getCapacity() * scale) : 0;
	}
}
