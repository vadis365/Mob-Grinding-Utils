package mob_grinding_utils.blocks;

import mob_grinding_utils.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class BlockSolidXP extends HalfTransparentBlock {
	public BlockSolidXP(Properties properties) {
      super(properties);
   }

	@Override
	public void fallOn(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockPos pos, Entity entity, float fallDistance) {
		if (entity.isSuppressingBounce())
			super.fallOn(level, state, pos, entity, fallDistance);
		else {
			entity.causeFallDamage(fallDistance, 0.0F, DamageSource.FALL);
			entity.getCommandSenderWorld().playSound(null, entity.blockPosition(), ModSounds.SOLID_XP_BLOCK_BOING.get(), SoundSource.BLOCKS, 0.3F, 1F);
		}
	}

	@Override
	public void updateEntityAfterFallOn(@Nonnull BlockGetter level, Entity entity) {
		if (entity.isSuppressingBounce())
			super.updateEntityAfterFallOn(level, entity);
		else 
			this.bounceEntity(entity);
	}

	private void bounceEntity(Entity entity) {
		Vec3 vector3d = entity.getDeltaMovement();
		if (vector3d.y < 0.0D) {
			double d0 = entity instanceof LivingEntity ? 1.0D : 0.8D;
			entity.setDeltaMovement(vector3d.x, -vector3d.y * d0, vector3d.z);
			
		}

	}

	@Override
	public void stepOn(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, Entity entityIn) {
		double d0 = Math.abs(entityIn.getDeltaMovement().y);
		if (d0 < 0.1D && !entityIn.isSteppingCarefully()) {
			double d1 = 0.4D + d0 * 0.2D;
			entityIn.setDeltaMovement(entityIn.getDeltaMovement().multiply(d1, 1.0D, d1));
		}
		super.stepOn(level, pos, state, entityIn);
	}
}
