package mob_grinding_utils.network;

import mob_grinding_utils.Reference;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;


public record FlagSyncPacket(boolean wither, boolean dragon) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "flag_sync");

    public FlagSyncPacket(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(FlagSyncPacket message, final PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> MGUClientPackets.handleFlagSyncPacket(message));
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(wither);
        friendlyByteBuf.writeBoolean(dragon);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}