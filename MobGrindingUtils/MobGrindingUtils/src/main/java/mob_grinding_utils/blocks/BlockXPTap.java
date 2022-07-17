package mob_grinding_utils.blocks;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.ModSounds;
import mob_grinding_utils.tile.TileEntityXPTap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockXPTap extends DirectionalBlock implements EntityBlock {

	public static final VoxelShape XP_TAP_WEST_AABB = Block.box(7D, 6D, 4D, 16D, 16D, 12D);
	public static final VoxelShape XP_TAP_EAST_AABB = Block.box(0D, 6D, 4D, 9D, 16D, 12D);
	public static final VoxelShape XP_TAP_SOUTH_AABB = Block.box(4D, 6D, 0D, 12D, 16D, 9D);
	public static final VoxelShape XP_TAP_NORTH_AABB = Block.box(4D, 6D, 7D, 12D, 16D, 16D);
	public static final BooleanProperty POWERED = BooleanProperty.create("powered");

	public BlockXPTap(Block.Properties properties) {
		super(properties);
		registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case EAST -> XP_TAP_EAST_AABB;
			case WEST -> XP_TAP_WEST_AABB;
			case SOUTH -> XP_TAP_SOUTH_AABB;
			default -> XP_TAP_NORTH_AABB;
		};
	}

	@Nonnull
	@Override
	public VoxelShape getInteractionShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos) {
		return switch (state.getValue(FACING)) {
			case EAST -> XP_TAP_EAST_AABB;
			case WEST -> XP_TAP_WEST_AABB;
			case SOUTH -> XP_TAP_SOUTH_AABB;
			default -> XP_TAP_NORTH_AABB;
		};
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getClickedFace();
		return this.defaultBlockState().setValue(FACING, direction).setValue(POWERED, false);
	}
	
	@Nonnull
	@Override
	public InteractionResult use(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		if (!world.isClientSide) {
			boolean swap = state.getValue(POWERED);
			world.setBlock(pos, state.setValue(POWERED, !swap), 3);
			float f = state.getValue(POWERED) ? 0.6F : 0.5F;
			world.playSound(null, pos, ModSounds.TAP_SQUEAK.get(), SoundSource.BLOCKS, 0.3F, f);
			TileEntityXPTap tileentity = (TileEntityXPTap) world.getBlockEntity(pos);
			tileentity.setActive(!swap);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader world, @Nonnull BlockPos pos) {
		for (Direction enumfacing : Direction.values()) {
			if (canPlaceAt(world, pos.relative(enumfacing.getOpposite()), enumfacing))
				return true;
		}
		return false;
	}

	private boolean canPlaceAt(LevelReader world, BlockPos pos, Direction facing) {
		BlockState blockstate = world.getBlockState(pos);
		boolean isSide = facing.getAxis().isHorizontal();
		BlockEntity te = world.getBlockEntity(pos);
		if (te != null && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).isPresent() && isSide)
			return true;
		return isSide && blockstate.getBlock() instanceof BlockTank;
	}

	@Override
	public void neighborChanged(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
		Direction facing = world.getBlockState(pos).getValue(FACING);
		if (!canPlaceAt(world, pos.relative(facing.getOpposite()), facing)) {
			popResource(world, pos, new ItemStack(ModBlocks.XP_TAP.getItem(), 1));
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
		super.neighborChanged(state, world, pos, block, fromPos, isMoving);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileEntityXPTap(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @Nonnull BlockState pState, @Nonnull BlockEntityType<T> pBlockEntityType) {
		return pLevel.isClientSide? null : TileEntityXPTap::serverTick;
	}
}
