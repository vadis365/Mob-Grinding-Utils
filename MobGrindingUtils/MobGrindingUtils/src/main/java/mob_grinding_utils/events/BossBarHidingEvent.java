package mob_grinding_utils.events;

import mob_grinding_utils.capability.bossbars.BossBarPlayerCapability;
import mob_grinding_utils.capability.bossbars.IBossBarCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.BossInfo;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class BossBarHidingEvent {

	@SubscribeEvent
	public void onRenderHUD(BossInfo event) {
		if (event.getType().equals(RenderGameOverlayEvent.ElementType.BOSSINFO)) {
			PlayerEntity player = Minecraft.getInstance().player;

			if (player != null) {
				/*IBossBarCapability cap = player.getCapability(BossBarPlayerCapability.CAPABILITY_PLAYER_BOSS_BAR, null); //todo this has changed

				if (!cap.renderWitherBar()) {
					if (event.getBossInfo().getName().getUnformattedText().equals("Wither") || isWitherCrumbsBoss(player))
						event.setCanceled(true);
				}

				if (!cap.renderEnderDragonBar()) {
					if (event.getBossInfo().getName().getUnformattedText().equals("Ender Dragon"))
						event.setCanceled(true);
				}
				 */
			}
		}
	}

	// FFS This is Shit!
	public boolean isWitherCrumbsBoss(PlayerEntity player) {
		String name = null;
		/* //todo this seems to have changed to getloadedentitieswithinAABB
		for (int mobs = 0; mobs < player.getEntityWorld().getLoadedEntityList().size(); mobs++) {
			name = player.getEntityWorld().getLoadedEntityList().get(mobs).toString();
			if (name.startsWith("EntityHumanWither"))
				break;
		}
		 */
		return name.startsWith("EntityHumanWither");
	}
}
