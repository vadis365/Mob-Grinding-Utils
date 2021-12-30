package mob_grinding_utils.network;


import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageTapParticle {

	public BlockPos tilePos;

	public MessageTapParticle(BlockPos pos) {
		tilePos = pos;
	}

	public MessageTapParticle(int x, int y, int z) {
		tilePos = new BlockPos(x, y, z);
	}

	public static void encode(final MessageTapParticle message, FriendlyByteBuf buf) {
		buf.writeBlockPos(message.tilePos);
	}

	public static MessageTapParticle decode(FriendlyByteBuf buf) {
		return new MessageTapParticle(buf.readBlockPos());
	}

	public static void handle(MessageTapParticle message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			MGUClientPackets.spawnGlitterParticles(message.tilePos.getX(), message.tilePos.getY(), message.tilePos.getZ(), 0D, 0D, 0D);
		});
		ctx.get().setPacketHandled(true);
	}
}