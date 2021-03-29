package mob_grinding_utils.network;

import mob_grinding_utils.tile.TileEntityXPSolidifier;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

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

    public static void encode(final MessageSolidifier message, PacketBuffer buf) {
        buf.writeInt(message.buttonID);
        buf.writeBlockPos(message.tilePos);
    }

    public static MessageSolidifier decode(PacketBuffer buf) {
        return new MessageSolidifier(buf.readInt(), buf.readBlockPos());
    }

    public static void handle(MessageSolidifier message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayerEntity player = ctx.get().getSender();
            ServerWorld world = player.getServerWorld();
            TileEntityXPSolidifier solidifier = (TileEntityXPSolidifier) world.getTileEntity(message.tilePos);
            if (solidifier != null) {
                if (message.buttonID == 0)
                    solidifier.toggleOutput();
                if (message.buttonID == 1)
                    solidifier.toggleOnOff();
                BlockState state = world.getBlockState(message.tilePos);
                world.notifyBlockUpdate(message.tilePos, state, state, 3);
            }

        });
    }
}
