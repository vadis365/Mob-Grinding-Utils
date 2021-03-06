package mob_grinding_utils.blocks;

import java.lang.reflect.Method;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockSpikes extends DirectionalBlock {

	public static final VoxelShape SPIKES_AABB = Block.makeCuboidShape(0.0625D, 0.0625D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);

	public BlockSpikes(Block.Properties properties) {
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SPIKES_AABB;
	}

	@Override
	public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return VoxelShapes.fullCube();
	}
/*
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
*/
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getFace().getOpposite();
		return this.getDefaultState().with(FACING, direction);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	//TODO add dragon and wither tags
/*
	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
      return !(entity instanceof EntityWither) && !(entity instanceof EntityDragon);
	}
*/
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isRemote && entity instanceof LivingEntity)
			entity.attackEntityFrom(MobGrindingUtils.SPIKE_DAMAGE, 5);
	}

	public static final Method xpPoints = getExperiencePoints();

	public static void dropXP(LivingDropsEvent event) {
		LivingEntity entity = event.getEntityLiving();
		World world = entity.getEntityWorld();
		if (entity != null) {
			if (!world.isRemote && !event.isRecentlyHit() && event.getSource() == MobGrindingUtils.SPIKE_DAMAGE) {
				int xp = 0;
				try {
					xp = (Integer) xpPoints.invoke(entity, FakePlayerFactory.getMinecraft((ServerWorld) world));
				} catch (Exception e) {
				}
				while (xp > 0) {
					int cap = ExperienceOrbEntity.getXPSplit(xp);
					xp -= cap;
					entity.getEntityWorld().addEntity(new ExperienceOrbEntity(entity.getEntityWorld(), entity.getPosX(), entity.getPosY(), entity.getPosZ(), cap));
				}
			}
		}
	}

	public static Method getExperiencePoints() {
		Method method = null;
		try {
			method = LivingEntity.class.getDeclaredMethod("getExperiencePoints", PlayerEntity.class);
			method.setAccessible(true);
		} catch (Exception e) {
		}
		try {
			method = LivingEntity.class.getDeclaredMethod("func_70693_a", PlayerEntity.class);
			method.setAccessible(true);
		} catch (Exception e) {
		}
		return method;
	}
}
