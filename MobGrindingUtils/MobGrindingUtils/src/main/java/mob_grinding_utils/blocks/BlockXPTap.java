package mob_grinding_utils.blocks;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModSounds;
import mob_grinding_utils.tile.TileEntityXPTap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class BlockXPTap extends DirectionalBlock implements ITileEntityProvider {

	public static final VoxelShape XP_TAP_WEST_AABB = Block.makeCuboidShape(7D, 6D, 4D, 16D, 16D, 12D);
	public static final VoxelShape XP_TAP_EAST_AABB = Block.makeCuboidShape(0D, 6D, 4D, 9D, 16D, 12D);
	public static final VoxelShape XP_TAP_SOUTH_AABB = Block.makeCuboidShape(4D, 6D, 0D, 12D, 16D, 9D);
	public static final VoxelShape XP_TAP_NORTH_AABB = Block.makeCuboidShape(4D, 6D, 7D, 12D, 16D, 16D);
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");

	public BlockXPTap(Block.Properties properties) {
		super(properties);
		setDefaultState(this.stateContainer.getBaseState().with(POWERED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(FACING)) {
		default:
		case EAST:
			return XP_TAP_EAST_AABB;
		case WEST:
			return XP_TAP_WEST_AABB;
		case SOUTH:
			return XP_TAP_SOUTH_AABB;
		case NORTH:
			return  XP_TAP_NORTH_AABB;
		}
	}

	@Override
	public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		switch (state.get(FACING)) {
		default:
		case EAST:
			return XP_TAP_EAST_AABB;
		case WEST:
			return XP_TAP_WEST_AABB;
		case SOUTH:
			return XP_TAP_SOUTH_AABB;
		case NORTH:
			return  XP_TAP_NORTH_AABB;
		}
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getFace();
		return this.getDefaultState().with(FACING, direction).with(POWERED, false);
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isRemote)
			return ActionResultType.SUCCESS;
		 else {
			boolean swap = state.get(POWERED);
			world.setBlockState(pos, state.with(POWERED, !swap), 3);
			float f = state.get(POWERED) ? 0.6F : 0.5F;
			world.playSound(null, pos, ModSounds.TAP_SQUEAK, SoundCategory.BLOCKS, 0.3F, f);
			TileEntityXPTap tileentity = (TileEntityXPTap) world.getTileEntity(pos);
			tileentity.setActive(!swap);
			return ActionResultType.SUCCESS;
		}
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		for (Direction enumfacing : Direction.values()) {
			if (canPlaceAt(world, pos.offset(enumfacing.getOpposite()), enumfacing))
				return true;
		}
		return false;
	}

	private boolean canPlaceAt(IWorldReader world, BlockPos pos, Direction facing) {
		BlockState blockstate = world.getBlockState(pos);
		boolean isSide = facing.getAxis().isHorizontal();
		return isSide && blockstate.getBlock() instanceof BlockTank;
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		Direction facing = world.getBlockState(pos).get(FACING);
		if (!canPlaceAt(world, pos.offset(facing.getOpposite()), facing)) {
			spawnAsEntity(world, pos, new ItemStack(ModBlocks.XP_TAP_ITEM, 1));
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
		super.neighborChanged(state, world, pos, block, fromPos, isMoving);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityXPTap();
	}
}
