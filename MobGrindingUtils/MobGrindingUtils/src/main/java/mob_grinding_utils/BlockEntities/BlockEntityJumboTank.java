package mob_grinding_utils.BlockEntities;

import mob_grinding_utils.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;

public class BlockEntityJumboTank extends BlockEntityTank {
    public BlockEntityJumboTank(BlockPos pos, BlockState state) {
        super(ModBlocks.JUMBO_TANK.getTileEntityType(), new FluidStacksResourceHandler(1, 1000 *  1024), pos, state);
    }
}
