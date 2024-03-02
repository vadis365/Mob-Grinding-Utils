package mob_grinding_utils.network;

import mob_grinding_utils.Reference;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record ChickenSyncPacket(int chickenID, CompoundTag nbt) implements CustomPacketPayload {
	public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "chicken_sync");
	public ChickenSyncPacket(LivingEntity chicken, CompoundTag chickenNBT) {
		this(chicken.getId(), chickenNBT);
	}

	public ChickenSyncPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readNbt());
	}

	public static void handle(ChickenSyncPacket message, final PlayPayloadContext ctx) {
		ctx.workHandler().submitAsync(() -> {
			MGUClientPackets.HandleChickenSync(message);
		});
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(chickenID);
		buf.writeNbt(nbt);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public int chickenID() {
		return chickenID;
	}

	@Override
	public CompoundTag nbt() {
		return nbt;
	}
}