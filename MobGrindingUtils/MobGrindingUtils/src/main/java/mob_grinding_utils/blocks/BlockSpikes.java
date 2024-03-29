package mob_grinding_utils.blocks;

import java.lang.reflect.Method;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class BlockSpikes extends DirectionalBlock {

	public static final VoxelShape SPIKES_AABB = Block.box(1D, 1D, 1D, 15D, 15D, 15D);

	public BlockSpikes(Block.Properties properties) {
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SPIKES_AABB;
	}

	@Override
	public VoxelShape getInteractionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return Shapes.block();
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getClickedFace();
		return this.defaultBlockState().setValue(FACING, direction);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (!world.isClientSide && entity instanceof LivingEntity)
			entity.hurt(MobGrindingUtils.SPIKE_DAMAGE, 5);
	}

	public static final Method xpPoints = getExperiencePoints();

	public static void dropXP(LivingDropsEvent event) {
		LivingEntity entity = event.getEntityLiving();
		Level world = entity.getCommandSenderWorld();
		if (entity != null) {
			if (!world.isClientSide && !event.isRecentlyHit() && event.getSource() == MobGrindingUtils.SPIKE_DAMAGE) {
				int xp = 0;
				try {
					xp = (Integer) xpPoints.invoke(entity, FakePlayerFactory.getMinecraft((ServerLevel) world));
				} catch (Exception e) {
				}
				while (xp > 0) {
					int cap = ExperienceOrb.getExperienceValue(xp);
					xp -= cap;
					entity.getCommandSenderWorld().addFreshEntity(new ExperienceOrb(entity.getCommandSenderWorld(), entity.getX(), entity.getY(), entity.getZ(), cap));
				}
			}
		}
	}

	public static Method getExperiencePoints() {
		Method method = null;
		try {
			method = LivingEntity.class.getDeclaredMethod("getExperienceReward", Player.class);
			method.setAccessible(true);
		} catch (Exception e) {
		}
		try {
			method = LivingEntity.class.getDeclaredMethod("m_6552_", Player.class);
			method.setAccessible(true);
		} catch (Exception e) {
		}
		return method;
	}
}
