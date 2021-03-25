package mob_grinding_utils.blocks;

import java.util.List;
import java.util.Random;

import mob_grinding_utils.ModBlocks;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.IMob;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.MobSpawnInfo.Spawners;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockDreadfulDirt extends Block {

	public BlockDreadfulDirt(Block.Properties properties) {
		super(properties);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (world.getLight(pos.up()) >= 10)
			world.getPendingBlockTicks().scheduleTick(pos, this, MathHelper.nextInt(RANDOM, 20,60));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
		if (world.getLight(pos.up()) >= 10)
			world.getPendingBlockTicks().scheduleTick(pos, this, MathHelper.nextInt(RANDOM, 20, 60));
		return super.updatePostPlacement(stateIn, facing, facingState, world, pos, facingPos);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (world.getLight(pos.up()) >= 10)
			world.getPendingBlockTicks().scheduleTick(pos, this, MathHelper.nextInt(RANDOM, 20, 60));
	}

	@Deprecated
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (world.getLight(pos.up()) >= 10) {
			BlockPos posUp = pos.up();
			BlockState blockstate = AbstractFireBlock.getFireForPlacement(world, posUp);
			if (world.getBlockState(posUp).getMaterial() == Material.AIR && blockstate.isValidPosition(world, posUp))
				world.setBlockState(posUp, blockstate, 11);
		}
		else {
			AxisAlignedBB areaToCheck = new AxisAlignedBB(pos).grow(5, 2, 5);
			int entityCount = world.getEntitiesWithinAABB(MobEntity.class, areaToCheck, entity -> entity != null && entity instanceof IMob).size();

			if (entityCount < 8) {
				Direction randomDirection = Direction.getRandomDirection(rand);
				if(randomDirection.getAxis().isHorizontal() && world.getBlockState(pos.offset(randomDirection)).getBlock() == ModBlocks.DREADFUL_DIRT)
					spawnMob(world, pos.offset(randomDirection)); // just to add a little more
				spawnMob(world, pos);
			}
		}
	}

	public void spawnMob(ServerWorld world, BlockPos pos) {
		List<Spawners> spawns = world.getBiome(pos).getMobSpawnInfo().getSpawners(EntityClassification.MONSTER);
		if (!spawns.isEmpty()) {
			int indexSize = spawns.size();
			EntityType<?> type = spawns.get(RANDOM.nextInt(indexSize)).type;
			if (type == null || !WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementType(type), world, pos.up(), type))
				return;
			MobEntity entity = (MobEntity) type.create(world);
			if (entity != null) {
				entity.setPosition(pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D);
				entity.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.NATURAL, null, null);
				world.addEntity(entity);
			}
		}
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
        if (side == Direction.UP)
            return true;
        return false;
    }

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
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
