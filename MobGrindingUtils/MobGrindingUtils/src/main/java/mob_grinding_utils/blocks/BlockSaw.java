package mob_grinding_utils.blocks;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.tile.TileEntitySaw;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkHooks;

public class BlockSaw extends DirectionalBlock implements EntityBlock {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final VoxelShape SAW_AABB = Block.box(1D, 1D, 1D, 15D, 15D, 15D);
	
	public BlockSaw(Block.Properties properties) {
		super(properties);
		registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileEntitySaw();
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SAW_AABB;
	}

	@Override
	public VoxelShape getInteractionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return Shapes.block();
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getClickedFace();
		return this.defaultBlockState().setValue(FACING, direction).setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
	}

	@Override
	  public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (state.getValue(POWERED)) {
			TileEntitySaw tile = (TileEntitySaw) world.getBlockEntity(pos);
			tile.setActive(true);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!world.isClientSide) {
			BlockEntity tileentity = world.getBlockEntity(pos);
			if (tileentity instanceof TileEntitySaw)
				NetworkHooks.openGui((ServerPlayer) player, (TileEntitySaw) tileentity, pos);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			TileEntitySaw tile = (TileEntitySaw) world.getBlockEntity(pos);
			if (tile != null) {
				Containers.dropContents(world, pos, tile);
				world.updateNeighbourForOutputSignal(pos, this);
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!world.isClientSide) {
			TileEntitySaw tile = (TileEntitySaw) world.getBlockEntity(pos);
			boolean flag = state.getValue(POWERED);
			if (flag != world.hasNeighborSignal(pos)) {
				if (flag)
					world.getBlockTicks().scheduleTick(pos, this, 4);
				else {
					world.setBlock(pos, state.cycle(POWERED), 2);
					if (tile != null)
						tile.setActive(!state.getValue(POWERED));
				}

			}
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
		if (!world.isClientSide) {
			TileEntitySaw tile = (TileEntitySaw) world.getBlockEntity(pos);
			if (state.getValue(POWERED) && !world.hasNeighborSignal(pos)) {
				world.setBlock(pos, state.cycle(POWERED), 2);
				if (tile != null)
					tile.setActive(!state.getValue(POWERED));
			}
		}
	}

	@Override
	public boolean getWeakChanges(BlockState state, LevelReader world, BlockPos pos) {
		return state.is(ModBlocks.SAW.getBlock());
	}
}
