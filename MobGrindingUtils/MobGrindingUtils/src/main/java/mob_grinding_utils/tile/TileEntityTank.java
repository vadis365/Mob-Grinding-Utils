package mob_grinding_utils.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityTank extends TileEntity implements ITickable {
    public FluidTankTile tank;

	public TileEntityTank() {
        this.tank = new FluidTankTile(null, Fluid.BUCKET_VOLUME * 32);
        this.tank.setTileEntity(this);
	}
	
	@Override
	public void update() {
		if (worldObj.isRemote)
			return;
		
		for (EnumFacing facing : EnumFacing.VALUES) {
				TileEntity tile = worldObj.getTileEntity(pos.offset(facing));
				if (tile != null && tile instanceof TileEntityTank && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing)) {
					IFluidHandler recepticle = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
					IFluidTankProperties[] tankProperties = recepticle.getTankProperties();
					for (IFluidTankProperties properties : tankProperties) {
						if (properties.canFill() && properties.getCapacity() > 0) {
							FluidStack contents = properties.getContents();
						if (tank.getFluid() != null) {
							if (facing != EnumFacing.DOWN && facing != EnumFacing.UP) {
								if (contents == null || contents.amount <= properties.getCapacity() - 100 && contents.containsFluid(new FluidStack(tank.getFluid(), 0)) && tank.getFluid().amount > contents.amount) {
									recepticle.fill(tank.drain(new FluidStack(tank.getFluid(), 100), true), true);
									markDirty();
								}
							} else if (facing == EnumFacing.DOWN) {
								if (contents == null || contents.amount <= properties.getCapacity() - 100 && contents.containsFluid(new FluidStack(tank.getFluid(), 0))) {
									recepticle.fill(tank.drain(new FluidStack(tank.getFluid(), 100), true), true);
									markDirty();
								}
							}
						/*	else if (facing == EnumFacing.UP) {
								if (contents == null || contents.amount <= properties.getCapacity() - 100 && contents.containsFluid(new FluidStack(tank.getFluid(), 0)) && tank.getFluid().amount >= properties.getCapacity()) {
									recepticle.fill(tank.drain(new FluidStack(tank.getFluid(), 100), true), true);
									markDirty();
								}
							}
						*/
						}
					}
				}
			}
		}
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readFromNBT(packet.getNbtCompound());
		tank.onContentsChanged();
		return;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new SPacketUpdateTileEntity(getPos(), 0, tag);
	}

	@Override
    public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = new NBTTagCompound();
        return writeToNBT(tag);
    }

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		tank.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tank.writeToNBT(tagCompound);
		return tagCompound;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) tank;
		return super.getCapability(capability, facing);
	}

	public int getScaledFluid(int scale) {
		return tank.getFluid() != null ? (int) ((float) tank.getFluidAmount() / (float) tank.getCapacity() * scale) : 0;
	}
}
