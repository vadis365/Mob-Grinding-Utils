package mob_grinding_utils.network;

import mob_grinding_utils.Reference;
import net.minecraft.resources.ResourceLocation;
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

        channel.messageBuilder(MessageChickenSync.class, 1, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MessageChickenSync::decode)
                .encoder(MessageChickenSync::encode)
                .consumer(MessageChickenSync::handle)
                .add();

        channel.messageBuilder(MessageTapParticle.class, 2, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MessageTapParticle::decode)
                .encoder(MessageTapParticle::encode)
                .consumer(MessageTapParticle::handle)
                .add();

        channel.messageBuilder(MessageFan.class, 3, NetworkDirection.PLAY_TO_SERVER)
                .decoder(MessageFan::decode)
                .encoder(MessageFan::encode)
                .consumer(MessageFan::handle)
                .add();

        channel.messageBuilder(MessageFlagSync.class, 4, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MessageFlagSync::decode)
                .encoder(MessageFlagSync::encode)
                .consumer(MessageFlagSync::handle)
                .add();

        channel.messageBuilder(MessageSolidifier.class, 5, NetworkDirection.PLAY_TO_SERVER)
                .decoder(MessageSolidifier::decode)
                .encoder(MessageSolidifier::encode)
                .consumer(MessageSolidifier::handle)
                .add();

        channel.messageBuilder(MessageEntitySpawner.class, 6, NetworkDirection.PLAY_TO_SERVER)
		        .decoder(MessageEntitySpawner::decode)
		        .encoder(MessageEntitySpawner::encode)
		        .consumer(MessageEntitySpawner::handle)
		        .add();

        return channel;
    }
}
