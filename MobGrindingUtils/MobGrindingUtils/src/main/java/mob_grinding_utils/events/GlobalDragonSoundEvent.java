package mob_grinding_utils.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class GlobalDragonSoundEvent {
	@SubscribeEvent
	public void onDragonSound(PlaySoundEvent event) {
		if (event.getName().equals("entity.ender_dragon.death"))
			event.setSound(null);
	}
}
