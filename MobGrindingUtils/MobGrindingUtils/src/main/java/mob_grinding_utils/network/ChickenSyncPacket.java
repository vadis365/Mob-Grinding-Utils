package mob_grinding_utils.network;

import mob_grinding_utils.Reference;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ChickenSyncPacket(int chickenID, CompoundTag nbt) implements CustomPacketPayload {
	public static final Type<ChickenSyncPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "chicken_sync"));
	public static final StreamCodec<FriendlyByteBuf, ChickenSyncPacket> STREAM_CODEC = CustomPacketPayload.codec(
			ChickenSyncPacket::write,
			ChickenSyncPacket::new
	);


	public ChickenSyncPacket(LivingEntity chicken, CompoundTag chickenNBT) {
		this(chicken.getId(), chickenNBT);
	}

	public ChickenSyncPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readNbt());
	}

	public static void handle(ChickenSyncPacket message, final IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			MGUClientPackets.HandleChickenSync(message);
		});
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(chickenID);
		buf.writeNbt(nbt);
	}

	@Override
	public int chickenID() {
		return chickenID;
	}

	@Override
	public CompoundTag nbt() {
		return nbt;
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}