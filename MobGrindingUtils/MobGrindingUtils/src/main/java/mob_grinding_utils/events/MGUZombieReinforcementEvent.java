package mob_grinding_utils.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.ZombieEvent.SummonAidEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MGUZombieReinforcementEvent {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void zombieEvent(SummonAidEvent event) {
		if (event.getWorld().isRemote)
			return;
		if (event.getAttacker() instanceof EntityPlayer) {
			EntityPlayer fakePlayer = (EntityPlayer) event.getAttacker();
			if (fakePlayer.getDisplayName().getFormattedText().matches(new TextComponentTranslation("fakeplayer.mob_masher").getFormattedText()))
				event.setResult(Result.DENY);
		}
	}

}
