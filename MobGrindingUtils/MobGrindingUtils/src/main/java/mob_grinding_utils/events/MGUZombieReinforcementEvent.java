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
		if (event.getLevel().isClientSide()) {
            return;
        }
        if (event.getSpawnType() == MobSpawnType.REINFORCEMENT && event.getSpawner() != null && event.getSpawner().right().isPresent()) {
            Entity spawnerEntity = event.getSpawner().right().get();
            if (spawnerEntity instanceof Zombie zombie) {
                Entity lastAttacker = zombie.getLastAttacker();
                if (lastAttacker instanceof FakePlayer fakePlayer && FakePlayerHandler.isMGUFakePlayer(fakePlayer)) {
                    event.setSpawnCancelled(true);
                }
            }
        }
	} // TODO clean this up!
}
