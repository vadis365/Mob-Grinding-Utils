package mob_grinding_utils;

import mob_grinding_utils.client.particles.ParticleFluidXP;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus= EventBusSubscriber.Bus.MOD)
public class ModParticles {
    @SuppressWarnings("resource")
	@SubscribeEvent
	public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(MobGrindingUtils.PARTICLE_FLUID_XP.get(), ParticleFluidXP.Factory::new);
	}
}
