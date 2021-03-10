package mob_grinding_utils.network;

import java.util.function.Supplier;

import mob_grinding_utils.tile.TileEntityFan;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageFan {
	public int buttonID;
	public BlockPos tilePos;

	public MessageFan(int button, BlockPos pos) {
		buttonID = button;
		tilePos = pos;
	}

	public MessageFan(int button, int x, int y, int z) {
		buttonID = button;
		tilePos = new BlockPos(x, y, z);
	}

	public static void encode(final MessageFan message, PacketBuffer buf) {
		buf.writeInt(message.buttonID);
		buf.writeBlockPos(message.tilePos);
	}

	public static MessageFan decode(PacketBuffer buf) {
		return new MessageFan(buf.readInt(), buf.readBlockPos());
	}

	public static void handle(MessageFan message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {

			ServerPlayerEntity player = ctx.get().getSender();
			ServerWorld world = player.getServerWorld();
			TileEntityFan fan = (TileEntityFan) world.getTileEntity(message.tilePos);
			if (fan != null) {
				if (message.buttonID == 0)
					fan.toggleRenderBox();
				BlockState state = world.getBlockState(message.tilePos);
				world.notifyBlockUpdate(message.tilePos, state, state, 3);
			}

		});
	}
}