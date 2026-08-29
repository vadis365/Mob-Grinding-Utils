package mob_grinding_utils.network;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.level.Level;

public class MGUClientPackets {
    public static void HandleChickenSync(ChickenSyncPacket message) {
        Level world = Minecraft.getInstance().level;

        if (world == null)
            return;

        else if (world.isClientSide()) {
            LivingEntity chicken = (Chicken) world.getEntity(message.chickenID());
            if (chicken != null) {
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
            } else {
                // LogManager.getLogger().info("WHY THE CLUCK IS THE CHICKEN NULL!!!!?");
            }
        }
    }

    public static void spawnGlitterParticles( double x, double y, double z, double vecX, double vecY, double vecZ) {
        Level world = Minecraft.getInstance().level;
        world.addParticle(MobGrindingUtils.PARTICLE_FLUID_XP.get(), false, x, y, z, vecX, vecY, vecZ);
    }

    public static void handleFlagSyncPacket(FlagSyncPacket packet) {
        CompoundTag nbt = Minecraft.getInstance().player.getPersistentData();
        nbt.putBoolean("MGU_WitherMuffle", packet.wither());
        nbt.putBoolean("MGU_DragonMuffle", packet.dragon());
    }
}
