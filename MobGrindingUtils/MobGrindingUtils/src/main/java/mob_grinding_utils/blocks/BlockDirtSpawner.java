package mob_grinding_utils.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class BlockDirtSpawner extends Block {
    public BlockDirtSpawner(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void tick(@Nonnull BlockState pState, @Nonnull ServerLevel pLevel, @Nonnull BlockPos pPos, @Nonnull RandomSource pRandom) {
        randomTick(pState, pLevel, pPos, pRandom);
    }

    public static boolean checkSpawnPosition(Mob mob, ServerLevelAccessor level, MobSpawnType spawnType) {
        return mob.checkSpawnRules(level, spawnType) && mob.checkSpawnObstruction(level); // Yoinked from EventHooks.checkSpawnPosition, but without the event post.
    }
}
