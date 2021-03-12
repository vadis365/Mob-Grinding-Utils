package mob_grinding_utils.client.particles;

import mob_grinding_utils.MobGrindingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class ClientParticles {
	public static void spawnGlitterParticles(World worldObj, double x, double y, double z, double vecX, double vecY, double vecZ) {
		World world = Minecraft.getInstance().world;
		world.addParticle(MobGrindingUtils.PARTICLE_FLUID_XP.get(), false, x, y, z, vecX, vecY, vecZ);
	}
}
