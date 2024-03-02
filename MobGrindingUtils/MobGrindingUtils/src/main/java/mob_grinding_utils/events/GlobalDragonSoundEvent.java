package mob_grinding_utils.events;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;

@OnlyIn(Dist.CLIENT)
public class GlobalDragonSoundEvent {
	@SubscribeEvent
	public void onDragonSound(PlaySoundEvent event) {
		if (event.getName().equals("entity.ender_dragon.death"))
			event.setSound(null);
	}
}
