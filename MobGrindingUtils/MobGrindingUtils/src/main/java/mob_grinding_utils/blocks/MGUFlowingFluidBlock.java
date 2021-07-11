package mob_grinding_utils.blocks;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;

import java.util.function.Supplier;

public class MGUFlowingFluidBlock extends FlowingFluidBlock {
    public MGUFlowingFluidBlock(FlowingFluid fluidIn, Properties builder) {
        super(fluidIn, builder);
    }

    public MGUFlowingFluidBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
        super(supplier, properties);
    }
}
