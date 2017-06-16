package mob_grinding_utils.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class AbsorptionHopperMessage implements IMessage {

	public int dimension, entityID, buttonID;
	public BlockPos tilePos;

	public AbsorptionHopperMessage() {
	}

	public AbsorptionHopperMessage(EntityPlayer player, int button, BlockPos pos) {
		dimension = player.dimension;
		entityID = player.getEntityId();
		buttonID = button;
		tilePos = pos;
	}

	public AbsorptionHopperMessage(EntityPlayer player, int button, int x, int y, int z) {
		dimension = player.dimension;
		entityID = player.getEntityId();
		buttonID = button;
		tilePos = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(entityID);
		buf.writeInt(buttonID);
		PacketUtils.writeBlockPos(buf, tilePos);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		entityID = buf.readInt();
		buttonID = buf.readInt();
		tilePos = PacketUtils.readBlockPos(buf);
	}
}