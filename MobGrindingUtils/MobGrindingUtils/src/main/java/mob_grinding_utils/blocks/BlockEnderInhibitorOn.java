package mob_grinding_utils.blocks;

import java.util.Locale;
import java.util.Random;

import mob_grinding_utils.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.BlockVoxelShape;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEnderInhibitorOn extends Block {

	public static final EnumProperty<EnumGemDirection> TYPE = EnumProperty.create("type", EnumGemDirection.class);
	public static final VoxelShape DOWN_NORTH_AABB = Block.makeCuboidShape(2D, 0D, 3D, 14D, 3D, 13D);
	public static final VoxelShape DOWN_SOUTH_AABB = Block.makeCuboidShape(2D, 0D, 3D, 14D, 3D, 13D);
	public static final VoxelShape DOWN_WEST_AABB = Block.makeCuboidShape(3D, 0D, 2D, 13D, 3D, 14D);
	public static final VoxelShape DOWN_EAST_AABB = Block.makeCuboidShape(3D, 0D, 2D, 13D, 3D, 14D);
	public static final VoxelShape UP_NORTH_AABB = Block.makeCuboidShape(2D, 13D, 3D, 14D, 16D, 13D);
	public static final VoxelShape UP_SOUTH_AABB = Block.makeCuboidShape(2D, 13D, 3D, 14D, 16D, 13D);
	public static final VoxelShape UP_WEST_AABB = Block.makeCuboidShape(3D, 13D, 2D, 13D, 16D, 14D);
	public static final VoxelShape UP_EAST_AABB = Block.makeCuboidShape(3D, 13D, 2D, 13D, 16D, 14D);
	public static final VoxelShape NORTH_AABB = Block.makeCuboidShape(2D, 3D, 0D, 14D, 13D, 3D);
	public static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(2D, 3D, 13D, 14D, 13D, 16D);
	public static final VoxelShape WEST_AABB = Block.makeCuboidShape(0D, 3D, 2D, 3D, 13D, 14D);
	public static final VoxelShape EAST_AABB = Block.makeCuboidShape(13D, 3D, 2D, 16D, 13D, 14D);

	public BlockEnderInhibitorOn(Block.Properties properties) {
		super(properties);
		setDefaultState(this.stateContainer.getBaseState().with(TYPE, EnumGemDirection.DOWN_NORTH));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
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


	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(TYPE)) {
		case SOUTH:
			return SOUTH_AABB;
		case NORTH:
			return NORTH_AABB;
		case EAST:
			return EAST_AABB;
		case WEST:
			return WEST_AABB;
		case UP_NORTH:
			return UP_NORTH_AABB;
		case UP_EAST:
			return UP_EAST_AABB;
		case UP_SOUTH:
			return UP_SOUTH_AABB;
		case UP_WEST:
			return UP_WEST_AABB;
		case DOWN_NORTH:
			return DOWN_NORTH_AABB;
		case DOWN_EAST:
			return DOWN_EAST_AABB;
		case DOWN_SOUTH:
			return DOWN_SOUTH_AABB;
		case DOWN_WEST:
			return DOWN_WEST_AABB;
		}
		return DOWN_NORTH_AABB;
	}

	@Override
	public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		switch (state.get(TYPE)) {
		case SOUTH:
			return SOUTH_AABB;
		case NORTH:
			return NORTH_AABB;
		case EAST:
			return EAST_AABB;
		case WEST:
			return WEST_AABB;
		case UP_NORTH:
			return UP_NORTH_AABB;
		case UP_EAST:
			return UP_EAST_AABB;
		case UP_SOUTH:
			return UP_SOUTH_AABB;
		case UP_WEST:
			return UP_WEST_AABB;
		case DOWN_NORTH:
			return DOWN_NORTH_AABB;
		case DOWN_EAST:
			return DOWN_EAST_AABB;
		case DOWN_SOUTH:
			return DOWN_SOUTH_AABB;
		case DOWN_WEST:
			return DOWN_WEST_AABB;
		}
		return DOWN_NORTH_AABB;
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		EnumGemDirection newFacing = state.get(TYPE);
		boolean flag = false;

		if (newFacing == EnumGemDirection.UP_NORTH || newFacing == EnumGemDirection.UP_EAST || newFacing == EnumGemDirection.UP_SOUTH || newFacing == EnumGemDirection.UP_WEST)
			if (world.getBlockState(pos.up()).func_242698_a(world, pos.up(), Direction.DOWN, BlockVoxelShape.RIGID))
				flag = true;

		if (newFacing == EnumGemDirection.DOWN_NORTH || newFacing == EnumGemDirection.DOWN_EAST || newFacing == EnumGemDirection.DOWN_SOUTH || newFacing == EnumGemDirection.DOWN_WEST)
			if (world.getBlockState(pos.down()).func_242698_a(world, pos.down(), Direction.UP, BlockVoxelShape.RIGID))
				flag = true;

		if (newFacing == EnumGemDirection.NORTH && world.getBlockState(pos.offset(Direction.NORTH)).func_242698_a(world, pos.offset(Direction.NORTH), Direction.NORTH, BlockVoxelShape.RIGID))
			flag = true;

		if (newFacing == EnumGemDirection.SOUTH && world.getBlockState(pos.offset(Direction.SOUTH)).func_242698_a(world, pos.offset(Direction.SOUTH), Direction.SOUTH, BlockVoxelShape.RIGID))
			flag = true;

		if (newFacing == EnumGemDirection.WEST && world.getBlockState(pos.offset(Direction.WEST)).func_242698_a(world, pos.offset(Direction.WEST), Direction.WEST, BlockVoxelShape.RIGID))
			flag = true;

		if (newFacing == EnumGemDirection.EAST && world.getBlockState(pos.offset(Direction.EAST)).func_242698_a(world, pos.offset(Direction.EAST), Direction.EAST, BlockVoxelShape.RIGID))
			flag = true;

		if (!flag) {
			spawnAsEntity( world, pos, new ItemStack(ModBlocks.ENDER_INHIBITOR_OFF_ITEM, 1));
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}

		super.neighborChanged(state, world, pos, block, fromPos, isMoving);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (!world.isRemote) {
			BlockState activeState = ModBlocks.ENDER_INHIBITOR_OFF.getDefaultState().with(BlockEnderInhibitorOff.TYPE, state.get(TYPE));
			world.setBlockState(pos, activeState, 3);
			world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		for (Direction enumfacing : Direction.values())
			if (hasEnoughSolidSide(world, pos.offset(enumfacing.getOpposite()), enumfacing))
				return true;
		return false;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction facing = context.getFace();
		Direction direction = context.getPlayer().getHorizontalFacing();
		EnumGemDirection newFacing = EnumGemDirection.DOWN_NORTH;
		if (facing == Direction.UP) {
			switch (direction) {
				case SOUTH:
					newFacing = EnumGemDirection.DOWN_SOUTH;
					break;
				case EAST:
					newFacing = EnumGemDirection.DOWN_WEST;
					break;
				case NORTH:
					newFacing = EnumGemDirection.DOWN_NORTH;
					break;
				case WEST:
					newFacing = EnumGemDirection.DOWN_EAST;
					break;
			}
		}
		else if (facing == Direction.DOWN) {
			switch (direction) {
			case SOUTH:
				newFacing = EnumGemDirection.UP_SOUTH;
				break;
			case EAST:
				newFacing = EnumGemDirection.UP_WEST;
				break;
			case NORTH:
				newFacing = EnumGemDirection.UP_NORTH;
				break;
			case WEST:
				newFacing = EnumGemDirection.UP_EAST;
				break;
			}
		}
		else {
			switch (facing) {
			case SOUTH:
				newFacing = EnumGemDirection.NORTH;
				break;
			case EAST:
				newFacing = EnumGemDirection.WEST;
				break;
			case NORTH:
				newFacing = EnumGemDirection.SOUTH;
				break;
			case WEST:
				newFacing = EnumGemDirection.EAST;
				break;
			}
		}

		return getDefaultState().with(TYPE, newFacing);
    }

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(TYPE);
	}

	public enum EnumGemDirection implements IStringSerializable {
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

		@Override
		public String getString() {
			return name().toLowerCase(Locale.ENGLISH);
		}
	}
}