package mob_grinding_utils.blocks;

import mob_grinding_utils.tile.TileEntityJumboTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class BlockTankJumbo extends BlockTank {
	public BlockTankJumbo(Block.Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileEntityJumboTank();
	}
}
