package mob_grinding_utils.network;

import mob_grinding_utils.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MGUNetwork {
    public static final ResourceLocation channelName = new ResourceLocation(Reference.MOD_ID, "network");
    public static final String networkVersion = new ResourceLocation(Reference.MOD_ID, "1").toString();


    public static SimpleChannel getNetworkChannel() {
        final SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(channelName)
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .networkProtocolVersion(() -> networkVersion)
                .simpleChannel();

        channel.messageBuilder(MessageAbsorptionHopper.class, 0, NetworkDirection.PLAY_TO_SERVER)
                .decoder(MessageAbsorptionHopper::decode)
                .encoder(MessageAbsorptionHopper::encode)
                .consumer(MessageAbsorptionHopper::handle)
                .add();

        channel.messageBuilder(MessageChickenSync.class, 1, NetworkDirection.PLAY_TO_SERVER)
                .decoder(MessageChickenSync::decode)
                .encoder(MessageChickenSync::encode)
                .consumer(MessageChickenSync::handle)
                .add();

/*
        NETWORK_WRAPPER.registerMessage(MessageChickenSync.class, MessageChickenSync.class, 1, Side.CLIENT);
        NETWORK_WRAPPER.registerMessage(MessageTapParticle.class, MessageTapParticle.class, 2, MixinEnvironment.Side.CLIENT);
        NETWORK_WRAPPER.registerMessage(MessageSyncEntityCapabilities.class, MessageSyncEntityCapabilities.class, 3, Side.CLIENT);
        NETWORK_WRAPPER.registerMessage(MessageFan.class, MessageFan.class, 4, Side.SERVER);
*/

        return channel;
    }
}
