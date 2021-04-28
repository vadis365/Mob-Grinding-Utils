package mob_grinding_utils.network;

import java.util.function.Supplier;

import mob_grinding_utils.tile.TileEntityMGUSpawner;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageEntitySpawner {
	public RegistryKey<World> dimension;
	public int entityID, buttonID;
	public BlockPos tilePos;

	public MessageEntitySpawner(PlayerEntity player, int button, BlockPos pos) {
		dimension = player.getEntityWorld().getDimensionKey();
		entityID = player.getEntityId();
		buttonID = button;
		tilePos = pos;
	}

	public MessageEntitySpawner(PlayerEntity player, int button, int x, int y, int z) {
		dimension = player.getEntityWorld().getDimensionKey();
		entityID = player.getEntityId();
		buttonID = button;
		tilePos = new BlockPos(x, y, z);
	}

	public MessageEntitySpawner(ResourceLocation dimensionKey, int entityID, int buttonID, BlockPos tilePos) {
		this.dimension = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, dimensionKey);
		this.entityID = entityID;
		this.buttonID = buttonID;
		this.tilePos = tilePos;
	}

	public static void encode(final MessageEntitySpawner message, PacketBuffer buf) {
		buf.writeResourceLocation(message.dimension.getLocation());
		buf.writeInt(message.entityID);
		buf.writeInt(message.buttonID);
		buf.writeBlockPos(message.tilePos);
	}

	public static MessageEntitySpawner decode(final PacketBuffer buf) {
		return new MessageEntitySpawner(buf.readResourceLocation(), buf.readInt(), buf.readInt(), buf.readBlockPos());
	}

	public static void handle(final MessageEntitySpawner message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {

			ServerPlayerEntity player = ctx.get().getSender();
			ServerWorld world = player.getServer().getWorld(message.dimension);
			if (world != null && !world.isRemote)
				if (player.getEntityId() == message.entityID) {
					TileEntityMGUSpawner spawner = (TileEntityMGUSpawner) world.getTileEntity(message.tilePos);
					if (spawner != null) {
						if (message.buttonID == 0)
							spawner.toggleRenderBox();

						if (message.buttonID > 0 && message.buttonID <= 6)
							spawner.toggleOffset(message.buttonID);

						BlockState state = world.getBlockState(message.tilePos);
						world.notifyBlockUpdate(message.tilePos, state, state, 3);
					}
				}
		});
		ctx.get().setPacketHandled(true);
	}
}