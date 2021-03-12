package mob_grinding_utils.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.ZombieEvent.SummonAidEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MGUZombieReinforcementEvent {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void zombieEvent(SummonAidEvent event) {
		if (event.getWorld().isRemote)
			return;
		if (event.getAttacker() instanceof PlayerEntity) {
			PlayerEntity fakePlayer = (PlayerEntity) event.getAttacker();
			if (fakePlayer.getDisplayName().getString().equals(new TranslationTextComponent("fakeplayer.mob_masher").getString()))
				event.setResult(Result.DENY);
		}
	}

}
