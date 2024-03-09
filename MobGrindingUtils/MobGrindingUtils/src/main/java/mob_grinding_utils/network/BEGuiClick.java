package mob_grinding_utils.network;

import mob_grinding_utils.tile.BEGuiClickable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record BEGuiClick(BlockPos tilePos, int buttonID) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation("mob_grinding_utils", "gui_link");
    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(tilePos);
        buffer.writeInt(buttonID);
    }

    public BEGuiClick(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readInt());
    }

    public static void handle(final BEGuiClick message, final PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            ctx.level().ifPresent(level -> {
                BlockEntity blockEntity = level.getBlockEntity(message.tilePos());
                if (blockEntity instanceof BEGuiClickable) {
                    ((BEGuiClickable) blockEntity).buttonClicked(message.buttonID());
                }
            });
        });
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
