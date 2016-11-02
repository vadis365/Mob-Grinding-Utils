package mob_grinding_utils.events;

import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GlobalDragonSoundEvent {

	@SubscribeEvent
	public void onDragonSound(PlaySoundEvent event) {
		if (event.getName().equals("entity.enderdragon.death")) 
			event.setResultSound(null);
	}
}
