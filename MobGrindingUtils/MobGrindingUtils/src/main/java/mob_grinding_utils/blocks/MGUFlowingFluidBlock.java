package mob_grinding_utils.blocks;

import java.util.function.Supplier;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.entity.EntityXPOrbFalling;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class MGUFlowingFluidBlock extends LiquidBlock {
	public MGUFlowingFluidBlock(FlowingFluid fluidIn, Properties builder) {
		super(fluidIn, builder);
	}

	public MGUFlowingFluidBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
		super(supplier, properties);
	}

	@Override
	public void entityInside(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Entity entity) {
		if (!world.isClientSide)
			if (entity instanceof Player player) {
				if (world.getGameTime() % 20 == 0 && player.getFoodData().getFoodLevel() > 0) {
					player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 1);
					EntityXPOrbFalling orb = new EntityXPOrbFalling(world, pos.getX() + 0.5D, pos.getY() - 0.125D, pos.getZ() + 0.5D, 1);
					world.addFreshEntity(orb);
				}
			}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(@Nonnull BlockState stateIn, Level world, BlockPos pos, @Nonnull RandomSource rand) {
		if (world.isEmptyBlock(pos.above()) && world.getGameTime()%5 == 0) {
			float xx = (float) pos.getX() + 0.5F;
			float zz = (float) pos.getZ() + 0.5F;
			float fixedOffset = 0.25F;
			float randomOffset = rand.nextFloat() * 0.6F - 0.3F;
			world.addParticle(MobGrindingUtils.PARTICLE_FLUID_XP.get(), false, (double) (xx - fixedOffset), (double) pos.getY() + 0.8D, (double) (zz + randomOffset), 0.0D, 0.0D, 0.0D);
			world.addParticle(MobGrindingUtils.PARTICLE_FLUID_XP.get(), false, (double) (xx + fixedOffset), (double) pos.getY() + 0.8D, (double) (zz + randomOffset), 0.0D, 0.0D, 0.0D);
			world.addParticle(MobGrindingUtils.PARTICLE_FLUID_XP.get(), false, (double) (xx + randomOffset), (double) pos.getY() + 0.8D, (double) (zz - fixedOffset), 0.0D, 0.0D, 0.0D);
			world.addParticle(MobGrindingUtils.PARTICLE_FLUID_XP.get(), false, (double) (xx + randomOffset), (double) pos.getY() + 0.8D, (double) (zz + fixedOffset), 0.0D, 0.0D, 0.0D);
		}
	}
}
