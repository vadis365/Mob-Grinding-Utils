package mob_grinding_utils.blocks;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.tile.TileEntityMGUSpawner;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class BlockEntitySpawner extends Block implements EntityBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public BlockEntitySpawner(Block.Properties properties) {
		super(properties);
		registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileEntityMGUSpawner(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @Nonnull BlockState pState, @Nonnull BlockEntityType<T> pBlockEntityType) {
		return pLevel.isClientSide ? TileEntityMGUSpawner::clientTick : TileEntityMGUSpawner::serverTick;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getHorizontalDirection().getOpposite();
		return this.defaultBlockState().setValue(FACING, direction).setValue(POWERED, false);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}

	@Nonnull
	@Override
	public InteractionResult use(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		if (!world.isClientSide) {
			BlockEntity tileentity = world.getBlockEntity(pos);
			if (tileentity instanceof TileEntityMGUSpawner tile)
				NetworkHooks.openScreen((ServerPlayer) player, tile, pos);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void playerWillDestroy(Level world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
		if (!world.isClientSide && !player.getAbilities().instabuild) {
			TileEntityMGUSpawner tile = (TileEntityMGUSpawner) world.getBlockEntity(pos);
			if (tile != null) {
				if(!tile.inputSlots.getStackInSlot(0).isEmpty())
					Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.inputSlots.getStackInSlot(0));
				if(!tile.inputSlots.getStackInSlot(1).isEmpty())
					Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.inputSlots.getStackInSlot(1));
				if(!tile.inputSlots.getStackInSlot(2).isEmpty())
					Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.inputSlots.getStackInSlot(2));
				if(!tile.inputSlots.getStackInSlot(3).isEmpty())
					Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.inputSlots.getStackInSlot(3));
				if(!tile.fuelSlot.getStackInSlot(0).isEmpty())
					Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.fuelSlot.getStackInSlot(0));
				world.removeBlockEntity(pos);
			}
		}
	}

	@Override
	public void onRemove(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			TileEntityMGUSpawner tile = (TileEntityMGUSpawner) world.getBlockEntity(pos);
			if (tile != null) {
				//InventoryHelper.dropInventoryItems(world, pos, tile);
				world.updateNeighbourForOutputSignal(pos, this);
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public void neighborChanged(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
		if (!world.isClientSide) {
			TileEntityMGUSpawner tile = (TileEntityMGUSpawner) world.getBlockEntity(pos);
			boolean flag = state.getValue(POWERED);
			if (flag != world.hasNeighborSignal(pos))
				if (flag)
					world.scheduleTick(pos, this, 4);
				else {
					world.setBlock(pos, state.cycle(POWERED), 2);
					if (tile != null)
						tile.isOn = !state.getValue(POWERED);
				}
		}
	}

	@Override
	public void tick(@Nonnull BlockState state, ServerLevel world, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		if (!world.isClientSide) {
			TileEntityMGUSpawner tile = (TileEntityMGUSpawner) world.getBlockEntity(pos);
			if (state.getValue(POWERED) && !world.hasNeighborSignal(pos)) {
				world.setBlock(pos, state.cycle(POWERED), 2);
				if (tile != null)
					tile.isOn = !state.getValue(POWERED);
			}
		}
	}

	@Override
	public boolean getWeakChanges(BlockState state, LevelReader world, BlockPos pos) {
		return state.is(ModBlocks.ENTITY_SPAWNER.getBlock());
	}
}
