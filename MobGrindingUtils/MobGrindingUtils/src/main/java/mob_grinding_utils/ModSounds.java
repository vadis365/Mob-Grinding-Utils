package mob_grinding_utils;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Reference.MOD_ID);
	public static final RegistryObject<SoundEvent> TAP_SQUEAK = registerSound("tap_squeak");
	public static final RegistryObject<SoundEvent> ENTITY_WITHER_SPAWN_LOCAL = registerSound("entity_wither_spawn_local");
	public static final RegistryObject<SoundEvent> ENTITY_WITHER_DEATH_LOCAL = registerSound("entity_wither_death_local");
	public static final RegistryObject<SoundEvent> ENTITY_DRAGON_DEATH_LOCAL = registerSound("entity_dragon_death_local");
	public static final RegistryObject<SoundEvent> CHICKEN_RISE = registerSound("chicken_rise");
	public static final RegistryObject<SoundEvent> SPOOPY_CHANGE = registerSound("spoopy_change");
	public static final RegistryObject<SoundEvent> SOLID_XP_BLOCK_BOING = registerSound("solid_xp_block_boing");

	public static final SoundType SOLID_XP_BLOCK = new SoundType(1.0F, 1.0F, SoundEvents.PLAYER_LEVELUP, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundEvents.EXPERIENCE_ORB_PICKUP);

	private static RegistryObject<SoundEvent> registerSound(String name) {
		return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(Reference.MOD_ID, name)));
	}

	public static void init(IEventBus bus) {
		SOUNDS.register(bus);
	}
}
