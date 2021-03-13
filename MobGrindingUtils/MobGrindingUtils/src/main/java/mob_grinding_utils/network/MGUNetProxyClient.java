package mob_grinding_utils.network;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;

public class MGUNetProxyClient extends MGUNetProxyCommon {
    public void HandleChickenSync(MessageChickenSync message) {
        World world = Minecraft.getInstance().world;

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
    }

    public void spawnGlitterParticles( double x, double y, double z, double vecX, double vecY, double vecZ) {
        World world = Minecraft.getInstance().world;
        world.addParticle(MobGrindingUtils.PARTICLE_FLUID_XP.get(), false, x, y, z, vecX, vecY, vecZ);
    }
}
