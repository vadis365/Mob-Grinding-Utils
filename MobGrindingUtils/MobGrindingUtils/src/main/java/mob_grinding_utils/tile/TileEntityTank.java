package mob_grinding_utils.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mob_grinding_utils.ModBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileEntityTank extends BlockEntity  implements TickableBlockEntity {
	public FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME *  32);
    private final LazyOptional<IFluidHandler> tank_holder = LazyOptional.of(() -> tank);
	public int prevTankAmount;

	public TileEntityTank() {
		super(ModBlocks.TANK.getTileEntityType());
	}

	public TileEntityTank(BlockEntityType<TileEntitySinkTank> TANK_SINK_TILE) {
		super(TANK_SINK_TILE);
	}

	public TileEntityTank(BlockEntityType<TileEntityJumboTank> JUMBO_TANK_TILE, FluidTank tankIn) {
		super(JUMBO_TANK_TILE);
		this.tank = tankIn;
	}

	@Override
	public void tick() {
		if (getLevel().isClientSide)
			return;
		if(prevTankAmount != tank.getFluidAmount())
			updateBlock();
		prevTankAmount = tank.getFluidAmount();
	}

	public void updateBlock() {
		getLevel().sendBlockUpdated(worldPosition, getLevel().getBlockState(worldPosition), getLevel().getBlockState(worldPosition), 3);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		super.onDataPacket(net, packet);
		load(getBlockState(), packet.getTag());
		return;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag nbt = new CompoundTag();
		save(nbt);
		return new ClientboundBlockEntityDataPacket(getBlockPos(), 0, nbt);
	}

	@Override
    public CompoundTag getUpdateTag() {
		CompoundTag nbt = new CompoundTag();
        return save(nbt);
    }

	@Override
	public void load(BlockState state, CompoundTag tagCompound) {
		super.load(state, tagCompound);
		tank.readFromNBT(tagCompound);
	}

	@Override
	public CompoundTag save(CompoundTag tagCompound) {
		super.save(tagCompound);
		tank.writeToNBT(tagCompound);
		return tagCompound;
	}

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return tank_holder.cast();
        return super.getCapability(capability, facing);
    }

    public FluidTank getTank(){
        return this.tank;
    }

	public int getScaledFluid(int scale) {
		return tank.getFluid() != null ? (int) ((float) tank.getFluidAmount() / (float) tank.getCapacity() * scale) : 0;
	}
}
