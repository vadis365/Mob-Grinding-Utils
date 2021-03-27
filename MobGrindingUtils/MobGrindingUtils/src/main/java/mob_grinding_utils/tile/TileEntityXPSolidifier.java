package mob_grinding_utils.tile;

import io.netty.buffer.Unpooled;
import mob_grinding_utils.ModBlocks;
import mob_grinding_utils.inventory.server.ContainerXPSolidifier;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class TileEntityXPSolidifier extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    public FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME *  16);
    private final LazyOptional<IFluidHandler> tank_holder = LazyOptional.of(() -> tank);
    private int prevFluidLevel = 0;

    public ItemStackHandler inputSlots = new ItemStackHandler(2);
    public ItemStackHandler outputSlot = new ItemStackHandler(1);
    private final LazyOptional<IItemHandler> outputCap = LazyOptional.of(() -> outputSlot);


    public TileEntityXPSolidifier() {
        super(ModBlocks.XPSOLIDIFIER_TILE);
    }

    public enum OutputDirection implements IStringSerializable {
        NONE("none"),
        NORTH("north"),
        EAST("east"),
        SOUTH("south"),
        WEST("west");

        String name;
        OutputDirection(String nameIn) {name = nameIn;}

        @Override
        public String getString() { return name; }

        public static OutputDirection fromString(String string) {
            try {
                return OutputDirection.valueOf(string);
            }
            catch(IllegalArgumentException ex) {
                return OutputDirection.NONE;
            }
        }
    }
    public OutputDirection outputDirection = OutputDirection.NONE;

    public OutputDirection toggleOutput() {
        switch(outputDirection) {
            case WEST:
                outputDirection = OutputDirection.NONE;
                break;
            case SOUTH:
                outputDirection = OutputDirection.WEST;
                break;
            case EAST:
                outputDirection = OutputDirection.SOUTH;
                break;
            case NORTH:
                outputDirection = OutputDirection.EAST;
                break;
            case NONE:
                outputDirection = OutputDirection.NORTH;
                break;
        }
        markDirty();
        return outputDirection;
    }

    @Override
    public void tick() {
        if (getWorld().getGameTime() % 20 == 0)
            run();

        if (prevFluidLevel != tank.getFluidAmount()){
            updateBlock();
            prevFluidLevel = tank.getFluidAmount();
        }
    }

    private void run() {
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        tank.readFromNBT(nbt);
        inputSlots.deserializeNBT(nbt.getCompound("inputSlots"));
        outputSlot.deserializeNBT(nbt.getCompound("outputSlot"));
        outputDirection = OutputDirection.fromString(nbt.getString("outputDirection"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        tank.writeToNBT(compound);
        compound.put("inputSlots", inputSlots.serializeNBT());
        compound.put("outputSlot", outputSlot.serializeNBT());
        compound.putString("outputDirection", outputDirection.getString());
        return compound;
    }
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        return write(nbt);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        write(nbt);
        return new SUpdateTileEntityPacket(getPos(), 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        read(getBlockState(), packet.getNbtCompound());
        onContentsChanged();
    }

    public void updateBlock() {
        getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
    }

    public void onContentsChanged() {
        if (this != null && !getWorld().isRemote) {
            final BlockState state = getWorld().getBlockState(getPos());
            getWorld().notifyBlockUpdate(getPos(), state, state, 8);
            markDirty();
        }
    }

    public int getScaledFluid(int scale) {
        return tank.getFluid() != null ? (int) ((float) tank.getFluid().getAmount() / (float) tank.getCapacity() * scale) : 0;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("block.mob_grinding_utils.xpsolidifier");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new ContainerXPSolidifier(p_createMenu_1_, p_createMenu_2_, new PacketBuffer(Unpooled.buffer()).writeBlockPos(pos));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return tank_holder.cast();
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return outputCap.cast();
        return super.getCapability(cap, side);
    }
}
