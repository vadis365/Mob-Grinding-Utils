package mob_grinding_utils.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockEntityConveyor extends Block {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final VoxelShape CONVEYOR_AABB = Block.makeCuboidShape(0D, 0D, 0D, 16D, 14D, 16D);

	public BlockEntityConveyor(Block.Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return CONVEYOR_AABB;
	}
	
	@Override
	public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return VoxelShapes.fullCube();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getPlacementHorizontalFacing().getOpposite();
		return this.getDefaultState().with(FACING, direction);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (entity.isSneaking())
			return;

		if (entity instanceof MobEntity)
			((MobEntity) entity).enablePersistence();
		else if (entity instanceof ItemEntity)
			((ItemEntity) entity).setNoDespawn();

		double speed = speed();
		int meta = state.get(FACING).getIndex() - 2;
		int[] factorX = { 0, 0, -1, 1 };
		int[] factorZ = { -1, 1, 0, 0 };

		if (entity.getPosY() > pos.getY() + 0.5D) {
			Vector3d vec3d = entity.getMotion();
			if (factorX[meta] == 0 && Math.abs(pos.getX() + 0.5D - entity.getPosX()) < 0.5D && Math.abs(pos.getX() + 0.5D - entity.getPosX()) > 0.1D)
				entity.setMotion(vec3d.x + Math.signum(pos.getX() + 0.5D - entity.getPosX()) * Math.min(speed, Math.abs(pos.getX() + 0.5D - entity.getPosX())) / 1.2D, vec3d.y, vec3d.z);
			if (factorZ[meta] == 0 && Math.abs(pos.getZ() + 0.5D - entity.getPosZ()) < 0.5D && Math.abs(pos.getZ() + 0.5D - entity.getPosZ()) > 0.1D)
				entity.setMotion(vec3d.x, vec3d.y, vec3d.z + Math.signum(pos.getZ() + 0.5D - entity.getPosZ()) * Math.min(speed, Math.abs(pos.getZ() + 0.5D - entity.getPosZ())) / 1.2D);

			entity.setMotion(vec3d.x + factorX[meta] * speed, vec3d.y, vec3d.z + factorZ[meta] * speed);

		}
	}

	protected double speed() {
		return 0.2D;
	}

}