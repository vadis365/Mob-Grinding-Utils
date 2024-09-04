package mob_grinding_utils.network;

import mob_grinding_utils.Reference;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class MGUNetwork {
    public static void register(final RegisterPayloadHandlersEvent event) {
        event.registrar(Reference.MOD_ID)
        .playToServer(BEGuiClick.TYPE, BEGuiClick.CODEC, BEGuiClick::handle)
        .playToClient(ChickenSyncPacket.TYPE, ChickenSyncPacket.STREAM_CODEC, ChickenSyncPacket::handle)
        .playToClient(TapParticlePacket.ID, TapParticlePacket::new, TapParticlePacket::handle)
        .playToClient(FlagSyncPacket.ID, FlagSyncPacket::new, FlagSyncPacket::handle);
    }
}
