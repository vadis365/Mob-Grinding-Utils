package mob_grinding_utils.blocks;

import mob_grinding_utils.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Locale;


public class BlockEnderInhibitorOn extends Block {

	public static final EnumProperty<EnumGemDirection> TYPE = EnumProperty.create("type", EnumGemDirection.class);
	public static final VoxelShape DOWN_NORTH_AABB = Block.box(2D, 0D, 3D, 14D, 3D, 13D);
	public static final VoxelShape DOWN_SOUTH_AABB = Block.box(2D, 0D, 3D, 14D, 3D, 13D);
	public static final VoxelShape DOWN_WEST_AABB = Block.box(3D, 0D, 2D, 13D, 3D, 14D);
	public static final VoxelShape DOWN_EAST_AABB = Block.box(3D, 0D, 2D, 13D, 3D, 14D);
	public static final VoxelShape UP_NORTH_AABB = Block.box(2D, 13D, 3D, 14D, 16D, 13D);
	public static final VoxelShape UP_SOUTH_AABB = Block.box(2D, 13D, 3D, 14D, 16D, 13D);
	public static final VoxelShape UP_WEST_AABB = Block.box(3D, 13D, 2D, 13D, 16D, 14D);
	public static final VoxelShape UP_EAST_AABB = Block.box(3D, 13D, 2D, 13D, 16D, 14D);
	public static final VoxelShape NORTH_AABB = Block.box(2D, 3D, 0D, 14D, 13D, 3D);
	public static final VoxelShape SOUTH_AABB = Block.box(2D, 3D, 13D, 14D, 13D, 16D);
	public static final VoxelShape WEST_AABB = Block.box(0D, 3D, 2D, 3D, 13D, 14D);
	public static final VoxelShape EAST_AABB = Block.box(13D, 3D, 2D, 16D, 13D, 14D);

	public BlockEnderInhibitorOn(Block.Properties properties) {
		super(properties);
		registerDefaultState(this.stateDefinition.any().setValue(TYPE, EnumGemDirection.DOWN_NORTH));
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(@Nonnull BlockState stateIn, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		for (int i = 0; i < 4; ++i) {
			double d0 = (double) ((float) pos.getX() + rand.nextFloat());
			double d1 = (double) ((float) pos.getY() + rand.nextFloat());
			double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
			double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			int j = rand.nextInt(2) * 2 - 1;

			if (world.getBlockState(pos.west()).getBlock() != this && world.getBlockState(pos.east()).getBlock() != this) {
				d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
				d3 = (double) (rand.nextFloat() * 2.0F * (float) j);
			} else {
				d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
				d5 = (double) (rand.nextFloat() * 2.0F * (float) j);
			}
			world.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
		}
	}


	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return switch (state.getValue(TYPE)) {
			case SOUTH -> SOUTH_AABB;
			case NORTH -> NORTH_AABB;
			case EAST -> EAST_AABB;
			case WEST -> WEST_AABB;
			case UP_NORTH -> UP_NORTH_AABB;
			case UP_EAST -> UP_EAST_AABB;
			case UP_SOUTH -> UP_SOUTH_AABB;
			case UP_WEST -> UP_WEST_AABB;
			case DOWN_NORTH -> DOWN_NORTH_AABB;
			case DOWN_EAST -> DOWN_EAST_AABB;
			case DOWN_SOUTH -> DOWN_SOUTH_AABB;
			case DOWN_WEST -> DOWN_WEST_AABB;
		};
	}

