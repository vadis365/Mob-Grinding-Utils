package mob_grinding_utils.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.OptionalCapabilityInstance;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityTank extends TileEntity {
    public FluidTankTile tank;
    private IFluidHandler fluidHandler;
	public TileEntityTank() {
		super(null);//hnnnngh TODO work that shit out too
        this.tank = new FluidTankTile(null, Fluid.BUCKET_VOLUME * 32);
        this.tank.setTileEntity(this);
	}

	//@Override
	//public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
	//	return oldState.getBlock() != newState.getBlock();
	//}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		read(packet.getNbtCompound());
		tank.onContentsChanged();
		return;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = new NBTTagCompound();
		write(tag);
		return new SPacketUpdateTileEntity(getPos(), 0, tag);
	}

	@Override
    public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
        return write(tag);
    }

	@Override
	public void read(NBTTagCompound tagCompound) {
		super.read(tagCompound);
		tank.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound write(NBTTagCompound tagCompound) {
		super.write(tagCompound);
		tank.writeToNBT(tagCompound);
		return tagCompound;
	}

	protected IFluidHandler createFluidHandler() {
		return tank;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> OptionalCapabilityInstance<T> getCapability(Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (fluidHandler == null) {
				fluidHandler = createFluidHandler();
			}
			return ((OptionalCapabilityInstance<IFluidHandler>) fluidHandler).cast();
		}
		return super.getCapability(cap, side);
	}

	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
	}

	public int getScaledFluid(int scale) {
		return tank.getFluid() != null ? (int) ((float) tank.getFluidAmount() / (float) tank.getCapacity() * scale) : 0;
	}
}
