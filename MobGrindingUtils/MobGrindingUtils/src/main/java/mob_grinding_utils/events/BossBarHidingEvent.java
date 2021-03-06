package mob_grinding_utils.events;

import mob_grinding_utils.capability.bossbars.BossBarPlayerCapability;
import mob_grinding_utils.capability.bossbars.IBossBarCapability;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.BossInfo;

@SideOnly(Side.CLIENT)
public class BossBarHidingEvent {

	@SubscribeEvent
	public void onRenderHUD(BossInfo event) {
		if (event.getType().equals(RenderGameOverlayEvent.ElementType.BOSSINFO)) {
			EntityPlayer player = Minecraft.getMinecraft().player;

			if (player != null) {
				IBossBarCapability cap = player.getCapability(BossBarPlayerCapability.CAPABILITY_PLAYER_BOSS_BAR, null);

				if (!cap.renderWitherBar()) {
					if (event.getBossInfo().getName().getUnformattedText().equals("Wither") || isWitherCrumbsBoss(player))
						event.setCanceled(true);
				}

				if (!cap.renderEnderDragonBar()) {
					if (event.getBossInfo().getName().getUnformattedText().equals("Ender Dragon"))
						event.setCanceled(true);
				}
			}
		}
	}

	// FFS This is Shit!
	public boolean isWitherCrumbsBoss(EntityPlayer player) {
		String name = null;
		for (int mobs = 0; mobs < player.getEntityWorld().getLoadedEntityList().size(); mobs++) {
			name = player.getEntityWorld().getLoadedEntityList().get(mobs).toString();
			if (name.startsWith("EntityHumanWither"))
				break;
		}
		return name.startsWith("EntityHumanWither");
	}
}
