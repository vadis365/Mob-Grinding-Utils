package mob_grinding_utils.events;

import mob_grinding_utils.util.FakePlayerHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Zombie;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;

public class MGUZombieReinforcementEvent {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void zombieEvent(FinalizeSpawnEvent event) {
		if (event.getLevel().isClientSide())
			return;
		if (event.getSpawnType() == MobSpawnType.REINFORCEMENT && event.getSpawner().right().isPresent()) {
			Entity entity = event.getSpawner().right().get();
			if (entity instanceof Zombie zombie && zombie.getLastAttacker() instanceof FakePlayer && FakePlayerHandler.isMGUFakePlayer((FakePlayer) zombie.getLastAttacker()))
				event.setSpawnCancelled(true);
		}
	} // TODO clean this up!

}
