package mob_grinding_utils;


import mob_grinding_utils.util.RL;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Reference.MOD_ID);
	public static final DeferredHolder<SoundEvent, SoundEvent> TAP_SQUEAK = registerSound("tap_squeak");
	public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WITHER_SPAWN_LOCAL = registerSound("entity_wither_spawn_local");
	public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WITHER_DEATH_LOCAL = registerSound("entity_wither_death_local");
	public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DRAGON_DEATH_LOCAL = registerSound("entity_dragon_death_local");
	public static final DeferredHolder<SoundEvent, SoundEvent> CHICKEN_RISE = registerSound("chicken_rise");
	public static final DeferredHolder<SoundEvent, SoundEvent> SPOOPY_CHANGE = registerSound("spoopy_change");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOLID_XP_BLOCK_BOING = registerSound("solid_xp_block_boing");

	public static final SoundType SOLID_XP_BLOCK = new SoundType(1.0F, 1.0F, SoundEvents.PLAYER_LEVELUP, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundEvents.EXPERIENCE_ORB_PICKUP);

	private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
		return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(RL.mgu(name)));
	}

	public static void init(IEventBus bus) {
		SOUNDS.register(bus);
	}
}
