package mob_grinding_utils.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class BlockTintedGlass extends Block {

	public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create("connected_down");
	public static final BooleanProperty CONNECTED_UP = BooleanProperty.create("connected_up");
	public static final BooleanProperty CONNECTED_NORTH = BooleanProperty.create("connected_north");
	public static final BooleanProperty CONNECTED_SOUTH = BooleanProperty.create("connected_south");
	public static final BooleanProperty CONNECTED_WEST = BooleanProperty.create("connected_west");
	public static final BooleanProperty CONNECTED_EAST = BooleanProperty.create("connected_east");

	public BlockTintedGlass(Block.Properties properties) {
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(CONNECTED_DOWN, Boolean.FALSE).setValue(CONNECTED_EAST, Boolean.FALSE).setValue(CONNECTED_NORTH, Boolean.FALSE).setValue(CONNECTED_SOUTH, Boolean.FALSE).setValue(CONNECTED_UP, Boolean.FALSE).setValue(CONNECTED_WEST, Boolean.FALSE));
	}

	@OnlyIn(Dist.CLIENT)
	public boolean skipRendering(@Nonnull BlockState state, BlockState adjacentBlockState, @Nonnull Direction side) {
		return adjacentBlockState.is(this) || super.skipRendering(state, adjacentBlockState, side);
	}

	@Override
	public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
		return false;
	}
	
    @Override
    public int getLightBlock (@Nonnull BlockState state, BlockGetter world, @Nonnull BlockPos pos) {
        return world.getMaxLightLevel();
    }

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockGetter world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		return super.getStateForPlacement(context)
			.setValue(CONNECTED_DOWN, this.isSideConnectable(world, pos, Direction.DOWN))
			.setValue(CONNECTED_EAST, this.isSideConnectable(world, pos, Direction.EAST))
			.setValue(CONNECTED_NORTH, this.isSideConnectable(world, pos, Direction.NORTH))
			.setValue(CONNECTED_SOUTH, this.isSideConnectable(world, pos, Direction.SOUTH))
			.setValue(CONNECTED_UP, this.isSideConnectable(world, pos, Direction.UP))
			.setValue(CONNECTED_WEST, this.isSideConnectable(world, pos, Direction.WEST));
	}
	
	@Nonnull
	@Override
	public BlockState updateShape(BlockState stateIn, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor world, @Nonnull BlockPos pos, @Nonnull BlockPos facingPos) {
		return stateIn.setValue(CONNECTED_DOWN, this.isSideConnectable(world, pos, Direction.DOWN))
			.setValue(CONNECTED_EAST, this.isSideConnectable(world, pos, Direction.EAST))
			.setValue(CONNECTED_NORTH, this.isSideConnectable(world, pos, Direction.NORTH))
			.setValue(CONNECTED_SOUTH, this.isSideConnectable(world, pos, Direction.SOUTH))
			.setValue(CONNECTED_UP, this.isSideConnectable(world, pos, Direction.UP))
			.setValue(CONNECTED_WEST, this.isSideConnectable(world, pos, Direction.WEST));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST);
	}

	private boolean isSideConnectable(BlockGetter world, BlockPos pos, Direction side) {
		final BlockState stateConnection = world.getBlockState(pos.relative(side));
		return stateConnection != null && stateConnection.getBlock() == this;
	}
}