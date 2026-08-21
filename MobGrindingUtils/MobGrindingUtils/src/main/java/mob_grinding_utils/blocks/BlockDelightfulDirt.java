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
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.minecraft.util.TriState;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nonnull;
import java.util.List;
public class BlockDelightfulDirt extends BlockDirtSpawner {

	public BlockDelightfulDirt(Block.Properties properties) {
		super(properties);
	}

	public boolean shouldSnowCap(LevelReader level, BlockPos pos) {
		// standard night ticks
		long time = level instanceof LevelAccessor accessor ? accessor.getLevelData().getGameTime() % 24000 : 0;
		return level.canSeeSkyFromBelowWater(pos) && time >= 13000 && time <= 23000;
	}

	public boolean shouldSpawnMob(LevelReader level, BlockPos pos) {
		return level.getMaxLocalRawBrightness(pos.above()) >= 10 && level.getBlockState(pos.above()).isAir();
	}

	@Override
	public void onPlace(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean isMoving) {
		if (shouldSnowCap(level, pos) || shouldSpawnMob(level, pos))
			level.scheduleTick(pos, this, Mth.nextInt(level.getRandom(), 20,60));
	}

	@Nonnull
	@SuppressWarnings("deprecation")
	@Override
	protected BlockState updateShape(@Nonnull BlockState stateIn, @Nonnull LevelReader level, @Nonnull ScheduledTickAccess ticks, @Nonnull BlockPos pos, @Nonnull Direction facing, @Nonnull BlockPos facingPos, @Nonnull BlockState facingState, @Nonnull RandomSource random) {
		if (shouldSnowCap(level, pos) || shouldSpawnMob(level, pos))
			ticks.scheduleTick(pos, this, Mth.nextInt(random, 20, 60));
		return super.updateShape(stateIn, level, ticks, pos, facing, facingPos, facingState, random);
	}

	@Override
	protected void neighborChanged(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Block blockIn, Orientation orientation, boolean isMoving) {
		if (shouldSnowCap(level, pos) || shouldSpawnMob(level, pos))
			level.scheduleTick(pos, this, Mth.nextInt(level.getRandom(), 20, 60));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		if (shouldSnowCap(level, pos)) {
			BlockPos posUp = pos.above();
			BlockState blockstate = Blocks.SNOW.defaultBlockState();
			if (level.getBlockState(posUp).isAir() && blockstate.canSurvive(level, posUp))
				level.setBlock(posUp, blockstate, 11);
		}
		if (!shouldSnowCap(level, pos) && shouldSpawnMob(level, pos)) {
			AABB areaToCheck = new AABB(pos).inflate(5, 2, 5);
			int entityCount = level.getEntitiesOfClass(Mob.class, areaToCheck, entity -> entity != null && entity.getType().getCategory() == MobCategory.CREATURE).size();

			if (entityCount < 8)
				spawnMob(level, pos);

			if (rand.nextInt(20) == 0) {
				BlockPos posUp = pos.above();
				if (level.getBlockState(posUp).isAir()) {
					PlacedFeature placedfeature;
					if (rand.nextInt(8) == 0) {
						List<ConfiguredFeature<?, ?>> list = level.getBiome(posUp).value().getGenerationSettings().getBoneMealFeatures();
						if (list.isEmpty())
							return;
						list.get(rand.nextInt(list.size())).place(level, level.getChunkSource().getGenerator(), rand, posUp);
						return;
					 } else {
							placedfeature = level.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE).get(VegetationPlacements.GRASS_BONEMEAL).orElseThrow().value();
			            }
					 placedfeature.place(level, level.getChunkSource().getGenerator(), rand, posUp);	
				}
			}
		}
	}

	public void spawnMob(ServerLevel level, BlockPos pos) {
		Holder<Biome> biomeHolder = level.getBiome(pos);
		Biome biome = !biomeHolder.is(ModTags.Biomes.PASSIVE_OVERRIDE) ? biomeHolder.value() : level.registryAccess().lookupOrThrow(Registries.BIOME)
				.getOptional(Biomes.PLAINS).orElseGet(biomeHolder::value);

		var spawns = biome.getMobSettings().getMobs(MobCategory.CREATURE).unwrap();
//		MobGrindingUtils.LOGGER.info("Spawns: " + spawns.size());
//		spawns.forEach(s -> MobGrindingUtils.LOGGER.info(s.toString()));
		if (!spawns.isEmpty()) {
			int indexSize = spawns.size();
			EntityType<?> type = spawns.get(level.getRandom().nextInt(indexSize)).value().type();
			if (type.builtInRegistryHolder().is(ModTags.Entities.NO_DIRT_SPAWN) || type.builtInRegistryHolder().is(ModTags.Entities.NO_DELIGHTFUL_SPAWN))
				return;
			Mob entity = (Mob) type.create(level, EntitySpawnReason.NATURAL);
			if (entity == null)
				return;
			entity.setPos(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D);
			if (!checkSpawnPosition(entity, level, EntitySpawnReason.NATURAL))
				return;
			if (level.getEntities(entity.getType(), entity.getBoundingBox(), EntitySelector.ENTITY_STILL_ALIVE).isEmpty() && level.noCollision(entity)) {
				TriState result = DirtSpawnEvent.checkEvent(entity, level, pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D, DirtSpawnEvent.DirtType.DELIGHTFUL);
				if (result == TriState.FALSE)
					return;
				EventHooks.finalizeMobSpawn(entity, level, level.getCurrentDifficultyAt(pos), EntitySpawnReason.NATURAL, null);
				level.addFreshEntity(entity);
			 }
		}
	}

	@Nonnull
	@Override
	public TriState canSustainPlant(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull Direction facing, @Nonnull BlockState plant) {
		return TriState.TRUE;
	}

	@Override
    public  boolean isFlammable(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull Direction face) {
        return true;
    }

	@Override
	public int getFlammability(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull Direction face) {
        return 200;
    }

	@Override
    public boolean isFireSource(@Nonnull BlockState state, @Nonnull LevelReader level, @Nonnull BlockPos pos, @Nonnull Direction side) {
		return side == Direction.UP;
	}

	@Override
	public void animateTick(@Nonnull BlockState stateIn, Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		if(level.getGameTime()%3 == 0 && level.getBlockState(pos.above()).isAir()) {
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
