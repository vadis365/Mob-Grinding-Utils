package mob_grinding_utils.blocks;

import java.util.Random;

import mob_grinding_utils.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

@SuppressWarnings("deprecation")
public class BlockEntitySpawner extends Block  {//implements ITileEntityProvider {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public BlockEntitySpawner(Block.Properties properties) {
		super(properties);
		setDefaultState(this.stateContainer.getBaseState().with(POWERED, false));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
/*
	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityMGUSpawner();
	}
*/
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getPlacementHorizontalFacing().getOpposite();
		return this.getDefaultState().with(FACING, direction).with(POWERED, false);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}
/*
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isRemote)
			return ActionResultType.SUCCESS;
		else {
			TileEntity tileentity = world.getTileEntity(pos);
			if (tileentity  instanceof TileEntityMGUSpawner)
			NetworkHooks.openGui((ServerPlayerEntity) player, (TileEntityMGUSpawner)tileentity, pos);
		return ActionResultType.SUCCESS;
		} 
	}
*/
	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.isIn(newState.getBlock())) {
		//	TileEntityMGUSpawner tile = (TileEntityMGUSpawner) world.getTileEntity(pos);
		//	if (tile != null) {
		//		InventoryHelper.dropInventoryItems(world, pos, tile);
				world.updateComparatorOutputLevel(pos, this);
		//	}
			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!world.isRemote) {
			boolean flag = state.get(POWERED);
			if (flag != world.isBlockPowered(pos))
				if (flag)
					world.getPendingBlockTicks().scheduleTick(pos, this, 4);
				else
					world.setBlockState(pos, state.func_235896_a_(POWERED), 2);
		}
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (!world.isRemote)
			if (state.get(POWERED) && !world.isBlockPowered(pos))
				world.setBlockState(pos, state.func_235896_a_(POWERED), 2);
	}

	@Override
	public boolean getWeakChanges(BlockState state, IWorldReader world, BlockPos pos) {
		return state.isIn(ModBlocks.ENTITY_SPAWNER);
	}
}
