package mob_grinding_utils.events;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.network.DragonBarMessage;
import mob_grinding_utils.network.WitherBarMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class MGUEntityPlayerSpawnedEvent {

	@SubscribeEvent
	public void onPlayerRespawned(PlayerRespawnEvent event) {

		if (!event.player.worldObj.isRemote && event.player instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) event.player;
			player.getServer().addScheduledTask(new Runnable() {
				public void run() {
					if (player.getEntityData().hasKey("turnOffWitherBossBar"))
						MobGrindingUtils.NETWORK_WRAPPER.sendTo(new WitherBarMessage(player.getEntityData().getBoolean("turnOffWitherBossBar")), player);

					if (player.getEntityData().hasKey("turnOffDragonBossBar"))
						MobGrindingUtils.NETWORK_WRAPPER.sendTo(new DragonBarMessage(player.getEntityData().getBoolean("turnOffDragonBossBar")), player);
				}
			});
		}
	}
}
