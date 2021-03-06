package mob_grinding_utils.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class GlobalWitherSoundEvent {
	@SubscribeEvent
	public void onWitherSound(PlaySoundEvent event) {
		if (event.getName().equals("entity.wither.spawn") || event.getName().equals("entity.wither.death"))
			event.setResultSound(null);
	}
}
