package mob_grinding_utils.tile;

import mob_grinding_utils.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityTank extends BlockEntity {
	public FluidTank tank = new FluidTank(1000 *  32);
	private final LazyOptional<IFluidHandler> tank_holder = LazyOptional.of(() -> tank);
	public int prevTankAmount;

	public TileEntityTank(BlockPos pos, BlockState state) {
		super(ModBlocks.TANK.getTileEntityType(), pos, state);
	}

	public TileEntityTank(BlockEntityType<TileEntitySinkTank> TANK_SINK_TILE, BlockPos pos, BlockState state) {
		super(TANK_SINK_TILE, pos, state);
	}

	public TileEntityTank(BlockEntityType<TileEntityJumboTank> JUMBO_TANK_TILE, FluidTank tankIn, BlockPos pos, BlockState state) {
		super(JUMBO_TANK_TILE, pos, state);
		this.tank = tankIn;
	}

	public static <T extends BlockEntity> void serverTick(Level world, BlockPos worldPosition, BlockState blockState, T t) {
		if (t instanceof TileEntityTank tile) {
			if(tile.prevTankAmount != tile.tank.getFluidAmount()) {
				tile.updateBlock();
				tile.setChanged();
			}
			tile.prevTankAmount = tile.tank.getFluidAmount();
		}
	}

	public void updateBlock() {
		getLevel().sendBlockUpdated(worldPosition, getLevel().getBlockState(worldPosition), getLevel().getBlockState(worldPosition), 3);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		super.onDataPacket(net, packet);
		load(packet.getTag());
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag nbt = new CompoundTag();
		saveAdditional(nbt);
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = new CompoundTag();
		saveAdditional(nbt);
		return nbt;
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		tank.readFromNBT(nbt);
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		tank.writeToNBT(nbt);
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
