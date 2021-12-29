package mob_grinding_utils.network;

import java.util.function.Supplier;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageChickenSync {

	public int chickenID;
	public CompoundTag nbt;

	public MessageChickenSync(LivingEntity chicken, CompoundTag chickenNBT) {
		chickenID = chicken.getId();
		nbt = chickenNBT;
	}

	public MessageChickenSync(int chickenID, CompoundTag nbt) {
		this.chickenID = chickenID;
		this.nbt = nbt;
	}

	public static void encode(final MessageChickenSync message, FriendlyByteBuf buf) {
		buf.writeInt(message.chickenID);
		buf.writeNbt(message.nbt);
	}

	public static MessageChickenSync decode(final FriendlyByteBuf buf) {
		return new MessageChickenSync(buf.readInt(), buf.readNbt());
	}

	public static void handle(MessageChickenSync message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			MobGrindingUtils.NETPROXY.HandleChickenSync(message);
		});
		ctx.get().setPacketHandled(true);
	}
}