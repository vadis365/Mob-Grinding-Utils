package mob_grinding_utils.blocks;

import com.mojang.serialization.MapCodec;
import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

public class BlockSpikes extends DirectionalBlock {
	public static final MapCodec<BlockSpikes> CODEC = simpleCodec(BlockSpikes::new);

	public static final VoxelShape SPIKES_AABB = Block.box(1D, 1D, 1D, 15D, 15D, 15D);

	public BlockSpikes(Block.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SPIKES_AABB;
	}

	@Nonnull
	@Override
	public VoxelShape getInteractionShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
		return Shapes.block();
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState state) {
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
	public void entityInside(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Entity entity) {
		if (!world.isClientSide && entity instanceof LivingEntity)
			entity.hurt(MobGrindingUtils.getSpikeDamage(world), 5);
	}

	public static void dropXP(LivingDropsEvent event) {
		LivingEntity entity = event.getEntity();
		Level level = entity.getCommandSenderWorld();
		if (entity != null) {
			if (!level.isClientSide && !event.isRecentlyHit() && event.getSource().is(MobGrindingUtils.SPIKE_TYPE)) {
				int xp = entity.getExperiencePoints();
				while (xp > 0) {
					int cap = ExperienceOrb.getExperienceValue(xp);
					xp -= cap;
					entity.getCommandSenderWorld().addFreshEntity(new ExperienceOrb(entity.getCommandSenderWorld(), entity.getX(), entity.getY(), entity.getZ(), cap));
				}
			}
		}
	}
}
