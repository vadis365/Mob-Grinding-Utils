package mob_grinding_utils.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.ZombieEvent.SummonAidEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MGUZombieReinforcementEvent {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void zombieEvent(SummonAidEvent event) {
		if (event.getWorld().isRemote)
			return;
		if (event.getAttacker() instanceof EntityPlayer) {
			EntityPlayer fakePlayer = (EntityPlayer) event.getAttacker();
			if (fakePlayer.getDisplayNameString().matches(new TextComponentTranslation("fakeplayer.mob_masher").getFormattedText()))
				event.setResult(Result.DENY);
		}
	}

}
