package mob_grinding_utils.network;

import mob_grinding_utils.tile.TileEntityFan;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

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

	public static void encode(final MessageFan message, FriendlyByteBuf buf) {
		buf.writeInt(message.buttonID);
		buf.writeBlockPos(message.tilePos);
	}

	public static MessageFan decode(FriendlyByteBuf buf) {
		return new MessageFan(buf.readInt(), buf.readBlockPos());
	}

	public static void handle(MessageFan message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {

			ServerPlayer player = ctx.get().getSender();
			ServerLevel world = player.serverLevel();
			TileEntityFan fan = (TileEntityFan) world.getBlockEntity(message.tilePos);
			if (fan != null) {
				if (message.buttonID == 0)
					fan.toggleRenderBox();
				BlockState state = world.getBlockState(message.tilePos);
				world.sendBlockUpdated(message.tilePos, state, state, 3);
			}

		});
	}
}