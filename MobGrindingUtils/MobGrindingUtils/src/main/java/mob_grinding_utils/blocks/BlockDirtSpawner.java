package mob_grinding_utils.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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
}
