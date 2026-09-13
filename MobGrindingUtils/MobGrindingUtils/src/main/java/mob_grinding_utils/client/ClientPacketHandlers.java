package mob_grinding_utils.client;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.network.ChickenSyncPacket;
import mob_grinding_utils.network.FlagSyncPacket;
import mob_grinding_utils.network.TapParticlePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.level.Level;

/**
 * Client-only packet/particle helpers. Kept out of common network classes so dedicated
 * servers never attempt to load {@link Minecraft}-touching code via packet class refs.
 */
public final class ClientPacketHandlers {
	private ClientPacketHandlers() {}

	public static void handleChickenSync(ChickenSyncPacket message) {
		Level world = Minecraft.getInstance().level;
		if (world == null || !world.isClientSide())
			return;

		LivingEntity chicken = (Chicken) world.getEntity(message.chickenID());
		if (chicken == null)
			return;

		RandomSource random = world.getRandom();
		CompoundTag nbt = chicken.getPersistentData();
		nbt.putBoolean("shouldExplode", message.nbt().getBooleanOr("shouldExplode", false));
		nbt.putInt("countDown", message.nbt().getIntOr("countDown", 0));
		if (message.nbt().getIntOr("countDown", 0) >= 20) {
			for (int k = 0; k < 20; ++k) {
				double xSpeed = random.nextGaussian() * 0.02D;
				double ySpeed = random.nextGaussian() * 0.02D;
				double zSpeed = random.nextGaussian() * 0.02D;
				world.addParticle(ParticleTypes.EXPLOSION, chicken.getX() + (double) (random.nextFloat() * chicken.getBbWidth() * 2.0F) - (double) chicken.getBbWidth(), chicken.getY() + (double) (random.nextFloat() * chicken.getBbHeight()), chicken.getZ() + (double) (random.nextFloat() * chicken.getBbWidth() * 2.0F) - (double) chicken.getBbWidth(), xSpeed, ySpeed, zSpeed);
				world.addParticle(ParticleTypes.LAVA, chicken.getX() + (double) (random.nextFloat() * chicken.getBbWidth() * 2.0F) - (double) chicken.getBbWidth(), chicken.getY() + (double) (random.nextFloat() * chicken.getBbHeight()), chicken.getZ() + (double) (random.nextFloat() * chicken.getBbWidth() * 2.0F) - (double) chicken.getBbWidth(), xSpeed, ySpeed, zSpeed);
			}
		}
	}

	public static void handleTapParticle(TapParticlePacket message) {
		Level world = Minecraft.getInstance().level;
		if (world == null)
			return;
		world.addParticle(MobGrindingUtils.PARTICLE_FLUID_XP.get(), false, false,
				message.tilePos().getX(), message.tilePos().getY(), message.tilePos().getZ(), 0D, 0D, 0D);
	}

	public static void handleFlagSync(FlagSyncPacket packet) {
		var player = Minecraft.getInstance().player;
		if (player == null)
			return;
		CompoundTag nbt = player.getPersistentData();
		nbt.putBoolean("MGU_WitherMuffle", packet.wither());
		nbt.putBoolean("MGU_DragonMuffle", packet.dragon());
	}
}
