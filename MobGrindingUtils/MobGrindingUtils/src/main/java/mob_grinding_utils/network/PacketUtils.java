package mob_grinding_utils.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;

public class PacketUtils {

	public static BlockPos readBlockPos(ByteBuf buf) {
		return new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	public static void writeBlockPos(ByteBuf buf, BlockPos pos) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}
}