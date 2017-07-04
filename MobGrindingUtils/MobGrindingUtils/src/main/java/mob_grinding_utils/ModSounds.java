package mob_grinding_utils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModSounds {
	public static SoundEvent TAP_SQUEAK, ENTITY_WITHER_SPAWN_LOCAL, ENTITY_WITHER_DEATH_LOCAL, ENTITY_DRAGON_DEATH_LOCAL, CHICKEN_RISE;

	public static void init() {
		TAP_SQUEAK = new SoundEvent(new ResourceLocation("mob_grinding_utils", "tap_squeak")).setRegistryName("mob_grinding_utils", "tap_squeak");
		ENTITY_WITHER_SPAWN_LOCAL = new SoundEvent(new ResourceLocation("mob_grinding_utils", "entity_wither_spawn_local")).setRegistryName("mob_grinding_utils", "entity_wither_spawn_local");
		ENTITY_WITHER_DEATH_LOCAL = new SoundEvent(new ResourceLocation("mob_grinding_utils", "entity_wither_death_local")).setRegistryName("mob_grinding_utils", "entity_wither_death_local");
		ENTITY_DRAGON_DEATH_LOCAL = new SoundEvent(new ResourceLocation("mob_grinding_utils", "entity_dragon_death_local")).setRegistryName("mob_grinding_utils", "entity_dragon_death_local");
		CHICKEN_RISE = new SoundEvent(new ResourceLocation("mob_grinding_utils", "chicken_rise")).setRegistryName("mob_grinding_utils", "chicken_rise");
	}

	@Mod.EventBusSubscriber(modid = "mob_grinding_utils")
	public static class RegistrationHandlerSounds {
		@SubscribeEvent
		public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
			event.getRegistry().registerAll(
					TAP_SQUEAK,
					ENTITY_WITHER_SPAWN_LOCAL,
					ENTITY_WITHER_DEATH_LOCAL,
					ENTITY_DRAGON_DEATH_LOCAL,
					CHICKEN_RISE
					);
		}
	}
}
