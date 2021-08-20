package mob_grinding_utils.blocks;

import java.util.Random;
import java.util.function.Supplier;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.entity.EntityXPOrbFalling;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MGUFlowingFluidBlock extends FlowingFluidBlock {
    public MGUFlowingFluidBlock(FlowingFluid fluidIn, Properties builder) {
        super(fluidIn, builder);
    }

    public MGUFlowingFluidBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
        super(supplier, properties);
    }
    
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isRemote)
			if (entity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entity;
				if (world.getGameTime() % 20 == 0 && player.getFoodStats().getFoodLevel() > 0) {
					player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 1);
					EntityXPOrbFalling orb = new EntityXPOrbFalling(world, pos.getX() + 0.5D, pos.getY() - 0.125D, pos.getZ() + 0.5D, 1);
					world.addEntity(orb);
				}
			}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
		if (world.isAirBlock(pos.up()) && world.getGameTime()%5 == 0) {
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
