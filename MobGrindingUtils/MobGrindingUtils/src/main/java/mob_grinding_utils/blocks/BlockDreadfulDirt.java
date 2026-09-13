package mob_grinding_utils.blocks;

import mob_grinding_utils.ModTags;
import mob_grinding_utils.events.DirtSpawnEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class BlockDreadfulDirt extends BlockDirtSpawner {
	public BlockDreadfulDirt(Identifier id) {
		this(Block.Properties.of()
				.mapColor(MapColor.COLOR_BLACK)
				.strength(1.0F, 2000.0F)
				.sound(SoundType.GRAVEL)
				.randomTicks()
				.isValidSpawn((state, level, pos, entitytype) -> entitytype.getCategory() == MobCategory.MONSTER)
				.setId(ResourceKey.create(Registries.BLOCK, id)));
	}

	public BlockDreadfulDirt(Block.Properties properties) {
		super(properties);
	}

	public boolean shouldCatchFire(LevelAccessor level, BlockPos pos) {
		return level.canSeeSkyFromBelowWater(pos) && (level.getGameTime() < 13000 || level.getGameTime() > 23000);
	}

	public boolean shouldSpawnMob(LevelAccessor level, BlockPos pos) {
		return level.getMaxLocalRawBrightness(pos.above()) < 5;
	}

	@Override
	public void onPlace(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean isMoving) {
		if (shouldCatchFire(level, pos) || shouldSpawnMob(level, pos))
			level.scheduleTick(pos, this, Mth.nextInt(level.getRandom(), 20,60));
	}

	@Nonnull
	@Override
	protected BlockState updateShape(@Nonnull BlockState stateIn, @Nonnull LevelReader level, @Nonnull ScheduledTickAccess ticks, @Nonnull BlockPos pos, @Nonnull Direction facing, @Nonnull BlockPos facingPos, @Nonnull BlockState facingState, @Nonnull RandomSource random) {
		if (level instanceof LevelAccessor levelAccessor && (shouldCatchFire(levelAccessor, pos) || shouldSpawnMob(levelAccessor, pos)))
			ticks.scheduleTick(pos, this, Mth.nextInt(random, 20, 60));
		return super.updateShape(stateIn, level, ticks, pos, facing, facingPos, facingState, random);
	}

	@Override
	protected void neighborChanged(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nullable Orientation orientation, boolean isMoving) {
		if (shouldCatchFire(level, pos) || shouldSpawnMob(level, pos))
			level.scheduleTick(pos, this, Mth.nextInt(level.getRandom(), 20, 60));
	}

	@Override
	public void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		if (shouldCatchFire(level, pos)) {
			BlockPos posUp = pos.above();
			BlockState blockstate = BaseFireBlock.getState(level, posUp);
			if (level.getBlockState(posUp).isAir() && blockstate.canSurvive(level, posUp))
				level.setBlock(posUp, blockstate, 11);
		}
		if (!shouldCatchFire(level, pos) && shouldSpawnMob(level, pos)) {
			AABB areaToCheck = new AABB(pos).inflate(5, 2, 5);
			int entityCount = level.getEntitiesOfClass(Mob.class, areaToCheck, entity -> entity instanceof Enemy).size();

			if (entityCount < 8)
				spawnMob(level, pos);
		}
	}

	public void spawnMob(ServerLevel level, BlockPos pos) {
		Holder<Biome> biomeHolder = level.getBiome(pos);
		Biome biome = !biomeHolder.is(ModTags.Biomes.HOSTILE_OVERRIDE) ? biomeHolder.value() : level.registryAccess().lookupOrThrow(Registries.BIOME)
				.get(Biomes.PLAINS)
				.map(Holder::value)
				.orElseGet(biomeHolder::value);

		WeightedList<SpawnerData> spawns = biome.getMobSettings().getMobs(MobCategory.MONSTER);
		if (!spawns.isEmpty()) {
			EntityType<?> type = spawns.getRandomOrThrow(level.getRandom()).type();
			if (type.builtInRegistryHolder().is(ModTags.Entities.NO_DIRT_SPAWN) || type.builtInRegistryHolder().is(ModTags.Entities.NO_DREADFUL_SPAWN))
				return;
			Mob entity = (Mob) type.create(level, EntitySpawnReason.NATURAL);
			if (entity == null)
				return;
			entity.setPos(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D);
			if (!checkSpawnPosition(entity, level, EntitySpawnReason.NATURAL))
				return;
			 if(level.getEntities(entity.getType(), entity.getBoundingBox(), EntitySelector.ENTITY_STILL_ALIVE).isEmpty() && level.noCollision(entity)) {
				 TriState result = DirtSpawnEvent.checkEvent(entity, level, pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D, DirtSpawnEvent.DirtType.DREADFUL);
				 if (result == TriState.FALSE)
					 return;
				 EventHooks.finalizeMobSpawn(entity, level, level.getCurrentDifficultyAt(pos), EntitySpawnReason.NATURAL, null);
				level.addFreshEntity(entity);
			 }
		}
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
	public void animateTick(@Nonnull BlockState stateIn, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
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
