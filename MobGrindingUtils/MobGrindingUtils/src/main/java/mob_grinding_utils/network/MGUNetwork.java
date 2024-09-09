package mob_grinding_utils.network;

import mob_grinding_utils.Reference;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class MGUNetwork {
    public static void register(final RegisterPayloadHandlersEvent event) {
        event.registrar(Reference.MOD_ID)
        .playToServer(BEGuiClick.TYPE, BEGuiClick.CODEC, BEGuiClick::handle)
        .playToClient(ChickenSyncPacket.TYPE, ChickenSyncPacket.STREAM_CODEC, ChickenSyncPacket::handle)
        .playToClient(TapParticlePacket.TYPE, TapParticlePacket.STREAM_CODEC, TapParticlePacket::handle)
        .playToClient(FlagSyncPacket.TYPE, FlagSyncPacket.STREAM_CODEC, FlagSyncPacket::handle);
    }
}
