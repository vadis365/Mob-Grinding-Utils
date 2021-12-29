package mob_grinding_utils.network;

import java.util.function.Supplier;

import mob_grinding_utils.tile.TileEntityMGUSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageEntitySpawner {
	public ResourceKey<Level> dimension;
	public int entityID, buttonID;
	public BlockPos tilePos;

	public MessageEntitySpawner(Player player, int button, BlockPos pos) {
		dimension = player.getCommandSenderWorld().dimension();
		entityID = player.getId();
		buttonID = button;
		tilePos = pos;
	}

	public MessageEntitySpawner(Player player, int button, int x, int y, int z) {
		dimension = player.getCommandSenderWorld().dimension();
		entityID = player.getId();
		buttonID = button;
		tilePos = new BlockPos(x, y, z);
	}

	public MessageEntitySpawner(ResourceLocation dimensionKey, int entityID, int buttonID, BlockPos tilePos) {
		this.dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, dimensionKey);
		this.entityID = entityID;
		this.buttonID = buttonID;
		this.tilePos = tilePos;
	}

	public static void encode(final MessageEntitySpawner message, FriendlyByteBuf buf) {
		buf.writeResourceLocation(message.dimension.location());
		buf.writeInt(message.entityID);
		buf.writeInt(message.buttonID);
		buf.writeBlockPos(message.tilePos);
	}

	public static MessageEntitySpawner decode(final FriendlyByteBuf buf) {
		return new MessageEntitySpawner(buf.readResourceLocation(), buf.readInt(), buf.readInt(), buf.readBlockPos());
	}

	public static void handle(final MessageEntitySpawner message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {

			ServerPlayer player = ctx.get().getSender();
			ServerLevel world = player.getServer().getLevel(message.dimension);
			if (world != null && !world.isClientSide)
				if (player.getId() == message.entityID) {
					TileEntityMGUSpawner spawner = (TileEntityMGUSpawner) world.getBlockEntity(message.tilePos);
					if (spawner != null) {
						if (message.buttonID == 0)
							spawner.toggleRenderBox();

						if (message.buttonID > 0 && message.buttonID <= 6)
							spawner.toggleOffset(message.buttonID);

						BlockState state = world.getBlockState(message.tilePos);
						world.sendBlockUpdated(message.tilePos, state, state, 3);
					}
				}
		});
		ctx.get().setPacketHandled(true);
	}
}