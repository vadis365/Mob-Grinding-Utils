package mob_grinding_utils.blocks;

import java.util.List;
import java.util.Random;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.MobSpawnInfo.Spawners;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;

public class BlockDelightfulDirt extends Block {

	public BlockDelightfulDirt(Block.Properties properties) {
		super(properties);
	}

	public boolean shouldSnowCap(World world, BlockPos pos) {
		// standard night ticks
		return world.canBlockSeeSky(pos) && (world.getDayTime() >= 13000 && world.getDayTime() <= 23000);
	}

	public boolean shouldSpawnMob(World world, BlockPos pos) {
		return world.getLight(pos.up()) >= 10 && world.getBlockState(pos.up()).getMaterial() == Material.AIR;
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (shouldSnowCap(world, pos) || shouldSpawnMob(world, pos))
			world.getPendingBlockTicks().scheduleTick(pos, this, MathHelper.nextInt(RANDOM, 20,60));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
		if (shouldSnowCap((World) world, pos) || shouldSpawnMob((World) world, pos))
			world.getPendingBlockTicks().scheduleTick(pos, this, MathHelper.nextInt(RANDOM, 20, 60));
		return super.updatePostPlacement(stateIn, facing, facingState, world, pos, facingPos);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (shouldSnowCap((World) world, pos) || shouldSpawnMob((World) world, pos))
			world.getPendingBlockTicks().scheduleTick(pos, this, MathHelper.nextInt(RANDOM, 20, 60));
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (shouldSnowCap(world, pos)) {
			BlockPos posUp = pos.up();
			BlockState blockstate = Blocks.SNOW.getDefaultState();
			if (world.getBlockState(posUp).getMaterial() == Material.AIR && blockstate.isValidPosition(world, posUp))
				world.setBlockState(posUp, blockstate, 11);
		}
		if (!shouldSnowCap(world, pos) && shouldSpawnMob(world, pos)) {
			AxisAlignedBB areaToCheck = new AxisAlignedBB(pos).grow(5, 2, 5);
			int entityCount = world.getEntitiesWithinAABB(MobEntity.class, areaToCheck, entity -> entity != null && entity.getType().getClassification() == EntityClassification.CREATURE).size();

			if (entityCount < 8)
				spawnMob(world, pos);

			if (world.getGameTime() % 20 == 0) {
				BlockPos posUp = pos.up();
				BlockState blockstate = Blocks.GRASS.getDefaultState();
				if (world.getBlockState(posUp).getMaterial() == Material.AIR) {
					if (rand.nextInt(8) == 0) {
						List<ConfiguredFeature<?, ?>> list = world.getBiome(posUp).getGenerationSettings().getFlowerFeatures();
						if (list.isEmpty())
							return;
						ConfiguredFeature<?, ?> configuredfeature = list.get(0);
						@SuppressWarnings("rawtypes")
						FlowersFeature flowersfeature = (FlowersFeature) configuredfeature.feature;
						blockstate = flowersfeature.getFlowerToPlace(rand, posUp, configuredfeature.config);
					}
					if (blockstate.isValidPosition(world, posUp))
						world.setBlockState(posUp, blockstate, 3);
				}

			}
		}
	}

	public void spawnMob(ServerWorld world, BlockPos pos) {
		List<Spawners> spawns = world.getBiome(pos).getMobSpawnInfo().getSpawners(EntityClassification.CREATURE);
		if (!spawns.isEmpty()) {
			int indexSize = spawns.size();
			EntityType<?> type = spawns.get(RANDOM.nextInt(indexSize)).type;
			if (type.isContained(MobGrindingUtils.NOSPAWN))
				return;
			if (type == null || !WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementType(type), world, pos.up(), type))
				return;
			MobEntity entity = (MobEntity) type.create(world);
			if (entity != null) {
				entity.setPosition(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D);
				if (world.getEntitiesWithinAABB(entity.getType(), entity.getBoundingBox(), EntityPredicates.IS_ALIVE).isEmpty() && world.hasNoCollisions(entity)) {
					entity.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.NATURAL, null, null);
					world.addEntity(entity);
				 }
			}
		}
	}

	@Override
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
		return true;
	}

	@Override
    public  boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return true;
    }

	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 200;
    }

	@Override
    public boolean isFireSource(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
		return side == Direction.UP;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
		if(world.getGameTime()%3 == 0 && world.getBlockState(pos.up()).getMaterial() == Material.AIR) {
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
