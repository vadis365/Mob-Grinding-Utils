package mob_grinding_utils.network;


import mob_grinding_utils.Reference;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;


public record TapParticlePacket(BlockPos tilePos) implements CustomPacketPayload {
	public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "tap_particle");

	public TapParticlePacket(final FriendlyByteBuf buf) {
		this(buf.readBlockPos());
	}

	public static TapParticlePacket decode(FriendlyByteBuf buf) {
		return new TapParticlePacket(buf.readBlockPos());
	}

	public static void handle(TapParticlePacket message, final PlayPayloadContext ctx) {
		ctx.workHandler().submitAsync(() -> {
			MGUClientPackets.spawnGlitterParticles(message.tilePos.getX(), message.tilePos.getY(), message.tilePos.getZ(), 0D, 0D, 0D);
		});
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(tilePos);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}
}