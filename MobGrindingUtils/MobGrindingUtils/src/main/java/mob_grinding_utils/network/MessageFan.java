package mob_grinding_utils.network;

import io.netty.buffer.ByteBuf;
import mob_grinding_utils.tile.TileEntityFan;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageFan implements IMessage, IMessageHandler<MessageFan, MessageFan> {

	public int dimension, entityID, buttonID;
	public BlockPos tilePos;

	public MessageFan() {
	}

	public MessageFan(EntityPlayer player, int button, BlockPos pos) {
		dimension = player.dimension;
		entityID = player.getEntityId();
		buttonID = button;
		tilePos = pos;
	}

	public MessageFan(EntityPlayer player, int button, int x, int y, int z) {
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

	@Override
	public MessageFan onMessage(MessageFan message, MessageContext ctx) {
		final World world = DimensionManager.getWorld(message.dimension);
		if (world == null)
			return null;
		else if (!world.isRemote)
			if (ctx.getServerHandler().player.getEntityId() == message.entityID) {
				final EntityPlayerMP player = ctx.getServerHandler().player;
				player.getServer().addScheduledTask(new Runnable() {
					public void run() {
						TileEntityFan fan = (TileEntityFan) world.getTileEntity(message.tilePos);
						if (fan != null) {
							if (message.buttonID == 0)
								fan.toggleRenderBox();
							IBlockState state = world.getBlockState(message.tilePos);
							world.notifyBlockUpdate(message.tilePos, state, state, 3);
						}
					}
				});
			}
		return null;
	}
}