package mob_grinding_utils.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockDreadfulDirt extends Block {

	public BlockDreadfulDirt(Block.Properties properties) {
		super(properties);
	}

	public boolean shouldCatchFire(Level world, BlockPos pos) {
		// standard night to day ticks
		return world.canSeeSkyFromBelowWater(pos) && (world.getDayTime() < 13000 || world.getDayTime() > 23000);
	}

	public boolean shouldSpawnMob(Level world, BlockPos pos) {
		return world.getMaxLocalRawBrightness(pos.above()) < 10;
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (shouldCatchFire(world, pos) || shouldSpawnMob(world, pos))
			world.getBlockTicks().scheduleTick(pos, this, Mth.nextInt(RANDOM, 20,60));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos) {
		if (shouldCatchFire((Level) world, pos) || shouldSpawnMob((Level) world, pos))
			world.getBlockTicks().scheduleTick(pos, this, Mth.nextInt(RANDOM, 20, 60));
		return super.updateShape(stateIn, facing, facingState, world, pos, facingPos);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (shouldCatchFire((Level) world, pos) || shouldSpawnMob((Level) world, pos))
			world.getBlockTicks().scheduleTick(pos, this, Mth.nextInt(RANDOM, 20, 60));
	}

	@Deprecated
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
		if (shouldCatchFire(world, pos)) {
			BlockPos posUp = pos.above();
			BlockState blockstate = BaseFireBlock.getState(world, posUp);
			if (world.getBlockState(posUp).getMaterial() == Material.AIR && blockstate.canSurvive(world, posUp))
				world.setBlock(posUp, blockstate, 11);
		}
		if (!shouldCatchFire(world, pos) && shouldSpawnMob(world, pos)) {
			AABB areaToCheck = new AABB(pos).inflate(5, 2, 5);
			int entityCount = world.getEntitiesOfClass(Mob.class, areaToCheck, entity -> entity != null && entity instanceof Enemy).size();

			if (entityCount < 8)
				spawnMob(world, pos);
		}
	}

	public void spawnMob(ServerLevel world, BlockPos pos) {
		List<SpawnerData> spawns = world.getBiome(pos).getMobSettings().getMobs(MobCategory.MONSTER);
		if (!spawns.isEmpty()) {
			int indexSize = spawns.size();
			EntityType<?> type = spawns.get(RANDOM.nextInt(indexSize)).type;
			if (type == null || !NaturalSpawner.isSpawnPositionOk(SpawnPlacements.getPlacementType(type), world, pos.above(), type))
				return;
			Mob entity = (Mob) type.create(world);
			if (entity != null) {
				entity.setPos(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D);
				 if(world.getEntities(entity.getType(), entity.getBoundingBox(), EntitySelector.ENTITY_STILL_ALIVE).isEmpty() && world.noCollision(entity)) {
					entity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null, null);
					world.addFreshEntity(entity);
				 }
			}
		}
	}

	@Override
    public  boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return true;
    }

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 200;
    }

	@Override
    public boolean isFireSource(BlockState state, LevelReader world, BlockPos pos, Direction side) {
		return side == Direction.UP;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level world, BlockPos pos, Random rand) {
		for (int i = 0; i < 4; ++i) {
			double d0 = (double) ((float) pos.getX() + rand.nextFloat());
			double d1 = (double) ((float) pos.getY() + rand.nextFloat());
			double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
			double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			world.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
		}
	}
}
