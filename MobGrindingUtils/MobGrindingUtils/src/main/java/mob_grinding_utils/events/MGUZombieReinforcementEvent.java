package mob_grinding_utils.events;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.ZombieEvent.SummonAidEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MGUZombieReinforcementEvent {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void zombieEvent(SummonAidEvent event) {
		if (event.getLevel().isClientSide)
			return;
		if (event.getAttacker() instanceof Player) {
			Player fakePlayer = (Player) event.getAttacker();
			if (fakePlayer.getDisplayName().getString().equals(Component.translatable("fakeplayer.mob_masher").getString()))
				event.setResult(Result.DENY);
		}
	}

}
