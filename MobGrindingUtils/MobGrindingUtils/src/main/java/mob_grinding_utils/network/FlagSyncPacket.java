package mob_grinding_utils.network;

import mob_grinding_utils.Reference;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public record FlagSyncPacket(boolean wither, boolean dragon) implements CustomPacketPayload {
    public static final Type<FlagSyncPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "flag_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FlagSyncPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, FlagSyncPacket::wither,
            ByteBufCodecs.BOOL, FlagSyncPacket::dragon,
            FlagSyncPacket::new
        );

    public static void handle(FlagSyncPacket message, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> MGUClientPackets.handleFlagSyncPacket(message));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}