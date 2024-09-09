package mob_grinding_utils.network;


import mob_grinding_utils.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public record TapParticlePacket(BlockPos tilePos) implements CustomPacketPayload {
	public static final Type<TapParticlePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "tap_particle"));
	public static final StreamCodec<RegistryFriendlyByteBuf, TapParticlePacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, TapParticlePacket::tilePos, TapParticlePacket::new
		);

	public static void handle(TapParticlePacket message, final IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			MGUClientPackets.spawnGlitterParticles(message.tilePos.getX(), message.tilePos.getY(), message.tilePos.getZ(), 0D, 0D, 0D);
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}