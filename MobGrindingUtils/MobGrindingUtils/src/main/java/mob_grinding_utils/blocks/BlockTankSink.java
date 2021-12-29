package mob_grinding_utils.blocks;

import mob_grinding_utils.tile.TileEntitySinkTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class BlockTankSink extends BlockTank {
	public BlockTankSink(Block.Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileEntitySinkTank();
	}
}
