package mob_grinding_utils.network;


import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageTapParticle {

	public BlockPos tilePos;

	public MessageTapParticle(BlockPos pos) {
		tilePos = pos;
	}

	public MessageTapParticle(int x, int y, int z) {
		tilePos = new BlockPos(x, y, z);
	}

	public static void encode(final MessageTapParticle message, PacketBuffer buf) {
		buf.writeBlockPos(message.tilePos);
	}

	public static MessageTapParticle decode(PacketBuffer buf) {
		return new MessageTapParticle(buf.readBlockPos());
	}

	public static void handle(MessageTapParticle message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			World world = ctx.get().getSender().world;

			if (world == null)
				return;

			if (world.isRemote) {
//TODO				world.addParticle(MobGrindingUtils.PARTICLE_FLUIDXP.get(),message.tilePos.getX() + world.rand.nextDouble() - 0.5 * 0.05, message.tilePos.getY() + 0.125D, message.tilePos.getZ() + world.rand.nextDouble() - 0.5 * 0.05, 0D, 0D, 0D);
			}
		});
		ctx.get().setPacketHandled(true);
	}
}