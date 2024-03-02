package mob_grinding_utils.events;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.ZombieEvent;

public class MGUZombieReinforcementEvent {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void zombieEvent(ZombieEvent.SummonAidEvent event) {
		if (event.getLevel().isClientSide)
			return;
		if (event.getAttacker() instanceof Player) {
			Player fakePlayer = (Player) event.getAttacker();
			if (fakePlayer.getDisplayName().getString().equals(Component.translatable("fakeplayer.mob_masher").getString()))
				event.setResult(Event.Result.DENY);
		}
	}

}
