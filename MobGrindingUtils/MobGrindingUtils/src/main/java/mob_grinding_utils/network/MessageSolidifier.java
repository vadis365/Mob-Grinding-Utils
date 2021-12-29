package mob_grinding_utils.network;

import java.util.function.Supplier;

import mob_grinding_utils.tile.TileEntityXPSolidifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageSolidifier {
    public int buttonID;
    public BlockPos tilePos;

    public MessageSolidifier(int button, BlockPos pos) {
        buttonID = button;
        tilePos = pos;
    }

    public MessageSolidifier(int button, int x, int y, int z) {
        buttonID = button;
        tilePos = new BlockPos(x, y, z);
    }

    public static void encode(final MessageSolidifier message, FriendlyByteBuf buf) {
        buf.writeInt(message.buttonID);
        buf.writeBlockPos(message.tilePos);
    }

    public static MessageSolidifier decode(FriendlyByteBuf buf) {
        return new MessageSolidifier(buf.readInt(), buf.readBlockPos());
    }

    public static void handle(MessageSolidifier message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();
            ServerLevel world = player.getLevel();
            TileEntityXPSolidifier solidifier = (TileEntityXPSolidifier) world.getBlockEntity(message.tilePos);
            if (solidifier != null) {
                if (message.buttonID == 0)
                    solidifier.toggleOutput();
                if (message.buttonID == 1)
                    solidifier.toggleOnOff();
                BlockState state = world.getBlockState(message.tilePos);
                world.sendBlockUpdated(message.tilePos, state, state, 3);
            }

        });
    }
}
