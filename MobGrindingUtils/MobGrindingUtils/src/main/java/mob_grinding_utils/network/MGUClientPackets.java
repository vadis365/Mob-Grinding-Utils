package mob_grinding_utils.network;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;

public class MGUClientPackets {
    public static void HandleChickenSync(ChickenSyncPacket message) {
        Level world = Minecraft.getInstance().level;

        if (world == null)
            return;

        else if (world.isClientSide) {
            LivingEntity chicken = (Chicken) world.getEntity(message.chickenID());
            if (chicken != null) {
                CompoundTag nbt = new CompoundTag();
                nbt = chicken.getPersistentData();
                nbt.putBoolean("shouldExplode", message.nbt().getBoolean("shouldExplode"));
                nbt.putInt("countDown", message.nbt().getInt("countDown"));
                if (message.nbt().getInt("countDown") >= 20) {
                    for (int k = 0; k < 20; ++k) {
                        double xSpeed = world.random.nextGaussian() * 0.02D;
                        double ySpeed = world.random.nextGaussian() * 0.02D;
                        double zSpeed = world.random.nextGaussian() * 0.02D;
                        world.addParticle(ParticleTypes.EXPLOSION, chicken.getX() + (double) (world.random.nextFloat() * chicken.getBbWidth() * 2.0F) - (double) chicken.getBbWidth(), chicken.getY() + (double) (world.random.nextFloat() * chicken.getBbHeight()), chicken.getZ() + (double) (world.random.nextFloat() * chicken.getBbWidth() * 2.0F) - (double) chicken.getBbWidth(), xSpeed, ySpeed, zSpeed);
                        world.addParticle(ParticleTypes.LAVA, chicken.getX() + (double) (world.random.nextFloat() * chicken.getBbWidth() * 2.0F) - (double) chicken.getBbWidth(), chicken.getY() + (double) (world.random.nextFloat() * chicken.getBbHeight()), chicken.getZ() + (double) (world.random.nextFloat() * chicken.getBbWidth() * 2.0F) - (double) chicken.getBbWidth(), xSpeed, ySpeed, zSpeed);
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
