package mob_grinding_utils.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TapParticleMessage implements IMessage {

	public BlockPos tilePos;

	public TapParticleMessage() {
	}

	public TapParticleMessage(BlockPos pos) {
		tilePos = pos;
	}

	public TapParticleMessage(int x, int y, int z) {
		tilePos = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketUtils.writeBlockPos(buf, tilePos);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tilePos = PacketUtils.readBlockPos(buf);
	}
}