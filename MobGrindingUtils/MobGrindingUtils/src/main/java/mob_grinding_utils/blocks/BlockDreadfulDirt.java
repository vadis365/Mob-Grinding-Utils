package mob_grinding_utils.blocks;

import mob_grinding_utils.ModTags;
import mob_grinding_utils.events.DirtSpawnEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.EventHooks;

import java.util.List;

public class BlockDreadfulDirt extends BlockDirtSpawner {

	public BlockDreadfulDirt(Block.Properties properties) {
		super(properties);
	}

	public boolean shouldCatchFire(LevelAccessor level, BlockPos pos) {
		// standard night to day ticks
		return level.canSeeSkyFromBelowWater(pos) && (level.dayTime() < 13000 || level.dayTime() > 23000);
	}

	public boolean shouldSpawnMob(LevelAccessor level, BlockPos pos) {
		return level.getMaxLocalRawBrightness(pos.above()) < 5;
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (shouldCatchFire(level, pos) || shouldSpawnMob(level, pos))
			level.scheduleTick(pos, this, Mth.nextInt(level.getRandom(), 20,60));
		//List<SpawnerData> spawns = level.getBiome(pos).value().getMobSettings().getMobs(MobCategory.MONSTER).unwrap();
		//spawns.forEach(spawn -> MobGrindingUtils.LOGGER.info(spawn.type.getRegistryName().toString()));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
		if (shouldCatchFire(level, pos) || shouldSpawnMob(level, pos))
			level.scheduleTick(pos, this, Mth.nextInt(level.getRandom(), 20, 60));
		return super.updateShape(stateIn, facing, facingState, level, pos, facingPos);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (shouldCatchFire(level, pos) || shouldSpawnMob(level, pos))
			level.scheduleTick(pos, this, Mth.nextInt(level.getRandom(), 20, 60));
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		if (shouldCatchFire(level, pos)) {
			BlockPos posUp = pos.above();
			BlockState blockstate = BaseFireBlock.getState(level, posUp);
			if (level.getBlockState(posUp).isAir() && blockstate.canSurvive(level, posUp))
				level.setBlock(posUp, blockstate, 11);
		}
		if (!shouldCatchFire(level, pos) && shouldSpawnMob(level, pos)) {
			AABB areaToCheck = new AABB(pos).inflate(5, 2, 5);
			int entityCount = level.getEntitiesOfClass(Mob.class, areaToCheck, entity -> entity != null && entity instanceof Enemy).size();

			if (entityCount < 8)
				spawnMob(level, pos);
		}
	}

	public void spawnMob(ServerLevel level, BlockPos pos) {
		Holder<Biome> biomeHolder = level.getBiome(pos);
		Biome biome = !biomeHolder.is(ModTags.Biomes.HOSTILE_OVERRIDE) ? biomeHolder.value() : level.registryAccess().registry(Registries.BIOME)
				.flatMap(reg -> reg.getOptional(Biomes.PLAINS))
				.orElseGet(biomeHolder::value);

		List<SpawnerData> spawns = biome.getMobSettings().getMobs(MobCategory.MONSTER).unwrap();
		if (!spawns.isEmpty()) {
			int indexSize = spawns.size();
			EntityType<?> type = spawns.get(level.getRandom().nextInt(indexSize)).type;
			if (type.is(ModTags.Entities.NO_DIRT_SPAWN) || type.is(ModTags.Entities.NO_DREADFUL_SPAWN))
				return;
			Mob entity = (Mob) type.create(level);
			if (entity == null)
				return;
			entity.setPos(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D);
			if (!checkSpawnPosition(entity, level, MobSpawnType.NATURAL))
				return;
			 if(level.getEntities(entity.getType(), entity.getBoundingBox(), EntitySelector.ENTITY_STILL_ALIVE).isEmpty() && level.noCollision(entity)) {
				 TriState result = DirtSpawnEvent.checkEvent(entity, level, pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D, DirtSpawnEvent.DirtType.DELIGHTFUL);
				 if (result == TriState.FALSE)
					 return;
				 EventHooks.finalizeMobSpawn(entity, level, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null);
				level.addFreshEntity(entity);
			 }
		}
	}

	@Override
    public  boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return true;
    }

	@Override
	public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return 200;
    }

	@Override
    public boolean isFireSource(BlockState state, LevelReader level, BlockPos pos, Direction side) {
		return side == Direction.UP;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
		for (int i = 0; i < 4; ++i) {
			double d0 = (double) ((float) pos.getX() + rand.nextFloat());
			double d1 = (double) ((float) pos.getY() + rand.nextFloat());
			double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
			double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
		}
	}
}
