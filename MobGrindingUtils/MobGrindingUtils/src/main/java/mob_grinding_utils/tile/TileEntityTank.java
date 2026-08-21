package mob_grinding_utils.tile;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.components.FluidContents;
import mob_grinding_utils.components.MGUComponents;
import mob_grinding_utils.util.FluidTankStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityTank extends BlockEntity {
	public FluidTankStorage tank = new FluidTankStorage(1000 * 32, this::setChanged);
	public int prevTankAmount;

	public TileEntityTank(BlockPos pos, BlockState state) {
		super(ModBlocks.TANK.getTileEntityType(), pos, state);
	}

	public TileEntityTank(BlockEntityType<TileEntitySinkTank> TANK_SINK_TILE, BlockPos pos, BlockState state) {
		super(TANK_SINK_TILE, pos, state);
	}

	public TileEntityTank(BlockEntityType<TileEntityJumboTank> JUMBO_TANK_TILE, int capacity, BlockPos pos, BlockState state) {
		super(JUMBO_TANK_TILE, pos, state);
		this.tank = new FluidTankStorage(capacity, this::setChanged);
	}

	public static <T extends BlockEntity> void serverTick(Level world, BlockPos worldPosition, BlockState blockState, T t) {
		if (t instanceof TileEntityTank tile) {
			if(tile.prevTankAmount != tile.tank.amount()) {
				tile.updateBlock();
				tile.setChanged();
			}
			tile.prevTankAmount = tile.tank.amount();
		}
	}

	public void updateBlock() {
		getLevel().sendBlockUpdated(worldPosition, getLevel().getBlockState(worldPosition), getLevel().getBlockState(worldPosition), 3);
	}

	@Override
	public void onDataPacket(Connection net, ValueInput input) {
		loadAdditional(input);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Nonnull
	@Override
	public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider registries) {
		return saveCustomOnly(registries);
	}

	@Override
	protected void loadAdditional(@Nonnull ValueInput input) {
		super.loadAdditional(input);
		tank.replace(input.read("fluid", FluidStack.CODEC).orElse(FluidStack.EMPTY));
	}

	@Override
	protected void saveAdditional(@Nonnull ValueOutput output) {
		super.saveAdditional(output);
		if (!tank.stack().isEmpty())
			output.store("fluid", FluidStack.CODEC, tank.stack());
	}

	public FluidTankStorage getTank(){
		return this.tank;
	}
	public FluidTankStorage getTank(@Nullable Direction direction){
		return this.tank;
	}

	public int getScaledFluid(int scale) {
		return !tank.stack().isEmpty() ? (int) ((float) tank.amount() / tank.capacity() * scale) : 0;
	}

	@Override
	protected void applyImplicitComponents(@Nonnull DataComponentGetter componentInput) {
		super.applyImplicitComponents(componentInput);

		tank.replace(componentInput.getOrDefault(MGUComponents.FLUID, FluidContents.EMPTY).get());
	}

	@Override
	protected void collectImplicitComponents(@Nonnull DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);

		builder.set(MGUComponents.FLUID, FluidContents.of(tank.stack()));
	}
}
