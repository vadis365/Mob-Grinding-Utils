package mob_grinding_utils.network;

import java.util.function.Supplier;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageChickenSync {

	public int chickenID;
	public CompoundNBT nbt;

	public MessageChickenSync(LivingEntity chicken, CompoundNBT chickenNBT) {
		chickenID = chicken.getEntityId();
		nbt = chickenNBT;
	}

	public MessageChickenSync(int chickenID, CompoundNBT nbt) {
		this.chickenID = chickenID;
		this.nbt = nbt;
	}

	public static void encode(final MessageChickenSync message, PacketBuffer buf) {
		buf.writeInt(message.chickenID);
		buf.writeCompoundTag(message.nbt);
	}

	public static MessageChickenSync decode(final PacketBuffer buf) {
		return new MessageChickenSync(buf.readInt(), buf.readCompoundTag());
	}

	public static void handle(MessageChickenSync message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			MobGrindingUtils.NETPROXY.HandleChickenSync(message);
		});
		ctx.get().setPacketHandled(true);
	}
}