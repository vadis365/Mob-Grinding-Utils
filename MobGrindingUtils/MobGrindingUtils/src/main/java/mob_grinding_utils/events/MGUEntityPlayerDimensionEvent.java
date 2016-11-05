package mob_grinding_utils.events;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.network.DragonBarMessage;
import mob_grinding_utils.network.WitherBarMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

public class MGUEntityPlayerDimensionEvent {

	@SubscribeEvent
	public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {

		if (!event.player.worldObj.isRemote && event.player instanceof EntityPlayerMP && !(event.player instanceof FakePlayer)) {
			final EntityPlayerMP player = (EntityPlayerMP) event.player;

			if (player.getEntityData().hasKey("turnOffWitherBossBar"))
				player.getServer().addScheduledTask(new Runnable() {
					public void run() {
						MobGrindingUtils.NETWORK_WRAPPER.sendTo(new WitherBarMessage(player.getEntityData().getBoolean("turnOffWitherBossBar")), player);
					}
				});

			if (event.player.getEntityData().hasKey("turnOffDragonBossBar"))
				player.getServer().addScheduledTask(new Runnable() {
					public void run() {
						MobGrindingUtils.NETWORK_WRAPPER.sendTo(new DragonBarMessage(player.getEntityData().getBoolean("turnOffDragonBossBar")), player);
					}
				});
		}
	}
}
