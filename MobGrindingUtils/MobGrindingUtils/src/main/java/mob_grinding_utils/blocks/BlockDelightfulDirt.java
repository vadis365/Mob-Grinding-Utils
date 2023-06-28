package mob_grinding_utils.blocks;

import mob_grinding_utils.ModTags;
import mob_grinding_utils.events.DirtSpawnEvent;
import mob_grinding_utils.network.MGUClientPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nonnull;
import java.util.List;
public class BlockDelightfulDirt extends Block {

	public BlockDelightfulDirt(Block.Properties properties) {
		super(properties);
	}

	public boolean shouldSnowCap(Level level, BlockPos pos) {
		// standard night ticks
		return level.canSeeSkyFromBelowWater(pos) && (level.getDayTime() >= 13000 && level.getDayTime() <= 23000);
	}

	public boolean shouldSpawnMob(Level level, BlockPos pos) {
		return level.getMaxLocalRawBrightness(pos.above()) >= 10 && level.getBlockState(pos.above()).getMaterial() == Material.AIR;
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (shouldSnowCap(level, pos) || shouldSpawnMob(level, pos))
			level.scheduleTick(pos, this, Mth.nextInt(level.random, 20,60));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
		if (shouldSnowCap((Level) level, pos) || shouldSpawnMob((Level) level, pos))
			level.scheduleTick(pos, this, Mth.nextInt(level.getRandom(), 20, 60));
		return super.updateShape(stateIn, facing, facingState, level, pos, facingPos);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (shouldSnowCap((Level) level, pos) || shouldSpawnMob((Level) level, pos))
			level.scheduleTick(pos, this, Mth.nextInt(level.random, 20, 60));
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	@Override
	public void tick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		if (shouldSnowCap(level, pos)) {
			BlockPos posUp = pos.above();
			BlockState blockstate = Blocks.SNOW.defaultBlockState();
			if (level.getBlockState(posUp).getMaterial() == Material.AIR && blockstate.canSurvive(level, posUp))
				level.setBlock(posUp, blockstate, 11);
		}
		if (!shouldSnowCap(level, pos) && shouldSpawnMob(level, pos)) {
			AABB areaToCheck = new AABB(pos).inflate(5, 2, 5);
			int entityCount = level.getEntitiesOfClass(Mob.class, areaToCheck, entity -> entity != null && entity.getType().getCategory() == MobCategory.CREATURE).size();

			if (entityCount < 8)
				spawnMob(level, pos);

			if (level.getGameTime() % 20 == 0) {
				BlockPos posUp = pos.above();
				if (level.getBlockState(posUp).getMaterial() == Material.AIR) {
					PlacedFeature placedfeature;
					if (rand.nextInt(8) == 0) {
						List<ConfiguredFeature<?, ?>> list = level.getBiome(posUp).value().getGenerationSettings().getFlowerFeatures();
						if (list.isEmpty())
							return;
						placedfeature = ((RandomPatchConfiguration)list.get(0).config()).feature().value();
					 } else {
							placedfeature = level.registryAccess().registryOrThrow(Registries.PLACED_FEATURE).get(VegetationPlacements.GRASS_BONEMEAL);
			            }
					 placedfeature.place(level, level.getChunkSource().getGenerator(), rand, posUp);	
				}
			}
		}
	}

	public void spawnMob(ServerLevel level, BlockPos pos) {
		Holder<Biome> biomeHolder = level.getBiome(pos);
		Biome biome = !biomeHolder.is(ModTags.Biomes.PASSIVE_OVERRIDE) ? biomeHolder.value() : level.registryAccess().registry(Registries.BIOME)
				.flatMap(reg -> reg.getOptional(Biomes.PLAINS))
				.orElseGet(biomeHolder::value);

		List<SpawnerData> spawns = biome.getMobSettings().getMobs(MobCategory.CREATURE).unwrap();
//		MobGrindingUtils.LOGGER.info("Spawns: " + spawns.size());
//		spawns.forEach(s -> MobGrindingUtils.LOGGER.info(s.toString()));
		if (!spawns.isEmpty()) {
			int indexSize = spawns.size();
			EntityType<?> type = spawns.get(level.random.nextInt(indexSize)).type;
			if (type.is(ModTags.Entities.NO_DIRT_SPAWN))
				return;
			if (type == null || !NaturalSpawner.isSpawnPositionOk(SpawnPlacements.getPlacementType(type), level, pos.above(), type))
				return;
			Mob entity = (Mob) type.create(level);
			if (entity != null) {
				entity.setPos(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D);
				if (level.getEntities(entity.getType(), entity.getBoundingBox(), EntitySelector.ENTITY_STILL_ALIVE).isEmpty() && level.noCollision(entity)) {
					Event.Result result = DirtSpawnEvent.checkEvent(entity, level, pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D, DirtSpawnEvent.DirtType.DELIGHTFUL);
					if (result == Event.Result.DENY) {
						return;
					}

					entity.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null, null);
					level.addFreshEntity(entity);
				 }
			}
		}
	}

	public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
		if (entityType == null)
			return super.isValidSpawn(state, level, pos, type, entityType);
		else
			return entityType.getCategory() == MobCategory.CREATURE;
	}

	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, Direction facing, IPlantable plantable) {
		return true;
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
		if(level.getGameTime()%3 == 0 && level.getBlockState(pos.above()).getMaterial() == Material.AIR) {
			for (int i = 0; i < 4; ++i) {
				double d0 = (double) ((float) pos.getX( ));
				double d1 = (double) ((float) pos.getY() + 1D);
				double d2 = (double) ((float) pos.getZ());
				double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
				double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
				double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
				MGUClientPackets.spawnGlitterParticles(d0 + d3, d1 + d4, d2 + d5, 0D, 0D, 0D);
			}
		}
	}
}
