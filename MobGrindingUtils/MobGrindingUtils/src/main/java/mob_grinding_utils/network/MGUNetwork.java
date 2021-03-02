package mob_grinding_utils.network;

import mob_grinding_utils.Reference;
import net.minecraft.util.ResourceLocation;
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
/*
        channel.messageBuilder(MessageAbsorptionHopper.class, 0, NetworkDirection.PLAY_TO_SERVER)
                .decoder(MessageAbsorptionHopper::fromBytes)
                .encoder(MessageAbsorptionHopper::toBytes)
                .consumer(MessageAbsorptionHopper::onMessage)
                .add();


        NETWORK_WRAPPER.registerMessage(MessageAbsorptionHopper.class, MessageAbsorptionHopper.class, 0, Side.SERVER);
        NETWORK_WRAPPER.registerMessage(MessageChickenSync.class, MessageChickenSync.class, 1, Side.CLIENT);
        NETWORK_WRAPPER.registerMessage(MessageTapParticle.class, MessageTapParticle.class, 2, MixinEnvironment.Side.CLIENT);
        NETWORK_WRAPPER.registerMessage(MessageSyncEntityCapabilities.class, MessageSyncEntityCapabilities.class, 3, Side.CLIENT);
        NETWORK_WRAPPER.registerMessage(MessageFan.class, MessageFan.class, 4, Side.SERVER);
*/

        return channel;
    }
}
