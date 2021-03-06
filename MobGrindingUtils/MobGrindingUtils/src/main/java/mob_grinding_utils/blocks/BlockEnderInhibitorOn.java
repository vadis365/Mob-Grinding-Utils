package mob_grinding_utils.blocks;

import java.util.Locale;
import java.util.Random;

import mob_grinding_utils.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
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
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEnderInhibitorOn extends Block {

	public static final EnumProperty<EnumGemDirection> TYPE = EnumProperty.create("type", EnumGemDirection.class);

	public BlockEnderInhibitorOn(Block.Properties properties) {
		super(properties);
		setDefaultState(this.stateContainer.getBaseState().with(TYPE, EnumGemDirection.DOWN_NORTH));
	}
//TODO this needs fixing in most blocks to use dragon and wither tags
	/*
	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
      return !(entity instanceof EntityWither) && !(entity instanceof EntityDragon);
    }
//
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
*/
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
/* TODO Voxel shapes (easy to do but can be sorted later)
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {

		float widthMin = 0, heightMin = 0, depthMin = 0;
		float widthMax = 0, heightMax = 0, depthMax = 0;
		switch (state.get(TYPE)) {

			case SOUTH:
				widthMin = 0.125F;
				heightMin = 0.1875F;
				depthMin = 0.8125F;
				widthMax = 0.125F;
				heightMax = 0.1875F;
				depthMax = 0F;
				break;
			case NORTH:
				widthMin = 0.125F;
				heightMin = 0.1875F;
				depthMin = 0F;
				widthMax = 0.125F;
				heightMax = 0.1875F;
				depthMax = 0.8125F;
				break;
			case EAST:
				widthMin = 0.8125F;
				heightMin = 0.1875F;
				depthMin = 0.125F;
				widthMax = 0F;
				heightMax = 0.1875F;
				depthMax = 0.125F;
				break;
			case WEST:
				widthMin = 0F;
				heightMin = 0.1875F;
				depthMin = 0.125F;
				widthMax = 0.8125F;
				heightMax = 0.1875F;
				depthMax = 0.125F;
				break;
			case UP_NORTH:
				widthMin = 0.125F;
				heightMin = 0.8125F;
				depthMin = 0.1875F;
				widthMax = 0.125F;
				heightMax = 0F;
				depthMax = 0.1875F;
				break;
			case UP_EAST:
				widthMin = 0.1875F;
				heightMin = 0.8125F;
				depthMin = 0.125F;
				widthMax = 0.1875F;
				heightMax = 0F;
				depthMax = 0.125F;
				break;
			case UP_SOUTH:
				widthMin = 0.125F;
				heightMin = 0.8125F;
				depthMin = 0.1875F;
				widthMax = 0.125F;
				heightMax = 0F;
				depthMax = 0.1875F;
				break;
			case UP_WEST:
				widthMin = 0.1875F;
				heightMin = 0.8125F;
				depthMin = 0.125F;
				widthMax = 0.1875F;
				heightMax = 0F;
				depthMax = 0.125F;
				break;
			case DOWN_NORTH:
				widthMin = 0.125F;
				heightMin = 0F;
				depthMin = 0.1875F;
				widthMax = 0.125F;
				heightMax = 0.8125F;
				depthMax = 0.1875F;
				break;
			case DOWN_EAST:
				widthMin = 0.1875F;
				heightMin = 0F;
				depthMin = 0.125F;
				widthMax = 0.1875F;
				heightMax = 0.8125F;
				depthMax = 0.125F;
				break;
			case DOWN_SOUTH:
				widthMin = 0.125F;
				heightMin = 0F;
				depthMin = 0.1875F;
				widthMax = 0.125F;
				heightMax = 0.8125F;
				depthMax = 0.1875F;
				break;
			case DOWN_WEST:
				widthMin = 0.1875F;
				heightMin = 0F;
				depthMin = 0.125F;
				widthMax = 0.1875F;
				heightMax = 0.8125F;
				depthMax = 0.125F;
				break;
		}
		return new AxisAlignedBB(0F + widthMin, 0F + heightMin, 0F + depthMin, 1F - widthMax, 1F - heightMax, 1F - depthMax);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return NULL_AABB;
	}
*/
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
			//TODO DROP THE BLOCK LOGIC
			//dropBlockAsItem(world, pos, state, 0);
			//world.setBlockToAir(pos);
		}

		super.neighborChanged(state, world, pos, block, fromPos, isMoving);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isRemote) {
			return ActionResultType.SUCCESS;
		} else {
			BlockState activeState = ModBlocks.ENDER_INHIBITOR_OFF.getDefaultState().with(BlockEnderInhibitorOff.TYPE, state.get(TYPE));
			world.setBlockState(pos, activeState, 3);
			world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
			return ActionResultType.SUCCESS;
		}
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
		Direction facing = context.getFace().getOpposite();
		Direction direction = context.getPlayer().getHorizontalFacing().getOpposite();
		EnumGemDirection newFacing = EnumGemDirection.DOWN_NORTH;
		if (facing == Direction.UP) {
			switch (direction) {
				case SOUTH:
					newFacing = EnumGemDirection.DOWN_SOUTH;
					break;
				case EAST:
					newFacing = EnumGemDirection.DOWN_EAST;
					break;
				case NORTH:
					newFacing = EnumGemDirection.DOWN_NORTH;
					break;
				case WEST:
					newFacing = EnumGemDirection.DOWN_WEST;
					break;
			}
		}
		else if (facing == Direction.DOWN) {
			switch (direction) {
			case SOUTH:
				newFacing = EnumGemDirection.UP_SOUTH;
				break;
			case EAST:
				newFacing = EnumGemDirection.UP_EAST;
				break;
			case NORTH:
				newFacing = EnumGemDirection.UP_NORTH;
				break;
			case WEST:
				newFacing = EnumGemDirection.UP_WEST;
				break;
			}
		}
		else {
			switch (facing) {
			case SOUTH:
				newFacing = EnumGemDirection.SOUTH;
				break;
			case EAST:
				newFacing = EnumGemDirection.EAST;
				break;
			case NORTH:
				newFacing = EnumGemDirection.NORTH;
				break;
			case WEST:
				newFacing = EnumGemDirection.WEST;
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