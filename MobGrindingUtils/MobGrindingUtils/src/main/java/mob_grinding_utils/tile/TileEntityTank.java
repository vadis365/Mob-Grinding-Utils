package mob_grinding_utils.tile;

import mob_grinding_utils.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityTank extends BlockEntity {
	public FluidTank tank = new FluidTank(1000 *  32);
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
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet, @Nonnull HolderLookup.Provider registries) {
		super.onDataPacket(net, packet, registries);
		loadAdditional(packet.getTag(), registries);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag nbt = new CompoundTag();
		saveAdditional(nbt, level.registryAccess());
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Nonnull
	@Override
	public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider registries) {
		CompoundTag nbt = new CompoundTag();
		saveAdditional(nbt, registries);
		return nbt;
	}

	@Override
	public void loadAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
		super.loadAdditional(nbt, registries);
		tank.readFromNBT(registries, nbt);
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag nbt, @Nonnull HolderLookup.Provider registries) {
		super.saveAdditional(nbt, registries);
		tank.writeToNBT(registries, nbt);
	}

	public FluidTank getTank(){
		return this.tank;
	}
	public FluidTank getTank(@Nullable Direction direction){
		return this.tank;
	}

	public int getScaledFluid(int scale) {
		return tank.getFluid() != null ? (int) ((float) tank.getFluidAmount() / (float) tank.getCapacity() * scale) : 0;
	}
}
