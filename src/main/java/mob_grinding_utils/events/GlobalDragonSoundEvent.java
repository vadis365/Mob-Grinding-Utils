package mob_grinding_utils.events;

import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GlobalDragonSoundEvent {
	@SubscribeEvent
	public void onDragonSound(PlaySoundEvent event) {
		if (event.getName().equals("entity.enderdragon.death"))
			event.setResultSound(null);
	}
}
