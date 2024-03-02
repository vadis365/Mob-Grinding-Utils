package mob_grinding_utils.tile;

import mob_grinding_utils.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class TileEntityJumboTank extends TileEntityTank {

	public TileEntityJumboTank(BlockPos pos, BlockState state) {
		super(ModBlocks.JUMBO_TANK.getTileEntityType(), new FluidTank(1000 *  1024), pos, state);
	}
}
