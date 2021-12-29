package mob_grinding_utils.blocks;

import java.util.List;
import java.util.Random;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
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
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;

public class BlockDelightfulDirt extends Block {

	public BlockDelightfulDirt(Block.Properties properties) {
		super(properties);
	}

	public boolean shouldSnowCap(Level world, BlockPos pos) {
		// standard night ticks
		return world.canSeeSkyFromBelowWater(pos) && (world.getDayTime() >= 13000 && world.getDayTime() <= 23000);
	}

	public boolean shouldSpawnMob(Level world, BlockPos pos) {
		return world.getMaxLocalRawBrightness(pos.above()) >= 10 && world.getBlockState(pos.above()).getMaterial() == Material.AIR;
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (shouldSnowCap(world, pos) || shouldSpawnMob(world, pos))
			world.getBlockTicks().scheduleTick(pos, this, Mth.nextInt(RANDOM, 20,60));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos) {
		if (shouldSnowCap((Level) world, pos) || shouldSpawnMob((Level) world, pos))
			world.getBlockTicks().scheduleTick(pos, this, Mth.nextInt(RANDOM, 20, 60));
		return super.updateShape(stateIn, facing, facingState, world, pos, facingPos);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (shouldSnowCap((Level) world, pos) || shouldSpawnMob((Level) world, pos))
			world.getBlockTicks().scheduleTick(pos, this, Mth.nextInt(RANDOM, 20, 60));
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
		if (shouldSnowCap(world, pos)) {
			BlockPos posUp = pos.above();
			BlockState blockstate = Blocks.SNOW.defaultBlockState();
			if (world.getBlockState(posUp).getMaterial() == Material.AIR && blockstate.canSurvive(world, posUp))
				world.setBlock(posUp, blockstate, 11);
		}
		if (!shouldSnowCap(world, pos) && shouldSpawnMob(world, pos)) {
			AABB areaToCheck = new AABB(pos).inflate(5, 2, 5);
			int entityCount = world.getEntitiesOfClass(Mob.class, areaToCheck, entity -> entity != null && entity.getType().getCategory() == MobCategory.CREATURE).size();

			if (entityCount < 8)
				spawnMob(world, pos);

			if (world.getGameTime() % 20 == 0) {
				BlockPos posUp = pos.above();
				BlockState blockstate = Blocks.GRASS.defaultBlockState();
				if (world.getBlockState(posUp).getMaterial() == Material.AIR) {
					if (rand.nextInt(8) == 0) {
						List<ConfiguredFeature<?, ?>> list = world.getBiome(posUp).getGenerationSettings().getFlowerFeatures();
						if (list.isEmpty())
							return;
						ConfiguredFeature<?, ?> configuredfeature = list.get(0);
						@SuppressWarnings("rawtypes")
						AbstractFlowerFeature flowersfeature = (AbstractFlowerFeature) configuredfeature.feature;
						blockstate = flowersfeature.getRandomFlower(rand, posUp, configuredfeature.config);
					}
					if (blockstate.canSurvive(world, posUp))
						world.setBlock(posUp, blockstate, 3);
				}

			}
		}
	}

	public void spawnMob(ServerLevel world, BlockPos pos) {
		List<SpawnerData> spawns = world.getBiome(pos).getMobSettings().getMobs(MobCategory.CREATURE);
		if (!spawns.isEmpty()) {
			int indexSize = spawns.size();
			EntityType<?> type = spawns.get(RANDOM.nextInt(indexSize)).type;
			if (type == null || !NaturalSpawner.isSpawnPositionOk(SpawnPlacements.getPlacementType(type), world, pos.above(), type))
				return;
			Mob entity = (Mob) type.create(world);
			if (entity != null) {
				entity.setPos(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D);
				if (world.getEntities(entity.getType(), entity.getBoundingBox(), EntitySelector.ENTITY_STILL_ALIVE).isEmpty() && world.noCollision(entity)) {
					entity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null, null);
					world.addFreshEntity(entity);
				 }
			}
		}
	}

	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
		return true;
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
		if(world.getGameTime()%3 == 0 && world.getBlockState(pos.above()).getMaterial() == Material.AIR) {
			for (int i = 0; i < 4; ++i) {
				double d0 = (double) ((float) pos.getX( ));
				double d1 = (double) ((float) pos.getY() + 1D);
				double d2 = (double) ((float) pos.getZ());
				double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
				double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
				double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
				MobGrindingUtils.NETPROXY.spawnGlitterParticles(d0 + d3, d1 + d4, d2 + d5, 0D, 0D, 0D);
			}
		}
	}
}
