package mob_grinding_utils.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;

import java.util.function.Supplier;

public class MessageChickenSync {

	public int chickenID;
	public CompoundNBT nbt;

	public MessageChickenSync(LivingEntity chicken, CompoundNBT chickenNBT) {
		chickenID = chicken.getEntityId();
		nbt = chickenNBT;
	}

	public MessageChickenSync(int chickenID, CompoundNBT nbt) {
		this.chickenID = chickenID;
		this.nbt = nbt;
	}

	public static void encode(final MessageChickenSync message, PacketBuffer buf) {
		buf.writeInt(message.chickenID);
		buf.writeCompoundTag(message.nbt);
	}

	public static MessageChickenSync decode(final PacketBuffer buf) {
		return new MessageChickenSync(buf.readInt(), buf.readCompoundTag());
	}

	public static void handle(MessageChickenSync message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {

			World world = ctx.get().getSender().world;

			if (world == null)
				return;

			else if (world.isRemote) {
				LivingEntity chicken = (ChickenEntity) world.getEntityByID(message.chickenID);
				if (chicken != null) {
					CompoundNBT nbt = new CompoundNBT();
					nbt = chicken.getPersistentData();
					nbt.putBoolean("shouldExplode", message.nbt.getBoolean("shouldExplode"));
					nbt.putInt("countDown", message.nbt.getInt("countDown"));
					if (message.nbt.getInt("countDown") >= 20) {
						for (int k = 0; k < 20; ++k) {
							double xSpeed = world.rand.nextGaussian() * 0.02D;
							double ySpeed = world.rand.nextGaussian() * 0.02D;
							double zSpeed = world.rand.nextGaussian() * 0.02D;
							world.addParticle(ParticleTypes.EXPLOSION, chicken.getPosX() + (double) (world.rand.nextFloat() * chicken.getWidth() * 2.0F) - (double) chicken.getWidth(), chicken.getPosY() + (double) (world.rand.nextFloat() * chicken.getHeight()), chicken.getPosZ() + (double) (world.rand.nextFloat() * chicken.getWidth() * 2.0F) - (double) chicken.getWidth(), xSpeed, ySpeed, zSpeed);
							world.addParticle(ParticleTypes.LAVA, chicken.getPosX() + (double) (world.rand.nextFloat() * chicken.getWidth() * 2.0F) - (double) chicken.getWidth(), chicken.getPosY() + (double) (world.rand.nextFloat() * chicken.getHeight()), chicken.getPosZ() + (double) (world.rand.nextFloat() * chicken.getWidth() * 2.0F) - (double) chicken.getWidth(), xSpeed, ySpeed, zSpeed);
						}
					}
				} else {
					LogManager.getLogger().info("WHY THE FUCK IS THE CHICKEN NULL!!!!?");
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}
}