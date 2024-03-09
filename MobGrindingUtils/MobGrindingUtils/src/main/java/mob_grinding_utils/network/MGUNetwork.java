package mob_grinding_utils.network;

import mob_grinding_utils.Reference;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class MGUNetwork {
    public static void register(final RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar reg = event.registrar(Reference.MOD_ID);

        reg.play(BEGuiClick.ID, BEGuiClick::new, handler -> handler.server(BEGuiClick::handle));
        reg.play(ChickenSyncPacket.ID, ChickenSyncPacket::new, handler -> handler.client(ChickenSyncPacket::handle));
        reg.play(TapParticlePacket.ID, TapParticlePacket::new, handler -> handler.client(TapParticlePacket::handle));
        reg.play(FlagSyncPacket.ID, FlagSyncPacket::new, handler -> handler.client(FlagSyncPacket::handle));
    }
}
