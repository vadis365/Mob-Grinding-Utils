package mob_grinding_utils.events;

import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GlobalWitherSoundEvent {

	@SubscribeEvent
	public void onWitherSound(PlaySoundEvent event) {
		if (event.getName().equals("entity.wither.spawn") || event.getName().equals("entity.wither.death")) 
			event.setResultSound(null);
	}
}
