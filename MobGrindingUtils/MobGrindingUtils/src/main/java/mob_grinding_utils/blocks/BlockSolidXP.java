package mob_grinding_utils.blocks;

import mob_grinding_utils.ModSounds;
import net.minecraft.block.BreakableBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockSolidXP extends BreakableBlock {
	public BlockSolidXP(Properties properties) {
      super(properties);
   }

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entity, float fallDistance) {
		if (entity.isSuppressingBounce())
			super.onFallenUpon(worldIn, pos, entity, fallDistance);
		else {
			entity.onLivingFall(fallDistance, 0.0F);
			entity.getEntityWorld().playSound(null, entity.getPosition(), ModSounds.SOLID_XP_BLOCK_BOING, SoundCategory.BLOCKS, 0.3F, 1F);
		}
	}

	@Override
	public void onLanded(IBlockReader worldIn, Entity entity) {
		if (entity.isSuppressingBounce())
			super.onLanded(worldIn, entity);
		else 
			this.bounceEntity(entity);
	}

	private void bounceEntity(Entity entity) {
		Vector3d vector3d = entity.getMotion();
		if (vector3d.y < 0.0D) {
			double d0 = entity instanceof LivingEntity ? 1.0D : 0.8D;
			entity.setMotion(vector3d.x, -vector3d.y * d0, vector3d.z);
			
		}

	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		double d0 = Math.abs(entityIn.getMotion().y);
		if (d0 < 0.1D && !entityIn.isSteppingCarefully()) {
			double d1 = 0.4D + d0 * 0.2D;
			entityIn.setMotion(entityIn.getMotion().mul(d1, 1.0D, d1));
		}
		super.onEntityWalk(worldIn, pos, entityIn);
	}
}