	@Nonnull
	@Override
	public VoxelShape getInteractionShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos) {
		return switch (state.getValue(TYPE)) {
			case SOUTH -> SOUTH_AABB;
			case NORTH -> NORTH_AABB;
			case EAST -> EAST_AABB;
			case WEST -> WEST_AABB;
			case UP_NORTH -> UP_NORTH_AABB;
			case UP_EAST -> UP_EAST_AABB;
			case UP_SOUTH -> UP_SOUTH_AABB;
			case UP_WEST -> UP_WEST_AABB;
			case DOWN_NORTH -> DOWN_NORTH_AABB;
			case DOWN_EAST -> DOWN_EAST_AABB;
			case DOWN_SOUTH -> DOWN_SOUTH_AABB;
			case DOWN_WEST -> DOWN_WEST_AABB;
		};
	}

	@Override
	public void neighborChanged(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Block block, @Nonnull BlockPos fromPos, boolean isMoving) {
		EnumGemDirection newFacing = state.getValue(TYPE);
		boolean flag = false;

		if (newFacing == EnumGemDirection.UP_NORTH || newFacing == EnumGemDirection.UP_EAST || newFacing == EnumGemDirection.UP_SOUTH || newFacing == EnumGemDirection.UP_WEST)
			if (world.getBlockState(pos.above()).isFaceSturdy(world, pos.above(), Direction.DOWN, SupportType.RIGID))
				flag = true;

		if (newFacing == EnumGemDirection.DOWN_NORTH || newFacing == EnumGemDirection.DOWN_EAST || newFacing == EnumGemDirection.DOWN_SOUTH || newFacing == EnumGemDirection.DOWN_WEST)
			if (world.getBlockState(pos.below()).isFaceSturdy(world, pos.below(), Direction.UP, SupportType.RIGID))
				flag = true;

		if (newFacing == EnumGemDirection.NORTH && world.getBlockState(pos.relative(Direction.NORTH)).isFaceSturdy(world, pos.relative(Direction.NORTH), Direction.NORTH, SupportType.RIGID))
			flag = true;

		if (newFacing == EnumGemDirection.SOUTH && world.getBlockState(pos.relative(Direction.SOUTH)).isFaceSturdy(world, pos.relative(Direction.SOUTH), Direction.SOUTH, SupportType.RIGID))
			flag = true;

		if (newFacing == EnumGemDirection.WEST && world.getBlockState(pos.relative(Direction.WEST)).isFaceSturdy(world, pos.relative(Direction.WEST), Direction.WEST, SupportType.RIGID))
			flag = true;

		if (newFacing == EnumGemDirection.EAST && world.getBlockState(pos.relative(Direction.EAST)).isFaceSturdy(world, pos.relative(Direction.EAST), Direction.EAST, SupportType.RIGID))
			flag = true;

		if (!flag) {
			popResource( world, pos, new ItemStack(ModBlocks.ENDER_INHIBITOR_OFF.getItem(), 1));
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}

		super.neighborChanged(state, world, pos, block, fromPos, isMoving);
	}

	@Nonnull
	@Override
	public InteractionResult useWithoutItem(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult hitResult) {
		if (!world.isClientSide) {
			BlockState activeState = ModBlocks.ENDER_INHIBITOR_OFF.getBlock().defaultBlockState().setValue(BlockEnderInhibitorOff.TYPE, state.getValue(TYPE));
			world.setBlock(pos, activeState, 3);
			world.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, 0.6F);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader world, @Nonnull BlockPos pos) {
		for (Direction enumfacing : Direction.values())
			if (canSupportCenter(world, pos.relative(enumfacing.getOpposite()), enumfacing))
				return true;
		return false;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction facing = context.getClickedFace();
		Direction direction = context.getPlayer().getDirection();
		EnumGemDirection newFacing = EnumGemDirection.DOWN_NORTH;
		if (facing == Direction.UP) {
			newFacing = switch (direction) {
				case SOUTH -> EnumGemDirection.DOWN_SOUTH;
				case EAST -> EnumGemDirection.DOWN_WEST;
				case NORTH -> EnumGemDirection.DOWN_NORTH;
				case WEST -> EnumGemDirection.DOWN_EAST;
				default -> newFacing;
			};
		}
		else if (facing == Direction.DOWN) {
			newFacing = switch (direction) {
				case SOUTH -> EnumGemDirection.UP_SOUTH;
				case EAST -> EnumGemDirection.UP_WEST;
				case NORTH -> EnumGemDirection.UP_NORTH;
				case WEST -> EnumGemDirection.UP_EAST;
				default -> newFacing;
			};
		}
		else {
			newFacing = switch (facing) {
				case SOUTH -> EnumGemDirection.NORTH;
				case EAST -> EnumGemDirection.WEST;
				case NORTH -> EnumGemDirection.SOUTH;
				case WEST -> EnumGemDirection.EAST;
				default -> newFacing;
			};
		}

		return defaultBlockState().setValue(TYPE, newFacing);
    }

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TYPE);
	}

	public enum EnumGemDirection implements StringRepresentable {
		DOWN_NORTH,
		DOWN_SOUTH,
		DOWN_WEST,
		DOWN_EAST,
		UP_NORTH,
		UP_SOUTH,
		UP_WEST,
		UP_EAST,
		NORTH,
		SOUTH,
		WEST,
		EAST;

		@Nonnull
		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ENGLISH);
		}
	}
}