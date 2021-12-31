package mob_grinding_utils.blocks;

import java.util.List;
import java.util.Random;

import mob_grinding_utils.network.MGUClientPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
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
			level.scheduleTick(pos, this, Mth.nextInt(RANDOM, 20,60));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
		if (shouldSnowCap((Level) level, pos) || shouldSpawnMob((Level) level, pos))
			level.scheduleTick(pos, this, Mth.nextInt(RANDOM, 20, 60));
		return super.updateShape(stateIn, facing, facingState, level, pos, facingPos);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (shouldSnowCap((Level) level, pos) || shouldSpawnMob((Level) level, pos))
			level.scheduleTick(pos, this, Mth.nextInt(RANDOM, 20, 60));
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
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
						List<ConfiguredFeature<?, ?>> list = level.getBiome(posUp).getGenerationSettings().getFlowerFeatures();
						if (list.isEmpty())
							return;
						placedfeature = ((RandomPatchConfiguration)list.get(0).config()).feature().get();
					 } else {
			               placedfeature = VegetationPlacements.GRASS_BONEMEAL;
			            }
					 placedfeature.place(level, level.getChunkSource().getGenerator(), rand, posUp);	
				}
			}
		}
	}

	public void spawnMob(ServerLevel level, BlockPos pos) {
		List<SpawnerData> spawns = level.getBiome(pos).getMobSettings().getMobs(MobCategory.CREATURE).unwrap();
		if (!spawns.isEmpty()) {
			int indexSize = spawns.size();
			EntityType<?> type = spawns.get(RANDOM.nextInt(indexSize)).type;
			if (type == null || !NaturalSpawner.isSpawnPositionOk(SpawnPlacements.getPlacementType(type), level, pos.above(), type))
				return;
			Mob entity = (Mob) type.create(level);
			if (entity != null) {
				entity.setPos(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D);
				if (level.getEntities(entity.getType(), entity.getBoundingBox(), EntitySelector.ENTITY_STILL_ALIVE).isEmpty() && level.noCollision(entity)) {
					entity.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null, null);
					level.addFreshEntity(entity);
				 }
			}
		}
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
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, Random rand) {
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
