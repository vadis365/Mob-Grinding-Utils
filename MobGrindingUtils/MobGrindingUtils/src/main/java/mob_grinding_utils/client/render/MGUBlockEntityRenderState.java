package mob_grinding_utils.client.render;

import mob_grinding_utils.tile.TileEntityAbsorptionHopper.EnumStatus;
import mob_grinding_utils.tile.TileEntityXPSolidifier.OutputDirection;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jspecify.annotations.Nullable;

/** Immutable-at-submit snapshot for the mod's animated block entities. */
public final class MGUBlockEntityRenderState extends BlockEntityRenderState {
    boolean valid;
    boolean showRenderBox;
    @Nullable AABB renderBox;
    float animation;
    Direction facing = Direction.NORTH;
    OutputDirection outputDirection = OutputDirection.NONE;
    EnumStatus[] absorptionStatus = new EnumStatus[0];
    FluidStack fluid = FluidStack.EMPTY;
    int fluidCapacity;
    ItemStack input = ItemStack.EMPTY;
    ItemStack output = ItemStack.EMPTY;
    @Nullable EntityRenderState spawnedEntity;
    final ItemStackRenderState inputItemState = new ItemStackRenderState();
    final ItemStackRenderState outputItemState = new ItemStackRenderState();
}
