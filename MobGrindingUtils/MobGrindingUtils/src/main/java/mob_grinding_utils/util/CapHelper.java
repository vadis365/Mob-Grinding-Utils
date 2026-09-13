package mob_grinding_utils.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class CapHelper {
    @Nonnull
    public static Optional<ResourceHandler<ItemResource>> getItemHandler(@Nonnull Level level, @Nonnull BlockPos pos, @Nullable Direction side) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.hasBlockEntity()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                return Optional.ofNullable(level.getCapability(Capabilities.Item.BLOCK, pos, blockState, blockEntity, side));
            }
        }
        return Optional.empty();
    }

    @Nonnull
    public static Optional<ResourceHandler<ItemResource>> getItemHandler(ItemStack stack) {
        return Optional.ofNullable(ItemAccess.forStack(stack).getCapability(Capabilities.Item.ITEM));
    }

    @Nonnull
    public static Optional<ResourceHandler<FluidResource>> getFluidHandler(@Nonnull Level level, @Nonnull BlockPos pos, @Nullable Direction side) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.hasBlockEntity()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                return Optional.ofNullable(level.getCapability(Capabilities.Fluid.BLOCK, pos, blockState, blockEntity, side));
            }
        }
        return Optional.empty();
    }

    @Nonnull
    public static Optional<ResourceHandler<FluidResource>> getItemFluidHandler(ItemStack stack) {
        return Optional.ofNullable(ItemAccess.forStack(stack).oneByOne().getCapability(Capabilities.Fluid.ITEM));
    }
}
