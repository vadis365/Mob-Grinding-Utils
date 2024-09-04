package mob_grinding_utils.network;

import mob_grinding_utils.tile.BEGuiClickable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BEGuiClick(BlockPos tilePos, int buttonID) implements CustomPacketPayload {
    public static final Type<BEGuiClick> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("mob_grinding_utils", "gui_link"));
    public static final StreamCodec<FriendlyByteBuf, BEGuiClick> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, BEGuiClick::tilePos,
            ByteBufCodecs.INT, BEGuiClick::buttonID,
            BEGuiClick::new
    );

    public static void handle(final BEGuiClick message, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            BlockEntity blockEntity = ctx.player().level().getBlockEntity(message.tilePos);
                if (blockEntity instanceof BEGuiClickable clickable) {
                    clickable.buttonClicked(message.buttonID());
                }
            });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
