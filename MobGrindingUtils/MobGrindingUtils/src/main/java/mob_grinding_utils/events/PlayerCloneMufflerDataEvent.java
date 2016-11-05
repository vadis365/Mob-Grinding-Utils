package mob_grinding_utils.events;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.network.DragonBarMessage;
import mob_grinding_utils.network.WitherBarMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerCloneMufflerDataEvent {

	@SuppressWarnings("unchecked")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onClonePlayer(PlayerEvent.Clone event) {

		if (!event.getEntityPlayer().worldObj.isRemote && event.getEntityPlayer() instanceof EntityPlayerMP && !(event.getEntityPlayer() instanceof FakePlayer)) {
			final EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();

			if (event.getOriginal().getEntityData().hasKey("turnOffWitherBossBar")) {
				final boolean witherBarShowing = event.getOriginal().getEntityData().getBoolean("turnOffWitherBossBar");
				player.getEntityData().setBoolean("turnOffWitherBossBar", witherBarShowing);
				player.getServer().addScheduledTask(new Runnable() {
					public void run() {
						MobGrindingUtils.NETWORK_WRAPPER.sendTo(new WitherBarMessage(witherBarShowing), player);
					}
				});
			}

			if (event.getOriginal().getEntityData().hasKey("turnOffDragonBossBar")) {
				final boolean dragonBarShowing = event.getOriginal().getEntityData().getBoolean("turnOffDragonBossBar");
				player.getEntityData().setBoolean("turnOffDragonBossBar", dragonBarShowing);
				player.getServer().addScheduledTask(new Runnable() {
					public void run() {
						MobGrindingUtils.NETWORK_WRAPPER.sendTo(new DragonBarMessage(dragonBarShowing), player);
					}
				});
			}
		}
	}
}
