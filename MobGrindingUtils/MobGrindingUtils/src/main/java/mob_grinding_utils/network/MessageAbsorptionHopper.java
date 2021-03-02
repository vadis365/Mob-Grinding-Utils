package mob_grinding_utils.network;

import io.netty.buffer.ByteBuf;
import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageAbsorptionHopper {
	public RegistryKey<World> dimension;
	public int entityID, buttonID;
	public BlockPos tilePos;

	public MessageAbsorptionHopper(PlayerEntity player, int button, BlockPos pos) {
		dimension = player.getEntityWorld().getDimensionKey();
		entityID = player.getEntityId();
		buttonID = button;
		tilePos = pos;
	}

	public MessageAbsorptionHopper(PlayerEntity player, int button, int x, int y, int z) {
		dimension = player.getEntityWorld().getDimensionKey();
		entityID = player.getEntityId();
		buttonID = button;
		tilePos = new BlockPos(x, y, z);
	}

	public MessageAbsorptionHopper(String dimensionKey, int entityID, int buttonID, BlockPos tilePos) {
		this.dimension = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(dimensionKey));
		this.entityID = entityID;
		this.buttonID = buttonID;
		this.tilePos = tilePos;
	}

	public static void encode(final MessageAbsorptionHopper message, PacketBuffer buf) {
		buf.writeString(message.dimension.getLocation().toString());
		buf.writeInt(message.entityID);
		buf.writeInt(message.buttonID);
		buf.writeBlockPos(message.tilePos);
	}


	public static MessageAbsorptionHopper decode(final PacketBuffer buf) {
		return new MessageAbsorptionHopper(buf.readString(), buf.readInt(), buf.readInt(), buf.readBlockPos());
	}

	public static void handle(final MessageAbsorptionHopper message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {

			ServerPlayerEntity player = ctx.get().getSender();
			ServerWorld world = player.getServer().getWorld(message.dimension);
			if (world != null && !world.isRemote)
				if (player.getEntityId() == message.entityID) {
					TileEntityAbsorptionHopper hopper = (TileEntityAbsorptionHopper) world.getTileEntity(message.tilePos);
					if (hopper != null) {
						if (message.buttonID < 6)
							hopper.toggleMode(Direction.byIndex(message.buttonID));

						if (message.buttonID == 6)
							hopper.toggleRenderBox();

						if (message.buttonID > 6 && message.buttonID <= 12)
							hopper.toggleOffset(message.buttonID);

						BlockState state = world.getBlockState(message.tilePos);
						world.notifyBlockUpdate(message.tilePos, state, state, 3);
					}
				}
		});
		ctx.get().setPacketHandled(true);
	}
}