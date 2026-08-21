package mob_grinding_utils.util;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;

/** One-slot fluid storage exposed through NeoForge's transaction API. */
public final class FluidTankStorage extends FluidStacksResourceHandler {
    private final Runnable changed;

    public FluidTankStorage(int capacity, Runnable changed) {
        super(1, capacity);
        this.changed = changed;
    }

    @Override
    protected void onContentsChanged(int slot, FluidStack previousContents) {
        changed.run();
    }

    public FluidStack stack() {
        return getResource(0).toStack(getAmountAsInt(0));
    }

    public void replace(FluidStack stack) {
        set(0, FluidResource.of(stack), stack.getAmount());
    }

    public int amount() {
        return getAmountAsInt(0);
    }

    public int capacity() {
        return getCapacityAsInt(0, getResource(0));
    }
}
