package mob_grinding_utils.events;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.network.DragonBarMessage;
import mob_grinding_utils.network.WitherBarMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerCloneMufflerDataEvent {

	@SuppressWarnings("unchecked")
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onClonePlayer(PlayerEvent.Clone event) {

		if (!event.getEntityPlayer().worldObj.isRemote && event.getEntityPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();

			player.getServer().addScheduledTask(new Runnable() {
				public void run() {
					if (event.getOriginal().getEntityData().hasKey("turnOffWitherBossBar")) {
						boolean witherBarShowing = event.getOriginal().getEntityData().getBoolean("turnOffWitherBossBar");
						player.getEntityData().setBoolean("turnOffWitherBossBar", witherBarShowing);
						MobGrindingUtils.NETWORK_WRAPPER.sendTo(new WitherBarMessage(witherBarShowing), player);
					}

					if (event.getOriginal().getEntityData().hasKey("turnOffDragonBossBar")) {
						boolean dragonBarShowing = event.getOriginal().getEntityData().getBoolean("turnOffDragonBossBar");
						player.getEntityData().setBoolean("turnOffDragonBossBar", dragonBarShowing);
						MobGrindingUtils.NETWORK_WRAPPER.sendTo(new DragonBarMessage(dragonBarShowing), player);
					}
				}
			});
		}
	}
}
