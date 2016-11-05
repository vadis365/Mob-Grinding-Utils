package mob_grinding_utils.events;

import mob_grinding_utils.MobGrindingUtils;
import mob_grinding_utils.network.DragonBarMessage;
import mob_grinding_utils.network.WitherBarMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MGUEntityJoinedWorldEvent {

	@SubscribeEvent
	public void onPlayerJoined(EntityJoinWorldEvent event) {

		if (!event.getEntity().worldObj.isRemote && event.getEntity() instanceof EntityPlayerMP && !(event.getEntity() instanceof FakePlayer)) {
			final EntityPlayerMP player = (EntityPlayerMP) event.getEntity();

			if (player.getEntityData().hasKey("turnOffWitherBossBar"))
				player.getServer().addScheduledTask(new Runnable() {
					public void run() {
						MobGrindingUtils.NETWORK_WRAPPER.sendTo(new WitherBarMessage(player.getEntityData().getBoolean("turnOffWitherBossBar")), player);
					}
				});

			if (player.getEntityData().hasKey("turnOffDragonBossBar"))
				player.getServer().addScheduledTask(new Runnable() {
					public void run() {
						MobGrindingUtils.NETWORK_WRAPPER.sendTo(new DragonBarMessage(player.getEntityData().getBoolean("turnOffDragonBossBar")), player);
					}
				});
		}
	}
}
