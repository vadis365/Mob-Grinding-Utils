package mob_grinding_utils.tile;

import mob_grinding_utils.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;

public class TileEntityJumboTank extends TileEntityTank {
    public TileEntityJumboTank(BlockPos pos, BlockState state) {
        super(ModBlocks.JUMBO_TANK.getTileEntityType(), new FluidStacksResourceHandler(1, 1000 *  1024), pos, state);
    }
}
