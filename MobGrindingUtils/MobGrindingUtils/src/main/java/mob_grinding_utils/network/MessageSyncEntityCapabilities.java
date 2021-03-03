package mob_grinding_utils.network;

import java.util.function.Supplier;
import mob_grinding_utils.capability.base.EntityCapability;
import mob_grinding_utils.capability.base.EntityCapabilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageSyncEntityCapabilities {
	private ResourceLocation capability;
	private CompoundNBT nbt;
	private int entityID;

	public MessageSyncEntityCapabilities(EntityCapability<?, ?, ?> entityCapability) {
		this.capability = entityCapability.getID();
		this.entityID = entityCapability.getEntity().getEntityId();
		this.nbt = new CompoundNBT();
		entityCapability.writeTrackingDataToNBT(this.nbt);
	}

	public MessageSyncEntityCapabilities(ResourceLocation capability, int entityID, CompoundNBT nbt) {
		this.capability = capability;
		this.nbt = nbt;
		this.entityID = entityID;
	}

	private Entity getEntity(World world) {
		return world.getEntityByID(this.entityID);
	}

	public static void handle(final MessageSyncEntityCapabilities message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			World world = Minecraft.getInstance().world;
			Entity entity = world.getEntityByID(message.entityID);
			if(entity != null) {
				EntityCapability<?, ?, Entity> capability = EntityCapabilityHandler.getCapability(message.capability, entity);
				if(capability != null) {
					capability.readTrackingDataFromNBT(message.nbt);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

	public static MessageSyncEntityCapabilities decode(PacketBuffer buffer) {
		return new MessageSyncEntityCapabilities(buffer.readResourceLocation(), buffer.readInt(), buffer.readCompoundTag());
	}

	public static void encode(final MessageSyncEntityCapabilities message, PacketBuffer buffer) {
		buffer.writeResourceLocation(message.capability);
		buffer.writeInt(message.entityID);
		buffer.writeCompoundTag(message.nbt);
	}
}
