package mob_grinding_utils.network;

import mob_grinding_utils.tile.TileEntityAbsorptionHopper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageAbsorptionHopper {
	public ResourceKey<Level> dimension;
	public int entityID, buttonID;
	public BlockPos tilePos;

	public MessageAbsorptionHopper(Player player, int button, BlockPos pos) {
		dimension = player.getCommandSenderWorld().dimension();
		entityID = player.getId();
		buttonID = button;
		tilePos = pos;
	}

	public MessageAbsorptionHopper(Player player, int button, int x, int y, int z) {
		dimension = player.getCommandSenderWorld().dimension();
		entityID = player.getId();
		buttonID = button;
		tilePos = new BlockPos(x, y, z);
	}

	public MessageAbsorptionHopper(ResourceLocation dimensionKey, int entityID, int buttonID, BlockPos tilePos) {
		this.dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, dimensionKey);
		this.entityID = entityID;
		this.buttonID = buttonID;
		this.tilePos = tilePos;
	}

	public static void encode(final MessageAbsorptionHopper message, FriendlyByteBuf buf) {
		buf.writeResourceLocation(message.dimension.location());
		buf.writeInt(message.entityID);
		buf.writeInt(message.buttonID);
		buf.writeBlockPos(message.tilePos);
	}

	public static MessageAbsorptionHopper decode(final FriendlyByteBuf buf) {
		return new MessageAbsorptionHopper(buf.readResourceLocation(), buf.readInt(), buf.readInt(), buf.readBlockPos());
	}

	public static void handle(final MessageAbsorptionHopper message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {

			ServerPlayer player = ctx.get().getSender();
			ServerLevel world = player.getServer().getLevel(message.dimension);
			if (world != null && !world.isClientSide)
				if (player.getId() == message.entityID) {
					TileEntityAbsorptionHopper hopper = (TileEntityAbsorptionHopper) world.getBlockEntity(message.tilePos);
					if (hopper != null) {
						if (message.buttonID < 6)
							hopper.toggleMode(Direction.from3DDataValue(message.buttonID));

						if (message.buttonID == 6)
							hopper.toggleRenderBox();

						if (message.buttonID > 6 && message.buttonID <= 12)
							hopper.toggleOffset(message.buttonID);

						BlockState state = world.getBlockState(message.tilePos);
						world.sendBlockUpdated(message.tilePos, state, state, 3);
					}
				}
		});
		ctx.get().setPacketHandled(true);
	}
}