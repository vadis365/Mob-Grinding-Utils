package mob_grinding_utils;

import mob_grinding_utils.client.particles.ParticleFluidXP;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

public class ModParticles {
    public static void init(IEventBus modBus) {
        modBus.addListener(ModParticles::registerParticleFactory);
    }

    private static void registerParticleFactory(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(MobGrindingUtils.PARTICLE_FLUID_XP.get(), ParticleFluidXP.Factory::new);
    }
}
