package mob_grinding_utils.network;


import java.util.function.Supplier;

import mob_grinding_utils.client.particles.ClientParticles;
import net.minecraft.client.Minecraft;
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
			World world = Minecraft.getInstance().world;

			if (world == null)
				return;

			if (world.isRemote) {
				ClientParticles.spawnGlitterParticles(world, message.tilePos.getX(), message.tilePos.getY(), message.tilePos.getZ(), 0D, 0D, 0D);
			}
		});
		ctx.get().setPacketHandled(true);
	}
}